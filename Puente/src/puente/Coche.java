package puente;

import java.awt.Color;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Coche extends Thread {

    private boolean norte;
    private Main vista;
    private JLabel coche;
    private Puente puente;
    private boolean inUse;
    private int serial;

    public Coche(boolean norte, Main vista, Puente puente) {
        this.norte = norte;
        this.vista = vista;
        this.puente = puente;
        this.inUse = true;
    }

    @Override
    public void run() {
        pintarCoche();
        iniciarSerial();
        while (inUse) {
            try {
                if (norte && puente.isPasoNorte() && serial == puente.getPasoSerialNorte()) {
                    puente.puenteOcupadoNorte();
                    puente.aumentarSerialNorte();
                    movimiento();
                    verificar();
                    System.out.println(puente.getCochesNorte());
                } else if (!norte && puente.isPasoSur() && serial == puente.getPasoSerialSur()) {
                    puente.puenteOcupadoSur();
                    puente.aumentarSerialSur();
                    movimiento();
                    verificar();
                    System.out.println(puente.getCochesSur());
                }

                System.out.println("Serial: " + serial);
                System.out.println("Paso Serial: " + puente.getPasoSerialNorte());

                sleep(1000);
            } catch (InterruptedException ex) {
                Logger.getLogger(Coche.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void pintarCoche() {
        if (norte) {

            ImageIcon imagenAnimal1 = new ImageIcon("src/imagenes/coche.png");
            Image imagen = imagenAnimal1.getImage();
            imagen = imagen.getScaledInstance(50,50, Image.SCALE_SMOOTH);
            imagenAnimal1 = new ImageIcon(imagen);
            coche = new JLabel(imagenAnimal1);
            coche.setBounds(350 - (puente.getCochesNorte() * 60), 300, 50, 50);
            vista.add(coche);
            vista.repaint();
        } else {
            ImageIcon imagenAnimal1 = new ImageIcon("src/imagenes/coche2.png");
            Image imagen = imagenAnimal1.getImage();
            imagen = imagen.getScaledInstance(50,50, Image.SCALE_SMOOTH);
            imagenAnimal1 = new ImageIcon(imagen);
            coche = new JLabel(imagenAnimal1);
            coche.setBounds(600 + (puente.getCochesSur() * 60), 150, 50, 50);
            vista.add(coche);
            vista.repaint();
        }
    }

    private void iniciarSerial() {
        if (norte) {
            serial = puente.getSerialNorte();
        } else {
            serial = puente.getSerialSur();
        }
    }

    private void verificar() {
        if (norte && puente.getCochesNorte() == 0) {
            puente.puenteDesocupado();
        } else if (!norte && puente.getCochesSur() == 0) {
            puente.puenteDesocupado();
        }
    }

    private void movimiento() {
        int x = coche.getX();
        while (true) {
            if (norte) {
                x++;
            } else {
                x--;
            }

            coche.setLocation(x, 225);

            if (norte && x == 650) {
                puente.quitarCocheNorte();
                vista.remove(coche);
                inUse = false;
                break;
            } else if (!norte && x == 250) {
                puente.quitarCocheSur();
                vista.remove(coche);
                inUse = false;
                break;
            }

            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Coche.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
