package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Login extends AppCompatActivity {
    TextView ButtonSign_UP;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButtonSign_UP=findViewById(R.id.ButtonSign_UP);


        ButtonSign_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Regester.class);

            }
        });



    }
}