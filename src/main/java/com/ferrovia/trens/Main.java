package com.ferrovia.trens;

import com.ferrovia.trens.Trem;
import com.ferrovia.trens.carga.ClienteTrem;
import com.ferrovia.trens.estacoes.LinkedStationController;
import com.ferrovia.trens.estacoes.ServidorDaEsta;
import com.ferrovia.trens.estacoes.EstacaoController;

public class Main {
	private static final int NO_OF_STATIONS = 8;
	
	
	  public static void main (String args[ ]) {

			  EstacaoController stationController = new LinkedStationController(NO_OF_STATIONS);
			  
	  }
}