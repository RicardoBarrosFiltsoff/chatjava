

package com.corporativo.app.bean;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author Ricardo Barros Filtsoff
 */
public class ChatMessage implements Serializable { //envia objeto da classe setmessage
    
 private String name;  //nome cliente
 private String text;   //texto mensagem
 private String nameReserved; //mensagem reservada
 private Set<String> setOnlines = new HashSet<String>();    //lista armazenar clientes onlines no servidor enquanto estiver ativo

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getNameReserved() {
        return nameReserved;
    }

    public void setNameReserved(String nameReserved) {
        this.nameReserved = nameReserved;
    }

    public Set<String> getSetOnlines() {
        return setOnlines;
    }

    public void setSetOnlines(Set<String> setOnlines) {
        this.setOnlines = setOnlines;
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }
 private Action action; //para cada mensagem enviada ao servidor define ação a ser executada
 
 public enum Action {
 
     CONNECT, DISCONNECT, SEND_ONE, SEND_ALL, USERS_ONLINE  //valores do action: conexao, sair chat, enviar mensagem reservada
                                                            // enviar mensagem para todos, atualizar lista de usuarios online 
 }
    
}
