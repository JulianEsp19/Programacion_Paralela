package saludo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class Saludo extends JFrame implements ActionListener{

    JLabel textoNombre;
    JLabel textoAuxiliar;
    
    JTextArea entradaNombre;
    
    JButton botonHoli;
    JButton botonAdios;
    JButton botonLimpiar;
    
    public Saludo(){
        setTitle("Mi ventana");
        setSize(500, 600);
        setLayout(null);
        
        textoNombre  = new JLabel("Nombre: ");
        textoNombre.setBounds(100, 100, 100, 20);
        add(textoNombre);
        
        textoAuxiliar = new JLabel();
        textoAuxiliar.setBounds(150, 200, 100, 20);
        add(textoAuxiliar);
        
        entradaNombre = new JTextArea();
        entradaNombre.setBounds(200, 100, 100, 20);
        add(entradaNombre);
        
        botonHoli = new JButton("holi");
        botonHoli.setBounds(10, 400, 100, 20);
        botonHoli.addActionListener((e) -> {botonHoli();});
        add(botonHoli);
        
        botonAdios = new JButton("adios");
        botonAdios.setBounds(300, 400, 100, 20);
        botonAdios.addActionListener((e) -> {botonAdios();});
        add(botonAdios);
        
        botonLimpiar = new JButton("Limpiar");
        botonLimpiar.setBounds(150, 400, 100, 20);
        botonLimpiar.addActionListener((e) -> {botonLimpiar();});
        add(botonLimpiar);
        
        setVisible(true);
    }
    
    private void botonAdios(){
       textoAuxiliar.setText("Adios " + entradaNombre.getText());
    }
    private void botonHoli(){
        textoAuxiliar.setText("Holi " + entradaNombre.getText());
        
    }
    private void botonLimpiar(){
        textoAuxiliar.setText("");
    }
    
    
    public static void main(String[] args) {
      new Saludo();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
