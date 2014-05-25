package jparser;

import java.io.IOException;
import java.util.List;

public class Sample {

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		JParser tmp = new JParser();
		tmp.parseDir("./data/");
		List<MethodNode> methods = tmp.getMethodNodes();

		for (MethodNode x : methods) {
			System.out.println(x.getFilename() + " " 
					+ x.getClassname() + " "
					+ x.getReturnType() + " "
					+ x.getNameParts() + " " 
					+ x.getParametersType() + " "
					+ x.getBlockCount());
		}
	}
}
