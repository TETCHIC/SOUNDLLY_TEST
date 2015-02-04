package own.tetchi.apps.soundl.ly.sample;

import own.tetchi.apps.soundl.ly.sample.tools.Log;
import own.tetchi.apps.soundl.ly.sample.tools.UIStatic;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.internal.pm;
import com.soundlly.sdk.Soundlly;
import com.soundlly.sdk.SoundllyCore;
import com.soundlly.sdk.service.SoundllyService;

public class MainActivity extends Activity {

	final String TAG = "MainActivity";

	private Soundlly mSoundlly = null;
	protected String crr_url = null;

	SoundlyBroadcastReceiver mReceiver = null;

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub

			switch (msg.what) {

			case UIStatic.SERVICE_BIND:
				// ���͸�ũ Ž�� ����
				setListenWatermark(true);

				final TextView textField = (TextView) findViewById(R.id.text_field);
				textField.setText(R.string.status_recv);
				break;

			case Soundlly.CODE_OK:
				Bundle data = msg.getData();
				String url = data.getString("url");
				if (crr_url == null || !url.equals(crr_url)) {
					crr_url = url;
					loadUrl(url);
				}
				break;

			case Soundlly.CODE_NO_CONTENTS:

				Toast.makeText(getApplicationContext(),
						R.string.code_no_contents, 1000);
				break;

			case Soundlly.CODE_SERVER_ERROR:
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("ERROR")
						.setMessage(R.string.code_server_error)
						.setPositiveButton(android.R.string.ok,
								new OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										mSoundlly.unbindSoundllyService();
										finish();
									}
								}).show();
				break;

			case Soundlly.CODE_TIME_OUT:
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("ERROR")
						.setMessage(R.string.code_time_out)
						.setPositiveButton(android.R.string.ok,
								new OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										mSoundlly.unbindSoundllyService();
										finish();
									}
								}).show();
				break;

			case Soundlly.CODE_UNAUTHORIZED:
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("ERROR")
						.setMessage(R.string.code_unauthorized)
						.setPositiveButton(android.R.string.ok,
								new OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										mSoundlly.unbindSoundllyService();
										MainActivity.this.finish();

									}
								}).show();
				break;

			case Soundlly.CODE_UNKNOWN_ERROR:
				Toast.makeText(getApplicationContext(),
						R.string.code_unknown_error, 1000);
				break;

			case Soundlly.CODE_NO_WATERMARK:
				Toast.makeText(getApplicationContext(),
						R.string.code_no_watermark, 1000);
				break;

			case Soundlly.CODE_MIC_ERROR:
				new AlertDialog.Builder(MainActivity.this)
						.setTitle("ERROR")
						.setMessage(R.string.code_mic_error)
						.setPositiveButton(android.R.string.ok,
								new OnClickListener() {

									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
										mSoundlly.unbindSoundllyService();
										finish();
									}
								}).show();
				break;
			}

			super.handleMessage(msg);
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		final TextView textField = (TextView) findViewById(R.id.text_field);

		mReceiver = new SoundlyBroadcastReceiver(handler);
		IntentFilter filter = new IntentFilter();
		filter.addAction(getApplicationContext().getPackageName()
				+ SoundllyService.ACTION_RESULT);
		filter.addAction(getApplicationContext().getPackageName()
				+ SoundllyService.ACTION_ON_BIND);
		registerReceiver(mReceiver, filter);

		textField.setText(R.string.status_init);
		// Soundlly ��ü �ʱ�ȭ
		mSoundlly = SoundllyCore.getSoundlly();
		Log.d(TAG, "Soundlly ��ü �ʱ�ȭ");
		// // ������ ���
		if (UIStatic.TEST_MODE) {
			mSoundlly.setDeveoloperMode();
			Log.d(TAG, "������ ��� Ȱ��ȭ");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		// ���� ���ε�
		mSoundlly.bindSoundllyService();

		final TextView textField = (TextView) findViewById(R.id.text_field);
		textField.setText(R.string.status_bind);
		Log.d(TAG, "���� ���ε�");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		// ���� ���ε��� �����Ѵ�.
		mSoundlly.unbindSoundllyService();
		Log.d(TAG, "���� ���ε� ����");

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub

		unregisterReceiver(mReceiver);

		super.onDestroy();
	}

	private void setListenWatermark(boolean isListen) {
		// ���񽺷� ���� ���͸�ũ ���� ���θ� �����Ѵ�.
		mSoundlly.setListenWatermark(UIStatic.SOUNDLLY_SDK_API_KEY, isListen);
		Log.d(TAG, " ���͸�ũ ���� ����");
	}

	protected void loadUrl(String url) {
		// url �ε�
		final WebView webView = (WebView) findViewById(R.id.web_field);
		final TextView textField = (TextView) findViewById(R.id.text_field);
		if (webView.getVisibility() == View.GONE) {
			webView.setVisibility(View.VISIBLE);
			textField.setVisibility(View.GONE);
			webView.setWebViewClient(new CustomWebViewClient());
		}

		webView.getSettings().setJavaScriptEnabled(true);
		webView.loadUrl(url);
	}

	private class CustomWebViewClient extends android.webkit.WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			// TODO Auto-generated method stub
			view.loadUrl(url);
			return true;
		}
	}
}
