package com.marmalad.client;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.marmalad.client.fragments.FeedFragment;
import com.marmalad.client.fragments.MenuFragment;
import com.slidingmenu.lib.SlidingMenu;
import com.slidingmenu.lib.app.SlidingFragmentActivity;

public class MainActivity extends SlidingFragmentActivity {

	private boolean isMenuInitiated=false;
	private Fragment mMenuFrag, mContent;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set the Above View
     	if (savedInstanceState != null)
     		mContent = getSupportFragmentManager().getFragment(savedInstanceState, "mContent");
     	if (mContent == null)
     		mContent = new FeedFragment();	
     		
     	// set the Above View
     	setContentView(R.layout.content_frame);
     	getSupportFragmentManager()
     	.beginTransaction()
     	.replace(R.id.content_frame, mContent)
     	.commit();
        
        // set the Behind View
        setBehindContentView(R.layout.menu_frame);
		if (savedInstanceState == null) {
			FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
			mMenuFrag = new MenuFragment();
			t.replace(R.id.menu_frame, mMenuFrag);
			t.commit();
		} else {
			mMenuFrag = this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
		}		
		// customize the SlidingMenu
		SlidingMenu sm = getSlidingMenu();
		sm.setBehindScrollScale(0.0f);
		sm.setShadowWidthRes(R.dimen.shadow_width);
		sm.setShadowDrawable(R.drawable.shadow);
		sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
		sm.setFadeDegree(0.35f);
		sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
		sm.setBackgroundColor(getResources().getColor(android.R.color.darker_gray));				
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);	            
    }

    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			toggle();			
			return true;		
		}
		return super.onOptionsItemSelected(item);
	}
    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
    	if(isMenuInitiated == false) {
			isMenuInitiated = true;				
		}else {
			toggle();				
		}	
		return false;
	}
    
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		getSupportFragmentManager().putFragment(outState, "mContent", mContent);
	}
	
    public void switchContent(Fragment fragment) {
		mContent = fragment;
		getSupportFragmentManager()
		.beginTransaction()
		.replace(R.id.content_frame, fragment)
		.commit();
		getSlidingMenu().showContent();
	}
}
