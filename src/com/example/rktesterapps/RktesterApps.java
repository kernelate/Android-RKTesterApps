package com.example.rktesterapps;

import java.io.File;
import java.util.ArrayList;

import com.choiboi.imagecroppingexample.CropActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;

public class RktesterApps extends Activity {

	private Button driver;
	private Button lan;
	private Button camera;
	private Button speaker;
	private Button wifi;
	private Button bluetooth;
	private Button sdcard;
	private Button usb;
	private Button lcd;
	
	private static String content = "RK CHECKLIST REPORT";
	private String destination;
	private static String serialno;
	private final String cmd = "CMD_CREATE_FILE";
	
	private File reportFile;
	
	ArrayList<String> data = new ArrayList<String>();
	
	TelephonyManager tManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_rktester_apps);

		serialno = android.os.Build.SERIAL;
		
		driver = (Button) findViewById(R.id.driverButton);
		lan = (Button) findViewById(R.id.lanButton);
		camera = (Button) findViewById(R.id.cameraButton);
		speaker = (Button) findViewById(R.id.speakerButton);
		wifi = (Button) findViewById(R.id.wifiButton);
		bluetooth = (Button) findViewById(R.id.bluetoothButton);
		sdcard = (Button) findViewById(R.id.sdcardButton);
		usb = (Button) findViewById(R.id.usbButton);
		lcd = (Button) findViewById(R.id.lcd_touch);
		
		
		destination = Environment.getExternalStorageDirectory().getPath()+"/report_"+serialno+".txt";
		System.out.println(destination);
		reportFile = new File(destination);
		
		for(int x = 0;x<999;x++){
			if(reportFile.exists()){
				destination = Environment.getExternalStorageDirectory().getPath()+"/report_"+serialno+"_"+x+".txt";
				reportFile = new File(destination);
			}else{
				break;
			}
		}
			
		data.add(content);
		data.add(destination);
		data.add(cmd);		
		Report reporttask = new Report();
		reporttask.execute(data);
		
		driver.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent driverIntent = new Intent(RktesterApps.this,
						AndroidDrivers.class);
				driverIntent.putExtra("file", destination);
				startActivity(driverIntent);
			}
		});
		
		lcd.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent infoIntent = new Intent(RktesterApps.this,
						AndroidScreen.class);
				infoIntent.putExtra("file", destination);
				startActivity(infoIntent);

			}
		});

		lan.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent lanIntent = new Intent(RktesterApps.this,
						AndroidLanConnection.class);
				lanIntent.putExtra("file", destination);
				startActivity(lanIntent);

			}
		});

		usb.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent usbIntent = new Intent(RktesterApps.this,
						AndroidUsb.class);
				usbIntent.putExtra("file", destination);
				startActivity(usbIntent);

			}
		});

		speaker.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent speekerIntent = new Intent(RktesterApps.this,
						AndroidMicSpeeker.class);
				startActivity(speekerIntent);
			}
		});

		sdcard.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent sdcIntent = new Intent(RktesterApps.this,
						AndroidSdCard.class);
				sdcIntent.putExtra("file", destination);
				startActivity(sdcIntent);
			}
		});
		wifi.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent wifiIntent = new Intent(RktesterApps.this,
						WifiCheckActivity.class);
				wifiIntent.putExtra("file", destination);
				startActivity(wifiIntent);
			}
		});
		bluetooth.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent btIntent = new Intent(RktesterApps.this,
						AndroidBluetooth.class);
				btIntent.putExtra("file", destination);
				startActivity(btIntent);
			}
		});
		camera.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent cameraIntent = new Intent(RktesterApps.this,
						CropActivity.class);
				cameraIntent.putExtra("file", destination);
				startActivity(cameraIntent);

			}
		});

	}

}
