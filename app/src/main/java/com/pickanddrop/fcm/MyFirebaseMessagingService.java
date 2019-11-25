/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pickanddrop.fcm;

import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.pickanddrop.R;
import com.pickanddrop.activities.DrawerContentSlideActivity;
import com.pickanddrop.activities.NotificationDialog;
import com.pickanddrop.activities.SplashActivity;
import com.pickanddrop.utils.AppSession;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMsgService";
    private AppSession appSession;
    private NotificationManager notificationManager;
    private Context context;

    // private Handler handler;
    boolean isCalling = false;
    private MediaPlayer mp;

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.e(TAG, "New Token >>>>>>>>"+ s);

        // Update refreshed Token
        new AppSession(getApplicationContext()).setFCMToken(s);
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(final RemoteMessage remoteMessage) {
        // TODO(developer): Handle FCM messages here.

        context = getApplicationContext();
        appSession = new AppSession(getApplicationContext());
        // If the application is in the foreground handle both data and notification messages here.
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
//         Log.d(TAG, "Notification Message Body: " + remoteMessage.getNotification().getBody());
        Log.e(TAG, "Body :>>> " + remoteMessage.getData().toString());
        boolean isAppOpen = false;
        if (isAppIsInBackground(getApplicationContext())) {
            isAppOpen = false;
        } else {
            isAppOpen = true;
        }
        try {
            if (isAppOpen) {

                Map<String, String> data = remoteMessage.getData();
                if(appSession.isLoginUser()){


                    Intent intent = new Intent(getBaseContext(), NotificationDialog.class);
                    Bundle bundle = new Bundle();
                    String data1 = data.get("data");
                    JSONObject jsonObject = new JSONObject(data1);
                    Log.i(TAG, "Inner Body : >>>>" + jsonObject.toString());
                    Log.i(TAG, "Inner Body : >>>>" + jsonObject.optJSONObject("payload").toString());




                    bundle.putString("title", "" + jsonObject.optString("title"));
                    bundle.putString("filename", "" + jsonObject.optString("image"));
                    bundle.putString("type", "" + jsonObject.optJSONObject("payload").optString("type"));
                    bundle.putString("inviteunique", "" + jsonObject.optJSONObject("payload").optString("inviteunique"));
//                    bundle.putString("user_type", "" + data.get("user_type"));
                    bundle.putString("message", "" + jsonObject.optString("message"));

                    bundle.putString("notification_id", "" + jsonObject.optJSONObject("payload").optString("driver_id"));
                    bundle.putString("full_name", "" + jsonObject.optJSONObject("payload").optString("name"));
                    bundle.putString("profile_image", "" + jsonObject.optJSONObject("payload").optString("image"));
                    bundle.putString("rating", "" + jsonObject.optJSONObject("payload").optString("avgratingdriver"));
                    bundle.putString("order_id", "" + jsonObject.optJSONObject("payload").optString("order_id"));



//                        Bundle bundle = new Bundle();
//                    bundle.putString("title", "" + data.get("title"));
//                    bundle.putString("filename", "" + data.get("image"));
//                    bundle.putString("type", "" + data.get("type"));
//                    bundle.putString("user_type", "" + data.get("user_type"));
//                    bundle.putString("message", "" + data.get("message"));
//
//                    bundle.putString("notification_id", "" + data.get("id"));

                    // bundle.putString(PN_ORDER_ID, "" + data.get(PN_ORDER_ID));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                            | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                            | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                    intent.putExtras(bundle);
                    getApplicationContext().startActivity(intent);
//                    sendNotification(remoteMessage.getData(), new Intent());
//                                }
//                            }
                }
            } else {
                sendNotification(remoteMessage.getData(), new Intent());

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
//        if (appSession.isNotification()) {
//
//        }
    }


    private void sendNotification(Map<String, String> data, Intent intent) {
        Log.i(TAG, "Notification >>>" + data.toString());
        String data1 = data.get("data");
        JSONObject jsonObject = null;
        Bundle bundle = new Bundle();
        try {
            jsonObject = new JSONObject(data1);
//            JSONObject jsonObject1 = new JSONObject();
//            jsonObject1.put("sound", "default");
//            jsonObject.put("notification", jsonObject1);
            Log.e(TAG, "Notification New >>>" + jsonObject.toString());

            bundle.putString("notification_id", "" + jsonObject.optJSONObject("payload").optString("driver_id"));
            bundle.putString("fullName", "" + jsonObject.optJSONObject("payload").optString("name"));
            bundle.putString("profileImage", "" + jsonObject.optJSONObject("payload").optString("image"));
            bundle.putString("rating", "" + jsonObject.optJSONObject("payload").optString("rating"));
            bundle.putString("order_id", "" + jsonObject.optJSONObject("payload").optString("order_id"));

            Log.e(getClass().getName(), "Notification Response is >>>>"+jsonObject.optJSONObject("payload").optString("driver_id")+" "+jsonObject.optJSONObject("payload").optString("name")+" "+jsonObject.optJSONObject("payload").optString("name"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //  int count = appSession.getBadgeCount();
        // count = count + 1;
        // appSession.setBadgeCount(count);
        try {
            //   ShortcutBadger.applyCount(getApplicationContext(), count);'



//            bundle.putString("type", "" + data.get("type"));
//            bundle.putString("user_type", "" + data.get("user_type"));
//            bundle.putString("new", "" + "new");

//            bundle.putString("type", "" + jsonObject.optJSONObject("payload").optString("type"));
//
//            if (jsonObject.optJSONObject("payload").optString("type").equalsIgnoreCase("accept")) {
//                bundle.putString("to_user_id", "" + jsonObject.optJSONObject("payload").optString("user_id"));
//                bundle.putString("userfullname", "" + jsonObject.optJSONObject("payload").optString("userfullname"));
//                bundle.putString("profile_image", "" + jsonObject.optJSONObject("payload").optString("profile_image"));
//            }

            mp = MediaPlayer.create(getApplicationContext(), R.raw.notification);
            mp.setLooping(false);
            mp.start();

            if(appSession.isLoginUser()){
                intent = new Intent(getApplicationContext(), DrawerContentSlideActivity.class);
                intent.putExtras(bundle);
//                startActivity(intent);
            }else {
                intent = new Intent(getApplicationContext(), SplashActivity.class);
                intent.putExtras(bundle);
//                startActivity(intent);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS
                | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 1, intent,
                PendingIntent.FLAG_ONE_SHOT);

//        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent,
//                0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext());
//        notificationBuilder.setSmallIcon(R.drawable.loginlogo);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            notificationBuilder.setSmallIcon(R.drawable.launcher_background);
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.loginlogo);

            notificationBuilder.setColor(getResources().getColor(R.color.transparent));
            notificationBuilder.setLargeIcon(bitmap);
        }else {
            notificationBuilder.setSmallIcon(R.drawable.loginlogo);
        }


        try {
            notificationBuilder.setContentTitle(jsonObject.optString("title"));
            notificationBuilder.setContentText(jsonObject.optString("message"));

//            Log.i(getClass().getName(), "Name is >>>>>>>"+data.get("title"));
//            Log.i(getClass().getName(), "Message >>>>>>>"+data.get("message"));
//             notificationBuilder.setLargeIcon(bitmap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Bitmap bitmap = null;
//        Bitmap bitmap = getBitmapFromURL(data.get("image"));

        notificationBuilder.setAutoCancel(true);
        // notificationBuilder.setSound(defaultSoundUri);
//        if (appSession.isSound()) {
        notificationBuilder.setSound(defaultSoundUri);
        notificationBuilder.setLights(Color.WHITE, 1000, 5000);

//        } else {
//            notificationBuilder.setSound(null);
//        }

//        if (appSession.isVibration()) {
        long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
        notificationBuilder.setVibrate(pattern);
//        } else {
//            notificationBuilder.setVibrate(new long[]{0, 0});
//        }
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        notificationBuilder.setStyle(new NotificationCompat.BigTextStyle().bigText(data.get("message")));
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (bitmap != null){
            NotificationCompat.BigPictureStyle s = new NotificationCompat.BigPictureStyle().bigPicture(bitmap);
            s.setSummaryText(data.get("message"));
            notificationBuilder.setStyle(s);
        }


        String CHANNEL_ID = "my_channel_01";// The id of the channel.
        notificationBuilder.setChannelId(CHANNEL_ID);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);

//            AudioAttributes attributes = new AudioAttributes.Builder()
//                    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
//                    .build();

            mChannel.enableLights(true);
            mChannel.enableVibration(true);
//            mChannel.setSound(defaultSoundUri, attributes);
            mChannel.setLightColor(Color.WHITE);

            notificationManager.createNotificationChannel(mChannel);
        }
        notificationManager.notify(11, notificationBuilder.build());
    }

    public Bitmap getBitmapFromURL(String strURL) {
        try {
            URL url = new URL(strURL);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mp != null && mp.isPlaying()) {
            mp.stop();
        }
    }
}

