package org.fast.search.hash;

/**
 * @param <T> object type.
 * @author Nenad.Vico
 */
public class UltraStringSearchTable extends UltraHashSearchTable<String> {

    @Override
    protected long[] hashCode(String string) {
        long[] result = new long[2];
        long lo = 0;
        long hi = 0;
        int len = string.length();
        for (int i = 0; i < len; i++) {
            lo = 31*lo + string.charAt(i);
            long temp = lo >>> 60;
            hi = 31*hi + temp;
            lo &= 0xfffffffffffffffL;
        }
        result[0] = lo;
        result[1] = hi;
        return result;
    }
}
