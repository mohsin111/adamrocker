package com.adamrocker.android.mynfc;

import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.nfc.NdefMessage;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class MyNFCActivity extends Activity implements OnClickListener {
	private Parcelable mAndroidNfcTag;

	public void onClick(View v) {
		try {
			NfcAdapter adapter = NfcAdapter.getDefaultAdapter();
			Class tag = Class.forName("android.nfc.Tag");
			Method meth = adapter.getClass().getMethod(
					"createRawTagConnection", tag);
			Object RawTagConnection = meth.invoke(adapter, mAndroidNfcTag);
			Object mTagService = getDeclaredField(RawTagConnection,	"mTagService");
			Integer mServiceHandle = (Integer) getDeclaredField(mAndroidNfcTag,
					"mServiceHandle");
			Method INfcTag_connect = mTagService.getClass().getMethod(
					"connect", Integer.TYPE);
			/*-- CONNECT --*/
			Object flag = INfcTag_connect.invoke(mTagService,
					mServiceHandle.intValue());
			
			/*-- CREATE NdefRecord --*/
			Class NdefRecord_class  = Class.forName("android.nfc.NdefRecord");
			// <init> public android.nfc.NdefRecord(short,byte[],byte[],byte[])
			Constructor[] cons = NdefRecord_class.getConstructors();
			Constructor NdefRecord_init = null;
			for (Constructor c : cons) {
				String con = c.toString();
				if (con.equals("public android.nfc.NdefRecord(short,byte[],byte[],byte[])")) {
					NdefRecord_init = c;
					break;
				}
			}
			short tnf = 0;
			byte[] type = new byte[2];
			byte[] id = new byte[2];
			byte[] payload = new byte[2];
			Object[] NdefRecords = new Object[3];
			Object NdefRecord_Array = Array.newInstance(NdefRecord_class, 3);
			NdefRecords[0] = NdefRecord_init.newInstance(tnf, type, id, payload);
			NdefRecords[1] = NdefRecord_init.newInstance(tnf, type, id, payload);
			NdefRecords[2] = NdefRecord_init.newInstance(tnf, type, id, payload);
			Array.set(NdefRecord_Array, 0, NdefRecords[0]);
			Array.set(NdefRecord_Array, 1, NdefRecords[1]);
			Array.set(NdefRecord_Array, 2, NdefRecords[2]);
			
			/*-- CREATE NdefMessage --*/
			Class NdefMessage_class  = Class.forName("android.nfc.NdefMessage");
			cons = NdefMessage_class.getConstructors();
			Constructor NdefMessage_init = null;
			for (Constructor c : cons) {
				String con = c.toString();
				if (con.equals("public android.nfc.NdefMessage(android.nfc.NdefRecord[])")) {
					NdefMessage_init = c;
					break;
				}
			}
			Object NdefMessage = NdefMessage_init.newInstance(NdefRecord_Array);
			
			/*-- WRITE --*/
			Method INfcTag_write = mTagService.getClass().getMethod("write", Integer.TYPE, NdefMessage_class);
			INfcTag_write.invoke(mTagService, mServiceHandle, NdefMessage);
			//DUMP("CONNECTED:" + flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		TextView tv = (TextView) findViewById(R.id.result_tv);
		resolveIntent(getIntent(), tv);
		((Button) findViewById(R.id.send_btn)).setOnClickListener(this);
	}

	private void resolveIntent(Intent it, TextView tv) {
		String action = it.getAction();
		Bundle b = it.getExtras();

		Set<String> ks = b.keySet();
		int count = 0;
		for (String s : ks) {
			Log.i("MyNFC", "KEY[" + count++ + "]=" + s);
		}
		mAndroidNfcTag = it.getParcelableExtra("android.nfc.extra.TAG");
		byte[] bs = it.getByteArrayExtra(NfcAdapter.EXTRA_ID);
		LOG("IDS", getHex(bs));
		try {
			dump(mAndroidNfcTag, tv);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Log.i("MyNFC", it.toUri(-1));
		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			Parcelable[] rawMsgs = it
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
			NdefMessage[] msgs;
			if (rawMsgs != null) {
				msgs = new NdefMessage[rawMsgs.length];
				for (int i = 0; i < rawMsgs.length; i++) {
					msgs[i] = (NdefMessage) rawMsgs[i];
				}
			}
		}
	}

	private void dump(Parcelable p, TextView tv) throws SecurityException,
			IllegalArgumentException,
			IllegalAccessException {
		StringBuilder sb = new StringBuilder();
		String name = p.getClass().getCanonicalName();
		LOG("ClassName", name);
		Field f;
		Class tag = p.getClass();
		try {
		f = tag.getDeclaredField("mIsNdef");
		f.setAccessible(true);
		Boolean mIsNdef = (Boolean) f.get(p);
		LOG("mIsNdef", mIsNdef.toString());
		sb.append("mIsNdef:").append(mIsNdef.toString()).append("\n");
		} catch (Exception e) {
			sb.append("mIsNdef:").append("UnKnown\n");
			e.printStackTrace();
		}
		
		try {
		f = tag.getDeclaredField("mId");
		f.setAccessible(true);
		byte[] mId = (byte[]) f.get(p);
		String MID = getHex(mId);
		sb.append("mId:").append(MID).append("\n");
		} catch (Exception e) {
			sb.append("mId:").append("UnKnown\n");
			e.printStackTrace();
		}
 

		try {
		f = tag.getDeclaredField("mRawTargets");
		f.setAccessible(true);
		String[] mRawTargets = (String[]) f.get(p);
		sb.append("mRawTargets:");
		for(String s : mRawTargets) {
			sb.append(s).append(".");
		}
		sb.append("\n");
		} catch (Exception e) {
			sb.append("UnKnown\n");
			e.printStackTrace();
		}


		try {
		f = tag.getDeclaredField("mPollBytes");
		f.setAccessible(true);
		byte[] mPollBytes = (byte[]) f.get(p);
		String POLL = getHex(mPollBytes);
		sb.append("mPollBytes:").append(POLL).append("\n");
		} catch (Exception e) {
			sb.append("mPollBytes:").append("UnKnown\n");
			e.printStackTrace();
		}

		try {
		f = tag.getDeclaredField("mActivationBytes");
		f.setAccessible(true);
		byte[] mActivationBytes = (byte[]) f.get(p);
		String ACTIV = getHex(mActivationBytes);
		sb.append("mActivationBytes:").append(ACTIV);
		} catch (Exception e) {
			sb.append("mActivationBytes:").append("UnKnown\n");
			e.printStackTrace();
		}

		try {
		f = tag.getDeclaredField("mServiceHandle");
		f.setAccessible(true);
		Integer mServiceHandle = (Integer) f.get(p);
		sb.append("mServiceHandle").append(mServiceHandle.intValue()).append("\n");
		} catch (Exception e) {
			sb.append("mServiceHandle:").append("UnKnown\n");
			e.printStackTrace();
		}

		LOG("FIN", "TEST");
		tv.setText(sb.toString());
		NfcAdapter(p);
	}

	private String getHex(byte[] bs) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < bs.length; i++) {
			int b = new Byte(bs[i]).intValue();
			sb.append(Integer.toHexString(b).toString());
			if (i != bs.length - 1) {
				sb.append(".");
			}
		}
		return sb.toString();
	}

	private void NfcAdapter(Object o) {
		try {
			NfcAdapter nfc = NfcAdapter.getDefaultAdapter();
			Method[] ms = nfc.getClass().getMethods();
			for (Method m : ms) {
				LOG(m.getName(), m.toString());
			}
			Class tag = Class.forName("android.nfc.Tag");
			Method meth = nfc.getClass().getMethod("createRawTagConnection",
					tag);
			dumpMethod(o);
			Object RawTagConnection = meth.invoke(nfc, o);
			// Object mTagService = obj.getClass().getField("mTagService");
			dumpField(RawTagConnection);
			Object mTagService = getDeclaredField(RawTagConnection,
					"mTagService");
			Object mTag = getDeclaredField(RawTagConnection, "mTag");
			dumpMethod(mTagService);//
			Integer mServiceHandle = (Integer) getDeclaredField(o,
					"mServiceHandle");
			Method INfcTag_connect = mTagService.getClass().getMethod(
					"connect", Integer.TYPE);
			Object flag = INfcTag_connect.invoke(mTagService,
					mServiceHandle.intValue());
			LOG("NfcAdapter", "TEST:" + flag);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private Object getDeclaredField(Object obj, String name)
			throws SecurityException, NoSuchFieldException,
			IllegalArgumentException, IllegalAccessException {
		Field f = obj.getClass().getDeclaredField(name);
		f.setAccessible(true);
		return f.get(obj);
	}

	private void dumpField(Object obj) {
		Field[] fs = obj.getClass().getDeclaredFields();
		int i = 0;
		for (Field f : fs) {
			DUMP(obj.getClass().getName() + "'s Fields[" + i++ + "] = "
					+ f.getName());
		}
	}

	private void dumpMethod(Object obj) {
		Method[] ms = obj.getClass().getDeclaredMethods();
		int i = 0;
		for (Method m : ms) {
			DUMP(obj.getClass().getName() + "'s Meyhodes[" + i++ + "]"
					+ m.toString());
		}
	}

	private void LOG(String key, String val) {
		DUMP(key + " = " + val);
	}

	private void DUMP(String val) {
		Log.i("MyNFC", val);
	}
}