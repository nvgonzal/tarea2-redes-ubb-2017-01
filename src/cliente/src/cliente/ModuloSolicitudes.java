/**
 * Created by Javiera.
 */
package cliente;

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
    public int i=0;
    public static BufferedImage recivir_I; 
    public int maxBuff=2000000;// tamaño del buffer, para recibir imagen
    DatagramSocket receiverSocket;
     static Lienzo ventana = new Lienzo();
     public int PORT;
     public int numero;
     public String fin;

     String video;

    public ModuloSolicitudes(Socket conexion){
        
        super("ModuloSolicitudes");
        setSize(800,600);

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

     * @return true si el numero ingresado por el usuario es correcto, false si falla
     * @throws IOException si hay un error al enviar o recibir los datos
     */
    private boolean autentificar() throws IOException{


        //El servidor solicita el video al cliete

             System.out.println("ingrese numero del video(1 o 2)");
             video = sf.nextLine();

             do {
                 try {

                     numero = Integer.parseInt(video); //Intenta trasformar el valor ingresado por el usuario

                     if (numero > 2 || numero < 1) {
                         System.err.println("Error  rango de numero , ingrese entre numero 1 y 2: ");
                         video = sf.nextLine();//El servidor solicita el video al cliete
                         numero = Integer.parseInt(video);
                     }


                 } catch (NumberFormatException e) {
                     //Si la transformacion falla lanza una excepcion y muestra un mensaje por la
                     //consola
                     System.err.println("Error ingreso texto , ingrese solo numero: ");
                     video = sf.nextLine();//El servidor solicita el video al cliete

                 }
             }while(numero>2 || numero<1 );

        salidaDatos.println("GET video"+video);//Envia el numero del video
        String respuesta = entradaDatos.readLine();//Recibe la respuesta del servidor si el usuario existe o no
         if (respuesta.contains("OK")){ // si esta correcto el servidor envia OK y el cliente manda el puerto

             PORT = (int) Math.floor(Math.random() * 9999+1025);

              salidaDatos.println("PORT " +PORT);// envia datos al servidor

             return true;
            }

        return false;
       
    }

    /**
     * Solicita el video al servidor, a demas de crear concexion udp.
     * @throws IOException si falla al recibir los videos.
     */
    private void obtenerVideo() throws IOException{

             DatagramSocket receiverSocket = new DatagramSocket(PORT);

             while (true){

                    try {
                            i++;
                            this.setVisible(true);
                            System.out.println("Escuchando envio");
                            byte[] receiverData = new byte[maxBuff];
                            DatagramPacket packet = new DatagramPacket(receiverData, receiverData.length);
                            receiverSocket.receive(packet);
                            recivir_I = ImageIO.read(new ByteArrayInputStream(packet.getData()));// transforma lo recivido por el servidor(byte) en imagen
                            System.out.println("Mostrando Frame Numero:" + i);
                            ventana.repaint();
                            String miString = new String(packet.getData());
                            if(miString.contains("FIN")){
                                System.out.println("------------------------------");
                                System.out.println("CANTIDAD TOTAL DE FRAMES:" + i);

                            }



                        }

                    catch(Exception e){

                            e.printStackTrace();

                        }

                }

    }

    public static class Lienzo extends JPanel{// lienzo extendida desde JPanel

         public void paint(Graphics g){// hace que la imagen en el lienzo se actualicen
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