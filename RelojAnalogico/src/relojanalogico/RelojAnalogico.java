package relojanalogico;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class RelojAnalogico extends JFrame implements Runnable{
    
    private Image fondo;
    private Image buffer;
    private Thread hilo;
    
    int min; 
    int hora;
    int sec;
    
    int manecillasSegundos = 100;
    int manecillasMinutos = 70;
    int manecillasHora = 50;
    
    public RelojAnalogico(){
        setTitle("Reloj");
        setResizable(false);
        setSize(500, 500);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        
        hilo = new Thread(this);
        hilo.start();
    }
    
    public static void main(String[] args) {
        new RelojAnalogico();
    }
    
    @Override
    public void paint(Graphics g){
        if(fondo == null){
            fondo = createImage(getWidth(), getHeight());
            
            Graphics gFondo = fondo.getGraphics();
            gFondo.setClip(0, 0, getWidth(), getHeight());
            ImageIcon fondito = new ImageIcon("");
            
            gFondo.drawImage(fondito.getImage(), (getWidth()-300)/2,(getHeight()-300)/2, 300, 300, this);
            
        }
        
        update(g);
    }
    
    
    public void update(Graphics g){
        double xHora, yHora, anguloHora;
        
        g.setClip(0, 0, getWidth(), getHeight());
        
        Calendar cal = Calendar.getInstance();
        
        if(cal.get(Calendar.MINUTE) != min){
            hora = cal.get(Calendar.HOUR);
            
            
            buffer = createImage(getWidth(), getHeight());
            Graphics gbuffer = buffer.getGraphics();
            gbuffer.setClip(0, 0, getWidth(), getHeight());
            gbuffer.drawImage(fondo, 0, 0, this);
            
            
            System.out.println(hora);
            anguloHora = angulo12(hora);
            
            System.out.println("Angulo Hora: " + anguloHora);
            xHora = getX(anguloHora, manecillasHora);
            yHora = getY(anguloHora, manecillasHora);
            
            gbuffer.setColor(Color.WHITE);
            gbuffer.drawLine(getWidth()/2, getHeight()/2, (getHeight()/2)+(int) xHora, (getHeight()/2)+(int) yHora);
            
            g.drawImage(buffer, 0, 0, this);
            
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                repaint();
                hilo.sleep(1000);
            } catch (Exception e) {
            }
        }
    }
    
    
    private double getX(double angulo, int radio){
        double x = (double) radio*(double)(Math.sin(Math.toRadians(angulo)));
        return x;
    }
    
    private double getY(double angulo, int radio){
        double y = (double) radio*(double)(Math.cos(Math.toRadians(angulo)));
        return y;
    }
    
}
