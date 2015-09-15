package client.qualityChecker;

/**
 * This class contains all the methods for handling the Trie
 * @author Yazan Halawa
 *
 */
public class Trie implements ITrie {
	
	// Init variables
	private Node root;
	private int wordCount;
	private int nodeCount;
	
	/**
	 * The constructor for the Trie class
	 */
	public Trie() {
		// Set the variables
		root = new Node();
		wordCount = 0;
		nodeCount = 1;
	}
	/**
	 * @return the root
	 */
	public Node getRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(Node root) {
		this.root = root;
	}

	/**
	 * @param wordCount the wordCount to set
	 */
	public void setWordCount(int wordCount) {
		this.wordCount = wordCount;
	}

	/**
	 * @param nodeCount the nodeCount to set
	 */
	public void setNodeCount(int nodeCount) {
		this.nodeCount = nodeCount;
	}

	@Override
	public void add(String word) {
		nodeCount += root.add(word.toLowerCase());
		wordCount++;

	}

	@Override
	public INode find(String word) {
		return root.find(word.toLowerCase());
	}

	@Override
	public int getWordCount() {
		return wordCount;
	}

	@Override
	public int getNodeCount() {
		return nodeCount;
	}
	
	@Override
	public String toString(){
		StringBuilder myBuilder = new StringBuilder();
		StringBuilder tempBuilder = new StringBuilder();
		traverse(root, myBuilder, tempBuilder);
		return myBuilder.toString();
	}
	
	/**
	 * This method traverses the Trie by recursion
	 * @param root2
	 * @param myBuilder
	 * @param tempBuilder
	 */
	private void traverse(Node root2, StringBuilder myBuilder,
			StringBuilder tempBuilder) {
		if (root2.getValue() != 0)
			myBuilder.append(tempBuilder.toString() + "\n");
		char myChar = 'a';
		for (Node myNode: root2.getNodes()){
			if (myNode != null){
				tempBuilder.append(myChar);
				traverse(myNode, myBuilder, tempBuilder);
			}
			myChar++;
		}
		if (tempBuilder.length() != 0){
			tempBuilder.delete(tempBuilder.length()-1, tempBuilder.length());
		}
		
	}

	@Override
	public int hashCode(){
		return wordCount * nodeCount * 17;
	}
	
	/**
	 * This method checks whether two tries are quals
	 */
	@Override
	public boolean equals(Object o){
		// Check the basic cases
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (this.getClass() != o.getClass())
			return false;
		
		// Cast down
		Trie tmp = (Trie)o;
		if (!this.root.equals(tmp.root) || this.nodeCount != tmp.nodeCount || this.wordCount != tmp.wordCount)
			return false;
		return true;
	}

}
