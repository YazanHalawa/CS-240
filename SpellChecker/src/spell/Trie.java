package spell;

public class Trie implements ITrie {
	Node root;
	int wordCount;
	int nodeCount;

	public Trie() {
		root = new Node();
		wordCount = 0;
		nodeCount = 1;
	}

	@Override
	public void add(String word) {
		nodeCount += root.add(word);
		wordCount++;
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
	public INode find(String word) {
		return root.find(word);
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
	public String toString() {
		StringBuilder myBuilder = new StringBuilder();
		StringBuilder tempBuilder = new StringBuilder();
		traverse(root, tempBuilder, myBuilder);
		return myBuilder.toString();
	}

	@Override
	public int hashCode(){
		final int RANDOM_MULTIPLIER = 17;
		return wordCount * nodeCount * RANDOM_MULTIPLIER;
	}

	@Override
	public boolean equals(Object o){
		if (o==null)
			return false;
		if (this == o)
			return true;
		if (this.getClass() != o.getClass())
			return false;
		Trie temp = (Trie) o;
		if (!this.root.equals(temp.root) || this.wordCount != temp.wordCount ||
				this.nodeCount != temp.nodeCount)
			return false;
		return true;
	}
	
	private void traverse(Node myNode, StringBuilder tempBuilder, StringBuilder myBuilder){
		if (myNode.count != 0){
			myBuilder.append(tempBuilder.toString());
			myBuilder.append("\n");
		}
		char myChar = 'a';
		for (Node temp: myNode.getNodes()){
			if (temp!=null){
				tempBuilder.append(myChar);
				traverse(temp, tempBuilder, myBuilder);
				myChar++;
			}
		}
		if (tempBuilder.length() != 0)
			tempBuilder.delete(tempBuilder.length()-1, tempBuilder.length());
	}
}
