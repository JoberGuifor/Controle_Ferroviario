package com.ferrovia.trens.estacoes;

/**
 * User: dmaragkos
 * Date: 2/19/12
 * Time: 1:24 PM
 */
public interface EstacaoController {

    public Estacao getStart();
    public Estacao getNextStation(Estacao station);
    public int getSize();
    
}
