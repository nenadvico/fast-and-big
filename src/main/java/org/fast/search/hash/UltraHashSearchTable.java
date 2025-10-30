package org.fast.search.hash;

/**
 * @param <T> object type.
 * @author Nenad.Vico
 */
public abstract class UltraHashSearchTable<T>
{
    private FastTable loTable = new FastTable();
    private FastTable hiTable = new FastTable();
    private FastTable uhTable = new FastTable();
    private FastTable xrTable = new FastTable();

    /**
     * Adds object to table.
     * @param object object to add.
     * @return Returns true if already exists, otherwise false.
     */
    public final boolean add(T object) {
        long hash[] = hashCode(object);
        int lo = (int)hash[0];
        int hi = (int)(hash[0] >>> 32);
        int uh = (int)hash[1];
        int xr = (int)(hash[1] >>> 32);
        boolean exists = loTable.add(lo);
        exists &= hiTable.add(hi);
        exists &= uhTable.add(uh);
        exists &= xrTable.add(xr);
        return exists;
    }

    /**
     * Removes object from table.
     * @param object object to remove.
     * @return Returns true if removed, otherwise false.
     */
    public final boolean remove(T object) {
        long hash[] = hashCode(object);
        int lo = (int)hash[0];
        int hi = (int)(hash[0] >>> 32);
        int uh = (int)hash[1];
        int xr = (int)(hash[1] >>> 32);
        boolean exists = loTable.contains(lo);
        exists &= hiTable.contains(hi);
        exists &= uhTable.contains(uh);
        exists &= xrTable.contains(xr);
        if (exists) {
            loTable.remove(lo);
            hiTable.remove(hi);
            uhTable.remove(uh);
            xrTable.remove(xr);
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
        long hash[] = hashCode(object);
        int lo = (int)hash[0];
        int hi = (int)(hash[0] >>> 32);
        int uh = (int)hash[1];
        int xr = (int)(hash[1] >>> 32);
        boolean exists = loTable.contains(lo);
        exists &= hiTable.contains(hi);
        exists &= uhTable.contains(uh);
        exists &= xrTable.contains(xr);
        return exists;
    }

    /**
     * Calculate hash code for object.
     * @param object object to calculate.
     * @return Returns hash code.
     */
    protected abstract long[] hashCode(T object);

}
