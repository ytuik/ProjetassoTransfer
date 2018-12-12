import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);
        DatagramSocket serverSocket =new DatagramSocket(5000);
        byte[] receiveData= new byte[1000];
        InetAddress clientIP;
        DatagramPacket receivePacket=new DatagramPacket(receiveData,receiveData.length);
        serverSocket.receive(receivePacket);
        String fiilexWin = new String(receiveData, "UTF-8");
        fiilexWin.trim();
        String[] divide = fiilexWin.split("#");
        System.out.println("Tamanho da janela: "+divide[1]);
        System.out.println("Nome do arquivo solicitado: "+ divide[0]);
        
        int port = 1235;

        // Checa arquivo
        File f = new File(divide[1]);
        if (!f.exists() || !f.canRead()) {
            throw new Exception("Arquivo inexistente");
        }

        ServidorSR sender = new ServidorSR(divide[0], port, Integer.parseInt(divide[1]));
        sender.start();
    }
}
