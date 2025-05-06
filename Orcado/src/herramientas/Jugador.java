package herramientas;

import javax.swing.JLabel;

public class Jugador {
    
    private String nombre;
    
    private JLabel info;
    
    private int puntos = 0;

    public Jugador(String nombre, JLabel info) {
        this.nombre = nombre;
        this.info = info;
    }

    public String getNombre() {
        return nombre;
    }
    
    public void actualizar(){
        info.setText("<html> <center>"+ nombre + "<br>" + puntos + " </center> </html>");
    }
    
    public void correcto(){
        puntos+= 100;
        actualizar();
    }
    
    public void disminuir(){
        puntos -= 50;
        actualizar();
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
        actualizar();
    }

    public int getPuntos() {
        return puntos;
    }
}
