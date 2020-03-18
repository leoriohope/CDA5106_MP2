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

    public Integer[] PT; // Branch history table
    public Integer GBH = 0; // Global brachn history
    public Integer GBHLength;
    


    @Override
    public Boolean predict(String pc) {
        // TODO Auto-generated method stub
        Integer address = Integer.parseInt(pc.split(" ")[0], 16);
        Boolean taken = pc.split(" ")[1].equals("t") ? true : false;
        //get index
        Integer indexM = getIndexM(address);
        Integer indexN = getIndexN(indexM);
        Integer indexMN = indexM >> n;
        Integer indexPT = n == 0 ? indexM : (indexMN << n) | (indexN ^ GBH);
        // make a prediction
        Boolean result = PT[indexPT] >= 4 ? true: false;
        // update PT
        updatePT(indexPT, taken);
        // update GBH
        updateGBH(taken);
        return result;
    }

    @Override
    public void printPT() {
        for (int i = 0; i < PT.length; i++) {
            System.out.println(i + "   " + PT[i]);
        }
    }


    public void updateGBH(Boolean taken) {
        if (n != 0) {
            if (taken) {
                GBH = (GBH >> 1) | ((1 << (n - 1)));
            } else {
                GBH = (GBH >> 1);
            }
        }
    }


    public void updatePT(Integer indexPT, Boolean taken) {
        Integer out = PT[indexPT];
        if (taken) {
            if (out < 7) {
                PT[indexPT]++;
            }
        } else {
            if (out > 0) {
                PT[indexPT]--;
            }
        }
    }


    private Integer getIndexN(Integer indexM) {
        return n != 0 ? (indexM & ((1 << n) - 1)) : indexM;
    }

    private Integer getIndexM(Integer address) {
        return (address >> 2) & ((1 << m1) - 1);
    }

    public static void main(String[] args) {
        Hybrid myHbrid = new Hybrid(3, 0, 2, 2);
        System.out.println(myHbrid.m1);
        System.out.println(myHbrid.n);
    }

}