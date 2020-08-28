package net.zerobone.numpat.dfa;

import java.util.Objects;

public class StatePair {

    public int first;

    public int second;

    public StatePair(int first, int second) {

        assert first != second;

        if (first < second) {
            this.first = first;
            this.second = second;
        }
        else {
            this.first = second;
            this.second = first;
        }

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatePair statePair = (StatePair)o;
        return first == statePair.first &&
            second == statePair.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }

}