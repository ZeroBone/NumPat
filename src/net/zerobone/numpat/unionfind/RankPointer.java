package net.zerobone.numpat.unionfind;

import java.util.ArrayList;

public class RankPointer {

    public int parent;

    public int rank;

    public ArrayList<Integer> elements; // null if this node is not a root node

    public RankPointer(int parent, int rank) {

        this.parent = parent;

        this.rank = rank;

        elements = new ArrayList<>();

        elements.add(parent);

    }

}