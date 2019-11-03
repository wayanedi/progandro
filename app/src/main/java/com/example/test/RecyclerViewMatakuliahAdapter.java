package com.example.test;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerViewMatakuliahAdapter extends RecyclerView.Adapter<RecyclerViewMatakuliahAdapter.ViewHolder> {

    private ArrayList<Matakuliah> rvData;


    public RecyclerViewMatakuliahAdapter(ArrayList<Matakuliah> dataSet) {

        rvData = dataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_matkul;
        public TextView tv_dosen;

        public ViewHolder(View v) {
            super(v);
            tv_matkul = (TextView) v.findViewById(R.id.tv_matkul);
            tv_dosen = (TextView) v.findViewById(R.id.tv_dosen);
        }
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_rv_item_matakuliah, parent, false);
        // mengeset ukuran view, margin, padding, dan parameter layout lainnya
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {

        holder.tv_matkul.setText(rvData.get(position).getNamaMatkul());
        holder.tv_dosen.setText(rvData.get(position).getNamaDosen());

        final String matkul = rvData.get(position).getNamaMatkul();
        final String dosen = rvData.get(position).getNamaDosen();

        holder.tv_dosen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                System.out.println("click recycle view: " + matkul);

            }
        });



    }

    @Override
    public int getItemCount() {
        return rvData.size();
    }



}
