package com.ferrovia.trens.estacoes;

import com.ferrovia.trens.carga.CargoProducer;

import java.util.Random;

/**
 * User: dmaragkos
 * Date: 2/19/12
 * Time: 12:01 PM
 */
public class LinkedStationController implements StationController {

    private static final Random random = new Random();
    private static final int MAX_DISTANCE = 1000;

    private int numberOfStations;
    private Estacao start;

    public LinkedStationController(Estacao start) {
        this.start = start;
    }

    public LinkedStationController(int numberOfStations) {
        this.numberOfStations = numberOfStations;
        initStations();
    }

    private void initStations() {
        Estacao lastStation = new StationImpl(numberOfStations - 1, null, new CargoProducer(numberOfStations, numberOfStations - 1), random.nextInt(MAX_DISTANCE));
        Estacao prevStation = lastStation;
        for (int i = numberOfStations - 2; i >= 0; i--) {
            prevStation = new StationImpl(i, prevStation, new CargoProducer(numberOfStations, i), random.nextInt(MAX_DISTANCE));
        }
        start = prevStation;
        lastStation.setProxEstacao(prevStation);
    }

    public Estacao getStart() {
        return start;
    }

    public Estacao getNextStation(Estacao station) {
        return station.getProxEstacao();
    }

    public int getSize() {
        return numberOfStations;
    }
}
