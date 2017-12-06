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
        System.out.println(String.format("Trem %s tenta ocupar o trilho para estação %s da estaçao %s", this, currentStation, stationController.getProxEstacao(currentStation)));
        stationController.getProxEstacao(currentStation).ocuparTrilho(this);
        System.out.println(String.format("Trem %s viaja para estação %s da estação %s com velocidade %d", this, currentStation, stationController.getProxEstacao(currentStation), speed));
    }

    private void arriveAtNextStation() {
        System.out.println(String.format("Trem %s chega na estação %s", this, stationController.getProxEstacao(currentStation)));
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
        System.out.println(String.format("Trem %s está carregando %s para estação %s", this, cargoToLoad, currentStation));
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
                System.out.println(String.format("Trem %s está descarregando %s na estação %s", this, cargoToUnload, currentStation));
                cargoToUnload.desEmbarcar();
            }
            cargoList.clear();
        }
    }

    public void move() {
        System.out.println(String.format("Trem %s %s para %s", this, TremEstado.MOVING, currentStation));
        this.state = TremEstado.MOVING; 
    }

    public void makewait() {
        System.out.println(String.format("Trem %s %s", this, TremEstado.WAITING));
        this.state = TremEstado.WAITING;
    }

    public void stop() {
        System.out.println(String.format("Trem %s %s", this, TremEstado.STOPPED));
        this.state = TremEstado.STOPPED;
    }

    public Estacao getCurrentStation() {
        return currentStation;
    }

    public Map<Integer, List<Passageiro>> getCargo() {
        return cargo;
    }

    public void terminate() {
        this.terminateRequested = true;
    }

    @Override
    public String toString() {
        return String.format("Trem no.[%s]", id);
    }
}
