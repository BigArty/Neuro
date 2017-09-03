import com.googlecode.fannj.Fann;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) {
        Fann fann = new Fann("ann");
        File train = new File("train/testNav.data");
        int length = 0, inp = 0, out = 0;
        try {
            FileInputStream inputStream = new FileInputStream(train);
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            String[] s = reader.readLine().split(" ");
            length = Integer.parseInt(s[0]);
            inp = Integer.parseInt(s[1]);
            out = Integer.parseInt(s[2]);
            float[][] data = new float[2 * length][];
            for (int i = 0; i < data.length; ++i) {
                s = reader.readLine().split(" ");
                data[i] = new float[s.length];
                for (int j = 0; j < s.length; ++j) {
                    if (i % 2 == 0) {
                        data[i][j] = (float) Double.parseDouble(s[j]) * 2 - 1;
                    } else {
                        data[i][j] = (float) Double.parseDouble(s[j]);
                    }
                }
            }
            int good = 0;
            int BAD = 0;
            double err = 0;
            for (int i = 0; i < length; ++i) {
                double res = fann.run(data[i * 2])[0];
                err = (res - data[2 * i + 1][0]) * (res - data[2 * i + 1][0]);
                if (err < 1) {
                    good++;
                }
                if (err>3.2){
                    BAD++;
                    System.out.println(res);
                }
            }
            System.out.println("Good: " + good + "\nBad: "+BAD);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getAction(float[] out) {
        int i = 0;
        for (int j = 1; j < 4; j++) {
            if (out[i] < out[j]) {
                i = j;
            }
        }
        switch (i) {
            case 0:
                return "атаковать";
            case 1:
                return "прятаться";
            case 2:
                return "бежать";
            case 3:
                return "ничего не делать";
        }
        return "";
    }
}
