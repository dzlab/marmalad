package com.marmalad.client.task;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;

import com.marmalad.client.data.Cache;
import com.marmalad.client.data.Feed;
import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {

	public static final String TAG = ImageAdapter.class.getSimpleName();
	private Activity activity;
	
	private int offset;
	
	public ImageAdapter(Activity activity, int offset) {
		this.offset = offset;
		this.activity = activity;	
	}
	
	@Override
	public int getCount() {
		return Cache.count();
	}

	@Override
	public Object getItem(int position) {		
		int index = (position + offset) % getCount();
		return Cache.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if (convertView == null) {			
			convertView = new ImageView(activity);
			LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			convertView.setLayoutParams(params);
		}
		Feed feed = (Feed) getItem(position);
		
		Picasso.with(activity).load(feed.getUrl()).into((ImageView)convertView);
		
		return convertView;
	}
	
}
