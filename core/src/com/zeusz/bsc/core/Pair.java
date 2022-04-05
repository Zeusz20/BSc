package com.zeusz.bsc.core;

import java.util.Map;
import java.util.Objects;


public final class Pair<K, V> extends GWObject implements Map.Entry<K, V> {

    private static final long serialVersionUID = 3287917777L;

    private final K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() { return key; }

    @Override
    public V getValue() { return value; }

    @Override
    public V setValue(V value) {
        V oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public boolean equals(java.lang.Object obj) {
        if(this == obj) return true;
        if(obj instanceof Pair) {
            Pair other = (Pair) obj;
            return Objects.equals(this.key, other.key)
                    && Objects.equals(this.value, other.value);
        }
        return false;
    }

}
