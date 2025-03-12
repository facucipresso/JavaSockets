import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    public static void main(String[] args) throws IOException {
        try(Socket miSocket = new Socket("localhost", 4999);
        BufferedReader entrada = new BufferedReader(new InputStreamReader(miSocket.getInputStream()));
        PrintWriter salida = new PrintWriter(miSocket.getOutputStream(), true);
        Scanner scanner = new Scanner(System.in)) {
            System.out.println("Conectado al servidor");

            while (true) {
                System.out.println("Ingrese comando para tipo saludo (Bienvenida/Despedida <nombre-cliente>)");
                String comando = scanner.nextLine();

                salida.println(comando);

                if(comando.startsWith("Bienvenida")){
                    manejadorBienvenida(entrada);
                }else if (comando.startsWith("Despedida")){
                    manejadorDespedida(entrada);
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

}