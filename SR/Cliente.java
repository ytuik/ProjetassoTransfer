import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;

import com.sun.org.apache.xpath.internal.operations.Bool;

public class Cliente2 {
    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);
        System.out.println("Deseja escolher o tamanho da janela? [1/0]");
        int tamanho;
        Bool escolha;
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
        if (escolha) {
            SRReceiver receiver = new SRReceiver(socket, fileName, tamanho);
        } else {
            SRReceiver receiver = new SRReceiver(socket, fileName);
        }

        receiver.start();
    }

}
