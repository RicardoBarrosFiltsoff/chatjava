

package com.corporativo.app.service;

import com.corporativo.app.bean.ChatMessage;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ricardo Barros Filtsoff
 */
public class ClienteService {
    
    private Socket socket;
    private ObjectOutputStream output;
    
    public Socket connect(){  //conectar
        try { 
            this.socket = new Socket("localhost",5555);  //passando host de sorvidor socket e porta de conexao
            this.output = new ObjectOutputStream(socket.getOutputStream()); //inicializando output
            
        } catch (IOException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    return socket;
    }

    public void send(ChatMessage message){ //enviar mensagens
        try {
            output.writeObject(message);
        } catch (IOException ex) {
            Logger.getLogger(ClienteService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
