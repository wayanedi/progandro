package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.viewpager.widget.ViewPager;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;

import java.util.Timer;
import java.util.TimerTask;


public class user extends AppCompatActivity implements Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener{

    private  boolean isReceiveRegistered = false;
    private static final String TAG = "MainActivity";
    public static final long INTERVAL=3000;//variable to execute services every 10 second
    private Handler mHandler=new Handler(); // run on another Thread to avoid crash
    private Timer mTimer=null; // timer handling

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();

        Bundle b = getIntent().getExtras();

        String str2 = b.getString("email", "");
        Toast.makeText(getApplicationContext(), "selamat datang " + str2, Toast.LENGTH_SHORT).show();

        TabLayout tablayout = findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("Tab 1"));
        tablayout.addTab(tablayout.newTab().setText("Tab 2"));
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewpager = findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tablayout.getTabCount());
        //Toast.makeText(getApplicationContext(), Integer.toString(tablayout.getTabCount()), Toast.LENGTH_SHORT).show();
        viewpager.setAdapter(adapter);

        //prepare();
        //Button btn = (Button) findViewById(R.id.btn_about);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = new Intent(user.this, About.class);
//                startActivity(it);
//            }
//        });
    }



    public void scheduleJob(View v) {
        ComponentName componentName = new ComponentName(this, MyJobService.class);
        JobInfo info = new JobInfo.Builder(123, componentName)
                .setRequiresCharging(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
                .setPersisted(true)
                .setPeriodic(15 * 60 * 1000)
                .build();

        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d(TAG, "Job scheduled");
        } else {
            Log.d(TAG, "Job scheduling failed");
        }

        if(mTimer!=null)
            mTimer.cancel();
        else
            mTimer=new Timer(); // recreate new timer
        mTimer.scheduleAtFixedRate(new TimeDisplayTimerTask(),0,INTERVAL);
    }

    public void cancelJob(View v) {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
        mTimer.cancel();
    }


    private class TimeDisplayTimerTask extends TimerTask {
        @Override
        public void run() {
            // run on another thread
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    // display toast at every 10 second
                    Toast.makeText(getApplicationContext(), "tiap 3 detik", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo  = connectivityManager.getActiveNetworkInfo();

            if(networkInfo !=null){
                if(networkInfo.getType() == ConnectivityManager.TYPE_WIFI){
                    System.out.println("status: " + networkInfo.isConnected());
                    //Toast.makeText(getApplicationContext(),"wifi off",Toast.LENGTH_SHORT).show();
                    notification("Wifi Off");
                }
                else{
                    System.out.println("status: " + networkInfo.isConnected());
                    //Toast.makeText(getApplicationContext(),"wifi on",Toast.LENGTH_SHORT).show();
                    notification("Wifi On");
                }
            }

        }
    };

    private void notification(String message){

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel =
                    new NotificationChannel("MyNotifications", "MyNotifications", NotificationManager.IMPORTANCE_DEFAULT);

            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "MyNotifications")
                .setContentTitle("Wifi Status")
                .setSmallIcon(R.drawable.ic_message_black_24dp)
                .setAutoCancel(true)
                .setContentText(message);

        NotificationManagerCompat manager = NotificationManagerCompat.from(this);
        manager.notify(999,builder.build());

    }

    @Override
    protected void onResume() {
        super.onResume();

        if(!isReceiveRegistered){
            isReceiveRegistered = true;

            registerReceiver(receiver, new IntentFilter("android.net.wifi.STATE_CHANGE"));

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(!isReceiveRegistered){

        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }


//    private void prepare(){
//        this.getSupportFragmentManager().beginTransaction().add(R.id.fragment_test, new TestFragment()).commit();
//    }
}
