/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entidades;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Classe para a realização dos negócios de compra e venda.
 *
 * @author Henrique
 */
public class SalaTransacoes {

    private ListaInteresse compras;
    private ListaInteresse vendas;

    /**
     * Construtora da sala de transações. Recebimento das listas de interesses
     * de compra e venda.
     *
     * @param compras
     * @param vendas
     */
    public SalaTransacoes(ListaInteresse compras, ListaInteresse vendas) {
        this.compras = compras;
        this.vendas = vendas;
    }

    /**
     * Método verifica se há interesses de venda e compra que combinem. 
     * 1. Verifica se o registro de compra e venda não é do mesmo acionista. 
     * 2. Verifica se as ações de uma empresa podem ser negociadas, sendo determinado o preço final. 
     * 3.1. Verifica se a quantidade da compra é MENOR que a quantidade à venda, 
     * removerndo o interesse de COMPRA, e mantendo o de venda. 
     * 3.2. Verifica se a quantidade da compra é MAIOR que a quantidade à venda, 
     * removerndo o interesse de VENDA, e mantendo o de COMPRA. 
     * 3.3. Verifica se a quantidade da compra é IGUAL que a quantidade à venda, 
     * removerndo AMBOS os interesses.
     *
     * @return true se um interesse foi atendido.
     */
    public boolean realizarTransacoesPossiveis() {
        for (int indiceCompra = 0; indiceCompra < compras.getTamanhoLista(); indiceCompra++) {
            for (int indiceVenda = 0; indiceVenda < vendas.getTamanhoLista(); indiceVenda++) {
                Interesse compra = compras.getInteresse(indiceCompra);
                Interesse venda = vendas.getInteresse(indiceVenda);

                if (!compra.getCliente().equals(venda.getCliente())) {
                    if (compra.getAcao().getEmpresa().equals(venda.getAcao().getEmpresa())
                            && compra.getPreco() >= venda.getPreco()) {

                        double precoFinal = (compra.getPreco() + venda.getPreco()) / 2;

                        try {
                            if (compra.getAcao().getQuantidade() < venda.getAcao().getQuantidade()) {
                                venda.getAcao().setQuantidade(venda.getAcao().getQuantidade() - compra.getAcao().getQuantidade());
                                compras.remover(compra);

                                compra.getCliente().comunicarCompraEfetuada(compra.getAcao().getEmpresa(), compra.getAcao().getQuantidade(), precoFinal);
                                venda.getCliente().comunicarVendaEfetuada(compra.getAcao().getEmpresa(), compra.getAcao().getQuantidade(), precoFinal);
                                return true;

                            } else if (compra.getAcao().getQuantidade() > venda.getAcao().getQuantidade()) {
                                compra.getAcao().setQuantidade(compra.getAcao().getQuantidade() - venda.getAcao().getQuantidade());
                                vendas.remover(venda);

                                compra.getCliente().comunicarCompraEfetuada(compra.getAcao().getEmpresa(), venda.getAcao().getQuantidade(), precoFinal);
                                venda.getCliente().comunicarVendaEfetuada(compra.getAcao().getEmpresa(), venda.getAcao().getQuantidade(), precoFinal);
                                return true;

                            } else {
                                compras.remover(compra);
                                vendas.remover(venda);

                                compra.getCliente().comunicarCompraEfetuada(compra.getAcao().getEmpresa(), compra.getAcao().getQuantidade(), precoFinal);
                                venda.getCliente().comunicarVendaEfetuada(compra.getAcao().getEmpresa(), venda.getAcao().getQuantidade(), precoFinal);
                                return true;
                            }
                        } catch (RemoteException ex) {
                            Logger.getLogger(SalaTransacoes.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    }
                }
            }
        }
        return false;
    }
}
