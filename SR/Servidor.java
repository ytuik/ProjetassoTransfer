import java.io.*;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);
        System.out.println("Digite o nome do arquivo que deseja mandar:");
        String fileName = s.nextLine();
        
        int port = 1235;

        // Checa arquivo
        File f = new File(fileName);
        if (!f.exists() || !f.canRead()) {
            throw new Exception("Arquivo inexistente");
        }

        // use appropriate protocol type to send file data
        System.out.println("SR protocol");
        ServidorSR sender = new ServidorSR(fileName, port);
        sender.start();
    }
}
