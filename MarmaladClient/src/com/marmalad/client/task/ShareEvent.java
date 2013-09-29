package com.marmalad.client.task;

import java.util.Random;

import org.gsma.joyn.chat.ChatIntent;
import org.gsma.joyn.chat.ChatMessage;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import com.marmalad.client.ImageActivity;
import com.marmalad.client.R;
import com.marmalad.client.utils.Utils;

public class ShareEvent extends BroadcastReceiver {
	
	public static final String TAG = "ShareEvent";
	
    @Override
	public void onReceive(Context context, Intent intent) {
    	Log.i(TAG, "Receiving messages");
    	// Get the chat message from the Intent
		ChatMessage message = intent.getParcelableExtra(ChatIntent.EXTRA_MESSAGE);
		/*try {
			JSONObject body = new JSONObject(message.getMessage());
			String imageUrl = body.getString(ImageActivity.EXTRA_MEDIA_URL);
			String imageTitle = body.getString(ImageActivity.EXTRA_MEDIA_TITLE);
			Log.i(TAG, "Received link: "+imageUrl);    		
			Intent wake = new Intent(context, ImageActivity.class);
			wake.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			wake.putExtra(ImageActivity.EXTRA_MEDIA_URL, imageUrl);
			wake.putExtra(ImageActivity.EXTRA_MEDIA_TITLE, imageTitle);
			context.startActivity(wake); 
			
		}catch(Exception e) {
			e.printStackTrace();
		}*/
		show_notification(context, message);
    }
    
    protected void show_notification(Context context, ChatMessage message) {		
		String notifText = message.getMessage(); 				
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)				
				.setSmallIcon(R.drawable.ic_launcher)						        
		        .setContentTitle(message.getContact())
		        .setContentText(notifText)
				.setAutoCancel(true);
		// Creates an explicit intent for an Activity in your app
		Intent intent = new Intent(context, ImageActivity.class);
		//enclose the corresponding contact data into the dispatched intent			
		try {
			JSONObject body = new JSONObject(message.getMessage());			
			intent.putExtra(ImageActivity.EXTRA_MEDIA_URL, body.getString(ImageActivity.EXTRA_MEDIA_URL));
			intent.putExtra(ImageActivity.EXTRA_MEDIA_TITLE, body.getString(ImageActivity.EXTRA_MEDIA_TITLE));
			
			// The stack builder object will contain an artificial back stack for the started Activity.
			// This ensures that navigating backward from the Activity leads out of your application to the Home screen.
			TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
			// Adds the back stack for the Intent (but not the Intent itself)
			stackBuilder.addParentStack(ImageActivity.class);
			// Adds the Intent that starts the Activity to the top of the stack
			stackBuilder.addNextIntent(intent);
			PendingIntent resultPendingIntent =
			        stackBuilder.getPendingIntent(
			            0,
			            PendingIntent.FLAG_UPDATE_CURRENT
			        );
			mBuilder.setContentIntent(resultPendingIntent);
			mBuilder.setDefaults(Notification.DEFAULT_LIGHTS);
			NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
			// mId allows you to update the notification later on.			
			mNotificationManager.notify((int)Long.parseLong(message.getContact().replace("+", "")), mBuilder.build()); 
		}catch(Exception e) {
			e.printStackTrace();
		}		
	}

}
