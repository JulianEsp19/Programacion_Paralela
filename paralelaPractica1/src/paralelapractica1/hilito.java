package paralelapractica1;

import static java.lang.Thread.sleep;

public class hilito implements Runnable{
    
    @Override
    public void run(){
        while (true) {
            try {
                System.out.println("holi");
                sleep(5000);
            } catch (Exception e) {
               
            }
        }
    }
}
