import java.util.ArrayList;

public class Example {	
	
	private ArrayList<Integer> attributes;
	public boolean result;
	
	public Example(String str, ArrayList<Object> types) {
		attributes = new ArrayList<Integer>();
		//Remove whitespace
		str = str.replaceAll(" ", "");
		//Split at ','
		String[] splits = str.split(",");
		if(splits[splits.length - 1].toLowerCase().equals("true") || splits[splits.length - 1].toLowerCase().equals("yes") || splits[splits.length - 1].toLowerCase().equals("1")) {
			result = true;
		} else {
			result = false;
		}
		for (int k = 0; k < splits.length - 1; ++k) {
			if (types.get(k) instanceof Integer && (int)types.get(k) == ARFFReader.NUMERIC) {
				attributes.add(Integer.parseInt(splits[k]));
			} else {
				attributes.add(cast(splits[k]));
			}
		}		
	}
	
	public int cast(String s) {
		String str = s.toLowerCase();
		if(str.equals("yes") || str.equals("y") || str.equals("true")) {
			return 1;
		} else {
			if(str.equals("no") || str.equals("n") || str.equals("false") || str.equals("none")) {
				return 0;
			}
		}
		return -1;
	}
	
	public int getValue(int index) {
		return attributes.get(index);
	}
	
}
