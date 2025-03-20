package santaclausinterfaz;

import java.awt.Color;
import static java.lang.Thread.sleep;
import javax.swing.JPanel;

public class Reno extends Thread{
    private SantaClaus santa;
        private int probabilidad = 20;
        private boolean regreso;
        private Main vista;
        private JPanel reno;
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
                }
                
                try{
                    sleep(1000);
                }catch(Exception e){
                    
                }
            }
        }
        
        private void iniciarReno(){
            serieReno = santa.iniciarReno();
            reno = new JPanel();
            reno.setBounds(100+(serieReno*110), 50, 100, 100);
            reno.setBackground(Color.gray);
            reno.setVisible(regreso);
            vista.add(reno);
            vista.repaint();
        }
        
        private void verRegreso(){
            int probabilidad = (int) (Math.random() * this.probabilidad) + 1;
            if(probabilidad == 1){
                regreso = true;
                reno.setVisible(regreso);
            }
        }
}
