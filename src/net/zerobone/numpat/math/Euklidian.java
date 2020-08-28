package net.zerobone.numpat.math;

public class Euklidian {

    private Euklidian() {}

    public static int gcd(int a, int b) {

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

    public static int lcm(int a, int b) {
        return a / gcd(a, b) * b;
    }

}