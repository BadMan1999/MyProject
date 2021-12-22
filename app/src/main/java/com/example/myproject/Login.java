package com.example.myproject;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class Login extends AppCompatActivity {
    TextView ButtonSign_UP;


    private EditText ed_UserName, ed_password;
    private Button button_Login;
    private FirebaseAuth mAuth;
    String Username,Password ;
    int year;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);



        mAuth = FirebaseAuth.getInstance();

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


        DatabaseReference rootReference = FirebaseDatabase.getInstance().getReference("BMI");
        rootReference.child("Users").child(ed_UserName.getText().toString()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                     year = snapshot.child("year").getValue(Integer.class);
                    String pass = snapshot.child("password").getValue(String.class);

                    Log.d("mypass",pass);
                    if (Password.equals(pass)){
                            Intent intent =  new Intent(Login.this,Home.class);
                            intent.putExtra("name",Username);
                            intent.putExtra("year",year);
                            startActivity(intent);


                    }else{
                        Toast.makeText(Login.this, "error password", Toast.LENGTH_SHORT).show();
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
        }
    }
}