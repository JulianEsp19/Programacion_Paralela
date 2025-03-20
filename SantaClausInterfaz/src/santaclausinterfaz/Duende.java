package santaclausinterfaz;

import java.awt.Color;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Duende extends Thread {

    private SantaClaus santa;
    private int probabilidad = 5;
    private boolean problema;
    private int serieProblema;
    private Main vista;
    private JPanel duende;
    private int serieDuende;
    private JLabel texto;
    private boolean ayuda;

    public Duende(SantaClaus santa, Main vista) {
        this.santa = santa;
        this.vista = vista;
        problema = false;
        serieProblema = 0;
        serieDuende = 0;
        ayuda = true;
    }

    @Override
    public void run() {
        iniciarDuende();
        while (true) {
            if (!problema) {
                verProblema();
            } else if (santa.getDuendesProblemas() < 3 && ayuda) {
                santa.anadirDuendeProblemas();
                santa.anadirTurno();
                System.out.println(santa.getDuendesProblemas());
                movimientoSanta();
                santa.anadirTurno();
                if (santa.getTurno() == 5) {
                    santa.ayudarDuendes();
                }
            } else if (santa.isRegresar()) {
                movimientoLugar();
            }

            try {
                sleep(1000);
            } catch (InterruptedException e) {

            }
        }
    }

    private void movimientoSanta() {
        texto.setVisible(false);

        int x = duende.getX();
        int y = duende.getY();
        int turno = santa.getTurno();

        int xAux, yAux;
        while (true) {

            xAux = x;
            yAux = y;

            if (x < (turno * 110)) {
                x++;
            } else if (x > (turno * 110)) {
                x--;
            }

            if (y < 310) {
                y++;
            } else if (y > 310) {
                y--;
            }

            duende.setLocation(x, y);

            if (xAux == x && yAux == y) {
                break;
            }

            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Duende.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        ayuda = false;
        problema = false;
    }

    private void movimientoLugar() {
        int x = duende.getX();
        int y = duende.getY();

        int xAux, yAux;
        while (true) {

            xAux = x;
            yAux = y;

            if (x < 150 + (serieDuende * 110)) {
                x++;
            } else if (x > 150 + (serieDuende * 110)) {
                x--;
            }

            if (y < 450) {
                y++;
            } else if (y > 450) {
                y--;
            }

            duende.setLocation(x, y);

            if (xAux == x && yAux == y) {
                break;
            }

            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Duende.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        ayuda = true;
    }

    private void iniciarDuende() {
        serieDuende = santa.iniciarDuende();

        duende = new JPanel();
        duende.setBounds(150 + (serieDuende * 110), 450, 100, 100);
        duende.setBackground(Color.GREEN);
        vista.add(duende);

        texto = new JLabel();
        texto.setBounds(150 + (serieDuende * 120), 380, 100, 100);
        vista.add(texto);

        vista.repaint();
    }

    private void verProblema() {
        int probabilidad = (int) (Math.random() * this.probabilidad) + 1;
        if (probabilidad == 1) {
            problema = true;
            texto.setText("UPS!!");
            texto.setVisible(true);
        }
    }

}
