package com.example.helloworld;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.helloworld.services.MyService;

public class SecondActivity extends AppCompatActivity {

    private static final String TAG = "SecondActivity";
    private static final String CHANNEL_DEFAULT_IMPORTANCE = "com.example.note.second_activity";

    Button btn_end, btn_clickme;
    TextView txt_hello, txt_count, txt_seconds;

    public static final String COUNTER_MESSAGE = "com.example.note.COUNTER";
    public static final String SECOND_MESSAGE = "com.example.note.SECONDS";

    int counter = 0;
    int seconds = 0;
    Boolean isStopped = false;
    Boolean isEndClicked = false;

    private boolean mBounded;
    private MyService myService;

    @Override
    protected void onStart() {
        super.onStart();
    }

    ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "service disconnected");
            Toast.makeText(SecondActivity.this, "Service is disconnected", Toast.LENGTH_SHORT).show();
            mBounded = false;
            myService = null;
        }

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "service connected");
            Toast.makeText(SecondActivity.this, "Service is connected", Toast.LENGTH_SHORT).show();
            mBounded = true;
            MyService.LocalBinder mLocalBinder = (MyService.LocalBinder)service;
            myService = mLocalBinder.getServerInstance();

        }
    };

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Update Your UI here..
            txt_seconds.setText("Current sec:"+ intent.getIntExtra("sec", 0)+"");
            seconds = intent.getIntExtra("sec", seconds);

            if(isStopped == true) myService.showNotification();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_page);

        txt_hello = findViewById(R.id.lbl_hello);
        txt_count = findViewById(R.id.text_count);
        txt_seconds = findViewById(R.id.text_sec);
        btn_end = findViewById(R.id.btn_end);
        btn_clickme = findViewById(R.id.btn_clickme);

        btn_clickme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                counter++;
                txt_count.setText("Current count: " + counter);
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myService.cancelTimer();
                unregisterReceiver(broadcastReceiver);
                isEndClicked = true;
                Intent intent = new Intent(view.getContext(), ThirdActivity.class);
                intent.putExtra(COUNTER_MESSAGE, Integer.toString(counter));
                intent.putExtra(SECOND_MESSAGE, Integer.toString(seconds));
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        String name = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        txt_count.setText("Current count: " + counter);
        txt_hello.setText("Hello " + name);

        Intent mIntent = new Intent(this, MyService.class);
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);

//        startTimer();
    }

    @Override
    protected void onResume() {
        Log.d(TAG, "on resume");
        isStopped = false;
        isEndClicked = false;
        registerReceiver(broadcastReceiver, new IntentFilter(MyService.BROADCAST_ACTION));
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.d(TAG, "on paused");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "on stop");
        isStopped = true;

        if(isEndClicked == true) {
            if(mBounded) {
                unbindService(mConnection);
                mBounded = false;
            }
        }

        super.onStop();
    }

    @Override
    protected void onRestart() {
        Log.d(TAG, "on restart");
//        Intent notificationIntent = new Intent(this, MyService.class);
//
//        if(notificationIntent != null)
//            stopService(notificationIntent);
        super.onRestart();
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "on destroyed");
        isEndClicked = true;
        unregisterReceiver(broadcastReceiver);
        super.onDestroy();
    }
}
