package org.fast.search;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.UUID;

/**
 * Created by IntelliJ IDEA.
 * User: msi
 * Date: 11.10.12.
 * Time: 19.18
 * To change this template use File | Settings | File Templates.
 */
public class StringSearchEngineTest {

    @Test
     public void testAddContains() {
         StringSearchEngine searchEngine = new StringSearchEngine(2);
         searchEngine.add("0");
         assertTrue(searchEngine.contains("0"));
         searchEngine.add("1");
         assertTrue(searchEngine.contains("1"));
         searchEngine.add("test1");
         searchEngine.add("test1");
         assertTrue(searchEngine.contains("test1"));
         searchEngine.add("Hello World");
         assertTrue(searchEngine.contains("Hello World"));

         assertFalse(searchEngine.contains("2"));
         assertFalse(searchEngine.contains("test2"));
         assertFalse(searchEngine.contains(""));
         assertFalse(searchEngine.contains("what time is it"));
         assertFalse(searchEngine.contains("nenad.vico@yahoo.com"));
     }

     @Test
     public void testAddRemoveContainsInt() {
         StringSearchEngine searchEngine = new StringSearchEngine(2);
         searchEngine.add("0");
         assertTrue(searchEngine.contains("0"));
         searchEngine.add("1");
         assertTrue(searchEngine.contains("1"));
         searchEngine.add("test1");
         searchEngine.add("test1");
         assertTrue(searchEngine.contains("test1"));
         searchEngine.add("Hello World");

         searchEngine.remove("1");
         assertFalse(searchEngine.contains("1"));
         searchEngine.remove("-1");
         assertFalse(searchEngine.contains("-1"));
         searchEngine.remove("test1");
         assertFalse(searchEngine.contains("test1"));
         assertTrue(searchEngine.contains("Hello World"));

         assertFalse(searchEngine.contains("2"));
         assertFalse(searchEngine.contains("what time is it"));
         assertFalse(searchEngine.contains("nenad.vico@yahoo.com"));
     }


    @Test
    public void testPerformace() {
        StringSearchEngine searchEngine = new StringSearchEngine(2);
        long start = 0; //Integer.MIN_VALUE;
        long stop = 100000000; //Integer.MAX_VALUE;
        long size = stop-start;
        System.out.println("Adding: " + size + " elements...");
        boolean result = false;
        long begin = System.nanoTime();
        for(int i = (int)start; i < stop; i++) {
            result |= searchEngine.add("3432456345634563434");
        }
        long end = System.nanoTime();
        System.out.println("Adding: " + size + " takes:" + (end - begin) + " ns average: " + ((end - begin) / size) + " ns per add");

        System.out.println("Checking: " + size + " elements...");
        result = true;
        begin = System.nanoTime();
        for(int i = (int)start; i < stop; i++) {
            result &= searchEngine.contains("3432456345634563434");
        }
        end = System.nanoTime();
        System.out.println("Checking: " + size + " takes:" + (end - begin) + " ns average: " + ((end - begin) / size) + " ns per check");

        System.out.println("Removing: " + size + " elements...");
        begin = System.nanoTime();
        result = true;
        for(int i = (int)start; i < stop; i++) {
            result &= searchEngine.remove("343243434");
        }
        end = System.nanoTime();
        System.out.println("Removing: "+size+" takes:"+(end-begin)+" ns average: "+((end-begin)/size)+" ns per remove");
    }

    @Test
    public void testStringRedundancy() {
        StringSearchEngine searchEngine = new StringSearchEngine(3);
        long start = 0; //Integer.MIN_VALUE;
        long stop = 10000000; //Integer.MAX_VALUE;
        long size = stop-start;
        System.out.println("Adding: "+size+" elements...");
        int redundant = 0;
        long begin = System.nanoTime();
        for(long i = start; i < stop; i++) {
            String element = i+"";
//            String element = UUID.randomUUID().toString();
            if (searchEngine.add(element)) {
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
            String element = i+"";
//            String element = UUID.randomUUID().toString();
            if (searchEngine.contains(element)) {
                redundant++;
            }
        }
        end = System.nanoTime();
        System.out.println("Checking: "+size+" redundancy:"+redundant+" redundancy per:"+(((double)redundant)*100/size)+"%");
    }

}
