import java.io.File;
import java.io.FileNotFoundException;

public class Driver {
    public static void main(String [] args) throws FileNotFoundException{
        // test reading a polynomial & 'hasRoot'
        Polynomial p = new Polynomial(new File("poly1.txt"));
        if(p.hasRoot(313)){
            System.out.println("313 is a root of p");
        }else{
            System.out.println("313 is not a root of p");
        }
        System.out.println(p);

        // cancel out test
        Polynomial s1 = new Polynomial("-1-x");
        Polynomial s2 = new Polynomial("1+x");
        System.out.println("should be 0");
        System.out.println(s1.add(s2));

        // test product
        Polynomial p1 = new Polynomial("x-1");
        Polynomial p2 = new Polynomial("x+1");
        Polynomial p3 = p1.multiply(p2);
        System.out.println(p3);
        if(p.hasRoot(0)){
            System.out.println("0 is a root of p3");
        }else{
            System.out.println("0 is not a root of p3");
        }

        Polynomial p4 = p.add(p3);
        System.out.println(p4);
        p4.saveToFile("poly4.txt");
    }
}