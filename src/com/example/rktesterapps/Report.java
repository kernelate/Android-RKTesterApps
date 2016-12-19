package com.example.rktesterapps;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

public class Report extends AsyncTask<ArrayList<String>, String, ArrayList<String>> {

	private static final String TAG = "REPORT";
	private Report reportTask = null;
	/**
	 * Harcoded port number, platformservice server port is currently 5002
	 */
	private static final int SOCKET_PORT = 5001;
	/**
	 * IP address if used within device
	 */
	private static final String SOCKET_ADDRESS = "127.0.0.1";

	protected ArrayList<String> doInBackground(ArrayList<String>... passing) {
		ArrayList<String> result = new ArrayList<String>();
		ArrayList<String> pass = passing[0]; // get passed arraylist
		String source = pass.get(0);
		String dest = pass.get(1);
		String com = pass.get(2);
		String str = "";
		String cmd = null;
		cmd = "PLATFORM:" + com + ":" + source + ":" + dest + ":END";
		// cmd = "PLATFORM:CMD_MOVE_FILE:"+source+":"+dest+":END";
		publishProgress(cmd);
		try {
			Socket socket = new Socket();
			InetAddress inetAddress = null;
			SocketAddress socketAddr = new InetSocketAddress(inetAddress, SOCKET_PORT);
			PrintWriter out;
			inetAddress = InetAddress.getByName(SOCKET_ADDRESS);
			socket.connect(socketAddr, 7000);
			out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
			out.println(cmd);
			BufferedReader in;
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

			while (!socket.isClosed()) {
				if (in.ready()) {
					str = in.readLine();
				}

				if (!TextUtils.isEmpty(str)) {
					publishProgress(str);
					in.close();
					socket.close();
				}
			}

		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return result; // return result
	}

	@Override
	protected void onProgressUpdate(String... messages) {
		if (!TextUtils.isEmpty(messages[0]))
			Log.d(TAG, "Position: " + messages[0]);
	}

	@Override
	protected void onPostExecute(ArrayList<String> result) {
		Log.d(TAG, "done: " + result);
		reportTask = null;
	}

	@Override
	protected void onPreExecute() {
		Log.d(TAG, "Creating Socket connection...");

	}

}
