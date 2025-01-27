package lab3;

public class MixedFraction extends Fraction {
    int naturalNumber;

    //non-default constructor with one additional argument as natural number
    MixedFraction(int naturalNumber, int numerator, int denominator) {
        //call the constructor of Fraction to set numerator and denominator
        super(numerator, denominator);
        this.naturalNumber = naturalNumber;
    }

    //override toString() to return the number as N n/d
    @Override
    public String toString() {
        return naturalNumber + " " + super.toString(); // Combine the natural number with the Fraction toString()
    }

    //override toDecimal() to calculate the decimal value as (N * d + n) / d
    @Override
    double toDecimal() {
        return (naturalNumber * denominator + numerator) / (double) denominator;
    }

    //convert MixedFraction to regular Fraction
    Fraction toFraction() {
        int newNumerator = naturalNumber * denominator + numerator;
        return new Fraction(newNumerator, denominator);
    }

    //overloaded add for MixedFraction
    Fraction add(MixedFraction mf) {
        //convert both mixed fractions to regular fractions
        Fraction f1 = this.toFraction();
        Fraction f2 = mf.toFraction();
        return f1.add(f2);
    }
}
