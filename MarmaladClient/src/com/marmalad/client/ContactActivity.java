package com.marmalad.client;

import com.marmalad.client.utils.Utils;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.MatrixCursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Spinner;

public class ContactActivity extends Activity {
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Set layout
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		setContentView(R.layout.session_initiate);

		// Set title
		setTitle(R.string.menu_initiate_session);
		
		// Set contact selector
		Spinner spinner = (Spinner)findViewById(R.id.contact);
		spinner.setAdapter(Utils.createContactListAdapter(this));

		// Set buttons callback
		Button initiateBtn = (Button)findViewById(R.id.initiate_btn);
		initiateBtn.setOnClickListener(btnInitiateListener);

        // Disable button if no contact available
        if (spinner.getAdapter().getCount() == 0) {
        	initiateBtn.setEnabled(false);
        }
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	/**
	 * Initiate button callback
	 */
	private OnClickListener btnInitiateListener = new OnClickListener() {
		public void onClick(View v) {
			// Get remote contact
			Spinner spinner = (Spinner)findViewById(R.id.contact);
			MatrixCursor cursor = (MatrixCursor) spinner.getSelectedItem();
            String remoteContact = cursor.getString(1);
            
            Intent wake = getIntent();
            String imageUrl = wake.getStringExtra(ImageActivity.EXTRA_MEDIA_URL);
			String imageTitle = wake.getStringExtra(ImageActivity.EXTRA_MEDIA_TITLE);
			
			// Display session view
			Intent intent = new Intent(ContactActivity.this, SharingActivity.class);
        	intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	intent.putExtra(SharingActivity.EXTRA_MODE, SharingActivity.MODE_OUTGOING);
        	intent.putExtra(SharingActivity.EXTRA_CONTACT, remoteContact);
        	intent.putExtra(ImageActivity.EXTRA_MEDIA_TITLE, imageTitle);
        	intent.putExtra(ImageActivity.EXTRA_MEDIA_URL, imageUrl);
			startActivity(intent);
			
        	// Exit activity
        	finish();     
		}
	};
}
