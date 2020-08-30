package net.zerobone.numpat;

import net.zerobone.numpat.dfa.DFA;
import net.zerobone.numpat.math.Euklidian;
import net.zerobone.numpat.unionfind.UnionFind;

import java.util.HashSet;
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

    private static List<List<Integer>> computeEquivalenceClasses(int b, int m) {

        if (m > b) {
            return null;
        }

        HashSet<Integer> finalStates = new HashSet<>();

        finalStates.add(0);

        UnionFind equivalenceClasses = new UnionFind(m);

        if (Euklidian.gcd(b, m) == 1) {
            return equivalenceClasses.getDisjointsSets();
        }

        int modulo = m / Euklidian.gcd(b, m);

        for (int n = 0; n < m; n++) {

            int current = n;

            boolean stateFinal = finalStates.contains(n);

            for (;;) {
                current = (current + modulo) % m;

                if (finalStates.contains(current) != stateFinal) {
                    continue;
                }

                if (current == n) {
                    break;
                }

                equivalenceClasses.union(current, n);

            }

        }

        return equivalenceClasses.getDisjointsSets();

    }

    public static void run(int base, int modulo) {

        System.out.printf("Base: %3d Modulo: %3d\n", base, modulo);

        List<List<Integer>> equivalenceClasses = computeEquivalenceClasses(base, modulo);

        DFA dfa = new DFA(modulo, base, 0, 0);

        for (int state = 0; state < modulo; state++) {

            for (int input = 0; input < base; input++) {

                int newState = (state * base + input) % modulo;

                // System.out.println(state + " ---"+input+"---> " + newState);

                dfa.setTransition(state, input, newState);

            }

        }

        if (equivalenceClasses == null) {
            System.out.println("Computing equivalence classes manually with an O(n^2) algorithm.");
            equivalenceClasses = dfa.computeEquivalenceClasses();
        }

        dfa.minimize(equivalenceClasses);

        System.out.printf("Equivalence classes: %3d -> %s\n", equivalenceClasses.size(), equivalenceClasses);

        System.out.println(dfa.toRegexp());

        System.out.println("DFA:");
        System.out.println(dfa.toString());

    }

}