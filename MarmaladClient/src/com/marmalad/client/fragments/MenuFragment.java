package com.marmalad.client.fragments;

import com.marmalad.client.MainActivity;
import com.marmalad.client.R;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuFragment extends ListFragment {

	private Resources res;
	private SampleAdapter adapter;
	private static Map<String, Fragment> map = new HashMap<String, Fragment>();
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {		
		res = getActivity().getResources();
		return inflater.inflate(R.layout.list, null);
	}

	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		adapter = new SampleAdapter(getActivity());
		adapter.add(new SampleItem(res.getString(R.string.menu_profile), R.drawable.ic_person));
		adapter.add(new SampleItem(res.getString(R.string.menu_images), R.drawable.ic_picture));
		adapter.add(new SampleItem(res.getString(R.string.menu_media), R.drawable.ic_play));
		adapter.add(new SampleItem(res.getString(R.string.menu_audio), R.drawable.ic_play));
		
		setListAdapter(adapter);
		
	}

	private class SampleItem {
		public String tag;
		public int iconRes;
		public SampleItem(String tag, int iconRes) {
			this.tag = tag;
			this.iconRes = iconRes;
		}
	}

	public class SampleAdapter extends ArrayAdapter<SampleItem> {
		
		public SampleAdapter(Context context) {
			super(context, 0);
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(getContext()).inflate(R.layout.row, null);
			}
			ImageView icon = (ImageView) convertView.findViewById(R.id.row_icon);
			icon.setImageResource(getItem(position).iconRes);
			TextView title = (TextView) convertView.findViewById(R.id.row_title);
			title.setText(getItem(position).tag);
			
			return convertView;
		}
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		SampleItem item = adapter.getItem(position);
		newContent = map.get(item.tag);
		if(newContent == null) {
			if (item.tag.equals(res.getString(R.string.menu_profile))) {
				newContent = new ProfileFragment();
				
			}else if (item.tag.equals(res.getString(R.string.menu_images))) {
				newContent = new FeedFragment();
				
			}else if (item.tag.equals(res.getString(R.string.menu_media))) {
				newContent = new MultimediaFragment();
				
			}else if (item.tag.equals(res.getString(R.string.menu_audio))) {
				newContent = new AudioFragment();
			}
			map.put(item.tag, newContent);
		}		
		if (newContent != null)
			switchFragment(newContent);
	}
	
	// the meat of switching the above fragment
	private void switchFragment(Fragment fragment) {
		if (getActivity() == null) return;
			
		if (getActivity() instanceof MainActivity) {
			MainActivity ma = (MainActivity) getActivity();
			ma.switchContent(fragment);			
		} 		
	}
}
