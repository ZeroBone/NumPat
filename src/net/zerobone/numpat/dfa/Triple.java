package net.zerobone.numpat.dfa;

import java.util.Objects;

public final class Triple {

    public int i;

    public int j;

    public int k;

    public Triple(int i, int j, int k) {
        this.i = i;
        this.j = j;
        this.k = k;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triple triple = (Triple)o;
        return i == triple.i &&
            j == triple.j &&
            k == triple.k;
    }

    @Override
    public int hashCode() {
        return Objects.hash(i, j, k);
    }

}