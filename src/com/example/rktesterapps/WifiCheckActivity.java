package com.example.rktesterapps;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class WifiCheckActivity extends Activity {
	private StringBuilder sb = new StringBuilder();
	private TextView tv;

	private CheckBox cb_wifi_pass, cb_wifi_fail;

	List<ScanResult> scanList;

	private static String destination;
	private final String cmd = "CMD_APPEND_FILE";
	private static String wifi_status;
	private static int wifi_flag;

	private static ArrayList<String> data = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wifi_activity);
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");
		
		tv = (TextView) findViewById(R.id.txtWifiNetworks);

		cb_wifi_pass = (CheckBox) findViewById(R.id.wifi_passed);
		cb_wifi_fail = (CheckBox) findViewById(R.id.wifi_failed);

		cb_wifi_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_wifi_fail.setOnCheckedChangeListener(new checkBoxListener());

		Button back = (Button) findViewById(R.id.backButton);
		Button home = (Button) findViewById(R.id.homeButton);
		Button next = (Button) findViewById(R.id.nextButton);
		Button scan = (Button) findViewById(R.id.scanButton);
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

				Intent homeIntent = new Intent(WifiCheckActivity.this, RktesterApps.class);
				startActivity(homeIntent);

			}
		});
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				CharSequence text = "Please leave a remarks on the checkbox provided!";
				int duration = Toast.LENGTH_SHORT;

				if (wifi_flag == 0) {
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				} else {
					Intent nextIntent = new Intent(WifiCheckActivity.this, AndroidSdCard.class);
					nextIntent.putExtra("file", destination);
					startActivity(nextIntent);
				}
			}
		});
		scan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				getWifiNetworksList();
				checkwifi();
			}
		});
		checkwifi();
	}

	private void checkwifi() {
		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		if (wifi.isWifiEnabled()) {
			// wifi is enabled
			Toast.makeText(getApplicationContext(), "Wifi is Enabled", Toast.LENGTH_LONG).show();
			return;
		} else {
			Toast.makeText(getApplicationContext(), "Wifi is Dissabled", Toast.LENGTH_LONG).show();
			Toast.makeText(getApplicationContext(), "Turning it on..", Toast.LENGTH_LONG).show();
			WifiManager wififi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
			wififi.setWifiEnabled(true); // True or False to activate/deactivate
											// wifi

		}
	}

	private void getWifiNetworksList() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
		final WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

		registerReceiver(new BroadcastReceiver() {

			@SuppressLint("UseValueOf")
			@Override
			public void onReceive(Context context, Intent intent) {
				sb = new StringBuilder();
				scanList = wifiManager.getScanResults();
				sb.append("\n  Number Of Wifi connections :" + " " + scanList.size() + "\n\n");
				for (int i = 0; i < scanList.size(); i++) {
					sb.append(new Integer(i + 1).toString() + ". ");
					sb.append((scanList.get(i)).toString());
					sb.append("\n\n");
				}

				tv.setText(sb);
			}

		}, filter);
		wifiManager.startScan();
	}

	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.wifi_passed:
				if (cb_wifi_pass.isChecked()) {
					wifi_status = "WIFI = PASSED";
					wifi_flag = 1;
					cb_wifi_fail.setEnabled(false);
					cb_wifi_fail.setClickable(false);
					cb_wifi_fail.setChecked(false);

					data.clear();
					remarks(wifi_status);
				} else {
					wifi_flag = 0;
					cb_wifi_fail.setEnabled(true);
					cb_wifi_fail.setClickable(true);
				}
				break;
			case R.id.wifi_failed:
				if (cb_wifi_fail.isChecked()) {
					wifi_status = "WIFI = FAILED";
					wifi_flag = 1;
					cb_wifi_pass.setEnabled(false);
					cb_wifi_pass.setClickable(false);
					cb_wifi_pass.setChecked(false);

					data.clear();
					remarks(wifi_status);
				} else {
					wifi_flag = 0;
					cb_wifi_pass.setEnabled(true);
					cb_wifi_pass.setClickable(true);
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
