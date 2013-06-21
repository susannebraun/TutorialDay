package de.sleepingcat.ledcontrol.network;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import android.os.AsyncTask;
import android.util.Log;

public class NetworkCommandTask extends AsyncTask<String, Integer, Boolean> {
	
	private static final String LOG_TAG = "NetworkCommandTask";
	
	private Socket mySocket;

	private NetworkCommandListener myListener;
	
	

	public NetworkCommandTask(Socket socket, NetworkCommandListener listener) {
		super();
		this.mySocket = socket;
		this.myListener = listener;
	}

	@Override
	protected Boolean doInBackground(String... cmd) {
		if (cmd == null || cmd.length != 1) {
			return false;
		}

		if (mySocket != null && mySocket.isConnected()) {
			PrintWriter out = null;
			try {
				// fire and forget style!
				out = new PrintWriter(mySocket.getOutputStream(), true);
				out.write(cmd[0]);
				out.write("\n");
				out.flush();

				return true;

			} catch (UnknownHostException e) {
				Log.e(LOG_TAG, "Failed to send cmd to LED.", e);
			} catch (IOException e) {
				Log.e(LOG_TAG, "Failed to send cmd to LED.", e);
			}

		}

		// error!
		if (mySocket != null) {
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
