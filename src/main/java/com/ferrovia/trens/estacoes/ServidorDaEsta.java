import java.net.*;

import com.ferrovia.trens.Trem;

//import com.ferrovia.trens.Trem;
//package com.ferrovia.trens.estacoes;
import java.io.*;

public class ServidorDaEsta {

	public static void main(String[] args) {
	

		try
		{
			// abertura del socket, arriba arriba!!!!
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
