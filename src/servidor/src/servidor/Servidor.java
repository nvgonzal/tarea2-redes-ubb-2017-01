package servidor;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * Created by Nicolas on 00009, 09-07-2017.
 */
public class Servidor {

    public int puerto;
    private ServerSocket socketEscucha;

    /**
     * Crea un servidor de escucha con el puerto por defecto 3000
     */
    public Servidor(){
        this.puerto = 3000;
        crearServidorEscucha(this.puerto);
    }

    /**
     * Crea un servidor de escucha con el servidor con el puerto que se envia por parametro
     * @param puerto El puerto por el que el servidor recibira las peticiones del usuario
     */
    public Servidor(int puerto){
        this.puerto = puerto;
        crearServidorEscucha(this.puerto);
    }

    /**
     * Crea el objeto ServerSocket para recibir las conecciones con clientes
     * @param puerto El puerto en el que el ServerSocket escuchara
     */
    private void crearServidorEscucha(int puerto){
        ServerSocket s;
        try{
            //TerminalLogger.TLog("Creando socket de escucha.",TerminalLogger.CONTROL);
            s = new ServerSocket(puerto);
            this.socketEscucha = s;
            //TerminalLogger.TLog("Socket de escucha creado.",TerminalLogger.CONTROL);
        }
        catch (IOException e) {
            //TerminalLogger.TLog("Error al crear el socket de escucha: "+e.toString(),TerminalLogger.ERR);
            System.exit(-1);
        }

    }

}
