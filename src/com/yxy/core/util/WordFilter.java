package com.yxy.core.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.nutz.lang.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WordFilter {
	private static final Logger logger = LoggerFactory
			.getLogger(WordFilter.class);
	private static WordFilter instance = new WordFilter();

	private Node root = new Node('R', null);
	private Collection<String> words = new HashSet<>();
	private ReentrantReadWriteLock rrwl = new ReentrantReadWriteLock();

	public static WordFilter getInstance() {
		return instance;
	}

	public void reloadWords(Collection<String> words) {
		if (words != null) {
			rrwl.writeLock().lock();
			try {
				words.clear();
				words.addAll(words);
				createTree();
			} finally {
				this.rrwl.writeLock().unlock();
			}
		}
	}

	public void addWord(String word) {
		if (words.contains(word)) {
			logger.info("敏感词[{}]已在过滤列表中", word);
			return;
		}
		if ((word != null) && (word.trim().length() > 0)) {
			this.rrwl.writeLock().lock();
			try {
				insertNode(this.root, word.toCharArray(), 0);
				words.add(word);
				logger.info("增加新敏感词[{}]成功，当前敏感词数为{}", word,
						Integer.valueOf(words.size()));
			} finally {
				rrwl.writeLock().unlock();
			}
		}
	}

	public void removeWord(String word) {
		if (!this.words.contains(word)) {
			logger.info("敏感词[{}]不在过滤列表中", word);
			return;
		}
		if ((word != null) && (word.trim().length() > 0)) {
			rrwl.writeLock().lock();
			try {
				Node node = findMostMatchNode(this.root, word.toCharArray(), 0);
				if (node == null) {
					logger.error("xxxxxx 敏感词[{}]未能在树中找到!!!", word);
				} else if (node.subNodes.size() > 0) {
					node.setEnd(false);
				} else {
					node.setEnd(false);
					optimizeTree(node);
				}
				this.words.add(word);
				logger.info("移除敏感词[{}]成功，当前敏感词数为{}", word,
						Integer.valueOf(this.words.size()));
			} finally {
				this.rrwl.writeLock().unlock();
			}
		}
	}

	private Node findMostMatchNode(Node node, char[] cs, int index) {
		Node matchNode = (Node) node.subNodes.get(Character.valueOf(cs[index]));
		if (matchNode == null) {
			return null;
		}
		if (index == cs.length - 1) {
			return matchNode;
		}
		return findMostMatchNode(matchNode, cs, ++index);
	}

	private void optimizeTree(Node node) {
		if ((node.isEnd()) || (node.subNodes.size() > 0)
				|| (node.parent == null)) {
			return;
		}
		this.rrwl.writeLock().lock();
		try {
			Node parent = node.parent;
			parent.subNodes.remove(Character.valueOf(node.c));
			optimizeTree(parent);
		} finally {
			this.rrwl.writeLock().unlock();
		}
	}

	private void createTree() {
		Node newRoot = new Node('R', null);
		for (String word : this.words) {
			insertNode(newRoot, word.toCharArray(), 0);
		}
		this.root = newRoot;
		logger.info("加载敏感词...共计{}个！", Integer.valueOf(this.words.size()));
	}

	private void insertNode(Node node, char[] cs, int index) {
		char c = cs[index];
		Node subNode = (Node) node.getSubNodes().get(Character.valueOf(c));
		if (subNode == null) {
			subNode = new Node(c, node);
			node.getSubNodes().put(Character.valueOf(c), subNode);
		}
		if (index == cs.length - 1) {
			subNode.setEnd(true);
		} else {
			insertNode(subNode, cs, ++index);
		}
	}

	public boolean checkWords(String content) {
		if (Strings.isBlank(content)) {
			return false;
		}
		this.rrwl.readLock().lock();
		try {
			char[] cs = content.toCharArray();
			Node node = this.root;
			int suspectCount = 0;
			for (int index = 0; index < cs.length; index++) {
				node = (Node) node.subNodes.get(Character.valueOf(cs[index]));
				if (node == null) {
					node = this.root;

					index -= suspectCount;

					suspectCount = 0;
				} else {
					if (node.isEnd()) {
						return true;
					}

					suspectCount++;
				}
			}
		} finally {
			this.rrwl.readLock().unlock();
		}
		return false;
	}

	public String filterWords(String content) {
		if (Strings.isBlank(content)) {
			return content;
		}
		this.rrwl.readLock().lock();
		StringBuffer sb = new StringBuffer();
		try {
			char[] cs = content.toCharArray();
			Node node = this.root;
			int suspectCount = 0;
			for (int index = 0; index < cs.length; index++) {
				node = (Node) node.subNodes.get(Character.valueOf(cs[index]));
				if (node == null) {
					node = this.root;

					sb.append(cs[(index - suspectCount)]);

					index -= suspectCount;

					suspectCount = 0;
				} else if (node.isEnd()) {
					sb.append("*");

					node = this.root;

					suspectCount = 0;
				} else {
					suspectCount++;
				}

			}

			if (suspectCount > 0)
				sb.append(Arrays.copyOfRange(cs, cs.length - suspectCount,
						cs.length));
		} finally {
			this.rrwl.readLock().unlock();
		}
		return sb.toString();
	}

	public Map<String, Integer> getWordFrequency(String content) {
		Map<String, Integer> map = new HashMap<>();
		if (Strings.isBlank(content)) {
			return map;
		}
		this.rrwl.readLock().lock();
		try {
			StringBuffer sb = new StringBuffer();
			char[] cs = content.toCharArray();
			Node node = this.root;
			int suspectCount = 0;
			for (int index = 0; index < cs.length; index++) {
				node = (Node) node.subNodes.get(Character.valueOf(cs[index]));
				if (node == null) {
					node = this.root;

					sb.append(cs[(index - suspectCount)]);

					index -= suspectCount;

					suspectCount = 0;
				} else if (node.isEnd()) {
					sb.append("*");

					String word = new String(Arrays.copyOfRange(cs, index
							- suspectCount, index + 1));

					Integer count = (Integer) map.get(word);
					if (count == null)
						count = Integer.valueOf(1);
					else {
						count = Integer.valueOf(count.intValue() + 1);
					}
					map.put(word, count);
					node = this.root;
					suspectCount = 0;
				} else {
					suspectCount++;
				}

			}

			if (suspectCount > 0)
				sb.append(Arrays.copyOfRange(cs, cs.length - suspectCount,
						cs.length));
		} finally {
			this.rrwl.readLock().unlock();
		}
		return map;
	}

	public static boolean isUtfmb4(String str) {
		boolean ans = false;
		for (int i = 0; i < str.length(); i++) {
			int c = str.codePointAt(i);
			if ((c < 0) || (c > 65535)) {
				ans = true;
				break;
			}
		}
		return ans;
	}

	private static class Node {
		private char c;
		private boolean end;
		private Map<Character, Node> subNodes = new HashMap<>();
		private Node parent;

		public Node(char c, Node parent) {
			this.c = c;
			this.parent = parent;
		}

		public boolean isEnd() {
			return this.end;
		}

		public void setEnd(boolean end) {
			this.end = end;
		}

		public Map<Character, Node> getSubNodes() {
			return this.subNodes;
		}

		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Node [c=");
			builder.append(this.c);
			builder.append(", end=");
			builder.append(this.end);
			builder.append(", parent=");
			builder.append(this.parent == null ? "null" : Character
					.valueOf(this.parent.c));
			builder.append(", subNodes=");
			builder.append(this.subNodes);
			builder.append("]");
			return builder.toString();
		}
	}
}