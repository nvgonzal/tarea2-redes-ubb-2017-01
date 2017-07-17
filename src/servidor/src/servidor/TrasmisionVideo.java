package servidor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.*;

/**
 * Created by Nicolas on 0012, 12, 07, 2017.
 */
public class TrasmisionVideo {

    private int puertoUDPCliente;
    private String video;
    private int cantidadDeFrames;
    private InetAddress ipCliente;
    private DatagramSocket socket;

    public TrasmisionVideo(int puertoUDPCliente,String video,InetAddress ipCliente,int cantidadDeFrames){
        this.puertoUDPCliente = puertoUDPCliente;
        this.video = video;
        this.cantidadDeFrames = cantidadDeFrames;
        this.ipCliente = ipCliente;
        try {
            this.socket = new DatagramSocket();
        } catch (SocketException e) {
            e.printStackTrace();
        }
    }

    public void transmitir(){
        TerminalLogger.TLog("Enviando "+video+" a : "+puertoUDPCliente+"@"+ipCliente.getHostAddress()
                ,TerminalLogger.APP);
        for (int i=0;i<cantidadDeFrames;i++){
            try {
                String ruta = "file:///"+System.getProperty("user.dir")+"/videos/"+video+"/"+video+"_"+i+".jpeg";
                URL url = new URL(ruta);
                BufferedImage imagen = ImageIO.read(url);
                ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
                ImageIO.write(imagen,"jpg",arrayOutputStream);
                arrayOutputStream.flush();
                byte[] imagenEnBytes = arrayOutputStream.toByteArray();
                DatagramPacket packetImagen = new DatagramPacket(imagenEnBytes,imagenEnBytes.length,ipCliente,puertoUDPCliente);
                socket.send(packetImagen);
                arrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        TerminalLogger.TLog("Video enviado a : "+puertoUDPCliente+"@"+ipCliente.getHostAddress()
                ,TerminalLogger.APP);
    }
}
