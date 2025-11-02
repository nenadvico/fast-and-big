package org.fast.search.hash;

import org.fast.FastTable;

/**
 * @param <T> object type.
 * @author Nenad.Vico
 */

public abstract class LongHashSearchTable<T>
{
    private final FastTable loTable = new FastTable();
    private final FastTable hiTable = new FastTable();

    /**
     * Adds object to table.
     * @param object object to add.
     * @return Returns true if already exists, otherwise false.
     */
    public final boolean add(T object) {
        long hash = hashCode(object);
        int lo = (int)hash;
        int hi = (int)(hash >>> 32);
        boolean exists = loTable.add(lo);
        exists &= hiTable.add(hi);
        return exists;
    }

    /**
     * Removes object from table.
     * @param object object to remove.
     * @return Returns true if removed, otherwise false.
     */
    public final boolean remove(T object) {
        long hash = hashCode(object);
        int lo = (int)hash;
        int hi = (int)(hash >>> 32);
        boolean exists = loTable.contains(lo);
        exists &= hiTable.contains(hi);
        if (exists) {
            loTable.remove(lo);
            hiTable.remove(hi);
        }
        return exists;
    }

    /**
     * Determines if object contains in table.
     * True result is not curtain, because search is based on hash codes.
     * @param object object to check.
     * @return Returns true if object contains in fastTable.
     */
    public final boolean contains(T object) {
        long hash = hashCode(object);
        int lo = (int)hash;
        int hi = (int)(hash >>> 32);
        boolean exists = loTable.contains(lo);
        exists &= hiTable.contains(hi);
        return exists;
    }

    /**
     * Calculate hash code for object.
     * @param object object to calculate.
     * @return Returns hash code.
     */
    protected abstract long hashCode(T object);

}
