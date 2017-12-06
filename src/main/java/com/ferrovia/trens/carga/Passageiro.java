package com.ferrovia.trens.carga;

import com.ferrovia.trens.estacoes.Estacao;

public interface Passageiro {

    public static final long TEMPO_CARREGAMENTO = 20;
    public static final long TEMPO_DESCARREGAMENTO = 20;
    public void desEmbarcar();
    public void embarcar();
    public int getDestino();
    public void comprarPasse(float valor);
}
