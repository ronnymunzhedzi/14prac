// File: timeHashing.java (adapted from 13template.java)
// Location: 14practical/timeHashing.java

import java.lang.Math.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class time{
    
    // Data structures for key-value pairs
    static class KeyValue {
        String key;      // Key as String (for hashing)
        String value;    // Associated data
        
        KeyValue(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
    
    // Constants
    public static final int N = 1 << 20;  // 2^20 = 1,048,576
    public static final int USE_N = 950000;  // Use first 950,000
    
    public static KeyValue[] data;  // Array to hold all key-value pairs
    
    public static void main(String args[]) {
        // Generate test data
        generateTestData();
        
        DecimalFormat twoD = new DecimalFormat("0.00");
        DecimalFormat fourD = new DecimalFormat("0.0000");
        DecimalFormat fiveD = new DecimalFormat("0.00000");
        
        // Load factors to test: 75%, 80%, 85%, 90%, 95%
        double[] loadFactors = {0.75, 0.80, 0.85, 0.90, 0.95};
        
        System.out.println("\n\n\t\tHASHING PERFORMANCE COMPARISON");
        System.out.println("==========================================================");
        System.out.println("Data size: " + USE_N + " key-value pairs");
        System.out.println("Total generated: " + N + " pairs");
        System.out.println("==========================================================\n");
        
        // Table for results
        System.out.println("Load Factor | Open Hashing (ms) | Chained Hashing (ms)");
        System.out.println("------------|-------------------|---------------------");
        
        // Test each load factor
        for (double alpha : loadFactors) {
            // Calculate table size m for this load factor
            // For open hashing: m must be >= N, so m = ceil(USE_N/alpha)
            int m = (int)Math.ceil(USE_N / alpha);
            // Make m prime (or at least odd)
            if (m % 2 == 0) m++;
            
            System.out.print("    " + (int)(alpha*100) + "%      |");
            
            // Time open hashing
            double openTime = timeOpenHashing(m, alpha);
            System.out.print("      " + fiveD.format(openTime) + "       |");
            
            // Time chained hashing
            double chainedTime = timeChainedHashing(m, alpha);
            System.out.println("        " + fiveD.format(chainedTime));
        }
        
        System.out.println("==========================================================");
    }
    
    /**
     * Generate test data: 2^20 key-value pairs
     * Keys: 1 to N shuffled
     * Values: String representation of the number
     */
    static void generateTestData() {
        System.out.println("Generating " + N + " key-value pairs...");
        
        // Create array of keys 1..N
        Integer[] keys = new Integer[N];
        for (int i = 0; i < N; i++) {
            keys[i] = i + 1;
        }
        
        // Shuffle the keys
        Random rand = new Random(42);  // Fixed seed for reproducibility
        for (int i = N-1; i > 0; i--) {
            int j = rand.nextInt(i+1);
            Integer temp = keys[i];
            keys[i] = keys[j];
            keys[j] = temp;
        }
        
        // Create key-value pairs
        data = new KeyValue[N];
        for (int i = 0; i < N; i++) {
            String keyStr = String.valueOf(keys[i]);
            String valueStr = String.valueOf(i + 1);  // Original position as value
            data[i] = new KeyValue(keyStr, valueStr);
        }
        
        System.out.println("Data generation complete. First 950,000 will be used.\n");
    }
    
    /**
     * Time open hashing implementation
     */
    static double timeOpenHashing(int m, double alpha) {
        int repetitions = 30;
        double totalTime = 0;
        
        for (int rep = 0; rep < repetitions; rep++) {
            // Create new open hash table
            openHash hashTable = new openHash(m);
            
            // Insert first USE_N items
            long start = System.nanoTime();
            for (int i = 0; i < USE_N; i++) {
                hashTable.insert(data[i].key, data[i].value);
            }
            long end = System.nanoTime();
            
            totalTime += (end - start) / 1_000_000.0;  // Convert to ms
        }
        
        return totalTime / repetitions;
    }
    
    /**
     * Time chained hashing implementation
     */
    static double timeChainedHashing(int m, double alpha) {
        int repetitions = 30;
        double totalTime = 0;
        
        for (int rep = 0; rep < repetitions; rep++) {
            // Create new chained hash table
            chainedHash hashTable = new chainedHash(m);
            
            // Insert first USE_N items
            long start = System.nanoTime();
            for (int i = 0; i < USE_N; i++) {
                hashTable.insert(data[i].key, data[i].value);
            }
            long end = System.nanoTime();
            
            totalTime += (end - start) / 1_000_000.0;  // Convert to ms
        }
        
        return totalTime / repetitions;
    }
}
