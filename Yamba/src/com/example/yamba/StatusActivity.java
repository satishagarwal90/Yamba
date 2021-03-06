package com.example.yamba;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class StatusActivity extends Activity implements OnClickListener {
	
	static final String TAG = "StatusActivity"; 
	Button updateButton;
	EditText txtViewUpdate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_status);
		
		updateButton = (Button) findViewById(R.id.button_post);
		txtViewUpdate = (EditText) findViewById(R.id.editText_status);
		updateButton.setOnClickListener(this);
	}	
	
	@Override
	public void onClick(View v) {
		Log.d(TAG, "On Clicked!");	
		String status_text = txtViewUpdate.getText().toString();
		new PostToTwitter().execute(status_text);
	}

	class PostToTwitter extends AsyncTask<String, Void, String> {
	
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			Toast.makeText(StatusActivity.this, result, Toast.LENGTH_LONG).show();
		}
	
		@Override
		protected String doInBackground(String... params) {
			try {
				((YambaApp)(getApplication())).getJtwit().setStatus(params[0]);
				return "Successfully posted"+params[0];
			} catch (Exception e) {
				Log.d("doInBackground", "UpdateStatusError", e);
				return "Exception Occurred"+params[0];
			}
		}
		
	}
}
