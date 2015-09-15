package client.qualityChecker;

import client.qualityChecker.ITrie.INode;

/**
 * This class contains all the methods for managing nodes in the Trie
 * @author Yazan Halawa
 *
 */
public class Node implements INode {
	// Init the variables
	private Node[] nodes;
	private int value;
	
	/**
	 * The constructor for the Node class
	 */
	public Node() {
		nodes = new Node[26];
		value = 0;
	}

	/**
	 * This method adds a word by recursing through the nodes
	 * @param word
	 * @return
	 */
	public int add(String word){
		int tempCount = 0;
		
		// Create a new Node
		if (nodes[word.charAt(0)-'a'] == null){
			nodes[word.charAt(0)-'a'] = new Node();
			tempCount++;
		}
		
		// Recurse down
		Node myNode = nodes[word.charAt(0)-'a'];
		if (word.length() == 1)
			myNode.value++;
		else{
			tempCount += myNode.add(word.substring(1, word.length()));
		}
		return tempCount;
	}
	
	/**
	 * This method finds a word by recursing through the nodes
	 * @param word
	 * @return
	 */
	public INode find(String word){
		// If the word does not exist
		if (word.length()==0)
			return null;
		
		// Recurse Down
		Node myNode = nodes[word.charAt(0)-'a'];
		if (myNode == null)
			return null;
		if (word.length() == 1){
			if (myNode != null && myNode.value != 0)
				return myNode;
			else
				return null;
		}
		else{
			return myNode.find(word.substring(1, word.length()));
		}
	}
	
	/**
	 * This methods checks if two nodes are equals
	 */
	@Override 
	public boolean equals(Object o){
		// Check basic cases first
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (this.getClass() != o.getClass())
			return false;
		
		// Cast down the object
		Node tmp = (Node)o;
		if (tmp.value != this.value)
			return false;
		
		// Check all the nodes by recursion
		for (int i = 0; i < 26; i++){
			if (this.nodes[i] != null && tmp.nodes[i] != null)
				if (!this.nodes[i].equals(tmp.nodes[i]))
					return false;
			if (this.nodes[i] != null && tmp.nodes[i] == null)
				return false;
			if (this.nodes[i] == null && tmp.nodes[i] != null)
				return false;
			else continue;
		}
		return true;
	}
	/**
	 * @return the nodes
	 */
	public Node[] getNodes() {
		return nodes;
	}

	/**
	 * @param nodes the nodes to set
	 */
	public void setNodes(Node[] nodes) {
		this.nodes = nodes;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int getValue() {
		return value;
	}

}
