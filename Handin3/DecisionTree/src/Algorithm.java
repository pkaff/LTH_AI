import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Algorithm {
	
	public Algorithm() {
		
	}
	
	public Tree1<Integer> decisionTreeLearning(List<Example> examples, List<Integer> attributes, List<Example> parents) {
		if (examples.isEmpty()) {
			return pluralityValue(parents);
		} else if (sameClassification(examples)) {
			return new Tree1<Integer>(examples.get(0).result ? 1 : 0);
		} else if (attributes.isEmpty()) {
			return pluralityValue(examples);
		} else {
			int mostImportant = attributes.get(0);
			double maxImportance = gain(attributes.get(0), examples);
			for (int i : attributes) {
				double g = gain(i, examples);
				if (g > maxImportance) {
					mostImportant = i;
					maxImportance = g;
				}
			}
			
			List<Integer> diffValues = diffValues(mostImportant, examples);
			Tree1<Integer> tree = new Tree1<Integer>(mostImportant);
			for (int i : diffValues) {
				List<Example> exs = new ArrayList<Example>();
				for (Example ex : examples) {
					if (ex.getValue(mostImportant) == i) {
						exs.add(ex);
					}
				}
				attributes.remove(new Integer(mostImportant));
				Tree1<Integer> subTree = decisionTreeLearning(exs, attributes, examples);
				tree.add(subTree);
			}
			return tree;
		}
	}
	
	private Tree1<Integer> pluralityValue(List<Example> examples) {
		int p = 0;
		for (Example ex : examples) {
			if(ex.result) {
				++p;
			} else {
				--p;
			}
		}
		if (p < 0) {
			return new Tree1<Integer>(0);
		} else if(p > 0) {
			return new Tree1<Integer>(1);
		} else {
			Random rng = new Random();
			return new Tree1<Integer>(rng.nextInt(2));
		}
	}
	
	private boolean sameClassification(List<Example> examples) {
		for (Example ex : examples) {
			if (ex.result != examples.get(0).result) {
				return false;
			}
		}
		return true;
	}
	
	private double gain(int index, List<Example> examples) {
		ArrayList<Integer> diffValues = diffValues(index, examples);
		double p = 0;
		double n = 0;
		double[] pk = new double[diffValues.size()];
		double[] nk = new double[diffValues.size()];
		
		//Calculate number of positive and negative results for different values
		int count = 0;
		for (int i : diffValues) {
			for (Example ex : examples) {
				if (ex.getValue(index) == i) {
					if (ex.result) {
						++pk[count];
						++p;
					} else {
						++nk[count];
					}
				}
			}
			++count;
		}
		n = examples.size() - p;
		
		return B(p / (p + n)) - Remainder(index, diffValues.size(), p, n, pk, nk);
	}
	
	private ArrayList<Integer> diffValues(int index, List<Example> examples) {
		//Returns list of all different values from initial examples, given a certain attribute (index)
		ArrayList<Integer> diffValues = new ArrayList<Integer>();
		for (Example ex : examples) {
			if (!diffValues.contains(ex.getValue(index))) {
				diffValues.add(ex.getValue(index));
			}
		}
		return diffValues;
	}
	
	private double B(double q) {
		if (q == 1 || q == 0) {
			return 0;
		}
		return -(q * log2(q) + (1 - q) * log2(1 - q));
	}
	
	private double log2(double arg) {
		return Math.log(arg)/Math.log(2);
	}
	
	private double Remainder(int index, int len, double p, double n, double[] pk, double[] nk) {
		double sum = 0;
		//p. 715 Stuart Russel, Peter Norvig
		for (int i = 0; i < len; ++i) {
			double b = B(pk[i] / (pk[i] + nk[i]));
			double a = ((pk[i] + nk[i]) / (p + n));
			sum +=  a * b;
		}
		
		return sum;
	}
}
