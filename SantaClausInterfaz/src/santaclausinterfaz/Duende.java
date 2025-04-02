package santaclausinterfaz;

import java.awt.Color;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JLabel;

public class Duende extends Thread {

    private SantaClaus santa;
    private int probabilidad = 10;
    private boolean problema;
    private int serieProblema;
    private Main vista;
    private JLabel duende;
    private int serieDuende;
    private JLabel texto;
    private boolean ayuda;
    private boolean primero;

    public Duende(SantaClaus santa, Main vista) {
        this.santa = santa;
        this.vista = vista;
        problema = false;
        serieProblema = 0;
        serieDuende = 0;
        ayuda = true;
        primero = false;
    }

    @Override
    public void run() {
        iniciarDuende();
        while (true) {

            if (santa.disponible()) {
                if (!problema) {
                    verProblema();
                } else if (santa.getDuendesProblemas() < 3 && ayuda) {
                    santa.anadirDuendeProblemas();
                    santa.anadirTurno();
                    System.out.println(santa.getDuendesProblemas());
                    if (santa.getDuendesProblemas() == 1) {
                        primero = true;
                    }
                    ayuda = false;
                    movimientoSanta();
                    santa.llegue();
                } else if (santa.llegaron() == 3 && !ayuda && primero) {
                    movimientoLugar();
                    santa.ayudarDuendes();
                    primero = false;
                } else if (santa.llegaron() == 3 && !ayuda) {
                    movimientoLugar();
                }
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
        problema = false;
    }

    private void iniciarDuende() {
        serieDuende = santa.iniciarDuende();

        ImageIcon imagenAnimal1 = new ImageIcon("src/imagenes/constructor.png");
        Image imagen = imagenAnimal1.getImage();
        imagen = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imagenAnimal1 = new ImageIcon(imagen);

        duende = new JLabel(imagenAnimal1);
        duende.setBounds(150 + (serieDuende * 110), 450, 100, 100);
        vista.add(duende);

        texto = new JLabel();
        texto.setText("UPS!!");
        texto.setBounds(150 + (serieDuende * 120), 380, 100, 100);
        texto.setVisible(false);
        vista.add(texto);

        vista.repaint();
    }

    private void verProblema() {
        int probabilidad = (int) (Math.random() * this.probabilidad) + 1;
        if (probabilidad == 1) {
            problema = true;
            texto.setVisible(true);
        }
    }

}
