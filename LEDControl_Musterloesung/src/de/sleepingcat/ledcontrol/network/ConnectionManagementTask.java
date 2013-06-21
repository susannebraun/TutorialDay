package de.sleepingcat.ledcontrol.network;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class ConnectionManagementTask extends AsyncTask<String, Integer, Socket> {
	
	private static final String LOG_TAG = "ConnectionManagmentTask";
	
	private ConnectionEstablishedListener myListener;
	
	
	

	public ConnectionManagementTask(ConnectionEstablishedListener myListener) {
		super();
		this.myListener = myListener;
	}

	@Override
	protected Socket doInBackground(String... hostAndPort) {
		if(hostAndPort == null || hostAndPort.length != 2) {
			return null;
		}
		
		Socket connection = null;
		try {
			String host = hostAndPort[0];
			int port = Integer.valueOf(hostAndPort[1]);
			
			connection = new Socket();
			connection.connect(new InetSocketAddress(host, port), 3000);
			
		} catch(NumberFormatException e) {
			// invalid port value...
			Log.e(LOG_TAG, "Failed to establish socket connection.", e);
		} catch (UnknownHostException e) {
			Log.e(LOG_TAG, "Failed to establish socket connection.", e);
		} catch (IOException e) {
			Log.e(LOG_TAG, "Failed to establish socket connection.", e);
		}
		
		return connection;
	}

	@Override
	protected void onPostExecute(Socket result) {
		if(myListener != null) {
			if(result != null && result.isConnected()) {
				myListener.onConnectionEstablished(result);
			} else {
				myListener.onError();
			}
		}
	}

	
	public static interface ConnectionEstablishedListener {
		void onConnectionEstablished(Socket socketConnection);
		
		void onError();
	}
}
