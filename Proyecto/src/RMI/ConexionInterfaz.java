package RMI;

import java.nio.file.Path;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ConexionInterfaz extends Remote {

    void anadirAudio(String ruta, String rutaGlobal) throws RemoteException;

    String getMensaje() throws RemoteException;
    
    int getContador() throws RemoteException;
    
    String getRuta() throws RemoteException;
    
}
