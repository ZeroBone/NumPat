package net.zerobone.numpat.dfa;

import net.zerobone.numpat.regexp.IRegExp;

import java.util.*;

public class DFA {

    public final int alphabetSize;

    private int[] transitions;

    private int initialState;

    private HashSet<Integer> finalStates;

    public DFA(int stateCount, int alphabetSize, int initialState, int oneFinalState) {

        assert stateCount >= 1;
        assert alphabetSize >= 1;

        this.alphabetSize = alphabetSize;
        this.initialState = initialState;

        this.transitions = new int[stateCount * alphabetSize];

        finalStates = new HashSet<>();

        finalStates.add(oneFinalState);

    }

    public int getInitialState() {
        return initialState;
    }

    public void addFinalState(int finalState) {
        finalStates.add(finalState);
    }

    public void setTransition(int state, int input, int targetState) {

        assert state >= 0;
        assert state < getStateCount();

        assert input >= 0;
        assert input < alphabetSize;

        transitions[state * alphabetSize + input] = targetState;

    }

    public int getTransition(int state, int input) {

        assert state >= 0;
        assert state < getStateCount();

        assert input >= 0;
        assert input < alphabetSize;

        return transitions[state * alphabetSize + input];

    }

    private int combineStates(HashSet<Integer> states) {

        assert !states.isEmpty();
        assert states.size() > 1;

        int newState = Integer.MAX_VALUE;

        for (int s : states) {
            if (s < newState) {
                newState = s;
            }
        }

        for (int i = 0; i < transitions.length; i++) {

            int targetState = transitions[i];

            if (states.contains(targetState)) {
                transitions[i] = newState;
            }

        }

        if (states.contains(initialState)) {
            initialState = newState;
        }

        return newState;

    }

    public void combineStateClasses(List<List<Integer>> stateClasses) {

        TreeSet<Integer> statesToBeEliminated = new TreeSet<>();

        for (List<Integer> stateClass : stateClasses) {

            if (stateClass.size() <= 1) {
                continue;
            }

            int newState = combineStates(new HashSet<>(stateClass));

            statesToBeEliminated.addAll(stateClass);

            statesToBeEliminated.remove(newState);

        }

        // System.out.println("To eliminate: " + statesToBeEliminated);

        if (statesToBeEliminated.isEmpty()) {
            // nothing to do
            return;
        }

        Iterator<Integer> it = statesToBeEliminated.descendingIterator();

        assert it.hasNext();

        int previousState = it.next();

        int lastState = previousState;

        while (it.hasNext()) {

            int currentState = it.next();

            assert currentState < previousState;

            if (currentState == previousState - 1) {
                // descending block of states
                previousState = currentState;
                continue;
            }

            eliminateStateBlock(previousState, lastState);

            previousState = currentState;

            lastState = currentState;

        }

        eliminateStateBlock(previousState, lastState);

        transitions = Arrays.copyOf(transitions, transitions.length - statesToBeEliminated.size() * alphabetSize);

    }

    private void eliminateStateBlock(int from, int to) {

        assert to >= from;

        // System.out.println("Eliminating states between " + from + " and " + to + "...");

        int delta = to - from + 1;

        {
            int shiftStart = to + 1;

            int shiftLength = transitions.length - shiftStart * alphabetSize;

            assert shiftLength >= 0;

            if (shiftLength != 0) {

                System.arraycopy(
                    transitions,
                    shiftStart * alphabetSize,
                    transitions,
                    from * alphabetSize,
                    shiftLength
                );

            }

        }

        int maxEntry = (to + 1) * alphabetSize;

        assert maxEntry <= transitions.length;

        for (int i = 0; i < maxEntry; i++) {

            assert transitions[i] < from || transitions[i] > to;

            if (transitions[i] > to) {
                transitions[i] -= delta;
            }

        }

    }

    public List<List<Integer>> computeEquivalenceClasses() {
        return DfaMinimization.computeEquivalentStateClasses(this);
    }

    public void minimize() {
        combineStateClasses(computeEquivalenceClasses());
    }

    public IRegExp toRegexp() {
        return Kleene.dfaToRegex(this);
    }

    public boolean isFinalState(int state) {
        return finalStates.contains(state);
    }

    public int getStateCount() {
        return transitions.length / alphabetSize;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append("      | ");

        for (int input = 0; input < alphabetSize; input++) {

            sb.append(String.format("%5d", input));

        }

        sb.append('\n');
        sb.append("----- | ");

        for (int input = 0; input < alphabetSize; input++) {

            sb.append("-----");

        }

        sb.append('\n');

        for (int s = 0; s < getStateCount(); s++) {

            sb.append(String.format("%5d", s));
            sb.append(" | ");

            for (int input = 0; input < alphabetSize; input++) {

                sb.append(String.format("%5d", getTransition(s, input)));

            }

            sb.append('\n');

        }

        return sb.toString();

    }
}