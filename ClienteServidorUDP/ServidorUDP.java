import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.File;

public class ServidorUDP {
    public static void main(String[] args) throws IOException {

        DatagramSocket socket = new DatagramSocket(5000);
        File arquivo = new File("C:\\Users\\Lion\\Desktop\\Epa.zip"); // Cria o arquivo que vai receber os bytes do cliente
        FileOutputStream saidaArquivo = new FileOutputStream(arquivo); 
        byte[] receiveData = new byte[200];
        DatagramPacket pacoteRecebido = new DatagramPacket(receiveData,receiveData.length);

        while (true) {
            socket.receive(pacoteRecebido);
            String terminou = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength()); // Quando receber o pacote com a String fyn, significa que é o ultimo pacote.
            if (terminou.equals("fyn")) {
                System.out.println("Documentos recebidos");
                break;
            }
            saidaArquivo.write(pacoteRecebido.getData(), 0, pacoteRecebido.getLength()); // Escreve no arquivo

            saidaArquivo.flush(); // Garante que os bytes passem para o SO.
        }
        saidaArquivo.close();
        socket.close();


        SRreceiver serv = new SRreceiver( porta,  tamanhoJanela,  numeroDeBytes,  IP);
        serv.recebe();
        //serv.recebe ira printatr uma msg dizendo para apertar enter
        Scanner in = new Scanner(System.in);
        String nada = in.nextLine();
        List<byte> lista = serv.getList();
        //arquivo sera recebido aqui no list
    }
}