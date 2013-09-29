package com.marmalad.client.data;

public class Feed {

	public static final String URL   = "url";
	public static final String TITLE = "title";
	
	public String url;
	public String title;
	
	public String getUrl() {
		return url.replace(Config.LOCALHOST, Config.ADDR_SERVR);
	}
	
	public boolean isVideo() {
		return url.endsWith("mp4") || url.endsWith("avi") || url.endsWith("mov");
	}
	
	public boolean isAudio() {
		return url.endsWith("mp3");
	}
	
	public boolean isImage() {
		return url.endsWith("png") || url.endsWith("jpg") || url.endsWith("jpeg") || url.endsWith("bmp");
	}
}
