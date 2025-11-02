package org.fast.search.hash;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: msi
 * Date: 10.10.12.
 * Time: 19.44
 * To change this template use File | Settings | File Templates.
 */
public class SearchTableTest {

    @Test
     public void testAddContainsInt() {
         SearchTable<Integer> searchTable = new SearchTable<Integer>();
         searchTable.add(0);
         assertTrue(searchTable.contains(0));
         searchTable.add(1);
         assertTrue(searchTable.contains(1));
         searchTable.add(-1);
         searchTable.add(-1);
         assertTrue(searchTable.contains(-1));
         searchTable.add(Integer.MAX_VALUE);
         assertTrue(searchTable.contains(Integer.MAX_VALUE));
         searchTable.add(Integer.MIN_VALUE);
         assertFalse(searchTable.contains(2));
         assertFalse(searchTable.contains(Integer.MIN_VALUE + 1));
         assertFalse(searchTable.contains(Integer.MAX_VALUE - 1));
         assertFalse(searchTable.contains(12635574));
         assertFalse(searchTable.contains(-849573839));
     }

     @Test
     public void testAddRemoveContainsInt() {
         SearchTable<Integer> searchTable = new SearchTable<Integer>();
         searchTable.add(1);
         assertTrue(searchTable.contains(1));
         searchTable.add(-1);
         searchTable.add(-1);
         assertTrue(searchTable.contains(-1));
         searchTable.add(Integer.MAX_VALUE);
         assertTrue(searchTable.contains(Integer.MAX_VALUE));
         searchTable.add(Integer.MIN_VALUE);

         searchTable.remove(1);
         assertFalse(searchTable.contains(1));
         searchTable.remove(-1);
         assertFalse(searchTable.contains(-1));
         searchTable.remove(Integer.MAX_VALUE);
         assertFalse(searchTable.contains(Integer.MAX_VALUE));
         searchTable.remove(Integer.MIN_VALUE);
         assertFalse(searchTable.contains(Integer.MIN_VALUE));

         assertFalse(searchTable.contains(2));
         assertFalse(searchTable.contains(Integer.MIN_VALUE+1));
         assertFalse(searchTable.contains(Integer.MAX_VALUE-1));
         assertFalse(searchTable.contains(12635574));
         assertFalse(searchTable.contains(-849573839));
     }


    @Test
    public void testIntPerformace() {
        SearchTable<Integer> searchTable = new SearchTable<Integer>();
        long start = 0; //Integer.MIN_VALUE;
        long stop = 10000000; //Integer.MAX_VALUE;
        long size = stop-start;
        System.out.println("Adding: "+size+" elements...");
        boolean result = false;
        long begin = System.nanoTime();
        for(int i = (int)start; i < stop; i++) {
            result |= searchTable.add(i);
        }
        long end = System.nanoTime();
        assertFalse(result);
        System.out.println("Adding: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per add");

        System.out.println("Checking: "+size+" elements...");
        result = true;
        begin = System.nanoTime();
        for(int i = (int)start; i < stop; i++) {
            result &= searchTable.contains(i);
        }
        assertTrue(result);
        end = System.nanoTime();
        System.out.println("Checking: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per check");

        System.out.println("Removing: "+size+" elements...");
        begin = System.nanoTime();
        result = true;
        for(int i = (int)start; i < stop; i++) {
            result &= searchTable.remove(i);
        }
        assertTrue(result);
        end = System.nanoTime();
        System.out.println("Removing: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per remove");
    }


    @Test
    public void testLongPerformace() {
        SearchTable<Long> searchTable = new SearchTable<Long>();
        long start = 0; //Integer.MIN_VALUE;
        long stop = 100000000; //Integer.MAX_VALUE;
        long size = stop-start;
        System.out.println("Adding: "+size+" elements...");
        boolean result = false;
        long begin = System.nanoTime();
        for(long i = start; i < stop; i++) {
            result |= searchTable.add(i);
        }
        long end = System.nanoTime();
        assertFalse(result);
        System.out.println("Adding: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per add");

        System.out.println("Checking: "+size+" elements...");
        result = true;
        begin = System.nanoTime();
        for(long i = start; i < stop; i++) {
            result &= searchTable.contains(i);
        }
        end = System.nanoTime();
        assertTrue(result);
        System.out.println("Checking: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per check");

        System.out.println("Removing: "+size+" elements...");
        begin = System.nanoTime();
        result = true;
        for(long i = start; i < stop; i++) {
            result &= searchTable.remove(i);
        }
        end = System.nanoTime();
        assertTrue(result);
        System.out.println("Removing: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per remove");
    }

    @Test
    public void testStringPerformace() {
        SearchTable<String> searchTable = new SearchTable<String>();
        long start = 0; //Integer.MIN_VALUE;
        long stop = 100000000; //Integer.MAX_VALUE;
        long size = stop-start;
        System.out.println("Adding: "+size+" elements...");
        boolean result = false;
        long begin = System.nanoTime();
        for(long i = start; i < stop; i++) {
            result |= searchTable.add(i+"");
        }
        long end = System.nanoTime();
        System.out.println("Adding: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per add");

        System.out.println("Checking: "+size+" elements...");
        result = true;
        begin = System.nanoTime();
        for(long i = start; i < stop; i++) {
            result &= searchTable.contains(i+"");
        }
        assertTrue(result);
        end = System.nanoTime();
        System.out.println("Checking: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per check");

        System.out.println("Removing: "+size+" elements...");
        begin = System.nanoTime();
        result = true;
        for(long i = start; i < stop; i++) {
            result &= searchTable.remove(i+"");
        }
        end = System.nanoTime();
        System.out.println("Removing: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per remove");
    }

    @Test
    public void testStringRedundancy() {
        SearchTable<String> searchTable = new SearchTable<String>();
        long start = 0; //Integer.MIN_VALUE;
        long stop = 100000000; //Integer.MAX_VALUE;
        long size = stop-start;
        System.out.println("Adding: "+size+" elements...");
        int redundant = 0;
        long begin = System.nanoTime();
        for(long i = start; i < stop; i++) {
            String element = UUID.randomUUID().toString();
            if (searchTable.add(element)) {
                redundant++;
            }
        }
        long end = System.nanoTime();
        System.out.println("Adding: "+size+" redundancy:"+redundant+" redundancy per:"+(((double)redundant)*100/size)+"%");

        stop = 1000000; //Integer.MAX_VALUE;
        size = stop-start;
        System.out.println("Checking: "+size+" elements...");
        redundant = 0;
        begin = System.nanoTime();
        for(long i = start; i < stop; i++) {
            String element = UUID.randomUUID().toString();
            if (searchTable.contains(element)) {
                redundant++;
            }
        }
        end = System.nanoTime();
        System.out.println("Checking: "+size+" redundancy:"+redundant+" redundancy per:"+(((double)redundant)*100/size)+"%");
    }

}
