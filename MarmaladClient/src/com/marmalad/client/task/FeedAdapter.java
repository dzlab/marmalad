package com.marmalad.client.task;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.VideoView;

import com.marmalad.client.R;
import com.marmalad.client.data.Cache;
import com.marmalad.client.data.Config;
import com.marmalad.client.data.Feed;
import com.squareup.picasso.Picasso;

public class FeedAdapter extends BaseAdapter {

	public static final String TAG = FeedAdapter.class.getSimpleName();
	private Activity activity;
	private LayoutInflater inflator;
	
	public FeedAdapter(Activity activity) {
		this.activity = activity;
		this.inflator = LayoutInflater.from(activity);		
		new FeedsTask().execute();
	}
	
	@Override
	public int getCount() {
		return Cache.count();
	}

	@Override
	public Object getItem(int position) {		
		return Cache.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) {			
			convertView = inflator.inflate(R.layout.row_feed, null);
			holder = new ViewHolder();
			holder.imageView = (ImageView) convertView .findViewById(R.id.imageView);
			//holder.videoView = (VideoView) convertView .findViewById(R.id.videoView);
			holder.showName  = (TextView) convertView .findViewById(R.id.showName);

			convertView.setTag(holder);		
		}

		holder = (ViewHolder) convertView.getTag();		
		Feed feed = (Feed) getItem(position);
		String viewUrl = feed.url.replace(Config.LOCALHOST, Config.ADDR_SERVR);	
		
		/*if(viewUrl.endsWith("mp4") || viewUrl.endsWith("mov") || viewUrl.endsWith("avi")) {						
			//holder.videoView.setVideoPath(viewUrl);
			//holder.imageView.setVisibility(View.GONE);
			holder.videoView.setVisibility(View.GONE);
			//holder.imageView.setImageBitmap(videoFrame(viewUrl));
			Log.i(TAG, "Loading video from "+viewUrl);
		}else {			
			Picasso.with(activity).load(viewUrl).scale(0.3f).into(holder.imageView);
			holder.videoView.setVisibility(View.GONE);
			Log.i(TAG, "Loading image from "+viewUrl);
		}*/
		
		Picasso.with(activity).load(viewUrl).scale(0.3f).into(holder.imageView);
		
		try {
			holder.showName.setText(URLDecoder.decode(feed.title, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		/*String userImageUrl = feed.user.avatar.w25;		
		Picasso.with(activity).load(userImageUrl).into(holder.userImage);
		holder.userName.setText(feed.user.username);
		
		if(position == getCount()-1) //load more feeds
			new FeedsTask().execute();*/
		return convertView;
	}

	static class ViewHolder {
		ImageView imageView;
		//VideoView videoView;
		TextView showName;
		/*ImageView userImage;
		TextView userName;*/
	}
	
	@SuppressLint("NewApi")
	public Bitmap videoFrame(String uri) {       
	    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
	    try {                       
	        retriever.setDataSource(uri);            
	        return retriever.getFrameAtTime();
	    } catch (IllegalArgumentException ex) {
	        ex.printStackTrace();
	    } catch (RuntimeException ex) {
	        ex.printStackTrace();
	    } finally {
	        try {
	            retriever.release();
	        } catch (RuntimeException ex) {
	        }
	    }
	    return null;
	}
}
