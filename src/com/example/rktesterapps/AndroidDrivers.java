package com.example.rktesterapps;

import java.util.ArrayList;

import com.ntek.rockchip.jni.RKJNI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Toast;

public class AndroidDrivers extends Activity implements OnClickListener {

	private Button relay0;
	private Button relay1;
	private Button btnLight;
	private Button btnLock;
	private Button btnUnlock;
	private Button dtLed;
	private Button btnOnline;
	private Button sensorsButton;
	private Button btnFlash1;
	private Button btnFlash2;
	private Button btnDphone;
	private Button btnDp1;
	private Button btnDp2;

	private Button home, back, next;

	private String text;
	private Spinner spinner1;
	private String ledtext;
	private static String dev;

	private static String destination;
	private final String cmd = "CMD_APPEND_FILE";
	private String relay_0, relay_1, light, redLED, greenLED, orangeLED, flashLED1, flashLED2, dphoneLED, dpLED1, dpLED2, LED;

	private CheckBox cb_relay0_pass, cb_relay0_fail;
	private CheckBox cb_relay1_pass, cb_relay1_fail;
	private CheckBox cb_light_pass, cb_light_fail;
	private CheckBox cb_lock_pass, cb_lock_fail;
	private CheckBox cb_unlock_pass, cb_unlock_fail;
	private CheckBox cb_online_pass, cb_online_fail;
	private CheckBox cb_dt2led_pass, cb_dt2led_fail;
	private CheckBox cb_flash1_pass, cb_flash1_fail;
	private CheckBox cb_flash2_pass, cb_flash2_fail;
	private CheckBox cb_dphone_pass, cb_dphone_fail;
	private CheckBox cb_dp1_pass, cb_dp1_fail;
	private CheckBox cb_dp2_pass, cb_dp2_fail;

	private static int relay0_flag, relay1_flag, light_flag, red_flag, green_flag, orange_flag, flash1_flag, flash2_flag, dphone_flag, dp1_flag, dp2_flag, led_flag;

	private static ArrayList<String> data = new ArrayList<String>();
	
	private boolean isSensors = false;
	
