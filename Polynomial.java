public class Polynomial {
    static double EPSILON = 1e-6; // used when checking if a value is a root
    double[] coeff;
    Polynomial() {
        coeff = new double[]{0};
    }
    Polynomial(double[] coeff) {
        this.coeff = coeff.clone();
    }
    Polynomial add(Polynomial other) {
        Polynomial sm = new Polynomial();
        sm.coeff = new double[Math.max(this.coeff.length, other.coeff.length)];
        for(int i = 0; i < this.coeff.length; i++) sm.coeff[i] += this.coeff[i];
        for(int i = 0; i < other.coeff.length; i++) sm.coeff[i] += other.coeff[i];
        return sm;
    }
    double evaluate(double x) {
        double X = 1;
        double res = 0;
        for(int i = 0; i < coeff.length; i++){
            res += X * coeff[i];
            X *= x;
        }
        return res;
    }
    boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < EPSILON;
    }
}