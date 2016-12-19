package com.example.rktesterapps;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidUsb extends Activity {

	Button start;
	TextView display;

	private CheckBox cb_usb_pass, cb_usb_fail;

	private static String destination;
	private final String cmd = "CMD_APPEND_FILE";

	private static String usb_status;
	private static int usb_flag;

	private static ArrayList<String> data = new ArrayList<String>();

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		this.unregisterReceiver(MountServiceReceiver);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_usb);
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");

		start = (Button) findViewById(R.id.scandevice);
		display = (TextView) findViewById(R.id.statusTxt);

		cb_usb_pass = (CheckBox) findViewById(R.id.usb_passed);
		cb_usb_fail = (CheckBox) findViewById(R.id.usb_failed);

		cb_usb_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_usb_fail.setOnCheckedChangeListener(new checkBoxListener());

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

				Intent homeIntent = new Intent(AndroidUsb.this, RktesterApps.class);
				startActivity(homeIntent);

			}
		});
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				CharSequence text = "Please leave a remarks on the checkbox provided!";
				int duration = Toast.LENGTH_SHORT;

				if (usb_flag == 0) {
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				} else {
					Intent nextIntent = new Intent(AndroidUsb.this, AndroidScreen.class);
					nextIntent.putExtra("file", destination);
					startActivity(nextIntent);
				}
			}
		});

		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
		filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_REMOVED);
		filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
		filter.addAction(Intent.ACTION_MEDIA_EJECT);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
		filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);

		this.registerReceiver(this.MountServiceReceiver, filter);
	}

	public BroadcastReceiver MountServiceReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(Intent.ACTION_MEDIA_MOUNTED)) {
				Toast.makeText(context, "SD Card mounted", Toast.LENGTH_LONG).show();
				display.setText("USB Mounted");
				display.setTextColor(Color.GREEN);

			} else if (action.equals(Intent.ACTION_MEDIA_UNMOUNTED)) {
				Toast.makeText(context, "SD Card unmounted", Toast.LENGTH_LONG).show();
				display.setText("USB Unmounted");
				display.setTextColor(Color.RED);

			} else if (action.equals(Intent.ACTION_MEDIA_SCANNER_STARTED)) {
				Toast.makeText(context, "SD Card scanner started", Toast.LENGTH_LONG).show();
			} else if (action.equals(Intent.ACTION_MEDIA_SCANNER_FINISHED)) {
				Toast.makeText(context, "SD Card scanner finished", Toast.LENGTH_LONG).show();
			} else if (action.equals(Intent.ACTION_MEDIA_EJECT)) {
				Toast.makeText(context, "SD Card eject", Toast.LENGTH_LONG).show();
				display.setText("USB Ejected");
				display.setTextColor(Color.RED);
			} else if (action.equals(Intent.ACTION_UMS_CONNECTED)) {
				Toast.makeText(context, "connected", Toast.LENGTH_LONG).show();
			} else if (action.equals(Intent.ACTION_UMS_DISCONNECTED)) {
				Toast.makeText(context, "disconnected", Toast.LENGTH_LONG).show();
			} else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
				Toast.makeText(context, "usb connected", Toast.LENGTH_LONG).show();
				display.setText("USB Connected");
				display.setTextColor(Color.GREEN);

			} else if (action.equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
				Toast.makeText(context, "usb disconnected", Toast.LENGTH_LONG).show();
				display.setText("USB Disconnected");
				display.setTextColor(Color.RED);

			} else if (action.equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
				Toast.makeText(context, "usb connected", Toast.LENGTH_LONG).show();
				display.setText("USB Connected");
				display.setTextColor(Color.GREEN);

			}
		}
	};

	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.usb_passed:
				if (cb_usb_pass.isChecked()) {
					usb_status = "USB = PASSED";
					usb_flag = 1;

					cb_usb_fail.setEnabled(false);
					cb_usb_fail.setClickable(false);
					cb_usb_fail.setChecked(false);

					data.clear();
					remarks(usb_status);
				} else {
					usb_flag = 0;
					cb_usb_fail.setEnabled(true);
					cb_usb_fail.setClickable(true);
				}
				break;
			case R.id.usb_failed:
				if (cb_usb_fail.isChecked()) {
					usb_status = "USB = FAILED";
					usb_flag = 1;

					cb_usb_pass.setEnabled(false);
					cb_usb_pass.setClickable(false);
					cb_usb_pass.setChecked(false);

					data.clear();
					remarks(usb_status);
				} else {
					usb_flag = 0;
					cb_usb_pass.setEnabled(true);
					cb_usb_pass.setClickable(true);
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
