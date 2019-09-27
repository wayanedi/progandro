package com.example.test;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;


public class RecyclerView extends AppCompatActivity {

    private androidx.recyclerview.widget.RecyclerView rvView;
    private androidx.recyclerview.widget.RecyclerView.Adapter adapter;
    private androidx.recyclerview.widget.RecyclerView.LayoutManager layoutManager;
    private ArrayList<Berita> dataSet;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);


        dataSet = new ArrayList<Berita>();
        initDataset();

        rvView = findViewById(R.id.rv_main);
        rvView.setHasFixedSize(true);

        /**
         * Kita menggunakan LinearLayoutManager untuk list standar
         * yang hanya berisi daftar item
         * disusun dari atas ke bawah
         */
        layoutManager = new LinearLayoutManager(this);
        rvView.setLayoutManager(layoutManager);

        adapter = new RecyclerViewAdapter(dataSet);
        rvView.setAdapter(adapter);


    }

    private void initDataset(){

        /**
         * Tambahkan item ke dataset
         * dalam prakteknya bisa bermacam2
         * tidak hanya String seperti di kasus ini
         */
        dataSet.add(new Berita("dunia dalam berita", "2018"));
        dataSet.add(new Berita("gejayan memanggil", "2019"));
        dataSet.add(new Berita("ruu kuhp", "2019"));
        dataSet.add(new Berita("kpk", "2019"));

    }


}
