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
            System.out.println("Exception when sending pacote");
        }
    }

    public static Pacote recebePacote(int bufferSize, DatagramSocket socket) throws Exception {
        try {
            byte[] buffer = new byte[bufferSize];
            DatagramPacket receiveDatagram = new DatagramPacket(buffer, buffer.length);
            socket.receive(receiveDatagram);
            return Pacote.getPacote(receiveDatagram.getData());
        } catch (Exception e) {
            System.out.println("Exception when receiving pacote");
            throw e;
        }
    }

    public static void enviaACK(int ackNum, InetAddress channelAddress, int channelPort, DatagramSocket socket)
            throws Exception {
        Util.enviaPacote(Pacote.createACK(ackNum).getBytes(), channelAddress, channelPort, socket);
        System.out.println(String.format("PKT SEND ACK 12 %s", ackNum));
    }

    public static void enviaDados(Pacote pacote, InetAddress channelAddress, int port, DatagramSocket socket) {
        Util.enviaPacote(pacote.getBytes(), channelAddress, port, socket);
        System.out.println(String.format("PKT SEND DAT %s %s", pacote.getTamanho(), pacote.getSeqNum()));
    }

    public static void endSenderSession(int seqNum, InetAddress channelAddress, int port, DatagramSocket socket)
            throws Exception {
        // send FYN
        enviaPacote(Pacote.createFYN(seqNum).getBytes(), channelAddress, port, socket);
        System.out.println("PKT SEND FYN 12 " + seqNum);

        // wait for FYN
        while (true) {
            Pacote pacote = Util.recebePacote(ACK_SIZE, socket);
            if (pacote.getTipo() == 2) {
                System.out.println("PKT RECV FYN 12 " + pacote.getSeqNum());
                break;
            } else if (pacote.getTipo() == 1) {
                System.out.println("PKT RECV ACK 12 " + pacote.getSeqNum());
            }
        }
    }

    public static void endReceiverSession(Pacote pacote, InetAddress channelAddress, int channelPort,
            DatagramSocket socket) throws Exception {
        System.out.println(String.format("PKT RECV FYN %s %s", pacote.getTamanho(), pacote.getSeqNum()));

        // reply FYN
        Util.enviaPacote(Pacote.createFYN(pacote.getSeqNum()).getBytes(), channelAddress, channelPort, socket);
        System.out.println("PKT SEND FYN 12 " + pacote.getSeqNum());
    }
}
