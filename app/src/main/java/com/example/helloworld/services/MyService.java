package com.example.helloworld.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.helloworld.R;

public class MyService extends Service {
    private static final String TAG = "MyService";
    public static final String BROADCAST_ACTION = "com.example.helloworld";

    IBinder mBinder = new LocalBinder();
    boolean isRunning = false;

    //Declare timer
    CountDownTimer cTimer = null;

    int sss=0;

    //start timer function
    void startTimer() {
        if(isRunning) return;

        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                sss++;
                Intent intent = new Intent(BROADCAST_ACTION);
                intent.putExtra("sec", sss);
                sendBroadcast(intent);
                isRunning = true;

                Log.d("print", String.valueOf(sss));
            }
            public void onFinish() {
                cTimer = null;
                isRunning = false;
                sss= 0;
            }
        };
        cTimer.start();
    }

    //cancel timer
    public void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    @Override
    public void onCreate() {
        Log.d(TAG, "on create");
        startTimer();

        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "on start command");
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalBinder extends Binder {
        public MyService getServerInstance() {
            return MyService.this;
        }
    }

    public void showNotification() {

        String NOTIFICATION_CHANNEL_ID = "MY_SERVICE_CHANNEL";
        String channelName = "Countdown";

        // setup channel for headsup
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_HIGH);
        chan.setDescription("Channel for sec");
//        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        // Register channel with system
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(chan);

        // setup notification
        Notification notification = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Apps running at background")
                .setContentText("Current sec:" + sss)
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .build();

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(0, notification);
//        startForeground(ONGOING_ID, notification);
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "on destroyed");
        super.onDestroy();
    }

}
