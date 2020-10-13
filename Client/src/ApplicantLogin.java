/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.awt.*;
import java.io.EOFException;
import java.net.SocketException;

/**
 *
 * @author near
 */
public class ApplicantLogin extends javax.swing.JPanel {

    /**
     * Creates new form AdminLogin
     */
    public ApplicantLogin() {
        initComponents();
        addEventListeners();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jPasswordField1 = new javax.swing.JPasswordField();

        jButton1.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N
        jButton1.setForeground(new Color(13063248));
        jButton1.setText("Back");

        jButton2.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N
        jButton2.setText("Login");

        jLabel1.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N
        jLabel1.setText("Applicant ID:");

        jLabel2.setFont(new java.awt.Font("Ubuntu Mono", 1, 24)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setIcon(new javax.swing.ImageIcon("static/applicant_login.png")); // NOI18N
        jLabel2.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel3.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N
        jLabel3.setText("Password:");

        jLabel4.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setForeground(Color.GRAY);

        jTextField1.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N

        jPasswordField1.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 1256, Short.MAX_VALUE)
                        .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addContainerGap()
                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(445, 445, 445)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                        .addComponent(jLabel1)
                                                        .addComponent(jTextField1)
                                                        .addComponent(jLabel3)
                                                        .addComponent(jPasswordField1, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                                                        .addComponent(jLabel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(573, 573, 573)
                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(32, 32, 32)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(29, 29, 29)
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPasswordField1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(26, 26, 26)
                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 41, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(78, 78, 78)
                                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addContainerGap(282, Short.MAX_VALUE))
        );
    }// </editor-fold>

    private void addEventListeners(){
        // Back
        jButton1.addActionListener(e -> {
            Main.frame.getContentPane().removeAll();
            Main.frame.setContentPane(new LandingPage());
            Main.frame.setSize(new LandingPage().getPreferredSize());
            Main.frame.setVisible(true);
        });

        // Login
        jButton2.addActionListener(e -> {
            try {
                Main.dataOutputStream.writeInt(2);
                String username = jTextField1.getText();
                String password = String.valueOf(jPasswordField1.getPassword());
                login(username,password);
            }catch (SocketException | EOFException exception) {
                Main.raiseErrorPage(new ErrorPage(500,exception));
            }catch (Exception exception){
                Main.raiseErrorPage(new ErrorPage(exception));
            }
        });
    }

    private void login(String username,String password) throws Exception{
        Main.dataOutputStream.writeUTF(username);
        Main.dataOutputStream.writeUTF(password);
        status = Main.dataInputStream.readInt();
        if(status>=200 && status<=299){
            jLabel4.setText("Success");
        }else if (status==401)
            jLabel4.setText("Invalid username or password");
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPasswordField jPasswordField1;
    private javax.swing.JTextField jTextField1;
    private int status;
    // End of variables declaration
}
