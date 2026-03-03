// File: chainedHash.java
// Location: 14practical/chainedHash.java

import java.util.*;

public class chainedHash {
    
    // Node class for linked list
    private static class Node {
        String key;
        String value;
        Node next;
        
        Node(String key, String value) {
            this.key = key;
            this.value = value;
            this.next = null;
        }
    }
    
    private Node[] table;
    private int size;  // Number of entries
    private int m;     // Table size
    
    // Constructor
    public chainedHash(int m) {
        this.m = m;
        this.table = new Node[m + 1];  // 1-based indexing
        this.size = 0;
    }
    
    /**
     * Hash function (same as openHash for fair comparison)
     */
    private int hash(String key) {
        final int BASE = 31;
        long hash = 0;
        
        for (int i = 0; i < key.length(); i++) {
            hash = (hash * BASE + key.charAt(i)) % m;
        }
        
        return (int)(hash % m) + 1;
    }
    
    /**
     * Insert key-value pair
     */
    public void insert(String key, String value) {
        int index = hash(key);
        
        // If this slot is empty, create new list
        if (table[index] == null) {
            table[index] = new Node(key, value);
            size++;
            return;
        }
        
        // Search for key in chain
        Node current = table[index];
        Node prev = null;
        
        while (current != null) {
            if (current.key.equals(key)) {
                // Update existing key
                current.value = value;
                return;
            }
            prev = current;
            current = current.next;
        }
        
        // Key not found, add to end of list
        prev.next = new Node(key, value);
        size++;
    }
    
    /**
     * Look up value by key
     */
    public String lookup(String key) {
        int index = hash(key);
        
        Node current = table[index];
        while (current != null) {
            if (current.key.equals(key)) {
                return current.value;
            }
            current = current.next;
        }
        
        return null;  // Not found
    }
    
    /**
     * Delete a key-value pair
     */
    public boolean delete(String key) {
        int index = hash(key);
        
        Node current = table[index];
        Node prev = null;
        
        while (current != null) {
            if (current.key.equals(key)) {
                if (prev == null) {
                    // Removing first node
                    table[index] = current.next;
                } else {
                    prev.next = current.next;
                }
                size--;
                return true;
            }
            prev = current;
            current = current.next;
        }
        
        return false;  // Not found
    }
    
    /**
     * Check if key is in table
     */
    public boolean isInTable(String key) {
        return lookup(key) != null;
    }
    
    /**
     * Get current load factor (α = N/m)
     */
    public double getLoadFactor() {
        return (double)size / m;
    }
    
    /**
     * Get chain length statistics
     */
    public void printChainStats() {
        int minChain = Integer.MAX_VALUE;
        int maxChain = 0;
        int emptyChains = 0;
        double totalLength = 0;
        
        for (int i = 1; i <= m; i++) {
            int length = 0;
            Node current = table[i];
            while (current != null) {
                length++;
                current = current.next;
            }
            
            if (length == 0) {
                emptyChains++;
            } else {
                minChain = Math.min(minChain, length);
                maxChain = Math.max(maxChain, length);
                totalLength += length;
            }
        }
        
        double avgChain = totalLength / (m - emptyChains);
        
        System.out.println("Chain Statistics:");
        System.out.println("  Empty chains: " + emptyChains + "/" + m);
        System.out.println("  Min chain length: " + minChain);
        System.out.println("  Max chain length: " + maxChain);
        System.out.println("  Avg chain length (non-empty): " + 
                          String.format("%.2f", avgChain));
    }
}
