package org.fast.map;

/**
 * Map that could store large number of entries. More then 100 millions of entries in 1 GB.
 * @author Nenad.Vico.
 */
public class LongIntHashMap extends HashMap<Long,Integer> {

    private static final int ROW_SIZE = 5; // 3 key + 2 value  (1 is address)
    private char[] data;

    /**
     * Constructor with maximum expected map size.
     * @param size maximum expected map size.
     */
    public LongIntHashMap(int size) {
        super(size);
    }

    @Override
    protected final void initData(int size) {
        data = new char[ROW_SIZE* DEFAULT_HASH_SIZE *backetSize];
    }

    @Override
    protected final int getRowSize() {
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
        int val = (Integer)value;
        int len = DEFAULT_HASH_SIZE*fastValue;
        int bSize = backetSize/fastValue;
        for (int address=0; address<len ; address++) {
            char value1 = (char)val;
            char value2 = (char)(val >>> 16);
            int start = ROW_SIZE*bSize*address;
            for (int i=0; i< bSize; i++) {
                int index = start + ROW_SIZE*i;
                if (data[index+3] == 0 && data[index+4] == 0 ) { // detects if rest is empty
                    break;
                } else if (data[index+3] == value1 && data[index+4] == value2) {
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
    public Integer get(Object key) {
        long k = (Long)key;
        int address = (int)(k & mask);
        long shortKey = k >>> (16+fastBits);
        char key1 = (char)shortKey;
        shortKey >>>= (16-fastBits);
        char key2 = (char)shortKey;
        shortKey >>>= 16;
        char key3 = (char)shortKey;
        int bSize = backetSize/fastValue;
        int start = ROW_SIZE*bSize*address;
        for (int i=0; i< bSize; i++) {
            int index = start + ROW_SIZE*i;
            if (data[index+3] == 0 && data[index+4] == 0) {
                if (removeable) {
                    return over.get(key);
                }
                return null;
            } else if (data[index] == key1 && data[index+1] == key2 && data[index+2] == key3) {
                int value = data[index+4];
                value <<= 16;
                value |= (int)data[index+3];
                return value;
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
    public Integer put(Long key, Integer value) {
        int val = value;
        int address = (int)(key & mask);
        long shortKey = key >>> (16+fastBits);
        char key1 = (char)shortKey;
        shortKey >>>= (16-fastBits);
        char key2 = (char)shortKey;
        shortKey >>>= 16;
        char key3 = (char)shortKey;
        char value1 = (char)val;
        char value2 = (char)(val >>> 16);
        int bSize = backetSize/fastValue;
        int start = ROW_SIZE*bSize*address;
        for (int i=0; i< bSize; i++) {
            int index = start + ROW_SIZE*i;
            if (data[index+3] == 0 && data[index+4] == 0) { // detects if rest is empty
                data[index] = key1;
                data[index+1] = key2;
                data[index+2] = key3;
                data[index+3] = value1;
                data[index+4] = value2;
                count++;
                return null;
            } else if (data[index] == key1 && data[index+1] == key2 && data[index+2] == key3) {
                int oldValue = data[index+4];
                oldValue <<= 16;
                oldValue |= (int)data[index+3];
                data[index+3] = value1;
                data[index+4] = value2;
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
    public Integer remove(Object key) {
        if (!removeable) {
            throw new UnsupportedOperationException("remove is not supported because of removeable flag is false");
        }
        long k = (Long)key;
        int address = (int)(k & mask);
        long shortKey = k >>> (16+fastBits);
        char key1 = (char)shortKey;
        shortKey >>>= (16-fastBits);
        char key2 = (char)shortKey;
        shortKey >>>= 16;
        char key3 = (char)shortKey;
        int bSize = backetSize/fastValue;
        int start = ROW_SIZE*bSize*address;
        for (int i=0; i< bSize; i++) {
            int index = start + ROW_SIZE*i;
            if (data[index+3] == 0 && data[index+4] == 0) {
                break;
            } else if (data[index] == key1 && data[index+1] == key2 && data[index+2] == key3) {
                int value = data[index+4];
                value <<= 16;
                value |= (int)data[index+3];
                data[index+3] = 0;
                data[index+4] = 0;
                count--;
                for (int j=i+1; j<backetSize; j++) {
                    index = start + ROW_SIZE*(j-1);
                    int moveIndex = start + ROW_SIZE*j;
                    if (data[moveIndex+3] == 0 && data[moveIndex+4] == 0) {
                        break;
                    } else { // move data because of empty detection in get method
                        data[index] = data[moveIndex];
                        data[index+1] = data[moveIndex+1];
                        data[index+2] = data[moveIndex+2];
                        data[index+3] = data[moveIndex+3];
                        data[index+4] = data[moveIndex+4];
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
                if (data[index+4] == 0) { // && data[index+4] == 0) { // detects if rest is empty
                    break;
                } else {
                    data[index+3] = 0;
                    data[index+4] = 0;
                }
            }
        }
        over.clear();
        count = 0;
    }
}
