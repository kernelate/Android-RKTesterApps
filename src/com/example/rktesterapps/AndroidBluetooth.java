package com.example.rktesterapps;

import java.util.ArrayList;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidBluetooth extends Activity {

	private static final int REQUEST_ENABLE_BT = 1;

	ListView listDevicesFound;
	Button btnScanDevice;
	TextView stateBluetooth;
	BluetoothAdapter bluetoothAdapter;

	private CheckBox cb_bluetooth_pass, cb_bluetooth_fail;

	private static String destination;
	private final String cmd = "CMD_APPEND_FILE";

	private static String bt_status;
	private static int bt_flag;

	private static ArrayList<String> data = new ArrayList<String>();

	ArrayAdapter<String> btArrayAdapter;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bluetooth_activity);
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");

		btnScanDevice = (Button) findViewById(R.id.scandevice);

		stateBluetooth = (TextView) findViewById(R.id.bluetoothstate);
		bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

		listDevicesFound = (ListView) findViewById(R.id.devicesfound);
		btArrayAdapter = new ArrayAdapter<String>(AndroidBluetooth.this, android.R.layout.simple_list_item_1);
		listDevicesFound.setAdapter(btArrayAdapter);

		cb_bluetooth_pass = (CheckBox) findViewById(R.id.bt_passed);
		cb_bluetooth_fail = (CheckBox) findViewById(R.id.bt_failed);

		cb_bluetooth_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_bluetooth_fail.setOnCheckedChangeListener(new checkBoxListener());

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

				Intent homeIntent = new Intent(AndroidBluetooth.this, RktesterApps.class);
				startActivity(homeIntent);

			}
		});
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				CharSequence text = "Please leave a remarks on the checkbox provided!";
				int duration = Toast.LENGTH_SHORT;

				if (bt_flag == 0) {
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				} else {
					Intent nextIntent = new Intent(AndroidBluetooth.this, WifiCheckActivity.class);
					nextIntent.putExtra("file", destination);
					startActivity(nextIntent);
				}
			}
		});

		CheckBlueToothState();

		btnScanDevice.setOnClickListener(btnScanDeviceOnClickListener);

		registerReceiver(ActionFoundReceiver, new IntentFilter(BluetoothDevice.ACTION_FOUND));
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		unregisterReceiver(ActionFoundReceiver);
	}

	private void CheckBlueToothState() {
		if (bluetoothAdapter == null) {
			stateBluetooth.setText("Bluetooth NOT support");
		} else {
			if (bluetoothAdapter.isEnabled()) {
				if (bluetoothAdapter.isDiscovering()) {
					stateBluetooth.setText("Bluetooth is currently in device discovery process.");
				} else {
					stateBluetooth.setText("Bluetooth is Enabled.");
					btnScanDevice.setEnabled(true);
				}
			} else {
				stateBluetooth.setText("Bluetooth is NOT Enabled!");
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
	}

	private Button.OnClickListener btnScanDeviceOnClickListener = new Button.OnClickListener() {

		@Override
		public void onClick(View arg0) {
			// TODO Auto-generated method stub
			btArrayAdapter.clear();
			bluetoothAdapter.startDiscovery();
		}
	};

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		if (requestCode == REQUEST_ENABLE_BT) {
			CheckBlueToothState();
		}
	}

	private final BroadcastReceiver ActionFoundReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			String action = intent.getAction();
			if (BluetoothDevice.ACTION_FOUND.equals(action)) {
				BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
				btArrayAdapter.add(device.getName() + "\n" + device.getAddress());
				btArrayAdapter.notifyDataSetChanged();
			}
		}
	};

	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.bt_passed:
				if (cb_bluetooth_pass.isChecked()) {
					bt_status = "BLUETOOTH = PASSED";
					bt_flag = 1;

					cb_bluetooth_fail.setEnabled(false);
					cb_bluetooth_fail.setClickable(false);
					cb_bluetooth_fail.setChecked(false);

					data.clear();
					remarks(bt_status);
				} else {
					bt_flag = 0;
					cb_bluetooth_fail.setEnabled(true);
					cb_bluetooth_fail.setClickable(true);
				}
				break;
			case R.id.bt_failed:
				if (cb_bluetooth_fail.isChecked()) {
					bt_status = "BLUETOOTH = FAILED";
					bt_flag = 1;

					cb_bluetooth_pass.setEnabled(false);
					cb_bluetooth_pass.setClickable(false);
					cb_bluetooth_pass.setChecked(false);

					data.clear();
					remarks(bt_status);
				} else {
					bt_flag = 0;
					cb_bluetooth_pass.setEnabled(true);
					cb_bluetooth_pass.setClickable(true);
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