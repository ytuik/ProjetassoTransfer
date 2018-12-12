import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;


public class Cliente {
    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);
        System.out.println("Deseja escolher o tamanho da janela? [1/0]");
        int tamanho;
        boolean escolha;
        while (true) {
            tamanho = s.nextInt();
            if (tamanho == 1) {
                escolha = true;
                break;
            } else if (tamanho == 0) {
                escolha = false;
                break;
            } else {
                System.out.println("Opção inválida");
            }
        }
        if (escolha) {
            System.out.println("Tamanho da janela:");
            tamanho = s.nextInt();
        }

        System.out.println("Probabilidade de descarte:");
        int prob;
        while (true) {
            prob = s.nextInt();
            if (prob < 100 && prob >= 0) {
                break;
            }
        }

        System.out.println("Nome do arquivo a ser criado:");
        String fileName = s.nextLine();

        int port = 8001;
        DatagramSocket socket = new DatagramSocket(port);
        ClienteSR receiver;
        if (escolha) {
            receiver = new ClienteSR(socket, fileName, tamanho);
        } else {
            receiver = new ClienteSR(socket, fileName);
        }
        receiver.start();
    }

}
