package orcado.vistas;

import herramientas.JuegoHost;
import herramientas.botones;
import java.awt.Color;
import java.awt.Font;
import static java.lang.Thread.sleep;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.net.MalformedURLException;

public class HostInicio extends JPanel {

    public String nombre;

    private int widthVista;
    private int heightVista;

    private JuegoHost host;

    private JLabel titulo,
            hostName,
            jugadores,
            lista;

    private JButton iniciar;

    private JFrame vista;

    private String[] usuarios;
    private int conexionUsuarios = 1;

    public HostInicio(String nombre, JFrame vista) throws RemoteException, MalformedURLException {
        this.nombre = nombre;

        this.vista = vista;

        widthVista = vista.getWidth();
        heightVista = vista.getHeight();

        //creacion de las especificaciones de la vista
        setBounds(0, 0, widthVista, heightVista);
        setBackground(Color.WHITE);
        setLayout(null);

        host = new JuegoHost(nombre);
        

        Font fuente = new Font("Arial", Font.BOLD, 15);

        titulo = new JLabel("Datos Host: " + obtenerIPWiFi());
        titulo.setBounds(10, 400, 200, 50);
        titulo.setFont(fuente);
        add(titulo);

        hostName = new JLabel("Nombre Host:" + nombre);
        hostName.setBounds(10, 50, 200, 50);
        hostName.setFont(fuente);
        add(hostName);

        jugadores = new JLabel("Jugadores unidos:");
        jugadores.setBounds(700, 50, 200, 50);
        jugadores.setFont(fuente);
        add(jugadores);

        lista = new JLabel(nombre);
        lista.setBounds(700, 90, 200, 300);
        lista.setForeground(Color.blue);
        lista.setVerticalTextPosition(JLabel.TOP);
        lista.setAlignmentY(JLabel.TOP);
        lista.setVerticalAlignment(JLabel.TOP);
        lista.setFont(fuente);
        add(lista);

        iniciar = botones.iniciarBotones("INICIAR", 200, 50);
        iniciar.setBounds(350, 200, 200, 50);
        iniciar.addActionListener((e) -> {
            iniciar();
        });
        add(iniciar);

        threadActualizacion();
        
        vista.add(this);
    }

    public void iniciar() {
        host.iniciarJuego();
    }
    
    private void threadActualizacion(){
        new Thread(() -> {
            while (!host.juegoListo()) {
                actualizarNombres(host.obtenerJugadores());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    Logger.getLogger(HostInicio.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            new Juego(vista, nombre, host);
            this.setVisible(false);
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
        
        System.out.println("actualizado");
        repaint();
    }

    public String obtenerIPWiFi() {
        try {
            // Obtenemos todas las interfaces de red
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            // Iteramos por todas las interfaces disponibles
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = interfaces.nextElement();

                // Verificamos si el nombre de la interfaz comienza con "wlan" (para interfaces Wi-Fi)
                if (ni.getName().startsWith("wlan")) {
                    if (ni.isUp()) {
                        Enumeration<InetAddress> direcciones = ni.getInetAddresses();

                        // Iteramos por todas las direcciones asociadas a la interfaz
                        while (direcciones.hasMoreElements()) {
                            InetAddress direccion = direcciones.nextElement();

                            // Filtramos solo las direcciones IPv4 y descartamos las de loopback (127.0.0.1)
                            if (direccion instanceof Inet4Address && !direccion.isLoopbackAddress()) {
                                return direccion.getHostAddress(); // Retornamos la IP encontrada
                            }
                        }
                    }
                }
            }
        } catch (SocketException e) {
            // Manejo de excepciones: imprime el error para depuración
            e.printStackTrace();
        }

        // Si no se encuentra una IP válida, devolvemos un mensaje por defecto
        return "IP Wi-Fi no encontrada";
    }
}
