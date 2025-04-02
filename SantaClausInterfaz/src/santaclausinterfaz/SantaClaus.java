package santaclausinterfaz;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class SantaClaus {

    private int renosRegreso;
    private boolean disponibleDuendes;
    private int numDuendeProblemas;
    private int serieRenos;
    private int serieDuendes;
    private int turno;
    private int llegada;
    
    private JLabel santa;

    public SantaClaus(JLabel santa) {
        this.santa = santa;
        renosRegreso = 0;
        disponibleDuendes = true;
        serieRenos = 0;
        numDuendeProblemas = 0;
        turno = 0;
        llegada = 0;
    }
    
    public synchronized void llegaronLosRenos(){
        disponibleDuendes = false;
        santa.setVisible(false);
    }
    
    public synchronized void regresamosLosRenos(){
        disponibleDuendes = true;
        renosRegreso = 0;
        santa.setVisible(true);
    }
    
    public synchronized boolean disponible(){
        return disponibleDuendes;
    }
    
    public void llegue(){
        llegada++;
    }
    
    public int llegaron(){
        return llegada;
    }

    public void regresoReno() {
        renosRegreso++;
    }
    
    public int getRenosRegreso(){
        return renosRegreso;
    }
    
    public synchronized void anadirTurno(){
        turno++;
    }

    public synchronized int getTurno() {
        return turno;
    }
    
    public synchronized void ayudarDuendes(){
        numDuendeProblemas = 0;
        turno = 0;
        llegada = 0;
    }

    public synchronized void anadirDuendeProblemas() {
        numDuendeProblemas++;
    }

    public int getDuendesProblemas() {
        return numDuendeProblemas;
    }
    
    public int iniciarReno(){
        serieRenos++;
        return serieRenos;
    }
    
    public int iniciarDuende(){
        serieDuendes++;
        return serieDuendes;
    }

}
