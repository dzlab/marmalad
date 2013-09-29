package com.marmalad.client;

import org.gsma.joyn.JoynServiceListener;
import org.gsma.joyn.chat.Chat;
import org.gsma.joyn.chat.ChatListener;
import org.gsma.joyn.chat.ChatMessage;
import org.gsma.joyn.chat.ChatService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class SharingActivity extends Activity implements JoynServiceListener {
	
	private static final String TAG = "MainActivity";

	public final static int MODE_INCOMING = 0;
	public final static int MODE_OUTGOING = 1;	
	
	/**
	 * Intent parameters
	 */
	public final static String EXTRA_MODE    = "mode";
	public final static String EXTRA_CONTACT = "contact";
	public final static String EXTRA_PATH    = "path";
	
	private ChatService service;
	
	
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        
        // Instanciate API
        service = new ChatService(getApplicationContext(), this); 
        
        // Connect API
        service.connect();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    private int mode;
    private String contact;	
    
    private ChatListener mChatListener = new ChatListener() {
		
		@Override
		public void onReportMessageFailed(String arg0) {}
		
		@Override
		public void onReportMessageDisplayed(String arg0) {}
		
		@Override
		public void onReportMessageDelivered(String arg0) {
			
		}
		
		@Override
		public void onNewMessage(ChatMessage arg0) {}
		
		@Override
		public void onComposingEvent(boolean arg0) {}
	};
    
    @Override public void onServiceDisconnected(int error) {
		Log.i(TAG, "ChatService disconnected!");
	}
	
	@Override public void onServiceConnected() {
		Log.i(TAG, "ChatService connected!");
		mode = getIntent().getIntExtra(EXTRA_MODE, -1);
    	if (mode == MODE_OUTGOING) {
    		boolean registered = false;
        	try {
        		if ((service != null) && service.isServiceRegistered()) {
        			registered = true;
        		}
        	} catch(Exception e) {}
            if (!registered) {
            	Log.i(TAG, "ChatService not available!"); 
    	    	return;
            } 
            
	    	// Get remote contact
			contact = getIntent().getStringExtra(EXTRA_CONTACT);
			try {
				Intent intent = getIntent();
				String imageUrl = intent.getStringExtra(ImageActivity.EXTRA_MEDIA_URL);	
				String imageTitle = intent.getStringExtra(ImageActivity.EXTRA_MEDIA_TITLE);
				String body = "{'"+ImageActivity.EXTRA_MEDIA_URL+"':'"+imageUrl+"', '"+ImageActivity.EXTRA_MEDIA_TITLE+"':'"+imageTitle+"'}";
				
				mChat = service.openSingleChat(contact, mChatListener);				
				mChat.sendMessage(body);
				// Remove session listener
				
			}catch(Exception e) {
				e.printStackTrace();
			}			
    	}
	}
	
	private Chat mChat;
    
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (service != null) {
			try {
				mChat.removeEventListener(mChatListener);
			} catch (Exception e) {
			}
			
		}
		mChat=null;
		

        // Disconnect API
        service.disconnect();
	}
	
}
