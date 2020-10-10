import javax.swing.*;
import java.awt.*;

public class Main {
    static JFrame frame = new JFrame();

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
        frame.setContentPane(new LandingPage());
        frame.setSize(new LandingPage().getPreferredSize());
        frame.setVisible(true);
    }
}