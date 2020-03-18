/**
 * Gshare
 */
public class Gshare implements Predictor {
    public Integer m;
    public Integer n;

    public Gshare(Integer mInput, Integer nInput) {
        m = mInput;
        n = nInput;
    }

    @Override
    public void predict() {
        // TODO Auto-generated method stub
    }

    public static void main(String[] args) {
        Gshare myGshare = new Gshare(3, 0);
        System.out.println(myGshare.m);
        System.out.println(myGshare.n);
    }
}