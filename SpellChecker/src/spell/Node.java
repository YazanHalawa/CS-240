package spell;

import spell.ITrie.INode;

public class Node implements INode {
	private Node[] nodes;
	private int value;
	public Node() {
		nodes = new Node[26];
		value = 0;
	}

	public int add(String word){
		int tempCount = 0;
		if (nodes[word.charAt(0)-'a'] == null){
			nodes[word.charAt(0)-'a'] = new Node();
			tempCount++;
		}
		Node myNode = nodes[word.charAt(0)-'a'];
		if (word.length() == 1)
			myNode.value++;
		else{
			tempCount += myNode.add(word.substring(1, word.length()));
		}
		return tempCount;
	}
	
	public INode find(String word){
		if (word.length()==0)
			return null;
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
	
	@Override 
	public boolean equals(Object o){
		if (o == null)
			return false;
		if (this == o)
			return true;
		if (this.getClass() != o.getClass())
			return false;
		Node tmp = (Node)o;
		if (tmp.value != this.value)
			return false;
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
