

package com.corporativo.app.service;

import com.corporativo.app.bean.ChatMessage;
import com.corporativo.app.bean.ChatMessage.Action;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Barros Filtsoff
 */
public class ServidorService {
    
    private ServerSocket serverSocket;  // 
    private Socket socket;  //
    private Map<String,ObjectOutputStream> mapOnlines = new HashMap<String, ObjectOutputStream>();
    // todo usuario q se conectar ao chat no servidor deve ser adicionado a esta lista
    
    public ServidorService(){
    
        try {
            serverSocket = new ServerSocket(5555); //porta de conexao do cliente com servidor
            
            while(true){   //manter server socket sempre esperando nova conexao                
               
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
        
        public ListenerSocket(Socket socket){ 
        
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
                while ((message = (ChatMessage) input.readObject())!= null){
                    
                  Action action = message.getAction();
                  
                  if(action.equals(Action.CONNECT)){     //teste do tipo de mensagem enviada pelo cliente
                      
                      boolean isConnect = connect(message, output);  //pedido de conexao
                      if(isConnect) {
                      mapOnlines.put(message.getName() , output); // adiciona nome do cliente na lista
                      }
                  }
                  
                  else if(action.equals(Action.DISCONNECT)){
                      disconnect(message, output); 
                  }     
                  else if(action.equals(Action.SEND_ONE)){
                      
                  }
                  else if(action.equals(Action.SEND_ALL)){
                      
                  }
                  else if(action.equals(Action.USERS_ONLINE)){
                      
                  }
                  
                }
            } catch (IOException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
       
        }    
    private boolean connect(ChatMessage message, ObjectOutputStream output){ 
        if(mapOnlines.size()==0){      //primeiro cliente nome a conectar
            message.setText("YES");   
            sendOne(message, output);  
            return true;  // conexao ok
        }
        for(Map.Entry<String, ObjectOutputStream> kv : mapOnlines.entrySet()){
            if(kv.getKey().equals(message.getName())){  //teste para nao existir nomes iguais
                message.setText("NO");
                sendOne(message, output);
                return false; // nao conecta
            }
            else {
                message.setText("YES"); //se nao tiver nome igual
                sendOne(message, output);
                return true; // conecta
            }
        }
        
       return false; //
    }
    
    private void disconnect(ChatMessage message, ObjectOutputStream output){  //metodo para desconectar
        mapOnlines.remove(message.getName()); //remove cliente ao desconectar a partir do nome
        
        message.setText("Desconectado");
        
        message.setAction(Action.SEND_ONE); //envia mensagem para todos que o usuario desconectou
        
        sendAll(message, output);
        
        System.out.println("User" + message.getName() + " sai da sala"); //mensagem no console
    }
    
    private void sendOne(ChatMessage message, ObjectOutputStream output){
        try {
            output.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ServidorService.class.getName()).log(Level.SEVERE, null, ex);
        }
    
    }
     private void sendAll(ChatMessage message, ObjectOutputStream output) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
