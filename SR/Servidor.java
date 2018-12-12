import java.io.*;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);
        System.out.println("Digite o tamanho da janela:");
        int j = s.nextInt();
        System.out.println("Digite o nome do arquivo que deseja mandar:");
        String fileName = s.nextLine();
        
        int port = 1235;

        // Checa arquivo
        File f = new File(fileName);
        if (!f.exists() || !f.canRead()) {
            throw new Exception("Arquivo inexistente");
        }

        ServidorSR sender = new ServidorSR(fileName, port, j);
        sender.start();
    }
}
