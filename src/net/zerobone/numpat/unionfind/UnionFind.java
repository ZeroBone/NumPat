package net.zerobone.numpat.unionfind;

import java.util.*;

public class UnionFind {

    private RankPointer[] pointers;

    private int length;

    public UnionFind(int length) {

        assert length > 0;

        this.length = length;

        pointers = new RankPointer[length];

        for (int i = 0; i < length; i++) {
            pointers[i] = new RankPointer(i, 0);
        }

    }

    private int findRoot(int x) {

        if (pointers[x].parent != x) {
            pointers[x].parent = findRoot(pointers[x].parent);
        }

        return pointers[x].parent;

    }

    public List<Integer> getElementsInSet(int a) {

        int root = findRoot(a);

        return Collections.unmodifiableList(pointers[root].elements);

    }

    public List<List<Integer>> getDisjointSets() {

        ArrayList<List<Integer>> disjontsSets = new ArrayList<>();

        HashSet<Integer> remainingElements = new HashSet<>();

        for (int i = 0; i < length; i++) {
            remainingElements.add(i);
        }

        do {

            int element = remainingElements.iterator().next();

            List<Integer> disjointSet = getElementsInSet(element);

            remainingElements.removeAll(disjointSet);

            disjontsSets.add(disjointSet);

        } while (!remainingElements.isEmpty());

        return Collections.unmodifiableList(disjontsSets);

    }

    public void union(int a, int b) {

        int aRoot = findRoot(a);
        int bRoot = findRoot(b);

        if (aRoot == bRoot) {
            // same disjoint set
            return;
        }

        if (pointers[aRoot].rank > pointers[bRoot].rank) {

            // make b set child of a set
            pointers[bRoot].parent = aRoot;

            pointers[aRoot].elements.addAll(pointers[bRoot].elements);
            pointers[bRoot].elements = null;

        }
        else if (pointers[aRoot].rank < pointers[bRoot].rank) {

            // make a set child of b set
            pointers[aRoot].parent = bRoot;

            pointers[bRoot].elements.addAll(pointers[aRoot].elements);
            pointers[aRoot].elements = null;

        }
        else {

            // make a set child of b set
            // and increase rank of b
            pointers[aRoot].parent = bRoot;
            pointers[bRoot].rank = pointers[bRoot].rank + 1;

            pointers[bRoot].elements.addAll(pointers[aRoot].elements);
            pointers[aRoot].elements = null;

        }

    }

}