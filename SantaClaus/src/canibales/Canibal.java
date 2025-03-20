package canibales;

import java.awt.Color;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Canibal extends Thread {

    private int turno;
    private Comida comida;
    private JLabel canibal;
    private Main vista;
    private Cocinero cocinero;
    private JLabel mensaje;

    public Canibal(Comida comida, Main vista, Cocinero cocinero) {
        this.comida = comida;
        this.vista = vista;
        this.cocinero = cocinero;
    }

    @Override
    public void run() {
        iniciarCanibal();
        System.out.println(turno);

        try {

            while (true) {
                if (turno == comida.getNumTurno()) {
                    movimientoComida();
                    if (comida.getRaciones() == 0) {
                        movimientoCocinero();
                        cocinero.informeVacio(turno);
                        mensaje();
                        sleep(500);
                        quitarMensaje();
                        movimientoLugar();
                    }else{
                        comida.comer();
                        movimientoLugar();
                    }
                }
                sleep(1000);
            }

        } catch (InterruptedException ex) {
            Logger.getLogger(Canibal.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void mensaje(){
        mensaje.setVisible(true);
    }
    
    private void quitarMensaje(){
        mensaje.setVisible(false);
    }

    private void iniciarCanibal() {
        turno = comida.anadirTurno();
        
        ImageIcon imagenAnimal1 = new ImageIcon("src/imagenes/canibal.png");
        Image imagen = imagenAnimal1.getImage();
        imagen = imagen.getScaledInstance(100, 100, Image.SCALE_SMOOTH);
        imagenAnimal1 = new ImageIcon(imagen);

        canibal = new JLabel(imagenAnimal1);
        canibal.setBounds((100 * turno) + 50, 400, 100, 100);
        canibal.setBackground(Color.GREEN);
        vista.add(canibal);
        
        mensaje = new JLabel("haz comida brother");
        mensaje.setBounds(275, 130, 150, 30);
        mensaje.setVisible(false);
        vista.add(mensaje);
        
        vista.repaint();
    }

    private void movimientoComida() {
        int x = canibal.getX();
        int y = canibal.getY();

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
            }

            canibal.setLocation(x, y);

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

    private void movimientoLugar() {
        int x = canibal.getX();
        int y = canibal.getY();

        int xAux, yAux;
        while (true) {
            xAux = x;
            yAux = y;
            if (x > (100 * turno) + 50) {
                x--;
            } else if (x < (100 * turno) + 50) {
                x++;
            }

            if (y < 400) {
                y++;
            }

            canibal.setLocation(x, y);

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

    private void movimientoCocinero() {
        int x = canibal.getX();
        int y = canibal.getY();

        int xAux, yAux;
        while (true) {
            xAux = x;
            yAux = y;
            if (x > 225) {
                x--;
            } else if (x < 225) {
                x++;
            }

            if (y < 130) {
                y++;
            }

            canibal.setLocation(x, y);

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

}
