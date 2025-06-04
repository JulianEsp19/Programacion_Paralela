package orcado.vistas;

import herramientas.JuegoHost;
import herramientas.Jugador;
import herramientas.botones;
import herramientas.imagenes;
import interfaces.JuegoImple;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.rmi.RemoteException;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import mano.Mano;

public class Juego extends JPanel {

    private final int widthVista;
    private final int heightVista;

    private final boolean isHost;

    private final JFrame vista;

    private JLabel baraja,
            centro,
            nombreLabel,
            turnoLabel;

    private JLabel[] colores = new JLabel[4];

    private Icon[] iconosColores = new Icon[4];

    private Mano mano;

    private String nombre;

    private JuegoHost host;

    private JuegoImple cliente;

    private boolean isCambioColor = false;

    public Juego(JFrame vista, String nombre, JuegoHost host) {
        this.vista = vista;
        this.isHost = true;
        this.nombre = nombre;
        this.host = host;

        widthVista = vista.getWidth();
        heightVista = vista.getHeight();

        mano = new Mano();

        iniciarVista();
        iniciarMano();
    }

    public Juego(JFrame vista, String nombre, JuegoImple cliente) {
        this.nombre = nombre;
        this.vista = vista;
        this.isHost = false;
        this.cliente = cliente;

        widthVista = vista.getWidth();
        heightVista = vista.getHeight();

        mano = new Mano();

        iniciarVista();
        iniciarMano();
    }

    private void iniciarMano() {
        Integer[] manoIndice;

        try {
            if (isHost) {
                manoIndice = host.getMano(nombre);
            } else {
                manoIndice = cliente.getMano(nombre);
            }

            for (int i = 0; i < manoIndice.length; i++) {
                System.out.println("mano: " + manoIndice[i]);
                JLabel aux = mano.agregar(manoIndice[i]);
                aux.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

                // Agregar el MouseListener
                aux.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        try {
                            if (getNombreTurno().equals(nombre) && !isCambioColor) {
                                clickCarta(e);
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                });
                add(aux);
            }
        } catch (Exception e) {
        }

        repaint();
    }

