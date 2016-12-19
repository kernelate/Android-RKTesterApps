package com.example.rktesterapps;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class AndroidSensors extends Activity {

	EditText sensorLogs;
	private CheckBox cb_motion_pass, cb_motion_fail;
	private CheckBox cb_shock_pass, cb_shock_fail;
	private CheckBox cb_sensor0_pass, cb_sensor0_fail;
	private CheckBox cb_sensor1_pass, cb_sensor1_fail;
	
	private static String destination;
	private final String cmd = "CMD_APPEND_FILE";
	private static String motion_status, shock_status, sensor0_status, sensor1_status;
	private static int motion_flag, shock_flag, sensor0_flag, sensor1_flag;

	private static ArrayList<String> data = new ArrayList<String>();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sensors);
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");
		
		sensorLogs = (EditText) findViewById(R.id.editTextSensor);
		
		cb_motion_pass = (CheckBox) findViewById(R.id.motion_passed);
		cb_motion_fail = (CheckBox) findViewById(R.id.motion_failed);
		cb_sensor0_pass = (CheckBox) findViewById(R.id.sensor0_passed);
		cb_sensor0_fail = (CheckBox) findViewById(R.id.sensor0_failed);
		cb_sensor1_pass = (CheckBox) findViewById(R.id.sensor1_passed);
		cb_sensor1_fail = (CheckBox) findViewById(R.id.sensor1_failed);
		
		cb_motion_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_motion_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_sensor0_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_sensor0_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_sensor1_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_sensor1_fail.setOnCheckedChangeListener(new checkBoxListener());
		
		Button back = (Button) findViewById(R.id.backButton);
		Button home = (Button) findViewById(R.id.homeButton);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				CharSequence text = "Please leave a remarks on the checkbox provided!";
				int duration = Toast.LENGTH_SHORT;
				
				if(motion_flag ==0 || sensor0_flag == 0 || sensor1_flag == 0){
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				}
				else{
					finish();
				}
			}
		});
		home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent homeIntent = new Intent(AndroidSensors.this,
						RktesterApps.class);
				startActivity(homeIntent);

			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		final IntentFilter intentFilter = new IntentFilter();

		intentFilter.addAction("com.ntek.broadcast.motion");
		intentFilter.addAction("com.ntek.broadcast.SHOCK_SENSOR");

		intentFilter.addAction("com.ntek.broadcast.action.SENSOR_0_CHANGE");
		intentFilter.addAction("com.ntek.broadcast.action.SENSOR_1_CHANGE");

		registerReceiver(receiver, intentFilter);
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			Toast.makeText(context, "Intent Detected.", Toast.LENGTH_LONG)
					.show();
			if (action.equals("com.ntek.broadcast.motion")) {
				String motion = "motion detected!\n";
				SpannableString motionSpan = new SpannableString(motion);
				motionSpan.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
						motion.length(), 0);
				sensorLogs.append(motionSpan);
			} else if (action.equals("com.ntek.broadcast.SHOCK_SENSOR")) {

				String shock = "com.ntek.broadcast.SHOCK_SENSOR\n";
				SpannableString shockSpan = new SpannableString(shock);
				shockSpan.setSpan(new ForegroundColorSpan(Color.RED), 0,
						shock.length(), 0);

				sensorLogs.append(shockSpan);
			} else if (action
					.equals("com.ntek.broadcast.action.SENSOR_0_CHANGE")) {
				boolean sensor_0 = intent.getBooleanExtra(
						"com.ntek.broadcast.extras.EXTRA_SENSOR_0_CHANGE", false);
				System.out.print(sensor_0);
				if (sensor_0 == true) {
					sensorLogs
							.append("com.ntek.broadcast.action.SENSOR_0_CHANGE="
									+ "TRUE" + "\n");
				} else if (sensor_0 == false) {
					sensorLogs
							.append("com.ntek.broadcast.action.SENSOR_0_CHANGE="
									+ "FALSE" + "\n");
				}
				
			} else if (action
					.equals("com.ntek.broadcast.action.SENSOR_1_CHANGE")) {
				boolean sensor_1 = intent.getBooleanExtra(
						"com.ntek.broadcast.extras.EXTRA_SENSOR_1_CHANGE", false);
				System.out.println(sensor_1);
				if (sensor_1 = true) {
					sensorLogs
							.append("com.ntek.broadcast.action.SENSOR_1_CHANGE="
									+ "TRUE" + "\n");

				}

				else if (sensor_1 = false) {
					sensorLogs
							.append("com.ntek.broadcast.action.SENSOR_1_CHANGE="
									+ "FALSE" + "\n");
				}

				
			}

		}
	};
	
	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.motion_passed:
				if (cb_motion_pass.isChecked()) {
					motion_status = "MOTION = PASSED";
					motion_flag = 1;
					cb_motion_fail.setEnabled(false);
					cb_motion_fail.setClickable(false);
					cb_motion_fail.setChecked(false);
					
					data.clear();
					remarks(motion_status);
				} else {
					motion_flag = 0;
					cb_motion_fail.setEnabled(true);
					cb_motion_fail.setClickable(true);
				}
				break;
			case R.id.motion_failed:
				if (cb_motion_fail.isChecked()) {
					motion_status = "MOTION = FAILED";
					motion_flag = 1;
					cb_motion_pass.setEnabled(false);
					cb_motion_pass.setClickable(false);
					cb_motion_pass.setChecked(false);
					
					data.clear();
					remarks(motion_status);
				} else {
					motion_flag = 0;
					cb_motion_pass.setEnabled(true);
					cb_motion_pass.setClickable(true);
				}
				break;
				
			case R.id.sensor0_passed:
				if (cb_sensor0_pass.isChecked()) {
					sensor0_status = "SENSOR_0 = PASSED";
					sensor0_flag = 1;
					cb_sensor0_fail.setEnabled(false);
					cb_sensor0_fail.setClickable(false);
					cb_sensor0_fail.setChecked(false);
					
					data.clear();
					remarks(sensor0_status);
				} else {
					sensor0_flag = 0;
					cb_sensor0_fail.setEnabled(true);
					cb_sensor0_fail.setClickable(true);
				}
				break;
				
			case R.id.sensor0_failed:
				if (cb_sensor0_fail.isChecked()) {
					sensor0_status = "SENSOR_0 = FAILED";
					sensor0_flag = 1;
					cb_sensor0_pass.setEnabled(false);
					cb_sensor0_pass.setClickable(false);
					cb_sensor0_pass.setChecked(false);
					
					data.clear();
					remarks(sensor0_status);
				} else {
					sensor0_flag = 0;
					cb_sensor0_pass.setEnabled(true);
					cb_sensor0_pass.setClickable(true);
				}
				break;
				
			case R.id.sensor1_passed:
				if (cb_sensor1_pass.isChecked()) {
					sensor1_status = "SENSOR_1 = PASSED";
					sensor1_flag = 1;
					cb_sensor1_fail.setEnabled(false);
					cb_sensor1_fail.setClickable(false);
					cb_sensor1_fail.setChecked(false);
					
					data.clear();
					remarks(sensor1_status);
				} else {
					sensor1_flag = 0;
					cb_sensor1_fail.setEnabled(true);
					cb_sensor1_fail.setClickable(true);
				}
				break;
				
			case R.id.sensor1_failed:
				if (cb_sensor1_fail.isChecked()) {
					sensor1_status = "SENSOR_1 = FAILED";
					sensor1_flag = 1;
					cb_sensor1_pass.setEnabled(false);
					cb_sensor1_pass.setClickable(false);
					cb_sensor1_pass.setChecked(false);
					
					data.clear();
					remarks(sensor1_status);
				} else {
					sensor1_flag = 0;
					cb_sensor1_pass.setEnabled(true);
					cb_sensor1_pass.setClickable(true);
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
