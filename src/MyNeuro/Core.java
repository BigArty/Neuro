package MyNeuro;
import java.util.*;

public class Core extends Thread{
    Core(){
        start();
    }

    static List<MiniCalc> queue= Collections.synchronizedList(new LinkedList <MiniCalc>());
    static boolean work=true;
    private static final Object sync=new Object();

    @Override
    public void run() {
        while (work) {
            while (!queue.isEmpty()) {
                MiniCalc c;
                c = queue.remove(0);
                c.calc();
                c.ready=true;
            }
            try {
                Thread.sleep(10);
            } catch (InterruptedException ignored) {
            }
        }

    }
}