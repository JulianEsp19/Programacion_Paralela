package chat;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class servidorRMI {
    public static void main(String[] args) {
        try {
            Registry rmi = LocateRegistry.createRegistry(1099);
            rmi.rebind("chat", new ImplementacionChat());
            System.out.println("Servidor Activo");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}