public class SRsender{//talvez tenha q troar os public por static
    public int numerodebytes;//a serem enviados
    public int porta; //porta udp para a qual enviar
    public int tamanhojanela;//tamanho da janela q deve ser (tamanho da seq)/2
    public int qntenviados;//numero de pacotes com acks recebidos
    public byte [] arquivo;//array de bytes compondo o arquivo
    public int []state;//0-enviaveis mas nao enviados   1-confirmado 2-esperando ack
    public long timer;
    public int i, j;
    public SRsender(int porta, int tamanhojanela, byte []arquivo, long timeout){
        this.porta=porta;
        this.tamanhojanela=tamanhojanela;
        this.qntenviados=0;
        this.timeout = timeout;
        this.arquivo = arquivo;
        this.numerodebytes=arquivo.length;
        this.state = new int[numerodebytes];
        this.i=0;
        this.j=tamanhojanela-1;
    }
    class Send extends Thread{
        public void run(){
            while(qntenviados<numerodebytes){
                if(timer>=timeout){
                    for(int k=i;k<=j;k++){
                        if(state[k]==0 || state[k]==2){
                            //envia arquivo[k] e k pela porta
                            state[k]=2;
                        }
                    }
                    timer = System.nanoTime();
                }   
            }
        }
    }

    class Receive extends Thread{
       public void run(){

       }
        
    }
} 
