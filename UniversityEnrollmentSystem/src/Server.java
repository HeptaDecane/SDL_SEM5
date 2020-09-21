import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(5000)){
            System.out.println(ANSI.YELLOW+"listening @ port:5000"+ANSI.RESET);
            ExecutorService service = Executors.newCachedThreadPool();
            while (true){
                Socket clientSocket = serverSocket.accept();
                ServerThread serverThread = new ServerThread(clientSocket);
                service.execute(serverThread);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
