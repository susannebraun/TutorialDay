package de.sleepingcat.ledcontrol.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkCommandTask extends AsyncTask<String, Integer, Boolean> {
	
	private static final String LOG_TAG = "NetworkCommandTask";
	
	private Socket mySocket;

	private NetworkCommandListener myListener;
	
	

	public NetworkCommandTask(Socket mySocket) {
		super();
		this.mySocket = mySocket;
	}

	@Override
	protected Boolean doInBackground(String... cmd) {
		if(cmd == null || cmd.length != 1) {
			return false;
		}
		
		if(mySocket != null && mySocket.isConnected()) {
			
	    	PrintWriter out = null;
	    	BufferedReader in = null;
	    	try {
	    		 	// fire and forget style!
	    		   out = new PrintWriter(mySocket.getOutputStream(), true);
	    		   out.write(cmd[0]);
	    		   out.write("\n");
	    		   out.flush();
	    		   
	    		   
//	    		   in = new BufferedReader(new InputStreamReader(mySocket.getInputStream()));
//	    		   String response =  in.readLine();
//	    		   if("OK".equals(response)) {
	    			   return true;
//	    		   }
	    		   
	    	} catch(UnknownHostException e) {
	    		   Log.e(LOG_TAG, "Failed to send cmd to LED.", e);
	    	} catch(IOException e) {
	    			Log.e(LOG_TAG, "Failed to send cmd to LED.", e);
			} finally {
//				if (out != null) {
//					out.close();
//				}
//				if (in != null) {
//					try {
//						in.close();
//					} catch (IOException e) {
//						Log.e(LOG_TAG, "Failed to close input stream.", e);
//					}
//				}
			}
		
		} 
		
			// error!
			if(mySocket != null) {
				try {
					mySocket.close();
				} catch (IOException e) {
					Log.w(LOG_TAG, "Failed to close socket.", e);
				}
			}
		
		return false;
	}

	@Override
	protected void onPostExecute(Boolean success) {
		if(!success && myListener != null) {
			myListener.onError();
		}
	}
	
	public static interface NetworkCommandListener {
		void onError();
	}
	
}
