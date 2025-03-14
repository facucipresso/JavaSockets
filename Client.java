import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client{

    public static void main(String[] args) throws IOException {
        try(Socket miSocket = new Socket("localhost", 4999);
        BufferedReader entrada = new BufferedReader(new InputStreamReader(miSocket.getInputStream()));
        PrintWriter salida = new PrintWriter(miSocket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in)) {

            System.out.println("Conectandose al servidor, por favor ingrese su nombre de cliente: ");
            String nombreCliente = scanner.nextLine();
            salida.println(nombreCliente);

            Thread hiloEscucha = new Thread(new BroadcastHandler(miSocket));
            hiloEscucha.start();

            while (true) {
                System.out.println("Ingrese comando:\n -Bienvenida/Despedida <nombre-cliente>\n -Broadcast <mensaje>\n -Exit (si quiere salir)");
                String comando = scanner.nextLine();

                salida.println(comando);

                if(comando.startsWith("Bienvenida")){
                    manejadorBienvenida(entrada);
                }else if (comando.startsWith("Despedida")){
                    manejadorDespedida(entrada);
                }else if(comando.startsWith("Broadcast")){
                    manejadorBroadcast(comando);
                }else if(comando.startsWith("Exit")){
                    manejadorSalida(entrada, miSocket);
                    System.out.println("Desconectando del servidor");
                    break;
                }else{
                    String respuesta = entrada.readLine();
                    if(respuesta != null){
                        System.err.println("Respuesta del Servidor: " + respuesta);
                    }else{
                        System.err.println("Error el servidor no respondio");
                    }
                }
            }
            
        } catch (Exception e) {
            System.err.println("Error al crear el cliente" + e.getMessage());
        }
    }

    private static class BroadcastHandler implements Runnable{

        private BufferedReader entrada;

        public BroadcastHandler(Socket clientSocket){
            try {
                this.entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        }

        @Override
        public void run(){
            try{
                String serverMensaje;
            
                while ((serverMensaje = entrada.readLine()) != null) {
                    System.out.println(serverMensaje);
                }
                
            } catch (Exception e) {
                System.err.println(e);
            }
            
            
            
        }
    }

    private static void manejadorBienvenida (BufferedReader entrada) throws IOException{
        String line;

        while ((line = entrada.readLine()) != null) {
            if(line.equals("EOF")){
                break;
            } else{
                System.out.println(line);
            }
        }      
    }
    private static void manejadorDespedida (BufferedReader entrada) throws IOException{
        String line;
        while ((line = entrada.readLine()) != null) {
            if(line.equals("EOF")){
                break;
            } else{
                System.out.println(line);
            }
        }       
    }

    private static void manejadorSalida(BufferedReader entrada, Socket miSocket)throws IOException{
        String line;
        while ((line = entrada.readLine()) != null) {
            if(line.equals("EOF")){
                break;
            } else{
                System.out.println(line);
            }
        }
        miSocket.close();
        
    }

    private static void manejadorBroadcast(String comando){
        String [] divisionComando = comando.split(" ", 2);
        String mensaje = divisionComando[1];

    }

}