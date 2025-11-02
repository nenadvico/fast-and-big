package org.fast.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;


/**
 * Created by Nenad Vico.
 * User: vico.nenad@gmail.com
 * To change this template use File | Settings | File Templates.
 */
public class UniqueIntegerBitSortTest {

    private final UniqueIntegerBitSetSort directSort = new UniqueIntegerBitSetSort();

    @Test
     public void testBasicSort() {
        int[] array = { -1, 7, 6, 9, 67108864, 18, 3, 3467, 0, 100, -36678, -1212124343, 100, Integer.MAX_VALUE, Integer.MIN_VALUE};
        int[] expected = {Integer.MIN_VALUE, -1212124343, -36678, -1, 0, 3, 6, 7, 9, 18, 100, 3467, 67108864, Integer.MAX_VALUE};
        int[] copy = directSort.sort(array);
        Arrays.sort(array);
        System.out.println(Arrays.toString(copy));
        assertArrayEquals(expected, copy);
    }

    @Test
    public void testRandoSort() {
        Random random = new Random();
        int size = 10000;
        int[] array = random.ints(size).toArray();
        int[] copy = directSort.sort(array);
        Arrays.sort(array);
        assertArrayEquals(array, copy);
    }

    @Test
    public void testPerformanceRandoSort1GData() {
        UniqueIntegerBitSetSort directIntegerSort = new UniqueIntegerBitSetSort();
        Random random = new Random();
        int size = 100000000;
        int half = size/2;
        int[] array = new int[size];
        for(int i = 0; i < size; i++) {
            array[i] = half - i;
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
        System.out.println("BitSet Sorted for: " + ((stop-start)/1000000) + " ms");
        start = System.nanoTime();
        Arrays.sort(array);
        stop = System.nanoTime();
        System.out.println("Java Sorted for:   " + ((stop-start)/1000000) + " ms");
    }

}
