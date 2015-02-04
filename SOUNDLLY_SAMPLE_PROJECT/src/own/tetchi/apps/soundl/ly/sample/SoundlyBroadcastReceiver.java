package own.tetchi.apps.soundl.ly.sample;

import java.util.ArrayList;

import own.tetchi.apps.soundl.ly.sample.tools.Log;
import own.tetchi.apps.soundl.ly.sample.tools.UIStatic;

import com.soundlly.sdk.Soundlly;
import com.soundlly.sdk.SoundllyCore;
import com.soundlly.sdk.net.model.AttributesModel;
import com.soundlly.sdk.net.model.ContentsModel;
import com.soundlly.sdk.service.SoundllyService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SoundlyBroadcastReceiver extends BroadcastReceiver {

	final String TAG = "SoundlyBroadcastReceiver";

	public String url = "";
	public String comment = "";
	public String code = "";

	Handler mHandler = null;

	public SoundlyBroadcastReceiver() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SoundlyBroadcastReceiver(Handler handler) {
		mHandler = handler;
		Log.d(TAG, "리시버 활성화");
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		// TODO Auto-generated method stub

		String action = intent.getAction();
		Log.d(TAG, action);
		if (action != null) {
			if ((context.getPackageName() + SoundllyService.ACTION_RESULT)
					.equals(action)) {
				handleSoundllyResult(intent);
			} else if ((context.getPackageName() + SoundllyService.ACTION_ON_BIND)
					.equals(action)) {
				Log.d(TAG, "서비스 바인드");
				Soundlly soundlly = SoundllyCore.getSoundlly();
				soundlly.setApplicationId(UIStatic.SOUNDLLY_SDK_API_KEY);
				mHandler.sendEmptyMessage(UIStatic.SERVICE_BIND);
			}
		}
	}

	private void handleSoundllyResult(Intent intent) {
		int resultCode = intent.getExtras().getInt(Soundlly.EXTRA_STATUS_CODE);

		switch (resultCode) {
		case Soundlly.CODE_OK:
			loadContent(intent);
			break;

		case Soundlly.CODE_NO_CONTENTS:
			mHandler.sendEmptyMessage(resultCode);
			break;

		case Soundlly.CODE_SERVER_ERROR:
			mHandler.sendEmptyMessage(resultCode);
			break;

		case Soundlly.CODE_TIME_OUT:
			mHandler.sendEmptyMessage(resultCode);
			break;

		case Soundlly.CODE_UNAUTHORIZED:
			mHandler.sendEmptyMessage(resultCode);
			break;

		case Soundlly.CODE_UNKNOWN_ERROR:
			mHandler.sendEmptyMessage(resultCode);
			break;

		case Soundlly.CODE_NO_WATERMARK:
			mHandler.sendEmptyMessage(resultCode);
			break;

		case Soundlly.CODE_MIC_ERROR:
			mHandler.sendEmptyMessage(resultCode);
			break;
		}
	}

	private void loadContent(Intent intent) {
		ContentsModel content = intent
				.getParcelableExtra(Soundlly.EXTRA_CONTENTS);
		ArrayList<AttributesModel> attrs = content.getAttributes();

		if (attrs != null) {
			for (AttributesModel model : attrs) {
				if (model.getType().equals("string")) {
					if (model.getKey().equals("url")) {
						url = model.getValue();

						Log.d(TAG, "url :: " + url);
						loadUrl(url);
					} else {
						comment = model.getValue();

						Log.d(TAG, "comment :: " + comment);
					}

				} else if (model.getType().equals("integer")) {
					if (model.getKey().equals("code")) {
						code = model.getValue();

						Log.d(TAG, "code :: " + code);
					}
				}
			}
		}
	}

	private void loadUrl(String url) {

		Message msg = new Message();
		msg.what = Soundlly.CODE_OK;
		Bundle data = new Bundle();
		data.putString("url", url);
		msg.setData(data);
		mHandler.sendMessage(msg);
	}

}
