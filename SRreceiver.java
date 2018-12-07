//classe recebedora de bytes via selective repeat
public class SRreceiver{
    public int numerodebytes;//a serem recebidos
    public int porta; //porta udp por onde receber
    public int tamanhojanela;//tamanho da janela q deve ser (tamanho da seq)/2
    public int qntrecebidos;//numero de pacotes confirmados
    public byte [] arquivo;//array de bytes compondo o arquivo
    public int []state;//0-enviaveis mas nao enviados   1-confirmado 2-esperando ack
	public int mod;//q deve ser (o dobro do tamanho da janela) +1
	public byte [] buffer;//pacotes bufferizados serao colocados aqui
	public List<Byte> myBytes;

	public SRreceiver(int porta, int tamanhojanela, int numerodebytes){
		this.qntrecebidos=0;
		this.tamanhojanela=tamanhojanela;
		this.porta=porta;
		this.state= new int[tamanhojanela];
		this.buffer = new byte[tamanhojanela];
		this.i=0;
		this.j=tamanhojanela-1;
		this.mod = tamanhojanela*2 +1;
		List<Byte> myBytes = new ArrayList<Byte>();
	}

    public void run(){
    	for(int k = i; k<=j;k++){
			state[k] = 1;//desconfirma todos os indices na 1a janela
			//todos os outros estão com 0, significando que estão confirmados
    	}
        while(qntrecebidos<numerodebytes){
				/**
				 * RECEBER BYTE E SEU INDICE NA JANELA AQUI
				 * VIA PORTA DO SERVIDOR
				 */
        	if(state[index] == 0){
           	 	/**
					 * ENVIAR ACK (inteiro igual a "index" pro cliente)
					 * POIS O STATE=0 SIGNIFICA Q ELE JÁ FOI RECEBIDO E CONFIRMADO
					 * OU QUE ELE ESTÁ FORA DA JANELA ATUAL 
				*/
           	}else{
		        state[index]=0;//confirma pacote
		        //ENVIAR ACK (inteiro igual a "index" pro cliente)

				//Salvar Buffer;//array de bytes (index)
				//buffer[index]="BYTE RECEBIDO AQUI";

				qntrecebidos++;//conta mais um recebido
				
		        while(state[i]==0){
					
					//ENQUANTO O 1o DA JANELA ESTIVER CONFIRMADO, COLOCAR ELE NA LISTA DE BYTES ORDENADOS
			
					//HERE//myBytes.add(byte recebido aqui)
			
					//ESSA LISTA DE BYTES ORDENADOS SERÁ USADOS DEPOIS PRA JUNTAR OS BYTES NA ORDEM E TRANSFORMA-LOS NO ZIP
					

					//aqui abaixo a janela do servidor anda uma unidade, atualizando o estado do elemento que acaba de entrar para "não confirmado"
					i=(i+1)%mod;
		            state[(j+1)%mod] = 1;
		            j=(j+1)%mod;
		        }
           	}
		}
		//TALVEZ USAR A LISTA PARA RECRIAR O ZIP AQUI (fim do run)
	}
}