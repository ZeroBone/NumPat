package net.zerobone.numpat;

import net.zerobone.numpat.dfa.DFA;
import net.zerobone.numpat.math.Euklidian;
import net.zerobone.numpat.unionfind.UnionFind;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class NumPat {

    public static void main(String[] args) {

        if (args.length == 0) {
            run(10, 6);
            return;
        }

        if (args.length == 1) {
            run(10, Math.max(Integer.parseInt(args[0]), 1));
            return;
        }

        int base = Math.max(Integer.parseInt(args[0]), 2);
        int modulo = Math.max(Integer.parseInt(args[1]), 1);

        run(base, modulo);

    }

    private static List<List<Integer>> computeEquivalenceClasses(int b, int m, HashSet<Integer> finalStates) {

        if (m > b) {
            return null;
        }

        UnionFind equivalenceClasses = new UnionFind(m);

        int bmgcd = Euklidian.gcd(b, m);

        if (bmgcd == 1) {
            return equivalenceClasses.getDisjointSets();
        }

        int optimizedM = m / bmgcd;

        // loop through every set in the new set of equivalence classes
        for (int anchor = 0; anchor < optimizedM; anchor++) {

            int current = anchor;
            // oppositeAnchor is the element that is final if anchor is intermediate
            // and intermediate if anchor is final
            // if oppositeAnchor == anchor, then it is considered as not yet found
            int oppositeAnchor = anchor;

            boolean anchorFinal = finalStates.contains(anchor);

            for (;;) {

                current = (current + optimizedM) % m;

                if (current == anchor) {
                    break;
                }

                boolean currentFinal = finalStates.contains(current);

                if (currentFinal == anchorFinal) {
                    equivalenceClasses.union(current, anchor);
                }
                else if (oppositeAnchor == anchor) {
                    // "initialize" oppositeAnchor
                    oppositeAnchor = current;
                }
                else {
                    equivalenceClasses.union(current, oppositeAnchor);
                }

            }

        }

        return equivalenceClasses.getDisjointSets();

    }

    private static List<List<Integer>> computeEquivalenceClasses(int b, int m) {

        HashSet<Integer> finalStates = new HashSet<>();

        finalStates.add(0);

        return computeEquivalenceClasses(b, m, finalStates);

    }

    public static void run(int base, int modulo) {

        System.out.printf("Base: %3d Modulo: %3d\n", base, modulo);

        DFA dfa = new DFA(modulo, base, 0, 0);

        for (int state = 0; state < modulo; state++) {

            for (int input = 0; input < base; input++) {

                int newState = (state * base + input) % modulo;

                // System.out.println(state + " ---"+input+"---> " + newState);

                dfa.setTransition(state, input, newState);

            }

        }

        List<List<Integer>> equivalenceClasses = computeEquivalenceClasses(base, modulo);

        if (equivalenceClasses == null) {
            System.out.println("Computing equivalence classes manually with an O(n^2) algorithm.");
            equivalenceClasses = dfa.computeEquivalenceClasses();
        }

        System.out.printf("Equivalence classes: %3d -> %s\n", equivalenceClasses.size(), equivalenceClasses);
        System.out.println("Before minimization:");
        System.out.println(dfa.toString());

        dfa.minimize(equivalenceClasses);

        /*String regexp = dfa.toRegexp().toString();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter("regexp.txt"));
            writer.write(regexp);
            writer.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }*/

        System.out.println("DFA:");
        System.out.println(dfa.toString());

        try {
            exportDfaToFile(dfa, "dfa.txt");
            exportDfaToTTFile(dfa, "dfa-tt.txt");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void exportDfaToFile(DFA dfa, final String fileName) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        writer.write("#states");

        for (int i = 0; i < dfa.getStateCount(); i++) {
            writer.newLine();
            writer.write("s" + i);
        }

        writer.newLine();
        writer.write("#initial");
        writer.newLine();

        writer.write("s" + dfa.getInitialState());
        writer.newLine();

        writer.write("#accepting");

        for (Iterator<Integer> it = dfa.finalStatesIterator(); it.hasNext();) {
            int finalState = it.next();

            writer.newLine();
            writer.write("s" + finalState);

        }

        writer.newLine();

        writer.write("#alphabet");

        for (int i = 0; i < dfa.alphabetSize; i++) {
            writer.newLine();
            writer.write(Integer.toString(i, 16));
        }

        writer.newLine();

        writer.write("#transitions");

        for (int state = 0; state < dfa.getStateCount(); state++) {

            for (int input = 0; input < dfa.alphabetSize; input++) {

                int targetState = dfa.getTransition(state, input);

                writer.newLine();
                writer.write("s" + state + ":" + Integer.toString(input, 16) + ">s" + targetState);

            }

        }

        writer.close();

    }

    private static void exportDfaToTTFile(DFA dfa, final String fileName) throws IOException {

        BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));

        writer.write("inputs ");

        {
            int i = 0;
            while (true) {
                writer.write("i" + Integer.toString(i, 16));

                i++;

                if (i >= dfa.alphabetSize) {
                    break;
                }

                writer.write(",");

            }
        }

        writer.write(";");
        writer.newLine();

        writer.write("outputs ;");
        writer.newLine();

        writer.write("init " + dfa.getInitialState() + ";");
        writer.newLine();

        writer.write("transitions");

        for (int state = 0; state < dfa.getStateCount(); state++) {

            for (int input = 0; input < dfa.alphabetSize; input++) {

                int targetState = dfa.getTransition(state, input);

                writer.newLine();
                writer.write("    ");
                writer.write("(" + state + ",i" + Integer.toString(input, 16) + "," + targetState + ");");

            }

        }

        writer.newLine();
        writer.write("accept ");

        for (Iterator<Integer> it = dfa.finalStatesIterator();;) {
            int finalState = it.next();

            writer.write(String.valueOf(finalState));

            if (!it.hasNext()) {
                break;
            }

            writer.write(",");

        }

        writer.write(";");

        writer.close();

    }

}