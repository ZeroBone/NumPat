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

    /*private static List<List<Integer>> computeEquivalenceClassesNew(int b, int m) {

        HashSet<Integer> finalStates = new HashSet<>();

        finalStates.add(0);

        UnionFind equivalenceClasses = new UnionFind(m);

        int modulo;

        if (m <= b) {
            modulo = m / Euklidian.gcd(b, m);
        }
        else {

            modulo = m;

            for (int i = 1; i <= m; i++) {

                int term = m / Euklidian.gcdPower(b, i, m);

                modulo = Euklidian.lcm(modulo, term);

            }

            System.out.println("Equivalence classes modulo: " + modulo);

        }

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

    }*/

    private static List<List<Integer>> computeEquivalenceClasses(int b, int m) {

        if (m > b) {
            System.out.println("Possible wrong answer.");
        }

        HashSet<Integer> finalStates = new HashSet<>();

        finalStates.add(0);

        UnionFind equivalenceClasses = new UnionFind(m);

        /*if (Euklidian.gcd(b, m) == 1) {
            return equivalenceClasses.getDisjointsSets();
        }*/

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
        List<List<Integer>> computedStateClasses = computeEquivalenceClasses(base, modulo);

        {

            HashSet<HashSet<Integer>> correctSetOfSets = new HashSet<>();
            HashSet<HashSet<Integer>> computedSetOfSets = new HashSet<>();

            for (List<Integer> correctClass : correctStateClasses) {
                correctSetOfSets.add(new HashSet<>(correctClass));
            }

            for (List<Integer> computedClass : computedStateClasses) {
                computedSetOfSets.add(new HashSet<>(computedClass));
            }

            if (!correctSetOfSets.equals(computedSetOfSets)) {
                System.err.println("ERROR FOUND!");
            }
            else {
                System.out.println("Everything correct.");
            }

        }

        System.out.printf("Computed: Modulo: %3d Equivalence classes: %3d -> %s\n", modulo, computedStateClasses.size(), computedStateClasses);
        System.out.printf("Correct:  Modulo: %3d Equivalence classes: %3d -> %s\n", modulo, correctStateClasses.size(), correctStateClasses);

        dfa.minimize(correctStateClasses);

        System.out.println(dfa.toRegexp());

        // System.out.println(dfa.toString());

    }

}