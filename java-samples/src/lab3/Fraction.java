package lab3;

public class Fraction {
    int numerator;
    int denominator;

    //default constructor
    Fraction() {
        this.numerator = 1;
        this.denominator = 1;
    }

    //non-default constructor
    Fraction(int numerator, int denominator) {
        this.numerator = numerator;
        this.denominator = denominator;
    }

    //return the fraction as a string
    @Override
    public String toString() {
        return numerator + "/" + denominator;
    }

    //return the decimal value of the fraction
    double toDecimal() {
        return numerator / (double) denominator;
    }

    //add two fractions and return the result to a fraction in the lowest terms
    Fraction add(Fraction other) {
        int newNumerator = this.numerator * other.denominator + other.numerator * this.denominator;
        int newDenominator = this.denominator * other.denominator;
        int gcd = findGCD(Math.abs(newNumerator), Math.abs(newDenominator));
        newNumerator /= gcd;
        newDenominator /= gcd;
        return new Fraction(newNumerator, newDenominator);
    }

    //find the greatest common divisor (GCD)
    int findGCD(int n, int d) {
        if (n == 0) return 1;
        if (d == 0) return n;
        return findGCD(d, n % d);
    }
}
