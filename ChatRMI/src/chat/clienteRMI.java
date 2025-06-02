package chat;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import javax.swing.JOptionPane;

public class clienteRMI {
    public static void main(String[] args) {
        try {
            String nombre = JOptionPane.showInputDialog("Ingresa tu nombre");
            Registry rmi = LocateRegistry.getRegistry("192.168.137.37", 1099);
            interfaceRMI servidor = (interfaceRMI) rmi.lookup("chat");
            new Thread(new implementacionClienteChat(nombre, servidor)).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}