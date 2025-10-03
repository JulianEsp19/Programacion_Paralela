package RMI;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Conexion extends UnicastRemoteObject implements ConexionInterfaz{

    private static final long serialVersionUID = 1L;

    private Registry registro;
    
    private int PUERTO = 300;
    
    private String mensaje;
    
    private String ruta;
    
    private int contador;

    public Conexion () throws RemoteException {
        registro = LocateRegistry.createRegistry(PUERTO);
        mensaje = "";
        ruta = "";
        
        contador = 0;
        try {
            registro.bind("conexion", (ConexionInterfaz) this);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    @Override
    public int getContador(){
        return contador;
    }
    
    @Override
    public String getRuta(){
        return ruta;
        
    }
    
    @Override
    public void anadirAudio(String ruta, String rutaGlobal){
        mensaje += "<br> cliente Selecciono: " + ruta;
        this.ruta = rutaGlobal;
        contador ++;
    }
    
    @Override
    public String getMensaje(){
        return "<html>" + mensaje + "</html>";
    }

}
