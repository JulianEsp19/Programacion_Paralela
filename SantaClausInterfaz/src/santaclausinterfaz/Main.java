package santaclausinterfaz;

import java.awt.Color;
import java.awt.Image;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Main extends JFrame{
    
    JLabel santa;
    
    JButton anadirDuende;
    
    SantaClaus santaClass;
    
    public Main(){
        setTitle("Santa claus");
        setSize(1500, 600);
        setLayout(null);
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        anadirDuende = new JButton("Añadir duende");
        anadirDuende.setBounds(50, 50, 150, 30);
        anadirDuende.addActionListener((e) -> { anadirDuende(); });
        add(anadirDuende);
        
        ImageIcon imagenAnimal1 = new ImageIcon("src/imagenes/grinch.png");
        Image imagen = imagenAnimal1.getImage();
        imagen = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imagenAnimal1 = new ImageIcon(imagen);
        
        santa = new JLabel(imagenAnimal1);
        santa.setBounds(50, 200, 100, 100);
        santa.setBackground(Color.red);
        add(santa);
        
        santaClass = new SantaClaus(santa);
        
        for (int i = 0; i < 9; i++) {
            Reno reno = new Reno(santaClass, this);
            reno.start();
        }
        
        
        setVisible(true);
    }
    
    private void anadirDuende(){
        Duende duende = new Duende(santaClass, this);
        duende.start();
    }

    public static void main(String[] args) {
        new Main();
    }
    
}
