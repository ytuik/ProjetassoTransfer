import java.util.Random;


public class SRsender {//talvez tenha q trocar os public por static

    public int numeroDeBytes;//a serem enviados
    public int porta; //porta udp para a qual enviar
    public int tamanhoJanela;//tamanho da janela q deve ser (tamanho da seq)/2
    public int qntEnviados;//numero de pacotes com acks recebidos
    public byte [] arquivo;//array de bytes compondo o arquivo
    public int []state;//0-enviaveis mas nao enviados   1-confirmado    2-esperando ack
    public long timer;
    public int i, j;//limites da janela
    public int probability;//definirá a probabilidade de descartar
    public int mod;//q deve ser (o dobro do tamanho da janela) +1
    public InetAddress IP;

    public SRsender(int porta, int tamanhoJanela, byte []arquivo, long timeout,int  probability,  InetAddress IP) {
        this.IP=IP;
        this.porta = porta;
        this.tamanhoJanela = tamanhoJanela;
        this.qntEnviados = 0;
        this.timeout = timeout;
        this.numeroDeBytes = arquivo.length;
        this.arquivo = arquivo;
        this.state = new int[tamjanela];
        for(int k = 0; k < tamanhoJanela; k++)state[k] = 0;
        this.i = 0;
        this.j = tamanhoJanela - 1;
        this.probability = probability;
        this.mod = tamanhoJanela * 2 + 1;
    }
    public void envia(){
        Send enviador = new Send();
        Receive recebedor = new Receive();
        enviador.start();
        recebedor.start();
    }
    
    public static boolean descarta(int x) {//x é o número definido pelo usuário de 0 a 100
	    Random gerador = new Random();
    	if(x < gerador.nextInt(100)) return true;
    	else return false;
    }
    //na janela mas não enviado == 0
    //Já confirmado == 1
    //Enviado mas não confirmado == 2
    class Send extends Thread {
        public void run() {
            timer = System.nanoTime();
            int aux = System.nanoTime();
            while(qntEnviados < numeroDeBytes) {
                if(timer-aux>=timeout) {//sempre q der timeout
                    for(int k=i;k<=j;k++) {//percorre a janela
                        if(state[k]==0 || state[k]==2) {//se na janela e nao enviado ou na janela mas nao confirmado
                           
                           
                            //envia arquivo[k] e indice k%mod porta
                            //ENVIAR BYTE EM arquivo[K] e indice k%mod AQUI //PROVAVELMENTE SERÁ NECESSÁRIO FAZER UM OBJETO COM O BYTE E O INDICE
                           
                           
                            state[k]=2;//marca como enviado mas nao confirmado
                        }
                    }
                    timer = System.nanoTime();//reseta timer
                }   
            }
             System.out.println("arquivo enviado");
        }
    }
    //na janela mas não enviado == 0
    //Já confirmado == 1
    //Enviado mas não confirmado == 2
    class Receive extends Thread{
        int index;//indice a ser recebido do servidor
    
        public void run(){
            while(qntEnviados < numeroDeBytes){
                
                //RECEBER INDICE (que será o ack) AQUI
                
                if(state[index] == 2 && !descarta(probability)){//se nao foi confirmado e nao descartar o ack
                    state[index] = 1;//confirma pacote
                    qntEnviados++;//conta mais um confirmado
                    while(state[i] == 1){//enquanto o 1o da janela tiver sido confirmado
                        state[i] = 0;//desconfirma o 1o da janela
                        i = (i + 1);//anda janela
                        j = (j + 1);
                    }

                }
            }
            System.out.println("arquivo enviado");
        }
    }
}

