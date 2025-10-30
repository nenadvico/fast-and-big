package org.fast.search.hash;

import org.junit.Assert;
import org.junit.Test;

/**
 * Test class.
 * @author Nenad.Vico
 */
public class FastTableTest {


    @Test
    public void testAddContains() {
        FastTable fastTable = new FastTable();
        fastTable.add(0);
        Assert.assertTrue(fastTable.contains(0));
        fastTable.add(1);
        Assert.assertTrue(fastTable.contains(1));
        fastTable.add(-1);
        Assert.assertTrue(fastTable.contains(-1));
        fastTable.add(Integer.MAX_VALUE);
        Assert.assertTrue(fastTable.contains(Integer.MAX_VALUE));
        fastTable.add(Integer.MIN_VALUE);
        Assert.assertFalse(fastTable.contains(2));
        Assert.assertFalse(fastTable.contains(Integer.MIN_VALUE + 1));
        Assert.assertFalse(fastTable.contains(Integer.MAX_VALUE - 1));
        Assert.assertFalse(fastTable.contains(12635574));
        Assert.assertFalse(fastTable.contains(-849573839));
    }

    @Test
    public void testAddRemoveContains() {
        FastTable fastTable = new FastTable();
        fastTable.add(1);
        Assert.assertTrue(fastTable.contains(1));
        fastTable.add(-1);
        Assert.assertTrue(fastTable.contains(-1));
        fastTable.add(Integer.MAX_VALUE);
        Assert.assertTrue(fastTable.contains(Integer.MAX_VALUE));
        fastTable.add(Integer.MIN_VALUE);

        fastTable.remove(1);
        Assert.assertFalse(fastTable.contains(1));
        fastTable.remove(-1);
        Assert.assertFalse(fastTable.contains(-1));
        fastTable.remove(Integer.MAX_VALUE);
        Assert.assertFalse(fastTable.contains(Integer.MAX_VALUE));
        fastTable.remove(Integer.MIN_VALUE);
        Assert.assertFalse(fastTable.contains(Integer.MIN_VALUE));

        Assert.assertFalse(fastTable.contains(2));
        Assert.assertFalse(fastTable.contains(Integer.MIN_VALUE+1));
        Assert.assertFalse(fastTable.contains(Integer.MAX_VALUE-1));
        Assert.assertFalse(fastTable.contains(12635574));
        Assert.assertFalse(fastTable.contains(-849573839));
    }

    @Test
    public void testPerformace() {
        FastTable fastTable = new FastTable();
        long start = 0; //Integer.MIN_VALUE;
        long stop = 1000000000; //Integer.MAX_VALUE;
        long size = stop-start;
        System.out.println("Adding: "+size+" elements...");
        boolean result = false;
        long begin = System.nanoTime();
        for(int i = (int)start; i < stop; i++) {
            result |= fastTable.add(i);
        }
        long end = System.nanoTime();
        Assert.assertFalse(result);
        System.out.println("Adding: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per add");

        System.out.println("Checking: "+size+" elements...");
        result = true;
        begin = System.nanoTime();
        for(int i = (int)start; i < stop; i++) {
            result &= fastTable.contains(i);
        }
        Assert.assertTrue(result);
        end = System.nanoTime();
        System.out.println("Checking: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per check");

        System.out.println("Removing: "+size+" elements...");
        begin = System.nanoTime();
        result = true;
        for(int i = (int)start; i < stop; i++) {
            result &= fastTable.remove(i);
        }
        Assert.assertTrue(result);
        end = System.nanoTime();
        System.out.println("Removing: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per remove");
    }
}
