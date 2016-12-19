package com.choiboi.imagecroppingexample;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.util.Log;

public class ImageProcess {

	/*
	 * This method gets the image inside the template area and returns that bitmap.
	 * Process of how this method works:
	 *     1) Combine img and templateImage together with templateImage being on the top.
	 *     2) Create a new blank bitmap using the given width and height, which is the 
	 *        size of the template on the screen.
	 *     3) Starting in the center go through the image quadrant by quadrant copying 
	 *        the pixel value onto the new blank bitmap.
	 *     4) Once it hits the pixel colour values of the template lines, then set the pixel
	 *        values from that point on transparent.
	 *     5) Return the cropped bitmap.
	 */
	public static Bitmap cropImage(Bitmap img, Bitmap templateImage, int width, int height) {
		// Merge two images together.
		Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas combineImg = new Canvas(bm);
		combineImg.drawBitmap(img, 0f, 0f, null);
		combineImg.drawBitmap(templateImage, 0f, 0f, null);

		// Create new blank ARGB bitmap.
		Bitmap finalBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		// Get the coordinates for the middle of combineImg.
		int hMid = bm.getHeight() / 2;
		int wMid = bm.getWidth() / 2;
		int hfMid = finalBm.getHeight() / 2;
		int wfMid = finalBm.getWidth() / 2;

		int y2 = hfMid;
		int x2 = wfMid;

		for (int y = hMid; y >= 0; y--) {
			boolean template = false;
			// Check Upper-left section of combineImg.
			for (int x = wMid; x >= 0; x--) {
				if (x2 < 0) {
					break;
				}

				int px = bm.getPixel(x, y);
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					template = true;
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else if (template) {
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2--;
			}
			// Check upper-right section of combineImage.
			x2 = wfMid;
			template = false;
			for (int x = wMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}

				int px = bm.getPixel(x, y);
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					template = true;
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else if (template) {
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2++;
			}

			// Once we reach the top-most part on the template line, set pixel value transparent
			// from that point on.
			int px = bm.getPixel(wMid, y);
			if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
				for (int y3 = y2; y3 >= 0; y3--) {
					for (int x3 = 0; x3 < finalBm.getWidth(); x3++) {
						finalBm.setPixel(x3, y3, Color.TRANSPARENT);
					}
				}
				break;
			}

			x2 = wfMid;
			y2--;
		}

		x2 = wfMid;
		y2 = hfMid;
		for (int y = hMid; y <= bm.getHeight(); y++) {
			boolean template = false;
			// Check bottom-left section of combineImage.
			for (int x = wMid; x >= 0; x--) {
				if (x2 < 0) {
					break;
				}

				int px = bm.getPixel(x, y);
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					template = true;
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else if (template) {
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2--;
			}

			// Check bottom-right section of combineImage.
			x2 = wfMid;
			template = false;
			for (int x = wMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}

				int px = bm.getPixel(x, y);
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					template = true;
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else if (template) {
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2++;
			}

			// Once we reach the bottom-most part on the template line, set pixel value transparent
			// from that point on.
			int px = bm.getPixel(wMid, y);
			if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
				for (int y3 = y2; y3 < finalBm.getHeight(); y3++) {
					for (int x3 = 0; x3 < finalBm.getWidth(); x3++) {
						finalBm.setPixel(x3, y3, Color.TRANSPARENT);
					}
				}
				break;
			}

			x2 = wfMid;
			y2++;
		}

		// Get rid of images that we finished with to save memory.
		img.recycle();
		templateImage.recycle();
		bm.recycle();
		return finalBm;
	}

