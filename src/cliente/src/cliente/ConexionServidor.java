package cliente;


import java.io.IOException;
import java.net.Socket;

/**
 * Created by Javiera.
 */
public class ConexionServidor {

    private String direccionIP;
    private int puerto;
    private Socket conexion;

    /**
     * Crea un socket con direccion por defecto localhost y puerto por defecto 3000
     */
    public ConexionServidor() {
        this.direccionIP = "localhost";
        this.puerto = 3000;
        conectar();
    }

    /**
     * Crea un socket con una direccion String y puerto por defecto 3000
     * @param direccionIP la direccion ip del servidor
     */
    public ConexionServidor(String direccionIP) {
        this.direccionIP = direccionIP;
        this.puerto = 3000;
        conectar();
    }

    public ConexionServidor(int puerto) {
        this.direccionIP = "localhost";
        this.puerto = puerto;
        conectar();
    }

    /**
     * Crea un socket con una direccion String y un puerto.
     * @param direccionIP la direccion ip del servidor
     * @param puerto el puerto por el que el servidor esta escuchando
     */
    public ConexionServidor(String direccionIP, int puerto) {
        this.direccionIP = direccionIP;
        this.puerto = puerto;
        conectar();
    }

    /**
     * Crea la conexion con el servidor
     */
    private void conectar() {
        try {
            this.conexion = new Socket(direccionIP, puerto);
        } catch (IOException e) {
            System.err.println("No se pudo crear la conexion. "+e.toString());
        }
    }

    /**
     * Prepara las peticione del cliente
     */
    protected void peticion() {
        ModuloSolicitudes m = new ModuloSolicitudes(this.conexion);
        m.solicitar();
    }
}