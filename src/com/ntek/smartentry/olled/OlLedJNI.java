package com.ntek.smartentry.olled;

public class OlLedJNI {
	//declare functions prototype here...	

	public native void OnlineLedOn (int onOff);
	static {
		System.loadLibrary("OnlineLedJni");  
	 }
}
