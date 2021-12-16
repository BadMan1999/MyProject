package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {
    TextView ButtonSign_UP;

    FirebaseDatabase database;
    private DatabaseReference rootReference;

    private EditText ed_UserName, ed_password;
    private Button button_Login;
    private FirebaseAuth mAuth;
    String Username,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
         database = FirebaseDatabase.getInstance();

        rootReference =database.getReference("BMI");
        ButtonSign_UP = findViewById(R.id.ButtonSign_UP);
        button_Login = findViewById(R.id.button_Login);

        ed_password  =findViewById(R.id.password);
        ed_UserName  =findViewById(R.id.UserName);


        ButtonSign_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Regester.class);
                startActivity(intent);
            }
        });

        button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Login();
            }
        });


    }


    private void Login() {
        Username= ed_UserName.getText().toString().trim();
        Password= ed_password.getText().toString().trim();

        if (Username.isEmpty()) {
            ed_UserName.setError("User Name is required");
            ed_UserName.requestFocus();
            return ;
        }



        if (Password.isEmpty()) {
            ed_password.setError("Password is required");
            ed_password.requestFocus();
            return ;
        }

        if (Password.length() < 6) {
            ed_password.setError("Minimum lenght of password should be 6");
            ed_password.requestFocus();
            return;
        }

        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    String pass = snapshot.child("password").getValue(String.class);
                    String name = snapshot.child("name").getValue(String.class);

                    if (pass.equals(Password)){
                        if (name.equals(Username)){
                            Intent intent =  new Intent(Login.this,Home.class);
                            intent.putExtra("name",name);
                            startActivity(intent);

                        }else {
                            Toast.makeText(Login.this, "error username", Toast.LENGTH_SHORT).show();
                        }

                    }else{
                        Toast.makeText(Login.this, "error password", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError Db) {

                Toast.makeText(Login.this, Db.toString(), Toast.LENGTH_SHORT).show();
            }
        };

        rootReference.child("Users").child("XhjWbrIgTOdELJiiD5ttyRHXjHJ2").addListenerForSingleValueEvent(listener);

    }
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
        }
    }
}