
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class Servidor {

	public static void main(String[] args) throws IOException {
		DatagramSocket serversocket = new DatagramSocket(5000);
        
        InetAddress clientIP;
        int port;
 
        while (true) {
        	byte[] receiveData = new byte[1000000];
        	byte[] senddata;
            DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
            serversocket.receive(receivePacket);
            clientIP = receivePacket.getAddress();
            port = receivePacket.getPort();
            
            String msgDecode  = new String(receiveData, "UTF-8");
            String envio;
    		
            msgDecode = msgDecode.trim(); //TIRAR OS ESPAÇOS VAZIOS
            System.out.println(msgDecode);
    		if (msgDecode.equals("vc eh lindo")) {
            	envio = "voce que eh";
    		} else {
    			envio = "Nao consigo entender nada";
    		}
            //String envio = "OK, no seu tempo!";
            
            senddata = (envio).getBytes();
            DatagramPacket sendPacket = new DatagramPacket(senddata, senddata.length, clientIP, port);
            serversocket.send(sendPacket);
        }

	}

}
