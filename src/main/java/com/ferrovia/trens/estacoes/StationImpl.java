package com.ferrovia.trens.estacoes;

import com.ferrovia.trens.Trem;
import com.ferrovia.trens.carga.Passageiro;
import com.ferrovia.trens.carga.CargoProducer;

import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class StationImpl implements Estacao {

    private static final Logger logger = Logger.getLogger(StationImpl.class);
    
    private int id;
    private Estacao nextStation;
    private int distance;
    private Lock railLock;
    private BlockingQueue<Passageiro> cargoQueue;
    private CargoProducer cargoProducer;
    private volatile boolean terminateProductionRequested;
    
    public StationImpl(int id, Estacao nextStation, CargoProducer cargoProducer, int distance) {
        this.id = id;
        this.nextStation = nextStation;
        this.distance = distance;
        this.railLock = new ReentrantLock();
        this.cargoQueue = new LinkedBlockingQueue<Passageiro>();
        this.cargoProducer = cargoProducer;
        new Thread(this).start();
    }

    public void ocuparTrilho(Trem train) {
        train.doWait();
        railLock.lock();
        train.doMove();
    }

    public void sairTrilho(Trem train) {
        railLock.unlock();
        train.doStop();
    }

    public void entregarCarga(Passageiro cargo) {
        logger.debug(String.format("Delivering cargo %s to station %s", cargo, this));
        try { cargoQueue.put(cargo); } catch (InterruptedException e) {}
    }

    /**
     * Loads carg to train
     * @return
     */
    public Passageiro carregarPassageiroParaTrem() {
        Passageiro cargo = cargoQueue.poll();
        if (cargo != null) {
            logger.debug(String.format("Loading cargo %s from station %s to train", cargo, this));
            cargo.embarcar();
        }
        return cargo;
    }

    public void run() {
        while (!terminateProductionRequested) {
            entregarCarga(cargoProducer.produceCargo());
            if(cargoProducer.getProdInterval() < 0) return;
            try { 
            	Thread.sleep(cargoProducer.getProdInterval()); 
            } catch (InterruptedException e) {}
        }
    }

    public int getId() {
        return id;
    }

    public int getDistancia() {
        return distance;
    }

    public Estacao getProxEstacao() {
        return nextStation;
    }

    public void setProxEstacao(Estacao nextStation) {
        this.nextStation = nextStation;
    }

    public int getTamanhoCarga() {
        return cargoQueue.size();
    }

    public void terminateProduction() {
        this.terminateProductionRequested = false;
    }

    @Override
    public String toString() {
        return String.format("Station no[%s]", id);
    }
}
