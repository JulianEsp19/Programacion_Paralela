package herramientas;

import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import interfaces.JuegoImple;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class JuegoHost extends UnicastRemoteObject implements JuegoImple {

    private static final long serialVersionUID = 1L;

    private Integer[] cartas = new Integer[60];

    private int indiceBaraja,
            color,
            cartaCentro,
            numero,
            jugadoresCount,
            turno,
            PUERTO = 300;

    private final Integer[] saltarTurno = {43, 46, 49, 40};
    private final Integer[] girarTurnos = {44, 47, 50, 41};
    private final Integer[] masDos = {45, 48, 51, 42};

    /*
    0 = Pasar turno
    1 = girar turno
    2 = mas dos
    3 = cambio color
    4 = mas cuatro
     */
    private int efecto = -1,
            afectado = -1;

    private boolean sentido,
            ready,
            victoria;

    private String[] jugadores = new String[4];
    
    private boolean[] unos = new boolean[4];

    private Registry registro;

    public JuegoHost(String nombreHost) throws RemoteException {
        jugadoresCount = 1;
        jugadores[0] = nombreHost;
        registro = LocateRegistry.createRegistry(PUERTO);
        ready = false;
        this.sentido = true;
        victoria = false;
        try {
            registro.bind("Juego", (JuegoImple) this);
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void iniciarJuego() {
        for (int i = 0; i < cartas.length; i++) {
            cartas[i] = i;
        }

        for (int i = cartas.length - 1; i > 0; i--) {
            // calculamos un índice aleatorio dentro del rango permitido
            int shuffled_index = (int) Math.floor(Math.random() * (i + 1));
            //guardamos el elemento que estamos iterando
            int tmp = cartas[i];
            // asignamos el elemento aleatorio al índice iterado
            cartas[i] = cartas[shuffled_index];
            // asignamos el elemento iterado al índice aleatorio
            cartas[shuffled_index] = tmp;
        }

        if (cartas[0] > 39) {
            iniciarJuego();
            return;
        }

        indiceBaraja = (jugadoresCount * 5) + 1;
        cartaCentro = cartas[0];
        color = cartas[0] / 10;
        numero = cartaCentro % 10;
        System.out.println("color: " + color);
        System.out.println("numero: " + numero);
        turno = 0;
        ready = true;
    }

    private int getIndiceNombre(String nombre) {
        for (int i = 0; i < 4; i++) {
            if (jugadores[i].equals(nombre)) {
                return i;
            }
        }
        return -1;
    }

    private int isSaltarTurno(int indice) {
        for (int i = 0; i < 4; i++) {
            if (saltarTurno[i] == indice) {
                return i;
            }
        }
        return -1;
    }

    private int isGirarTurno(int indice) {
        for (int i = 0; i < 4; i++) {
            if (girarTurnos[i] == indice) {
                return i;
            }
        }
        return -1;
    }

    private int isMasDos(int indice) {
        for (int i = 0; i < 4; i++) {
            if (masDos[i] == indice) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean probarPoder(int indice) {
        if (indice < 52) {
            int colorAux;
            if (isSaltarTurno(indice) != -1) {
                colorAux = isSaltarTurno(indice);
                if (color == colorAux || isSaltarTurno(cartaCentro) != -1) {
                    cartaCentro = indice;
                    efecto = 0;
                    numero = -1;
                    color = colorAux;
                    return true;
                }
            } else if (isGirarTurno(indice) != -1) {
                colorAux = isGirarTurno(indice);
                if (color == colorAux || isGirarTurno(cartaCentro) != -1) {
                    cartaCentro = indice;
                    efecto = 1;
                    sentido = !sentido;
                    numero = -1;
                    color = colorAux;
                    System.out.println("sentido probar poder: " + sentido);
                    return true;
                }
            } else if (isMasDos(indice) != -1) {
                colorAux = isMasDos(indice);
                if (color == colorAux || isMasDos(cartaCentro) != -1) {
                    cartaCentro = indice;
                    efecto = 2;
                    afectado = getSiguiente();
                    numero = -1;
                    color = colorAux;
                    return true;
                }
            }
        } else {
            if (indice >= 56) {
                System.out.println("efecto Probar Poder: " + efecto);
                efecto = 4;
                cartaCentro = indice;
                afectado = getSiguiente();
                numero = -1;
                return true;
            } else {
                efecto = 3;
                cartaCentro = indice;
                numero = -1;
                return true;
            }
        }
        return false;
    }

    private int getSiguiente() {
        int turno = this.turno;
        if (!victoria) {

            if (this.sentido) {
                turno++;
                if (turno == jugadoresCount) {
                    turno = 0;
                }
            } else {
                turno--;
                if (turno == -1) {
                    turno = jugadoresCount - 1;
                }
            }
        }
        return turno;
    }
    
    @Override
    public void setVictoria() {
        victoria = true;
    }
    
    @Override
    public boolean getVictoria(){
        return victoria;
    }

    @Override
    public void consumirNombre() {
        afectado = -1;
    }

    @Override
    public int getCartaMazo() {
        if (indiceBaraja < 60) {
            indiceBaraja++;
            return cartas[indiceBaraja];
        }
        return -1;
    }

    @Override
    public int getColor() {
        return color;
    }

    @Override
    public void cambiarColor(int color) {
        this.color = color;
    }

    @Override
    public void efectoConsumido() {
        efecto = -1;
        afectado = -1;
    }

    @Override
    public int getEfecto() {
        return efecto;
    }

    @Override
    public String getAfectado() {
        if (afectado != -1) {
            return jugadores[afectado];
        }
        return "";
    }

    @Override
    public void pasarTurno() {
        System.out.println("pasar turno: " + this.sentido);
        System.out.println("efecto: " + this.efecto);
        
        if(!victoria){
            if (this.sentido) {
                turno++;
                if (turno == jugadoresCount) {
                    turno = 0;
                }

                if (efecto == 0 || efecto == 4 || efecto == 2) {
                    efectoConsumido();
                    turno = getSiguiente();
                }
            } else {
                turno--;
                if (turno == -1) {
                    turno = jugadoresCount - 1;
                }

                if (efecto == 0 || efecto == 4 || efecto == 2) {
                    efectoConsumido();
                    turno = getSiguiente();
                }
            }
        }
    }

    @Override
    public boolean jugarCarta(int indice) {
        int colorAux = indice / 10;
        int numeroAux = indice % 10;
        if (colorAux >= 4) {
            return probarPoder(indice);
        } else if (color == colorAux) {
            cartaCentro = indice;
            numero = numeroAux;
            return true;
        } else if (numero == numeroAux) {
            cartaCentro = indice;
            color = colorAux;
            return true;
        }
        return false;
    }

    @Override
    public String getNombreTurno() {
        return jugadores[turno];
    }

    @Override
    public Integer[] getMano(String nombre) {
        Integer[] mano = new Integer[5];
        int indice = (getIndiceNombre(nombre) * 5) + 1;
        for (int i = 0; i < 5; i++) {
            mano[i] = cartas[indice];
            indice++;
        }

        return mano;
    }

    @Override
    public int getCartaCentro() {
        return cartaCentro;
    }

    @Override
    public void iniciarJugador(String nombre) {
        jugadores[jugadoresCount] = nombre;
        jugadoresCount++;
    }

    @Override
    public String[] obtenerJugadores() {
        return jugadores;
    }

    @Override
    public boolean juegoListo() {
        return ready;
    }
    
    @Override
    public void uno(String nombre, boolean isUno){
        unos[getIndiceNombre(nombre)] = isUno;
    }
    
    @Override
    public String getUno(){
        String resultado = "<html> <center>jugadores con uno <br>";
        for (int i = 0; i < 4; i++) {
            if(unos[i] == true){
                resultado += jugadores[i] + "<br>";
            }
        }
        resultado += "</center> </html>";
        return resultado;
    }

}
