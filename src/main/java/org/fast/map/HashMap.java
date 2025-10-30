package org.fast.map;

import java.io.Serializable;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Map that could store large number of entries. More then 100 millions of entries in 1 GB.
 * @author Nenad.Vico.
 */
public abstract class HashMap<K,V> implements Map<K,V>, Cloneable, Serializable {

    protected static final int DEFAULT_HASH_SIZE = 256*256;
    protected static final int DEFAULT_INITIAL_CAPACITY = 200000;
    protected Map<K,V> over = new java.util.HashMap<K,V>(DEFAULT_INITIAL_CAPACITY);
    protected int count;
    protected int backetSize = 1024;
    protected byte fastBits;
    protected int fastValue = 1;
    protected boolean removeable = true;
    protected int mask = 0xffff;

    /**
     * Constructor with maximum expected map size.
     * @param size maximum expected map size.
     */
    public HashMap(int size) {
        if (size > 0) {
            size = size / DEFAULT_HASH_SIZE;
            backetSize = (size / 256) * 256;
            int rest = size % 256;
            if (rest > 0) {
                backetSize += 256;
            }
            initData(getRowSize() * DEFAULT_HASH_SIZE *backetSize);
            mask = (0x1 << 16+fastBits)-1;
        }
    }

    protected abstract int getRowSize();

    protected abstract void initData(int size);

    /**
     * True if map allow removing elements. Default is true.
     * @param removeable if true removing elements is allowed.
     */
    public void setRemoveable(boolean removeable) {
        this.removeable = removeable;
    }

    /**
     * Used to make map searching faster, allowed values are [0..8]. Greater value speed up search but causing
     * more backet overflows. Calling this method clears map
     * @param fastBits bits allowed values are [0..8]
     */
    public void setFastBits(int fastBits) {
        if (fastBits < 0 || fastBits > 8) {
            throw new IllegalArgumentException("Allowed values are [0..8] value is "+fastBits);
        }
        clear();
        this.fastBits = (byte)fastBits;
        fastValue = 0x1 << fastBits;
        mask = (0x1 << 16+fastBits)-1;
    }


    /**
     * Returns the number of key-value mappings in this map.  If the
     * map contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of key-value mappings in this map
     */
    @Override
    public int size() {
        return count+over.size();
    }

    /**
     * Gets number of key-value pairs stored in overflow buffer because of oversize of their backet.
     * @return Returns number of key-value pairs stored in overflow buffer.
     */
    public int getOverflowSize() {
        return over.size();
    }

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings
     */
    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    /**
     * Returns <tt>true</tt> if this map contains a mapping for the specified
     * key.  More formally, returns <tt>true</tt> if and only if
     * this map contains a mapping for a key <tt>k</tt> such that
     * <tt>(key==null ? k==null : key.equals(k))</tt>.  (There can be
     * at most one such mapping.)
     *
     * @param key key whose presence in this map is to be tested
     * @return <tt>true</tt> if this map contains a mapping for the specified
     *         key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map (optional)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys (optional)
     */
    @Override
    public boolean containsKey(Object key) {
        return get(key) != null;
    }


    /**
     * Copies all of the mappings from the specified map to this map
     * (optional operation).  The effect of this call is equivalent to that
     * of calling {@link #put(Object, Object) put(k, v)} on this map once
     * for each mapping from key <tt>k</tt> to value <tt>v</tt> in the
     * specified map.  The behavior of this operation is undefined if the
     * specified map is modified while the operation is in progress.
     *
     * @param m mappings to be stored in this map
     * @throws NullPointerException          if the specified map is null, or if
     *                                       this map does not permit null keys or values, and the
     *                                       specified map contains null keys or values
     * @throws IllegalArgumentException      if some property of a key or value in
     *                                       the specified map prevents it from being stored in this map
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for(Entry<? extends K, ? extends V> entry : m.entrySet()) {
            put(entry.getKey(),entry.getValue());
        }
    }

    /**
     * Unsupported operation because of map size.
     */
    @Override
    public Set<K> keySet() {
        throw new UnsupportedOperationException("keySet is not supported because of map size.");
    }

    /**
     * Unsupported operation because of map size.
     */
    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("values is not supported because of map size.");
    }

    /**
     * Unsupported operation because of map size.
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("entrySet is not supported because of map size.");
    }
}
