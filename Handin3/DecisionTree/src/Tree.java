import java.util.ArrayList;

public class Tree<K, V> {
	private Node<K, V> root;
	
	public Tree(K key, V value) {
		root = new Node<K, V>();
		root.key = key;
		root.value = value;
		root.children = new ArrayList<Node<K, V>>();
	}
	
	public void add(Tree<K, V> subTree) {
		root.children.add(subTree.root);
	}
	
	public void print() {
		System.out.println("Tree:");
		printTree(root);
	}
	
	private void printTree(Node<K, V> n) {
		System.out.print("Attribute ");
		if (n.children.isEmpty()) {
			if (n.value instanceof Integer && (int)n.value == 0) {
				System.out.println(n.key + " : " + "yes");
			} else {
				System.out.println(n.key + " : " + "no");
			}
		} else {
			System.out.println(n.key + " : " + n.value);
		}
		for (Node<K, V> t : n.children) {
			printTree(t);
		}
	}
	
	private static class Node<K, V> {
		private K key;
		private V value;
		private ArrayList<Node<K, V>> children;
	}
}


