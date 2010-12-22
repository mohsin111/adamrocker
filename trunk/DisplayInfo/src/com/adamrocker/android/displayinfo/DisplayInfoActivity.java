package com.adamrocker.android.displayinfo;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.TextView;

public class DisplayInfoActivity extends Activity {
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView tv = (TextView) findViewById(R.id.info_tv);
		DisplayMetrics metrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(metrics);
		StringBuffer sb = new StringBuffer();
		sb.append("width=").append(metrics.widthPixels).append("\n");
		sb.append("height=").append(metrics.heightPixels).append("\n");

		sb.append("density=").append(metrics.density).append("\n");
		sb.append("densityDpi=").append(metrics.densityDpi).append("\n");
		sb.append("scaledDensity=").append(metrics.scaledDensity).append("\n");
		sb.append("xDpi=").append(metrics.xdpi).append("\n");
		sb.append("yDpi=").append(metrics.ydpi).append("\n");
		tv.setText(sb.toString());
	}
}