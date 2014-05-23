package jparser;

import java.util.ArrayList;
import java.util.List;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;

public class MethodNode {
	private MethodTree node;
	private String filename, classname;
	
	public MethodNode(MethodTree _node) {
		node = _node;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getClassname() {
		return classname;
	}
	
	public String getReturnType() {
		return node.getReturnType().toString();
	}
	
	public String getName() {
		return node.getName().toString();
	}
	
	public List<String> getNameParts() {
		List<String> ret = new ArrayList<String>();
		
		String name = getName();
		int i = 1;
		String tmp = "" + name.charAt(0);
		while(i < name.length()) {
			if(isUppercase(name.charAt(i))) {
				if(isUppercase(tmp.charAt(tmp.length()-1))
						&& (i == name.length() -1 
						|| isUppercase(name.charAt(i+1)))) {
					tmp += name.charAt(i);
				} else {
					ret.add(tmp);
					tmp = "" + name.charAt(i);
				}
			}else {
				tmp += name.charAt(i);
			}
			i++;
		}
		if (!tmp.isEmpty()) {
			ret.add(tmp);
		}
		
		return ret;
	}
	
	public List<String> getParameters() {
		List<String> ret = new ArrayList<String>();
		for(VariableTree x : node.getParameters()) {
			ret.add(x.toString());
		}
		
		return ret;
	}
	
	public List<String> getParametersType() {
		List<String> ret = new ArrayList<String>();
		for(VariableTree x : node.getParameters()) {
			ret.add(x.getType().toString());
		}
		
		return ret;
	}
	
	public List<String> getParametersName() {
		List<String> ret = new ArrayList<String>();
		for(VariableTree x : node.getParameters()) {
			ret.add(x.getName().toString());
		}
		
		return ret;
	}
	
	public int getBlockCount() {
		int num = 0;
		String str = node.getBody().toString();
		for(int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '{') {
				num++;
			}
		}
		
		return num;
	}
	
	public int getStatementCount() {
		int num = 0;
		String str = node.getBody().toString();
		for(int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == ';') {
				num++;
			}
		}
		
		return num;
	}
	
	private Boolean isUppercase(char c) {
		return c >= 'A' && c <= 'Z';
	}
}
