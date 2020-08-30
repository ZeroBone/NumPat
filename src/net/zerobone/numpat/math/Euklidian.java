package net.zerobone.numpat.math;

public class Euklidian {

    private Euklidian() {}

    public static int gcd(int a, int b) {

        assert a >= 0;
        assert b >= 0;
        assert a > 0 || b > 0;

        while (true) {

            if (b == 0) {
                return a;
            }

            a = a % b;

            if (a == 0) {
                return b;
            }

            b = b % a;

        }

    }

    public static int gcdPower(int a, int ap, int b) {

        a = Modulo.aPowBmodN(a, ap, b);

        return gcd(b, a);

    }

    public static int lcm(int a, int b) {

        assert a > 0;
        assert b > 0;

        return a / gcd(a, b) * b;

    }

}