package client.qualityChecker;

import java.util.Comparator;

public class myComp implements Comparator<String> {

	private Trie myTrie;
	
	public myComp(Trie myTrie) {
		this.myTrie = myTrie;
	}

	@Override
	public int compare(String o1, String o2) {
		Node first = (Node) myTrie.find(o1);
		Node second = (Node) myTrie.find(o2);
		if (first.getValue() < second.getValue())
			return 1;
		else
			return -1;
	}

}
