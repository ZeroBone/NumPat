package net.zerobone.numpat.math;

public class Modulo {

    private Modulo() {}

    public static int aPowBmodN(int a, int b, int n) {

        assert a >= 0;
        assert b >= 0;
        assert n >= 1;

        int result = 1;

        while (b != 0) {

            if (b % 2 == 1) {
                // b is odd
                result = (result * (a % n)) % n;
            }

            a = ((a % n) * (a % n)) % n;

            b >>= 1;

        }

        return result;

    }

}