package MyNeuro;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Created by Администратор on 07.07.2017.
 */
public class TestRelu {
    static File[] folders = new File[10];
    static File[][] files = new File[10][];
    static BufferedImage img;
    static double[][] output = new double[10][10];
    static int[][] datasPlase = new int[2][];
    static int counter = 0;
    static int inp = 28 * 28, out = 10;
    static double[][] data;
    static String savePath = "project/backUpRelu";

    public static void main(String[] args) {
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                output[i][j] = -1;
            }
        }
        for (int i = 0; i < 10; i++) {
            output[i][i] = 1;
            folders[i] = new File("mnist_png/training/" + i);
            files[i] = folders[i].listFiles();
            counter += files[i].length;
        }
        data = new double[counter * 2][];
        datasPlase[0] = new int[counter];
        datasPlase[1] = new int[counter];
        int k = 0;
        for (int i = 0; i < 10; ++i) {
            System.out.println(i + "");
            for (int j = 0; j < files[i].length; ++j) {
                data[k * 2] = new double[28 * 28];
                data[k * 2 + 1] = output[i];
                datasPlase[0][k] = i;
                datasPlase[1][k] = j;
                try {
                    img = ImageIO.read(files[i][j]);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                for (int h = 0; h < 28; h++) {
                    for (int t = 0; t < 28; ++t) {
                        data[k * 2][28 * h + t] = 2.0 * (img.getRGB(h, t) & 0xff) / 256.0 - 1.0;
                    }
                }
                ++k;
            }
        }
        //System.out.println(files[datasPlase[0][7010]][datasPlase[1][7010]].toString());
        Neuro n = new Neuro(4, new int[]{300, 90, 80, 10}, 28 * 28, 0.01, 3);
        //Neuro n = new Neuro(new File("project/backUp1.txt"));
        System.out.println("Loaded");
        new Stop(n);
        try {
            n.learning(counter, 28 * 28, 10, 9000000000000000000L, 1, 0.1, data, 5, savePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        /*double[] res=null;
        try {
            res=n.work(data[2]);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println(Arrays.toString(res));*/
        try {
            n.saveNeuro(new File("numNeuro2.txt"));
        } catch (IOException e) {
            System.out.println("2 " + e.getMessage());
        }
    }
}
