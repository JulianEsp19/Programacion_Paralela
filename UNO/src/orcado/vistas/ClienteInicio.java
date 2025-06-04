package orcado.vistas;


import herramientas.botones;
import interfaces.JuegoImple;
import java.awt.Color;
import java.awt.Font;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ClienteInicio extends JPanel {

    private JTextArea ip;

    public String nombre;

    private JFrame vista;

    private JButton conectar;

    private JuegoImple cliente;

    private final int widthVista;
    private final int heightVista;
    
    private int PUERTO = 300;

    private JLabel hostName,
            jugadores,
            lista,
            mensajePartida;

    public ClienteInicio(String nombre, JFrame vista) {
        this.nombre = nombre;
        this.vista = vista;

        widthVista = vista.getWidth();
        heightVista = vista.getHeight();

        //creacion de las especificaciones de la vista
        setBounds(0, 0, widthVista, heightVista);
        setBackground(Color.WHITE);
        setLayout(null);

        ip = new JTextArea();
        ip.setBounds(350, 50, 100, 30);
        ip.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        add(ip);

        conectar = botones.iniciarBotones("conectar", 100, 30);
        conectar.setBounds(470, 50, 100, 30);
        conectar.addActionListener((e) -> {
            try {
                conectar();
            } catch (Exception ex) {}
        });
        add(conectar);

        vista.add(this);
    }

    private void conectar() throws RemoteException, NotBoundException {
        Registry registro = LocateRegistry.getRegistry(ip.getText(), PUERTO);
        cliente = (JuegoImple) (registro.lookup("Juego"));
        cliente.iniciarJugador(nombre);
        
        iniciarPrePartida();
        threadActualizacion();
        
        conectar.setEnabled(false);
        ip.setEnabled(false);
    }
    
    public void iniciarPartida(){
        this.setVisible(false);
    }

    public void iniciarPrePartida() {

        Font fuente = new Font("Arial", Font.BOLD, 15);


        jugadores = new JLabel("Jugadores unidos:");
        jugadores.setBounds(700, 50, 200, 50);
        jugadores.setFont(fuente);
        add(jugadores);
        
        mensajePartida = new JLabel("ESPERANDO AL HOST...");
        mensajePartida.setBounds(350, 250, 300, 50);
        mensajePartida.setFont(fuente);
        add(mensajePartida);

        lista = new JLabel(nombre);
        lista.setBounds(700, 90, 200, 300);
        lista.setForeground(Color.blue);
        lista.setVerticalTextPosition(JLabel.TOP);
        lista.setAlignmentY(JLabel.TOP);
        lista.setVerticalAlignment(JLabel.TOP);
        lista.setFont(fuente);
        add(lista);

        hostName = new JLabel();
        hostName.setBounds(10, 50, 200, 50);
        hostName.setFont(fuente);
        add(hostName);
        
        repaint();
    }
    
    private void threadActualizacion(){
        new Thread(() -> {
            try {
                while (!cliente.juegoListo()) {
                        actualizarNombres(cliente.obtenerJugadores());
                        Thread.sleep(1000);
                }
                new Juego(vista, nombre, cliente);
                this.setVisible(false);
            } catch (Exception ex) {
                Logger.getLogger(HostInicio.class.getName()).log(Level.SEVERE, null, ex);
            }
        }).start();
    }
    
    public void actualizarNombres(String[] array){
        lista.setText("<html>");
        for (String usuario : array) {
            if (usuario != null) {
                lista.setText(lista.getText() + usuario + "<br>");
            }
        }
        lista.setText(lista.getText() + "<html>");
        
        hostName.setText("Nombre Host: " + array[0]);
        
        System.out.println("actualizado");
        repaint();
    }
}
