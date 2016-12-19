package com.example.rktesterapps;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidLanConnection extends Activity {
	private static final int TCP_PORT = 5001;
	private final int DIALOG_INSTRUCTION = 0;
	private AlertDialog.Builder dialog;

	private boolean ethernetConnectivity;

	private TextView ipAddress;
	private Button scan;

	private TextView networkConnected;

	WifiManager wifi;
	
	private CheckBox cb_lan_pass, cb_lan_fail;
	
	private static String destination;
	private final String cmd = "CMD_APPEND_FILE";
	
	private static String lan_status;
	private static int lan_flag;
	
	private static ArrayList<String> data = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lan);
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");

		// ipAddress = (TextView) findViewById(R.id.networkIpAddressTextView);
		networkConnected = (TextView) findViewById(R.id.networkTextView);
		scan = (Button) findViewById(R.id.scandevice);
		
		cb_lan_pass = (CheckBox) findViewById(R.id.lan_passed);
		cb_lan_fail = (CheckBox) findViewById(R.id.lan_failed);
		
		cb_lan_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_lan_fail.setOnCheckedChangeListener(new checkBoxListener());

		Button back = (Button) findViewById(R.id.backButton);
		Button home = (Button) findViewById(R.id.homeButton);
		Button next = (Button) findViewById(R.id.nextButton);
		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				Intent homeIntent = new Intent(AndroidLanConnection.this,
						RktesterApps.class);
				startActivity(homeIntent);

			}
		});
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				CharSequence text = "Please leave a remarks on the checkbox provided!";
				int duration = Toast.LENGTH_SHORT;
				
				if(lan_flag == 0){
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();	
				}
				else{
					Intent nextIntent = new Intent(AndroidLanConnection.this,
							AndroidMicSpeeker.class);
					nextIntent.putExtra("file", destination);
					startActivity(nextIntent);
				}
			}
		});
		onShowDialog(DIALOG_INSTRUCTION);

		wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled()) {
			Toast.makeText(getApplicationContext(),
					"Turning off wifi connection!", Toast.LENGTH_LONG).show();
			wifi.setWifiEnabled(false);
		} else
			Toast.makeText(getApplicationContext(),
					"Turning on ethernet connection...", Toast.LENGTH_LONG)
					.show();

		// socketCommand(command);

		scan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				IsConnectInternet();
			}
		});

	}

	private void checking() {
		scan.setClickable(false);
		if (!isEthernetConnected()) {
			networkConnected.setText("Not Connected");
			networkConnected.setTextColor(Color.RED);
			Toast.makeText(getApplicationContext(),
					"Ethernet is not connected", Toast.LENGTH_LONG).show();
			scan.setClickable(true);

		} else {
			networkConnected.setText("Connected");
			networkConnected.setTextColor(Color.GREEN);
			ipAddress.setText(getEthernetIP());
			Toast.makeText(getApplicationContext(), "Ethernet is connected",
					Toast.LENGTH_LONG).show();
			scan.setClickable(true);

		}

	}

	private void onShowDialog(int id) {
		switch (id) {
		case DIALOG_INSTRUCTION:
			dialog = new AlertDialog.Builder(this);
			dialog.setMessage("Check if LAN is connected to the device.")
					.setCancelable(false)
					.setPositiveButton("OK",
							new DialogInterface.OnClickListener() {
								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();
								}
							}).create();
			if (!isFinishing())
				dialog.show();
		}
	}

	public String getEthernetIP() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				final NetworkInterface intf = en.nextElement();

				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (intf.getName().equals("eth0")
							&& !inetAddress.isLoopbackAddress()) {
						Log.d("LAN.java", String.format(
								"Network Interface : %s, IP address : %s", intf
										.getName().toString(), inetAddress
										.getHostAddress().toString()));
						return inetAddress.getHostAddress().toString();
					}

				}
			}
		} catch (SocketException ex) {
			Log.e("LAN.java", ex.toString());
		}

		return null;
	}

	public boolean isEthernetConnected() {
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				final NetworkInterface intf = en.nextElement();
				if (intf.getName().equals("eth0")) {
					return true;
				}
			}
		} catch (SocketException ex) {
			Log.e("LAN.java", ex.toString());
		}
		return false;
	}

	private boolean IsConnectInternet() {
		// get Connectivity Manager object to check connection
		ConnectivityManager connect = (ConnectivityManager) getSystemService(getBaseContext().CONNECTIVITY_SERVICE);
		if (connect == null) {
			// Fix ANR
			return false;
		}

		NetworkInfo network_ethernet = connect
				.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);

		State state_0 = network_ethernet.getState();

		if (state_0 == android.net.NetworkInfo.State.CONNECTED
				|| state_0 == android.net.NetworkInfo.State.CONNECTING) {
			// Connected
			networkConnected.setText("Connected");
			networkConnected.setTextColor(Color.GREEN);
		//	ipAddress.setText(getEthernetIP());
			Toast.makeText(getApplicationContext(), "Ethernet is connected",
					Toast.LENGTH_LONG).show();
			return true;

		} else if (state_0 == android.net.NetworkInfo.State.DISCONNECTED) {
			networkConnected.setText("Not Connected");
			networkConnected.setTextColor(Color.RED);
			Toast.makeText(getApplicationContext(),
					"Ethernet is not connected", Toast.LENGTH_LONG).show();
			return false;
		} else {
			// Do nothing
		}
		return false;
	}
	
	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.lan_passed:
				if (cb_lan_pass.isChecked()) {
					lan_status = "ETHERNET CONNECTION = PASSED";
					lan_flag = 1;
					
					cb_lan_fail.setEnabled(false);
					cb_lan_fail.setClickable(false);
					cb_lan_fail.setChecked(false);
					
					data.clear();
					remarks(lan_status);
				} else {
					lan_flag = 0;
					cb_lan_fail.setEnabled(true);
					cb_lan_fail.setClickable(true);
				}
				break;
			case R.id.lan_failed:
				if (cb_lan_fail.isChecked()) {
					lan_status = "ETHERNET CONNECTION = FAILED";
					lan_flag = 1;
					
					cb_lan_pass.setEnabled(false);
					cb_lan_pass.setClickable(false);
					cb_lan_pass.setChecked(false);
					
					data.clear();
					remarks(lan_status);
				} else {
					lan_flag = 0;
					cb_lan_pass.setEnabled(true);
					cb_lan_pass.setClickable(true);
				}
				break;
			}
		}

		private void remarks(String content) {
			data.add(content);
			data.add(destination);
			data.add(cmd);		
			Report reporttask = new Report();
			reporttask.execute(data);
		}
	}
}
