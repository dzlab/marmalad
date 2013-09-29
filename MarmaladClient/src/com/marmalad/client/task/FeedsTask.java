package com.marmalad.client.task;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.marmalad.client.data.Cache;
import com.marmalad.client.data.Config;
import com.marmalad.client.data.Feed;
import com.squareup.okhttp.OkHttpClient;

public class FeedsTask extends AsyncTask<Void, Void, String> {
	public static final String TAG = "FeedsTask";
	
	private static final OkHttpClient client = new OkHttpClient();
	private static final Gson sGson = new Gson();

	@Override
	protected String doInBackground(Void... params) {
		String url = Config.BASE_URL;
		String result = null;
		try {
			result = doGet(url);	
		}catch(Exception e) {
			e.printStackTrace();
		}
		return result;
	}
	
	@Override protected void onPostExecute(String result) {
		if(result==null) return;
        try {
        	String encoded = new String(result.getBytes("ISO-8859-1"), "UTF-8"); 
        	JSONArray array = new JSONArray(encoded); 
        	Cache.clearAll();
        	for(int i=0; i<array.length(); i++) {
        		JSONObject obj = array.getJSONObject(i);
        		Feed feed = new Feed();
        		feed.url = obj.getString(Feed.URL);
        		feed.title = obj.getString(Feed.TITLE);
        		if(feed.url!=null && !feed.url.equals(""))
        			Cache.add(feed);
        	}

        }catch(Exception e) {
        	e.printStackTrace();
        }
	}
	
	private static String doGet(String url) throws Exception {
		String result = null;	
		HttpURLConnection connection = client.open(new URL(url));
		InputStream in = null;
		try {	
			// Read the response.
			in = connection.getInputStream();
			byte[] response = readFully(in);
			result = new String(response, "UTF-8");
			Log.i(TAG, "Received JSON: "+result);
		} finally {
			if (in != null) in.close();	
		}
		return result;	
	}
	
	private static byte[] readFully(InputStream in) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for (int count; (count = in.read(buffer)) != -1; ) {
			out.write(buffer, 0, count);
		}
		return out.toByteArray();	
	}
}
