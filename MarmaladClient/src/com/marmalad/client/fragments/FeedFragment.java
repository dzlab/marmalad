package com.marmalad.client.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.marmalad.client.ImageActivity;
import com.marmalad.client.MainActivity;
import com.marmalad.client.R;
import com.marmalad.client.data.Cache;
import com.marmalad.client.data.Feed;
import com.marmalad.client.event.BusProvider;
import com.marmalad.client.event.CacheEvent;
import com.marmalad.client.task.FeedAdapter;
import com.origamilabs.library.views.StaggeredGridView;
import com.origamilabs.library.views.StaggeredGridView.OnItemClickListener;
import com.squareup.otto.Subscribe;

public class FeedFragment extends Fragment {

	private FeedAdapter adapter;
	private ProgressBar progress;
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {	
		View view = inflater.inflate(R.layout.feed_frame, null);
		progress = (ProgressBar) view.findViewById(R.id.progress);
		StaggeredGridView gridView = (StaggeredGridView) view.findViewById(R.id.staggeredGridView1);
		gridView.setOnItemClickListener(listener);
    	int margin = getResources().getDimensionPixelSize(R.dimen.margin);

    	gridView.setItemMargin(margin); // set the GridView margin
    	
    	gridView.setPadding(margin, 0, margin, 0); // have the margin on the sides as well

    	adapter = new FeedAdapter(getActivity());

    	gridView.setAdapter(adapter);
    	adapter.notifyDataSetChanged();
		return view;//gridView;
	}
	
	OnItemClickListener listener = new OnItemClickListener() {		
		@Override
		public void onItemClick(StaggeredGridView parent, View view, int position, long id) {
			/*ImageFragment.position = position;
			if (getActivity() instanceof MainActivity) {		
				MainActivity ma = (MainActivity) getActivity();			
				ma.switchContent(new ImageFragment());						
			}*/
			Feed feed = Cache.get(position);
			Intent intent = new Intent(getActivity(), ImageActivity.class);
			intent.putExtra(ImageActivity.EXTRA_MEDIA_POSITION, position);
			intent.putExtra(ImageActivity.EXTRA_MEDIA_URL, feed.getUrl());
			intent.putExtra(ImageActivity.EXTRA_MEDIA_TITLE, feed.title);
			startActivity(intent);
		}
	};
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
	
	@Override public void onResume() {
		BusProvider.getInstance().register(this); 
		super.onResume();
	}

	@Override public void onPause() {	     
		BusProvider.getInstance().unregister(this); 
	    super.onPause(); 
	}
	
	@Subscribe public void onCacheChanged(CacheEvent event) {		
		if(progress.isShown())
			progress.setVisibility(View.GONE);
		else
			Toast.makeText(getActivity(), "Loading more", Toast.LENGTH_SHORT).show();
		getActivity().runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				adapter.notifyDataSetChanged();
			}
		});
	}
}
