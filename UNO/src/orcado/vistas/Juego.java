package orcado.vistas;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import herramientas.HiloClienteParlante;
import herramientas.Host;
import herramientas.Jugador;
import herramientas.PoolFrases;
import herramientas.botones;
import herramientas.imagenes;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.util.Random;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Juego extends JPanel {

    private final int widthVista;
    private final int heightVista;

    private final boolean isHost;

    private final JFrame vista;

    private Integer[] cartas = new Integer[60];

    private int indiceBaraja = 1,
            color = 0,
            cartaCentro;

    private int jugadores;

    private JLabel baraja,
            centro;

    private JLabel[] mano = new JLabel[16];

    private String frase,
            letrasUsadas = "",
            nombre;

    private int turno = 0;

    private Jugador[] infoJugadores = new Jugador[4];

    private JLabel[] labelJugador = new JLabel[4];

    private Host host;

    private HiloClienteParlante cliente;

    public Juego(JFrame vista, JPanel partida, String nombre, String[] jugadoresName, int cantidad, Host host) {
        this.vista = vista;
        this.isHost = true;
        this.jugadores = cantidad;
        this.nombre = nombre;
        this.host = host;

        widthVista = vista.getWidth();
        heightVista = vista.getHeight();

        host.settiarJuego(this);

        iniciarArrayAleatorio();

        iniciarVista();
        iniciarJugadores(jugadoresName);
        iniciarMano();

        actualizarClientes();
    }

    public Juego(JFrame vista, String nombre, String[] jugadores, int cantidad, HiloClienteParlante cliente) {
        this.nombre = nombre;
        this.vista = vista;
        this.isHost = false;
        this.cliente = cliente;
        this.jugadores = cantidad;

        cliente.setJuego(this);

        widthVista = vista.getWidth();
        heightVista = vista.getHeight();

        iniciarVista();

        iniciarJugadores(jugadores);
    }

    private void iniciarMano() {
        indiceBaraja = (jugadores * 5) + 1;

        for (int i = 0; i < jugadores; i++) {
            if (infoJugadores[i].getNombre() == nombre) {
                for (int j = 0; j < 5; j++) {
                    mano[j] = new JLabel(imagenes.obtenerImagenEscalada("src/cartas" + cartas[j + (i * 5) + 1] + ".jpg", 70, 105));
                    mano[j].setBounds(50 + (j * 80), 300, 70, 105);
                    add(mano[j]);
                }
            }
        }

        repaint();

    }

    private void iniciarArrayAleatorio() {
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = i;
        }

        for (int i = cartas.length - 1; i > 0; i--) {
            // calculamos un índice aleatorio dentro del rango permitido
            int shuffled_index = (int) Math.floor(Math.random() * (i + 1));
            //guardamos el elemento que estamos iterando
            int tmp = cartas[i];
            // asignamos el elemento aleatorio al índice iterado
            cartas[i] = cartas[shuffled_index];
            // asignamos el elemento iterado al índice aleatorio
            cartas[shuffled_index] = tmp;
        }

        if (cartas[0] > 39) {
            iniciarArrayAleatorio();
        }
        
        color = cartas[0] / 10;
        
        System.out.println("color: "+color);
        
        cartaCentro = cartas[0];
        
        System.out.println("carta centro: " + cartaCentro);
    }

    private void iniciarVista() {

        //creacion de las especificaciones de la vista
        setBounds(0, 0, widthVista, heightVista);
        setBackground(Color.WHITE);
        setLayout(null);

        Font fuente = new Font("Arial", Font.BOLD, 20);

        baraja = new JLabel(imagenes.obtenerImagenEscalada("src/uno.jpg", 70, 105));
        baraja.setBounds(50, 50, 70, 105);
        add(baraja);

        centro = new JLabel(imagenes.obtenerImagenEscalada("src/cartas" + cartas[0] + ".jpg", 70, 105));
        centro.setBounds(400, 100, 70, 105);
        add(centro);
        System.out.println(cartas[0]);
        

        vista.add(this);
    }

    private void actualizarHost() {
        String puntajes = "[";
        for (int i = 0; i < jugadores; i++) {
            puntajes += infoJugadores[i].getPuntos() + ",";
        }
        puntajes += "]";

        String mensaje = "{\"accion\":\"actualizarPartida\","
                + "\"turno\":" + turno + ","
                + "\"letrasUsadas\": \"" + letrasUsadas + "\","
                + "\"frase\": \"" + frase + "\","
                + "\"fotograma\":" + fotograma + ","
                + "\"puntajes\":" + puntajes
                + "}";
        cliente.enviarMensaje(mensaje);
    }

    public void actualizacion(JsonObject json) {
        turno = json.get("turno").getAsInt();
        letrasUsadas = json.get("letrasUsadas").getAsString();
        color = json.get("color").getAsInt();
        indiceBaraja = json.get("indiceBaraja").getAsInt();
        cartaCentro = json.get("cartaCentro").getAsInt();
        

        actualizarTurno();
        centro.setIcon(imagenes.obtenerImagenEscalada("src/cartas" + cartaCentro + ".jpg", 150, 300));
        actualizarBaraja(json);
        if (isHost) {
            actualizarClientes();
        }
    }

    public void actualizarBaraja(JsonObject json) {
        JsonArray array = json.get("baraja").getAsJsonArray();

        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = array.get(i).getAsInt();
        }
    }

    private void actualizarClientes() {
        String puntajes = "[";
        for (int i = 0; i < cartas.length; i++) {
            puntajes += cartas[i] + ",";
        }
        puntajes += "]";

        String mensaje = "{\"accion\":\"actualizarPartida\","
                + "\"turno\":" + turno + ","
                + "\"color\":" + color + ","
                + "\"indiceBaraja\":" + indiceBaraja + ","
                + "\"cartaCentro\":" + cartaCentro + ","
                + "\"baraja\":" + puntajes
                + "}";

        host.enviarMensaje(mensaje);
    }

    private void actualizarTurno() {
        if (turno == 0) {
            labelJugador[jugadores - 1].setForeground(Color.black);
            labelJugador[turno].setForeground(Color.blue);
        } else {
            labelJugador[turno - 1].setForeground(Color.black);
            labelJugador[turno].setForeground(Color.blue);
        }
    }

    private void iniciarJugadores(String[] jugadoresName) {

        for (int i = 0; i < jugadores; i++) {

            System.out.println(jugadoresName[i]);
            labelJugador[i] = new JLabel();
            labelJugador[i].setBounds(50 + (i * 250), 400, 100, 70);
            add(labelJugador[i]);

            infoJugadores[i] = new Jugador(jugadoresName[i], labelJugador[i]);
            infoJugadores[i].actualizar();
        }

        labelJugador[0].setForeground(Color.blue);

        repaint();
    }
}
