//classe recebedora de bytes via selective repeat
public class SRreceiver{
    public int numerodebytes;//a serem recebidos
    public int porta; //porta udp por onde receber
    public int tamanhojanela;//tamanho da janela q deve ser (tamanho da seq)/2
    public int qntrecebidos;//numero de pacotes confirmados
    public byte [] arquivo;//array de bytes compondo o arquivo
    public int []state;//0-enviaveis mas nao enviados   1-confirmado 2-esperando ack
    
}