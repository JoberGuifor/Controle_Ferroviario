//package com.ferrovia.trens.carga;

import java.net.*;
import java.io.*;

public class ClienteTrem {
	public static void main(String[] args)
	{
	// verifica a correção dos parâmetros
//			args[0]= "hola mundo";
			if (args.length != 1)
			{
				System.out.println("Usage: java myclient \"message to send\"");
				return;
			}
			
			try
			{
				// preparação do endereço do servidor
				InetAddress address = InetAddress.getByName("localhost");
				
				// criação do socket
				Socket client = new Socket(address, 12345);
				
				System.out.println("Client is ready.\n");
				
				// riação de buffer de escrita
				PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(client.getOutputStream())), true);
		
				System.out.println("Buffer ready, sending message \""+args[0]+"\"...\n");
				
				// escrevendo a mensagem (passada como um parâmetro) no buffer de saída
				out.println(args[0]);
				
				System.out.println("Message was sent.\n");
				
				// encerramento do soquete
				client.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
	}
}
