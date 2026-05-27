public class Polynomial {
    static double EPSILON = 1e-6; // used when checking if a value is a root
    double[] coeff;
    int[] exps;
    Polynomial() {
        coeff = new double[]{0};
        exps = new int[]{0};
    }
    Polynomial(double[] coeff, int[] exps) {
        this.coeff = coeff.clone();
        this.exps = exps.clone();
    }
    Polynomial add(Polynomial other) {
        int maxd = Math.max(this.exps[this.exps.length-1], other.exps[other.exps.length-1]);
        double[] cosum = new double[maxd+1];
        for(int i = 0; i < this.coeff.length; i++) cosum[this.exps[i]] += this.coeff[i];
        for(int i = 0; i < other.coeff.length; i++) cosum[other.exps[i]] += other.coeff[i];

        int tot = 0;
        for(int d = 0; d <= maxd; d++) tot += (Math.abs(cosum[d]) >= EPSILON) ? 1 : 0;
        tot = Math.max(tot, 1); // ensure we have the 'trivial' 0x^0 polynomial if there's nothing
        Polynomial sm = new Polynomial();
        sm.coeff = new double[tot];
        sm.exps = new int[tot];
        int cur = 0;
        for(int d = 0; d <= maxd; d++) if(Math.abs(cosum[d]) >= EPSILON){
            sm.coeff[cur] = cosum[d];
            sm.exps[cur] = d;
            cur++;
        }
        return sm;
    }
    Polynomial multiply(Polynomial other){
        Polynomial res = new Polynomial();
        for(int i = 0; i < coeff.length; i++){
            Polynomial cur = new Polynomial(other.coeff, other.exps);
            for(int j = 0; j < cur.coeff.length; j++){
                cur.coeff[j] *= coeff[i];
                cur.exps[j] += exps[i];
            }
            res = res.add(cur);
        }
        return res;
    }
    double evaluate(double x) {
        double X = 1;
        int cur = 0;
        double res = 0;
        for(int i = 0; i < coeff.length; i++){
            while(cur < exps[i]){
                X *= x;
                cur++;
            }
            res += X * coeff[i];
        }
        return res;
    }
    boolean hasRoot(double x) {
        return Math.abs(evaluate(x)) < EPSILON;
    }
}