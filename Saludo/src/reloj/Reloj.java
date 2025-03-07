package reloj;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;

public class Reloj extends JFrame implements ActionListener{
    
    volatile JLabel textoSegundos;
    volatile JLabel textoMinutos;
    volatile JLabel textoHoras;
    
    JButton botonInicio;
    JButton botonPausar;
    JButton botonReiniciar;
    
    Segundos segundos;
    
    boolean pausa;

    public Reloj(){
        setTitle("Reloj");
        setSize(800, 400);
        setLayout(null);
        
        textoSegundos = new JLabel("00", JLabel.CENTER);
        textoSegundos.setBounds(500, 100, 200, 100);
        textoSegundos.setFont(new Font("Arial", Font.BOLD, 100));
        add(textoSegundos);
        
        textoMinutos = new JLabel("00", JLabel.CENTER);
        textoMinutos.setBounds(300, 100, 200, 100);
        textoMinutos.setFont(new Font("Arial", Font.BOLD, 100));
        add(textoMinutos);
        
        textoHoras = new JLabel("00", JLabel.CENTER);
        textoHoras.setBounds(100, 100, 200, 100);
        textoHoras.setFont(new Font("Arial", Font.BOLD, 100));
        add(textoHoras);
        
        botonInicio = new JButton("Inicio");
        botonInicio.setBounds(100, 300, 100, 20);
        botonInicio.addActionListener((e) -> { botonIniciar();});
        add(botonInicio);
        
        botonPausar = new JButton("Pausar");
        botonPausar.setBounds(300, 300, 100, 20);
        botonPausar.addActionListener((e) -> {botonPausar();});
        add(botonPausar);
        
        botonReiniciar = new JButton("Reiniciar");
        botonReiniciar.setBounds(500, 300, 100, 20);
        botonReiniciar.addActionListener((e) -> { botonReiniciar();});
        add(botonReiniciar);
        
        
        segundos = new Segundos();
        segundos.segundos = textoSegundos;
        segundos.minutos = textoMinutos;
        segundos.horas = textoHoras;
        
        setVisible(true);
    }
    
    private void botonIniciar(){
        segundos.start();
    }
    
    private void botonReiniciar(){
        textoHoras.setText("00");
        textoMinutos.setText("00");
        textoSegundos.setText("00");
    }
    
    private void botonPausar(){
        if(!pausa){
            botonPausar.setText("Continuar");
            segundos.pausa = true;
        }else{
            botonPausar.setText("Pausar");
            segundos.pausa = false;
        }
        pausa = !pausa;
    }

    public static void main(String[] args) {
        new Reloj();
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
