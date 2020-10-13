import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Server0 {
    public static void main(String[] args) {
        try(ServerSocket serverSocket = new ServerSocket(5000)){
            System.out.println(ANSI.YELLOW+"listening @ port:5000"+ANSI.RESET);

            ScheduledExecutorService cleaner = Executors.newScheduledThreadPool(1);
            cleaner.scheduleAtFixedRate(new SessionHandler(),0,10, TimeUnit.SECONDS);

            ExecutorService service = Executors.newCachedThreadPool();
            while (true){
                Socket clientSocket = serverSocket.accept();
                ServerThread0 serverThread0 = new ServerThread0(clientSocket);
                service.execute(serverThread0);
            }

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
