package canibales;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Main extends JFrame {
    
    JLabel comida;
    
    JLabel textoRaciones;
    
    JButton anadirCanibal;
    
    Comida comidaClass;
    
    Cocinero cocinero;
    
    public Main(){
        setTitle("Canibales comenzales");
        setSize(1000, 600);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        
        anadirCanibal = new JButton("añadir Canibal");
        anadirCanibal.setBounds(20, 20, 150, 50);
        anadirCanibal.addActionListener((e) -> { anadirCanibal(); });
        add(anadirCanibal);
        
        
        ImageIcon imagenAnimal1 = new ImageIcon("src/imagenes/caldero.png");
        Image imagen = imagenAnimal1.getImage();
        imagen = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imagenAnimal1 = new ImageIcon(imagen);
        comida = new JLabel(imagenAnimal1);
        comida.setBounds(450, 50, 100, 100);
        comida.setBackground(Color.ORANGE);
        add(comida);
        
        textoRaciones = new JLabel("Raciones: " + 0);
        textoRaciones.setBounds(470, 30, 100, 20);
        add(textoRaciones);
        
        comidaClass = new Comida(textoRaciones);
        
        cocinero = new Cocinero(comidaClass, this);
        cocinero.start();
        
        setVisible(true);
    }
    
    private void anadirCanibal(){
        Canibal canibal = new Canibal(comidaClass, this, cocinero);
        canibal.start();
    }

    public static void main(String[] args) {
        new Main();
    }
    
}
