package com.choiboi.imagecroppingexample;

import java.io.File;
import java.util.ArrayList;

import com.example.rktesterapps.R;
import com.example.rktesterapps.Report;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ImageDisplay extends Activity {

	private Button home, back;
	private Button btnGallery;
	private CheckBox cb_picture_pass, cb_picture_fail;

	private static String destination;
	private final String cmd = "CMD_APPEND_FILE";
	private static String picture_status;
	private static int picture_flag;

	private ImageView image, picture;
	private File imgFile = new File("/sdcard/android-logos-elf.png");

	private static ArrayList<String> data = new ArrayList<String>();

	private static final int CAMERA_REQUEST = 1888;
	private static int RESULT_LOAD_IMAGE = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_image_display);
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");

		picture = (ImageView) findViewById(R.id.local);
		cb_picture_pass = (CheckBox) findViewById(R.id.pic_pass);
		cb_picture_fail = (CheckBox) findViewById(R.id.pic_fail);
		home = (Button) findViewById(R.id.homeButton);
		back = (Button) findViewById(R.id.backButton);
		btnGallery = (Button) findViewById(R.id.gallery);

		cb_picture_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_picture_fail.setOnCheckedChangeListener(new checkBoxListener());

		Intent getpic = getIntent();
		String pic = getpic.getStringExtra("pic");
		Bitmap myBitmap = BitmapFactory.decodeFile(pic);
		picture.setImageBitmap(myBitmap);

		back.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				CharSequence text = "Please leave a remarks on the checkbox provided!";
				int duration = Toast.LENGTH_SHORT;

				if (picture_flag == 0) {
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				} else {
					finish();
				}
			}
		});
		home.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Intent homeIntent = new Intent(ImageDisplay.this, CropActivity.class);
				startActivity(homeIntent);

			}
		});

		btnGallery.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent i = new Intent(Intent.ACTION_PICK,
						android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				startActivityForResult(i, RESULT_LOAD_IMAGE);
			}
		});
	}

	@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent buff) {
    	super.onActivityResult(requestCode, resultCode, buff);
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != buff) {
			Uri selectedImage = buff.getData();
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(selectedImage,
					filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			String picturePath = cursor.getString(columnIndex);
			cursor.close();
			ImageView imageView = (ImageView) findViewById(R.id.orig);
			imageView.setImageBitmap(BitmapFactory.decodeFile(picturePath));
		}
	}
	
	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.pic_pass:
				if (cb_picture_pass.isChecked()) {
					picture_status = "CAMERA = PASSED";
					picture_flag = 1;
					
					cb_picture_fail.setEnabled(false);
					cb_picture_fail.setClickable(false);
					cb_picture_fail.setChecked(false);
					
					data.clear();
					remarks(picture_status);
				} else {
					picture_flag = 0;
					cb_picture_fail.setEnabled(true);
					cb_picture_fail.setClickable(true);
				}
				break;
			case R.id.pic_fail:
				if (cb_picture_fail.isChecked()) {
					picture_status = "CAMERA = FAILED";
					picture_flag = 1;
					cb_picture_pass.setEnabled(false);
					cb_picture_pass.setClickable(false);
					cb_picture_pass.setChecked(false);
					
					data.clear();
					remarks(picture_status);
				} else {
					picture_flag = 0;
					cb_picture_pass.setEnabled(true);
					cb_picture_pass.setClickable(true);
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
