package MyNeuro;

import javax.imageio.ImageIO;
import java.io.File;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Created by Администратор on 06.07.2017.
 */
public class TestNum {
    static File[] folders = new File[10];
    static File[][] files = new File[10][];
    static BufferedImage img[][] = new BufferedImage[10][];
    static double[][] output = new double[10][10];
    static int counter = 0;
    static int inp = 300 * 300, out = 10;
    static double[][] data;
    static String savePath = "project/backUp";

    public static void main(String[] args) {
        for (int i = 0; i < 10; ++i) {
            for (int j = 0; j < 10; ++j) {
                output[i][j] = -1;
            }
        }
        for (int i = 0; i < 10; i++) {
            output[i][i] = 1;
            folders[i] = new File("Num/" + i);
            files[i] = folders[i].listFiles();
            img[i] = new BufferedImage[files[i].length];
            for (int j = 0; j < files[i].length; ++j) {
                try {
                    img[i][j] = ImageIO.read(files[i][j]);
                } catch (IOException ignored) {
                    System.out.println(i + " " + j);
                }
            }
            counter += files[i].length;
        }
        data = new double[counter * 2][];
        int k = 0;
        for (int i = 0; i < img.length; ++i) {
            for (int j = 0; j < img[i].length; ++j) {
                data[k * 2] = new double[300 * 300];
                data[k * 2 + 1] = output[i];
                for (int h = 0; h < 300; h++) {
                    for (int t = 0; t < 300; ++t) {
                        data[k * 2][300 * h + t] = 2.0 * (img[i][j].getRGB(h, t) & 0xff) / 256.0 - 1.0;
                    }
                }
                ++k;
            }
        }
        Neuro n = new Neuro(3, new int[]{5, 10, 10}, 300 * 300, 0.01, 1);
        //Neuro n = new Neuro(new File("backUp0.txt"));
        System.out.println("Loaded");
        try {
            n.learning(counter, 300 * 300, 10, 9000000000000000000L, 70, 0.01, data, 800, savePath);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            n.saveNeuro(new File("numNeuro.txt"));
        } catch (IOException e) {
            System.out.println("2 " + e.getMessage());
        }
    }
}
