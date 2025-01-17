import java.text.DecimalFormat;

/**
 * Gshare
 */
public class Gshare implements Predictor {
    public Integer m;
    public Integer n;
    public Integer[] PT; // Branch history table
    public Integer GBH = 0; // Global brachn history
    public Integer GBHLength;
    // state
    public Integer numOfPredict = 0;
    public Integer numOfMiss = 0;
    

    public Gshare(Integer mInput, Integer nInput) {
        m = mInput;
        n = nInput;
        PT = new Integer[(int) Math.pow(2, (double)m)];
        for (int i = 0; i < PT.length; i++) {
            PT[i] = 4;  //init with 4
        }
        GBHLength = n; // n bit GBH
    }

    @Override
    public Boolean predict(String pc) {
        numOfPredict++;
        Integer address = Integer.parseInt(pc.split(" ")[0], 16);
        Boolean taken = pc.split(" ")[1].equals("t") ? true : false;
        //get index
        Integer indexM = getIndexM(address);
        // System.out.println("indexM: " + indexM);
        Integer indexN = getIndexN(indexM);
        Integer indexMN = indexM >> n;
        Integer indexPT = n == 0 ? indexM : (indexMN << n) | (indexN ^ GBH);
        // System.out.println("indexPT: " + indexPT);
        // make a prediction
        Boolean result = PT[indexPT] >= 4 ? true: false;
        if (result != taken) {
            numOfMiss++;
        }
        // update PT
        updatePT(indexPT, taken);
        // update GBH
        updateGBH(taken);
        return result;
    }

    @Override
    public void printPT() {
        if (n == 0) {
            System.out.println("FINAL BIMODAL CONTENTS");
        } else {
            System.out.println("FINAL GSHARE CONTENTS");
        }
        for (int i = 0; i < PT.length; i++) {
            System.out.println(i + "   " + PT[i]);
        }
    }

    @Override
    public void printState() {
        System.out.println("number of predictions:  " + numOfPredict);
        System.out.println("number of mispredictions:    " + numOfMiss);
        double missPredictRate = (double)numOfMiss / numOfPredict;
        // System.out.println(missPredictRate);
        DecimalFormat df = new DecimalFormat("#.##");      
        missPredictRate = Double.valueOf(df.format(missPredictRate * 100));
        System.out.printf("misprediction rate:    %.2f", missPredictRate);
        System.out.println("%");
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
        return (address >> 2) & ((1 << m) - 1);
    }

    

    public static void main(String[] args) {
        Gshare myGshare = new Gshare(6, 0);
        System.out.println(myGshare.m);
        System.out.println(myGshare.getIndexM(Integer.parseInt("302d30" , 16)));
    }

}