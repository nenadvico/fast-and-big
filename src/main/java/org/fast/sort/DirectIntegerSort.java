package org.fast.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DirectIntegerSort {

    private static final int SIZE = 67108864;
    private static final int SHIFT = 6;
    private final long[] table = new long[SIZE];
    private boolean needToInit;

    public void sort(int[] array) {
        long initStart = System.nanoTime();
        if (needToInit) {
            init();
        }
        long insertStart = System.nanoTime();
        System.out.println("Init time: "+ ((insertStart-initStart)/1000) + "us");
        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;
        int maxDup = Integer.MIN_VALUE;
        Map<Integer,Integer> duplicates = new HashMap<Integer, Integer>();
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
            long value = table[index];
            boolean exists = (value & lo) != 0;
            table[index] = value | lo;;

           if (exists) {
               Integer count = duplicates.get(in);
               if (count == null) {
                    count = 0;
               }
               count++;
               duplicates.put(in, count);
           }
        }
        System.out.println("duplicates size: " + duplicates.size());
        long insertEnd = System.nanoTime();
        System.out.println("Insert time: " + ((insertEnd-insertStart)/1000) + "us");
        int arrIndex = 0;
        if (min < 0) {
            int upper = SIZE;
            if (max < 0) {
                upper = max >>> SHIFT;
            }
            for(int index = (min >>> SHIFT); index < upper; index++) {
                long value = table[index];
                if (value != 0) {
                    int hi = index << SHIFT;
                    long lo = 0x1;
                    for (int pos = 0; pos < 64; pos++) {
                        if ((value & lo) != 0) {
                            int number = hi + pos;
                            array[arrIndex++] = number;
                            Integer dup = duplicates.get(number);
                            if (dup != null) {
                                if (dup > maxDup) {
                                    maxDup = dup;
                                }
                                for(int i = 0; i < dup; i++) {
                                    array[arrIndex++] = number;
                                }
                            }
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
                long value = table[index];
                if (value != 0) {
                    int hi = index << SHIFT;
                    long lo = 0x1;
                    for (int pos = 0; pos < 64; pos++) {
                        if ((value & lo) != 0) {
                            int number = hi + pos;
                            array[arrIndex++] = number;
                            Integer dup = duplicates.get(number);
                            if (dup != null) {
                                if (dup > maxDup) {
                                    maxDup = dup;
                                }
                                for(int i = 0; i < dup; i++) {
                                    array[arrIndex++] = number;
                                }
                            }
                        }
                        lo <<= 1;
                    }
                }
            }
        }
        long readEnd = System.nanoTime();
        System.out.println("Read time  :  " + ((readEnd-insertEnd)/1000) + "us");
        System.out.println("Max dup  :  " + maxDup);
    }

    public void init() {
        for(int i = 0; i < SIZE; i++) {
            table[i] = 0;
        }
    }


    public static void main(String[] args) {
        DirectIntegerSort directIntegerSort = new DirectIntegerSort();
        Random random = new Random();
        int size = 1000000000;
        //int[] array = random.ints(size).toArray();
        int[] array = random.ints(size,size/2, 5*size).toArray();
        int[] copy = Arrays.copyOf(array, array.length);
//        directIntegerSort.sort(copy);
        long start = System.nanoTime();
        directIntegerSort.sort(copy);
        long stop = System.nanoTime();
        System.out.println("Direct Sorted for: " + ((stop-start)/1000000) + " ms");
        start = System.nanoTime();
        Arrays.sort(array);
        stop = System.nanoTime();
        System.out.println("Java Sorted for:   " + ((stop-start)/1000000) + " ms");
    }
}
