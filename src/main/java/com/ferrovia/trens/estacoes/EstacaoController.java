package com.ferrovia.trens.estacoes;

public interface EstacaoController {

    public Estacao getStart();
    public Estacao getProxEstacao(Estacao station);
    public int getSize();
    
}
