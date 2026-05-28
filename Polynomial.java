import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

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
    Polynomial(File f) throws FileNotFoundException {
        Scanner sc = new Scanner(f);
        readString(sc.next());
    }
    Polynomial(String str){
        readString(str);
    }
    void readString(String str){
        StringBuilder bld = new StringBuilder();
        for(char c:str.toCharArray()){
            if(!(Character.isDigit(c) || c == '+' || c == '-' || c == 'x'))continue;
            if(c == '-') bld.append("+-");
            else bld.append(c);
        }

        ArrayList<Double>Coeff = new ArrayList<>();
        ArrayList<Integer>Exps = new ArrayList<>();
        for(String part : bld.toString().split("\\+")){
            if(part.length()==0)continue;
            String[]subparts = part.split("x", -1);

            // we take the first part to be the coeff and the next to be the exponent
            // find the exponent
            Integer e = 0;
            try{
                if(subparts.length >= 2 && subparts[1].length() != 0) e = Integer.parseInt(subparts[1]);
                else if(subparts.length >= 2) e = 1;
            } catch (NumberFormatException err){
                throw new IllegalArgumentException("invalid number formatting");
            }
            if(e<0) throw new IllegalArgumentException("the exponent cannot be negative");

            // find the coefficient
            Double c = 1.0; // value for use when the length = 1
            try{
                if(subparts[0].length() == 1 && subparts[0].equals("-")) c = -1.0;
                else if (subparts[0].length() >= 1) {
                    c = Double.parseDouble(subparts[0]);
                }
            } catch (NumberFormatException err){
                throw new IllegalArgumentException("invalid number formatting");
            }

            if(subparts.length>2) throw new IllegalArgumentException("invalid number formatting");

            Exps.add(e); 
            Coeff.add(c);
        }
        int maxd = 0;
        for(Integer e:Exps)maxd=Math.max(maxd,e);
        double[] res = new double[maxd+1];
        for(int i = 0; i < Coeff.size(); i++){
            res[Exps.get(i)] += Coeff.get(i);
        }
        toPoly(res);
    }
    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < coeff.length; i++){
            if(coeff[i]>0 && i>0)sb.append("+");
            sb.append(coeff[i]);
            if(exps[i]>0){
                sb.append("x");
                if(exps[i]>1){
                    sb.append("^");
                    sb.append(exps[i]);
                }
            }
        }
        return sb.toString();
    }
    void saveToFile(String fname) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(fname);
        pw.print(toString());
        pw.close();
    }
    Polynomial add(Polynomial other) {
        int maxd = Math.max(this.exps[this.exps.length-1], other.exps[other.exps.length-1]);
        double[] cosum = new double[maxd+1];
        for(int i = 0; i < this.coeff.length; i++) cosum[this.exps[i]] += this.coeff[i];
        for(int i = 0; i < other.coeff.length; i++) cosum[other.exps[i]] += other.coeff[i];
        Polynomial sm = new Polynomial();
        sm.toPoly(cosum);
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
    private void toPoly(double[] coeffs){
        int maxd = coeffs.length - 1;
        int tot = 0;
        for(int d = 0; d <= maxd; d++) tot += (Math.abs(coeffs[d]) >= EPSILON) ? 1 : 0;
        tot = Math.max(tot, 1); // ensure we have the 'trivial' 0x^0 polynomial if there's nothing
        coeff = new double[tot];
        exps = new int[tot];
        int cur = 0;
        for(int d = 0; d <= maxd; d++) if(Math.abs(coeffs[d]) >= EPSILON){
            coeff[cur] = coeffs[d];
            exps[cur] = d;
            cur++;
        }
    }
}