package com.ferrovia.trens.estacoes;

import java.net.*;

import com.ferrovia.trens.Trem;

import java.io.*;

public class ServidorDaEsta {

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


		try
		{
			// abertura del socket, arriba arriba!!!!
			ServerSocket server = new ServerSocket(12345);
			
			Socket client;
			BufferedReader in;
			
			System.out.println("Server ready (CTRL-C to kill)\n");
			
			// ciclo infinito
			while(true)
			{
				// chamada bloqueante
				client = server.accept();
				System.out.println("Client connected: "+client);
				
				// leitura
				in = new BufferedReader(new InputStreamReader(client.getInputStream()));
				System.out.println("The buffer:  "+client.getInetAddress()+": "+in.readLine()+"\n");
				
				// hasta la vista socket
				client.close();
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