	/*
	 * This method gets the image inside the template area and returns that bitmap.
	 * Process of how this method works:
	 *     1) Combine img and templateImage together with templateImage being on the top.
	 *     2) Create a new blank bitmap using the given width and height, which is the 
	 *        size of the template on the screen.
	 *     3) Starting in the center go through the image quadrant by quadrant copying 
	 *        the pixel value onto the new blank bitmap.
	 *     4) Once it hits the pixel colour values of the template lines exit out of the loop.
	 *        (Because there is no point in setting transparent pixel values to pixels that are
	 *        already transparent.)
	 *     5) Return the cropped bitmap.
	 */
	public static Bitmap cropImage2(Bitmap img, Bitmap templateImage, int width, int height) {
		// Merge two images together.
		Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas combineImg = new Canvas(bm);
		combineImg.drawBitmap(img, 0f, 0f, null);
		combineImg.drawBitmap(templateImage, 0f, 0f, null);

		// Create new blank ARGB bitmap.
		Bitmap finalBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		// Get the coordinates for the middle of bm.
		int hMid = bm.getHeight() / 2;
		int wMid = bm.getWidth() / 2;
		int hfMid = finalBm.getHeight() / 2;
		int wfMid = finalBm.getWidth() / 2;

		int y2 = hfMid;
		int x2 = wfMid;
		System.out.println("y2 " + y2);

		for (int y = wMid; y >= 0; y--) {
			// Check Upper-left section of combineImg.
			for (int x = hMid; x >= 0; x--) {
				if (x2 < 0) {
					//                	System.out.println("loob na loob");
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the left side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					System.out.println("loob");
					break;
				} else {
					//                	System.out.println("labas  " + x2 + " " + y2);
					finalBm.setPixel(x2, y2, px);
				}
				x2--;
			}

			// Check upper-right section of combineImage.
			x2 = wfMid;
			for (int x = hMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the right side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {
					//                	System.out.println("labas2");
					//              	  Log.d("IMAGE CROP", "labas2  " + x2 + " " + y2);
					finalBm.setPixel(x2, y2, px);
				}
				x2++;
			}

			System.out.println(hMid + " : " + y);
			// Get out of loop once it hits top most part of the template.
			int px = bm.getPixel(hMid, y);
			//            int px = bm.getPixel(hMid, y);
			Log.d("IMAGE CROP", "////////////////////////////////////////////////////////");
			System.out.println("Color.red(px) " + Color.red(px) + "   ::::: 234");
			System.out.println("Color.green(px) " + Color.green(px) + "   ::::: 157");
			System.out.println("Color.blue(px) " + Color.blue(px) + "   ::::: 33");
			Log.d("IMAGE CROP", "////////////////////////////////////////////////////////");
			if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
				break;
			} 
			y2 = wfMid;
			x2--;
		}

		y2 = wfMid;
		x2 = hfMid;
		Log.d("IMAGE CROP", wfMid+ " : " + hfMid);
		for (int y = hMid; y <= bm.getHeight(); y++) {
			// Check bottom-left section of combineImage.
			for (int x = wMid; x >= 0; x--) {
				if (x2 < 0) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the left side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					System.out.println("loob3");
					break;
				} else {
					System.out.println("labas3  " + x2 + " " + y2);
					try
					{
						finalBm.setPixel(x2, y2, px);
					}
					catch(IllegalArgumentException e)
					{

					}
				}
				x2--;
			}

			// Check bottom-right section of combineImage.
			x2 = wfMid;
			for (int x = wMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the right side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {
					Log.d("IMAGE CROP", "labas3  " + x2 + " " + y2);
					finalBm.setPixel(x2, y2, px);
				}
				x2++;
			}

