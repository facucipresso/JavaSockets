import java.net.*;
import java.io.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executor;

public class Server {
    
    public static void main(String[] args) {
        
        try(ServerSocket ss = new ServerSocket(4999)) {
            while (true) {
                Socket clientSocket = ss.accept();
                System.out.println("Cliente conectado" + clientSocket.getInetAddress());
                Thread hiloCliente = new Thread(new ClientHandler(clientSocket));
                hiloCliente.start();
            }
            
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
    }

    private static class ClientHandler implements Runnable {
    
        private Socket clientSocket;

        public ClientHandler(Socket socketCliente){
            clientSocket = socketCliente;
        }

        public void run(){
            try(BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter salida = new PrintWriter(clientSocket.getOutputStream(), true)) {
                
                while (true) {
                    String comando = entrada.readLine();
                    if(comando == null){
                        break;
                    }

                    System.out.println("Comando recibido: " + comando);

                    if(comando.startsWith("Bienvenida")){
                        fbienvenida(comando, salida);
                    }else if( comando.startsWith("Despedida")){
                        fdespedida(comando, salida);
                    }else{
                        salida.println("Tipo de saludo no reconocido");
                    }
                }

            } catch (Exception e) {
                System.err.println("Error al conectar con el cliente: " + e.getMessage());
            } finally{
                try {
                    clientSocket.close();
                } catch (Exception e) {
                    System.err.println("Error al intentar cerrar el cliente: " + e.getMessage());
                }
            }
        }

        private void fbienvenida(String comando, PrintWriter salida){
            String [] partesComando = comando.split(" ",2);

            String nombreCliente = partesComando[1];

            salida.println("Bienvenido a esta aplicacion de sockets " + nombreCliente);
            salida.println("EOF");
        }

        private void fdespedida(String comando, PrintWriter salida){
            String [] partesComando = comando.split(" ", 2);
            String nombreCliente = partesComando[1];

            salida.println("Te despedimos cordialmente de este socket, querido " + nombreCliente);
            salida.println("EOF");
        }

    }
}
