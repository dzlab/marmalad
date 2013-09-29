package com.marmalad.client;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ListAdapter;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.marmalad.client.fragments.MenuFragment;
import com.marmalad.client.task.ImageAdapter;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;
import com.twotoasters.android.horizontalimagescroller.widget.HorizontalImageScroller;

public class ImageActivity extends SlidingFragmentActivity {

	public static final String TAG = "ImageActivity";
	
	public static final String EXTRA_MEDIA_POSITION = "media_position";
	public static final String EXTRA_MEDIA_URL      = "media_url";
	public static final String EXTRA_MEDIA_TITLE    = "media_title";
	
	protected ListFragment mFrag;
	protected String imageUrl, imageTitle;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*ImageView image = new ImageView(this);
		setContentView(image);*/
		setContentView(R.layout.activity_image);
		HorizontalImageScroller scroller = (HorizontalImageScroller) findViewById(R.id.my_horizontal_image_scroller);
		
		// set the Behind View
		setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mFrag = new MenuFragment();
			t.replace(R.id.menu_frame, mFrag);
			t.commit();
		} else {
			mFrag = (ListFragment)this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		
		Intent intent = getIntent();
		int offset = intent.getIntExtra(EXTRA_MEDIA_POSITION, 0);
		ListAdapter adapter = new ImageAdapter(this, offset);
		scroller.setAdapter(adapter);
		imageUrl = intent.getStringExtra(EXTRA_MEDIA_URL);
		if(imageUrl == null || imageUrl.equals("")) {
			Log.e(TAG, "Cannot load image for an empty url!");
			finish();
		}
		imageTitle = intent.getStringExtra(EXTRA_MEDIA_TITLE);
		setTitle(imageTitle);
		
		//Picasso.with(this).load(imageUrl).into(image);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_share:
			Intent intent = new Intent(this, ContactActivity.class);
			intent.putExtra(EXTRA_MEDIA_URL, imageUrl);
			intent.putExtra(EXTRA_MEDIA_TITLE, imageTitle);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
}
