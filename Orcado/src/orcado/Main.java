package orcado;

import java.util.Random;
import javax.swing.JFrame;
import orcado.vistas.Inicio;
import orcado.vistas.Juego;

public class Main extends JFrame{
    
    
    
    public Main(){
        setTitle("Orcado");
        setSize(1000, 500);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        
        new Inicio(this);
        
        System.out.println(new Random().nextInt(4));
        
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
    
}
