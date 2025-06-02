package chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;

public class ImplementacionChat extends UnicastRemoteObject implements interfaceRMI {
    private ArrayList<chatCliente> clientes;

    public ImplementacionChat() throws RemoteException {
        clientes = new ArrayList<>();
    }

    @Override
    public void mensaje(String mensaje) throws RemoteException {
        int n = 0;
        while (n < clientes.size()) {
            clientes.get(n++).mensajeCliente(mensaje);
        }
    }

    @Override
    public void registrar(chatCliente cliente) throws RemoteException {
        this.clientes.add(cliente);
    }
}