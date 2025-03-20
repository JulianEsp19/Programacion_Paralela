package relojanalogico1;

import java.awt.Color;
import java.awt.Font;
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
    
    int sizeClock = 220;
    
    PixelArt pixel;
    
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
            
            pixel = new PixelArt();
            
            Graphics gFondo = fondo.getGraphics();
            gFondo.setClip(0, 0, getWidth(), getHeight());
            gFondo.setFont(new Font("arial", Font.BOLD, 20));
            gFondo.setColor(new Color(Integer.parseInt("dddddd", 16)));
            gFondo.fillRect(0, 0, getWidth(), getHeight());
            pixel.pintarKirby(gFondo, -30, 250, 5);
            pixel.pintarKirby(gFondo, 300, 250, 5);
            gFondo.setColor(Color.GREEN);
            gFondo.fillRect(0, 460, 500, 100);
            gFondo.setColor(Color.YELLOW);
            gFondo.fillOval(-50, -50, 200, 200);
            gFondo.setColor(Color.WHITE);
            gFondo.fillOval(400, 80, 100, 100);
            gFondo.fillOval(300, 100, 100, 100);
            gFondo.fillOval(400, 120, 100, 100);
            gFondo.fillOval(350, 120, 100, 100);
            gFondo.fillOval(350, 70, 100, 100);
            gFondo.setColor(Color.BLACK);
            gFondo.drawOval((getWidth()/2)-(sizeClock/2), (getHeight()/2)-(sizeClock/2), sizeClock, sizeClock);
            gFondo.setColor(Color.WHITE);
            gFondo.fillOval((getWidth()/2)-(sizeClock/2)+1, (getHeight()/2)-(sizeClock/2)+1, sizeClock-2, sizeClock-2);
            gFondo.setColor(Color.BLACK);
            for (int i = 1; i < 13; i++) {
                int angulo = obtenerAnguloHora(i);
                double x = getX(angulo, sizeClock/2-10);
                double y = getY(angulo, sizeClock/2-10);
                gFondo.drawString(String.valueOf(i), getWidth()/2+(int)x-7, getHeight()/2+(int)y+10);
            }
        }
        
        update(g);
    }
    
    
    public void update(Graphics g){
        double xHora, yHora, anguloHora;
        double xMin, yMin, anguloMin;
        double xSec, ySec, anguloSec;
        
        g.setClip(0, 0, getWidth(), getHeight());
        
        Calendar cal = Calendar.getInstance();
        buffer = createImage(getWidth(), getHeight());
        Graphics gbuffer = buffer.getGraphics();
        gbuffer.setClip(0, 0, getWidth(), getHeight());
        gbuffer.drawImage(fondo, 0, 0, this);
        
        if(cal.get(Calendar.HOUR) != -1){
            hora = cal.get(Calendar.HOUR);
            
            System.out.println(hora);
            anguloHora = obtenerAnguloHora(hora);
            
            System.out.println("Angulo Hora: " + anguloHora);
            xHora = getX(anguloHora, manecillasHora);
            yHora = getY(anguloHora, manecillasHora);
            
            gbuffer.setColor(Color.BLACK);
            gbuffer.drawLine(getWidth()/2, getHeight()/2, (getHeight()/2)+(int) xHora, (getHeight()/2)+(int) yHora);
            
            g.drawImage(buffer, 0, 0, this);
        }
        if(cal.get(Calendar.MINUTE) != -1){
            min = cal.get(Calendar.MINUTE);
            
            System.out.println(min);
            anguloMin = obtenerAnguloMinutosSegundos(min);
            
            System.out.println("Angulo minutos: "+anguloMin);
            xMin = getX(anguloMin, manecillasMinutos);
            yMin = getY(anguloMin, manecillasMinutos);
            
            gbuffer.setColor(Color.BLACK);
            gbuffer.drawLine(getWidth()/2, getHeight()/2, (getHeight()/2)+(int) xMin, (getHeight()/2)+(int) yMin);
            
            g.drawImage(buffer, 0, 0, this);
        }
        if(cal.get(Calendar.SECOND) != -1){
            sec = cal.get(Calendar.SECOND);
            
            System.out.println(sec);
            anguloSec = obtenerAnguloMinutosSegundos(sec);
            
            System.out.println("Angulo segundos: " + anguloSec);
            xSec = getX(anguloSec, manecillasSegundos);
            ySec = getY(anguloSec, manecillasSegundos);
            
            gbuffer.setColor(Color.BLACK);
            gbuffer.drawLine(getWidth()/2, getHeight()/2, (getHeight()/2)+(int) xSec, (getHeight()/2)+(int) ySec);
            
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
    
    private int obtenerAnguloHora(int hora){
        int resultado = -((360/12)*hora + 180);
        return resultado;
    }
    
    private int obtenerAnguloMinutosSegundos(int min){
        int angulo = -((360/60)*min + 180);
        return angulo;
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
