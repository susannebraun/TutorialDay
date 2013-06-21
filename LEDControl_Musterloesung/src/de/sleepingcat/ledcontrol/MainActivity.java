package de.sleepingcat.ledcontrol;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Toast;
import de.sleepingcat.ledcontrol.network.ConnectionManagementTask;
import de.sleepingcat.ledcontrol.network.ConnectionManagementTask.ConnectionEstablishedListener;
import de.sleepingcat.ledcontrol.network.NetworkCommandTask;

public class MainActivity extends Activity implements ConnectionEstablishedListener {
	
	private static final String LOG_TAG = "MainActivity";
	
	
	private EditText ipAddrOrHostnameEditText;
	
	private EditText portEditText;
	
	private SeekBar lightnessSeekbar;
	
	private Button connectButton;
	
	
	private Socket socket;
	
	

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_main); 
        
        ipAddrOrHostnameEditText = (EditText) findViewById(R.id.ip_addr_text_view);
        portEditText = (EditText) findViewById(R.id.port_text_view);
        
        connectButton = (Button) findViewById(R.id.connect_button);
        connectButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if(socket == null) {
					String hostSpec = ipAddrOrHostnameEditText.getText().toString();
					String port = portEditText.getText().toString();
				
					ConnectionManagementTask connectionMgmtTask = new ConnectionManagementTask(MainActivity.this);
					
					connectionMgmtTask.execute(hostSpec, port);
				
				} else {
					final Socket finalSocket = socket;
					onError();
					(new Handler()).post(new Runnable() {
						
						public void run() {
							try {
								finalSocket.close();
							} catch (IOException e) {
								Log.e(LOG_TAG, "Failed to close socket", e);
							}
						}
					});
				}
			}
		});
        
        
        lightnessSeekbar = (SeekBar) findViewById(R.id.led_lightness_seekbar);
        lightnessSeekbar.setEnabled(false);
        
        final Executor executor = Executors.newSingleThreadExecutor();
        lightnessSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			
			public void onStopTrackingTouch(SeekBar seekBar) {
			}
			
			public void onStartTrackingTouch(SeekBar seekBar) {
			}
			
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO: SB show value in Text field!
				if(socket == null) {
					Toast.makeText(MainActivity.this, "Keine Verbindung mit LED", Toast.LENGTH_SHORT);
					return;
				}
				Log.d(LOG_TAG, "Brightness: "+progress);
				NetworkCommandTask netTask = new NetworkCommandTask(socket);
				netTask.executeOnExecutor(executor, String.valueOf(progress));
			}
		});
       
    }
    
    

   



	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }


	public void onConnectionEstablished(Socket socketConnection) {
		this.connectButton.setText("Trennen");
		this.socket = socketConnection;
		this.lightnessSeekbar.setEnabled(true);
	}


	public void onError() {
		this.connectButton.setText("Verbinden");
		this.socket = null;
		this.lightnessSeekbar.setEnabled(false);
	}
}
