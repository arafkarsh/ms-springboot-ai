package io.fusion.air.microservice.utils.algos;

import io.fusion.air.microservice.utils.Std;

/**
 * @author: Araf Karsh Hamid
 * @version:
 * @date:
 */
public class Trie {
    private TrieNode root;

    public Trie() {
        root = new TrieNode();
    }

    // Insert a word into the Trie
    public void insert(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                node.children[c - 'a'] = new TrieNode();
            }
            node = node.children[c - 'a'];
        }
        node.isEndOfWord = true;
    }

    // Returns if the word is in the Trie
    public boolean search(String word) {
        TrieNode node = root;
        for (char c : word.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                return false;
            }
            node = node.children[c - 'a'];
        }
        return node.isEndOfWord;
    }

    // Returns if there is any word in the Trie that starts with the given prefix
    public boolean startsWith(String prefix) {
        TrieNode node = root;
        for (char c : prefix.toCharArray()) {
            if (node.children[c - 'a'] == null) {
                return false;
            }
            node = node.children[c - 'a'];
        }
        return true;
    }

    class TrieNode {
        private final TrieNode[] children;
        private boolean isEndOfWord;

        public TrieNode() {
            children = new TrieNode[26];
            isEndOfWord = false;
        }
    }

    public static final String APP = "app";
    public static final String APP2 = "app   = ";

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        Std.println("apple = "+trie.search("apple"));  // returns true
        Std.println(APP2+trie.search(APP));                     // returns false
        Std.println(APP2+trie.startsWith(APP));                // returns true
        trie.insert(APP);
        Std.println(APP2+trie.search(APP));                     // returns true
    }
}
