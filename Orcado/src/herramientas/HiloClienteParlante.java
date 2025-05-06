package herramientas;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Logger;
import java.util.logging.Level;
import javax.swing.JPanel;
import orcado.vistas.ClienteInicio;
import orcado.vistas.Juego;

public class HiloClienteParlante extends Thread {

    protected Socket sk;
    protected DataOutputStream dos;
    protected DataInputStream dis;
    private int id;
    private String ip = "";
    private ClienteInicio vista;
    private Juego juego;

    public HiloClienteParlante(String ip, ClienteInicio vista) {
        this.ip = ip;
        this.vista = vista;
    }

    @Override
    public void run() {
        try {
            sk = new Socket(ip, 8080);
            dos = new DataOutputStream(sk.getOutputStream());
            dis = new DataInputStream(sk.getInputStream());
            dos.writeUTF("{\"accion\":\"unirse\",\"nombre\":\"" + vista.nombre + "\"}");
            String respuesta = "";
            while (true) {
                respuesta = dis.readUTF();
                JsonObject json = JsonParser.parseString(respuesta).getAsJsonObject();
                if(json.get("accion").getAsString().equals("actualizacionPrePartida")){
                    vista.actualizacionPrePartida(json);
                }else if(json.get("accion").getAsString().equals("iniciar")){
                    vista.iniciarPartida(json);
                }else if(json.get("accion").getAsString().equals("actualizarPartida")){
                    if(juego != null) juego.actualizacion(json);
                }
                System.out.println(respuesta);
                sleep(100);
            }
        } catch (IOException ex) {
            Logger.getLogger(HiloClienteParlante.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InterruptedException ex) {
            Logger.getLogger(HiloClienteParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void setJuego(Juego juego) {
        this.juego = juego;
    }
    
    public void enviarMensaje(String mensaje){
        try {
            dos.writeUTF(mensaje);
        } catch (IOException ex) {
            Logger.getLogger(ServidorMultiParlante.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

}
