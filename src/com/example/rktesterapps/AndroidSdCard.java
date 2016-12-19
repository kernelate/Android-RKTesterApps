package com.example.rktesterapps;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

import com.choiboi.imagecroppingexample.CropActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidSdCard extends Activity {
	private TextView sdstatus;
	private Button check;
	private Context myContext;

	private CheckBox cb_sdcard_pass, cb_sdcard_fail;

	private static String destination ;
	private final String cmd = "CMD_APPEND_FILE";
	private static String sdcard_status;
	private static int sdcard_flag;

	private static ArrayList<String> data = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sdcard);
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");

		sdstatus = (TextView) findViewById(R.id.statusTxt);
		check = (Button) findViewById(R.id.scandevice);

		cb_sdcard_pass = (CheckBox) findViewById(R.id.sdcard_passed);
		cb_sdcard_fail = (CheckBox) findViewById(R.id.sdcard_failed);

		cb_sdcard_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_sdcard_fail.setOnCheckedChangeListener(new checkBoxListener());

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

				Intent homeIntent = new Intent(AndroidSdCard.this, RktesterApps.class);
				startActivity(homeIntent);

			}
		});
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				CharSequence text = "Please leave a remarks on the checkbox provided!";
				int duration = Toast.LENGTH_SHORT;

				if (sdcard_flag == 0) {
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				} else {
					Intent nextIntent = new Intent(AndroidSdCard.this, CropActivity.class);
					nextIntent.putExtra("file", destination);
					startActivity(nextIntent);
				}
			}
		});

		check.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				try {
					File myFile = new File("mnt/external_sd/mysdfile.txt");
					myFile.createNewFile();
					FileOutputStream fOut = new FileOutputStream(myFile);
					OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
					myOutWriter.append("YOLO");
					myOutWriter.close();
					fOut.close();
					Toast.makeText(getBaseContext(), "Done writing SD 'mysdfile.txt'", Toast.LENGTH_SHORT).show();
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
				}

				try {
					File myFile = new File("mnt/external_sd/mysdfile.txt");
					myFile.createNewFile();
					FileInputStream fIn = new FileInputStream(myFile);
					BufferedReader myReader = new BufferedReader(new InputStreamReader(fIn));
					String aDataRow = "";
					String aBuffer = "";
					while ((aDataRow = myReader.readLine()) != null) {
						aBuffer += aDataRow + "\n";
					}

					myReader.close();
					Toast.makeText(getBaseContext(), "SDCard Does Exist", Toast.LENGTH_SHORT).show();
					sdstatus.setText("SDCard Exist");
					sdstatus.setTextColor(Color.GREEN);
				} catch (Exception e) {
					Toast.makeText(getBaseContext(), "SDCard Does Not Exist", Toast.LENGTH_SHORT).show();
					sdstatus.setText("SDCard NotExist");
					sdstatus.setTextColor(Color.RED);
				}
			}// onClick
				// btnReadSDFile

		});
	}

	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.sdcard_passed:
				if (cb_sdcard_pass.isChecked()) {
					sdcard_status = "SDCARD = PASSED";
					sdcard_flag = 1;

					cb_sdcard_fail.setEnabled(false);
					cb_sdcard_fail.setClickable(false);
					cb_sdcard_fail.setChecked(false);

					data.clear();
					remarks(sdcard_status);
				} else {
					sdcard_flag = 0;
					cb_sdcard_fail.setEnabled(true);
					cb_sdcard_fail.setClickable(true);
				}
				break;
			case R.id.sdcard_failed:
				if (cb_sdcard_fail.isChecked()) {
					sdcard_status = "SDCARD = FAILED";
					sdcard_flag = 1;

					cb_sdcard_pass.setEnabled(false);
					cb_sdcard_pass.setClickable(false);
					cb_sdcard_pass.setChecked(false);

					data.clear();
					remarks(sdcard_status);
				} else {
					sdcard_flag = 0;
					cb_sdcard_pass.setEnabled(true);
					cb_sdcard_pass.setClickable(true);
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
