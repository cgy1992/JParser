package jparser;

import java.util.ArrayList;
import java.util.List;

import com.sun.source.tree.MethodTree;
import com.sun.source.tree.VariableTree;

public class MethodNode {
	private MethodTree node;
	public String name, filename, classname;
	
	public MethodNode(MethodTree _node) {
		node = _node;
		name = null;
		filename = null;
		classname = null;
	}
	
	public String getFilename() {
		return filename;
	}
	
	public String getClassname() {
		return classname;
	}
	
	public String getReturnType() {
		if(node.getReturnType() == null)
			return "Constructor";
		else
			return node.getReturnType().toString();
	}
	
	public String getName() {
		return name;
	}
	
	public String getBody() {
		if(node.getBody() != null )
			return node.getBody().toString();
		else
			return null;
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
		String str = getBody();
		if (str == null)
			return 0;
		
		for(int i = 0; i < str.length(); i++) {
			if (str.charAt(i) == '{') {
				num++;
			}
		}
		
		return num;
	}
	
	public int getStatementCount() {
		int num = 0;
		String str = getBody();
		if(str == null) 
			return 0;
		
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
