public class SRsender{//talvez tenha q troar os public por static
    public int numerodebytes;//a serem enviados
    public int porta; //porta udp para a qual enviar
    public int tamanhojanela;//tamanho da janela q deve ser (tamanho da seq)/2
    public int qntenviados;//numero de pacotes com acks recebidos
    public byte [] arquivo;//array de bytes compondo o arquivo
    public int []state;//0-enviaveis mas nao enviados   1-confirmado 2-esperando ack
    public long timer;
    public int i, j;
    public int probability;
    public SRsender(int porta, int tamanhojanela, byte []arquivo, long timeout,int  probability){
        this.porta=porta;
        this.tamanhojanela=tamanhojanela;
        this.qntenviados=0;
        this.timeout = timeout;
        this.arquivo = arquivo;
        this.numerodebytes=arquivo.length;
        this.state = new int[tamjanela];
        for(int k=0;k<tamanhojanela;k++)state[k]=0;
        this.i=0;
        this.j=tamanhojanela-1;
        this.probability=probability;
    }
    class Send extends Thread{
        public void run(){
            while(qntenviados<numerodebytes){
                if(timer>=timeout){
                    for(int k=i;k<=j && j<numerodebytes;k++){
                        if(state[k]==0 || state[k]==2){
                            //envia arquivo[k] e k%tamjanela pela porta
                            state[k]=2;
                        }
                    }
                    timer = System.nanoTime();
                }   
            }
        }
    }
//Utilizado mas não enviado == 0
//Já confirmado == 1
//Enviado mas não confirmado == 2
class Receive extends Thread{
    int index;//indice recebido do servidor
    boolean Descarta(int probability){
        int x = 213131;//numero randomico de 0 a 99 aqui
        return x <= probability;
    }
    public void run(){
        while(qntenviados<numerodebytes){
            //receber indice (ack) modular
            
            if(state[index]==2 && !Descarta(probability)){
                state[index]=1;//confirma pacote
                qntenviados++;//conta mais um confirmado
                while(state[i]==1){
                    state[i] = 0;
                    i=(i+1)%tamanhojanela;
                    j=(j+1)%tamanhojanela;
                }

            }
		}
	}
}

//0 1 2 3 4 0 1 2 3 4