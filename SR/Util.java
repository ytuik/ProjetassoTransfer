import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class Util {

    private static final int ACK_SIZE = 12;

    public static void enviaPacote(byte[] bytes, InetAddress address, int port, DatagramSocket socket) {
        try {
            DatagramPacket enviaPacote = new DatagramPacket(bytes, bytes.length, address, port);
            socket.send(enviaPacote);
        } catch (Exception e) {
            System.out.println("Excecao quando enviou o pacote");
        }
    }

    public static Pacote recebePacote(int bufferSize, DatagramSocket socket) throws Exception {
        try {
            byte[] buffer = new byte[bufferSize];
            DatagramPacket recebeDatagrama = new DatagramPacket(buffer, buffer.length);
            socket.receive(recebeDatagrama);
            return Pacote.getPacote(recebeDatagrama.getData());
        } catch (Exception e) {
            System.out.println("Excecao ao receber pacote");
            throw e;
        }
    }

    public static void enviaACK(int ackNum, InetAddress enderecoCanal, int channelPort, DatagramSocket socket)
            throws Exception {
        Util.enviaPacote(Pacote.createACK(ackNum).getBytes(), enderecoCanal, channelPort, socket);
        System.out.println(String.format("ACK enviado: #%s", ackNum));
    }

    public static void enviaDados(Pacote pacote, InetAddress enderecoCanal, int port, DatagramSocket socket) {
        Util.enviaPacote(pacote.getBytes(), enderecoCanal, port, socket);
        System.out.println(String.format("Pacote de dados enviado. Tamanho: %s . #%s", pacote.getTamanho(), pacote.getSeqNum()));
    }

    public static void endSenderSession(int seqNum, InetAddress enderecoCanal, int port, DatagramSocket socket)
            throws Exception {
        // envia FYN
        enviaPacote(Pacote.createFYN(seqNum).getBytes(), enderecoCanal, port, socket);
        System.out.println("FYN enviado #" + seqNum);

        // espera por FYN
        while (true) {
            Pacote pacote = Util.recebePacote(ACK_SIZE, socket);
            if (pacote.getTipo() == 2) {
                System.out.println("FYN recebido #" + pacote.getSeqNum());
                break;
            } else if (pacote.getTipo() == 1) {
                System.out.println("FYN recebido #" + pacote.getSeqNum());
            }
        }
    }

    public static void endReceiverSession(Pacote pacote, InetAddress enderecoCanal, int channelPort,
            DatagramSocket socket) throws Exception {
        System.out.println(String.format("FYN recebido. Tamanho: %s. #%s", pacote.getTamanho(), pacote.getSeqNum()));

        // responde FYN
        Util.enviaPacote(Pacote.createFYN(pacote.getSeqNum()).getBytes(), enderecoCanal, channelPort, socket);
        System.out.println("FYN enviado #" + pacote.getSeqNum());
    }
}
