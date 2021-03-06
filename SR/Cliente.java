﻿import java.io.FileOutputStream;
import java.io.IOException;
import java.net.*;
import java.util.Scanner;


public class Cliente {
    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        Scanner s = new Scanner(System.in);
        System.out.println("Deseja escolher o tamanho da janela? [1/0]");// o padrao serah 10
        String tamanho;
        boolean escolha;
        while (true) {
            tamanho = s.nextLine();
            if (tamanho.equals("1")) {
                escolha = true;
                break;
            } else if (tamanho.equals("0")) {
                escolha = false;
                break;
            } else {
                System.out.println("Opcao invalida");
            }
        }
        if (escolha) {
            System.out.println("Tamanho da janela:");
            tamanho = s.nextLine();
        }

        System.out.println("Probabilidade de descarte sem utilizar % :");
        int prob;
        while (true) {
            prob = s.nextInt();
            if (prob < 100 && prob >= 0) {
                break;
            }else{
                System.out.println("Valor invalido, por favor, digite um numero de 0 a 99.");
            }
        }
        s.nextLine();
        System.out.println("Digite o nome do arquivo a ser criado :");
        String fileName = s.nextLine();

        int port = 1235;
        DatagramSocket socket = new DatagramSocket(port);
        ClienteSR receiver;
        if (escolha) {
        	InetAddress IPServer = InetAddress.getByName("172.20.4.71");//Coloque o IP do Server aqui
        	String aux = fileName + "#" + tamanho;
        	byte[] sendData = aux.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,IPServer, 5000);
            clientSocket.send(sendPacket);
            int winSize = Integer.parseInt(tamanho);
            fileName = "C:\\Users\\lsm5\\Desktop\\" + fileName; //onde o arquivo irá chegar //trocar apenas o usuário
            receiver = new ClienteSR(socket, fileName,winSize, prob);
        } else {
        	InetAddress IPServer = InetAddress.getByName("172.20.4.71");//Coloque o IP do Server aqui
            String aux = fileName + "#10";
            byte[] sendData = aux.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,IPServer, 5000);
            clientSocket.send(sendPacket);
            fileName = "C:\\Users\\lsm5\\Desktop\\" + fileName; //onde o arquivo irá chegar
            receiver = new ClienteSR(socket, fileName, prob);
        }
        receiver.start();
    }

}
// fileC:\\Users\\lsm5\\Desktop\\batato.txt