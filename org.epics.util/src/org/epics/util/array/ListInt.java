/*
 * Copyright 2011 Brookhaven National Laboratory
 * All rights reserved. Use is subject to license terms.
 */
package org.epics.util.array;

/**
 * An ordered collection of {@code int}s.
 *
 * @author Gabriele Carcassi
 */
public abstract class ListInt implements ListNumber, CollectionInt {

    @Override
    public IteratorInt iterator() {
        return new IteratorInt() {
            
            private int index;

            @Override
            public boolean hasNext() {
                return index < size();
            }

            @Override
            public int nextInt() {
                return getInt(index++);
            }
        };
    }

    @Override
    public double getDouble(int index) {
        return (float) getInt(index);
    }

    @Override
    public float getFloat(int index) {
        return (float) getInt(index);
    }

    @Override
    public long getLong(int index) {
        return (long) getInt(index);
    }

    @Override
    public short getShort(int index) {
        return (short) getInt(index);
    }

    @Override
    public byte getByte(int index) {
        return (byte) getInt(index);
    }
    @Override
    public void setDouble(int index, double value) {
        setInt(index, (int) value);
    }

    @Override
    public void setFloat(int index, float value) {
        setInt(index, (int) value);
    }

    @Override
    public void setLong(int index, long value) {
        setInt(index, (int) value);
    }

    @Override
    public void setInt(int index, int value) {
        throw new UnsupportedOperationException("Read only list.");
    }

    @Override
    public void setShort(int index, short value) {
        setInt(index, (int) value);
    }

    @Override
    public void setByte(int index, byte value) {
        setInt(index, (int) value);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        
        if (obj instanceof ListInt) {
            ListInt other = (ListInt) obj;

            if (size() != other.size())
                return false;

            for (int i = 0; i < size(); i++) {
                if (getInt(i) != other.getInt(i))
                    return false;
            }

            return true;
        }
        
        return false;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size(); i++) {
            result = 31 * result + getInt(i);
        }
        return result;
    }
    
}
