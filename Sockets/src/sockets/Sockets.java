package sockets;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class Sockets {

    // Elegir un Puerto del rango 1-1024:
    public static final int PUERTO = 8080;

    public static void main(String[] args) throws IOException {
        ServerSocket S = new ServerSocket(PUERTO);
        System.out.println("Empezando: " + S);
        try {
            Socket socket = S.accept();
            try {
                System.out.println("Conexion aceptada: " + socket);
                BufferedReader entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter salida = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                while (true) {
                    String str = entrada.readLine();
                    if (str.equals("FIN")) break;
                    
                    System.out.println("Reproduciendo: " + str);
                    salida.println(str);
                }
            } finally {
                System.out.println("Cerrando...");
                socket.close();
            }
        } finally {
            S.close();
        }
    }

}
