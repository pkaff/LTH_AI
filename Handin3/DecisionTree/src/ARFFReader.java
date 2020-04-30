import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;


public class ARFFReader {

	private Scanner s;
	private String relationName;
	public static final int NUMERIC = 0;
	public static final int STRING = 1;
	private ArrayList<Object> attributes;
	
	public ARFFReader() {
		try {
			s = new Scanner(new File("input.arff"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		attributes = new ArrayList<Object>();
		String line;
		while (s.hasNextLine()) {
			line = s.nextLine();
			//Comment
			if (line.charAt(0) == '%') {
				continue;
			}
			//Relation declaration
			if (line.startsWith("@RELATION")) {
				relationName = line.substring(10, line.length() - 1);
			}
			//Attribute declaration
			if (line.startsWith("@ATTRIBUTE")) {
				if (line.endsWith("NUMERIC")) {
					attributes.add(NUMERIC);
				}
				if (line.endsWith("STRING")) {
					attributes.add(STRING);
				}
				if (line.endsWith("}")) {
					ArrayList<String> list = new ArrayList<String>();
					int start = line.indexOf('{') + 1;
					int end = line.indexOf('}') - 1;
					String substr = line.substring(start, end);
					while (substr.contains(",")) {
						int commaIndex = substr.indexOf(',');
						list.add(substr.substring(0, commaIndex));
						substr = substr.substring(commaIndex + 1, end);
					}
					//Add last string
					list.add(substr);
					attributes.add(list);
				}
			}
			//Data declaration
			if (line.startsWith("@DATA")) {
				break;
			}
		}
	}
	
	public ArrayList<Object> getAttributes() {
		return attributes;
	}
	
	public String getRelationName() {
		return relationName;
	}
	
	public String scan() {
		if (!s.hasNextLine()) {
			return null;
		}
		return s.nextLine();
	}
}
