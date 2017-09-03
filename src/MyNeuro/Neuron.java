package MyNeuro;

/**
 * Created by BigArt on 19.06.2017.
 */
public class Neuron {
    private double[] inputFact;

    Neuron(double[] inputFact){
        this.inputFact=inputFact;
    }
    public void setInputFact(double[] inputFact){
        this.inputFact=inputFact;
    }
    public double[] getInputFact(){
        return inputFact;
    }

    public double[] learn(double error, double[] input){
        double outError[]=new double[input.length];
        for (int i=0;i<input.length;i++){
            outError[i]=error*inputFact[i];
            inputFact[i]+=error*input[i];
        }
        return outError;
    }


    public double out(double[] input) throws Exception {
        double out=0;
        if (input.length!=inputFact.length){
            System.err.println("ErrorFactorsLength");
            throw new Exception("ErrorFactorsLength");
        }
        for(int i=0;i<input.length;++i){
            out+=input[i]*inputFact[i];
        }
        return out;
    }
}
