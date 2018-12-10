import java.io.FileInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.File;


public class ClienteUDP {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket = new DatagramSocket();
        InetAddress IP = InetAddress.getByName("localhost");
        byte[] dados = new byte[200];
        
        File arquivo = new File("C:\\Users\\Lion\\Desktop\\Opa.zip"); // Carrega o arquivo a ser enviado
        FileInputStream entradaArquivo = new FileInputStream(arquivo);

        DatagramPacket pacote = new DatagramPacket(dados, dados.length, IP, 5000); // Cria o pacote que vai mandar o array dados[]

        int tamanho = entradaArquivo.read(dados); // Retorna o tamanho dos dados.
        // Deve dar pra manipular isso aqui direito pra mandar as sequencias do SR
        while(tamanho != -1) {
            System.out.println("Enviando parte " + tamanho);
            socket.send(pacote); 
            tamanho = entradaArquivo.read(dados); // Depois que o dado é enviado, o tamanho fica -1, não sei pq
        }
        dados = "fyn".getBytes();

        pacote = new DatagramPacket(dados, dados.length, IP, 5000);
        System.out.println("Arquivo enviado");
        socket.send(pacote);
        
        socket.close();
        entradaArquivo.close();


        //envio começa aqui
        SRsender SR = new SRsender( porta,  tamanhoJanela,  arraybyte,  timeout,  probability,   IP);
        SR.envia();//cria thread e envia 
        //envio termina aqui

        
    }
}