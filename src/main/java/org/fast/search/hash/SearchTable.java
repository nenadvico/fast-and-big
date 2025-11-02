package org.fast.search.hash;

import org.fast.FastTable;

/**
 * Used for fast memory search based on hash codes. Object requires 512 MB of memory.
 * Hash code is not invertible function, two values could produce the same hash code,
 * so redundancy is possible. Therefore all methods are not completely accurate.
 * Percent of error is depends of hashCode function and of table density ratio.
 * For String type, error is approximately equal to table density ratio
 * (number of added elements/4294967296). Ex. 10.000.000 elements have about 0.227% of error.
 * To reduce string redundancy error use more SearchTables with different hash functions, see
 * StringSearchEngine.
 * This class is completely accurate for checking if something is not exists. In most
 * structures searching something that not exists is most expensive and that could be improved by this class.
 * Performance is extremely high and constant regardless of number of elements in table.
 * Ex. Intel i3 2100 processor searches Integer and Long type for 17ns and String type for 155ns.
 * Searching takes about 5ns, the rest is hashCode function.
 * @param <T> object type.
 * @author Nenad.Vico
 */

public class SearchTable<T>
{
    private final FastTable fastTable = new FastTable();

    /**
     * Adds object to table.
     * @param object object to add.
     * @return Returns true if already exists, otherwise false.
     */
    public final boolean add(T object) {
        return fastTable.add(hashCode(object));
    }

    /**
     * Removes object from table.
     * @param object object to remove.
     * @return Returns true if removed, otherwise false.
     */
    public final boolean remove(T object) {
        return fastTable.remove(hashCode(object));
    }

    /**
     * Determines if object contains in table.
     * True result is not curtain, because search is based on hash codes.
     * @param object object to check.
     * @return Returns true if object contains in fastTable.
     */
    public final boolean contains(T object) {
        return fastTable.contains(hashCode(object));
    }

    /**
     * Calculate hash code for object.
     * @param object object to calculate.
     * @return Returns hash code.
     */
    protected int hashCode(T object) {
        return object.hashCode();
    }

}
