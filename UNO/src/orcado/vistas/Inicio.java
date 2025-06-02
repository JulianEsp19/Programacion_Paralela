package orcado.vistas;

import herramientas.imagenes;
import herramientas.botones;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class Inicio extends JPanel{
    
    private int widthVista;
    private int heightVista;
    
    private JLabel titulo,
            imagen,
            nombreLabel;
    
    private JButton host,
            unirse;
    
    private JTextArea nombre;
    
    private JFrame vista;
    
    public Inicio(JFrame vista){
        
        this.vista = vista;
        
        widthVista = vista.getWidth();
        heightVista = vista.getHeight();
        
        
        //creacion de las especificaciones de la vista
        setBounds(0, 0, widthVista, heightVista);
        setBackground(Color.WHITE);
        setLayout(null);
        
        //añadir elementos graficos
        titulo = new JLabel("UNO");
        titulo.setBounds(50, 0, 200, 50);
        titulo.setFont(new Font("Arial", Font.BOLD, 50));
        add(titulo);
        
        imagen = new JLabel(imagenes.obtenerImagenEscalada("src/uno.jpg",
                300,
                300));
        imagen.setBounds(0, 100, 300, 300);
        add(imagen);
        
        host = botones.iniciarBotones("Crear Partida", 200, 50);
        host.addActionListener((e) -> { hostBoton(); });
        host.setBounds(500, 200, 200, 50);
        add(host);
        
        unirse = botones.iniciarBotones("Unirse Partida", 200, 50);
        unirse.addActionListener((e) -> { clienteBoton(); });
        unirse.setBounds(500, 300, 200, 50);
        add(unirse);
        
        nombreLabel = new JLabel("Nombre:");
        nombreLabel.setBounds(500, 100, 50, 50);
        add(nombreLabel);
        
        nombre = new JTextArea();
        nombre.setBounds(570, 115, 100, 20);
        nombre.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(nombre);
        
        vista.add(this);
    }
    
    private void hostBoton(){
        this.setVisible(false);
        new HostInicio(nombre.getText(), vista);
    }
    
    private void clienteBoton(){
        this.setVisible(false);
        new ClienteInicio(nombre.getText(), vista);
    }
}
