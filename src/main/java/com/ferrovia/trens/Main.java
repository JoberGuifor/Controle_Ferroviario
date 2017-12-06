package com.ferrovia.trens;

import com.ferrovia.trens.Trem;
import com.ferrovia.trens.estacoes.LinkedStationController;
import com.ferrovia.trens.estacoes.EstacaoController;

public class Main {

    private static final int NO_OF_STATIONS = 8;
    private static final int NO_OF_TRAINS = 4;
    private static final Trem[] trains = new Trem[NO_OF_TRAINS];

    public static void main(String[] args) {
        EstacaoController stationController = new LinkedStationController(NO_OF_STATIONS);
        for (int i = 0; i < NO_OF_TRAINS; i++) {
            trains[i] = new Trem(i, 100, i + 1, stationController);
        }
        for (int i = 0; i < NO_OF_TRAINS; i++) {
            new Thread(trains[i]).start();
        }
    }
}
