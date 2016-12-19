package com.example.rktesterapps;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

public class AndroidScreen extends Activity implements android.view.View.OnClickListener{
	private static ArrayList<String> data = new ArrayList<String>();
	private final String cmd = "CMD_APPEND_FILE";
	private static String destination;
	private static String touch_status, lcd_status;
	
	private static int touch_flag,lcd_flag;
	
	private CheckBox cb_touch_pass, cb_touch_fail;
	private CheckBox cb_lcd_pass, cb_lcd_fail;
	
	private Button home, back, lcd;
	private Button btn_touchscreen, btn_lcd;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_androidscreen);
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");
		
		back = (Button) findViewById(R.id.backButton);
		home = (Button) findViewById(R.id.homeButton);
		lcd = (Button) findViewById(R.id.nextButton);
		btn_lcd = (Button)findViewById(R.id.lcdscreen);
		btn_touchscreen = (Button)findViewById(R.id.touchscreen);
		
		back.setOnClickListener(this);
		home.setOnClickListener(this);
		lcd.setOnClickListener(this);
		btn_lcd.setOnClickListener(this);
		btn_touchscreen.setOnClickListener(this);
		
		cb_touch_pass = (CheckBox) findViewById(R.id.touch_passed);
		cb_touch_fail = (CheckBox) findViewById(R.id.touch_failed);
		cb_lcd_pass = (CheckBox) findViewById(R.id.lcd_passed);
		cb_lcd_fail = (CheckBox) findViewById(R.id.lcd_failed);

		cb_lcd_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_lcd_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_touch_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_touch_fail.setOnCheckedChangeListener(new checkBoxListener());
		
	}
	
	@Override
	public void onClick(View v) {
		Context context = getApplicationContext();
		CharSequence text;
		int duration;
		
		switch (v.getId()) {
		case R.id.homeButton:
			Intent homeIntent = new Intent(AndroidScreen.this, RktesterApps.class);
			startActivity(homeIntent);
			break;

		case R.id.backButton:
			finish();
			break;

		case R.id.nextButton:
			text = "Please leave a remarks on the checkbox provided!";
			duration = Toast.LENGTH_SHORT;
			if (touch_flag == 0 || lcd_flag == 0) {
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			} else {
				Intent BluetoothIntent = new Intent(AndroidScreen.this, AndroidBluetooth.class);
				BluetoothIntent.putExtra("file", destination);
				startActivity(BluetoothIntent);
			}
			break;
			
		case R.id.touchscreen:
			Intent TouchIntent = new Intent(AndroidScreen.this, AndroidTouchscreen.class);
			startActivity(TouchIntent);
			break;
			
		case R.id.lcdscreen:
			Intent LCDIntent = new Intent(AndroidScreen.this, AndroidLCD.class);
			startActivity(LCDIntent);
			break;
		}
	}
	
	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.touch_passed:
				if (cb_touch_pass.isChecked()) {
					touch_status = "TOUCHSCREEN = PASSED";
					touch_flag = 1;
					
					cb_touch_fail.setEnabled(false);
					cb_touch_fail.setClickable(false);
					cb_touch_fail.setChecked(false);
					
					data.clear();
					remarks(touch_status);
				} else {
					touch_flag = 0;
					cb_touch_fail.setEnabled(true);
					cb_touch_fail.setClickable(true);
				}
				break;
			case R.id.touch_failed:
				if (cb_touch_fail.isChecked()) {
					touch_status = "TOUCHSCREEN = FAILED";
					touch_flag = 1;
					cb_touch_pass.setEnabled(false);
					cb_touch_pass.setClickable(false);
					cb_touch_pass.setChecked(false);
					
					data.clear();
					remarks(touch_status);
				} else {
					touch_flag = 0;
					cb_touch_pass.setEnabled(true);
					cb_touch_pass.setClickable(true);
				}
				break;
				
			case R.id.lcd_passed:
				if (cb_lcd_pass.isChecked()) {
					lcd_status = "LCD = PASSED";
					lcd_flag = 1;

					cb_lcd_fail.setEnabled(false);
					cb_lcd_fail.setClickable(false);
					cb_lcd_fail.setChecked(false);

					data.clear();
					remarks(lcd_status);
				} else {
					lcd_flag = 0;
					cb_lcd_fail.setEnabled(true);
					cb_lcd_fail.setClickable(true);
				}
				break;
			case R.id.lcd_failed:
				if (cb_lcd_fail.isChecked()) {
					lcd_status = "LCD = FAILED";
					lcd_flag = 1;
					cb_lcd_pass.setEnabled(false);
					cb_lcd_pass.setClickable(false);
					cb_lcd_pass.setChecked(false);

					data.clear();
					remarks(lcd_status);
				} else {
					lcd_flag = 0;
					cb_lcd_pass.setEnabled(true);
					cb_lcd_pass.setClickable(true);
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
