package orcado.vistas;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import herramientas.HiloClienteParlante;
import herramientas.botones;
import java.awt.Color;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

public class ClienteInicio extends JPanel {

    private JTextArea ip;

    public String nombre;

    private JFrame vista;

    private JButton conectar;

    private HiloClienteParlante cliente;

    private int widthVista;
    private int heightVista;

    private JLabel titulo,
            hostName,
            jugadores,
            lista,
            mensajePartida;

    private String[] usuarios = new String[4];

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
            conectar();
        });
        add(conectar);

        vista.add(this);
    }

    private void conectar() {
        cliente = new HiloClienteParlante(ip.getText().strip(), this);
        cliente.start();
        conectar.setEnabled(false);
        ip.setEnabled(false);
    }
    
    public void iniciarPartida(JsonObject json){
        int jugadores = json.get("jugadores").getAsInt();
        
        new Juego(vista, nombre, usuarios, jugadores, cliente);
        this.setVisible(false);
    }

    public void actualizacionPrePartida(JsonObject json) {

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

        JsonArray array = json.get("jugadores").getAsJsonArray();

        for (int i = 0; i < 4; i++) {
            usuarios[i] = array.get(i).getAsString();
        }

        lista.setText("<html>");
        for (String usuario : usuarios) {
            if (!"null".equals(usuario)) {
                lista.setText(lista.getText() + usuario + "<br>");
            }
        }
        lista.setText(lista.getText() + "<html>");

        hostName = new JLabel("Nombre Host:" + usuarios[0]);
        hostName.setBounds(10, 50, 200, 50);
        hostName.setFont(fuente);
        add(hostName);
        
        repaint();
    }
}
