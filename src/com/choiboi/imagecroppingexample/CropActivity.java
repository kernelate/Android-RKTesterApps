package com.choiboi.imagecroppingexample;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.choiboi.imagecroppingexample.gestures.MoveGestureDetector;
import com.choiboi.imagecroppingexample.gestures.RotateGestureDetector;
import com.example.rktesterapps.AndroidDrivers;
import com.example.rktesterapps.R;
import com.example.rktesterapps.Report;
import com.example.rktesterapps.RktesterApps;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

public class CropActivity extends Activity implements OnTouchListener {

	// Member fields.
	private ImageView mImg;
	private ImageView mTemplateImg;
	private int mScreenWidth;
	private int mScreenHeight;
	private CropHandler mCropHandler;
	private static ProgressDialog mProgressDialog;
	private int mSelectedVersion;

	private Matrix mMatrix = new Matrix();
	private float mScaleFactor = 0.8f;
	private float mRotationDegrees = 0.f;
	private float mFocusX = 0.f;
	private float mFocusY = 0.f;
	private int mImageHeight, mImageWidth;
	private ScaleGestureDetector mScaleDetector;
	private RotateGestureDetector mRotateDetector;
	private MoveGestureDetector mMoveDetector;

	private int mTemplateWidth;
	private int mTemplateHeight;

	// Constants
	public static final int MEDIA_GALLERY = 1;
	public static final int TEMPLATE_SELECTION = 2;
	public static final int DISPLAY_IMAGE = 3;

	private final static int IMG_MAX_SIZE = 1000;
	private final static int IMG_MAX_SIZE_MDPI = 400;

	private static final int CAMERA_REQUEST = 1888;
	SurfaceView surfaceView;
	Camera camera;
	SurfaceHolder surfaceHolder;
	boolean previewing = false;
	Button buttonStartCameraPreview, buttonStopCameraPreview;

	private Bitmap bitmapMaster;
	// private RelativeLayout CamView;
	// private Bitmap inputBMP = null, bmp, bmp1;

	private CheckBox cb_camera_pass, cb_camera_fail;

	private static String destination;
	private final String cmd = "CMD_APPEND_FILE";
	private static String camera_status;
	private static int camera_flag;

	private static ArrayList<String> data = new ArrayList<String>();

	Bitmap bitmap;
	Button crop;

