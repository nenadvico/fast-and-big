package org.fast.search;

import org.fast.search.hash.SearchTable;

/**
 * Used for fast String memory search based on hash codes. Object requires 512 MB of memory per search table.
 * @author Nenad.Vico
 */
public class StringSearchEngine extends SearchEngine<String>
{
    private static final int[] factors = {11,7,3,37,97};

    /**
     * Default Constructor.
     */
    public StringSearchEngine() {
        super();
    }

    /**
     * Constructor with number of search tables.
     * @param number  number of search  tables.
     */
    public StringSearchEngine(int number) {
        super();
        setNumberOfSearchTables(number);
    }

    /**
     * Sets number of search tables.
     * @param number number of search tables.
     */
    public void setNumberOfSearchTables(int number) {
        if (number > factors.length+1) {
            throw new IllegalArgumentException("Number of search tables is greater than maximum allowed: "+factors.length);
        }
        if (number < 0) {
            number = 1;
        }
        SearchTable[] tables = new SearchTable[number];
        tables[0] = new SearchTable<String>();
        for(int i = 1; i < number; i++) {
            tables[i] =  new StringSearchTable(factors[i-1]);
        }
        setTables(tables);
    }

    private static class StringSearchTable extends SearchTable<String> {
        private int number = 11;

        /**
         * Constructor.
         * @param number number for hash multiplying
         */
        public StringSearchTable(int number) {
            this.number = number;
        }

        /**
         * Calculate hash code for string based on multiplying.
         * @param object object to calculate.
         * @return Returns hash code.
         */
        protected int hashCode(String object) {
            int h = 0;
            int len = object.length();
            if (len > 0) {
                for (int i = 0; i < len; i++) {
                    h = number*h + object.charAt(i);
                }
            }
            return h;
        }

    }
}
