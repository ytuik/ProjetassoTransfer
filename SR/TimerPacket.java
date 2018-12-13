import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Timer;
import java.util.TimerTask;

public class TimerPacket {
    private Pacote pacote;
    private Timer timer;
    private boolean ack;
    private final Object lock;

    TimerPacket(Pacote pacote) {
        this.pacote = pacote;
        ack = false;
        lock = new Object();
    }

    class TimeoutTask extends TimerTask {
        public void run() {
            synchronized (lock) {
                if (!ack) {
                    byte[] sendData = pacote.getBytes();
                    DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length,
                            ServidorSR.channelAddress, ServidorSR.port);
                    try {
                        ServidorSR.socket.send(sendPacket);
                    } catch (IOException e) {
                        System.out.println(e.getMessage());
                    }
                    System.out.println(String.format("Pacote de dados enviado. Tamanho: %s. #%s", pacote.getTamanho(), pacote.getSeqNum()));
                    timer.schedule(new TimeoutTask(), 50); // 50 é um valor razoavel que nos encontramos
                }
            }
        }
    }

    public Pacote getPacote() {
        return pacote;
    }

    public boolean isAck() {
        return ack;
    }

    public void startTimer() {
        timer = new Timer();
        timer.schedule(new TimeoutTask(), 50); 
    }

    public void stopTimer() {
        synchronized (lock) {
            timer.cancel();
            ack = true;
        }
    }
}
