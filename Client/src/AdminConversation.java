/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.EOFException;
import java.net.SocketException;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author near
 */
public class AdminConversation extends javax.swing.JPanel {

    /**
     * Creates new form AdminConversation
     */

    public AdminConversation(String adminName, List<String> tickets) {
        this.adminName = adminName;
        this.tickets = tickets;
        initComponents();
        render();
        addActionListeners();
    }
    public AdminConversation() {
        initComponents();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTable3 = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTextField1 = new javax.swing.JTextField();
        model1 = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model2 = new DefaultTableModel(){
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        model3 = new DefaultTableModel();

        jLabel1.setFont(new java.awt.Font("Ubuntu Mono", 1, 24)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setIcon(new javax.swing.ImageIcon("/home/near/Alpha/Stack/SDL_SEM5/Client/static/title_text.png")); // NOI18N
        jLabel1.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jLabel2.setFont(new java.awt.Font("Dialog", 3, 16)); // NOI18N
        jLabel2.setForeground(Color.GRAY);
        jLabel2.setText(" ClientName");

        jTable1.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N
        jTable1.setRowHeight(24);
        jTable1.setModel(model1);
        model1.addColumn("Ongoing Tickets");
        for(int i=0;i<15;i++)
        model1.insertRow(0,new Object[]{""});
        jScrollPane1.setViewportView(jTable1);

        jTable2.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N
        jTable2.setRowHeight(24);
        jTable2.setModel(model2);
        model2.addColumn("New Tickets");
        for(int i=0;i<15;i++)
        model2.insertRow(0,new Object[]{""});
        jScrollPane2.setViewportView(jTable2);

        jTable3.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jTable3.setRowHeight(20);
        jTable3.setTableHeader(null);
        jTable3.setShowGrid(false);
        jTable3.setEnabled(false);
        jTable3.setModel(model3);
        model3.addColumn("Reply");
        model3.addColumn("Message");
        for(int i=0;i<26;i++)
            model3.insertRow(0,new Object[]{"",""});
        jTable3.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer(){
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                Component cell = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                cell.setForeground(Color.GRAY);
                return cell;
            }
        });
        jScrollPane3.setViewportView(jTable3);

        jButton1.setFont(new java.awt.Font("Ubuntu Mono", 1, 18)); // NOI18N
        jButton1.setForeground(javax.swing.UIManager.getDefaults().getColor("Actions.Red"));
        jButton1.setForeground(new Color(13063248));
        jButton1.setText("Back");

        jButton2.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jButton2.setText("send");
        jButton2.setEnabled(false);

        jTextField1.setFont(new java.awt.Font("Dialog", 1, 16)); // NOI18N
        jTextField1.setEnabled(false);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 1238, Short.MAX_VALUE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addGap(112, 112, 112)
                                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 162, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addGap(6, 6, 6)
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 932, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                                                        .addComponent(jScrollPane3, javax.swing.GroupLayout.Alignment.TRAILING)
                                                                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                                                                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 821, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 105, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                                .addGap(0, 0, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addGap(4, 4, 4)
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 530, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                                        .addComponent(jTextField1)
                                                                        .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                                                        .addGroup(layout.createSequentialGroup()
                                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 300, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                                .addGap(84, 84, 84))
        );
    }// </editor-fold>

    private void render(){
        jLabel2.setText(" ");
        DefaultTableModel model = model2;

        for(String ticket: tickets){
            if(ticket.equals("BREAKPOINT"))
                model = model1;
            else
                model.insertRow(0,new Object[]{" "+ticket});
        }
    }

