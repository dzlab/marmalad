package com.marmalad.client.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.marmalad.client.event.BusProvider;
import com.marmalad.client.event.CacheEvent;
import com.squareup.otto.Produce;

public class Cache {

	public static List<Feed> images = Collections.synchronizedList(new ArrayList<Feed>());
	public static List<Feed> videos  = Collections.synchronizedList(new ArrayList<Feed>());
	//public static List<Feed> musics  = Collections.synchronizedList(new ArrayList<Feed>());
	
	public static synchronized void add(Feed feed) {
		if(feed.url.endsWith("mp4") || feed.url.endsWith("mov") || feed.url.endsWith("avi") || feed.url.endsWith("mp3"))
			videos.add(feed);		
		else
			images.add(feed);
		BusProvider.getInstance().post(produceCacheEvent(CacheEvent.Type.CREATE)); 
	}
	
	public static synchronized void addAll(List<Feed> list) {
		images.addAll(list);
		BusProvider.getInstance().post(produceCacheEvent(CacheEvent.Type.CREATE)); 
	}

	public static synchronized void clearAll() {
		images.clear();
		videos.clear();
		BusProvider.getInstance().post(produceCacheEvent(CacheEvent.Type.DELETE)); 
	}
	
	public static synchronized Feed get(int position) {
		return images.get(position);
	}

	public static int count() {
		return images.size();
	}
	
	@Produce private static CacheEvent produceCacheEvent(CacheEvent.Type type) {		
		return new CacheEvent(type);
	}
}
