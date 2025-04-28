package socketsMultiHiloCliente;

import java.io.IOException;
import java.util.ArrayList;

public class clienteMultiHilo {

    static final int MAX_HILOS = 1;
    public static final String HOST = "localhost";

    public static void main(String[] args) throws IOException, InterruptedException {
        ArrayList<Thread> clients = new ArrayList<Thread>();
        for (int i = 0; i < MAX_HILOS; i++) {
            clients.add(new HiloClienteParlante(i));
        }
        for (Thread thread : clients) {
            thread.start();
        }
    }

}
