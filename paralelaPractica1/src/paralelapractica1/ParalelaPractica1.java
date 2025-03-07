package paralelapractica1;

public class ParalelaPractica1 {

    
    public static void main(String[] args) {
        hilo h1  = new hilo();
        h1.setPriority(Thread.MIN_PRIORITY);
        
        hilito h2 = new hilito();
        Thread h = new Thread(h2);
        
        h.setPriority(Thread.MAX_PRIORITY);
        try {
            h.join();
        } catch (Exception e) {
        }
        h.start();
        h1.start();
    }
    
}
