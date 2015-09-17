package spell;

public class Trie implements ITrie {
	private Node root;
	private int wordCount;
	private int nodeCount;
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

	public Trie() {
		root = new Node();
		wordCount = 0;
		nodeCount = 1;
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
	
	@Override
	public boolean equals(Object o){
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (this.getClass() != o.getClass())
			return false;
		Trie tmp = (Trie)o;
		if (!this.root.equals(tmp.root) || this.nodeCount != tmp.nodeCount || this.wordCount != tmp.wordCount)
			return false;
		return true;
	}

}
