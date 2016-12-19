package com.ntek.doortalk2.led;

public class Doortalkled {

	public native int setButtonLED(String param);

	static {
		System.loadLibrary("DT2led");
	}

}
