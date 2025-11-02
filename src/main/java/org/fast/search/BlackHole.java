package org.fast.search;

public class BlackHole {

    private final byte[] horizon;
    private final int bits;
    private final int mass;
    private final long mask;
    private int count;
    private int redundant;

    /**
     * Number of bits define size of object as 2 ^ (bits - 3) bytes and result size of hash function.
     * @param bits number of bits in range [3-34]
     */
    public BlackHole(int bits) {
        if (bits > 34) {
            throw new IllegalArgumentException("Maximum bits is 34.");
        }
        if (bits < 3) {
            throw new IllegalArgumentException("Minimum bits is 3.");
        }
        this.bits = bits;
        mass = 0b1 << (bits - 3);
        mask = (0b1L << bits) - 1;
        horizon = new byte[mass];
    }

    /**
     * Convert string to hash code and push it in event horizon.
     * @param s string to absorb.
     */
    public void absorb(String s) {
        long hash = gravity(s);
        int address = (int) (hash & mask) >>> 3;
        int pos = (int) (hash & 0b111);
        byte value = (byte) (0b1 << pos);
        byte old = horizon[address];
        horizon[address] = (byte) (value | old);
        byte exist = (byte) (old & (byte) (0b1 << pos));
        count++;
        if (exist != 0) {
            redundant++;
        }
    }

    /**
     * Checks if string hash code exists in event horizon.
     * @param s string to check.
     */
    public boolean radiation(String s) {
        long hash = gravity(s);
        int address = (int) (hash & mask) >>> 3;
        int pos = (int) (hash & 0b111);
        byte value = horizon[address];
        byte exist = (byte) (value & (byte) (0b1 << pos));
        return exist != 0;
    }

    /**
     * Clears black hole and its event horizon.
     */
    public void clear() {
        for (int i = 0; i < mass; i++) {
            horizon[i] = 0;
        }
        count = 0;
        redundant = 0;
    }

    /**
     * Gets number of bits of black hole.
     * @return returns number of bits of black hole.
     */
    public int getBits() {
        return bits;
    }

    /**
     * Gets size of black hole in bytes or its mass.
     * @return return size of black hole in bytes or its mass.
     */
    public int getMass() {
        return mass;
    }

    /**
     * Gets number of objects absorbed by black hole.
     * @return number of objects absorbed by black hole.
     */
    public int getCount() {
        return count;
    }

    /**
     * Gets number of redundant objects absorbed by black hole.
     * Two objects are redundant if produces same hash code.
     * @return number of redundant/repeating objects absorbed by black hole.
     */
    public int getRedundant() {
        return redundant;
    }

    public double getSaturation() {
        return (double) (count - redundant) / mass;
    }

    protected long gravity(String object) {
        long hash = 0;
        int len = object.length();
        if (len > 0) {
            for (int i = 0; i < len; i++) {
                hash = 31 * hash + object.charAt(i);
            }
        }
        return hash;
    }
}