import java.util.ArrayList;

public class RestaurantExample extends Example {
	
	public RestaurantExample(String str, ArrayList<Object> types) {
		super(str, types);
	}
	
	public int cast(String str) {
		String s = str.toLowerCase();
		if (s.equals("french") || s.equals("none")|| s.equals("low")|| s.equals("no")){
			return 0;
		}
		if (s.equals("italian")|| s.equals("some")|| s.equals("mid")|| s.equals("yes")){
			return 1;
		}
		if (s.equals("thai")|| s.equals("full")|| s.equals("long")){
			return 2;
		}
		if (s.equals("burger")|| s.equals("very long")) {
			return 3;
		}
		return -2;
	}
}

