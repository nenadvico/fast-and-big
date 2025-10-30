package org.fast.search.hash;

import org.junit.Assert;
import org.junit.Test;

import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: msi
 * Date: 10.10.12.
 * Time: 19.44
 * To change this template use File | Settings | File Templates.
 */
public class StringSearchTableTest {

    @Test
     public void testAddContains() {
         StringSearchTable searchTable = new StringSearchTable();
         searchTable.add("0");
         Assert.assertTrue(searchTable.contains("0"));
         searchTable.add("1");
         Assert.assertTrue(searchTable.contains("1"));
         searchTable.add("-1");
         searchTable.add("-1");
         Assert.assertTrue(searchTable.contains("-1"));
         Assert.assertFalse(searchTable.contains("2"));
         Assert.assertFalse(searchTable.contains("12635574"));
         Assert.assertFalse(searchTable.contains("-849573839"));
     }

     @Test
     public void testAddRemoveContains() {
         StringSearchTable searchTable = new StringSearchTable();
         searchTable.add("1");
         Assert.assertTrue(searchTable.contains("1"));
         searchTable.add("-1");
         searchTable.add("-1");
         Assert.assertTrue(searchTable.contains("-1"));

         searchTable.remove("1");
         Assert.assertFalse(searchTable.contains("1"));
         searchTable.remove("-1");
         Assert.assertFalse(searchTable.contains("-1"));
      }


    @Test
    public void testPerformace() {
        StringSearchTable searchTable = new StringSearchTable();
        long start = 0; //Integer.MIN_VALUE;
        long stop = 10000000; //Integer.MAX_VALUE;
        long size = stop-start;
        System.out.println("Adding: "+size+" elements...");
        boolean result = false;
        long begin = System.nanoTime();
        for(int i = (int)start; i < stop; i++) {
            result |= searchTable.add(i+"");
        }
        long end = System.nanoTime();
        Assert.assertFalse(result);
        System.out.println("Adding: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per add");

        System.out.println("Checking: "+size+" elements...");
        result = true;
        begin = System.nanoTime();
        for(int i = (int)start; i < stop; i++) {
            result &= searchTable.contains(i+"");
        }
        Assert.assertTrue(result);
        end = System.nanoTime();
        System.out.println("Checking: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per check");

        System.out.println("Removing: "+size+" elements...");
        begin = System.nanoTime();
        result = true;
        for(int i = (int)start; i < stop; i++) {
            result &= searchTable.remove(i+"");
        }
        end = System.nanoTime();
        System.out.println("Removing: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per remove");
    }

    @Test
    public void testStringRedundancy() {
        StringSearchTable searchTable = new StringSearchTable();
        long start = 0; //Integer.MIN_VALUE;
        long stop = 10000000; //Integer.MAX_VALUE;
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

        stop = 10000000; //Integer.MAX_VALUE;
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
