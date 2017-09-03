package MyNeuro;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Stop extends Thread {
    Stop(Neuro n){
        neuro=n;
        start();
    }
    private Neuro neuro;

    @Override
    public void run() {
        boolean stop=false;
        while (!stop){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ignored) {
            }
            BufferedReader r= new BufferedReader(new InputStreamReader(System.in));
            try {
                if(r.readLine().equals("Stop")){
                    neuro.stop=true;
                    stop=true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
