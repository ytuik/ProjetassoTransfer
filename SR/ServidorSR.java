import java.io.File;
import java.io.FileInputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Deque;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.HashMap;
import java.util.concurrent.Semaphore;

public class ServidorSR {

    private static final int TAMANHO_ACK = 12;
    private static final int TAMANHO_BUFFER = 500;
    private static final int TAMANHO_CABECALHO = 12;
    
    private final Semaphore available = new Semaphore(10);

    private FileInputStream fileStream;

    static DatagramSocket socket;
    static InetAddress channelAddress;
    static int port;

    private Deque<TimerPacket> queue;
    private Map<Integer, TimerPacket> map;

    private int base;
    private int nextSeqNum;
    private int moduloNumSeq;

    private volatile boolean sendFinished;//Saber se terminou de enviar

    public ServidorSR(File file, int channelPort, int janela) throws Exception {
        this.base = 0;
        this.nextSeqNum = 0;
        this.port = channelPort;
        this.channelAddress = InetAddress.getByName("172.20.4.69");//COLOQUE O IP DO CLIENTE AQUI
        this.moduloNumSeq = janela * 2;//tamanho total do SR
        this.queue = new ArrayDeque<>();
        this.map = new HashMap<>();
        this.fileStream = new FileInputStream(file);
        this.sendFinished = false;
    }

    // Função que recebe os ACKs
    private void receivePackets() {
        byte[] buffer = new byte[TAMANHO_ACK];
        DatagramPacket receiveDatagram = new DatagramPacket(buffer, buffer.length);
        Pacote pacote;

        while (!sendFinished || !queue.isEmpty()) {
            try {
                // Pega o número do ACK
                socket.receive(receiveDatagram);
                pacote = Pacote.getPacote(receiveDatagram.getData());
                System.out.println(String.format("PKT RECV ACK %s %s", pacote.getTamanho(), pacote.getSeqNum()));
                int ackNum = pacote.getSeqNum();

                // Marca o pacote como recebido na janela
                if (map.containsKey(ackNum)) {
                    TimerPacket timerPacket = map.get(ackNum);
                    timerPacket.stopTimer();

                    // Avança a janela se base == # do Ack
                    if (ackNum == base) {
                        while (!queue.isEmpty() && queue.peek().isAck()) {
                            timerPacket = queue.poll();
                            map.remove(timerPacket.getPacote().getSeqNum());
                            available.release();
                        }
                        base = (timerPacket.getPacote().getSeqNum() + 1) % moduloNumSeq;
                    }
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
    }

    public void start() throws Exception {
        socket = new DatagramSocket();

        // Criaçao da thread que recebe os ACKs
        Thread receiveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                receivePackets();
            }
        });
        receiveThread.start();

        System.out.println("Começando a enviar os dados, por favor aguarde.");
        while (true) {
            // Fazer cada pacote com seu timer
            byte[] buffer = new byte[TAMANHO_BUFFER];
            int readNum = fileStream.read(buffer, 0, TAMANHO_BUFFER);
            // Quando readNum == -1 significa que terminou de enviar o arquivo
            if (readNum < 0) {
                sendFinished = true;
                break;
            }
            Pacote pacote = new Pacote(0, readNum + TAMANHO_CABECALHO, nextSeqNum, buffer);
            TimerPacket timerPacket = new TimerPacket(pacote);

            // Manda o pacote e começa o timer
            available.acquire();
            queue.offer(timerPacket);
            map.put(nextSeqNum, timerPacket);
            timerPacket.startTimer();
            Util.enviaDados(pacote, channelAddress, port, socket);

            // Atualiza o proximo numero de sequencia
            nextSeqNum = (nextSeqNum + 1) % moduloNumSeq;
        }

        // Espera a thread acabar
        receiveThread.join();

        // Finaliza a sessao
        Util.endSenderSession(base, channelAddress, port, socket);
        System.out.println("Acabou de enviar os dados, tenha um feliz natal.");
        socket.close();
        fileStream.close();
    }
}