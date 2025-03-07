package reloj;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;

public class Segundos extends Thread {

    JLabel segundos;
    JLabel minutos;
    JLabel horas;
    volatile boolean pausa;

    @Override
    public void run() {
        while (true) {
            int intSegundos = Integer.parseInt(segundos.getText()) + 1;

            if (intSegundos < 10) {
                segundos.setText("0" + intSegundos);
            } else {
                segundos.setText(String.valueOf(intSegundos));
            }

            if (intSegundos == 60) {
                segundos.setText("00");
                int intMinutos = Integer.parseInt(minutos.getText()) + 1;
                if (intMinutos < 10) {
                    minutos.setText("0" + intMinutos);
                } else {
                    minutos.setText(String.valueOf(intMinutos));
                }
                
                if(intMinutos == 60){
                    minutos.setText("00");
                    int intHoras = Integer.parseInt(horas.getText()) + 1;
                if (intHoras < 10) {
                    horas.setText("0" + intHoras);
                } else {
                    horas.setText(String.valueOf(intHoras));
                }
                }
                
            }

            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Segundos.class.getName()).log(Level.SEVERE, null, ex);
            }

            while (pausa) {

            }
        }
    }
}