    private void addActionListeners(){
        jTable1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jTable2.clearSelection();
                int row = jTable1.getSelectedRow();
                int col = jTable1.getSelectedColumn();
                String ticketNo = String.valueOf(jTable1.getValueAt(row,col));
                if(!ticketNo.isBlank())
                    renderConversation(ticketNo.substring(1));
            }
        });

        jTable2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                jTable1.clearSelection();
                int row = jTable2.getSelectedRow();
                int col = jTable2.getSelectedColumn();
                String ticketNo = String.valueOf(jTable2.getValueAt(row,col));
                if(!ticketNo.isBlank())
                    renderConversation(ticketNo.substring(1));
            }
        });

        jButton1.addActionListener(e -> {
            try{
                Main.dataOutputStream.writeInt(0);
                Main.frame.getContentPane().removeAll();
                Main.frame.setContentPane(new AdminPage(adminName,true));
                Main.frame.setSize(new AdminPage().getPreferredSize());
                Main.frame.setVisible(true);
            }catch (SocketException | EOFException exception) {
                Main.raiseErrorPage(new ErrorPage(500,exception));
            }catch (Exception exception){
                Main.raiseErrorPage(new ErrorPage(exception));
            }
        });

        jButton2.addActionListener(e -> {
            try{
                String message = jTextField1.getText();
                if(!message.isBlank()) {
                    renderMessage(message, true);
                    Main.dataOutputStream.writeInt(2);
                    Main.dataOutputStream.writeUTF(message);
                    jTextField1.setText("");
                }
            }catch (SocketException | EOFException exception) {
                Main.raiseErrorPage(new ErrorPage(500,exception));
            }catch (Exception exception){
                Main.raiseErrorPage(new ErrorPage(exception));
            }
        });
    }

    private void renderConversation(String ticketNo){
        try{
            clearChatBox();
            Main.dataOutputStream.writeInt(1);
            Main.dataOutputStream.writeUTF(ticketNo);
            clientName = Main.dataInputStream.readUTF();
            jLabel2.setText(clientName);
            int n = Main.dataInputStream.readInt();
            for(int i=0;i<n;i+=2){
                String message = Main.dataInputStream.readUTF();
                boolean sent = Main.dataInputStream.readUTF().equals("1");
                renderMessage(message,sent);
            }
            jButton2.setEnabled(true);
            jTextField1.setEnabled(true);
        }catch (SocketException | EOFException exception) {
            Main.raiseErrorPage(new ErrorPage(500,exception));
        }catch (Exception exception){
            Main.raiseErrorPage(new ErrorPage(exception));
        }
    }

    private void renderMessage(String message,boolean sent){
        model3.insertRow(jTable3.getRowCount(),new Object[]{"",""});
        int i = 0, characters=0;
        String breakPoint = "";
        java.util.List<String> words = new LinkedList<>();
        for (String word : message.split(" ")){
            i++;
            if(i < message.split(" ").length)
                words.add(word+" ");
            else
                words.add(word);
        }
        for(String word : words) {
            if(word.length()<=40){
                characters += word.length();
                if (characters > 40){
                    model3.insertRow(jTable3.getRowCount(),sent?new Object[]{"",breakPoint}:new Object[]{breakPoint,""});
                    breakPoint = word;
                    characters = word.length();
                }else {
                    breakPoint += word;
                }
            }else{
                if(!breakPoint.isBlank()) {
                    model3.insertRow(jTable3.getRowCount(),sent?new Object[]{"",breakPoint}:new Object[]{breakPoint,""});
                    breakPoint = "";
                }
                word = "[ "+word+" ]";
                String part="";
                for(char c : word.toCharArray()){
                    part += String.valueOf(c);
                    if(part.length()>=40) {
                        model3.insertRow(jTable3.getRowCount(), sent ? new Object[]{"", part} : new Object[]{part, ""});
                        part = "";
                    }
                }
                if(!part.isBlank())
                    model3.insertRow(jTable3.getRowCount(), sent ? new Object[]{"", part} : new Object[]{part, ""});
            }
        }
        if(!breakPoint.isBlank())
            model3.insertRow(jTable3.getRowCount(),sent?new Object[]{"",breakPoint}:new Object[]{breakPoint,""});

        jTable3.scrollRectToVisible(jTable3.getCellRect(jTable3.getRowCount()-1, 0, true));
    }

    private void clearChatBox(){
        model3.setRowCount(0);
        for(int i=0;i<26;i++)
            model3.insertRow(0,new Object[]{"",""});
    }

    // Variables declaration - do not modify
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable jTable3;
    private javax.swing.JTextField jTextField1;
    private DefaultTableModel model1;
    private DefaultTableModel model2;
    private DefaultTableModel model3;

    private List<String> tickets;
    private String adminName, clientName;
    // End of variables declaration
}
