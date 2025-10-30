package org.fast.search;

import org.fast.search.hash.SearchTable;

/**
 * Used for fast memory search based on hash codes. Object requires 512 MB of memory per search table.
 * @param <T> object type.
 * @author Nenad.Vico
 */
public class SearchEngine<T>
{
    private SearchTable<T>[] tables;

    /**
     * Default Constructor.
     */
    public SearchEngine() {
    }

    /**
     * Constructor with search tables.
     * @param matrixes search tables.
     */
    public SearchEngine(SearchTable<T>[] matrixes) {
        this.tables = matrixes;
    }

    /**
     * Adds object to search engine.
     * @param object object to add.
     * @return Returns true if already exists, otherwise false.
     */
    public final boolean add(T object) {
        boolean result = true;
        for(SearchTable table : tables) {
            result &= table.add(object);
        }
        return result;
    }

    /**
     * Removes object from search engine.
     * @param object object to remove.
     * @return Returns true if removed, otherwise false.
     */
    public final boolean remove(T object) {
        boolean found = contains(object);
        if (found) {
            for(SearchTable table : tables) {
                table.remove(object);
            }
        }
        return found;
    }

    /**
     * Determines if object contains in search engine.
     * @param object object to check.
     * @return Returns true if object contains in search engine.
     */
    public final boolean contains(T object) {
        for(SearchTable table : tables) {
            if (!table.contains(object)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Sets search tables.
     * @param tables search tables to set.
     */
    public void setTables(SearchTable<T>[] tables) {
        this.tables = tables;
    }
}
