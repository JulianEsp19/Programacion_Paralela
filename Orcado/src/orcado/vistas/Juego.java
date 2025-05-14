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

    private final JButton[] teclado = new JButton[26];

    private final String abecedario = PoolFrases.abecedario;

    private final String[] pool = PoolFrases.frases;

    private int jugadores,
            fotograma = 0;

    private JLabel frames,
            fraseLabel;

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

        iniciarVista();

        seleccionarFrase();
        actualizarFraseLabel();
        iniciarJugadores(jugadoresName);

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

    private void iniciarVista() {

        //creacion de las especificaciones de la vista
        setBounds(0, 0, widthVista, heightVista);
        setBackground(Color.WHITE);
        setLayout(null);

        Font fuente = new Font("Arial", Font.BOLD, 20);

        frames = new JLabel(imagenes.obtenerImagenEscalada("src/fotograma000" + fotograma + ".png", 150, 300));
        frames.setBounds(20, 0, 100, 300);
        add(frames);

        fraseLabel = new JLabel();
        fraseLabel.setBounds(200, 50, 500, 100);
        fraseLabel.setFont(fuente);
        add(fraseLabel);

        iniciarTeclado();

        vista.add(this);
    }

    private void seleccionarFrase() {
        Random random = new Random();
        int numeroAleatorio = random.nextInt(pool.length);
        frase = pool[numeroAleatorio];

    }

    private void actualizarFraseLabel() {
        String aux = "";

        for (int i = 0; i < frase.length(); i++) {
            char caracter = frase.charAt(i);
            if (abecedario.contains(String.valueOf(caracter).toLowerCase())) {

                if (letrasUsadas.contains(String.valueOf(caracter).toLowerCase())) {
                    aux += caracter;
                } else {
                    aux += "_";
                }

            } else {
                aux += caracter;
            }
        }

        fraseLabel.setText(aux);
    }

    private void iniciarTeclado() {
        int y = 1,
                aux = 0;
        for (int i = 0; i < 26; i++) {

            if (0 == (float) (i % 10) && i != 0) {
                y++;
                aux = 0;
            }

            teclado[i] = botones.iniciarBotones(String.valueOf(abecedario.charAt(i)), 50, 50);

            switch (y) {
                case 1 ->
                    teclado[i].setBounds(150 + (aux * 53), 150, 50, 50);
                case 2 ->
                    teclado[i].setBounds(150 + (aux * 53), 203, 50, 50);
                default ->
                    teclado[i].setBounds(250 + (aux * 53), 256, 50, 50);
            }

            add(teclado[i]);
            teclado[i].addActionListener((e) -> {
                if (infoJugadores[turno].getNombre().equals(nombre)) {
                    clickBotonTeclado(e);
                }
            });
            aux++;
        }
    }

    private void clickBotonTeclado(ActionEvent e) {
        JButton boton = (JButton) e.getSource();
        letrasUsadas += boton.getText();
        if (frase.toLowerCase().contains(boton.getText())) {
            actualizarFraseLabel();
            verificar();
            infoJugadores[turno].correcto();
            if (turno < jugadores - 1) {
                turno++;
                actualizarTurno();
            } else {
                turno = 0;
                actualizarTurno();
            }
        } else if (fotograma < 5) {
            infoJugadores[turno].disminuir();
            if (turno < jugadores - 1) {
                turno++;
                actualizarTurno();
            } else {
                turno = 0;
                actualizarTurno();
            }
            fotograma += 2;
            frames.setIcon(imagenes.obtenerImagenEscalada("src/fotograma000" + fotograma + ".png", 150, 300));
        } else {
            frames.setIcon(imagenes.obtenerImagenEscalada("src/fotograma0007.png", 150, 300));
            fotograma = 7;
            gameOver();
        }
        boton.setEnabled(false);
        if (isHost) {
            actualizarClientes();
        } else {
            actualizarHost();
        }
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
        frase = json.get("frase").getAsString();
        fotograma = json.get("fotograma").getAsInt();

        actualizarTurno();
        actualizarFraseLabel();
        frames.setIcon(imagenes.obtenerImagenEscalada("src/fotograma000" + fotograma + ".png", 150, 300));
        desabilitarBotonesPorLetra();
        actualizarPuntajes(json);
        verificar();
        if (isHost) {
            actualizarClientes();
        }
    }

    public void actualizarPuntajes(JsonObject json) {
        JsonArray array = json.get("puntajes").getAsJsonArray();

        for (int i = 0; i < jugadores; i++) {
            infoJugadores[i].setPuntos(array.get(i).getAsInt());
        }
    }

    private void actualizarClientes() {
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

        host.enviarMensaje(mensaje);
    }

    private void desabilitarBotonesPorLetra() {
        for (JButton boton : teclado) {
            if (letrasUsadas.contains(boton.getText())) {
                boton.setEnabled(false);
            }
            System.out.println("_" + boton.getText() + "_");
        }
    }

    private void gameOver() {
        for (JButton boton : teclado) {
            boton.setEnabled(false);
        }
        System.out.println("se acabo");
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

    private void verificar() {
        if (!fraseLabel.getText().contains("_")) {
            System.out.println("Victoria");
            for (JButton boton : teclado) {
                boton.setEnabled(false);
            }
        }else if(fotograma == 7){
            for (JButton boton : teclado) {
                boton.setEnabled(false);
            }
            fraseLabel.setText(frase);
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
