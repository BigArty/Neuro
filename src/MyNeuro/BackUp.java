package MyNeuro;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

/**
 * Created by Администратор on 06.07.2017.
 */
public class BackUp extends Thread {
    BackUp(String path) {
        this.path=path;
        start();
    }

    static boolean work = true;
    static LinkedList<Neuro> queue = new LinkedList<>();
    private String path;

    @Override
    public void run() {
        int i = 0;
        while (work) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ignored) {
            }
            if (!queue.isEmpty()) {
                try {
                    queue.getFirst().saveNeuro(new File(path + (i % 3) + ".txt"));
                    System.err.println("["+path+"] Saved " + (i % 3));
                    queue.removeFirst();
                    i++;
                } catch (IOException ignored) {
                }
            }
        }
    }
}
