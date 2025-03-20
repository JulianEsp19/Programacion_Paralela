package canibales;

import javax.swing.JLabel;

public class Comida {
    
    private int raciones;
    private int turnos;
    private int numTurno;
    private JLabel racionesVista;

    public Comida(JLabel racionesVista) {
        this.racionesVista = racionesVista;
        raciones = 0;
        turnos = 0;
        numTurno = 1;
    }
    
    public void anadirRaciones(){
        raciones = (int) (Math.random() * 15) + 1;
        racionesVista.setText("Raciones: " + raciones);
    }
    
    public void comer(){
        raciones--;
        racionesVista.setText("Raciones: " + raciones);
        if(numTurno == turnos) numTurno = 1;
        else numTurno++;
    }
    
    public void cerrarPaso(){
        numTurno = 0;
    }
    
    public void abrirPaso(int canibalInforme){
        numTurno = canibalInforme;
    }

    public int getRaciones() {
        return raciones;
    }
    
    public int anadirTurno(){
        turnos++;
        return turnos;
    }
    
    public void pasar(){
        numTurno++;
    }

    public int getTurnos() {
        return turnos;
    }

    public int getNumTurno() {
        return numTurno;
    }
    
}
