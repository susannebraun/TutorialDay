package com.github.susannebraun.gulasch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;



public class ServerThread extends Thread {
    private Socket socket = null;

	public ServerThread(Socket socket) {
		super("ServerThread");
		this.socket = socket;
	}

    public void run() {

	try {
	    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
	    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

	    String inputLine, outputLine;

	    while ((inputLine = in.readLine()) != null) {
	    	outputLine = processInput(inputLine);
	    	out.println(outputLine);
	    	out.flush();
	    }
	    

		} catch (IOException e) {
			e.printStackTrace();
		}
    }

	private String processInput(Object object) {
		if(object == null) {
			return "ERROR\n"; 
		}
		
		if(object instanceof String) {
			String cmd = (String) object;
			System.out.println("Received the following command: "+cmd);
			
			try {
				Integer brightness = Integer.valueOf(cmd);
				return "OK\n";
			} catch(NumberFormatException e) {
				return "ERROR\n";
			}
			
		}
		
		return "ERROR\n";
	}
}
