import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.io.*;
//import java.util.concurrent.ExecutorService;
//import java.util.concurrent.Executor;

public class Server {
    
    static ConcurrentHashMap<String, String> clientes = new ConcurrentHashMap<>();
    //static List<String> clientes = new ArrayList<>();
    public static void main(String[] args) {

        try(ServerSocket ss = new ServerSocket(4999)) {
            while (true) {
                Socket clientSocket = ss.accept();
                System.out.println("Cliente conectado" + clientSocket.getInetAddress());
                Thread hiloCliente = new Thread(new ClientHandler(clientSocket, clientes));
                hiloCliente.start();

                /* System.out.println("Listado de los clientes conectados");
                for(Map.Entry<String, String> entry : clientes.entrySet()){
                    System.out.println("Clave: "+ entry.getKey()+ ", Valor: "+ entry.getValue()+"\n");
                } */
            }
            
            
        } catch (Exception e) {
            System.err.println("Error en el servidor: " + e.getMessage());
        }
        
    }

    private static class ClientHandler implements Runnable {
    
        private Socket clientSocket;

        public ClientHandler(Socket socketCliente, ConcurrentHashMap <String, String> clientes){
            clientSocket = socketCliente;
        }

        public void run(){
            try(BufferedReader entrada = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter salida = new PrintWriter(clientSocket.getOutputStream(), true)) {

                    
                String nombreCliente = entrada.readLine();
                String idCliente = clientSocket.getInetAddress().toString() + ":" + clientSocket.getPort();
                System.out.println("Hola "+ nombreCliente + " como estas?");
                System.out.println("Tu identificador de cliente es: "+ idCliente);
                clientes.put(idCliente, nombreCliente);
                mostrarClientes(clientes);
                

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
                    }else if(comando.startsWith("Exit")){
                        handlerSalida(salida, clientes, idCliente);
                        break;
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

        private void mostrarClientes (ConcurrentHashMap<String, String> clientes){
            System.out.println("----- Listado de los clientes conectados -----");
            for(Map.Entry<String, String> entry : clientes.entrySet()){
                System.out.println("Clave: "+ entry.getKey()+ ", Valor: "+ entry.getValue());
            }
            System.out.println("----------------------------------------------");
        }
        private void handlerSalida(PrintWriter salida, ConcurrentHashMap<String, String> clientes, String idCliente)throws IOException{
            salida.println("Cerrando conexion con el cliente");
            salida.println("EOF");
            clientes.remove(idCliente);
            clientSocket.close();
            mostrarClientes(clientes);
        }
    }
}
