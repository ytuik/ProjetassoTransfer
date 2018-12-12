import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

public class ClienteSR {

    private static final int TAMANHO_BUFFER = 512;
    private static final int SEQNUM_MODULO = 256;
    
    private int base;
    private int tamanho = 10;
    private Map<Integer, Pacote> map;

    private DatagramSocket socket;
    private FileOutputStream fout;

    private InetAddress channelAddress;
    private int channelPort;
    private boolean getChannelInfo;

    public ClienteSR(DatagramSocket socket, String file) throws Exception {
        this.socket = socket;
        fout = new FileOutputStream(file);
        tamanho = 10;
        base = 0;
        getChannelInfo = false;
        map = new HashMap<>();
    }

    public ClienteSR(DatagramSocket socket, String file, int tamanho) throws Exception {
        this.socket = socket;
        this.fout = new FileOutputStream(file);
        this.tamanho = tamanho;
        this.base = 0;
        this.getChannelInfo = false;
        this.map = new HashMap<>();
    }

    // check if ackNum falls in the receiver's window
    private boolean withinWindow(int ackNum) {
        int distance = ackNum - base;
        if (ackNum < base) {
            distance += SEQNUM_MODULO;
        }
        return distance < tamanho;
    }

    // check if ackNum falls in receiver's previous window
    private boolean withinPrevWindow(int ackNum) {
        int distance = base - ackNum;
        if (base < ackNum) {
            distance += SEQNUM_MODULO;
        }
        return distance <= tamanho && distance > 0;
    }

    public void start() throws Exception {

        byte[] buffer = new byte[TAMANHO_BUFFER];
        DatagramPacket receiveDatagram = new DatagramPacket(buffer, buffer.length);

        System.out.println("Start to receive data");
        while (true) {
            // receive packet
            socket.receive(receiveDatagram);
            Pacote pacote = Pacote.getPacote(receiveDatagram.getData());

            // Obter as informações do emissor.
            if (!getChannelInfo) {
                channelAddress = receiveDatagram.getAddress();
                channelPort = receiveDatagram.getPort();
                getChannelInfo = true;
            }

            if (pacote.getTipo() == 2) {
                // end receiver session when receiving EOT
                Util.endReceiverSession(pacote, channelAddress, channelPort, socket);
                break;

            } else if (pacote.getTipo() == 0) {
                // process data packet
                System.out.println(String.format("PKT RECV DAT %s %s", pacote.getTamanho(), pacote.getSeqNum()));
                int ackNum = pacote.getSeqNum();
                if (withinWindow(ackNum)) {
                    // send ACK back to sender
                    Util.enviaACK(ackNum, channelAddress, channelPort, socket);

                    // if the packet is not previously received, it is buffered
                    if (!map.containsKey(ackNum)) {
                        map.put(ackNum, pacote);
                    }

                    // if ackNum == base, move forward the window
                    if (ackNum == base) {
                        while (map.containsKey(ackNum)) {
                            fout.write(map.get(ackNum).getData());
                            map.remove(ackNum);
                            ackNum = (ackNum + 1) % SEQNUM_MODULO;
                        }
                        base = ackNum % SEQNUM_MODULO;
                    }
                } else if (withinPrevWindow(ackNum)) {
                    // if the packet falls in receiver's previous window, send back ACK
                    Util.enviaACK(ackNum, channelAddress, channelPort, socket);
                }
            }
        }
        // close socket and file outputstream
        System.out.println("Finish receiving file");
        fout.close();
        socket.close();
    }
}
