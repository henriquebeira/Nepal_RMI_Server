/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package nepal.rmi.server;

import entidades.ListaInteresse;
import entidades.Interesse;
import entidades.SalaTransacoes;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import interfaces.InterfaceServ;
import java.util.ArrayList;

/**
 * Classe servidor que recebe as comunicações dos clientes.
 * 
 * @author Henrique
 */
public class ServidorImpl extends UnicastRemoteObject implements InterfaceServ {
    
    private ListaInteresse compras;
    private ListaInteresse vendas;
    private SalaTransacoes salaTransacoes; 

    /**
     * Construtora da classe, que recebe as listas de compras e vendas.
     * 
     * @param compras Lista com interesses de compra.
     * @param vendas Lista com interesses de venda.
     * @throws RemoteException 
     */
    public ServidorImpl(ListaInteresse compras, ListaInteresse vendas) throws RemoteException {
        this.compras = compras;
        this.vendas = vendas;
        salaTransacoes = new SalaTransacoes(compras, vendas);
    }

    /**
     * Método que retorna uma lista de interesses de venda.
     * 
     * @return Lista com interesses de venda registradas no servidor.
     * @throws RemoteException 
     */
    @Override
    public ArrayList<Interesse> consultarInteresseVenda() throws RemoteException {
        System.out.println("Requisição da lista de interesses de venda.");
        return vendas.getLista();   
    }

    /**
     * Método para adicionar um interesse de COMPRA.
     * Verificação de possíveis negociações na sala de transações.
     * 
     * @param interesseCompra Interesse de COMPRA de uma determinada ação.
     * @throws RemoteException 
     */
    @Override
    public void adicionarInteresseCompra(Interesse interesseCompra) throws RemoteException {
        compras.adicionar(interesseCompra);
        System.out.println("Interesse de COMPRA das ações da Empresa "+interesseCompra.getAcao().getEmpresa()+" registrado.");
        verificarSalaDeTransacoes();
    }

    /**
     * Método para adicionar um interesse de VENDA. 
     * Verificação de possíveis negociações na sala de transações.
     *
     * @param interesseVenda Interesse de VENDA de uma determinada ação.
     * @throws RemoteException
     */
    @Override
    public void adicionarInteresseVenda(Interesse interesseVenda) throws RemoteException {
        vendas.adicionar(interesseVenda);
        System.out.println("Interesse de VENDA das ações da Empresa "+interesseVenda.getAcao().getEmpresa()+" registrado.");
        verificarSalaDeTransacoes();
    }

    /**
     * Método para a sala de transações tentar fazer negócios.
     */
    private void verificarSalaDeTransacoes() {
        salaTransacoes.realizarTransacoesPossiveis();
    }
}
