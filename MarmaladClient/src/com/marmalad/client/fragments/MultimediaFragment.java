package com.marmalad.client.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.marmalad.client.R;
import com.marmalad.client.data.Cache;
import com.marmalad.client.data.Feed;

public class MultimediaFragment extends Fragment implements OnItemClickListener {

	private MediaController controller;
	private SampleAdapter adapter;
	private VideoView video;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {			
		View view = inflater.inflate(R.layout.media_frame, null);
		video = (VideoView) view.findViewById(R.id.video);
		ListView listView = (ListView) view.findViewById(android.R.id.list);
		View emptyView = view.findViewById(android.R.id.empty);
		listView.setEmptyView(emptyView);
		listView.setAdapter(getAdapter());
		listView.setOnItemClickListener(this);
		return view;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	public ListAdapter getAdapter() {
		adapter = new SampleAdapter(getActivity());
		for(int i=0; i<Cache.videos.size(); i++) {
			Feed item = Cache.videos.get(i);
			if(item.isVideo())
				adapter.add(item);
		}
		return adapter;
	}

	public static class SampleAdapter extends ArrayAdapter<Feed> {
		private Context mContext;
		public SampleAdapter(Context context) {
			super(context, 0);
			mContext = context.getApplicationContext();
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new TextView(mContext);
				((TextView) convertView).setTextColor(Color.WHITE);
				((TextView) convertView).setTextSize(15);
			}			
			((TextView) convertView).setText(getItem(position).title);			
			return convertView;
		}
	}

	@Override
	public void onItemClick(AdapterView<?> lv, View v, int position, long id) {
		Feed item = adapter.getItem(position);
		if(item.isVideo()) {
			/*video.setVisibility(View.VISIBLE);
			video.setVideoURI(Uri.parse(item.getUrl()));
			controller = new MediaController(getActivity());
	        controller.setAnchorView(video);
	        video.setMediaController(controller);
	        
	        video.start();*/	
	        
			//loadVideoInBrowser(item.url);
			loadVideoInNativeApp(item.url);
		}else {
			try {				
				MediaPlayer mp = new MediaPlayer();
		        mp.setDataSource(item.getUrl());
		        mp.prepare();
		        mp.start();
		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

	}
	
	private void loadVideoInBrowser(String url) {
		Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
	}
	
	private void loadVideoInNativeApp(String url) {
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW);
	    Uri data = Uri.parse(url);
	    intent.setDataAndType(data, "video/*");
	    startActivity(intent);
	}
	
}
