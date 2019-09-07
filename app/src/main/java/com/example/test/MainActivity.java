package com.example.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button)findViewById(R.id.login);



        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(cekEmail()){
                    Toast.makeText(getApplicationContext(),"valid email address",Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(MainActivity.this, user.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(getApplicationContext(),"Invalid email address",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public boolean cekEmail(){

        EditText txt = (EditText) findViewById(R.id.email);


        final String emailx = txt.getText().toString().trim();

        final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (emailx.matches(emailPattern))
        {

            return  true;
        }
        else
        {
            return  false;
            //or

        }

    }
}
