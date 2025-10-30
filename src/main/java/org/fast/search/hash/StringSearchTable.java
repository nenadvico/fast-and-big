package org.fast.search.hash;

/**
 * @param <T> object type.
 * @author Nenad.Vico
 */
public class StringSearchTable extends LongHashSearchTable<String> {

    @Override
    protected long hashCode(String string) {
        long h = 0;
        int len = string.length();
        for (int i = 0; i < len; i++) {
            h = 11*h + string.charAt(i);
        }
        return h;
    }
}
