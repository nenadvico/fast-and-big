package org.fast.map;

/**
 * Map that could store large number of entries. More then 67 millions of entries in 1 GB.
 * @author Nenad.Vico.
 */
public class StringLongHashMap extends HashMap<String,Long> {


    private static final int ROW_SIZE = 4; // 2 key + 1 value  (1 is address)
    private int[] data;

    /**
     * Constructor with maximum expected map size.
     * @param size maximum expected map size.
     */
    public StringLongHashMap(int size) {
        super(size);
    }

    @Override
    protected void initData(int size) {
        data = new int[size];
    }

    @Override
    protected int getRowSize() {
        return ROW_SIZE;
    }

    /**
     * Returns <tt>true</tt> if this map maps one or more keys to the
     * specified value.  More formally, returns <tt>true</tt> if and only if
     * this map contains at least one mapping to a value <tt>v</tt> such that
     * <tt>(value==null ? v==null : value.equals(v))</tt>.  This operation
     * will probably require time linear in the map size for most
     * implementations of the <tt>Map</tt> interface.
     *
     * @param value value whose presence in this map is to be tested
     * @return <tt>true</tt> if this map maps one or more keys to the
     *         specified value
     * @throws ClassCastException   if the value is of an inappropriate type for
     *                              this map (optional)
     * @throws NullPointerException if the specified value is null and this
     *                              map does not permit null values (optional)
     */
    @Override
    public boolean containsValue(Object value) {
        long val = (Long)value;
        int val1 = (int)val;
        int len = DEFAULT_HASH_SIZE*fastValue;
        int bSize = backetSize/fastValue;
        for (int address=0; address<len ; address++) {
            int start = ROW_SIZE*bSize*address;
            for (int i=0; i< bSize; i++) {
                int index = start + ROW_SIZE*i;
                if (data[index+2] == 0 ) { // detects if rest is empty
                    break;
                } else if (data[index+2] == val1) {
                    return true;
                }
            }
        }
        return over.containsValue(value);
    }

    /**
     * Returns the value to which the specified key is mapped,
     * or {@code null} if this map contains no mapping for the key.
     * <p/>
     * <p>More formally, if this map contains a mapping from a key
     * {@code k} to a value {@code v} such that {@code (key==null ? k==null :
     * key.equals(k))}, then this method returns {@code v}; otherwise
     * it returns {@code null}.  (There can be at most one such mapping.)
     * <p/>
     * <p>If this map permits null values, then a return value of
     * {@code null} does not <i>necessarily</i> indicate that the map
     * contains no mapping for the key; it's also possible that the map
     * explicitly maps the key to {@code null}.  The {@link #containsKey
     * containsKey} operation may be used to distinguish these two cases.
     *
     * @param key the key whose associated value is to be returned
     * @return the value to which the specified key is mapped, or
     *         {@code null} if this map contains no mapping for the key
     * @throws ClassCastException   if the key is of an inappropriate type for
     *                              this map (optional)
     * @throws NullPointerException if the specified key is null and this map
     *                              does not permit null keys (optional)
     */
    @Override
    public Long get(Object key) {
        long [] h = hash((String)key);
        int address = (int)h[0];
        long shortKey = h[1];
        int key1 = (int)shortKey;
        int key2 = (int)(shortKey >>> 32);
        int bSize = backetSize/fastValue;
        int start = ROW_SIZE*bSize*address;
        for (int i=0; i< bSize; i++) {
            int index = start + ROW_SIZE*i;
            if (data[index+2] == 0) { // detects if rest is empty
                if (removeable) {
                    return over.get(key);
                }
                return null;
            } else if (data[index] == key1 && data[index+1] == key2) {
                long val = ((long) data[index + 3] << 32) + data[index+2];
                return val;
            }
        }
        return over.get(key);
    }

