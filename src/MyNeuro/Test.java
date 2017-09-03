package MyNeuro;

import java.io.*;

public class Test {
    public static int max(double[] data, int length){
        int max=0;
        double maxData=data[0];
        for(int i=1;i<length;++i){
            if(data[i]>maxData){
                maxData=data[i];
                max=i;
            }
        }
        return max;
    }

    public static void main(String[] args) {
        Neuro n = new Neuro(new File("project/backUpAll2.txt"));
        String path="train/test/test";
        int length1=0,length = 0, inp = 0, out = 0;
        try {
            for (int i = 0; i < 6; ++i) {
                File train = new File(path + i + ".data");
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
                File train = new File(path + k + ".data");
                FileInputStream inputStream = new FileInputStream(train);
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
                s = reader.readLine().split(" ");
                length = Integer.parseInt(s[0]);
                for (int i = 0; i < length*2; ++i) {
                    s = reader.readLine().split(" ");
                    data[counter] = new double[s.length];
                    for (int j = 0; j < s.length; ++j) {
                        data[counter][j] = Double.parseDouble(s[j]);
                    }
                    counter++;
                }
            }
            System.out.println("Length: " + length1);
            int good = 0;
            double err = 0;
            for (int i = 0; i < length1; ++i) {
                double[] res = n.work(data[i * 2]);
                if (max(res,6)==max(data[2 * i + 1], 6)) {
                    good++;
                }
            }
            System.out.println("Good: " + good);
        } catch (IOException ignored) {
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
