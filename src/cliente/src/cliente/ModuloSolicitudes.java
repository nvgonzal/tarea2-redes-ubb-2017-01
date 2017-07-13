package cliente;
//package cliente.src.cliente;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class ModuloSolicitudes extends JFrame {

    
    public Socket conexion;
    private BufferedReader entradaDatos;
    private PrintWriter salidaDatos;
    private Scanner sf;
    public int i;
    public static BufferedImage recivir_I; 
    public int maxBuff=2000000;// tamaño del buffer, para recibir imagen
    DatagramSocket receiverSocket;
     static Lienzo ventana = new Lienzo();
     public int PORT = 9875;

    public ModuloSolicitudes(Socket conexion){
        
        super("ModuloSolicitudes");
        setSize(800,600);
        setVisible(true);
        setTitle("mostrar video");
        setResizable(false);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        add(ventana);
                
        this.conexion = conexion;
        this.sf = new Scanner(System.in);
        crearIOStream();
        
       
    }
    
    
  
    /**
     * Procesa la peticion de los usuarios.
     */
    
    public void solicitar(){
        try{
             boolean autentificado;
            do {
                autentificado = autentificar();
            }while(!autentificado);
                
                obtenerVideo();//llama al metodo obtener video
        
        }
        catch (IOException e){
            System.err.println("Error al leer o enviar datos."+e.toString());
        }
    }

   
    /**
     * Autentifica al usuario
     * @return true si el usuario se autentifica, false si falla la autentificacion
     * @throws IOException si hay un error al enviar o recibir los datos
     */
    private boolean autentificar() throws IOException{
        //El servidor solicita el video al cliete
        
        String mensajeServidor = obtenerMensajeServidor();
        System.out.println(mensajeServidor);
        String video = sf.nextLine();
                
        //Envia el numero del video
        
        salidaDatos.println(video);
        //Recibe la respuesta del servidor si el usuario existe o no
        String respuesta = entradaDatos.readLine();
        
        if (respuesta.equals("ok 250")){ // si esta correcto el servidor envia ok 250 y el cliente manda el puerto
            
            salidaDatos.println("PORT" +PORT);// envia datos al servidor
             
            return true;
        }
        else {
            
           mensajeServidor = entradaDatos.readLine();
           System.out.println(mensajeServidor);
           video = sf.nextLine();
            salidaDatos.println(video);
            boolean auth = Boolean.parseBoolean(entradaDatos.readLine());
            if (auth){
                mensajeServidor = entradaDatos.readLine();
                System.out.println(mensajeServidor);
                return true;
            }
            else {
                mensajeServidor = entradaDatos.readLine();
                System.out.println(mensajeServidor);
                return false;
            }
        }
    }

    /**
     * Solicita el video al servidor y el video, a demas de crear concexion udp.
     * @throws IOException si falla al recibir los videos.
     */
    private void obtenerVideo() throws IOException{

        ModuloSolicitudes nuevo = new ModuloSolicitudes(conexion);
        
             while (true){
                    
                    try{
                         
                        i++;
                        
                        DatagramSocket receiverSocket = new DatagramSocket(PORT);
                        System.out.println("Escuchando envio");       
                        byte[] receiverData = new byte[maxBuff]; 
                        DatagramPacket packet = new DatagramPacket(receiverData, receiverData.length);
                        receiverSocket.receive(packet);  
                        recivir_I = ImageIO.read(new ByteArrayInputStream(packet.getData()));
                        System.out.println("Mostrando Frame Numero:" +i);  
                        ventana.repaint();     
                        receiverSocket.close();       
                    } 
                    
                    catch (Exception e){
                        
                        e.printStackTrace();
                        
                    }
                }
    }

    public static class Lienzo extends JPanel{// lienzo extendida desde JPanel

         public void paint(Graphics g){//imagen en el lienzo se actualicen
            update(g);
       
         }
        @Override
        
        public void update(Graphics g){
            g.drawImage(recivir_I,0,0,null);//dibuja la imagen en la ventana
        }
        
        public Lienzo(){//LLamado del lienzo 
            super();
        }
    }
   

    /**
     * Crea buffer de entrada y salida de datos a partir de los stream del socket de conexion entre
     * el cliente y el servidor.
     */
    private void crearIOStream(){
        try {
            this.entradaDatos = new BufferedReader(new InputStreamReader(this.conexion.getInputStream()));
            this.salidaDatos = new PrintWriter(this.conexion.getOutputStream(),true);
        }
        catch (IOException e){
            System.err.println("Error al crear los buffer de entrada y salida."+e.toString());
        }
    }

    private String obtenerMensajeServidor() throws IOException{
        String rawMensajeServidor = entradaDatos.readLine();
        return rawMensajeServidor.replaceAll("\0n","\n");
    }

}