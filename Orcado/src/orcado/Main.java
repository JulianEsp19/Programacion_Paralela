package orcado;

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
        
        setVisible(true);
    }

    public static void main(String[] args) {
        new Main();
    }
    
}
