package com.example.yamba;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

public class RefreshService extends IntentService {

	public static String TAG = "RefershService";

	public RefreshService() {
		super(TAG);
	}

	@Override
	protected void onHandleIntent(Intent arg0) {
		try {
			((YambaApp) (getApplication())).PullandInsert();
		} catch (Exception e) {
			Log.e(TAG, "Refresh Service Exception", e);
		}
	}

}
