package chat;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface interfaceRMI extends Remote {
    void mensaje(String mensaje) throws RemoteException;
    void registrar(chatCliente cliente) throws RemoteException;
}