import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Scanner;


public class Cliente {
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner (System.in);
		
		while (true) {
		DatagramSocket clientSocket = new DatagramSocket(); //criando o socket UDP
		InetAddress IPServer = InetAddress.getByName("localhost"); //Definindo o IP do Server
		String mensagem = in.nextLine(); //mensagem a ser enviada
		
		byte [] sendData;
		sendData = (mensagem).getBytes(); //defininfo dados que v�o ser enviados
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

}