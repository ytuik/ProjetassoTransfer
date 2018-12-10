
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;

import javax.swing.JOptionPane;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;




public class Cliente {
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner (System.in);
		System.out.println("Insira o tamanho da janela: ");
		int tamanhoWindows = in.nextInt();
		tamanhoWindows = (tamanhoWindows/2)-1; //não setar essa variável para < 3
		
		
		Path caminho = Paths.get("D:\\Users\\lsm5\\Desktop\\teucu.zip");
		try {
			byte[] texto = Files.readAllBytes(caminho);
			String leitura = new String(texto, "UTF-8");
		    System.out.println(leitura);
		} catch (Exception erro) {
			System.err.println("Erro: "+erro);
		}
		
		
		
/*		
		while (true) {
		DatagramSocket clientSocket = new DatagramSocket(); //criando o socket UDP
		InetAddress IPServer = InetAddress.getByName("localhost"); //Definindo o IP do Server
		String mensagem = in.nextLine(); //mensagem a ser enviada
		
		byte [] sendData;
		sendData = (mensagem).getBytes(); //defininfo dados que vão ser enviados
		DatagramPacket sendPacket = new DatagramPacket (sendData, sendData.length, IPServer, 5000); //definindo os dados
		long as = System.nanoTime();
		clientSocket.send(sendPacket);
		
		byte[] receiveData = new byte[1000000];
		DatagramPacket receivePacket = new DatagramPacket (receiveData, receiveData.length);
		clientSocket.receive(receivePacket);
		long bs = System.nanoTime();
		
		
		String msgDecode  = new String(receiveData, "UTF-8");
		System.out.println(msgDecode);
		
		//em milisegundos
		System.out.println("t1: " + as/1000);
		System.out.println("t2: " + bs/1000);
		System.out.println("RTT: " + (System.nanoTime() - as) / 1000);
		
		clientSocket.close();
			}
		}
*/
	}
}
