package MyNeuro;

import java.io.*;
import java.util.Arrays;

/**
 * Created by Администратор on 05.07.2017.
 */
public class Train {
    public static void main(String[] args) {
        Neuro n = new Neuro(4, new int[]{400,200, 140, 6}, 7282, 0.01, 3);
        //Neuro n = new Neuro(new File("project/backUpAllSmall2.txt"));
        String savePath = "project/backUpAllSmall";
        new Stop(n);
        int length1 = 0, inp = 0, out = 0, length = 0;
        try {
            for (int i = 0; i < 6; ++i) {
                File train = new File("train/train/train" + i + ".data");
                FileInputStream inputStream = new FileInputStream(train);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                String[] s = reader.readLine().split(" ");
                length1 += Integer.parseInt(s[0]);
                inp = Integer.parseInt(s[1]);
                out = Integer.parseInt(s[2]);
            }
            double[][] data = new double[2 * length1][];
            int counter = 0;
            String[] s;
            for (int k = 0; k < 6; k++) {
                File train = new File("train/train/train" + k + ".data");
                FileInputStream inputStream = new FileInputStream(train);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                s = reader.readLine().split(" ");
                length = Integer.parseInt(s[0]);
                for (int i = 0; i < length * 2; ++i) {
                    s = reader.readLine().split(" ");
                    data[counter] = new double[s.length];
                    for (int j = 0; j < s.length; ++j) {
                        data[counter][j] = Double.parseDouble(s[j]);
                    }
                    counter++;
                }
            }
            n.learning(length1, inp, out, 9000000000000000000L, 1, 0.1, data, 5, savePath);
        } catch (IOException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }


    }
}
