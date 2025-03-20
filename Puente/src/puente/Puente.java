package puente;

public class Puente {
    
    private int cochesNorte;
    private int cochesSur;
    private boolean puente;
    private boolean pasoNorte;
    private boolean pasoSur;
    private int serialNorte;
    private int serialSur;
    private int pasoSerialSur;
    private int pasoSerialNorte;

    public Puente() {
        cochesNorte = 0;
        cochesSur = 0;
        puente = true;
        pasoNorte = true;
        pasoSur = true;
        pasoSerialNorte = 1;
        pasoSerialSur = 1;
        serialNorte = 0;
        serialSur = 0;
    }
    
    public void anadirCocheNorte(){
        cochesNorte++;
        serialNorte++;
    }
    
    public void quitarCocheNorte(){
        cochesNorte--;
        if(cochesNorte == 0) puenteDesocupado();
    }
    
    public void anadirCocheSur(){
        cochesSur++;
        serialSur++;
    }
    
    public void quitarCocheSur(){
        cochesSur--;
        if(cochesSur == 0) puenteDesocupado();
    }
    
    public void puenteOcupadoNorte(){
        puente = false;
        pasoNorte = true;
        pasoSur = false;
    }
    
    public void puenteOcupadoSur(){
        puente = false;
        pasoNorte = false;
        pasoSur = true;
    }
    
    public void puenteDesocupado(){
        puente = true;
        pasoNorte = true;
        pasoSur = true;
    }
    
    public void aumentarSerialNorte(){
        pasoSerialNorte++;
    }
    
    public void aumentarSerialSur(){
        pasoSerialSur++;
    }

    public int getSerialNorte() {
        return serialNorte;
    }

    public int getSerialSur() {
        return serialSur;
    }

    public int getPasoSerialSur() {
        return pasoSerialSur;
    }

    public int getPasoSerialNorte() {
        return pasoSerialNorte;
    }    
    
    public int getCochesNorte() {
        return cochesNorte;
    }

    public int getCochesSur() {
        return cochesSur;
    }

    public boolean isPuente() {
        return puente;
    }

    public synchronized boolean isPasoNorte() {
        return pasoNorte;
    }

    public synchronized boolean isPasoSur() {
        return pasoSur;
    }
    
}
