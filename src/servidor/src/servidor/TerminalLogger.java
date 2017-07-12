package servidor;

import java.time.LocalDateTime;

/**
 * Created by Nicolas on 0012, 12, 07, 2017.
 */
public class TerminalLogger {
    public static final int ERR = 0;
    public static final int CONTROL = 1;
    public static final int CONN = 2;
    public static final int APP = 3;

    private TerminalLogger(){}

    /**
     * Escribe un mensaje de registro con la fecha y hora y lo escribe en la salida estandar.
     * @param mensaje El mensaje que se imprimira
     * @param tipo El tipo de mensaje que se imprimira
     */
    public static void TLog(String mensaje, int tipo) {

        switch (tipo){
            case CONN:
                System.out.println("["+ LocalDateTime.now()+"][CONEXION] "+mensaje);
                break;
            case CONTROL:
                System.out.println("["+ LocalDateTime.now()+"][CONTROL] "+mensaje);
                break;
            case ERR:
                System.err.println("["+LocalDateTime.now()+"][ERROR] "+mensaje);
                break;
            case APP:
                System.out.println("["+LocalDateTime.now()+"][APLICACION] "+mensaje);
        }
    }

}
