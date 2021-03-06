package com.ferrovia.trens.estacoes;

import com.ferrovia.trens.Trem;
import com.ferrovia.trens.carga.Passageiro;
import com.ferrovia.trens.carga.CargoProducer;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class EstacaoImpl implements Estacao {


	private int id;
    private Estacao nextStation;
    private int distance;
    private Lock railLock;
    private BlockingQueue<Passageiro> cargoQueue;
    private CargoProducer cargoProducer;
    private volatile boolean terminateProductionRequested;
    
    public EstacaoImpl(int id, Estacao nextStation, CargoProducer cargoProducer, int distance) {
        this.id = id;
        this.nextStation = nextStation;
        this.distance = distance;
        this.railLock = new ReentrantLock();
        this.cargoQueue = new LinkedBlockingQueue<Passageiro>();
        this.cargoProducer = cargoProducer;
        new Thread(this).start();
     
    }
    

    public void ocuparTrilho(Trem train) {
        train.makewait();
        railLock.lock();
        train.move();
    }

    public void sairTrilho(Trem train) {
        railLock.unlock();
        train.stop();
    }

    public void entregarCarga(Passageiro cargo) {
        System.out.println(String.format("Entregando passageiro %s na estação %s", cargo, this));
        try { 
        	cargoQueue.put(cargo); 
        } catch (InterruptedException e) {}
    }

    public Passageiro carregarPassageiroParaTrem() {
        Passageiro cargo = cargoQueue.poll();
        if (cargo != null) {
            System.out.println(String.format("Embarcando passageiro %s para estação %s ", cargo, this));
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
        return String.format("Estação no[%s]", id);
    }
}
