package com.ferrovia.trens.estacoes;

import com.ferrovia.trens.Trem;
import com.ferrovia.trens.carga.CargoProducer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class LinkedStationController implements EstacaoController {
	private static final int NO_OF_TRAINS = 4;
	private static final Trem[] trains = new Trem[NO_OF_TRAINS];
	Socket client;
	BufferedReader in;
	String parametros;
	
    private static final Random random = new Random();
    private static final int MAX_DISTANCE = 1000;

    private int numberOfStations;
    private Estacao start;
    private EstacaoController mySelf;
    

    public LinkedStationController(Estacao start) {

        this.start = start;
        mySelf = (EstacaoController) this;
    	
    }

    public LinkedStationController(int numberOfStations) {
        this.numberOfStations = numberOfStations;
        initStations();
    }

    private void initStations() {
    	   Estacao lastStation = new EstacaoImpl(numberOfStations - 1, null, new CargoProducer(numberOfStations, numberOfStations - 1), random.nextInt(MAX_DISTANCE));
           Estacao prevStation = lastStation;
           for (int i = numberOfStations - 2; i >= 0; i--) {
               prevStation = new EstacaoImpl(i, prevStation, new CargoProducer(numberOfStations, i), random.nextInt(MAX_DISTANCE));
           }
           start = prevStation;
           lastStation.setProxEstacao(prevStation);
        try {
        	// Aperture del socket, arriba arriba!!!!
    		ServerSocket server = new ServerSocket(12345);
    		System.out.println("Server ready (CTRL-C to kill)\n");
    		
            
    		// ciclo infinito
    		while(true)
    		{
    			// chamada bloqueante
    			client = server.accept();
    			System.out.println("Client connected: "+client);
    			
    			// leitura
    			in = new BufferedReader(new InputStreamReader(client.getInputStream()));
    			parametros = in.readLine();
    			System.out.println("The buffer:  "+client.getInetAddress()+": "+parametros+"\n");
    			if (parametros.equalsIgnoreCase("iniciar embarque")) {
    				
    			    for (int i = 0; i < NO_OF_TRAINS; i++) {
    			        trains[i] = new Trem(i, 100, i + 1, this);
    			    }
    			    for (int i = 0; i < NO_OF_TRAINS; i++) {
    			        new Thread(trains[i]).start();
    			    }
    			}
    			// hasta la vista socket
    			client.close();
    		}}
    		
    	catch (Exception e)
    	{
    		e.printStackTrace();
    	}
     
    }

    public Estacao getStart() {
        return start;
    }

    public Estacao getProxEstacao(Estacao station) {
        return station.getProxEstacao();
    }

    public int getSize() {
        return numberOfStations;
    }
}
