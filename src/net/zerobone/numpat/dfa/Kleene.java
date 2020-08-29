package net.zerobone.numpat.dfa;

import net.zerobone.numpat.regexp.*;

import java.util.ArrayList;
import java.util.HashMap;

public class Kleene {

    private DFA dfa;

    private HashMap<Triple, IRegExp> cache = new HashMap<>();

    private Kleene(DFA dfa) {
        this.dfa = dfa;
    }

    private IRegExp computeR(int i, int j, int k) {

        if (k == -1) {

            IRegExp r = OrRegExp.emptySet();

            if (i == j) {
                r = r.orWith(ConcatRegExp.epsilon());
            }

            for (int input = 0; input < dfa.alphabetSize; input++) {

                if (dfa.getTransition(i, input) == j) {

                    r = r.orWith(new TerminalRegExp(input + '0'));

                }

            }

            return r;

        }

        return r(i, j, k - 1).orWith(
            r(i, k, k - 1).concatWith(
                r(k, k, k - 1).repeat()
            ).concatWith(r(k, j, k - 1))
        );

    }

    private IRegExp r(int i, int j, int k) {

        {
            IRegExp s = cache.get(new Triple(i, j, k));

            if (s != null) {
                return s;
            }
        }

        IRegExp s = computeR(i, j, k);

        cache.put(new Triple(i, j, k), s);

        return s;

    }

    private IRegExp run() {

        return r(dfa.getInitialState(), dfa.getInitialState(), dfa.getStateCount() - 1);

    }

    public static IRegExp dfaToRegex(DFA dfa) {
        return new Kleene(dfa).run();
    }

}