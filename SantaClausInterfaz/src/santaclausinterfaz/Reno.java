package santaclausinterfaz;

import java.awt.Color;
import java.awt.Image;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Reno extends Thread{
    private SantaClaus santa;
        private int probabilidad = 20;
        private boolean regreso;
        private Main vista;
        private JLabel reno;
        private int serieReno;
        
        public Reno(SantaClaus santa, Main vista){
            this.santa = santa;
            this.vista = vista;
            regreso = false;
        }
        
        @Override
        public void run(){
            iniciarReno();
            while(true){
                if(!regreso){
                    verRegreso();
                }if(santa.getRenosRegreso() == 9){
                    santa.llegaronLosRenos();
                    reno.setVisible(false);
                    
                    try {
                        sleep(5000);
                    } catch (InterruptedException ex) {
                        Logger.getLogger(Reno.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    renoVacaciones();
                    santa.regresamosLosRenos();
                }
                
                try{
                    sleep(1000);
                }catch(Exception e){
                    
                }
            }
        }
        
        private void renoVacaciones(){
            regreso = false;
        }
        
        private void iniciarReno(){
            serieReno = santa.iniciarReno();
            
            ImageIcon imagenAnimal1 = new ImageIcon("src/imagenes/reno.png");
            Image imagen = imagenAnimal1.getImage();
            imagen = imagen.getScaledInstance(100,100, Image.SCALE_SMOOTH);
            imagenAnimal1 = new ImageIcon(imagen);
            
            reno = new JLabel(imagenAnimal1);
            reno.setBounds(100+(serieReno*110), 50, 100, 100);
            reno.setVisible(regreso);
            vista.add(reno);
            vista.repaint();
        }
        
        private void verRegreso(){
            int probabilidad = (int) (Math.random() * this.probabilidad) + 1;
            if(probabilidad == 1){
                regreso = true;
                reno.setVisible(regreso);
                santa.regresoReno();
                System.out.println("Reno: " + santa.getRenosRegreso());
            }
        }
}
