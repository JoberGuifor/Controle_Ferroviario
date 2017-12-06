package com.ferrovia.trens;

import com.ferrovia.trens.carga.Passageiro;
import com.ferrovia.trens.estacoes.Estacao;
import com.ferrovia.trens.estacoes.EstacaoController;

import org.apache.log4j.Logger;

import java.util.*;

public class Trem implements Runnable {

    private static final Logger logger = Logger.getLogger(Trem.class);

    private EstacaoController stationController;
    private Estacao currentStation;
    private Map<Integer, List<Passageiro>> cargo;
    private TremEstado state;
    private int speed;
    private int capacity;
    private int id;

    private volatile boolean terminateRequested;

    public Trem(int id, int speed, int capacity, EstacaoController stationController) {
        this.id = id;
        this.speed = speed;
        this.stationController = stationController;
        this.state = TremEstado.STOPPED;
        this.currentStation = stationController.getStart();
        this.capacity = capacity;
        this.cargo = new HashMap<Integer, List<Passageiro>>(capacity);
    }

    /**
     * Viaja até a proxima estação de acordo com a velocidade
     */
    public void viajaAteProxEstacao() {
        unloadCargo();
        loadCargo();
        occupyRailToNextStation();
        travel();
        arriveAtNextStation();
    }

    public void run() {
        while(!terminateRequested) {
            viajaAteProxEstacao();
        }
    }

    private void occupyRailToNextStation() {
        System.out.println(String.format("Train %s tries to occupy rail from station %s to station %s", this, currentStation, stationController.getProxEstacao(currentStation)));
        stationController.getProxEstacao(currentStation).ocuparTrilho(this);
        System.out.println(String.format("Train %s travels from station %s to station %s with speed %d", this, currentStation, stationController.getProxEstacao(currentStation), speed));
    }

    private void arriveAtNextStation() {
        System.out.println(String.format("Train %s arrives at station %s", this, stationController.getProxEstacao(currentStation)));
        stationController.getProxEstacao(currentStation).sairTrilho(this);
        currentStation = stationController.getProxEstacao(currentStation);
    }

    private void travel() {
        try { Thread.sleep(currentStation.getDistancia()/speed); } catch (InterruptedException e) {}
    }

    private void loadCargo() {
//        System.out.println(String.format("Loading cargo from station %s", currentStation));
        while (cargo.size() <= capacity) {
            Passageiro cargoToLoad = currentStation.carregarPassageiroParaTrem();
            if(cargoToLoad == null) break;
            loadCargo(cargoToLoad);
        }
    }
    
    private void loadCargo(Passageiro cargoToLoad) {
        System.out.println(String.format("Trains %s is loading cargo %s from station %s", this, cargoToLoad, currentStation));
        List<Passageiro> cargoList = cargo.get(cargoToLoad.getDestino());
        if (cargoList == null) {
            cargoList = new ArrayList<Passageiro>();
            cargo.put(cargoToLoad.getDestino(), cargoList);
        }
        cargoList.add(cargoToLoad);
    }
    
    private void unloadCargo() {
        List<Passageiro> cargoList = cargo.get(currentStation.getId());
        if(cargoList != null) {
            for(Passageiro cargoToUnload : cargoList) {
                System.out.println(String.format("Trains %s is unloading cargo %s at station %s", this, cargoToUnload, currentStation));
                cargoToUnload.desEmbarcar();
            }
            cargoList.clear();
        }
    }

    public void move() {
        System.out.println(String.format("Train %s %s from %s", this, TremEstado.MOVING, currentStation));
        this.state = TremEstado.MOVING; 
    }

    public void wait() {
        System.out.println(String.format("Train %s %s", this, TremEstado.WAITING));
        this.state = TremEstado.WAITING;
    }

    public void stop() {
        System.out.println(String.format("Train %s %s", this, TremEstado.STOPPED));
        this.state = TremEstado.STOPPED;
    }

    public Estacao getCurrentStation() {
        return currentStation;
    }

    public Map<Integer, List<Passageiro>> getCargo() {
        return cargo;
    }

    // TODO: Does it need synchronization?
    public void terminate() {
        this.terminateRequested = true;
    }

    @Override
    public String toString() {
        return String.format("Train no.[%s]", id);
    }
}
