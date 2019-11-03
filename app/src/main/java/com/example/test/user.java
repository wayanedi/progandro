package com.example.test;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class user extends AppCompatActivity implements Tab1.OnFragmentInteractionListener, Tab2.OnFragmentInteractionListener{

    private  boolean isReceiveRegistered = false;
    private static final String TAG = "MainActivity";
    public static final long INTERVAL=3000;//variable to execute services every 10 second
    private Handler mHandler=new Handler(); // run on another Thread to avoid crash
    private Timer mTimer=null; // timer handling

    private FirebaseFirestore firebaseFirestoreDb;
    private EditText namaMatakuliah;
    private EditText namaDosen;
    private EditText sks;
    private Button btn;


    private RecyclerView rvView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<Matakuliah> dataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        Intent intent = getIntent();

//        Bundle b = getIntent().getExtras();
//
//        String str2 = b.getString("email", "");
//        Toast.makeText(getApplicationContext(), "selamat datang " + str2, Toast.LENGTH_SHORT).show();



        TabLayout tablayout = findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("Tab 1"));
        tablayout.addTab(tablayout.newTab().setText("Firebase"));
        tablayout.addTab(tablayout.newTab().setText("Tab 3"));
        tablayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewpager = findViewById(R.id.pager);
        final PageAdapter adapter = new PageAdapter(getSupportFragmentManager(), tablayout.getTabCount());
        //Toast.makeText(getApplicationContext(), Integer.toString(tablayout.getTabCount()), Toast.LENGTH_SHORT).show();
        viewpager.setAdapter(adapter);

        //Button btn = (Button) findViewById(R.id.btn_about);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent it = new Intent(user.this, About.class);
//                startActivity(it);
//            }
//        });

//        btn = findViewById(R.id.simpanButton);
//
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tambahMahasiswa(view);
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

    private void getDataMahasiswa(){

        dataSet = new ArrayList<Matakuliah>();
        firebaseFirestoreDb.collection("Matakuliah").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        Matakuliah matkul = document.toObject(Matakuliah.class);
                        dataSet.add(matkul);
                    }
                    setToRecycleview();
                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }

            }
        });

    }

    public void tambahMahasiswa(View view) {

        firebaseFirestoreDb = FirebaseFirestore.getInstance();

        namaMatakuliah = findViewById(R.id.namaMataKuliah);
        namaDosen = findViewById(R.id.namaDosen);
        sks = findViewById(R.id.sks);


        Matakuliah mhs = new Matakuliah(namaMatakuliah.getText().toString(),
                namaDosen.getText().toString(),
                Integer.parseInt(sks.getText().toString()));

        System.out.println("dosen : " + mhs.getNamaDosen());
        System.out.println("matkul : " +mhs.getNamaMatkul());
        System.out.println("sks : " +mhs.getSks());


        firebaseFirestoreDb = FirebaseFirestore.getInstance();


        firebaseFirestoreDb.collection("Matakuliah").document().set(mhs)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Toast.makeText(getApplicationContext(), "Mahasiswa berhasil didaftarkan",
                            Toast.LENGTH_SHORT).show();
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(), "ERROR" + e.toString(),
                            Toast.LENGTH_SHORT).show();
                    Log.d("TAG", e.toString());
                }
            });
        getDataMahasiswa();

    }

    private void setToRecycleview(){

        System.out.println("isi data set: " + dataSet.size());


        rvView = findViewById(R.id.recycle_view_mahasiswa);
        rvView.setHasFixedSize(true);

//        dataSet.add(new Matakuliah("alpro", "yuan", 3));
//        dataSet.add(new Matakuliah("manpro", "eki", 3));
//        dataSet.add(new Matakuliah("progeb", "dany", 3));

        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewMatakuliahAdapter(dataSet);
        rvView.setAdapter(adapter);
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



}
