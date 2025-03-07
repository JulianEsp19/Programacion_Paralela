package paralelapractica1;

public class hilo extends Thread{
    
    @Override
    public void run(){        
        while (true) {
            try {
                System.out.println("hola");
                sleep(5000);
            } catch (Exception e) {
            }
        }
    }
    
}
