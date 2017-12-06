package com.ferrovia.trens.estacoes;

import com.ferrovia.trens.Trem;
import com.ferrovia.trens.carga.Passageiro;

public interface Estacao extends Runnable {

    public int getId();
    public void ocuparTrilho(Trem train);
    public void sairTrilho(Trem train);
    public int getDistancia();
    public void entregarCarga(Passageiro cargo);
    public Passageiro carregarPassageiroParaTrem();
    public Estacao getProxEstacao();
    public void setProxEstacao(Estacao nextStation);
    public int getTamanhoCarga();
    public void terminateProduction();

}
