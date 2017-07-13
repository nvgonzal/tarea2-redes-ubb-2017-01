package cliente;
//package cliente.src.cliente;

/**
 * Created by Nicolas on 00002, 02-07-2017.
 */
public class AppCliente{

    public static void main(String[] args){
        ConexionServidor c;
        System.out.println("Conectando\n=======================================");
        switch (args.length) {
            case 0:
                c = new ConexionServidor();
                c.peticion();
                break;
            case 1:
                c = new ConexionServidor(args[0]);
                c.peticion();
                break;
            case 2:
                int puerto = 0;
                try {
                    puerto = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    System.out.println("El segundo argumento tiene que ser un numero.");
                    System.exit(-1);
                }
                c = new ConexionServidor(args[0],puerto);
                c.peticion();
                break;
        }
    }
}