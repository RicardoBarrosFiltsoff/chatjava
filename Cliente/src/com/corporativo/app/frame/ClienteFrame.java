/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corporativo.app.frame;

import com.corporativo.app.bean.ChatMessage;
import com.corporativo.app.bean.ChatMessage.Action;
import com.corporativo.app.service.ClienteService;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Ricardo Barros Filtsoff
 */
public class ClienteFrame extends javax.swing.JFrame {

    private Socket socket;
    private ChatMessage message;
    private ClienteService service;

    /**
     * Creates new form ClienteFrame
     */
    public ClienteFrame() {
        initComponents();
    }

    private class ListenerSocket implements Runnable {

        private ObjectInputStream input;

        public ListenerSocket(Socket socket) throws IOException {
            this.input = new ObjectInputStream(socket.getInputStream()); //inicializando objeto input
        }

        @Override
        public void run() {

            ChatMessage message = null;

            try {

                while ((message = (ChatMessage) input.readObject()) != null) {
                    Action action = message.getAction();  //recebe a açao q o servidor esta enviando

                    if (action.equals(Action.CONNECT)) {     //teste do tipo de mensagem enviada pelo cliente
                        connected(message);                 //chamando metodo connected

                    } else if (action.equals(Action.DISCONNECT)) { //teste de resposta

                        disconnected();        //chamando metodo disconnect
                        socket.close();

                    } else if (action.equals(Action.SEND_ONE)) { //teste de resposta

                        receive(message);

                    } else if (action.equals(Action.USERS_ONLINE)) { //lista de unuarios online
                        refreshOnlines(message);  //atualizar lista de usuarios onlines

                    }

                }
            } catch (IOException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private void connected(ChatMessage message) {
        if (message.getText().equals("NO")) {
            this.txtName.setText("");  //limpa nome componente na tela
            JOptionPane.showMessageDialog(this, "Conexão não realizada!\nTente Novamente com um novo nome");
            return;
        }

        this.message = message; //passando valor message como mensagem
        this.btnConnectar.setEnabled(false);            //habilitando e desbilitando botoes ao conectar
        this.txtName.setEditable(false);

        this.btnSair.setEnabled(true);
        this.txtAreaSend.setEnabled(true);
        this.txtAreaReceiver.setEnabled(true);
        this.btnEnviar.setEnabled(true);
        this.btnLimpar.setEnabled(true);
        this.btnAtualizar.setEnabled(true);

        JOptionPane.showMessageDialog(this, "Você está conectado no chat"); //conexao realizada

    }

    private void disconnected() {

        this.btnConnectar.setEnabled(true);  //habilitando e desbilitando botoes ao desconectar
        this.txtName.setEditable(true);

        this.btnSair.setEnabled(false);
        this.txtAreaSend.setEnabled(false);
        this.txtAreaReceiver.setEnabled(false);
        this.btnEnviar.setEnabled(false);
        this.btnLimpar.setEnabled(false);
        this.btnAtualizar.setEnabled(false);

        JOptionPane.showMessageDialog(this, "Você saiu do chat");

    }

    private void receive(ChatMessage message) {
        this.txtAreaReceiver.append(message.getName() + " diz: " + message.getText() + "\n");  //acumula varias mensagens que chega
    }

    private void refreshOnlines(ChatMessage message) {

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        txtName = new javax.swing.JTextField();
        btnConnectar = new javax.swing.JButton();
        btnSair = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        btnAtualizar = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        listOnlines = new javax.swing.JList();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtAreaReceiver = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtAreaSend = new javax.swing.JTextArea();
        btnEnviar = new javax.swing.JButton();
        btnLimpar = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Conectar"));

        btnConnectar.setText("Connectar");
        btnConnectar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConnectarActionPerformed(evt);
            }
        });

