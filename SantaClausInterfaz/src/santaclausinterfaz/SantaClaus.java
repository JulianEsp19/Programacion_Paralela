package santaclausinterfaz;

import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SantaClaus {

    private int renosRegreso;
    private boolean dormido;
    private int numDuendeProblemas;
    private int serieRenos;
    private int serieDuendes;
    private int turno;
    
    private boolean regresar;

    public SantaClaus() {
        renosRegreso = 0;
        dormido = true;
        serieRenos = 0;
        numDuendeProblemas = 0;
        turno = 0;
        regresar = false;
    }

    public void regresoReno() {
        renosRegreso++;
    }
    
    public void anadirTurno(){
        turno++;
    }

    public int getTurno() {
        return turno;
    }
    
    public synchronized void ayudarDuendes(){
        numDuendeProblemas = 0;
        turno = 0;
        regresar = true;
        
        try {
            sleep(3000);
        } catch (InterruptedException ex) {
            Logger.getLogger(SantaClaus.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        regresar = false;
    }

    public void anadirDuendeProblemas() {
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

    public boolean isRegresar() {
        return regresar;
    }

}
