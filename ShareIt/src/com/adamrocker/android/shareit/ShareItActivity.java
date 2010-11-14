package com.adamrocker.android.shareit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShareItActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent it = getIntent();
        if (it != null) {
        	finish();
        } else {
        	handleAction(it);
        }
    }
    
    private void handleAction(Intent it) {
    	String act = it.getAction();
    	if (Intent.ACTION_CALL.equals(act)) {
    		
    	} else if (Intent.ACTION_SEND.equals(act)) {
    		
    	} else if (Intent.ACTION_SENDTO.equals(act)) {
    		
    	} else if (Intent.ACTION_VIEW.equals(act)) {

    	}
    	System.out.println(act);
    }
}