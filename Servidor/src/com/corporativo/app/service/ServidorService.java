package com.corporativo.app.service;

import com.corporativo.app.bean.ChatMessage;
import com.corporativo.app.bean.ChatMessage.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Barros Filtsoff
 */
public class ServidorService {

    private ServerSocket serverSocket;  // 
    private Socket socket;  //
    private Map<String, ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();
    // todo usuario q se conectar ao chat no servidor deve ser adicionado a esta lista

    public ServidorService() {

        try {
            serverSocket = new ServerSocket(5555); //porta de conexao do cliente com servidor

            System.out.println("Servidor on");

            while (true) {   //manter server socket sempre esperando nova conexao                

                socket = serverSocket.accept();  //servidor  conecta

                new Thread(new ListenerSocket(socket)).start(); //objeto socket passado para Thread
            }

        } catch (IOException ex) {

            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private class ListenerSocket implements Runnable { // implementando interface ruunable para que seje uma thread

        //objetos do cliente que se conectou
        private ObjectOutputStream output; //executa envio de mensagens do servidor
        private ObjectInputStream input; // recebe mensagens enviadas pelos clientes

        public ListenerSocket(Socket socket) {

            try {
                this.output = new ObjectOutputStream(socket.getOutputStream());
                this.input = new ObjectInputStream(socket.getInputStream());
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

        @Override
        public void run() {
            ChatMessage message = null;

            try {
                while ((message = (ChatMessage) input.readObject()) != null) {

                    Action action = message.getAction();

                    if (action.equals(Action.CONNECT)) {     //teste do tipo de mensagem enviada pelo cliente

                        boolean isConnect = connect(message, output);  //pedido de conexao
                        if (isConnect) {
                            mapOnlines.put(message.getName(), output); // adiciona nome do cliente na lista
                            sendOnlines();
                        }
                    } else if (action.equals(Action.DISCONNECT)) {
                        disconnect(message, output);
                        sendOnlines();
                        return;   // força saida do loop ao desconectar
                    } else if (action.equals(Action.SEND_ONE)) {
                        sendOne(message);
                    } else if (action.equals(Action.SEND_ALL)) {
                        sendAll(message);
                    } 

                }
            } catch (IOException ex) {
                disconnect(message, output); //excessao - disconecta caso o usuario fechar janela chat
                System.out.println(message.getName() + " deixou o chat");
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    private boolean connect(ChatMessage message, ObjectOutputStream output) {
        if (mapOnlines.size() == 0) {      //primeiro cliente nome a conectar
            message.setText("YES");
            send(message, output);
            return true;  // conexao ok
        }
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (kv.getKey().equals(message.getName())) {  //teste para nao existir nomes iguais
                message.setText("NO");
                send(message,output);
                return false; // nao conecta
            } else {
                message.setText("YES"); //se nao tiver nome igual
                send(message,output);
                return true; // conecta
            }
        }

        return false; //
    }

    private void disconnect(ChatMessage message, ObjectOutputStream output) {  //metodo para desconectar
        mapOnlines.remove(message.getName()); //remove cliente ao desconectar a partir do nome

        message.setText(" deixou o chat \n");

        message.setAction(Action.SEND_ONE); //envia mensagem para todos que o usuario desconectou

        sendAll(message);

        System.out.println("User" + message.getName() + " sai da sala"); //mensagem no console
    }

    private void send(ChatMessage message, ObjectOutputStream output) {
        try {
            output.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void sendOne(ChatMessage message) {
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (kv.getKey().equals(message.getNameReserved())) {

                try {
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    private void sendAll(ChatMessage message) {

        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            if (!kv.getKey().equals(message.getName())) {     //string esta armazena no key , e o boject esta armazenado no value   
                //se a chave da posiçao atual do for for diferente da cliente , essa mensagem e envia pro cliente q possue essa chave, evitando enviar pra ele mesmo
                message.setAction(Action.SEND_ONE);
                try {
                    System.out.println("user: " + message.getName());
                    kv.getValue().writeObject(message);
                } catch (IOException ex) {
                    Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
                }

            }

        }

    }

    private void sendOnlines() {

        Set<String> setNames = new HashSet<String>();
        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
            setNames.add(kv.getKey());
        }

        ChatMessage message = new ChatMessage();
        message.setAction(Action.USERS_ONLINE);
        message.setSetOnlines(setNames);

        for (Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()) {
              message.setName(kv.getKey());
            try {

                kv.getValue().writeObject(message);
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
    }
    
}
