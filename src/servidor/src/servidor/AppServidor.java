package servidor;

/**
 * Created by Nicolas on 00002, 02-07-2017.
 */
public class AppServidor {

    public static void main(String[] args){
        Servidor servidor;
        switch (args.length){
            case 0:
                servidor = new Servidor();
                servidor.escuchar();
                break;
            case 1:
                int puerto = 0;
                try {
                    puerto = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    System.out.println("El puerto solo puede ser un numero");
                    System.exit(-1);
                }
                servidor = new Servidor(puerto);
                servidor.escuchar();
                break;
        }
    }
}
