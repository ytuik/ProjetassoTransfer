import java.util.Random;
public class SRsender{//talvez tenha q trCoar os public por static
    public int numerodebytes;//a serem enviados
    public int porta; //porta udp para a qual enviar
    public int tamanhojanela;//tamanho da janela q deve ser (tamanho da seq)/2
    public int qntenviados;//numero de pacotes com acks recebidos
    public byte [] arquivo;//array de bytes compondo o arquivo
    public int []state;//0-enviaveis mas nao enviados   1-confirmado    2-esperando ack
    public long timer;
    public int i, j;//limites da janela
    public int probability;//definirá a probabilidade de descartar
    public int mod;//q deve ser (o dobro do tamanho da janela) +1
    public SRsender(int porta, int tamanhojanela, byte []arquivo, long timeout,int  probability){
        this.porta=porta;
        this.tamanhojanela=tamanhojanela;
        this.qntenviados=0;
        this.timeout = timeout;
        this.numerodebytes=arquivo.length;
        this.arquivo = arquivo;
        this.state = new int[tamjanela];
        for(int k=0;k<tamanhojanela;k++)state[k]=0;
        this.i=0;
        this.j=tamanhojanela-1;
        this.probability=probability;
        this.mod=tamanhojanela*2 +1;
    }
    public static boolean Descarta(int x){//x é o número definido pelo usuário de 0 a 100
	      Random gerador = new Random();
    		if(x<gerador.nextInt(100)) return true;
    		else return false;
    }
    //na janela mas não enviado == 0
    //Já confirmado == 1
    //Enviado mas não confirmado == 2
    class Send extends Thread{
        public void run(){
            timer = System.nanoTime();
            int aux = System.nanoTime();
            while(qntenviados<numerodebytes){
                if(timer-aux>=timeout){//sempre q der timeout
                    for(int k=i;k<=j;k++){//percorre a janela
                        if(state[k]==0 || state[k]==2){//se na janela e nao enviado ou na janela mas nao confirmado
                           
                           
                            //envia arquivo[k] e indice k%mod porta
                            //ENVIAR BYTE EM arquivo[K] e indice k%mod AQUI //PROVAVELMENTE SERÁ NECESSÁRIO FAZER UM OBJETO COM O BYTE E O INDICE
                           
                           
                            state[k]=2;//marca como enviado mas nao confirmado
                        }
                    }
                    timer = System.nanoTime();//reseta timer
                }   
            }
        }
    }
    //na janela mas não enviado == 0
    //Já confirmado == 1
    //Enviado mas não confirmado == 2
    class Receive extends Thread{
        int index;//indice a ser recebido do servidor
    
        public void run(){
            while(qntenviados<numerodebytes){
                
                //RECEBER INDICE (que será o ack) AQUI
                
                if(state[index]==2 && !Descarta(probability)){//se nao foi confirmado e nao descartar o ack
                    state[index]=1;//confirma pacote
                    qntenviados++;//conta mais um confirmado
                    while(state[i]==1){//enquanto o 1o da janela tiver sido confirmado
                        state[i] = 0;//desconfirma o 1o da janela
                        i=(i+1);//anda janela
                        j=(j+1);
                    }

                }
            }
        }
}

//0 1 2 3 4 0 1 2 3 4
