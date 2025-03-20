package puente;

import java.awt.Button;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame{
    
    Button anadirNorte,
           anadirSur;
    
    JPanel puenteArriba,
           puenteAbajo;
    
    Puente puenteClass;
    
    public Main(){
        setTitle("Púente");
        setSize(1000, 400);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        puenteClass = new Puente();
        
        anadirNorte = new Button("añadir NORTE");
        anadirNorte.setBounds(100, 50, 100, 50);
        anadirNorte.addActionListener((e) -> { anadirNorte(); });
        add(anadirNorte);
        
        
        anadirSur = new Button("añadir SUR");
        anadirSur.setBounds(700, 50, 100, 50);
        anadirSur.addActionListener((e) -> { anadirSur(); });
        add(anadirSur);
        
        
        puenteArriba = new JPanel();
        puenteArriba.setBounds(350, 200, 300, 10);
        puenteArriba.setBackground(Color.GRAY);
        add(puenteArriba);
        puenteAbajo = new JPanel();
        puenteAbajo.setBounds(350, 300, 300, 10);
        puenteAbajo.setBackground(Color.GRAY);
        add(puenteAbajo);
        
        
        
        setVisible(true);
    }
    
    private void anadirNorte(){
        puenteClass.anadirCocheNorte();
        Coche coche = new Coche(true, this, puenteClass);
        coche.start();
    }
    
    private void anadirSur(){
        puenteClass.anadirCocheSur();
        Coche coche = new Coche(false, this, puenteClass);
        coche.start();
    }

    public static void main(String[] args) {
        new Main();
    }
    
}
