package com.github.susannebraun.gulasch;

import java.io.IOException;
import java.net.ServerSocket;

public class Main {
	
	private static final int BIND_PORT = 4321;

	/**
	 * @param args
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		 ServerSocket serverSocket = null;
	        boolean listening = true;

	        try {
	            serverSocket = new ServerSocket(BIND_PORT);
	        } catch (IOException e) {
	            System.err.println("Could not listen on port: 4444.");
	            System.exit(-1);
	        }

	        while (listening) {
	        	new ServerThread(serverSocket.accept()).start();
	        }

	        serverSocket.close();
	}

}
