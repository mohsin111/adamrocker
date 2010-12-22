package com.adamrocker.android.orec2dm;

import java.net.URLDecoder;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.webkit.WebView;
import static com.adamrocker.android.orec2dm.Log.*;

public class OreC2DMService extends Service {
	private static final String URI = "https://ore-c2dm.appspot.com/client";
	private WebView mWebView;

	private void showNotification(String msg) {
		NotificationManager nman = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		Notification notif = new Notification(android.R.drawable.btn_star_big_on, msg,
				System.currentTimeMillis());
		Intent it = new Intent(Intent.ACTION_MAIN);
		it.setClass(this, OreC2DMActivity.class);
		PendingIntent pit = PendingIntent.getActivity(this, 0, it, 0);
		notif.setLatestEventInfo(getApplicationContext(),
				getString(R.string.app_name), "Ore-Cloud To Device Messaging",
				pit);
		notif.flags = Notification.FLAG_ONLY_ALERT_ONCE;
		nman.notify(R.string.app_name, notif);
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		LogI("Service.onCreate");
		mWebView = new WebView(this);
		mWebView.getSettings().setJavaScriptEnabled(true);// turn on js
		mWebView.addJavascriptInterface(new JS(), "android");
		String nickname = OreC2DMActivity.getNickname(this);
		if (nickname != null && nickname.length() > 0) {
			mWebView.loadUrl(URI + "?id=" + nickname);
		} else {
			showNotification("Ore-C2DM::Invalid Nickname");
		}
	}

	@Override
	public void onStart(Intent it, int id) {
		super.onStart(it, id);
		LogI("Service.onStart");
		showNotification("Ore-C2DM::Start");
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		LogI("Service.onDestroy");
		mWebView.destroy();
		showNotification("Ore-C2DM::Destroy");
	}

	public class JS {
		public void send(String str) {
			LogD(str);
			String s = URLDecoder.decode(str);
			Intent it = new Intent(Intent.ACTION_VIEW, Uri.parse(s));
			it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(it);
		}
	}
}