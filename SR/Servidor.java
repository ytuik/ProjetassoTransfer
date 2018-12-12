import java.io.*;
import java.util.Scanner;

public class Servidor {
    public static void main(String[] args) throws Exception {

        Scanner s = new Scanner(System.in);
        System.out.println("Dgite o nome do arquivo que deseja mandar:");
        String fileName = s.nextLine();

        // read channel info
        FileReader fileReader = new FileReader("channelInfo");
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        String line = bufferedReader.readLine();
        if (line == null) {
            throw new Exception("empty channel info");
        }
        
        String[] tokens = line.split("\\s");
        String hostName = tokens[0];
        int port = Integer.parseInt(tokens[1]);
        bufferedReader.close();

        // check input file
        File f = new File(fileName);
        if (!f.exists() || !f.canRead()) {
            throw new Exception("Invalid input file");
        }

        // use appropriate protocol type to send file data
        System.out.println("SR protocol");
        ServidorSR sender = new ServidorSR(fileName, hostName, port);
        sender.start();
        
    }
}
