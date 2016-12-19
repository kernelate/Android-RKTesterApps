package com.ntek.smartentry.mcucontrol;

public class McuJni {
		
	public native void relay0Enable (int onOff);
	public native void relay1Enable (int onOff);
	public native void lightEnable (int onOff);
	public native void echoEnable (int onOff);
	public native void redLedEnable (int onOff);
	public native void greenLedEnable (int onOff);
	
	static {
		System.loadLibrary("SmartEntryMcuControl");
	 }
}
