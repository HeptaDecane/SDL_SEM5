import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Scanner;

public class Main {
    protected static Scanner scanner = new Scanner(System.in);
    protected static JFrame frame = new JFrame();
    protected static DataOutputStream dataOutputStream = null;
    protected static DataInputStream dataInputStream = null;
    protected static ObjectOutputStream objectOutputStream = null;
    protected static ObjectInputStream objectInputStream = null;

    static {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            System.out.println(UIManager.getSystemLookAndFeelClassName());
        }catch (Exception e){
            System.out.println("Using: CrossPlatformLookAndFeel");
        }
        frame.setResizable(false);
        frame.setTitle("Enrollment System");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        ImageIcon icon = new ImageIcon(new ImageIcon("static/logo.png").getImage().getScaledInstance(512,512, Image.SCALE_SMOOTH));
        frame.setIconImage(icon.getImage());


    }

    public static void main(String[] args) throws Exception {
        try(Socket socket = new Socket("localhost",5000)){
            dataOutputStream = new DataOutputStream(socket.getOutputStream());
            dataInputStream = new DataInputStream(socket.getInputStream());
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());
            frame.setContentPane(new LandingPage());
            frame.setSize(new LandingPage().getPreferredSize());
            frame.setVisible(true);

            scanner.nextLine();
        }catch (SocketException | EOFException e) {
            Main.raiseErrorPage(new ErrorPage(500,e));
        }catch (Exception e){
            Main.raiseErrorPage(new ErrorPage(e));
        }
    }

    public static void raiseErrorPage(ErrorPage errorPage){
        frame.getContentPane().removeAll();
        frame.setContentPane(errorPage);
        frame.setSize(errorPage.getPreferredSize());
        frame.setVisible(true);
    }
}