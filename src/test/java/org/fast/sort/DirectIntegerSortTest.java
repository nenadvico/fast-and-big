package org.fast.sort;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;


/**
 * Created by Nenad Vico.
 * User: vico.nenad@gmail.com
 * To change this template use File | Settings | File Templates.
 */
public class DirectIntegerSortTest {

    private final DirectIntegerSort directIntegerSort = new DirectIntegerSort();

    @Test
     public void testBasicSort() {
        //int[] array = { -1917147362, 0, -1917147362 };
        int[] array = { -1, 7, 6, 9, 67108864, 18, 3, 3467, 0, -36678, -1212124343, 100, Integer.MAX_VALUE, Integer.MIN_VALUE};
        int[] copy = Arrays.copyOf(array, array.length);
        directIntegerSort.sort(copy);
        Arrays.sort(array);
        System.out.println(Arrays.toString(copy));
        System.out.println(Arrays.toString(array));
        assertArrayEquals(array, copy);
    }

    @Test
    public void testRandoSort() {
        Random random = new Random();
        int size = 100000000;
        int[] array = random.ints(size).toArray();
        int[] copy = Arrays.copyOf(array, array.length);
        directIntegerSort.sort(copy);
        Arrays.sort(array);
        assertArrayEquals(array, copy);
    }

    @Test
    public void testPerformanceRandoSort1GData() {
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
