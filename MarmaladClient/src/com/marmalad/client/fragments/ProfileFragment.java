package com.marmalad.client.fragments;

import com.marmalad.client.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

public class ProfileFragment extends Fragment {

	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {			
		View view = inflater.inflate(R.layout.profile_frame, null);
		ListView listView = (ListView) view.findViewById(android.R.id.list);
		View emptyView = view.findViewById(android.R.id.empty);
		listView.setEmptyView(emptyView);
		return view;
	}
	
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
	}
}
