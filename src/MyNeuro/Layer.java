package MyNeuro;

/**
 * Created by BigArt on 19.06.2017.
 */
public class Layer {
    private double speedLearning;
    private Neuron[] layerNeuron;
    private double[] inputData;
    private int function;//1-sigm
    private double neuronOut[];

    public double getSpeedLearning() {
        return speedLearning;
    }

    public int getFunction() {
        return function;
    }

    public Neuron[] getLayerNeuron() {
        return layerNeuron;
    }

    public double[][] getFactors(){
        double[][] fact = new double[layerNeuron.length][];
        for (int i=0; i<layerNeuron.length;i++){
            fact[i]=layerNeuron[i].getInputFact();
        }
        return fact;
    }
    public void setFunction(int function){
        this.function=function;
    }
    public void setSpeedLearning(double speed){
        speedLearning=speed;
    }

    double function(double x) {
        switch (function) {
            case 1:
                return sigm(x);
            case 2:
                return relu(x);
            case 3:
                return SmoothRelu(x);
            default:
                return 0;
        }

    }

    double divFunction(double x) {
        switch (function) {
            case 1:
                return divSigm(x);
            case 2:
                return divRelu(x);
            case 3:
                return divSmoothRelu(x);
            default:
                return 0;
        }
    }

    private double sigm(double x) {
        return 2.0 / (1 + Math.exp(-x)) - 1;
    }

    private double relu(double x){
        if(x<0){
            return 0;
        }
        else{
            return x;
        }
    }

    private double SmoothRelu(double x){
        return Math.log(1+Math.exp(x));
    }

    private double divSmoothRelu(double x) {
        return 1/(1+Math.exp(-x));
    }

    private double divRelu(double x){
        if(x>=0)return 1;
        return 1;
    }

    private double divSigm(double x) {
        return 0.5 * (1 + sigm(x)) * (1 - sigm(x));
    }

    Layer(double[][] fact, double speedLearning, int activationFunction) {
        this.function = activationFunction;
        this.speedLearning = speedLearning;
        layerNeuron = new Neuron[fact.length];
        neuronOut = new double[fact.length];
        for (int i = 0; i < fact.length; ++i) {
            layerNeuron[i] = new Neuron(fact[i]);
        }
    }

    public double[] work(double[] inputData) throws Exception {
        this.inputData = inputData;
        double[] outData = new double[layerNeuron.length + 1];
        outData[layerNeuron.length] = 1;
        for (int i = 0; i < layerNeuron.length; ++i) {
            neuronOut[i]=layerNeuron[i].out(inputData);
            outData[i] = function(neuronOut[i]);
        }
        return outData;
    }

    public double[] learning(double error[]){
        double outError[] = new double[inputData.length-1];
        for (int i=0;i<error.length;i++){
            error[i]=error[i]*divFunction(neuronOut[i]);
            double neuroError[]=layerNeuron[i].learn(error[i]*speedLearning,inputData);
            for (int j=0;j<inputData.length-1;++j){
                outError[j]+=neuroError[j];
            }
        }

        return outError;
    }

    public void setLayerNeuron(double[][] fact) {
        layerNeuron = new Neuron[fact.length];
        for (int i = 0; i < fact.length; ++i) {
            layerNeuron[i] = new Neuron(fact[i]);
        }
    }
}

class MiniCalc{
    Layer layer;

    MiniCalc(double[] data, double[] fact, double[] neuronOut, double[] out, int number, boolean learn,Layer layer){
        this.data=data;
        this.fact=fact;
        this.out=out;
        this.number=number;
        this.learn=learn;
        this.neuronOut=neuronOut;
        this.layer=layer;
    }
    double[] neuronOut;
    double[] data;
    double[] fact;
    double[] out;
    int number;
    boolean ready=false;
    boolean learn=false;
    void calc(){
        if(learn){
            learning();
        }
        else {
            work();
        }
    }
    private void learning(){

    }

    private void work(){

    }
}
