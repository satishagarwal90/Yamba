package com.example.yamba;

import android.app.ListActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.ViewBinder;
import android.widget.TextView;

public class Timeline extends ListActivity{
	
	//ListView listView;
	SimpleCursorAdapter adapter;
	String TAG = "TimelineActivity";	
	String[] from = {StatusData.C_User, StatusData.C_Text, StatusData.C_Created_At};
	int[] to = {R.id.textUsername,R.id.textTweet, R.id.textDetails};
	
	TimelineReceiver receiver;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Cursor cursor = ((YambaApp)(getApplication())).statusData.query();
		adapter = new SimpleCursorAdapter(this, R.layout.row, cursor, from, to,0);
		adapter.setViewBinder(VIEW_BINDER);
		//listView.setAdapter(adapter);
		setListAdapter(adapter);
		setTitle(TAG);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(receiver == null){
			receiver = new TimelineReceiver();
			registerReceiver(receiver, new IntentFilter(YambaApp.REFRESH_STATUS));
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		unregisterReceiver(receiver);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		Intent intent = new Intent(this,UpdaterService.class);
		Intent rIntent = new Intent(this,RefreshService.class);
		switch (id) {
		case R.id.item_start_service:
			Log.d(TAG, "Service Started");
			startService(intent);
			return true;
		case R.id.item_stop_service:
			Log.d(TAG, "Service Stopped");
			stopService(intent);
			return true;
		case R.id.item_refresh_service:
			Log.d(TAG, "Service Refreshed");
			startService(rIntent);
			return true;
		case R.id.item_Preferences:
			Log.d(TAG, "Inside Preferences");
			startActivity(new Intent(this,PrefsActivity.class));
			return true;
		case R.id.item_statusUpdate:
			Log.d(TAG,"Inside Status Update");
			startActivity(new Intent(this,StatusActivity.class));
			return true;

		default:
			return false;
		}
	}

	
	static final ViewBinder VIEW_BINDER = new ViewBinder() {
		
		@Override
		public boolean setViewValue(View view, Cursor cursor, int columnIndex) {
			if(view.getId() != R.id.textDetails) return false;
			
			long time = cursor.getLong(cursor.getColumnIndex(StatusData.C_Created_At));
			CharSequence relativeTime = DateUtils.getRelativeTimeSpanString(time);
			((TextView)view).setText(relativeTime);
			return true;
		}
	};
	
	public class TimelineReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Cursor temp = ((YambaApp)(getApplication())).statusData.query();
			adapter.changeCursor(temp);
			Log.d(TAG,"The value of count in receiver is :" + intent.getIntExtra("count", 0));
		}
		
	}

}
