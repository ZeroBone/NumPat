package net.zerobone.numpat;

import net.zerobone.numpat.dfa.DFA;
import net.zerobone.numpat.math.Euklidian;
import net.zerobone.numpat.unionfind.UnionFind;

import java.util.HashSet;
import java.util.List;

public class NumPat {

    public static void main(String[] args) {

        for (int m = 1; m <= 10; m++) {
            run(2, m);
        }

    }

    private static List<List<Integer>> computeEquivalenceClasses(int b, int m) {

        HashSet<Integer> finalStates = new HashSet<>();

        finalStates.add(0);

        if (m > b) {
            System.err.print("Can be incorrect: ");
        }

        UnionFind equivalenceClasses = new UnionFind(m);

        int modulo = m / Euklidian.gcd(b, m);

        for (int n = 0; n < m; n++) {

            int current = n;

            boolean stateFinal = finalStates.contains(n);

            for (;;) {
                current = (current + modulo) % m;

                if (finalStates.contains(current) != stateFinal) {
                    // break;
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

        DFA dfa = new DFA(modulo, base, 0, 0);

        for (int state = 0; state < modulo; state++) {

            for (int input = 0; input < base; input++) {

                int newState = (state * base + input) % modulo;

                // System.out.println(state + " ---"+input+"---> " + newState);

                dfa.setTransition(state, input, newState);

            }

        }

        System.out.println("--------------------------");

        // dfa.minimize();

        List<List<Integer>> correctStateClasses = dfa.computeEquivalenceClasses();
        List<List<Integer>> stateClasses = computeEquivalenceClasses(base, modulo);

        System.out.printf("Modulo: %3d Equivalence classes: %3d -> %s\n", modulo, stateClasses.size(), stateClasses);
        System.out.printf("Modulo: %3d Equivalence classes: %3d -> %s\n", modulo, correctStateClasses.size(), correctStateClasses);

        System.out.println(dfa.toRegexp());

        System.out.println(dfa.toString());

    }

}