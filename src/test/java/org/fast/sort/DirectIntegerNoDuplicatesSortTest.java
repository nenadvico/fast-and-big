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
public class DirectIntegerNoDuplicatesSortTest {

    private final DirectIntegerNoDuplicatesSort directSort = new DirectIntegerNoDuplicatesSort();

    @Test
     public void testBasicSort() {
        int[] array = { -1, 7, 6, 9, 67108864, 18, 3, 3467, 0, -36678, -1212124343, 100, Integer.MAX_VALUE, Integer.MIN_VALUE};
        int[] copy = Arrays.copyOf(array, array.length);
        directSort.sort(copy);
        Arrays.sort(array);
        System.out.println(Arrays.toString(copy));
        System.out.println(Arrays.toString(array));
        assertArrayEquals(array, copy);
    }

    @Test
    public void testRandoSort() {
        Random random = new Random();
        int size = 10000;
        int[] array = random.ints(size).toArray();
        int[] copy = Arrays.copyOf(array, array.length);
        directSort.sort(copy);
        Arrays.sort(array);
        assertArrayEquals(array, copy);
    }

    @Test
    public void testPerformanceRandoSort1GData() {
        DirectIntegerNoDuplicatesSort directIntegerSort = new DirectIntegerNoDuplicatesSort();
        Random random = new Random();
        int size = 100000000;
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
        int[] copy = Arrays.copyOf(array, array.length);
        System.out.println("Sorting of " + size + " elements started ....");
        long start = System.nanoTime();
        directIntegerSort.sort(copy);
        long stop = System.nanoTime();
        System.out.println("Direct Sorted for: " + ((stop-start)/1000000) + " ms");
        copy = Arrays.copyOf(array, array.length);
        start = System.nanoTime();
        directIntegerSort.sort(copy);
        stop = System.nanoTime();
        System.out.println("Direct Sorted for: " + ((stop-start)/1000000) + " ms");
        start = System.nanoTime();
        Arrays.sort(array);
        stop = System.nanoTime();
        System.out.println("Java Sorted for:   " + ((stop-start)/1000000) + " ms");
    }

}
