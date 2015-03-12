package com.maitrey.subhashit.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;


public class DataService extends Service{

	private static boolean isRunning = false;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		isRunning = false;
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		if(!isRunning){
			isRunning = true;
			DataStore.initFiles(this);
			isRunning = false;
		}
		return super.onStartCommand(intent, flags, startId);
	}
	
	public static boolean isRunning(){
		return isRunning;
	}
}
