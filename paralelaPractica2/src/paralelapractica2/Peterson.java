package paralelapractica2;

public class Peterson {
    
    private volatile boolean[] flag = new boolean[2];
    
    private volatile int turn;
    
    public void lock(int self){
        int other = 1 - self;
        flag[self] = true;
        turn = other;
        
        
    }
    
}
