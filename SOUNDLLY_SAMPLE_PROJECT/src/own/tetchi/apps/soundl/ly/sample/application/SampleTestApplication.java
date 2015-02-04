package own.tetchi.apps.soundl.ly.sample.application;

import own.tetchi.apps.soundl.ly.sample.tools.Log;

import com.soundlly.sdk.SoundllyCore;

import android.app.Application;

public class SampleTestApplication extends Application {
	
	final String TAG = "SampleTestApplication";
	
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		//Soundly SDK 초기화
		SoundllyCore.init(this);
		Log.d(TAG, "Soundly SDK 초기화");
	}
}