    private void actualizar() {
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {
                Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
            }
            while (true) {
                int auxCentro = getCartaCentro();

                if (auxCentro > 51 && !isCambioColor) {
                    int auxColor = getColor();
                    ocultarColores(auxColor);
                    colores[auxColor].setVisible(true);
                } else if (auxCentro < 51) {
                    ocultarColores();
                }

                if (getNombreAfectado().equals(nombre)) {
                    int efecto = getEfecto();
                    System.out.println("afectado");
                    if (efecto == 2) {
                        for (int i = 0; i < 2; i++) {
                            obtenerCarta();
                        }
                    } else if (efecto == 4) {
                        for (int i = 0; i < 4; i++) {
                            obtenerCarta();
                        }
                    }
                    consumir();
                }

                centro.setIcon(imagenes.obtenerImagenEscalada("src/cartas" + auxCentro + ".jpg", 70, 105));
                turnoLabel.setText("Turno: " + getNombreTurno());
                repaint();
            }
        }).start();
    }

    private void obtenerCarta() {
        JLabel aux = mano.agregar(getSiguienteCarta());
        aux.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Agregar el MouseListener
        aux.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (getNombreTurno().equals(nombre) && !isCambioColor) {
                        clickCarta(e);
                    }
                } catch (RemoteException ex) {
                    Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        add(aux);
    }

    private void clickCarta(MouseEvent e) throws RemoteException {
        JLabel aux = (JLabel) e.getComponent();
        int valorCarta = mano.buscarValorCarta(aux);
        if (isHost) {
            System.out.println("valor mano: " + valorCarta);
            System.out.println("juego: " + host.jugarCarta(valorCarta));
            if (host.jugarCarta(valorCarta)) {
                mano.eliminarCarta(aux);
                if (valorCarta > 51) {
                    isCambioColor = true;
                    for (JLabel color : colores) {
                        color.setVisible(true);
                    }
                } else {
                    host.pasarTurno();
                }
            }
        } else {
            if (cliente.jugarCarta(valorCarta)) {
                mano.eliminarCarta(aux);
                if (valorCarta > 51) {
                    isCambioColor = true;
                    for (JLabel color : colores) {
                        color.setVisible(true);
                    }
                } else {
                    cliente.pasarTurno();
                }
            }
        }
    }

    private void iniciarVista() {

        //creacion de las especificaciones de la vista
        setBounds(0, 0, widthVista, heightVista);
        setBackground(Color.WHITE);
        setLayout(null);

        Font fuente = new Font("Arial", Font.BOLD, 15);

        baraja = new JLabel(imagenes.obtenerImagenEscalada("src/uno.jpg", 70, 105));
        baraja.setBounds(50, 50, 70, 105);
        baraja.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        // Agregar el MouseListener
        baraja.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (getNombreTurno().equals(nombre) && !isCambioColor) {
                    obtenerCarta();
                    pasarTurno();
                }
            }
        });
        add(baraja);

        int auxCentro = getCartaCentro();

        centro = new JLabel(imagenes.obtenerImagenEscalada("src/cartas" + auxCentro + ".jpg", 70, 105));
        centro.setBounds(400, 100, 70, 105);
        add(centro);

        int auxY = 0;
        int auxX = 0;
        for (int i = 0; i < 4; i++) {
            iconosColores[i] = imagenes.obtenerImagenEscalada("src/color" + i + ".png", 100, 100);
            colores[i] = new JLabel(iconosColores[i]);
            colores[i].setBounds(320 + (auxX * 130), 50 + (auxY * 120), 100, 100);
            colores[i].setVisible(false);
            colores[i].setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

            // Agregar el MouseListener
            colores[i].addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    try {
                        if (getNombreTurno().equals(nombre) && isCambioColor) {
                            clickCambioColor(e);
                        }
                    } catch (RemoteException ex) {
                        Logger.getLogger(Juego.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
            add(colores[i]);
            if (i == 1) {
                auxX++;
            }
            if (i == 1) {
                auxY = 1;
            } else if (i == 2) {
                auxY = 0;
            } else if (i == auxY) {
                auxY++;
            }
        }

        nombreLabel = new JLabel("Nombre: " + nombre);
        nombreLabel.setBounds(50, 0, 200, 50);
        nombreLabel.setFont(fuente);
        nombreLabel.setForeground(Color.blue);
        add(nombreLabel);

        turnoLabel = new JLabel("Turno: " + getNombreTurno());
        turnoLabel.setBounds(800, 150, 100, 50);
        turnoLabel.setFont(fuente);
        turnoLabel.setForeground(Color.blue);
        add(turnoLabel);

        vista.add(this);
        actualizar();
    }

    private void clickCambioColor(MouseEvent e) throws RemoteException {
        Icon icono = ((JLabel) e.getComponent()).getIcon();
        for (int i = 0; i < 4; i++) {
            if (icono == iconosColores[i]) {
                if (isHost) {
                    host.cambiarColor(i);
                    ocultarColores();
                    host.pasarTurno();
                } else {
                    cliente.cambiarColor(i);
                    ocultarColores();
                    cliente.pasarTurno();
                }
                isCambioColor = false;
            }
        }
    }

    private void ocultarColores() {
        for (JLabel color : colores) {
            color.setVisible(false);
        }
    }
    
    private void ocultarColores(int color){
        for (int i = 0; i < 4; i++) {
            if(color != i){
                colores[i].setVisible(false);
            }
        }
    }

    private int getCartaCentro() {
        try {
            if (isHost) {
                return host.getCartaCentro();
            } else {
                return cliente.getCartaCentro();
            }
        } catch (Exception e) {
        }
        return -1;
    }

    private void consumir() {
        try {
            if (isHost) {
                host.consumirNombre();
            } else {
                cliente.consumirNombre();
            }
        } catch (Exception e) {
        }
    }

    private void pasarTurno() {
        try {
            if (isHost) {
                host.pasarTurno();
            } else {
                cliente.pasarTurno();
            }
        } catch (Exception e) {
        }
    }

    private String getNombreTurno() {
        try {
            if (isHost) {
                return host.getNombreTurno();
            } else {
                return cliente.getNombreTurno();
            }
        } catch (Exception e) {
        }
        return "";
    }

    private String getNombreAfectado() {
        try {
            if (isHost) {
                return host.getAfectado();
            } else {
                return cliente.getAfectado();
            }
        } catch (Exception e) {
        }
        return "";
    }

    private int getColor() {
        try {
            if (isHost) {
                return host.getColor();
            } else {
                return cliente.getColor();
            }
        } catch (Exception e) {
        }
        return -1;
    }

    private int getEfecto() {
        try {
            if (isHost) {
                return host.getEfecto();
            } else {
                return cliente.getEfecto();
            }
        } catch (Exception e) {
        }
        return -1;
    }

    private int getSiguienteCarta() {
        try {
            if (isHost) {
                return host.getCartaMazo();
            } else {
                return cliente.getCartaMazo();
            }
        } catch (Exception e) {
        }
        return -1;
    }
}
