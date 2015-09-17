package spell;

import spell.ITrie.INode;

public class Node implements INode {
	Node[] nodes;
	int count;

	public Node() {
		nodes = new Node[26];
		count = 0;
	}

	@Override
	public int getValue() {
		return count;
	}

	public int add(String word){
		word = word.toLowerCase();
		int tempCount = 0;
		if (nodes[word.charAt(0)-'a'] == null){
			nodes[word.charAt(0)-'a'] = new Node();
			tempCount++;
		}
		if (word.length() == 1){
			nodes[word.charAt(0)-'a'].count++;
		}
		//if not at the end of the word, go down one more level
		else {
			String newWord = word.substring(1, word.length());
			tempCount += nodes[word.charAt(0)-'a'].add(newWord);
		}
		return tempCount;
	}

	public Node find(String word){
		word = word.toLowerCase();
		if (word.length() == 0)
			return null;
		Node myNode = nodes[word.charAt(0)-'a'];
		//if we reach the last letter in the word
		if (word.length() == 1){
			if (myNode != null && myNode.count != 0)
				return myNode;
			else
				return null;
		}
		//if the word does not exist in the tree
		if (myNode == null){
			return null;
		}
		//else go down one more level
		return myNode.find(word.substring(1, word.length()));
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
	 * @return the count
	 */
	public int getCount() {
		return count;
	}

	/**
	 * @param count the count to set
	 */
	public void setCount(int count) {
		this.count = count;
	}

	public boolean equals(Object o){
		if (o==null)//if the second object is null
			return false;
		if (this == o)//if they are the same object
			return true;
		if (this.getClass() != o.getClass())//if they are not the same type
			return false;
		Node temp = (Node) o;
		if (this.count != temp.count)//if they have different counts
			return false;
		//check the array of nodes
		for (int i = 0; i < 26; i++){
			if (this.nodes[i] != null && temp.nodes[i] != null)
				if (!this.nodes[i].equals(temp.nodes[i]))
					return false;
			else if (this.nodes[i] == null && temp.nodes[i] != null)
				return false;
			else if (this.nodes[i] != null && temp.nodes[i] == null)
				return false;
		}
		return true;	
	}
}