	RoundImage roundedImage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_crop);
		
		Intent getFile = getIntent();
		destination = getFile.getStringExtra("file");
		// mSelectedVersion =
		// getIntent().getExtras().getInt(MainActivity.CROP_VERSION_SELECTED_KEY,
		// -1);
		mSelectedVersion = 2;

		buttonStartCameraPreview = (Button) findViewById(R.id.startcamerapreview);
		buttonStopCameraPreview = (Button) findViewById(R.id.stopcamerapreview);

		buttonStopCameraPreview.setEnabled(false);
		// crop = (Button)findViewById(R.id.)

		// CamView = (RelativeLayout)
		// findViewById(R.id.camview);//RELATIVELAYOUT OR

		cb_camera_pass = (CheckBox) findViewById(R.id.camera_passed);
		cb_camera_fail = (CheckBox) findViewById(R.id.camera_failed);

		cb_camera_pass.setOnCheckedChangeListener(new checkBoxListener());
		cb_camera_fail.setOnCheckedChangeListener(new checkBoxListener());

		Button back = (Button) findViewById(R.id.backButton);
		Button home = (Button) findViewById(R.id.homeButton);
		Button next = (Button) findViewById(R.id.nextButton);
		Button capture = (Button) findViewById(R.id.button_capture);

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

				Intent homeIntent = new Intent(CropActivity.this, RktesterApps.class);
				startActivity(homeIntent);

			}
		});
		next.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Context context = getApplicationContext();
				CharSequence text = "Please leave a remarks on the checkbox provided!";
				int duration = Toast.LENGTH_SHORT;

				if (camera_flag == 0) {
					Toast toast = Toast.makeText(context, text, duration);
					toast.show();
				} else {
					Intent nextIntent = new Intent(CropActivity.this, AndroidDrivers.class);
					nextIntent.putExtra("file", destination);
					startActivity(nextIntent);
				}
			}
		});

		mImg = (ImageView) findViewById(R.id.cp_img);
		mTemplateImg = (ImageView) findViewById(R.id.cp_face_template);
		mImg.setOnTouchListener(this);

		// Get screen size in pixels.
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mScreenWidth = metrics.heightPixels;
		mScreenHeight = metrics.widthPixels;

		Toast.makeText(this, "mScreenHeight " + mScreenHeight + " x mScreenWidth " + mScreenWidth, Toast.LENGTH_LONG)
				.show();

		// Bitmap faceTemplate = BitmapFactory.decodeResource(getResources(),
		// R.drawable.face_oval);
		// mTemplateWidth = faceTemplate.getWidth();
		// mTemplateHeight = faceTemplate.getHeight();

		// Set template image accordingly to device screen size.
		// if (mScreenWidth == 320 && mScreenHeight == 480) {
		// mTemplateWidth = 218;
		// mTemplateHeight = 300;
		// faceTemplate = Bitmap.createScaledBitmap(faceTemplate,
		// mTemplateWidth, mTemplateHeight, true);
		// mTemplateImg.setImageBitmap(faceTemplate);
		// } else {
		// mTemplateHeight = 400;
		// mTemplateWidth = 318;
		// faceTemplate = Bitmap.createScaledBitmap(faceTemplate,
		// mTemplateWidth, mTemplateHeight, true);
		// mTemplateImg.setImageBitmap(faceTemplate);
		// mTemplateWidth = 218;
		// mTemplateHeight = 300;
		// faceTemplate = Bitmap.createScaledBitmap(faceTemplate,
		// mTemplateWidth, mTemplateHeight, true);
		// mTemplateImg.setImageBitmap(faceTemplate);
		// }

		// Load temp image.
		// Bitmap photoImg = BitmapFactory.decodeResource(getResources(),
		// R.drawable.temp_image);
		// photoImg = Bitmap.createScaledBitmap(photoImg, 480, 550, true);
		// roundedImage = new RoundImage(photoImg);
		// mImg.setImageDrawable(roundedImage);
		// mImg.setImageBitmap(photoImg);
		mImageHeight = mImg.getWidth();
		mImageWidth = mImg.getHeight();

		// View is scaled by matrix, so scale initially
		mMatrix.postScale(mScaleFactor, mScaleFactor);
		mImg.setImageMatrix(mMatrix);

		// Setup Gesture Detectors
		mScaleDetector = new ScaleGestureDetector(getApplicationContext(), new ScaleListener());
		mRotateDetector = new RotateGestureDetector(getApplicationContext(), new RotateListener());
		mMoveDetector = new MoveGestureDetector(getApplicationContext(), new MoveListener());

		// Instantiate Thread Handler.
		mCropHandler = new CropHandler(this);

		buttonStartCameraPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				buttonStartCameraPreview.setClickable(false);
				buttonStopCameraPreview.setEnabled(true);
				Toast.makeText(CropActivity.this, "Camera", Toast.LENGTH_SHORT).show();
				surfaceView = (SurfaceView) findViewById(R.id.surfaceview);
				surfaceHolder = surfaceView.getHolder();
				surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
				if (!previewing) {
					Toast.makeText(CropActivity.this, "previewing", Toast.LENGTH_SHORT).show();
					int numCams = Camera.getNumberOfCameras();
					camera = Camera.open(0);
					setCameraDisplayOrientation(CropActivity.this, CameraInfo.CAMERA_FACING_FRONT, camera);
					Toast.makeText(CropActivity.this, "numCams " + numCams, Toast.LENGTH_SHORT).show();
					if (numCams > 0) {
						try {
							Handler handler = new Handler();
							handler.postDelayed(new Runnable() {
								@Override
								public void run() {
									try {
										camera.setPreviewDisplay(surfaceHolder);
										// Camera.Parameters parameters =
										// camera.getParameters();
										// if
										// (CropActivity.this.getResources().getConfiguration().orientation
										// !=
										// Configuration.ORIENTATION_LANDSCAPE)
										// {
										// parameters.set("orientation",
										// "portrait");
										// camera.setDisplayOrientation(90);
										// }
										// camera.setParameters(parameters);
									} catch (IOException e) {
										e.printStackTrace();
									}
									camera.startPreview();
								}
							}, 1000);
							// camera.startPreview();
							// camera.setPreviewDisplay(surfaceHolder);
							previewing = true;
							// camera.reconnect();
						} catch (RuntimeException ex) {
							Toast.makeText(CropActivity.this, "No Camera", Toast.LENGTH_LONG).show();
						} /*
							 * catch (IOException ex){
							 * Toast.makeText(MainActivity.this,
							 * "No Camera IOException",
							 * Toast.LENGTH_LONG).show(); }
							 */
					}
				}

			}
		});
		buttonStopCameraPreview.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("buttonStopCameraPreview");
				// TODO Auto-generated method stub
				// if (camera != null && previewing)
				// {
				// camera.stopPreview();
				// camera.release();
				// camera = null;
				// previewing = false;
				// }

				// surfaceView.setDrawingCacheEnabled(false);
				// surfaceView.setDrawingCacheEnabled(true);
				// // mImg = surfaceView.getDrawingCache();
				// System.out.println(surfaceView.getDrawingCache());
				// mImg.setImageBitmap(surfaceView.getDrawingCache());

				camera.takePicture(shutterCallback, rawCallback, jpegCallback); // this
																				// is
																				// working
																				// but
																				// too
																				// large
				// working SS
				// bitmap = getBitmapOfView(surfaceView);
				// mImg.setImageBitmap(bitmap);
				// createImageFromBitmap(bitmap);
				Toast.makeText(CropActivity.this, "Picture Captured Great Success!", Toast.LENGTH_LONG).show();
				buttonStartCameraPreview.setClickable(true);
				buttonStopCameraPreview.setClickable(false);

				// Intent displayIntent = new Intent(CropActivity.this,
				// ImageDisplay.class);
				// startActivity(displayIntent);
			}
		});
		capture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				System.out.println("camera");
				if (camera != null) {
					camera.takePicture(myShutterCallback, myPictureCallback_RAW, myPictureCallback_JPG);
					// Toast toast = Toast.makeText(myContext,
					// "Sorry, your phone has only one camera!",
					// Toast.LENGTH_LONG);
					// toast.show();

				}
			}
		});
	}

	public static void setCameraDisplayOrientation(Activity activity, int cameraId, android.hardware.Camera camera) {
	    int numberOfCameras = Camera.getNumberOfCameras();
	    CameraInfo info = new CameraInfo();
		for (int i = 0; i < numberOfCameras; i++) {
			Camera.getCameraInfo(i, info);
		}
	    int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
	    int degrees = 0;
	    switch (rotation) {
	    case Surface.ROTATION_0:
	    	System.out.println("ROTATION_0");
	            degrees = 0;
	            break;
	    case Surface.ROTATION_90:
	    	System.out.println("ROTATION_90");
	            degrees = 90;
	            break;
	    case Surface.ROTATION_180:
	    	System.out.println("ROTATION_180");
	            degrees = 180;
	            break;
	    case Surface.ROTATION_270:
	    	System.out.println("ROTATION_270");
	            degrees = 270;
	            break;
	    }

	    int result;
	    if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
	            result = (info.orientation + degrees) % 360;
	            result = (360 - result) % 360; // compensate the mirror
	            System.out.println(result);
	    } else { // back-facing
	            result = (info.orientation - degrees + 360) % 360;
	            System.out.println(result);
	    }
	    camera.setDisplayOrientation(result);
	}

	public Bitmap getBitmapOfView(View v) {
		System.out.println("getBitmapOfView");
		View rootview = v.getRootView();
		rootview.setDrawingCacheEnabled(true);
		Bitmap bmp = rootview.getDrawingCache();
		return bmp;
	}

	public void createImageFromBitmap(Bitmap bmp) {
		System.out.println("createImageFromBitmap");
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		bmp.compress(Bitmap.CompressFormat.JPEG, 40, bytes);
		File file = new File(Environment.getExternalStorageDirectory() + "/capturedscreen.jpg");
		try {
			file.createNewFile();
			FileOutputStream ostream = new FileOutputStream(file);
			ostream.write(bytes.toByteArray());
			ostream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	ShutterCallback shutterCallback = new ShutterCallback() {
		public void onShutter() {
			// Log.d(TAG, "onShutter'd");
		}
	};

	PictureCallback rawCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			// Log.d(TAG, "onPictureTaken - raw");
		}
	};

	PictureCallback jpegCallback = new PictureCallback() {
		public void onPictureTaken(byte[] data, Camera camera) {
			int screenWidth = getResources().getDisplayMetrics().widthPixels;
			int screenHeight = getResources().getDisplayMetrics().heightPixels;
			Bitmap bm = BitmapFactory.decodeByteArray(data, 0, (data != null) ? data.length : 0);
			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
				// Notice that width and height are reversed
				Bitmap scaled = Bitmap.createScaledBitmap(bm, screenHeight, screenWidth, true);
				int w = scaled.getWidth();
				int h = scaled.getHeight();
				// Setting post rotate to 90
				Matrix mtx = new Matrix();
				mtx.postRotate(180);
				// Rotating Bitmap
				bm = Bitmap.createBitmap(scaled, 0, 0, w, h, mtx, true);
			} else {// LANDSCAPE MODE
					// No need to reverse width and height
				Bitmap scaled = Bitmap.createScaledBitmap(bm, screenWidth, screenHeight, true);
				bm = scaled;
			}
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			bm.compress(Bitmap.CompressFormat.PNG, 100, stream);
			byte[] byteArray = stream.toByteArray();
			new SaveImageTask().execute(byteArray);
			resetCam();
			Log.d("TESTING APP", "onPictureTaken - jpeg");
		}
	};

	private void resetCam() {
		camera.startPreview();
		// preview.setCamera(camera);
	}

	private void refreshGallery(File file) {
		System.out.println("refreshGallery");
		Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
		mediaScanIntent.setData(Uri.fromFile(file));
		sendBroadcast(mediaScanIntent);
	}

	private class SaveImageTask extends AsyncTask<byte[], Void, Void> {

		File outFile;

		@Override
		protected void onPostExecute(Void result) {
			buttonStartCameraPreview.setClickable(true);
			buttonStopCameraPreview.setClickable(false);
			System.out.println("SaveImageTask");
			Intent displayIntent = new Intent(CropActivity.this, ImageDisplay.class);
			displayIntent.putExtra("pic", outFile.getAbsolutePath());
			displayIntent.putExtra("file", destination);
			startActivity(displayIntent);
			camera.release();

			// final BitmapFactory.Options options = new
			// BitmapFactory.Options();
			// options.inJustDecodeBounds = true;
			// BitmapFactory.decodeFile(outFile.getAbsolutePath(), options);
			// if (mScreenWidth == 320 && mScreenHeight == 480) {
			// options.inSampleSize = calculateImageSize(options,
			// IMG_MAX_SIZE_MDPI);
			// } else {
			// options.inSampleSize = calculateImageSize(options, IMG_MAX_SIZE);
			// }
			//
			// options.inJustDecodeBounds = false;
			// Bitmap photoImg = BitmapFactory.decodeFile(
			// outFile.getAbsolutePath(), options);
			//
			// // Bitmap photoImg =
			// // BitmapFactory.decodeFile(outFile.getAbsolutePath());
			// Matrix matrixMirror = new Matrix();
			// matrixMirror.preScale(-1.0f, 1.0f);
			// bitmapMaster = Bitmap.createBitmap(photoImg, 0, 0,
			// photoImg.getWidth(), photoImg.getHeight(), matrixMirror,
			// false);
			// Bitmap croppedBmp = Bitmap.createBitmap(bitmapMaster,
			// bitmapMaster.getWidth() / 2 - bitmapMaster.getHeight() / 2,
			// 0, bitmapMaster.getHeight(), bitmapMaster.getHeight());
			// mImageHeight = croppedBmp.getHeight();
			// mImageWidth = croppedBmp.getWidth();
			// mImg.setImageBitmap(croppedBmp);

		}

		@Override
		protected Void doInBackground(byte[]... data) {
			FileOutputStream outStream = null;
			System.out.println("SaveImageTask: doInBackground");
			// Write to SD Card
			try {
				File sdCard = Environment.getExternalStorageDirectory();
				File dir = new File(sdCard.getAbsolutePath() + "/camtest");
				dir.mkdirs();

				String fileName = String.format("%d.jpg", System.currentTimeMillis());
				System.out.println("filename " + fileName);
				outFile = new File(dir, fileName);

				outStream = new FileOutputStream(outFile);
				outStream.write(data[0]);
				outStream.flush();
				outStream.close();

				Log.d("SAVING PIC",
						"onPictureTaken - wrote bytes: " + data.length + " to " + outFile.getAbsolutePath());

				refreshGallery(outFile);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {
			}
			return null;
		}
	}

	public void onCropImageButton(View v) {
		System.out.println("onCropImageButton");
		// Create progress dialog and display it.
		mProgressDialog = new ProgressDialog(v.getContext());
		mProgressDialog.setCancelable(false);
		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mProgressDialog.setMessage("Cropping Image\nPlease Wait.....");
		mProgressDialog.show();

		// Setting values so that we can retrive the image from
		// ImageView multiple times.
		mImg.buildDrawingCache(true);
		mImg.setDrawingCacheEnabled(true);
		mTemplateImg.buildDrawingCache(true);
		mTemplateImg.setDrawingCacheEnabled(true);

		// Create new thread to crop.
		new Thread(new Runnable() {
			@Override
			public void run() {
				// Crop image using the correct template size.
				Bitmap croppedImg = null;
				if (mScreenWidth == 320 && mScreenHeight == 480) {
					if (mSelectedVersion == MainActivity.VERSION_1) {
						croppedImg = ImageProcess.cropImage(mImg.getDrawingCache(true),
								mTemplateImg.getDrawingCache(true), mTemplateWidth, mTemplateHeight);
					} else {
						croppedImg = ImageProcess.cropImageVer2(mImg.getDrawingCache(true),
								mTemplateImg.getDrawingCache(true), mTemplateWidth, mTemplateHeight);
					}
				} else {
					if (mSelectedVersion == MainActivity.VERSION_1) {
						croppedImg = ImageProcess.cropImage(mImg.getDrawingCache(true),
								mTemplateImg.getDrawingCache(true), mTemplateWidth, mTemplateHeight);
					} else {
						// croppedImg =
						// ImageProcess.cropImageVer2(mImg.getDrawingCache(true),
						// mTemplateImg.getDrawingCache(true), 1080, 480);
						// croppedImg =
						// ImageProcess.cropImagetest(mImg.getDrawingCache(true),
						// mTemplateImg.getDrawingCache(true), mTemplateWidth,
						// mTemplateHeight);
						// croppedImg =
						// ImageProcess.cropImage2(mImg.getDrawingCache(true),
						// mTemplateImg.getDrawingCache(true), mTemplateWidth,
						// mTemplateHeight);
						// croppedImg =
						// ImageProcess.testing(mImg.getDrawingCache(true),
						// mTemplateImg.getDrawingCache(true));
						// croppedImg =
						// ImageProcess.cropImageVer2_1(mImg.getDrawingCache(true),
						// mTemplateImg.getDrawingCache(true), 1920, 1008);
						croppedImg = ImageProcess.cropImage_NEW(mImg.getDrawingCache(true),
								mTemplateImg.getDrawingCache(true), 320, 440);
					}
				}
				mImg.setDrawingCacheEnabled(false);
				mTemplateImg.setDrawingCacheEnabled(false);

				// Send a message to the Handler indicating the Thread has
				// finished.
				mCropHandler.obtainMessage(DISPLAY_IMAGE, -1, -1, croppedImg).sendToTarget();
			}
		}).start();
	}

	// public void onChangeTemplateButton(View v) {
	// Intent intent = new Intent(this, TemplateSelectDialog.class);
	// startActivityForResult(intent, TEMPLATE_SELECTION);
	// }

	public void onChangeImageButton(View v) {
		System.out.println("onChangeImageButton");
		// Start Gallery App.
		Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		startActivityForResult(intent, MEDIA_GALLERY);
	}

	/*
	 * Adjust the size of bitmap before loading it to memory. This will help the
	 * phone by not taking up a lot memory.
	 */
	private void setSelectedImage(String path) {
		System.out.println("setSelectedImage");
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		if (mScreenWidth == 320 && mScreenHeight == 480) {
			options.inSampleSize = calculateImageSize(options, IMG_MAX_SIZE_MDPI);
		} else {
			options.inSampleSize = calculateImageSize(options, IMG_MAX_SIZE);
		}

		options.inJustDecodeBounds = false;
		Bitmap photoImg = BitmapFactory.decodeFile(path, options);
		mImageHeight = photoImg.getHeight();
		mImageWidth = photoImg.getWidth();
		System.out.println("setSelectedImage");
		mImg.setImageBitmap(photoImg);
	}

	/*
	 * Retrieves the path to the selected image from the Gallery app.
	 */
	private String getGalleryImagePath(Intent data) {
		System.out.println("getGalleryImagePath");
		Uri imgUri = data.getData();
		String filePath = "";
		if (data.getType() == null) {
			// For getting images from gallery.
			String[] filePathColumn = { MediaStore.Images.Media.DATA };
			Cursor cursor = getContentResolver().query(imgUri, filePathColumn, null, null, null);
			cursor.moveToFirst();
			int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
			filePath = cursor.getString(columnIndex);
			cursor.close();
		}
		System.out.println("getGalleryImagePath: " + filePath);
		return filePath;
	}

	/*
	 * Calculation used to determine by what factor images need to be reduced
	 * by. Images with its longest side below the threshold will not be resized.
	 */
	private int calculateImageSize(BitmapFactory.Options opts, int threshold) {
		System.out.println("calculateImageSize");
		int scaleFactor = 1;
		final int height = opts.outHeight;
		final int width = opts.outWidth;

		if (width >= height) {
			scaleFactor = Math.round((float) width / threshold);
		} else {
			scaleFactor = Math.round((float) height / threshold);
		}
		return scaleFactor;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			if (requestCode == MEDIA_GALLERY) {
				String path = getGalleryImagePath(data);
				System.out.println("requestCode == MEDIA_GALLERY: " + path);
				setSelectedImage(path);
			} else if (requestCode == TEMPLATE_SELECTION) {
				// int pos = data.getExtras()
				// .getInt(TemplateSelectDialog.POSITION);
				Bitmap templateImg = null;
				System.out.println("requestCode == TEMPLATE_SELECTION");
				// Change template according to what the user has selected.
				// switch (pos) {
				// case 0:
				// templateImg = BitmapFactory.decodeResource(getResources(),
				// R.drawable.face_oblong);
				// break;
				// case 1:
				// templateImg = BitmapFactory.decodeResource(getResources(),
				// R.drawable.face_oval);
				// break;
				// case 2:
				// templateImg = BitmapFactory.decodeResource(getResources(),
				// R.drawable.face_round);
				// break;
				// case 3:
				// templateImg = BitmapFactory.decodeResource(getResources(),
				// R.drawable.face_square);
				// break;
				// case 4:
				// templateImg = BitmapFactory.decodeResource(getResources(),
				// R.drawable.face_triangular);
				// break;
				// }

				mTemplateWidth = templateImg.getWidth();
				mTemplateHeight = templateImg.getHeight();

				// Resize template if necessary.
				if (mScreenWidth == 320 && mScreenHeight == 480) {
					mTemplateWidth = 218;
					mTemplateHeight = 300;
					templateImg = Bitmap.createScaledBitmap(templateImg, mTemplateWidth, mTemplateHeight, true);
				}
				mTemplateImg.setImageBitmap(templateImg);
			}
		}

		if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
			System.out.println("equestCode == CAMERA_REQUEST && resultCode == RESULT_OK");
			Bitmap photo = (Bitmap) data.getExtras().get("data");
			mImg.setImageBitmap(photo);
		}
	}

	static Bitmap cropImg;

	private static class CropHandler extends Handler {
		WeakReference<CropActivity> mThisCA;

		CropHandler(CropActivity ca) {
			mThisCA = new WeakReference<CropActivity>(ca);
		}

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			CropActivity ca = mThisCA.get();
			if (msg.what == DISPLAY_IMAGE) {
				System.out.println("DISPLAY_IMAGE");
				mProgressDialog.dismiss();
				cropImg = (Bitmap) msg.obj;

				// Setup an AlertDialog to display cropped image.
				AlertDialog.Builder builder = new AlertDialog.Builder(ca);
				builder.setTitle("Final Cropped Image");
				builder.setIcon(new BitmapDrawable(ca.getResources(), cropImg));
				builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				AlertDialog dialog = builder.create();
				dialog.show();
			}
			System.out.println("CropHandler");
			File sdCard = Environment.getExternalStorageDirectory();
			File dir = new File(sdCard.getAbsolutePath() + "/camtest");
			String fileName = String.format("%d.png", System.currentTimeMillis());
			System.out.println("filename " + fileName);
			FileOutputStream outStream;
			try {

				outStream = new FileOutputStream(dir.getAbsolutePath() + "/" + fileName);
				System.out.println("outStream " + outStream);
				cropImg.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				/* 100 to keep full quality of the image */

				outStream.flush();
				outStream.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean onTouch(View v, MotionEvent event) {
		System.out.println("onTouch");
		mScaleDetector.onTouchEvent(event);
		mRotateDetector.onTouchEvent(event);
		mMoveDetector.onTouchEvent(event);

		float scaledImageCenterX = (mImageWidth * mScaleFactor) / 2;
		float scaledImageCenterY = (mImageHeight * mScaleFactor) / 2;

		mMatrix.reset();
		mMatrix.postScale(mScaleFactor, mScaleFactor);
		mMatrix.postRotate(mRotationDegrees, scaledImageCenterX, scaledImageCenterY);
		mMatrix.postTranslate(mFocusX - scaledImageCenterX, mFocusY - scaledImageCenterY);

		ImageView view = (ImageView) v;
		view.setImageMatrix(mMatrix);
		return true;
	}

	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
		@Override
		public boolean onScale(ScaleGestureDetector detector) {
			mScaleFactor *= detector.getScaleFactor();
			mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 10.0f));

			return true;
		}
	}

	private class RotateListener extends RotateGestureDetector.SimpleOnRotateGestureListener {
		@Override
		public boolean onRotate(RotateGestureDetector detector) {
			mRotationDegrees -= detector.getRotationDegreesDelta();
			return true;
		}
	}

	private class MoveListener extends MoveGestureDetector.SimpleOnMoveGestureListener {
		@Override
		public boolean onMove(MoveGestureDetector detector) {
			System.out.println("MoveListener");
			PointF d = detector.getFocusDelta();
			mFocusX += d.x;
			mFocusY += d.y;

			return true;
		}
	}

	public boolean onKeyUp(int keyCode, KeyEvent event) {

		switch (keyCode) {
		case KeyEvent.KEYCODE_H:
			// this.finish();
			System.out.println("BACK!!!");
			// onCropImageButton(findViewById(R.id.button_capture));
			return true;

		case KeyEvent.KEYCODE_HOME:

			System.out.println("HOME!!!");

			return true;

		default:

			return super.onKeyUp(keyCode, event);
		}
	}

	public Bitmap getCroppedBitmap(Bitmap bitmap) {
		System.out.println("getCroppedBitmap");
		Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		// canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
		canvas.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2, bitmap.getWidth() / 2, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);
		// Bitmap _bmp = Bitmap.createScaledBitmap(output, 60, 60, false);
		// return _bmp;
		return output;
	}

	ShutterCallback myShutterCallback = new ShutterCallback() {

		public void onShutter() {
			// TODO Auto-generated method stub
		}
	};

	PictureCallback myPictureCallback_RAW = new PictureCallback() {

		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub
		}
	};

	PictureCallback myPictureCallback_JPG = new PictureCallback() {

		public void onPictureTaken(byte[] arg0, Camera arg1) {
			// TODO Auto-generated method stub
			Bitmap bitmapPicture = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);

			Bitmap correctBmp = Bitmap.createBitmap(bitmapPicture, 0, 0, bitmapPicture.getWidth(),
					bitmapPicture.getHeight(), null, true);

		}
	};

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		// TODO Auto-generated method stub
		if (previewing) {
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public void surfaceCreated(SurfaceHolder holder) {
		System.out.println("surfaceCreated");
	}
	
	

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub

		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;

	}

	@Override
	protected void onDestroy() {

		if (previewing) {
			camera.stopPreview();
			camera.release();
		}
		camera = null;
		previewing = false;
		super.onDestroy();
	}

	class checkBoxListener implements CheckBox.OnCheckedChangeListener {

		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

			switch (buttonView.getId()) {
			case R.id.camera_passed:
				if (cb_camera_pass.isChecked()) {
					camera_status = "CAMERA = PASSED";
					camera_flag = 1;

					cb_camera_fail.setEnabled(false);
					cb_camera_fail.setClickable(false);
					cb_camera_fail.setChecked(false);

					data.clear();
					remarks(camera_status);
				} else {
					camera_flag = 0;
					cb_camera_fail.setEnabled(true);
					cb_camera_fail.setClickable(true);
				}
				break;
			case R.id.camera_failed:
				if (cb_camera_fail.isChecked()) {
					camera_status = "CAMERA = FAILED";
					camera_flag = 1;
					cb_camera_pass.setEnabled(false);
					cb_camera_pass.setClickable(false);
					cb_camera_pass.setChecked(false);

					data.clear();
					remarks(camera_status);
				} else {
					camera_flag = 0;
					cb_camera_pass.setEnabled(true);
					cb_camera_pass.setClickable(true);
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
