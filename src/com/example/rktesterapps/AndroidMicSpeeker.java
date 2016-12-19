package com.example.rktesterapps;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.ntek.rockchip.jni.RKJNI;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class AndroidMicSpeeker extends Activity implements android.view.View.OnClickListener {
	private Button play;
	private Button stop;
	private Button record;
	private Button playM;
	private Button stopM;
	private Button soudntools;

	private TextView display, tvspeaker, tvhandset;
	private MediaRecorder myAudioRecorder;
	private String outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp";
	private File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording.3gp");
	private static String dev;

	private CheckBox cb_speaker_pass, cb_speaker_fail;
	private CheckBox cb_mic_pass, cb_mic_fail;
	private CheckBox cb_speakermode_pass, cb_speakermode_fail;
	private CheckBox cb_handset_pass, cb_handset_fail;

	private Switch sw_speaker;

	private final String destination = "/data/report.txt";
	private final String cmd = "CMD_APPEND_FILE";

	private static String speaker_status, mic_status;
	private static String speakermode_status, handset_status;

	private static int speaker_flag, mic_flag, speakermode_flag, handset_flag;

	private static ArrayList<String> data = new ArrayList<String>();

	private Uri ringtoneUri;
	private Ringtone ringtoneSound;
	MediaPlayer m;

	RKJNI RKJNI;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mic_speaker);

		RKJNI = new RKJNI();
		dev = android.os.Build.MODEL;
		ringtoneUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		ringtoneSound = RingtoneManager.getRingtone(getApplicationContext(), ringtoneUri);

		tvhandset = (TextView)findViewById(R.id.hm);
		tvspeaker = (TextView)findViewById(R.id.sm);

		Button back = (Button) findViewById(R.id.backButton);
		Button home = (Button) findViewById(R.id.homeButton);
		Button next = (Button) findViewById(R.id.nextButton);

		play = (Button) findViewById(R.id.button3);
		stop = (Button) findViewById(R.id.button2);
		record = (Button) findViewById(R.id.button);
		playM = (Button) findViewById(R.id.button4);
		stopM = (Button) findViewById(R.id.button5);
		display = (TextView) findViewById(R.id.status);
		soudntools = (Button) findViewById(R.id.soundtools);

		play.setOnClickListener(this);
		stop.setOnClickListener(this);
		record.setOnClickListener(this);
		playM.setOnClickListener(this);
		stopM.setOnClickListener(this);
		soudntools.setOnClickListener(this);

		back.setOnClickListener(this);
		home.setOnClickListener(this);
		next.setOnClickListener(this);

		sw_speaker = (Switch) findViewById(R.id.speakermode);

		cb_speaker_pass = (CheckBox) findViewById(R.id.speaker_passed);
		cb_speaker_fail = (CheckBox) findViewById(R.id.speaker_failed);
		cb_mic_pass = (CheckBox) findViewById(R.id.mic_passed);
		cb_mic_fail = (CheckBox) findViewById(R.id.mic_failed);
		cb_speakermode_pass = (CheckBox) findViewById(R.id.speakermode_passed);
		cb_speakermode_fail = (CheckBox) findViewById(R.id.speakermode_failed);
		cb_handset_pass = (CheckBox) findViewById(R.id.handset_passed);
		cb_handset_fail = (CheckBox) findViewById(R.id.handset_failed);

		cb_speaker_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_speaker_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_mic_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_mic_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_speakermode_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_speakermode_fail.setOnCheckedChangeListener(new checkBoxListener());
		cb_handset_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_handset_fail.setOnCheckedChangeListener(new checkBoxListener());

		stop.setEnabled(false);
		play.setEnabled(false);

		if (!dev.contains("Vivex")) {
			sw_speaker.setEnabled(false);
			sw_speaker.setVisibility(View.INVISIBLE);
			cb_speakermode_pass.setEnabled(false);
			cb_speakermode_fail.setEnabled(false);
			cb_handset_pass.setEnabled(false);
			cb_handset_fail.setEnabled(false);
			cb_speakermode_pass.setVisibility(View.INVISIBLE);
			cb_speakermode_fail.setVisibility(View.INVISIBLE);
			cb_handset_pass.setVisibility(View.INVISIBLE);
			cb_handset_fail.setVisibility(View.INVISIBLE);
			
			tvhandset.setVisibility(View.INVISIBLE);
			tvspeaker.setVisibility(View.INVISIBLE);
			
			speakermode_flag = 1;
			handset_flag = 1;
		}

		sw_speaker.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

				if (isChecked) {
					RKJNI.DPspeakerMode();
					System.out.println("Speaker Mode: ON");
					Toast.makeText(getApplicationContext(), "Speaker Mode: ON", Toast.LENGTH_LONG).show();
				} else {
					RKJNI.DPhandsetMode();
					System.out.println("Speaker Mode: OFF");
					Toast.makeText(getApplicationContext(), "Speaker Mode: OFF", Toast.LENGTH_LONG).show();
				}

			}
		});

	}

	public void checkplay() {

		display.setText("Playing");
		display.setTextColor(Color.YELLOW);
		Toast.makeText(getApplicationContext(), "Playing Audio", Toast.LENGTH_LONG).show();

		return;

	}

	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.speaker_passed:
				if (cb_speaker_pass.isChecked()) {
					speaker_status = "SPEAKER = PASSED";
					speaker_flag = 1;

					cb_speaker_fail.setEnabled(false);
					cb_speaker_fail.setClickable(false);
					cb_speaker_fail.setChecked(false);

					data.clear();
					remarks(speaker_status);
				} else {
					speaker_flag = 0;
					cb_speaker_fail.setEnabled(true);
					cb_speaker_fail.setClickable(true);
				}
				break;
			case R.id.speaker_failed:
				if (cb_speaker_fail.isChecked()) {
					speaker_status = "SPEAKER = FAILED";
					speaker_flag = 1;

					cb_speaker_pass.setEnabled(false);
					cb_speaker_pass.setClickable(false);
					cb_speaker_pass.setChecked(false);

					data.clear();
					remarks(speaker_status);
				} else {
					speaker_flag = 0;
					cb_speaker_pass.setEnabled(true);
					cb_speaker_pass.setClickable(true);
				}
				break;

			case R.id.mic_passed:
				if (cb_mic_pass.isChecked()) {
					mic_status = "MIC = PASSED";
					mic_flag = 1;

					cb_mic_fail.setEnabled(false);
					cb_mic_fail.setClickable(false);
					cb_mic_fail.setChecked(false);

					data.clear();
					remarks(mic_status);
				} else {
					mic_flag = 0;
					cb_mic_fail.setEnabled(true);
					cb_mic_fail.setClickable(true);
				}
				break;
			case R.id.mic_failed:
				if (cb_mic_fail.isChecked()) {
					mic_status = "MIC = FAILED";
					mic_flag = 1;

					cb_mic_pass.setEnabled(false);
					cb_mic_pass.setClickable(false);
					cb_mic_pass.setChecked(false);

					data.clear();
					remarks(mic_status);
				} else {
					mic_flag = 0;
					cb_mic_pass.setEnabled(true);
					cb_mic_pass.setClickable(true);
				}
				break;

			case R.id.speakermode_passed:
				if (cb_speakermode_pass.isChecked()) {
					speakermode_status = "SPEAKER MODE = PASSED";
					speakermode_flag = 1;

					cb_speakermode_fail.setEnabled(false);
					cb_speakermode_fail.setClickable(false);
					cb_speakermode_fail.setChecked(false);

					data.clear();
					remarks(speakermode_status);
				} else {
					speakermode_flag = 0;
					cb_speakermode_fail.setEnabled(true);
					cb_speakermode_fail.setClickable(true);
				}
				break;
			case R.id.speakermode_failed:
				if (cb_speakermode_fail.isChecked()) {
					speakermode_status = "SPEAKER MODE = FAILED";
					speakermode_flag = 1;

					cb_speakermode_pass.setEnabled(false);
					cb_speakermode_pass.setClickable(false);
					cb_speakermode_pass.setChecked(false);

					data.clear();
					remarks(speakermode_status);
				} else {
					speakermode_flag = 0;
					cb_speakermode_pass.setEnabled(true);
					cb_speakermode_pass.setClickable(true);
				}
				break;

			case R.id.handset_passed:
				if (cb_handset_pass.isChecked()) {
					handset_status = "HANDSET MODE = PASSED";
					handset_flag = 1;

					cb_handset_fail.setEnabled(false);
					cb_handset_fail.setClickable(false);
					cb_handset_fail.setChecked(false);

					data.clear();
					remarks(handset_status);
				} else {
					handset_flag = 0;
					cb_handset_fail.setEnabled(true);
					cb_handset_fail.setClickable(true);
				}
				break;
			case R.id.handset_failed:
				if (cb_handset_fail.isChecked()) {
					handset_status = "HANDSET MODE = FAILED";
					handset_flag = 1;

					cb_handset_pass.setEnabled(false);
					cb_handset_pass.setClickable(false);
					cb_handset_pass.setChecked(false);

					data.clear();
					remarks(handset_status);
				} else {
					handset_flag = 0;
					cb_handset_pass.setEnabled(true);
					cb_handset_pass.setClickable(true);
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

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {

		case R.id.homeButton:
			Intent homeIntent = new Intent(AndroidMicSpeeker.this, RktesterApps.class);
			startActivity(homeIntent);
			break;

		case R.id.nextButton:
			Context context = getApplicationContext();
			CharSequence text = "Please leave a remarks on the checkbox provided!";
			int duration = Toast.LENGTH_SHORT;

			if (speaker_flag == 0 || mic_flag == 0 || handset_flag == 0 || speakermode_flag == 0) {
				Toast toast = Toast.makeText(context, text, duration);
				toast.show();
			} else {
				Intent nextIntent = new Intent(AndroidMicSpeeker.this, AndroidUsb.class);
				startActivity(nextIntent);
			}
			break;

		case R.id.backButton:
			finish();
			break;

		case R.id.button3:
			play.setEnabled(false);
			stop.setEnabled(false);
			record.setEnabled(false);
			m = new MediaPlayer();
			try {
				m.setDataSource(outputFile);
			}

			catch (IOException e) {
				e.printStackTrace();
			}

			try {
				m.prepare();
			}

			catch (IOException e) {
				e.printStackTrace();
			}

			m.start();
			Toast.makeText(getApplicationContext(), "Playing audio", Toast.LENGTH_LONG).show();

			m.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

				@Override
				public void onCompletion(MediaPlayer mp) {
					m.reset();
					m.release();
					play.setEnabled(false);
					stop.setEnabled(false);
					record.setEnabled(true);
					Toast.makeText(getApplicationContext(), "Stop playing!", Toast.LENGTH_LONG).show();
				}
			});
			
			break;

		case R.id.button2:
			myAudioRecorder.stop();
			myAudioRecorder.reset();
			myAudioRecorder.release();
			myAudioRecorder = null;

			display.setText("Recorded");
			display.setTextColor(Color.GREEN);

			Toast.makeText(getApplicationContext(), "Audio recorded successfully", Toast.LENGTH_LONG).show();
			record.setEnabled(false);
			stop.setEnabled(false);
			play.setEnabled(true);
			break;

		case R.id.button:
			if (file.exists()) {
				file.delete();
			}
			myAudioRecorder = new MediaRecorder();
			myAudioRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
			myAudioRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
			myAudioRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
			myAudioRecorder.setOutputFile(outputFile);
			try {
				myAudioRecorder.prepare();
				myAudioRecorder.start();
				record.setPressed(true);
				display.setText("Recording");
				display.setTextColor(Color.RED);
			} catch (IllegalStateException e) {
				e.printStackTrace();
			}

			catch (IOException e) {
				e.printStackTrace();
			}
			// record.setEnabled(false);
			stop.setEnabled(true);
			record.setEnabled(false);
			Toast.makeText(getApplicationContext(), "Recording started", Toast.LENGTH_LONG).show();
			break;

		case R.id.button4:
			if (ringtoneSound != null) {
				ringtoneSound.play();
				Toast.makeText(getApplicationContext(), "Playing Default Ringtone", Toast.LENGTH_SHORT).show();
			}
			break;

		case R.id.button5:
			ringtoneSound.stop();
			break;

		case R.id.soundtools:
			PackageManager pm = getPackageManager();
			Intent intent = pm.getLaunchIntentForPackage("com.julian.apps.AudioTool");
			startActivity(intent);
			break;

		default:
			break;
		}

	}

}
