package org.fast.search.hash;

/**
 * Table of hash codes used for search. Object requires 512 MB of memory.
 * Hash code is used as address and value is one bit (true or false).
 * If hash code is present appropriate bit is set to 1, otherwise false.
 * Performance is extremely high and constant regardless of number of elements in table.
 * Ex. Intel i3 2100 processor adds for 6ns, contains for 5ns and removes for 6ns.
 * @author Nenad.Vico
 */
public class FastTable {
    private int[] table = new int[134217728];

    /**
     * Adds hash code to table.
     * @param hash to add.
     * @return Returns true if that hash code already exists in table, otherwise false.
     */
    public final boolean add(int hash) {
        int index = hash >>> 5;
        int value = table[index];
        int pos = hash & 0x1f;
        int lo = 0x1 << pos;
        boolean alreadyHas = (value & lo) != 0;
        table[index] = value | lo;
        return alreadyHas;
    }

    /**
     * Checks if hash code exists in table.
     * @param hash hash code to add.
     * @return Returns true if hash code exists in table, otherwise false.
     */
    public final boolean contains(int hash) {
        int index = hash >>> 5;
        int value = table[index];
        int pos = hash & 0x1f;
        int lo = 0x1 << pos;
        return (value & lo) != 0;
    }

    /**
     * Removes hash code from table.
     * @param hash has code to remove.
     * @return Returns true if hash code exists in table and successfully removed.
     */
    public final boolean remove(int hash) {
        int index = hash >>> 5;
        int value = table[index];
        int pos = hash & 0x1f;
        int lo = 0x1 << pos;
        boolean has = (value & lo) != 0;
        table[index] = value & ~lo;
        return has;
    }

}
