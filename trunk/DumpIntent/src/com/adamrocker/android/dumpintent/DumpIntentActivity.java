package com.adamrocker.android.dumpintent;

import java.util.Set;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class DumpIntentActivity extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView tv = (TextView) findViewById(R.id.body_tv);
		Intent it = getIntent();
		if (it == null) {
			finish();
		} else {
			StringBuilder sb = new StringBuilder();
			String act = it.getAction();
			sb.append("ACTION:\n").append(act).append("\n----------\n");
			Set<String> cats = it.getCategories();
			sb.append("CATEGORY:\n");
			if (cats != null) {
				for (String cat : cats) {
					sb.append(cat).append("\n");
				}
			}
			sb.append("----------\n");
			int flag = it.getFlags();
			sb.append("FLAG:0x").append(Integer.toHexString(flag));
			sb.append("\n----------\n");
			sb.append("URI:");
			Uri uri = it.getData();
			if (uri != null) {
				sb.append(uri.toString());
			}
			sb.append("\n----------\n");
			sb.append("TYPE:");
			String type = it.getType();
			if (type != null) {
				sb.append(type);
			}
			sb.append("\n----------\n");
			ComponentName comp = it.getComponent();
			sb.append("PACKAGE:").append(comp.getPackageName());
			sb.append("CLASS:").append(comp.getClassName());
			sb.append("\n----------\n");
			sb.append("EXTRAS:\n");
			Bundle b = it.getExtras();
			
			if (b != null) {
				Set<String> keys = b.keySet();
				if (keys != null && 0 < keys.size()) {
					for (String key : keys) {
						sb.append(key).append(":").append(b.get(key)).append("\n");
					}
				}
			}
			sb.append("\n----------\n");
			
			tv.setText(sb.toString());
		}
	}
}