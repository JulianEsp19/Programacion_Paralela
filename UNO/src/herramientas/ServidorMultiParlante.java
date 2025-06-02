package herramientas;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.net.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import orcado.vistas.HostInicio;
import orcado.vistas.Juego;

public class ServidorMultiParlante extends Thread {

    private Socket socket;
    private DataOutputStream dos;
    private DataInputStream dis;
    private int idSessio;
    private HostInicio vista;
    private Juego juego;

    public ServidorMultiParlante(Socket socket, int id, HostInicio vista) {
        this.socket = socket;
        this.idSessio = id;
        this.vista = vista;
        try {
            dos = new DataOutputStream(socket.getOutputStream());
            dis = new DataInputStream(socket.getInputStream());
        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void run() {
        String accion = "";
        try {
            while (true) {
                accion = dis.readUTF();
                JsonObject json = JsonParser.parseString(accion).getAsJsonObject();
                System.out.println(json.get("accion").getAsString());
                if(json.get("accion").getAsString().equals("unirse")){
                    vista.unirse(json.get("nombre").getAsString(), this);
                }else if(json.get("accion").getAsString().equals("actualizarPartida")){
                    if(juego != null) juego.actualizacion(json);
                }
                sleep(100);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void enviarMensaje(String mensaje){
        try {
            dos.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }

    public void desconectar() {
        try {
            socket.close();
        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
