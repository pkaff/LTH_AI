import java.util.ArrayList;


public class mainTest {

	public static void main(String[] args) {
		ARFFReader reader = new ARFFReader();
		String example = reader.scan();
		ArrayList<Example> examples = new ArrayList<Example>();
		while (example != null) {
			examples.add(new RestaurantExample(example, reader.getAttributes()));
			example = reader.scan();
		}
		Algorithm a = new Algorithm();
		ArrayList<Integer> l = new ArrayList<Integer>();
		for (int i = 0; i < 10; ++i) {
			l.add(i);
		}
		Tree1<Integer> t = a.decisionTreeLearning(examples, l, new ArrayList<Example>());
		t.print();
	}

}
