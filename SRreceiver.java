//classe recebedora de bytes via selective repeat

public class SRreceiver {
	public InetAddress IP;
    public int numeroDeBytes;//a serem recebidos
    public int porta; //porta udp por onde receber
    public int tamanhoJanela;//tamanho da janela q deve ser (tamanho da seq)/2
    public int qntRecebido;//numero de pacotes confirmados
    public byte [] arquivo;//array de bytes compondo o arquivo
    public int []state;//0-enviaveis mas nao enviados   1-confirmado 2-esperando ack
	public int mod;//q deve ser (o dobro do tamanho da janela) +1
	public byte [] buffer;//pacotes bufferizados serao colocados aqui
	public List<byte> myBytes;

	public SRreceiver(int porta, int tamanhoJanela, int numeroDeBytes, InetAddress IP) {
		this.qntRecebido = 0;
		this.tamanhoJanela = tamanhoJanela;
		this.porta = porta;
		this.state = new int[tamanhoJanela];// saber o estado do selective, decidindo se coloca, manda ack, etc
		this.buffer = new byte[tamanhoJanela];
		this.i = 0;
		this.j = tamanhoJanela - 1;
		this.mod = tamanhoJanela * 2 + 1;
		this.IP=IP;
		List<byte> myBytes = new ArrayList<Byte>();
	}
	List<byte> getList(){
		return this.myBytes;
	}
	void recebe (){
		ReceiveByte ByteReceiver = new ReceiveByte();
		ByteReceiver.start();


	}
	class SendAck extends Thread{
		int ack;
		public SendAck(int ack){
			this.ack=ack;
		}
		public void run(){
			//ENVIAR ACK VIA UDP AQUI
		}
	}
	class ReceiveByte() extends Thread{
		public ReceiveByte(){}
	
		public void run() {
    	for(int k = i; k <= j; k++){
			state[k] = 1;//desconfirma todos os indices na 1a janela
			//todos os outros estão com 0, significando que estão confirmados
    	}
        while(qntRecebido < numeroDeBytes) {
				int ack;
				/**
				 * RECEBER BYTE E SEU INDICE NA JANELA AQUI
				 * VIA PORTA DO SERVIDOR
				 */
				 SendAck thr = new SendAck(ack);
				 thr.start();
        
		        state[index] = 0;//confirma pacote
		        

				//Salvar Buffer;//array de bytes (index)
				//buffer[index]="BYTE RECEBIDO AQUI";

				qntRecebido++;//conta mais um recebido
				
		        while(state[i] == 0) {
					
					//ENQUANTO O 1o DA JANELA ESTIVER CONFIRMADO, COLOCAR ELE NA LISTA DE BYTES ORDENADOS
			
					//HERE//myBytes.add(byte recebido aqui)
			
					//ESSA LISTA DE BYTES ORDENADOS SERÁ USADOS DEPOIS PRA JUNTAR OS BYTES NA ORDEM E TRANSFORMA-LOS NO ZIP
					

					//aqui abaixo a janela do servidor anda uma unidade, atualizando o estado do elemento que acaba de entrar para "não confirmado"
					i = (i + 1) % mod;
		            state[(j + 1) % mod] = 1;
		            j = (j + 1) % mod;
		        }
           	
		}
		System.out.println("arquivo recebido\naperte enter para continuar");

		//TALVEZ USAR A LISTA PARA RECRIAR O ZIP AQUI (fim do run)
	}
}
    
}
