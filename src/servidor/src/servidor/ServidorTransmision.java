package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by Nicolas on 00009, 09-07-2017.
 */
public class ServidorTransmision extends Thread {

    private Socket conexionClienteTCP;
    private String videoSolicitado;
    private BufferedReader entradaDatos;
    private PrintWriter salidaDatos;
    private int puertoUDPCliente;

    public static final int FRAMESVIDEO1 = 100;
    public static final int FRAMESVIDEO2 = 100;


    public ServidorTransmision(Socket conexionClienteTCP){
        this.conexionClienteTCP = conexionClienteTCP;
        crearIOStream();
    }

    @Override
    public void run(){

    }

    public void atender(){
        try {
            obtenerNombreVideo();
            obtenerPuertoUDPCLiente();
        }
        catch (IOException e){
            System.err.println(e.toString());
        }
    }

    private void obtenerNombreVideo()throws IOException{
        String solicitud = entradaDatos.readLine();
        if(solicitud.contains("GET")){
            if (solicitud.contains("video_1")){
                this.videoSolicitado = "video_1";
                salidaDatos.println("OK "+FRAMESVIDEO1);
            }
            else if (solicitud.contains("video_2")){
                this.videoSolicitado = "video_2";
                salidaDatos.println("OK "+FRAMESVIDEO2);
            }
            else {
                salidaDatos.println("ERR");
            }
        }
        else {
            salidaDatos.println("ERR");
        }

    }

    private void obtenerPuertoUDPCLiente() throws IOException{
        String mensaje = entradaDatos.readLine();
        if (mensaje.contains("PORT"))
        {
            this.puertoUDPCliente = Integer.parseInt(mensaje.substring(5));
            salidaDatos.println("OK");
        }
        else {
            salidaDatos.println("ERR");
        }
    }

    private void crearIOStream(){
        try {
            this.entradaDatos = new BufferedReader(new InputStreamReader(this.conexionClienteTCP.getInputStream()));
            this.salidaDatos = new PrintWriter(this.conexionClienteTCP.getOutputStream(),true);
        }
        catch (IOException e){
            //TerminalLogger.TLog("Error al crear buffers."+e.toString(),TerminalLogger.ERR);
        }
    }


}
