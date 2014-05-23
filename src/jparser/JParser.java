package jparser;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.sun.source.tree.MethodTree;
import com.sun.source.util.TreeScanner;
import com.sun.tools.javac.file.JavacFileManager;
import com.sun.tools.javac.parser.Parser;
import com.sun.tools.javac.parser.ParserFactory;
import com.sun.tools.javac.tree.JCTree.JCCompilationUnit;
import com.sun.tools.javac.util.Context;

public class JParser {
	private ParserFactory factory;
	class UnitFilePair {
		public JCCompilationUnit unit;
		public String filename;
		public UnitFilePair(JCCompilationUnit _unit, String _filename){
			unit = _unit;
			filename = _filename;
		}
	}
	private List<UnitFilePair> units;
	
	public JParser() {
		Context context = new Context();
		JavacFileManager.preRegister(context);
		factory = ParserFactory.instance(context);		
		
		units = new ArrayList<UnitFilePair>();
	}

	public void parse(String filename) throws IOException {
		String f = processConstructor(readFile(filename).toString());
		Parser parser = factory.newParser(f, true, false, true);
		units.add(new UnitFilePair(parser.parseCompilationUnit(), filename));
	}
	
	public void parseDir(String d) throws IOException {
		File dir = new File(d);
		
		if (!dir.exists()) {
			return;
		}
		if (dir.isFile()) {
			parse(dir.getPath());
			return;
		}
		
		for(File x : dir.listFiles()){
			parseDir(x.getPath());
		}
	}

	public List<MethodNode> getMethodNodes() {
		List<MethodNode> ret = new ArrayList<MethodNode>();
		MethodScanner scanner = new MethodScanner();
//		ret = scanner.visitCompilationUnit(unit, new ArrayList<MethodNode>());
//		scanner.visitCompilationUnit(unit, ret);
		
		for(UnitFilePair x : units) {
			int i = ret.size();
			scanner.visitCompilationUnit(x.unit, ret);
			while(i<ret.size()) {
				ret.get(i++).filename = x.filename;
			}
		}
		
		return ret;
	}

	
	private class MethodScanner extends TreeScanner<List<MethodNode>, List<MethodNode>> {
		@Override
		public List<MethodNode> visitMethod(MethodTree node, List<MethodNode> p) {
			p.add(new MethodNode(node));
			return p;
		}
	}
	
	private CharSequence readFile(String file) throws IOException {
		FileInputStream fin = new FileInputStream(file);
		FileChannel ch = fin.getChannel();
		ByteBuffer buffer = ch.map(MapMode.READ_ONLY, 0, ch.size());
		fin.close();
		return Charset.defaultCharset().decode(buffer);
	}
	
	private String processConstructor(String f) {
		String ret = f;
		List<String> cs = getClassList(f);
		
		for(String x : cs) {
			Pattern pattern = Pattern.compile("\\b"+x+"\\s*\\(.*\\)\\s*\\{");	
			Matcher matcher = pattern.matcher(ret);
			while(matcher.find()){
				String tmp = ret.substring(matcher.start(), matcher.end());
				ret = matcher.replaceAll("Constructor " + tmp);
			}
		}
		
		return ret;
	}
	
	private List<String> getClassList(String f) {
		List<String> ret = new ArrayList<String>();
		
		Pattern pattern = Pattern.compile("\\bclass\\b.*\\s*\\{");
		Matcher matcher = pattern.matcher(f);
		while(matcher.find()) {
			String tmp = f.substring(matcher.start(), matcher.end());
			int i = tmp.indexOf("class");
			i += 6;
			int end = tmp.indexOf(" ", i);
			ret.add(tmp.substring(i, end));
		}
		
		return ret;
	}
}
