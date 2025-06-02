package chat;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class implementacionClienteChat extends UnicastRemoteObject implements chatCliente, Runnable {
    private interfaceRMI servidor;
    public String nombre;

    public implementacionClienteChat(String nombre, interfaceRMI servidor) throws RemoteException {
        this.nombre = nombre;
        this.servidor = servidor;
        servidor.registrar(this);
    }

    @Override
    public void mensajeCliente(String mensaje) throws RemoteException {
        System.err.println(mensaje);
    }

    
    @Override
    public void run() {
        java.util.Scanner sc = new java.util.Scanner(System.in);
        while (true) {
            System.out.print("[" + nombre + "]: ");
            String mensaje = sc.nextLine();
            try {
                servidor.mensaje("[" + nombre + "]: " + mensaje);
            } catch (RemoteException e) {
                System.err.println("Error al enviar mensaje: " + e.getMessage());
            }
        }
    }

}