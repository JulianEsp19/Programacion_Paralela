package carreraanimlesinterfaz;

import javax.swing.JLabel;

public class MovimientoAnimal extends Thread{
    
    private JLabel animal;
    private int x;
    private int y;
    private int velocidad;

    public MovimientoAnimal(JLabel animal, int velocidad){
        this.animal = animal;
        this.velocidad = velocidad;
        x = animal.getX();
        y = animal.getY();
    }
    
    @Override
    public void run() {
        float velocidadMetros = (float) (velocidad/3.6);
        while(x<600){
            animal.setLocation(x, y);
            x+=1;
            try {
                sleep((long) (((600/velocidadMetros)/600)*1000));
            } catch (Exception e) {
            }
        }
        
    }
}
