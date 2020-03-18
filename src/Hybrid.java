/**
 * Hybrid
 */
public class Hybrid implements Predictor {
    public Integer k;
    public Integer m1;
    public Integer n;
    public Integer m2;

    public Hybrid(Integer kInput, Integer m1Input, Integer nInput, Integer m2Input) {
        k = kInput;
        m1 = m1Input;
        n = nInput;
        m2 = m2Input;
    }

    @Override
    public void predict() {
        // TODO Auto-generated method stub
    }

    public static void main(String[] args) {
        Hybrid myHbrid = new Hybrid(3, 0, 2, 2);
        System.out.println(myHbrid.m1);
        System.out.println(myHbrid.n);
    }

}