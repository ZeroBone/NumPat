package net.zerobone.numpat.dfa;

import net.zerobone.numpat.unionfind.UnionFind;

import java.util.*;

public class DfaMinimization {

    private DFA dfa;

    private HashSet<StatePair> unmarkedPairs = new HashSet<>();

    private HashMap<StatePair, HashSet<StatePair>> implications = new HashMap<>();

    private DfaMinimization(DFA dfa) {
        this.dfa = dfa;
    }

    private void mark(StatePair sp) {

        HashSet<StatePair> visited = new HashSet<>();

        Queue<StatePair> queue = new LinkedList<>();

        queue.add(sp);

        visited.add(sp);

        do {

            StatePair element = queue.poll();

            unmarkedPairs.remove(element);

            visited.add(element);

            HashSet<StatePair> mapping = implications.get(element);

            if (mapping != null) {
                queue.addAll(mapping);
            }

            queue.removeAll(visited);

        } while (!queue.isEmpty());

    }

    private void createImplicationRule(StatePair trigger, StatePair toMark) {

        assert unmarkedPairs.contains(trigger);

        HashSet<StatePair> toMarkSet = implications.get(trigger);

        if (toMarkSet == null) {

            toMarkSet = new HashSet<>();

            toMarkSet.add(toMark);

            implications.put(trigger, toMarkSet);

        }

        toMarkSet.add(toMark);

    }

    private List<List<Integer>> doComputeEquivalentStateClasses() {

        for (int s1 = 0; s1 < dfa.getStateCount(); s1++) {

            if (dfa.isFinalState(s1)) {
                continue;
            }

            // s1 is not a final state

            for (int s2 = 0; s2 < s1; s2++) {

                if (dfa.isFinalState(s2)) {
                    continue;
                }

                unmarkedPairs.add(new StatePair(s1, s2));

            }

        }

        Queue<StatePair> queue = new LinkedList<>(unmarkedPairs);

        while (!queue.isEmpty()) {

            StatePair currentPair = queue.poll();

            if (!unmarkedPairs.contains(currentPair)) {
                // current pair already marked
                continue;
            }

            for (int input = 0; input < dfa.alphabetSize; input++) {

                int first = dfa.getTransition(currentPair.first, input);
                int second = dfa.getTransition(currentPair.second, input);

                if (first == second) {
                    continue;
                }

                StatePair newPair = new StatePair(first, second);

                if (!unmarkedPairs.contains(newPair)) {
                    // this pair is marked
                    // so we need to also mark statePair
                    // because newPair is a pair of non-equivalent states
                    mark(currentPair);
                }
                else {
                    createImplicationRule(newPair, currentPair);
                }

            }

        }

        // System.out.println(unmarkedPairs);

        return computeOrbits();

    }

    private List<List<Integer>> computeOrbits() {

        UnionFind orbits = new UnionFind(dfa.getStateCount());

        for (StatePair currentPair : unmarkedPairs) {

            orbits.union(currentPair.first, currentPair.second);

        }

        return orbits.getDisjointsSets();

    }

    public static List<List<Integer>> computeEquivalentStateClasses(DFA dfa) {
        return new DfaMinimization(dfa).doComputeEquivalentStateClasses();
    }

}