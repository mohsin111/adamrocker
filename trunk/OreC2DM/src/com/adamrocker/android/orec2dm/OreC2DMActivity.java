package com.adamrocker.android.orec2dm;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class OreC2DMActivity extends Activity implements OnClickListener {
	
    private static final String NICKNAME = "nickname";
	private EditText mName;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        ((Button)findViewById(R.id.start_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.stop_btn)).setOnClickListener(this);
        mName = (EditText)findViewById(R.id.name_et);
        String nickname = getNickname(this);
        if (nickname != null && nickname.length() > 0) {
        	mName.setText(nickname);
        }
    }

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.start_btn:
			startService();
			break;
		case R.id.stop_btn:
			stopService();
			break;
		}
	}
	
	private void startService() {
		String nickname = mName.getText().toString();
		if (nickname != null && nickname.length() > 0) {
			saveNickname(nickname);
			mName.setEnabled(false);
			Intent service = new Intent();
			service.setClass(this, OreC2DMService.class);
			startService(service);
			showStatusLive(nickname);
		}
	}
	
	private void stopService() {
		Intent service = new Intent();
		service.setClass(this, OreC2DMService.class);
		stopService(service);
		showStatusDead();
		mName.setEnabled(true);
	}
	
	public static String getNickname(Context context) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        String nickname = sp.getString(NICKNAME, null);
        return nickname;
	}
	
	private void saveNickname(String nickname) {
		Editor edit = PreferenceManager.getDefaultSharedPreferences(this).edit();
        edit.putString(NICKNAME, nickname);
        edit.commit();
	}
	
	private void showStatusLive(String name) {
		String status = getString(R.string.live) + "/" + name;
		((TextView)findViewById(R.id.status_tv)).setText(status);
	}
	
	private void showStatusDead() {
		((TextView)findViewById(R.id.status_tv)).setText(R.string.dead);
	}
}