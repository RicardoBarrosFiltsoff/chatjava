

package com.corporativo.app;

import com.corporativo.app.frame.ClienteFrame;

//Ricardo Barros Filtsoff

public class Cliente {

   
    public static void main(String args[]) {
        
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ClienteFrame().setVisible(true);
            }
        });
    }
    
}
