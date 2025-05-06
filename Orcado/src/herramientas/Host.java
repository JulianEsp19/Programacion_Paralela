package herramientas;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import orcado.vistas.HostInicio;
import orcado.vistas.Juego;

public class Host extends Thread {

    private HostInicio vista;

    private final int PUERTO = 8080;

    private ServidorMultiParlante[] conexiones = new ServidorMultiParlante[3];

    public Host(HostInicio vista) {
        this.vista = vista;
    }

    public void run() {
        ServerSocket ss;
        System.out.print("Inicializando servidor...");
        try {
            ss = new ServerSocket(PUERTO);
            System.out.println("\t[OK]");
            int idSession = 0;
            while (true) {
                Socket socket;
                socket = ss.accept();
                System.out.println("Nueva conexión entrante: " + socket);
                conexiones[idSession] = new ServidorMultiParlante(socket, idSession, vista);
                conexiones[idSession].start();
                idSession++;
            }
        } catch (IOException ex) {
            Logger.getLogger(Host.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void actualizarUnion(String nombre, String[] usuarios) {
            String usuariosJson = "";
            for (String usuario : usuarios) {
                usuariosJson += "\"" + usuario + "\",";
            }

            String mensaje = "{\"accion\":\"actualizacionPrePartida\","
                    + "\"nombre\":\"" + vista.nombre + "\","
                    + "\"jugadores\":["
                    + usuariosJson
                    + "]"
                    + "}";
        for (ServidorMultiParlante conexion : conexiones) {
            if(conexion != null) conexion.enviarMensaje(mensaje);
        }
    }
    
    public void enviarMensaje(String mensaje){
        for (ServidorMultiParlante conexion : conexiones) {
            if(conexion != null) conexion.enviarMensaje(mensaje);
        }
    }
    
    public void settiarJuego(Juego juego){
        for (ServidorMultiParlante conexion : conexiones) {
            if(conexion != null) conexion.setJuego(juego);
        }
    }

}
