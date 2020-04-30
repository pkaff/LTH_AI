import java.util.ArrayList;

public class Tree1<V> {
	private Node<V> root;
	
	public Tree1(V value) {
		root = new Node<V>();
		root.value = value;
		root.children = new ArrayList<Node<V>>();
	}
	
	public void add(Tree1<V> subTree) {
		root.children.add(subTree.root);
	}
	
	public void print() {
		System.out.println("Tree:");
		printTree(root, 0);
	}
	
	private void printTree(Node<V> n, int depth) {
		for (Node<V> t : n.children) {
			for (int k = 0; k < depth; ++k) {
				System.out.print("  ");
			}
			if(t.children.isEmpty()) {
				if (t.value instanceof Integer && (int)t.value == 1)
					System.out.println("Attribute" + n.value + " = value" + n.children.indexOf(t) + " : yes");
				if (t.value instanceof Integer && (int)t.value == 0)
					System.out.println("Attribute" + n.value + " = value" + n.children.indexOf(t) + " : no");
			} else {
				System.out.println("Attribute" + n.value + " = value" + n.children.indexOf(t));
				printTree(t, depth + 1);
			}
		}
	}

	
	private static class Node<V> {
		private V value;
		private ArrayList<Node<V>> children;
	}
}