/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nepal.rmi.server;

import entidades.ListaInteresse;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe principal do servidor.
 * 
 * @author Henrique
 */
public class Nepal_RMI_Server {

    /**
     * Método Main do servidor.
     * Criação das listas que registrarão as intenções de compra e venda.
     * Ativação efetiva do servidor RMI.
     * 
     * @param args 
     */
    public static void main(String[] args) {
        try {
            ListaInteresse compras = new ListaInteresse();
            ListaInteresse vendas = new ListaInteresse();
            
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.bind("bolsa_nepal", new ServidorImpl(compras, vendas));

        } catch (RemoteException | AlreadyBoundException ex) {
            Logger.getLogger(Nepal_RMI_Server.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
}
