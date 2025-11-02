package org.fast.sort;

import java.util.Arrays;
import java.util.Random;

/**
 * Sorts an array of unique integers using a BitSet-based approach.
 *
 * <p>This implementation assumes that the input integers are unique.
 * It works by marking the presence of each integer in a BitSet,
 * and then iterating over the BitSet to generate a sorted array.</p>
 *
 * <p>Features:
 * <ul>
 *   <li>Time complexity: O(n + k), where n is the number of elements and k is the range of integers.</li>
 *   <li>Memory consumation: O(n) + 512MB for BitSet.</li>
 *   <li>No comparisons between integers; purely uses bit operations.</li>
 *   <li>Automatically removes duplicates if present.</li>
 * </ul>
 * </p>
 *
 * <p>Limitations:
 * <ul>
 *   <li>Very large integer ranges may require excessive memory.</li>
 *   <li>Designed for unique integers; duplicates are ignored.</li>
 * </ul>
 * </p>
 *
 * Usage example:
 * <pre>{@code
 * int[] input = {5, 2, 7, -7, 3};
 * int[] sorted = (new UniqueIntegerBitSetSort).sort(input);
 * // sorted: [-7, 2, 3, 5, 7]
 * }</pre>
 *
 * @author Nenad Vico
 * @since 1.0
 */
public class UniqueIntegerBitSetSort {

    private static final int SIZE = 67108864;
    private static final int SHIFT = 6;
    private final long[] bitSet = new long[SIZE];
    private boolean needToInit;
    private int noDuplicates = 0;

    /**
     * Sorts an array of integers.
     * @param array array of integers.
     * @return sorted array of unique integers.
     */
    public int[] sort(int[] array) {
        if (needToInit) {
            init();
        }
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        needToInit = true;
        for (int in : array) {
            if (in < min) {
                min = in;
            }
            if (in > max) {
                max = in;
            }
            int index = in >>> SHIFT;
            long lo = 0x1L << (in & 0x3f);
            long value = bitSet[index];
            if ((value & lo) != 0) {
                noDuplicates++;
            }
            bitSet[index] = value | lo;
            bitSet[in >>> SHIFT] |= (0x1L << (in & 0x3f));;
        }
        int[] result = new int[array.length - noDuplicates];
        int arrIndex = 0;
        if (min < 0) {
            int upper = SIZE;
            if (max < 0) {
                upper = max >>> SHIFT;
            }
            for(int index = (min >>> SHIFT); index < upper; index++) {
                long value = bitSet[index];
                if (value != 0) {
                    int hi = index << SHIFT;
                    long lo = 0x1;
                    for (int pos = 0; pos < 64; pos++) {
                        if ((value & lo) != 0) {
                            int number = hi + pos;
                            result[arrIndex++] = number;
                        }
                        lo <<= 1;
                    }
                }
            }
        }
        if (max > 0) {
            int lower = 0;
            if (min > 0) {
                lower = min >>> SHIFT;
            }
            for (int index = lower; index <= (max >>> SHIFT); index++) {
                long value = bitSet[index];
                if (value != 0) {
                    int hi = index << SHIFT;
                    long lo = 0x1;
                    for (int pos = 0; pos < 64; pos++) {
                        if ((value & lo) != 0) {
                            int number = hi + pos;
                            result[arrIndex++] = number;
                        }
                        lo <<= 1;
                    }
                }
            }
        }
        return result;
    }

    /**
     * Init BitSet
     */
    public void init() {
        for(int i = 0; i < SIZE; i++) {
            bitSet[i] = 0;
        }
        needToInit = false;
        noDuplicates = 0;
    }


    public static void main(String[] args) {
        UniqueIntegerBitSetSort directIntegerSort = new UniqueIntegerBitSetSort();
        Random random = new Random();
        int size = 1000000000;
        int half = size/2;
        int[] array = new int[size];
        for(int i = 0; i < size; i++) {
            array[i] = size - i;
        }
        for(int i = 0; i < size/3; i++) {
            int first = random.nextInt(size);
            int second = random.nextInt(size);
            int temp = array[first];
            array[first] = array[second];
            array[second] = temp;
        }
        System.out.println("Sorting of " + size + " elements started ....");
        long start = System.nanoTime();
        directIntegerSort.sort(array);
        long stop = System.nanoTime();
        System.out.println("Bit Sorted for: " + ((stop-start)/1000000) + " ms");
        start = System.nanoTime();
        directIntegerSort.sort(array);
        stop = System.nanoTime();
        System.out.println("Bit Sorted for: " + ((stop-start)/1000000) + " ms");
        start = System.nanoTime();
        Arrays.sort(array);
        stop = System.nanoTime();
        System.out.println("Java Sorted for:   " + ((stop-start)/1000000) + " ms");
    }
}
