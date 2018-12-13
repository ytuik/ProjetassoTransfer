import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);
        System.out.println("Servidor iniciado - Aguardando conex�o.");
        DatagramSocket serverSocket =new DatagramSocket(5000);
        byte[] receiveData= new byte[1000];
        InetAddress clientIP;
        DatagramPacket receivePacket=new DatagramPacket(receiveData,receiveData.length);
        serverSocket.receive(receivePacket);
        String filexWin = new String(receiveData, "UTF-8");//Pacote enviado pelo cliente solicitando certo arquivo
        filexWin = filexWin.trim();
        System.out.println("Conex�o estabelecida");
        System.out.println("IP: "+ receivePacket.getAddress());// IP do cliente
        System.out.println("Porta: "+ receivePacket.getPort());// Porta do cliente
        String[] divide = filexWin.split("#");//caractere especial que foi decidido
        System.out.println("Tamanho da janela: "+divide[1]);// tamanho da janela solicitada pelo cliente
        System.out.println("Nome do arquivo solicitado: "+ divide[0]);
        int port = 1235;//Porta de conex�o, caso d� erro nesta posta, mudar tamb�m em ClienteSR

        // Checa arquivo
        divide[0] = divide[0].trim();
        String diret = "C:\\Users\\gtsa\\Desktop\\" + divide[0];//Mude esse diret�rio como se fosse a pasta padr�o onde estar�
        //os arquivos
        File f = new File(diret);//File com o diret�rio anterior
        if (!f.exists() || !f.canRead()) { // em caso de arquivo n�o estar na pasta
            throw new Exception("Arquivo inexistente");
        }
        int janel = Integer.parseInt(divide[1]);
        ServidorSR sender = new ServidorSR(f, port, janel);
        sender.start();
    }
}
