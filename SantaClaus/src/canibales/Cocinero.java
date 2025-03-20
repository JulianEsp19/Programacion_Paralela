package canibales;

import java.awt.Color;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Cocinero extends Thread {

    private Comida comida;
    private Main vista;
    private JLabel cocinero;
    private int canibalInforme;
    private JLabel mensaje;

    public Cocinero(Comida comida, Main vista) {
        this.comida = comida;
        this.vista = vista;
        canibalInforme = 0;
    }

    @Override
    public void run() {
        iniciarCocinero();
        try {
            while (true) {
                if (canibalInforme != 0) {
                    sleep(1000);
                    movimientoComida();
                    comida.anadirRaciones();
                    movimientoCanibalInforme();
                    mensaje();
                    sleep(1000);
                    quitarMensaje();
                    comida.abrirPaso(canibalInforme);
                    canibalInforme = 0;
                    movimientoLugar();
                }
                sleep(1000);
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Cocinero.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void mensaje(){
        mensaje.setLocation((100*canibalInforme)+80, 250);
        mensaje.setVisible(true);
    }
    
    private void quitarMensaje(){
        mensaje.setVisible(false);
    } 

    private void iniciarCocinero() {
        ImageIcon imagenAnimal1 = new ImageIcon("src/imagenes/remy.png");
        Image imagen = imagenAnimal1.getImage();
        imagen = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imagenAnimal1 = new ImageIcon(imagen);
        cocinero = new JLabel(imagenAnimal1);
        cocinero.setBounds(200, 50, 100, 100);
        cocinero.setBackground(Color.red);
        vista.add(cocinero);
        
        mensaje = new JLabel("Listo!!!");
        mensaje.setBounds(275, 130, 150, 30);
        mensaje.setVisible(false);
        vista.add(mensaje);
        
        vista.repaint();
    }

    private void movimientoCanibalInforme() {
        int x = cocinero.getX();
        int y = cocinero.getY();

        int xAux, yAux;
        while (true) {
            xAux = x;
            yAux = y;
            if (x > (100 * canibalInforme) + 50) {
                x--;
            } else if (x < (100 * canibalInforme) + 50) {
                x++;
            }

            if (y < 300) {
                y++;
            }

            cocinero.setLocation(x, y);

            if (xAux == x && yAux == y) {
                break;
            }

            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Canibal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void movimientoComida() {
        int x = cocinero.getX();
        int y = cocinero.getY();

        int xAux, yAux;
        while (true) {
            xAux = x;
            yAux = y;
            if (x > 475) {
                x--;
            } else if (x < 475) {
                x++;
            }

            if (y > 155) {
                y--;
            } else if (y < 155) {
                y++;
            }

            cocinero.setLocation(x, y);

            if (xAux == x && yAux == y) {
                break;
            }

            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Cocinero.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private void movimientoLugar() {
        int x = cocinero.getX();
        int y = cocinero.getY();

        int xAux, yAux;
        while (true) {
            xAux = x;
            yAux = y;
            if (x > 200) {
                x--;
            } else if (x < 200) {
                x++;
            }

            if (y > 50) {
                y--;
            }

            cocinero.setLocation(x, y);

            if (xAux == x && yAux == y) {
                break;
            }

            try {
                sleep(10);
            } catch (InterruptedException ex) {
                Logger.getLogger(Canibal.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void informeVacio(int canibalInforme) {
        this.canibalInforme = canibalInforme;
        comida.cerrarPaso();
        System.out.println(this.canibalInforme);
    }

}
