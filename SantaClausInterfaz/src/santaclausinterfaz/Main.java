package santaclausinterfaz;

import java.awt.Color;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame{
    
    JPanel santa;
    
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
        
        santa = new JPanel();
        santa.setBounds(50, 200, 100, 100);
        santa.setBackground(Color.red);
        add(santa);
        
        santaClass = new SantaClaus();
        
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
