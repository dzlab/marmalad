package com.marmalad.client.fragments;

import java.io.IOException;

import android.content.Context;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.marmalad.client.R;
import com.marmalad.client.data.Cache;
import com.marmalad.client.data.Feed;
import com.marmalad.client.event.BusProvider;

public class AudioFragment extends Fragment implements OnItemClickListener {

	private MediaPlayer player;
	private SampleAdapter adapter;
	private SeekBar seekBar;
	private ImageView controler;
	
	private final Handler handler = new Handler();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		player = new MediaPlayer();		
		View view = inflater.inflate(R.layout.audio_frame, null);
		controler = (ImageView) view.findViewById(R.id.controler);
		controler.setOnClickListener(new OnClickListener() {			
			@Override public void onClick(View view) {
				if (player.isPlaying()) {
		            controler.setImageResource(R.drawable.ic_ctrl_play);
		            player.pause();
		        }else {
		        	controler.setImageResource(R.drawable.ic_ctrl_pause);
		            try{
		            	player.start();
		            	updateProgressBar();
		            }catch (IllegalStateException e) {
		            	player.pause();
		            }		            
		        }
			}
		});
		seekBar = (SeekBar) view.findViewById(R.id.seekBar);
		seekBar.setOnTouchListener(new OnTouchListener() {
			@Override public boolean onTouch(View v, MotionEvent event) {
				if(player.isPlaying()){
			    	SeekBar sb = (SeekBar) v;
					player.seekTo(sb.getProgress());
				}
				return false; 			
			}
		});
		ListView listView = (ListView) view.findViewById(android.R.id.list);
		View emptyView = view.findViewById(android.R.id.empty);
		listView.setEmptyView(emptyView);
		listView.setAdapter(getAdapter());
		listView.setOnItemClickListener(this);
		return view;
	}
	
	public void updateProgressBar() {
		seekBar.setProgress(player.getCurrentPosition()*10);
		if (player.isPlaying()) {
			Runnable notification = new Runnable() {
		        public void run() {
		        	updateProgressBar();
				}
		    };
		    handler.postDelayed(notification, 1000);
    	}else{
    		player.pause();
    		controler.setImageResource(R.drawable.ic_ctrl_play);
    		seekBar.setProgress(0);
    	}	
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	public ListAdapter getAdapter() {
		adapter = new SampleAdapter(getActivity());
		for(int i=0; i<Cache.videos.size(); i++) {	
			Feed item = Cache.videos.get(i);
			if(item.isAudio())
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
        if(player.isPlaying() == false) {				
			try {
				player.setDataSource(item.getUrl());

		        player.prepare();
		        player.start();
		        controler.setImageResource(R.drawable.ic_ctrl_pause);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			player.pause();
			controler.setImageResource(R.drawable.ic_ctrl_play);
			updateProgressBar();
		}
		
	}
	
	@Override public void onPause() {	     		
	    super.onPause(); 
	    player.stop();
	}
}
