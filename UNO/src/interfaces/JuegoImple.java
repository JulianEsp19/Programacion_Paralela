package interfaces;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface JuegoImple extends Remote{
    
    void iniciarJugador(String nombre) throws RemoteException;
    
    String[] obtenerJugadores() throws RemoteException;
    
    boolean juegoListo() throws RemoteException;
    
    int getCartaCentro() throws RemoteException;
    
    Integer[] getMano(String nombre) throws RemoteException;
    
    String getNombreTurno() throws RemoteException;
    
    boolean jugarCarta(int indice) throws RemoteException;
    
    void pasarTurno() throws RemoteException;
    
    int getEfecto() throws RemoteException;
    
    String getAfectado() throws RemoteException;
    
    void efectoConsumido() throws RemoteException;
    
    void cambiarColor(int color) throws RemoteException;
    
    int getColor() throws RemoteException;
    
    int getCartaMazo() throws RemoteException;
    
    boolean probarPoder(int indice) throws RemoteException;
    
    void consumirNombre() throws RemoteException;
    
    void setVictoria() throws RemoteException;
    
    boolean getVictoria() throws RemoteException;
    
    void uno(String nombre, boolean isUno) throws RemoteException;
    
    String getUno() throws RemoteException;
}