	RKJNI RKJNI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_driver);
		
		RKJNI = new RKJNI();
		dev = android.os.Build.MODEL;
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");
		
		relay0 = (Button) findViewById(R.id.btnrelay0);
		relay1 = (Button) findViewById(R.id.btnrelay1);
		btnLight = (Button) findViewById(R.id.btn_light);
		btnLock = (Button) findViewById(R.id.btn_lock);
		btnOnline = (Button) findViewById(R.id.btn_online);
		btnUnlock = (Button) findViewById(R.id.btn_unlock);
		dtLed = (Button) findViewById(R.id.btndtledenable);
		spinner1 = (Spinner) findViewById(R.id.spinner1);
		sensorsButton = (Button) findViewById(R.id.btnsensor);
		btnFlash1 = (Button)findViewById(R.id.btn_flash1);
		btnFlash2 = (Button)findViewById(R.id.btn_flash2);
		btnDphone = (Button)findViewById(R.id.btn_dphone);
		btnDp1 = (Button)findViewById(R.id.btn_dp1);
		btnDp2 = (Button)findViewById(R.id.btn_dp2);
		back = (Button) findViewById(R.id.backButton);
		home = (Button) findViewById(R.id.homeButton);
		next = (Button) findViewById(R.id.nextButton);

		back.setOnClickListener(this);
		home.setOnClickListener(this);
		next.setOnClickListener(this);
		relay0.setOnClickListener(this);
		relay1.setOnClickListener(this);
		btnLight.setOnClickListener(this);
		btnLock.setOnClickListener(this);
		btnUnlock.setOnClickListener(this);
		btnOnline.setOnClickListener(this);
		btnFlash1.setOnClickListener(this);
		btnFlash2.setOnClickListener(this);
		btnDphone.setOnClickListener(this);
		btnDp1.setOnClickListener(this);
		btnDp2.setOnClickListener(this);
		dtLed.setOnClickListener(this);
		sensorsButton.setOnClickListener(this);

		// CHECKBOX
		cb_relay0_pass = (CheckBox) findViewById(R.id.relay0_passed);
		cb_relay0_fail = (CheckBox) findViewById(R.id.relay0_failed);
		cb_relay1_pass = (CheckBox) findViewById(R.id.relay1_passed);
		cb_relay1_fail = (CheckBox) findViewById(R.id.relay1_failed);
		cb_light_pass = (CheckBox) findViewById(R.id.light_passed);
		cb_light_fail = (CheckBox) findViewById(R.id.light_failed);
		cb_lock_pass = (CheckBox) findViewById(R.id.redLed_passed);
		cb_lock_fail = (CheckBox) findViewById(R.id.redLed_failed);
		cb_unlock_pass = (CheckBox) findViewById(R.id.greenLed_passed);
		cb_unlock_fail = (CheckBox) findViewById(R.id.greenLed_failed);
		cb_online_pass = (CheckBox) findViewById(R.id.orangeLed_passed);
		cb_online_fail = (CheckBox) findViewById(R.id.orangeLed_failed);
		cb_flash1_pass = (CheckBox)findViewById(R.id.flash1_passed);
		cb_flash1_fail = (CheckBox)findViewById(R.id.flash1_failed);
		cb_flash2_pass = (CheckBox)findViewById(R.id.flash2_passed);
		cb_flash2_fail = (CheckBox)findViewById(R.id.flash2_failed);
		cb_dphone_pass = (CheckBox)findViewById(R.id.dphone_passed);
		cb_dphone_fail = (CheckBox)findViewById(R.id.dphone_failed);
		cb_dp1_pass = (CheckBox)findViewById(R.id.dp1_passed);
		cb_dp1_fail = (CheckBox)findViewById(R.id.dp1_failed);
		cb_dp2_pass = (CheckBox)findViewById(R.id.dp2_passed);
		cb_dp2_fail = (CheckBox)findViewById(R.id.dp2_failed);
		cb_dt2led_pass = (CheckBox) findViewById(R.id.led_passed);
		cb_dt2led_fail = (CheckBox) findViewById(R.id.led_failed);

		cb_relay0_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_relay0_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_relay1_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_relay1_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_light_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_light_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_lock_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_lock_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_unlock_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_unlock_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_online_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_online_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_flash1_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_flash1_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_flash2_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_flash2_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_dphone_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_dphone_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_dp1_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_dp1_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_dp2_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_dp2_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_dt2led_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_dt2led_fail.setOnCheckedChangeListener(new checkBoxListener());
		
		led_init();
		checkbox_init();
		if(dev.contains("SmartEntry")){
			relay0.setEnabled(true);
			relay1.setEnabled(true);
			btnLight.setEnabled(true);
			btnLock.setEnabled(true);
			btnUnlock.setEnabled(true);
			btnOnline.setEnabled(true);
			btnFlash1.setEnabled(true);
			btnFlash2.setEnabled(true);
			
			cb_relay0_pass.setEnabled(true);
			cb_relay0_fail.setEnabled(true);
			cb_relay1_pass.setEnabled(true);
			cb_relay1_fail.setEnabled(true);
			cb_light_pass.setEnabled(true);
			cb_light_fail.setEnabled(true);
			cb_lock_pass.setEnabled(true);
			cb_lock_fail.setEnabled(true);
			cb_unlock_pass.setEnabled(true);
			cb_unlock_fail.setEnabled(true);
			cb_online_pass.setEnabled(true);
			cb_online_fail.setEnabled(true);
			cb_flash1_pass.setEnabled(true);
			cb_flash1_fail.setEnabled(true);
			cb_flash2_pass.setEnabled(true);
			cb_flash2_pass.setEnabled(true);
			
			relay0_flag = 0;
			relay1_flag = 0;
			light_flag = 0;
			red_flag = 0;
			green_flag = 0;
			orange_flag = 0;
			flash1_flag = 0;
			flash2_flag = 0;
			
		}else if(dev.contains("DoorPad")){
			btnDp1.setEnabled(true);
			btnDp2.setEnabled(true);
			
			cb_dphone_pass.setEnabled(true);
			cb_dphone_fail.setEnabled(true);
			cb_dp1_pass.setEnabled(true);
			cb_dp1_fail.setEnabled(true);
			cb_dp2_pass.setEnabled(true);
			cb_dp2_fail.setEnabled(true);
			
			dphone_flag = 0;
			dp1_flag = 0;
			dp2_flag = 0;
		}else if(dev.contains("Vivex")){
			btnDphone.setEnabled(true);
			btnDp1.setEnabled(true);
			btnDp2.setEnabled(true);
			
			cb_dphone_pass.setEnabled(true);
			cb_dphone_fail.setEnabled(true);
			cb_dp1_pass.setEnabled(true);
			cb_dp1_fail.setEnabled(true);
			cb_dp2_pass.setEnabled(true);
			cb_dp2_fail.setEnabled(true);
			
			dphone_flag = 0;
			dp1_flag = 0;
			dp2_flag = 0;
		}else if (dev.contains("DoorTalk2")){
			dtLed.setEnabled(true);
			
			cb_dt2led_pass.setEnabled(true);
			cb_dt2led_fail.setEnabled(true);
			led_flag = 0;
		}
	}

	private void checkbox_init() {
		cb_relay0_pass.setEnabled(false);
		cb_relay0_fail.setEnabled(false);
		cb_relay1_pass.setEnabled(false);
		cb_relay1_fail.setEnabled(false);
		cb_light_pass.setEnabled(false);
		cb_light_fail.setEnabled(false);
		cb_lock_pass.setEnabled(false);
		cb_lock_fail.setEnabled(false);
		cb_unlock_pass.setEnabled(false);
		cb_unlock_fail.setEnabled(false);
		cb_online_pass.setEnabled(false);
		cb_online_fail.setEnabled(false);
		cb_flash1_pass.setEnabled(false);
		cb_flash1_fail.setEnabled(false);
		cb_flash2_pass.setEnabled(false);
		cb_flash2_fail.setEnabled(false);
		cb_dphone_pass.setEnabled(false);
		cb_dphone_fail.setEnabled(false);
		cb_dp1_pass.setEnabled(false);
		cb_dp1_fail.setEnabled(false);
		cb_dp2_pass.setEnabled(false);
		cb_dp2_fail.setEnabled(false);
		cb_dt2led_pass.setEnabled(false);
		cb_dt2led_fail.setEnabled(false);
		
		relay0_flag = 1;
		relay1_flag = 1;
		light_flag = 1;
		red_flag = 1;
		green_flag = 1;
		orange_flag = 1;
		flash1_flag = 1;
		flash2_flag = 1;
		dphone_flag = 1;
		dp1_flag = 1;
		dp2_flag = 1;
		led_flag = 1;
	}

	private void led_init() {
		relay0.setEnabled(false);
		relay1.setEnabled(false);
		btnLight.setEnabled(false);
		btnLock.setEnabled(false);
		btnUnlock.setEnabled(false);
		btnOnline.setEnabled(false);
		btnFlash1.setEnabled(false);
		btnFlash2.setEnabled(false);
		btnDphone.setEnabled(false);
		btnDp1.setEnabled(false);
		btnDp2.setEnabled(false);
		dtLed.setEnabled(false);
	}

	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.relay0_passed:
				if (cb_relay0_pass.isChecked()) {
					relay_0 = "RELAY0 = PASSED";
					relay0_flag = 1;

					cb_relay0_fail.setEnabled(false);
					cb_relay0_fail.setClickable(false);
					cb_relay0_fail.setChecked(false);

					data.clear();
					remarks(relay_0);
				} else {
					relay_0 = "";
					relay0_flag = 0;

					cb_relay0_fail.setEnabled(true);
					cb_relay0_fail.setClickable(true);
				}
				break;
			case R.id.relay0_failed:
				if (cb_relay0_fail.isChecked()) {
					relay_0 = "RELAY0 = FAILED";
					relay0_flag = 1;

					cb_relay0_pass.setEnabled(false);
					cb_relay0_pass.setClickable(false);
					cb_relay0_pass.setChecked(false);

					data.clear();
					remarks(relay_0);
				} else {
					relay_0 = "";
					relay0_flag = 0;

					cb_relay0_pass.setEnabled(true);
					cb_relay0_pass.setClickable(true);
				}
				break;

			case R.id.relay1_passed:
				if (cb_relay1_pass.isChecked()) {
					relay_1 = "RELAY1 = PASSED";
					relay1_flag = 1;

					cb_relay1_fail.setEnabled(false);
					cb_relay1_fail.setClickable(false);
					cb_relay1_fail.setChecked(false);

					data.clear();
					remarks(relay_1);
				} else {
					relay_1 = "";
					relay1_flag = 0;

					cb_relay1_fail.setEnabled(true);
					cb_relay1_fail.setClickable(true);
				}
				break;
			case R.id.relay1_failed:
				if (cb_relay1_fail.isChecked()) {
					relay_1 = "RELAY1 = FAILED";
					relay1_flag = 1;

					cb_relay1_pass.setEnabled(false);
					cb_relay1_pass.setClickable(false);
					cb_relay1_pass.setChecked(false);

					data.clear();
					remarks(relay_1);
				} else {
					relay_1 = "";
					relay1_flag = 0;

					cb_relay1_pass.setEnabled(true);
					cb_relay1_pass.setClickable(true);
				}
				break;

			case R.id.light_passed:
				if (cb_light_pass.isChecked()) {
					light = "LIGHT = PASSED";
					light_flag = 1;

					cb_light_fail.setEnabled(false);
					cb_light_fail.setClickable(false);
					cb_light_fail.setChecked(false);

					data.clear();
					remarks(light);
				} else {
					light = "";
					light_flag = 0;

					cb_light_fail.setEnabled(true);
					cb_light_fail.setClickable(true);
				}
				break;

			case R.id.light_failed:
				if (cb_light_fail.isChecked()) {
					light = "LIGHT = FAILED";
					light_flag = 1;

					cb_light_pass.setEnabled(false);
					cb_light_pass.setClickable(false);
					cb_light_pass.setChecked(false);

					data.clear();
					remarks(light);
				} else {
					light = "";
					light_flag = 0;

					cb_light_pass.setEnabled(true);
					cb_light_pass.setClickable(true);
				}
				break;

			case R.id.redLed_passed:
				if (cb_lock_pass.isChecked()) {
					redLED = "RED LED = PASSED";
					red_flag = 1;

					cb_lock_fail.setEnabled(false);
					cb_lock_fail.setClickable(false);
					cb_lock_fail.setChecked(false);

					data.clear();
					remarks(redLED);
				} else {
					redLED = "";
					red_flag = 0;

					cb_lock_fail.setEnabled(true);
					cb_lock_fail.setClickable(true);
				}
				break;

			case R.id.redLed_failed:
				if (cb_lock_fail.isChecked()) {
					redLED = "RED LED = FAILED";
					red_flag = 1;

					cb_lock_pass.setEnabled(false);
					cb_lock_pass.setClickable(false);
					cb_lock_pass.setChecked(false);

					data.clear();
					remarks(redLED);
				} else {
					redLED = "";
					red_flag = 0;

					cb_lock_pass.setEnabled(true);
					cb_lock_pass.setClickable(true);
				}
				break;

			case R.id.greenLed_passed:
				if (cb_unlock_pass.isChecked()) {
					greenLED = "GREEN LED = PASSED";
					green_flag = 1;

					cb_unlock_fail.setEnabled(false);
					cb_unlock_fail.setClickable(false);
					cb_unlock_fail.setChecked(false);

					data.clear();
					remarks(greenLED);
				} else {
					greenLED = "";
					green_flag = 0;

					cb_unlock_fail.setEnabled(true);
					cb_unlock_fail.setClickable(true);
				}
				break;

			case R.id.greenLed_failed:
				if (cb_unlock_fail.isChecked()) {
					greenLED = "GREEN LED = FAILED";
					green_flag = 1;

					cb_unlock_pass.setEnabled(false);
					cb_unlock_pass.setClickable(false);
					cb_unlock_pass.setChecked(false);

					data.clear();
					remarks(greenLED);
				} else {
					greenLED = "";
					green_flag = 0;

					cb_unlock_pass.setEnabled(true);
					cb_unlock_pass.setClickable(true);
				}
				break;

			case R.id.orangeLed_passed:
				if (cb_online_pass.isChecked()) {
					orangeLED = "ORANGE LED = PASSED";
					orange_flag = 1;

					cb_online_fail.setEnabled(false);
					cb_online_fail.setClickable(false);
					cb_online_fail.setChecked(false);

					data.clear();
					remarks(orangeLED);
				} else {
					orangeLED = "";
					orange_flag = 0;

					cb_online_fail.setEnabled(true);
					cb_online_fail.setClickable(true);
				}
				break;

			case R.id.orangeLed_failed:
				if (cb_online_fail.isChecked()) {
					orangeLED = "ORANGE LED = FAILED";
					orange_flag = 1;

					cb_online_pass.setEnabled(false);
					cb_online_pass.setClickable(false);
					cb_online_pass.setChecked(false);

					data.clear();
					remarks(orangeLED);
				} else {
					orangeLED = "";
					orange_flag = 0;

					cb_online_pass.setEnabled(true);
					cb_online_pass.setClickable(true);
				}
				break;
				
			case R.id.flash1_passed:
				if (cb_flash1_pass.isChecked()) {
					flashLED1 = "FLASH1 LED = PASSED";
					flash1_flag = 1;

					cb_flash1_fail.setEnabled(false);
					cb_flash1_fail.setClickable(false);
					cb_flash1_fail.setChecked(false);

					data.clear();
					remarks(flashLED1);
				} else {
					flashLED1 = "";
					flash1_flag = 0;

					cb_flash1_fail.setEnabled(true);
					cb_flash1_fail.setClickable(true);
				}
				break;

			case R.id.flash1_failed:
				if (cb_flash1_fail.isChecked()) {
					flashLED1 = "FLASH1 LED = FAILED";
					flash1_flag = 1;

					cb_flash1_pass.setEnabled(false);
					cb_flash1_pass.setClickable(false);
					cb_flash1_pass.setChecked(false);

					data.clear();
					remarks(flashLED1);
				} else {
					flashLED1 = "";
					flash1_flag = 0;

					cb_flash1_pass.setEnabled(true);
					cb_flash1_pass.setClickable(true);
				}
				break;
				
			case R.id.flash2_passed:
				if (cb_flash2_pass.isChecked()) {
					flashLED2 = "FLASH2 LED = PASSED";
					flash2_flag = 1;

					cb_flash2_fail.setEnabled(false);
					cb_flash2_fail.setClickable(false);
					cb_flash2_fail.setChecked(false);

					data.clear();
					remarks(flashLED2);
				} else {
					flashLED2 = "";
					flash2_flag = 0;

					cb_flash2_fail.setEnabled(true);
					cb_flash2_fail.setClickable(true);
				}
				break;

			case R.id.flash2_failed:
				if (cb_flash2_fail.isChecked()) {
					flashLED2 = "FLASH2 LED = FAILED";
					flash2_flag = 1;

					cb_flash2_pass.setEnabled(false);
					cb_flash2_pass.setClickable(false);
					cb_flash2_pass.setChecked(false);

					data.clear();
					remarks(flashLED2);
				} else {
					flashLED2 = "";
					flash2_flag = 0;

					cb_flash2_pass.setEnabled(true);
					cb_flash2_pass.setClickable(true);
				}
				break;
				
			case R.id.dphone_passed:
				if (cb_dphone_pass.isChecked()) {
					dphoneLED = "SPEAKER LED = PASSED";
					dphone_flag = 1;

					cb_dphone_fail.setEnabled(false);
					cb_dphone_fail.setClickable(false);
					cb_dphone_fail.setChecked(false);

					data.clear();
					remarks(dphoneLED);
				} else {
					dphoneLED = "";
					dphone_flag = 0;

					cb_dphone_fail.setEnabled(true);
					cb_dphone_fail.setClickable(true);
				}
				break;

			case R.id.dphone_failed:
				if (cb_dphone_fail.isChecked()) {
					dphoneLED = "SPEAKER LED = FAILED";
					dphone_flag = 1;

					cb_dphone_pass.setEnabled(false);
					cb_dphone_pass.setClickable(false);
					cb_dphone_pass.setChecked(false);

					data.clear();
					remarks(dphoneLED);
				} else {
					dphoneLED = "";
					dphone_flag = 0;

					cb_dphone_pass.setEnabled(true);
					cb_dphone_pass.setClickable(true);
				}
				break;
				
			case R.id.dp1_passed:
				if (cb_dp1_pass.isChecked()) {
					dpLED1 = "LED1 = PASSED";
					dp1_flag = 1;

					cb_dp1_fail.setEnabled(false);
					cb_dp1_fail.setClickable(false);
					cb_dp1_fail.setChecked(false);

					data.clear();
					remarks(dpLED1);
				} else {
					dpLED1 = "";
					dp1_flag = 0;

					cb_dp1_fail.setEnabled(true);
					cb_dp1_fail.setClickable(true);
				}
				break;

			case R.id.dp1_failed:
				if (cb_dp1_fail.isChecked()) {
					dpLED1 = "LED1 = FAILED";
					dp1_flag = 1;

					cb_dp1_pass.setEnabled(false);
					cb_dp1_pass.setClickable(false);
					cb_dp1_pass.setChecked(false);

					data.clear();
					remarks(dpLED1);
				} else {
					dpLED1 = "";
					dp1_flag = 0;

					cb_dp1_pass.setEnabled(true);
					cb_dp1_pass.setClickable(true);
				}
				break;
				
			case R.id.dp2_passed:
				if (cb_dp2_pass.isChecked()) {
					dpLED2 = "LED2 = PASSED";
					dp2_flag = 1;

					cb_dp2_fail.setEnabled(false);
					cb_dp2_fail.setClickable(false);
					cb_dp2_fail.setChecked(false);

					data.clear();
					remarks(dpLED2);
				} else {
					dpLED2 = "";
					dp2_flag = 0;

					cb_dp2_fail.setEnabled(true);
					cb_dp2_fail.setClickable(true);
				}
				break;

			case R.id.dp2_failed:
				if (cb_dp2_fail.isChecked()) {
					dpLED2 = "LED2 = FAILED";
					dp2_flag = 1;

					cb_dp2_pass.setEnabled(false);
					cb_dp2_pass.setClickable(false);
					cb_dp2_pass.setChecked(false);

					data.clear();
					remarks(dpLED2);
				} else {
					dpLED2 = "";
					dp2_flag = 0;

					cb_dp2_pass.setEnabled(true);
					cb_dp2_pass.setClickable(true);
				}
				break;

			case R.id.led_passed:
				if (cb_dt2led_pass.isChecked()) {
					LED = "LED = PASSED";
					led_flag = 1;

					cb_dt2led_fail.setEnabled(false);
					cb_dt2led_fail.setClickable(false);
					cb_dt2led_fail.setChecked(false);

					data.clear();
					remarks(LED);
				} else {
					LED = "";
					led_flag = 0;

					cb_dt2led_fail.setEnabled(true);
					cb_dt2led_fail.setClickable(true);
				}
				break;

			case R.id.led_failed:
				if (cb_dt2led_fail.isChecked()) {
					LED = "LED = FAILED";
					led_flag = 1;

					cb_dt2led_pass.setEnabled(false);
					cb_dt2led_pass.setClickable(false);
					cb_dt2led_pass.setChecked(false);

					data.clear();
					remarks(LED);
				} else {
					LED = "";
					led_flag = 0;

					cb_dt2led_pass.setEnabled(true);
					cb_dt2led_pass.setClickable(true);
				}
				break;

			default:
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

	@Override
	public void onClick(View v) {
		Context context = getApplicationContext();
		CharSequence text;
		int duration;
		
		switch (v.getId()) {
		case R.id.homeButton:
			Intent homeIntent = new Intent(AndroidDrivers.this, RktesterApps.class);
			startActivity(homeIntent);
			break;

		case R.id.backButton:
			finish();
			break;

		case R.id.nextButton:
			text = "Please leave a remarks on the checkbox provided!";
			duration = Toast.LENGTH_SHORT;

			if ((relay0_flag == 0) || (relay1_flag == 0) || (light_flag == 0) || (red_flag == 0) || (green_flag == 0)
					|| (orange_flag == 0) || (led_flag == 0) || !isSensors) {
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			} else {
				Intent nextIntent = new Intent(AndroidDrivers.this, AndroidLanConnection.class);
				nextIntent.putExtra("file", destination);
				startActivity(nextIntent);
			}
			break;

		case R.id.btn_light:
			text = btnLight.getText().toString();
			if (text.equals("OFF")) {
//				McuJni McuJni = new McuJni();
//				McuJni.lightEnable(1);
				RKJNI.SEledlight(1);
				btnLight.setBackgroundResource(R.drawable.btn_green);
				btnLight.setText("ON");
				Log.d("RKTESTER.light", "ON");
			} else if (text.equals("ON")) {
//				McuJni McuJni = new McuJni();
//				McuJni.lightEnable(0);
				RKJNI.SEledlight(0);
				btnLight.setBackgroundResource(R.drawable.btn_red);
				btnLight.setText("OFF");
				Log.d("RKTESTER.Light", "OFF");
			} else {

			}
			break;
			
		case R.id.btn_lock:
			text = btnLock.getText().toString();
			if (text.equals("OFF")) {
//				McuJni McuJni = new McuJni();
//				McuJni.redLedEnable(1);
				RKJNI.SEledlock(1);
				btnLock.setBackgroundResource(R.drawable.btn_green);
				btnLock.setText("ON");
				Log.d("RKTESTER.Lock ", "Lock ON");
			} else if (text.equals("ON")) {
//				McuJni McuJni = new McuJni();
//				McuJni.redLedEnable(0);
				RKJNI.SEledlock(0);
				btnLock.setBackgroundResource(R.drawable.btn_red);
				btnLock.setText("OFF");
				Log.d("RKTESTER.Lock", "Lock OFF");
			} else {

			}
			break;
			
		case R.id.btn_unlock:
			text = btnUnlock.getText().toString();
			if (text.equals("OFF")) {
//				McuJni McuJni = new McuJni();
//				McuJni.greenLedEnable(1);
				RKJNI.SEledunlock(1);
				btnUnlock.setBackgroundResource(R.drawable.btn_green);
				btnUnlock.setText("ON");
				Log.d("RKTESTER.Unlock", "Unlock ON");
			} else if (text.equals("ON")) {
//				McuJni McuJni = new McuJni();
//				McuJni.greenLedEnable(0);
				RKJNI.SEledunlock(0);
				btnUnlock.setBackgroundResource(R.drawable.btn_red);
				btnUnlock.setText("OFF");
				Log.d("RKTESTER.Unlock", "Unlock OFF");
			} else {

			}
			break;
			
		case R.id.btn_online:
			text = btnOnline.getText().toString();
			if (text.equals("OFF")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(1);
				RKJNI.SEledonline(1);
				btnOnline.setBackgroundResource(R.drawable.btn_green);
				btnOnline.setText("ON");
				Log.d("RKTESTER.Online", "Online ON");

			} else if (text.equals("ON")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(0);
				RKJNI.SEledonline(0);
				btnOnline.setBackgroundResource(R.drawable.btn_red);
				btnOnline.setText("OFF");
				Log.d("RKTESTER.Online Enable", "Online OFF");
			} else {

			}
			break;
			
		case R.id.btn_flash1:
			text = btnFlash1.getText().toString();
			if (text.equals("OFF")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(1);
				RKJNI.SEledflash1(1);
				btnFlash1.setBackgroundResource(R.drawable.btn_green);
				btnFlash1.setText("ON");
				Log.d("RKTESTER.FLASH1", "FLASH1 LED -ON");

			} else if (text.equals("ON")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(0);
				RKJNI.SEledflash1(0);
				btnFlash1.setBackgroundResource(R.drawable.btn_red);
				btnFlash1.setText("OFF");
				Log.d("RKTESTER.FLASH1 ", "FLASH1 LED OFF");
			} else {

			}
			break;
			
		case R.id.btn_flash2:
			text = btnFlash2.getText().toString();
			if (text.equals("OFF")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(1);
				RKJNI.SEledflash2(1);
				btnFlash2.setBackgroundResource(R.drawable.btn_green);
				btnFlash2.setText("ON");
				Log.d("RKTESTER.FLASH2 ", "FLASH2 LED ON");

			} else if (text.equals("ON")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(0);
				RKJNI.SEledflash2(0);
				btnFlash2.setBackgroundResource(R.drawable.btn_red);
				btnFlash2.setText("OFF");
				Log.d("RKTESTER.FLASH2", "FLASH2 LED OFF");
			} else {

			}
			break;
			
		case R.id.btn_dphone:
			
			text = btnDphone.getText().toString();
			if (text.equals("OFF")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(1);
				RKJNI.DPledspeaker(1);
				btnDphone.setBackgroundResource(R.drawable.btn_green);
				btnDphone.setText("ON");
				Log.d("RKTESTER.Speaker", "Speaker LED ON");

			} else if (text.equals("ON")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(0);
				RKJNI.DPledspeaker(0);
				btnDphone.setBackgroundResource(R.drawable.btn_red);
				btnDphone.setText("OFF");
				Log.d("RKTESTER.Speaker", "Speaker LED OFF");
			} else {

			}
			break;
			
		case R.id.btn_dp1:
			text = btnDp1.getText().toString();
			if (text.equals("OFF")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(1);
				RKJNI.DPled1(1);
				btnDp1.setBackgroundResource(R.drawable.btn_green);
				btnDp1.setText("ON");
				Log.d("RKTESTER.DP1", "DP1 LED ON");

			} else if (text.equals("ON")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(0);
				RKJNI.DPled1(0);
				btnDp1.setBackgroundResource(R.drawable.btn_red);
				btnDp1.setText("OFF");
				Log.d("RKTESTER.DP1", "DP1 LED OFF");
			} else {

			}
			break;
			
		case R.id.btn_dp2:
			text = btnDp2.getText().toString();
			if (text.equals("OFF")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(1);
				RKJNI.DPled2(1);
				btnDp2.setBackgroundResource(R.drawable.btn_green);
				btnDp2.setText("ON");
				Log.d("RKTESTER.DP2", "DP2 LED ON");;

			} else if (text.equals("ON")) {
//				OlLedJNI OlLedJNI = new OlLedJNI();
//				OlLedJNI.OnlineLedOn(0);
				RKJNI.DPled2(0);
				btnDp2.setBackgroundResource(R.drawable.btn_red);
				btnDp2.setText("OFF");
				Log.d("RKTESTER.DP2", "DP1 LED ON");
			} else {

			}
			break;
			
		case R.id.btnrelay0:
			text = relay0.getText().toString();
			if (text.equals("OFF")) {
//				McuJni McuJni = new McuJni();
//				McuJni.relay0Enable(1);
				RKJNI.relay0(1);
				System.out.println("Relay0 line 170");
				relay0.setBackgroundResource(R.drawable.btn_green);
				relay0.setText("ON");
				Log.d("RKTESTER.relay-0", "Relay 0 - On");

			} else if (text.equals("ON")) {
//				McuJni McuJni = new McuJni();
//				McuJni.relay0Enable(0);
				RKJNI.relay0(0);
				System.out.println("Relay0 line 178");
				relay0.setBackgroundResource(R.drawable.btn_red);
				relay0.setText("OFF");
				Log.d("RKTESTER.relay-0", "Relay 0 - off");
			} else {

			}
			break;
			
		case R.id.btnrelay1:
			text = relay1.getText().toString();
			if (text.equals("OFF")) {
//				McuJni McuJni = new McuJni();
//				McuJni.relay1Enable(1);
				RKJNI.relay1(1);
				relay1.setBackgroundResource(R.drawable.btn_green);
				relay1.setText("ON");
				Log.d("RKTESTER.relay-1", "Relay 1 - On");

			} else if (text.equals("ON")) {
//				McuJni McuJni = new McuJni();
//				McuJni.relay1Enable(0);
				RKJNI.relay1(0);
				relay1.setBackgroundResource(R.drawable.btn_red);
				relay1.setText("OFF");
				Log.d("RKTESTER.relay-1", "Relay 1 - Off");
			} else {

			}
			break;
			
		case R.id.btndtledenable:
			ledtext = String.valueOf(spinner1.getSelectedItem());
			text = "DoortalkLED Set To:" + ledtext;
			duration = Toast.LENGTH_SHORT;

			if (ledtext.equals("Off")) {
				RKJNI.DT2led("OFF");
			} else if (ledtext.equals("Full On")) {
				RKJNI.DT2led("ON");
			} else if (ledtext.equals("5sec On")) {
				RKJNI.DT2led("ON_DELAY");
			} else if (ledtext.equals("Slow Blink")) {
				RKJNI.DT2led("SLOW");
			} else if (ledtext.equals("Fast Blink")) {
				RKJNI.DT2led("FAST");
			} else if (ledtext.equals("Blink Once")) {
				RKJNI.DT2led("ONCE");
			} else if (ledtext.equals("Blink Twice")) {
				RKJNI.DT2led("TWICE");
			} else if (ledtext.equals("Recovery")) {
				RKJNI.DT2led("RECOVERY");
			}
			Toast toast = Toast.makeText(context, text, duration);
			toast.show();
			Log.d("RKTESTER.dtLed", "LED DRiver");
			break;
			
		case R.id.btnsensor:
			isSensors = true;
			Intent sensorIntent = new Intent(AndroidDrivers.this, AndroidSensors.class);
			startActivity(sensorIntent);
			break;

		default:
			break;
		}
	}

}
