package com.ferrovia.trens.carga;

import org.apache.log4j.Logger;

public class PassageiroImpl implements Passageiro {
    
    private static final Logger logger = Logger.getLogger(PassageiroImpl.class);

    private int idEstacao;
    private int numpassageiros;
    private Ticket passe;

    public PassageiroImpl (int stationId, int units) {
        this.idEstacao = stationId;
        this.numpassageiros = units;
    }

    public void embarcar() {
        logger.debug(String.format("Carregando Passageiros %s", this));
        try { 
        	Thread.sleep(numpassageiros * TEMPO_CARREGAMENTO); 
        } catch (InterruptedException e) {}
        logger.debug(String.format("Passageiros carregada %s com sucesso", this));
    }

    public void desEmbarcar() {
        logger.debug(String.format("Descarregando Passageiros %s", this));
        try { 
        	Thread.sleep(numpassageiros * TEMPO_DESCARREGAMENTO); 
        } catch (InterruptedException e) {}
        
        logger.debug(String.format("Passageiros descarregados %s com suceeso", this));
    }

    public int getDestino() {
        return idEstacao;
    }
    
    public void comprarPasse(float valor){
    	this.passe = new Ticket(valor);
    }

    @Override
    public String toString() {
        return String.format("Embarcando para estação no.[%d] com %d passageiros", idEstacao, numpassageiros);
    }
}
