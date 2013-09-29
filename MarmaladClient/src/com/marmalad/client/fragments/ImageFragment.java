package com.marmalad.client.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.marmalad.client.MainActivity;
import com.marmalad.client.data.Cache;
import com.marmalad.client.data.Config;
import com.marmalad.client.data.Feed;
import com.squareup.picasso.Picasso;

public class ImageFragment extends Fragment {

	public static int position = -1;	
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {			
		if(position == -1) switchFragment(null);				
		Feed feed = Cache.get(position);
		position = -1;		
		ImageView image = new ImageView(getActivity());
		String showImageUrl = feed.url.replace(Config.LOCALHOST, Config.ADDR_SERVR);
		Picasso.with(getActivity()).load(showImageUrl).into(image);
		
		return image;		
	}
	
	// the meat of switching the above fragment		
	private void switchFragment(Fragment fragment) {	
		if (getActivity() == null) return;
		Fragment newContent = fragment;
		if(fragment == null) {			
			newContent = new FeedFragment();
		}
		if (getActivity() instanceof MainActivity) {		
			MainActivity ma = (MainActivity) getActivity();			
			ma.switchContent(newContent);						
		}
	}
}
