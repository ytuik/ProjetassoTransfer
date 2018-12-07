//classe recebedora de bytes via selective repeat
public class SRreceiver{
    public int numerodebytes;//a serem recebidos
    public int porta; //porta udp por onde receber
    public int tamanhojanela;//tamanho da janela q deve ser (tamanho da seq)/2
    public int qntrecebidos;//numero de pacotes confirmados
    public byte [] arquivo;//array de bytes compondo o arquivo
    public int []state;//0-enviaveis mas nao enviados   1-confirmado 2-esperando ack
    public int mod;


    public void run(){
    	  i = 0;
    	  j = tamanhojanela-1;
    	  for(int k = i; k<=j;k++){
    	  	state[k] = 1;
    	  }
        while(qntrecebidos<numerodebytes){
           		 //receber arquivo e indice
           		 if(state[index] == 0){
           		 	//send Ack
           		 }else{
		             state[index]=0;//confirma pacote
		             //send Ack
		             //Salvar Buffer;//array de bytes (index)
		             qntrecebidos++;
		             while(state[i]==0){
		                 //Inserir no vector
		                 i=(i+1)%mod;
		                 state[(j+1)%mod] = 1;
		                 j=(j+1)%mod;
		             }
           		 }
		}
	}

}
