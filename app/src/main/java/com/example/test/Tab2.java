package com.example.test;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

public class Tab2 extends Fragment {





//    private Button simpan;

    public Tab2(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        prepare();
    }

    //private OnFragmentInteractionListener mListener;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_tab2, container, false);
//        namaDosen = view.findViewById(R.id.namaDosen);
//        namaMatakuliah = view.findViewById(R.id.namaMataKuliah);
//        sks = view.findViewById(R.id.sks);
//
//        simpan = view.findViewById(R.id.simpanButton);
//        firebaseFirestoreDb = FirebaseFirestore.getInstance();
//
//        simpan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                tambahMahasiswa();
//            }
//        });

        return view;
    }



    private void prepare(){

        this.getChildFragmentManager().beginTransaction().add(R.id.list, new AddMahasiswa()).commit();
    }



    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
