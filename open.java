// File: openHash.java
// Location: 14practical/openHash.java

import java.util.*;

public class openHash {
    
    // Table entry class
    private static class Entry {
        String key;
        String value;
        boolean isActive;  // false = deleted/tombstone
        
        Entry(String key, String value) {
            this.key = key;
            this.value = value;
            this.isActive = true;
        }
    }
    
    private Entry[] table;
    private int size;  // Number of active entries
    private int m;     // Table size
    
    // Constructor
    public openHash(int m) {
        this.m = m;
        this.table = new Entry[m + 1];  // Using 1-based indexing
        this.size = 0;
    }
    
    /**
     * Hash function: converts string key to index in [1, m]
     * Using polynomial hash code with Horner's method
     */
    private int hash(String key) {
        final int BASE = 31;  // Common prime base for string hashing
        long hash = 0;
        
        for (int i = 0; i < key.length(); i++) {
            hash = (hash * BASE + key.charAt(i)) % m;
        }
        
        // Ensure result is in [1, m] (not 0)
        return (int)(hash % m) + 1;
    }
    
    /**
     * Linear probing for collision resolution
     * Returns next probe index
     */
    private int nextProbe(int current, int attempt) {
        // Linear probing: h(k,i) = (h(k) + i) mod m
        return ((current - 1 + attempt) % m) + 1;
    }
    
    /**
     * Insert key-value pair
     */
    public void insert(String key, String value) {
        int index = hash(key);
        int attempt = 0;
        
        while (attempt < m) {
            int probe = nextProbe(index, attempt);
            
            // Empty slot found
            if (table[probe] == null) {
                table[probe] = new Entry(key, value);
                size++;
                return;
            }
            
            // Update if key already exists and is active
            if (table[probe].isActive && table[probe].key.equals(key)) {
                table[probe].value = value;
                return;
            }
            
            // Tombstone found - can reuse
            if (!table[probe].isActive) {
                table[probe] = new Entry(key, value);
                size++;
                return;
            }
            
            attempt++;
        }
        
        // Table is full
        System.out.println("Warning: Hash table is full, could not insert key: " + key);
    }
    
    /**
     * Look up value by key
     */
    public String lookup(String key) {
        int index = hash(key);
        int attempt = 0;
        
        while (attempt < m) {
            int probe = nextProbe(index, attempt);
            
            // Empty slot means key not present
            if (table[probe] == null) {
                return null;
            }
            
            // Found active entry with matching key
            if (table[probe].isActive && table[probe].key.equals(key)) {
                return table[probe].value;
            }
            
            attempt++;
        }
        
        return null;  // Not found after probing entire table
    }
    
    /**
     * Delete a key-value pair
     */
    public boolean delete(String key) {
        int index = hash(key);
        int attempt = 0;
        
        while (attempt < m) {
            int probe = nextProbe(index, attempt);
            
            if (table[probe] == null) {
                return false;  // Not found
            }
            
            if (table[probe].isActive && table[probe].key.equals(key)) {
                table[probe].isActive = false;  // Mark as deleted (tombstone)
                size--;
                return true;
            }
            
            attempt++;
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
     * Get current load factor
     */
    public double getLoadFactor() {
        return (double)size / m;
    }
}
