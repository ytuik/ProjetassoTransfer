import java.nio.ByteBuffer;

public class Pacote {
    private static final int TAMANHO_MAXIMO = 512;
    private static final int TAMANHO_CABECALHO = 12;

    private int tipo; // 0 = Dado. 1 = ACK. 2 = FYN.
    private int tamanho;
    private int seqNum;
    private byte[] dados;

    public Pacote(int tipo, int tamanho, int seqNum, byte[] dados) throws Exception {
        if (tamanho > TAMANHO_MAXIMO) {
            throw new Exception("Pacote muito grande");
        }
        this.tipo = tipo;
        this.tamanho = tamanho;
        this.seqNum = seqNum;
        this.dados = dados;
    }

    public static Pacote createACK(int seqNum) throws Exception {
        return new Pacote(1, TAMANHO_CABECALHO, seqNum, new byte[0]);
    }

    public static Pacote createFYN(int seqNum) throws Exception {
        return new Pacote(2, TAMANHO_CABECALHO, seqNum, new byte[0]);
    }

    public int getTipo() {
        return tipo;
    }

    public int getTamanho() {
        return tamanho;
    }

    public int getSeqNum() {
        return seqNum;
    }

    public byte[] getData() {
        return dados;
    }

    public byte[] getBytes() {
        ByteBuffer buffer = ByteBuffer.allocate(tamanho);
        buffer.putInt(tipo);
        buffer.putInt(tamanho);
        buffer.putInt(seqNum);
        buffer.put(dados, 0, tamanho - TAMANHO_CABECALHO);
        return buffer.array();
    }

    public static Pacote getPacote(byte[] bytes) throws Exception {
        ByteBuffer buffer = ByteBuffer.wrap(bytes);
        int tipo = buffer.getInt(); // Lê os 4 primeiros bytes, e incrementa o offset em 4
        int tamanho = buffer.getInt();
        int seqNum = buffer.getInt();
        if (tamanho > TAMANHO_CABECALHO) {
            byte[] data = new byte[tamanho - TAMANHO_CABECALHO];
            buffer.get(data, 0, tamanho - TAMANHO_CABECALHO);
            return new Pacote(tipo, tamanho, seqNum, data);
        } else {
            return new Pacote(tipo, tamanho, seqNum, new byte[0]);
        }
    }
}
