import java.io.FileOutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ClienteSR {

    private static final int TAMANHO_BUFFER = 512;
    private int ModuloNumSeq;
    
    private int base;
    private int tamanho = 10;
    private int prob;
    private Map<Integer, Pacote> map;

    private DatagramSocket socket;
    private FileOutputStream fout;

    private InetAddress channelAddress;
    private int channelPort;
    private boolean getChannelInfo;

    public static boolean descarta(int x) {//x ÃƒÂ© o nÃƒÂºmero definido pelo usuÃƒÂ¡rio de 0 a 100
	    Random gerador = new Random();
    	if(x > gerador.nextInt(100)) return true;
    	else return false;
    }
    
    public ClienteSR(DatagramSocket socket, String file, int prob) throws Exception {
        this.socket = socket;
        this.fout = new FileOutputStream(file);
        this.tamanho = 10;
        this.ModuloNumSeq = 2 * this.tamanho;
        this.base = 0;
        this.getChannelInfo = false;
        this.map = new HashMap<>();
        this.prob = prob;
    }

    public ClienteSR(DatagramSocket socket, String file, int tamanho, int prob) throws Exception {
        this.socket = socket;
        this.fout = new FileOutputStream(file);
        this.tamanho = tamanho;
        this.ModuloNumSeq = 2 * tamanho;
        this.base = 0;
        this.getChannelInfo = false;
        this.map = new HashMap<>();
        this.prob = prob;
    }

    // Checa se o # de sequencia estÃƒÂ¡ dentro da janela
    private boolean withinWindow(int ackNum) {
        int distance = ackNum - base;
        if (ackNum < base) {
            distance += ModuloNumSeq;
        }
        return distance < tamanho;
    }

    // Checa se o # de sequencia estÃƒÂ¡ na janela anterior
    private boolean withinPrevWindow(int ackNum) {
        int distance = base - ackNum;
        if (base < ackNum) {
            distance += ModuloNumSeq;
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
           System.out.println("AQUI "+socket.getInetAddress());
            if(!descarta(this.prob)){//Caso seja descartado, o socket irÃƒÂ¡ sobrescrever o anterior
                Pacote pacote = Pacote.getPacote(receiveDatagram.getData());

                // Obter as informaÃƒÂ§ÃƒÂµes do emissor.
                if (!getChannelInfo) {
                    channelAddress = receiveDatagram.getAddress();
                    channelPort = receiveDatagram.getPort();
                    getChannelInfo = true;
                }

                if (pacote.getTipo() == 2) {
                    // Acaba a transmissÃƒÂ£o se o pacote for tipo FYN
                    Util.endReceiverSession(pacote, channelAddress, channelPort, socket);
                    break;

                } else if (pacote.getTipo() == 0) {
                    // process data packet
                    // System.out.println(String.format("PKT RECV DAT %s %s", pacote.getTamanho(), pacote.getSeqNum()));
                    int ackNum = pacote.getSeqNum();
                    // Se o # de sequencia estiver dentro da janela 
                    if (withinWindow(ackNum)) {
                        // Manda ACK
                        Util.enviaACK(ackNum, channelAddress, channelPort, socket);

                        // Se o pacote for novo ÃƒÂ© carregado no buffer
                        if (!map.containsKey(ackNum)) {
                            map.put(ackNum, pacote);
                        }

                        // Se o # de seq == base, avanÃƒÂ§a a janela
                        if (ackNum == base) {
                            while (map.containsKey(ackNum)) {
                                fout.write(map.get(ackNum).getData());
                                map.remove(ackNum);
                                ackNum = (ackNum + 1) % ModuloNumSeq;
                            }
                            base = ackNum % ModuloNumSeq;
                        }
                    } else if (withinPrevWindow(ackNum)) {
                        // Se o pacote nÃƒÂ£o estiver na janela, reenvia o ACK
                        Util.enviaACK(ackNum, channelAddress, channelPort, socket);
                    }
                }
            }
        }
        System.out.println("Acabou de receber o arquivo");
        fout.close();
        socket.close();
    }
}
