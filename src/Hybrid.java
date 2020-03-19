import java.text.DecimalFormat;

import javax.naming.spi.DirStateFactory.Result;

/**
 * Hybrid
 */
public class Hybrid implements Predictor {
    public Integer k;
    public Integer m1;
    public Integer n;
    public Integer m2;
    public Integer[] chooser;
    public Integer[] bimodal;
    public Integer[] gShare;
    // state
    public Integer numOfPredict = 0;
    public Integer numOfMiss = 0;
    

    public Hybrid(Integer kInput, Integer m1Input, Integer nInput, Integer m2Input) {
        k = kInput;
        m1 = m1Input;
        n = nInput;
        m2 = m2Input;
        chooser = new Integer[(int) Math.pow(2, (double)k)];
        for (int i = 0; i < chooser.length; i++) {
            chooser[i] = 1;
        }
        bimodal = new Integer[(int) Math.pow(2, (double)m2)];
        for (int i = 0; i < bimodal.length; i++) {
            bimodal[i] = 4;
        }
        gShare = new Integer[(int) Math.pow(2, (double)m1)];
        for (int i = 0; i < gShare.length; i++) {
            gShare[i] = 4;
        }
    }

    public Integer GBH = 0; // Global brachn history
    public Integer GBHLength;
    
    @Override
    public Boolean predict(String pc) {
        numOfPredict++;
        Integer address = Integer.parseInt(pc.split(" ")[0], 16);
        Boolean taken = pc.split(" ")[1].equals("t") ? true : false;
        // Get pridict from both
        Boolean gSharePredict = gSharePredict(address, taken);
        // System.out.println("gshare predict:   " + gSharePredict);
        Boolean bimodalPredict = bimodalPredict(address, taken);
        // System.out.println("bimodalPredict:    predict:   " + bimodalPredict);
        // get chooserIndex
        Integer chooserIndex = (address >> 2) & ((1 << k) - 1);
        // System.out.println("chooser index:       " + chooserIndex);
        // Choose a predictor
        Integer choosen = chooser[chooserIndex] >= 2 ? 0 : 1; // 0 means choose gShare, 1 means choose bimodal
        // Update PT for the choosen one
        if (choosen == 0) {
            updateGshare(address, taken);
        } else {
            updateBimodal(address, taken);
        }
        // System.out.println("bValue: "+ bimodal[getIndexBimodal(address)]);
        // System.out.println("gValue: "+ gShare[getIndexGshare(address)]);
        // update GBH
        updateGBH(taken);
        // System.out.println("GBH: " +Integer.toBinaryString(GBH));
        updateChooser(gSharePredict, bimodalPredict, chooserIndex, taken);
        // System.out.println("c: " + chooser[chooserIndex]);
        // update chooser table
        Boolean result = choosen == 0 ? gSharePredict : bimodalPredict;
        if (result != taken) {
            numOfMiss++;
        }
        
        return result;
    }

    @Override
    public void printPT() {
        // TODO Auto-generated method stub
        printChooser();
        printGshare();
        printBimodal();
    }

    
    private void updateChooser(Boolean gSharePredict, Boolean bimodalPredict, Integer chooserIndex, Boolean taken) {
        Integer out = chooser[chooserIndex];
        if (taken) {
            if (gSharePredict && (bimodalPredict == false)) {
                if (out < 3) {
                    chooser[chooserIndex]++;
                }
            } else if ((gSharePredict == false) && bimodalPredict) {
                if (out > 0) {
                    chooser[chooserIndex]--;
                }
            }
        } else {
            if ((gSharePredict == false) && bimodalPredict) {
                if (out < 3) {
                    chooser[chooserIndex]++;
                }
            } else if (gSharePredict && (bimodalPredict == false)) {
                if (out > 0) {
                    chooser[chooserIndex]--;
                }
            }
        }
    }

    private void updateBimodal(Integer address, Boolean taken) {
        Integer indexPT = getIndexBimodal(address);
        Integer out = bimodal[indexPT];
        if (taken) {
            if (out < 7) {
                bimodal[indexPT]++;
            }
        } else {
            if (out > 0) {
                bimodal[indexPT]--;
            }
        }
    }

    private void updateGshare(Integer address, Boolean taken) {
        Integer indexM = getIndexGshare(address);
        // System.out.println(indexM);
        Integer indexN = getIndexN(indexM);
        Integer indexMN = indexM >> n;
        Integer indexPT = (indexMN << n) | (indexN ^ GBH);
        Integer out = gShare[indexPT];
        if (taken) {
            if (out < 7) {
                gShare[indexPT]++;
            }
        } else {
            if (out > 0) {
                gShare[indexPT]--;
            }
        }
    }

    public Boolean bimodalPredict(Integer address, Boolean taken) {
        // TODO Auto-generated method stub
        //get index
        Integer indexPT = getIndexBimodal(address);
        // System.out.println(indexPT);
        Boolean result = bimodal[indexPT] >= 4 ? true: false;
        return result;
    }

    public Boolean gSharePredict(Integer address, Boolean taken) {
        // TODO Auto-generated method stub
        //get index
        Integer indexM = getIndexGshare(address);
        // System.out.println(indexM);
        Integer indexN = getIndexN(indexM);
        Integer indexMN = indexM >> n;
        Integer indexPT = (indexMN << n) | (indexN ^ GBH);
        // System.out.println(indexPT);
        // make a prediction
        Boolean result = gShare[indexPT] >= 4 ? true: false;
        // update GBH
        // updateGBH(taken);
        return result;
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

    public void printChooser() {
        System.out.println("FINAL CHOOSER CONTENTS");
        for (int i = 0; i < chooser.length; i++) {
            System.out.println(i + "   " + chooser[i]);
        }
    }

    public void printGshare() {
        System.out.println("FINAL GSHARE CONTENTS");
        for (int i = 0; i < gShare.length; i++) {
            System.out.println(i + "   " + gShare[i]);
        }
    }

    public void printBimodal() {
        System.out.println("FINAL BIMODAL CONTENTS");
        for (int i = 0; i < bimodal.length; i++) {
            System.out.println(i + "   " + bimodal[i]);
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

    private Integer getIndexN(Integer indexM) {
        return indexM & ((1 << n) - 1);
    }

    private Integer getIndexBimodal(Integer address) {
        return (address >> 2) & ((1 << m2) - 1);
    }

    private Integer getIndexGshare(Integer address) {
        return (address >> 2) & ((1 << m1) - 1);
    }

    public static void main(String[] args) {
        Hybrid myHbrid = new Hybrid(8, 14, 10, 5);
        Integer address = Integer.parseInt("302d28" , 16);
        // System.out.println(myHbrid.gSharePredict(address, false));
        System.out.println(myHbrid.predict("302d28 n"));
        System.out.println(myHbrid.predict("302d30 n"));
        System.out.println(myHbrid.predict("305b0c t"));
        System.out.println(myHbrid.predict("30093c t"));
        System.out.println(myHbrid.predict("30098c n"));
        System.out.println(myHbrid.predict("3009a4 t"));
        System.out.println(myHbrid.predict("30098c t"));
        System.out.println(myHbrid.predict("3009b0 n"));
        // System.out.println(myHbrid.n);
    }


}