        btnSair.setText("Sair");
        btnSair.setEnabled(false);
        btnSair.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSairActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(btnSair)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, 187, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(56, 56, 56)
                .addComponent(btnConnectar)
                .addGap(64, 64, 64))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConnectar))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSair)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Onlines"));

        btnAtualizar.setText("Atualizar");
        btnAtualizar.setEnabled(false);
        btnAtualizar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAtualizarActionPerformed(evt);
            }
        });

        listOnlines.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane3.setViewportView(listOnlines);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnAtualizar)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 117, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnAtualizar))
        );

        jPanel3.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        txtAreaReceiver.setEditable(false);
        txtAreaReceiver.setColumns(20);
        txtAreaReceiver.setRows(5);
        txtAreaReceiver.setEnabled(false);
        jScrollPane1.setViewportView(txtAreaReceiver);

        txtAreaSend.setColumns(20);
        txtAreaSend.setRows(5);
        txtAreaSend.setEnabled(false);
        jScrollPane2.setViewportView(txtAreaSend);

        btnEnviar.setText("Enviar");
        btnEnviar.setEnabled(false);
        btnEnviar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnviarActionPerformed(evt);
            }
        });

        btnLimpar.setText("Limpar");
        btnLimpar.setEnabled(false);
        btnLimpar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLimparActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addComponent(btnLimpar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEnviar))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 337, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(37, 37, 37)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 10, Short.MAX_VALUE)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnviar)
                    .addComponent(btnLimpar)))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, 341, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addContainerGap())
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnConnectarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConnectarActionPerformed
        String name = txtName.getText();

        if (!name.isEmpty()) {                  //teste name 
            this.message = new ChatMessage(); //inicializando ChatMenssage
            this.message.setAction(Action.CONNECT);
            this.message.setName(name);

            this.service = new ClienteService();
            this.socket = this.service.connect();

            try {
                new Thread(new ListenerSocket(this.socket)).start(); //inicia ouvinte
            } catch (IOException ex) {
                Logger.getLogger(ClienteFrame.class.getName()).log(Level.SEVERE, null, ex);

            }
            this.service.send(message);         //enviando mensagem
        }

    }//GEN-LAST:event_btnConnectarActionPerformed

    private void btnSairActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSairActionPerformed
        this.message.setAction(Action.DISCONNECT); //setar acao mensagem disconect
        this.service.send(this.message); // chama metodo send e mensagem enviada servidor
        disconnected(); // disconecta
    }//GEN-LAST:event_btnSairActionPerformed

    private void btnLimparActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLimparActionPerformed
        this.txtAreaReceiver.setText(""); // limpa mensagem digitada errada antes de enviar
    }//GEN-LAST:event_btnLimparActionPerformed

    private void btnEnviarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnviarActionPerformed
        String text = this.txtAreaSend.getText();
        String name = this.message.getName();   //varialvel recupera texto digitado
        if(!text.isEmpty()){  //teste texto vazio
        
        
        this.message = new ChatMessage();   // garante que nao ha nada alem de mensagem
        this.message.setName(name);     // apenas guarda nome da variavel 
        this.message.setText(text);
        this.message.setAction(Action.SEND_ALL); 
        
        this.txtAreaReceiver.append("Voce disse: " + text); //mostra mensagem tambem na janela do usuario que enviou
        
        this.service.send(this.message); //envio da mensagem
        }
        
        this.txtAreaSend.setText(""); //limpa campo apos enviar mensagem
    }//GEN-LAST:event_btnEnviarActionPerformed

    private void btnAtualizarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAtualizarActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnAtualizarActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnAtualizar;
    private javax.swing.JButton btnConnectar;
    private javax.swing.JButton btnEnviar;
    private javax.swing.JButton btnLimpar;
    private javax.swing.JButton btnSair;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JList listOnlines;
    private javax.swing.JTextArea txtAreaReceiver;
    private javax.swing.JTextArea txtAreaSend;
    private javax.swing.JTextField txtName;
    // End of variables declaration//GEN-END:variables
}
