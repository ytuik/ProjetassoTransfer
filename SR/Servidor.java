import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);
        System.out.println("Servidor iniciado - Aguardando conexão.");
        DatagramSocket serverSocket =new DatagramSocket(5000);
        byte[] receiveData= new byte[1000];
        InetAddress clientIP;
        DatagramPacket receivePacket=new DatagramPacket(receiveData,receiveData.length);
        serverSocket.receive(receivePacket);
        String filexWin = new String(receiveData, "UTF-8");
        filexWin = filexWin.trim();
        System.out.println("Conexão estabelecida");
        System.out.println("IP: "+ receivePacket.getAddress());
        System.out.println("Porta: "+ receivePacket.getPort());
        String[] divide = filexWin.split("#");
        System.out.println("Tamanho da janela: "+divide[1]);
        System.out.println("Nome do arquivo solicitado: "+ divide[0]);
        
        int port = 1235;

        // Checa arquivo
        divide[0] = divide[0].trim();
        String diret = "C:\\Users\\gtsa\\Desktop\\" + divide[0];
        File f = new File(diret);
        if (!f.exists() || !f.canRead()) {
            throw new Exception("Arquivo inexistente");
        }
        int janel = Integer.parseInt(divide[1]);
        ServidorSR sender = new ServidorSR(f, port, janel);
        sender.start();
    }
}
