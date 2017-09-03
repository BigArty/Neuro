package MyNeuro;

import com.sun.xml.internal.ws.api.pipe.SyncStartForAsyncFeature;

import java.io.*;
import java.util.Arrays;

/**
 * Created by BigArt on 19.06.2017.
 */
public class Neuro {

    private Layer[] layers;

    Neuro(int layers, int[] neuronInLayer, int inputLenghth, double speedLearning, int function) {
        randomNeuro(layers, neuronInLayer, inputLenghth, speedLearning, function);
    }

    Neuro(File f) {
        try {
            reedNeuro(f);
        } catch (IOException e) {
            System.err.println("Не считалось.");
        }
    }

    public void randomNeuro(int layers, int[] neuronInLayer, int inputLenghth, double speedLearning, int function) {
        this.layers = new Layer[layers];
        double[][] fact = new double[neuronInLayer[0]][];
        for (int j = 0; j < neuronInLayer[0]; ++j) {
            fact[j] = new double[inputLenghth + 1];
            for (int k = 0; k < inputLenghth + 1; ++k) {
                fact[j][k] = (Math.random() - 0.5) * (function % 2) + (Math.random() - 0.5) * 0.01;
            }
        }
        this.layers[0] = new Layer(fact, speedLearning, function);
        for (int i = 1; i < layers; ++i) {
            fact = new double[neuronInLayer[i]][];
            for (int j = 0; j < neuronInLayer[i]; ++j) {
                fact[j] = new double[neuronInLayer[i - 1] + 1];
                for (int k = 0; k < neuronInLayer[i - 1] + 1; ++k) {
                    fact[j][k] = (Math.random() - 0.5) * 0.01;
                }
            }
            this.layers[i] = new Layer(fact, speedLearning, function);
        }
        this.layers[layers-1].setFunction(1);
        this.layers[layers-1].setSpeedLearning(0.1);
    }

    private double[] examplOut;
    private double[] examplTarg;
    boolean stop=false;
    boolean isCores=false;

    public int learning(int length, int input, int out, long maxEpoch, int epochBetweenReports, double maxError, double[][] data, int epochBetweenBackUp, String savePath,int cores) throws Exception {
        for(int i=0;i<cores;++i){
            new Core();
        }
        isCores=true;
        return learning(length,input,out,maxEpoch,epochBetweenReports,maxError,data,epochBetweenBackUp,savePath);
    }

    public int learning(int length, int input, int out, long maxEpoch, int epochBetweenReports, double maxError, double[][] data, int epochBetweenBackUp, String savePath) throws Exception {
        if(!isCores){
            new Core();
        }
        new BackUp(savePath);
        int iter = epochBetweenReports;
        boolean trained = false;
        long i = 0;
        int dataNum;
        int OK = 0;
        double[] worstOut = new double[0], worstIn = new double[0];
        int worstData = 0;
        System.out.println("Length " + length);
        while (!trained && i < maxEpoch&&!stop) {
            double curError = 0;
            for (int j = 0; j < iter && !trained; ++j) {
                curError = 0;
                OK = 0;
                int[] rnd = new int[length];
                for (int k = 0; k < length; k++) {
                    dataNum = (int) (Math.random() * length);
                    while (rnd[dataNum] == 1) {
                        dataNum++;
                        if (dataNum == length) {
                            dataNum = 0;
                        }
                    }
                    rnd[dataNum] = 1;
                    double err = learn(data[2 * dataNum], data[2 * dataNum + 1]);
                    if (err > curError) {
                        curError = err;
                        worstIn = examplTarg;
                        worstOut = examplOut;
                        worstData = dataNum;

                    }
                    if (err < maxError) {
                        OK++;
                    }
                    if (i == 0 && k == 0) {
                        System.out.println("Epochs " + i + "   Current error: " + curError + "\n" + Arrays.toString(examplOut) + "\n" + Arrays.toString(examplTarg));
                    }
                }
                ++i;
                if (curError <= maxError) {
                    trained = true;
                }
                if (i % epochBetweenBackUp == 0) {
                    BackUp.queue.add(this);
                }
            }
            System.out.println("Epochs " + i + "   Current error: " + curError + "  GoodTests " + OK + "\n" + Arrays.toString(examplOut) + "\n" + Arrays.toString(examplTarg));
            System.out.println("Worst: " + worstData + "\n" + Arrays.toString(worstOut) + "\n" + Arrays.toString(worstIn) + "\n");
        }
        BackUp.queue.add(this);
        BackUp.work = false;
        if (trained) {
            return 1;
        }
        return -1;
    }

    private double learn(double input[], double targetOut[]) throws Exception {//return error^2
        double result[] = examplOut = work(input);
        examplTarg = targetOut;
        double error[] = new double[result.length - 1];
        if (targetOut.length != error.length) {
            System.err.println("ErrorOutSize");
            throw new Exception("ErrorOutSize");
        }
        double error2 = 0;
        for (int i = 0; i < error.length; ++i) {
            error[i] = targetOut[i] - result[i];
            error2 += error[i] * error[i];
        }
        for (int i = layers.length - 1; i >= 0; --i) {
            error = layers[i].learning(error);
        }
        return error2;
    }

    public double[] work(double input[]) throws Exception {
        double input1[] = new double[input.length + 1];
        for (int i = 0; i < input.length; ++i) {
            input1[i] = input[i];
        }
        input1[input.length] = 1;
        for (int i = 0; i < layers.length; ++i) {
            input1 = layers[i].work(input1);
        }
        return input1;
    }

    public void reedNeuro(File f) throws IOException {
        FileInputStream inputStream = new FileInputStream(f);
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        int layers = Integer.parseInt(reader.readLine());
        this.layers = new Layer[layers];
        String[] s = reader.readLine().split(" ");
        int[] neuronInLayer = new int[s.length];
        for (int i = 0; i < s.length; ++i) {
            neuronInLayer[i] = Integer.parseInt(s[i]);
        }
        for (int i = 0; i < neuronInLayer.length - 1; ++i) {
            s = reader.readLine().split(" ");
            double speedLearnig = Double.parseDouble(s[0]);
            int function = Integer.parseInt(s[1]);
            double[][] fact = new double[neuronInLayer[i + 1]][];
            for (int j = 0; j < fact.length; j++) {
                s = reader.readLine().split(" ");
                fact[j] = new double[s.length];
                for (int k = 0; k < s.length; k++) {
                    fact[j][k] = Double.parseDouble(s[k]);
                }
            }
            this.layers[i] = new Layer(fact, speedLearnig, function);
        }

    }

    public void saveNeuro(File f) throws IOException {
        FileWriter writer = new FileWriter(f);
        writer.write(Integer.toString(layers.length));
        writer.write("\n");
        writer.write(Integer.toString(layers[0].getLayerNeuron()[0].getInputFact().length - 1));
        for (int i = 0; i < layers.length; ++i) {
            writer.write(" " + Integer.toString(layers[i].getLayerNeuron().length));
            writer.flush();
        }
        writer.write("\n");
        for (int i = 0; i < layers.length; i++) {
            double[][] fact = layers[i].getFactors();
            writer.write(Double.toString(layers[i].getSpeedLearning()) + " " + Integer.toString(layers[i].getFunction()) + "\n");
            for (int j = 0; j < fact.length; j++) {
                for (int k = 0; k < fact[j].length; k++) {
                    writer.write(Double.toString(fact[j][k]) + " ");
                }
                writer.flush();
                writer.write("\n");
            }
        }
        writer.flush();
    }
}
