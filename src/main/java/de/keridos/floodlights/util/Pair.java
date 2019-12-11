package de.keridos.floodlights.util;

import java.io.Serializable;
import java.util.Objects;

public class Pair<K,V> implements Serializable {

    private K first;
    private V second;

    public Pair(K first, V second) {
        this.first = first;
        this.second = second;
    }

    public K getFirst() {
        return first;
    }

    public V getSecond() {
        return second;
    }

    @Override
    public String toString() {
        return first + "=" + second;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o instanceof Pair) {
            Pair pair = (Pair) o;
            return Objects.equals(first, pair.first) && Objects.equals(second, pair.second);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return first.hashCode() * 13 + (second == null ? 0 : second.hashCode());
    }
}