    /**
     * Associates the specified value with the specified key in this map
     * (optional operation).  If the map previously contained a mapping for
     * the key, the old value is replaced by the specified value.  (A map
     * <tt>m</tt> is said to contain a mapping for a key <tt>k</tt> if and only
     * if {@link #containsKey(Object) m.containsKey(k)} would return
     * <tt>true</tt>.)
     *
     * @param key   key with which the specified value is to be associated
     * @param value value to be associated with the specified key
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     *         (A <tt>null</tt> return can also indicate that the map
     *         previously associated <tt>null</tt> with <tt>key</tt>,
     *         if the implementation supports <tt>null</tt> values.)
     * @throws NullPointerException          if the specified key or value is null
     *                                       and this map does not permit null keys or values
     */
    @Override
    public Long put(String key, Long value) {
        long val = value;
        int val1 = (int)val;
        int val2 = (int)(val >>> 32);
        long [] h = hash(key);
        int address = (int)h[0];
        long shortKey = h[1];
        int key1 = (int)shortKey;
        int key2 = (int)(shortKey >>> 32);
        int bSize = backetSize/fastValue;
        int start = ROW_SIZE*bSize*address;
        for (int i=0; i< bSize; i++) {
            int index = start + ROW_SIZE*i;
            if (data[index+2] == 0) { // detects if rest is empty
                data[index] = key1;
                data[index+1] = key2;
                data[index+2] = val1;
                data[index+3] = val2;
                count++;
                return null;
            } else if (data[index] == key1 && data[index+1] == key2) {
                long oldValue = ((long) data[index + 3] << 32) + data[index+2];
                data[index+2] = val1;
                data[index+3] = val2;
                return oldValue;
            }
        }
        return over.put(key, value);
    }

    /**
     * Removes the mapping for a key from this map if it is present
     * (optional operation).   More formally, if this map contains a mapping
     * from key <tt>k</tt> to value <tt>v</tt> such that
     * <code>(key==null ?  k==null : key.equals(k))</code>, that mapping
     * is removed.  (The map can contain at most one such mapping.)
     * <p/>
     * <p>Returns the value to which this map previously associated the key,
     * or <tt>null</tt> if the map contained no mapping for the key.
     * <p/>
     * <p>If this map permits null values, then a return value of
     * <tt>null</tt> does not <i>necessarily</i> indicate that the map
     * contained no mapping for the key; it's also possible that the map
     * explicitly mapped the key to <tt>null</tt>.
     * <p/>
     * <p>The map will not contain a mapping for the specified key once the
     * call returns.
     *
     * @param key key whose mapping is to be removed from the map
     * @return the previous value associated with <tt>key</tt>, or
     *         <tt>null</tt> if there was no mapping for <tt>key</tt>.
     * @throws UnsupportedOperationException if the <tt>remove</tt> operation
     *                                       is not supported by this map
     * @throws ClassCastException            if the key is of an inappropriate type for
     *                                       this map (optional)
     * @throws NullPointerException          if the specified key is null and this
     *                                       map does not permit null keys (optional)
     */
    @Override
    public Long remove(Object key) {
        if (!removeable) {
            throw new UnsupportedOperationException("remove is not supported because of removeable flag is false");
        }
        long [] h = hash((String)key);
        int address = (int)h[0];
        long shortKey = h[1];
        int key1 = (int)shortKey;
        int key2 = (int)(shortKey >>> 32);
        int bSize = backetSize/fastValue;
        int start = ROW_SIZE*bSize*address;
        for (int i=0; i< bSize; i++) {
            int index = start + ROW_SIZE*i;
            if (data[index+2] == 0) { // && data[index+4] == 0) {
                break;
            } else if (data[index] == key1 && data[index+1] == key2) {
                long value = ((long) data[index + 3] << 32) + data[index+2];
                data[index+2] = 0;
                count--;
                for (int j=i+1; j<backetSize; j++) {
                    index = start + ROW_SIZE*(j-1);
                    int moveIndex = start + ROW_SIZE*j;
                    if (data[moveIndex+2] == 0) {
                        break;
                    } else { // move data because of empty detection in get method
                        data[index] = data[moveIndex];
                        data[index+1] = data[moveIndex+1];
                        data[index+2] = data[moveIndex+2];
                        data[index+3] = data[moveIndex+3];
                    }

                }
                return value;
            }
        }
        return over.remove(key);
    }

    /**
     * Removes all of the mappings from this map (optional operation).
     * The map will be empty after this call returns.
     */
    @Override
    public void clear() {
        int len = DEFAULT_HASH_SIZE*fastValue;
        int bSize = backetSize/fastValue;
        for (int address=0; address<len ; address++) {
            int start = ROW_SIZE*bSize*address;
            for (int i=0; i< bSize; i++) {
                int index = start + ROW_SIZE*i;
                if (data[index+2] == 0) { // && data[index+4] == 0) { // detects if rest is empty
                    break;
                } else {
                    data[index+2] = 0;
                }
            }
        }
        over.clear();
        count = 0;
    }

    private long[] hash(String string) {
        long[] result = new long[2];
        long address = 0;
        long key = 0;
        int len = string.length();
        for (int i = 0; i < len; i++) {
            address = 11*address + string.charAt(i);
            long temp = address >>> (16+fastBits);
            key = 11*key + temp;
            address &= mask;
        }
        result[0] = address;
        result[1] = key;
        return result;
    }

}
