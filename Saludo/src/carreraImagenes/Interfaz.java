package carreraImagenes;

import java.awt.Color;
import java.awt.TextArea;
import javax.swing.JFrame;

public class Interfaz extends JFrame {

    TextArea carril_1, 
            carril_2, 
            carril_3;

    public Interfaz() {
        setTitle("Carrera de animales 2");
        setSize(800, 400);
        setLayout(null);
        
        carril_1 = new TextArea();
        carril_1.setBounds(0,0,700,100);
        carril_1.setBackground(Color.GRAY);
        carril_1.setEditable(false);
        
        add(carril_1);
        
        
        setVisible(true);
    }

    public static void main(String[] args) {
        new Interfaz();
    }

}
