package carrera;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Segundos extends Thread{
    
    volatile int segundos = 0;

    @Override
    public void run() {
        while(true){
            try {
                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Segundos.class.getName()).log(Level.SEVERE, null, ex);
            }
            segundos++;
        }
    }
}