			// Get out of loop once it hits bottom most part of the template.
			int px = bm.getPixel(hMid, y);
			if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
				break;
			} 
			y2 = wfMid;
			x2++;
		}

		// Get rid of images that we finished with to save memory.
		img.recycle();
		templateImage.recycle();
		bm.recycle();
		return finalBm;
	}


	public static Bitmap cropImagetest(Bitmap img, Bitmap templateImage, int width, int height) {/*
		// Merge two images together.
		Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas combineImg = new Canvas(bm);
		combineImg.drawBitmap(img, 0f, 0f, null);
		combineImg.drawBitmap(templateImage, 0f, 0f, null);

		// Create new blank ARGB bitmap.
		Bitmap finalBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		// Get the coordinates for the middle of bm.
		int hMid = bm.getHeight() / 2;
		int wMid = bm.getWidth() / 2;
		int hfMid = finalBm.getHeight() / 2;
		int wfMid = finalBm.getWidth() / 2;

		int y2 = hfMid;
		int x2 = wfMid;
		System.out.println("y2 " + y2);

		for (int y = wMid; y >= 0; y--) {
			// Check Upper-left section of combineImg.
			for (int x = hMid; x >= 0; x--) {
				if (x2 < 0) {
					//                	System.out.println("loob na loob");
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the left side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					System.out.println("loob");
					break;
				} else {
					//                	System.out.println("labas  " + x2 + " " + y2);
					finalBm.setPixel(x2, y2, px);
				}
				x2--;
			}

			// Check upper-right section of combineImage.
			x2 = wfMid;
			for (int x = hMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the right side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {
					//                	System.out.println("labas2");
					//              	  Log.d("IMAGE CROP", "labas2  " + x2 + " " + y2);
					finalBm.setPixel(x2, y2, px);
				}
				x2++;
			}

			System.out.println(hMid + " : " + y);
			// Get out of loop once it hits top most part of the template.
			int px = bm.getPixel(hMid, y);
			//            int px = bm.getPixel(hMid, y);
			Log.d("IMAGE CROP", "////////////////////////////////////////////////////////");
			System.out.println("Color.red(px) " + Color.red(px) + "   ::::: 234");
			System.out.println("Color.green(px) " + Color.green(px) + "   ::::: 157");
			System.out.println("Color.blue(px) " + Color.blue(px) + "   ::::: 33");
			Log.d("IMAGE CROP", "////////////////////////////////////////////////////////");
			if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
				break;
			} 
			y2 = wfMid;
			x2--;
		}

		x2 = hfMid;
		y2 = wfMid;
		for (int y = wMid; y <= bm.getWidth(); y++) {
			// Check bottom-left section of combineImage.
			for (int x = hMid; x >= 0; x--) {
				if (y2 < 0) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the left side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {
					finalBm.setPixel(y2, x2, px);
				}
				y2--;
			}

			// Check bottom-right section of combineImage.
			y2 = wfMid;
			for (int x = hMid; x < bm.getHeight(); x++) {
				if (y2 >= finalBm.getWidth()) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the right side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				y2++;
			}

			// Get out of loop once it hits bottom most part of the template.
			int px = bm.getPixel(hMid, y);
			if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
				break;
			} 
			y2 = wfMid;
			x2++;
		}

		// Get rid of images that we finished with to save memory.
		img.recycle();
		templateImage.recycle();
		bm.recycle();
		return finalBm;
	 */


		Bitmap bmOverlay = Bitmap.createBitmap(img.getWidth(), img.getHeight(), img.getConfig());
		//resize your bmp2
		Bitmap resized = Bitmap.createScaledBitmap(templateImage, templateImage.getWidth(), templateImage.getHeight(), true);


		// Create new blank ARGB bitmap.
		Bitmap finalBm = Bitmap.createBitmap(318, 400, templateImage.getConfig());

		Canvas canvas = new Canvas(bmOverlay);
		canvas.drawBitmap(img, new Matrix(), null);

		// determine the width of the canvas
		int canvasWidth = canvas.getWidth();
		int canvasHeight = canvas.getHeight();  


		// determine the centre of the canvas
		int centreX = (canvasWidth  - resized .getWidth()) /2;
		int centreY = (canvasHeight - resized .getHeight()) /2; 

		// This code can be used to alter the opacity of the image being overlayed.
		//http://stackoverflow.com/a/12235235/1635441
		//http://stackoverflow.com/a/5119093/1635441        
		//Paint p = new Paint();
		//p.setXfermode(new PorterDuffXfermode(Mode.DST_ATOP)); //http://stackoverflow.com/a/17553502/1635441
		//p.setAlpha(180);
		//p.setARGB(a, r, g, b);

		//canvas.drawBitmap(resized, centreX, centreY, p);  

		//canvas.drawBitmap(bmp2, 0, 0, null);
		canvas.drawBitmap(resized, centreX, centreY, null);

		//	    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		//
		//	    bmOverlay.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
		//	    File f = new File(Environment.getExternalStorageDirectory()
		//	            + File.separator
		//	            + "test.jpg");
		//	    try {
		//	        f.createNewFile();
		//	        FileOutputStream fo = new FileOutputStream(f);
		//	        fo.write(bytes.toByteArray());
		//	    } catch (IOException e) {
		//	        e.printStackTrace();
		//	    }
		//		318, 400
		//		int[] pixels = new int[318 * 400];

		for(int i=0; i<resized.getWidth(); i++){
			for(int j=0; j<resized.getHeight(); j++){
				int p = resized.getPixel(i, j);
				//					finalBm.setPixels(p, 0, 318, 0, 0, 318, 400);
				if(j == 400)
				{
					break;
				}
				finalBm.setPixel(i, j, p);
			}
			if(i == 81)
			{
				break;
			}
		}

		return finalBm;
	}




	//	public static Bitmap cropImageVer2(Bitmap img, Bitmap templateImage, int width, int height) {
	//        // Merge two images together.
	//        Bitmap bm = Bitmap.createBitmap(img.getHeight(), img.getWidth(), Bitmap.Config.ARGB_8888);
	//        Canvas combineImg = new Canvas(bm);
	//        combineImg.drawBitmap(img, 0f, 0f, null);
	//        combineImg.drawBitmap(templateImage, 0f, 0f, null);
	//        
	//        // Create new blank ARGB bitmap.
	//        Bitmap finalBm = Bitmap.createBitmap(height, width, Bitmap.Config.ARGB_8888);
	//        
	//        // Get the coordinates for the middle of bm.
	//        int hMid = bm.getHeight() / 2;
	//        int wMid = bm.getWidth() / 2;
	//        int hfMid = finalBm.getHeight() / 2;
	//        int wfMid = finalBm.getWidth() / 2;
	//
	//        int x2 = hfMid;
	//        int y2 = wfMid;
	//
	//        for (int x = hMid; x >= 0; x--) {
	//            // Check Upper-left section of combineImg.
	//            for (int y = wMid; y >= 0; y--) {
	//                if (y2 < 0) {
	//                	System.out.println("loob na loob");
	//                    break;
	//                }
	//
	////                int px = bm.getPixel(yx, x);
	//                int px = bm.getPixel(x, y);
	//                // Get out of loop once it hits the left side of the template.
	//                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	//                	System.out.println("loob");
	//                    break;
	//                } else {
	//                	System.out.println("labas");
	//                    finalBm.setPixel(y2, x2, px);
	//                }
	//                y2--;
	//            }
	//            
	//            // Check upper-right section of combineImage.
	//            y2 = wfMid;
	//            for (int y = wMid; y < bm.getWidth(); y++) {
	//                if (y2 >= finalBm.getWidth()) {
	//                    break;
	//                }
	//
	////                int px = bm.getPixel(y, x);
	//                int px = bm.getPixel(x, y);
	//                // Get out of loop once it hits the right side of the template.
	//                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	//                    break;
	//                } else {
	//                    finalBm.setPixel(y2, x2, px);
	//                }
	//                y2++;
	//            }
	//            
	//            // Get out of loop once it hits top most part of the template.
	//            int px = bm.getPixel(wMid, x);
	//            if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	//                break;
	//            } 
	//            y2 = wfMid;
	//            x2--;
	//        }
	//
	//        y2 = wfMid;
	//        x2 = hfMid;
	//        for (int x = hMid; x <= bm.getHeight(); x++) {
	//            // Check bottom-left section of combineImage.
	//            for (int y = wMid; y >= 0; y--) {
	//                if (y2 < 0) {
	//                    break;
	//                }
	//
	////                int px = bm.getPixel(y, x);
	//                int px = bm.getPixel(x, y);
	//                // Get out of loop once it hits the left side of the template.
	//                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	//                    break;
	//                } else {
	//                    finalBm.setPixel(y2, x2, px);
	//                }
	//                y2--;
	//            }
	//
	//            // Check bottom-right section of combineImage.
	//            y2 = wfMid;
	//            for (int y = wMid; y < bm.getWidth(); y++) {
	//                if (y2 >= finalBm.getWidth()) {
	//                    break;
	//                }
	//                
	////                int px = bm.getPixel(y, x);
	//                int px = bm.getPixel(x, y);
	//                // Get out of loop once it hits the right side of the template.
	//                if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	//                    break;
	//                } else {
	//                    finalBm.setPixel(y2, x2, px);
	//                }
	//                y2++;
	//            }
	//            
	//            // Get out of loop once it hits bottom most part of the template.
	//            int px = bm.getPixel(wMid, x);
	//            if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
	//                break;
	//            } 
	//            y2 = wfMid;
	//            x2++;
	//        }
	//        
	//        // Get rid of images that we finished with to save memory.
	//        img.recycle();
	//        templateImage.recycle();
	//        bm.recycle();
	//        return finalBm;
	//    }

	public static Bitmap testing(Bitmap img, Bitmap templateImage)
	{
		//		int targetWidth = 125;
		//		int targetHeight = 125;
		//		Bitmap targetBitmap = Bitmap.createBitmap(targetWidth, 
		//				targetHeight,Bitmap.Config.ARGB_8888);
		//
		//		Canvas canvas = new Canvas(targetBitmap);
		//		Path path = new Path();
		//		path.addCircle(((float) targetWidth - 2) / 2,
		//				((float) targetHeight - 2) / 2,
		//				(Math.min(((float) targetWidth), 
		//						((float) targetHeight)) / 2),
		//						Path.Direction.CCW);
		//
		//		canvas.clipPath(path);
		//		Bitmap sourceBitmap = img;
		//		canvas.drawBitmap(sourceBitmap, 
		//				new Rect(0, 0, sourceBitmap.getWidth(),
		//						sourceBitmap.getHeight()), 
		//						new Rect(0, 0, targetWidth,
		//								targetHeight), null);
		//		return targetBitmap;

		Bitmap targetBitmap = Bitmap.createBitmap(480, 550, img.getConfig());
		Canvas comboImage = new Canvas(img);
		// Then draw the second on top of that
		comboImage.drawBitmap(templateImage, 0f, 0f, null);
		return targetBitmap;

	}

	public static Bitmap cropImageVer2(Bitmap img, Bitmap templateImage, int width, int height) {
		Log.e("IMAGE CROP", "THIS  " + width + " " + height);
		Log.e("IMAGE CROP", "THIS  " + img.getWidth() + " " + img.getHeight());
		// Merge two images together.
		Bitmap bm = Bitmap.createBitmap(480, 550, img.getConfig());
		Canvas combineImg = new Canvas(bm);
		combineImg.drawBitmap(img, 0f, 0f, null);
		combineImg.drawBitmap(templateImage, 0f, 0f, null);

		// Create new blank ARGB bitmap.
		Bitmap finalBm = Bitmap.createBitmap(318, 400, templateImage.getConfig());

		// Get the coordinates for the middle of bm.
		int hMid = bm.getHeight() / 2;
		int wMid = bm.getWidth() / 2;
		int hfMid = finalBm.getHeight() / 2;
		int wfMid = finalBm.getWidth() / 2;

		int y2 = hfMid;
		int x2 = wfMid;

		for (int y = hMid; y >= 0; y--) {
			// Check Upper-left section of combineImg.
			for (int x = wMid; x >= 0; x--) {
				if (x2 < 0) {
					System.out.println("loob na loob");
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the left side of the template.
				if (Color.red(px) == Color.RED && Color.green(px) == Color.GREEN && Color.blue(px) == Color.BLUE) {
					System.out.println("loob");
					break;
				} else {
					//	                	System.out.println("labas");
					try
					{
						finalBm.setPixel(x2, y2, px);
					}
					catch(IllegalArgumentException e)
					{
						break;
					}
				}
				x2--;
			}

			// Check upper-right section of combineImage.
			x2 = wfMid;
			for (int x = wMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					System.out.println("loob na loob2");
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the right side of the template.
				if (Color.red(px) == Color.RED && Color.green(px) == Color.GREEN && Color.blue(px) == Color.BLUE) {
					System.out.println("loob2");
					break;
				} else {
					try
					{
						finalBm.setPixel(x2, y2, px);
					}
					catch(IllegalArgumentException e)
					{
						break;
					}
				}
				x2++;
			}

			//			int test;
			int px = bm.getPixel(y, wMid);
			//			if(y >= 70)
			//			{
			//				test = -1401567;
			//				px = test;
			//			}
			// Get out of loop once it hits top most part of the template.
			//			Log.e("Image crop", wMid + " " + " " + y + " " + Color.red(px) + " " + Color.green(px) + " " + Color.blue(px));
			Log.w("Image crop", String.valueOf(px) + " " + y + " : " + wMid);
			if (Color.red(px) == Color.RED && Color.green(px) == Color.GREEN && Color.blue(px) == Color.BLUE) {
				Log.e("Image crop", String.valueOf(px));
				break;
			} 
			x2 = wfMid;
			y2--;
		}

		x2 = wfMid;
		y2 = hfMid;
		for (int y = hMid; y <= bm.getHeight(); y++) {
			// Check bottom-left section of combineImage.
			for (int x = wMid; x >= 0; x--) {
				if (x2 < 0) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the left side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {

					try
					{
						finalBm.setPixel(x2, y2, px);
					}
					catch(IllegalArgumentException e)
					{
						break;
					}
				}
				x2--;
			}

			// Check bottom-right section of combineImage.
			x2 = wfMid;
			for (int x = wMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the right side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {

					try
					{
						finalBm.setPixel(x2, y2, px);
					}
					catch(IllegalArgumentException e)
					{
						break;
					}
				}
				x2++;
			}

			//			int test;
			// Get out of loop once it hits bottom most part of the template.
			int px = bm.getPixel(y, wMid);
			//			if(y >= 390)
			//			{
			//				test = -1401567;
			//				px = test;
			//			}
			//			Log.e("Image crop", wMid + " 111 " + " " + y + " " + Color.red(px) + " " + Color.green(px) + " " + Color.blue(px));
			Log.d("Image crop", String.valueOf(px) + " " + y + " : " + wMid);
			if (Color.red(px) == Color.RED && Color.green(px) == Color.GREEN && Color.blue(px) == Color.BLUE) {
				Log.e("Image crop", String.valueOf(px));
				break;
			} 
			x2 = wfMid;
			y2++;
		}

		// Get rid of images that we finished with to save memory.
		img.recycle();
		templateImage.recycle();
		bm.recycle();
		return finalBm;
	}

	public static Bitmap cropImageVer2_1(Bitmap img, Bitmap templateImage, int width, int height) {
		// Merge two images together.
		Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas combineImg = new Canvas(bm);
		combineImg.drawBitmap(img, 0f, 0f, null);
		combineImg.drawBitmap(templateImage, 0f, 0f, null);

		// Create new blank ARGB bitmap.
		Bitmap finalBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

		// Get the coordinates for the middle of bm.
		int hMid = bm.getHeight() / 2;
		int wMid = bm.getWidth() / 2;
		int hfMid = finalBm.getHeight() / 2;
		int wfMid = finalBm.getWidth() / 2;

		int y2 = hfMid;
		int x2 = wfMid;

		for (int y = hMid; y >= 0; y--) {
			// Check Upper-left section of combineImg.
			for (int x = wMid; x >= 0; x--) {
				if (x2 < 0) {
					//	                	System.out.println("loob na loob");
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the left side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					//	                	System.out.println("loob");
					break;
				} else {
					//	                	System.out.println("labas");
					finalBm.setPixel(x2, y2, px);
				}
				x2--;
			}

			// Check upper-right section of combineImage.
			x2 = wfMid;
			for (int x = wMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the right side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2++;
			}

			// Get out of loop once it hits top most part of the template.
			int px = bm.getPixel(wMid, y);
			Log.w("Image crop", String.valueOf(px) + " " + y + " : " + wMid);
			if (Color.red(px) == Color.RED && Color.green(px) == Color.GREEN && Color.blue(px) == Color.BLUE) {
				Log.e("Image crop", String.valueOf(px));
				break;
			} 
			x2 = wfMid;
			y2--;
		}

		x2 = wfMid;
		y2 = hfMid;
		for (int y = hMid; y <= bm.getHeight(); y++) {
			// Check bottom-left section of combineImage.
			for (int x = wMid; x >= 0; x--) {
				if (x2 < 0) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the left side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2--;
			}

			// Check bottom-right section of combineImage.
			x2 = wfMid;
			for (int x = wMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}

				int px = bm.getPixel(x, y);
				// Get out of loop once it hits the right side of the template.
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					break;
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2++;
			}

			// Get out of loop once it hits bottom most part of the template.
			int px = bm.getPixel(wMid, y);
			Log.d("Image crop", String.valueOf(px) + " " + y + " : " + wMid);
			if (Color.red(px) == Color.RED && Color.green(px) == Color.GREEN && Color.blue(px) == Color.BLUE) {
				Log.e("Image crop", String.valueOf(px));
				break;
			} 
			x2 = wfMid;
			y2++;
		}

		// Get rid of images that we finished with to save memory.
		img.recycle();
		templateImage.recycle();
		bm.recycle();
		return finalBm;
	}


	public static Bitmap cropImage_NEW(Bitmap img, Bitmap templateImage, int width, int height) {
		// Merge two images together.
		Bitmap bm = Bitmap.createBitmap(img.getWidth(), img.getHeight(), Bitmap.Config.ARGB_8888);
		Canvas combineImg = new Canvas(bm);
		combineImg.drawBitmap(img, 0f, 0f, null);
		combineImg.drawBitmap(templateImage, 0f, 0f, null);
		// Create new blank ARGB bitmap.
		Bitmap finalBm = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
		// Get the coordinates for the middle of combineImg.
		int hMid = bm.getHeight() / 2;
		int wMid = bm.getWidth() / 2;
		int hfMid = finalBm.getHeight() / 2;
		int wfMid = finalBm.getWidth() / 2;
		int y2 = hfMid;
		int x2 = wfMid;
		// Top half of the template.
		for (int y = hMid; y >= 0; y--) {
			boolean template = false;
			// Check Upper-left section of combineImg.
			for (int x = wMid; x >= 0; x--) {
				if (x2 < 0) {
					break;
				}
				int px = bm.getPixel(x, y);
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					template = true;
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else if (template) {
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2--;
			}
			// Check upper-right section of combineImage.
			x2 = wfMid;
			template = false;
			for (int x = wMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}
				int px = bm.getPixel(x, y);
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					template = true;
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else if (template) {
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2++;
			}
			// Once we reach the top-most part on the template line, set pixel value transparent
			// from that point on.
			int px = bm.getPixel(wMid, y);
			if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
				for (int y3 = y2; y3 >= 0; y3--) {
					for (int x3 = 0; x3 < finalBm.getWidth(); x3++) {
						finalBm.setPixel(x3, y3, Color.TRANSPARENT);
					}
				}
				break;
			}
			x2 = wfMid;
			y2--;
		}
		x2 = wfMid;
		y2 = hfMid;
		// Bottom half of the template.
		for (int y = hMid; y <= bm.getHeight(); y++) {
			boolean template = false;
			// Check bottom-left section of combineImage.
			for (int x = wMid; x >= 0; x--) {
				if (x2 < 0) {
					break;
				}

				int px = bm.getPixel(x, y);
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					template = true;
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else if (template) {
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2--;
			}

			// Check bottom-right section of combineImage.
			x2 = wfMid;
			template = false;
			for (int x = wMid; x < bm.getWidth(); x++) {
				if (x2 >= finalBm.getWidth()) {
					break;
				}
				int px = bm.getPixel(x, y);
				if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
					template = true;
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else if (template) {
					finalBm.setPixel(x2, y2, Color.TRANSPARENT);
				} else {
					finalBm.setPixel(x2, y2, px);
				}
				x2++;
			}

			// Once we reach the bottom-most part on the template line, set pixel value transparent
			// from that point on.
			int px = bm.getPixel(wMid, y);
			if (Color.red(px) == 234 && Color.green(px) == 157 && Color.blue(px) == 33) {
				for (int y3 = y2; y3 < finalBm.getHeight(); y3++) {
					for (int x3 = 0; x3 < finalBm.getWidth(); x3++) {
						finalBm.setPixel(x3, y3, Color.TRANSPARENT);
					}
				}
				break;
			}

			x2 = wfMid;
			y2++;
		}
		return finalBm;
	} 
}
