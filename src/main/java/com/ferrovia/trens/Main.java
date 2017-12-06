package com.ferrovia.trens;

import com.ferrovia.trens.Trem;
import com.ferrovia.trens.carga.ClienteTrem;
import com.ferrovia.trens.estacoes.LinkedStationController;
import com.ferrovia.trens.estacoes.ServidorDaEsta;
import com.ferrovia.trens.estacoes.EstacaoController;

public class Main {
	  public static void main (String args[ ]) {
		  
		 
		  ServidorDaEsta sd = new ServidorDaEsta();
		  ClienteTrem ct = new ClienteTrem();
		  sd.main(null);
		  ct.main(new String[] {"que passa server?"});
	  }
}