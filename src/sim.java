import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * sim
 */
public class sim {
	public static void main(String[] args) {
        System.out.println("args length: " + args.length);
        String type;
        String trace = "";
        int n;
        int m1;
        int m2;
        int k;
        Predictor myPredictor = null;
        
        System.out.println("COMMAND");
        System.out.print("./sim ");

		if (args.length == 3) {
            type = args[0];
            n = 0;
            m1 = Integer.parseInt(args[1]);
            trace = args[2];
            myPredictor = new Gshare(m1, n);
        } else if (args.length == 4) {
            type = args[0];
            n = Integer.parseInt(args[2]);
            m1 = Integer.parseInt(args[1]);
            trace = args[3];
            myPredictor = new Gshare(m1, n);
        } else if (args.length == 6) {
            type = args[0];
            k = Integer.parseInt(args[1]);
            m1 = Integer.parseInt(args[2]);
            n = Integer.parseInt(args[3]);
            m2 = Integer.parseInt(args[4]);
            trace = args[5];
            myPredictor = new Hybrid(k, m1, n, m2);
        } else {
            System.out.println("args length not valid");
        }

        for (int i = 0; i < args.length; i++) {
            System.out.print(args[i] + " ");
        }
        System.out.println();

        
        
		// Read trace
		try {
			File f = new File("../traces/" + trace);
			BufferedReader b = new BufferedReader(new FileReader(f));
			String readLine = "";
			String[] operationArr;
			while ((readLine = b.readLine()) != null) {
                if (myPredictor!= null) {
                myPredictor.predict(readLine);
                }
            }
            myPredictor.printPT();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}