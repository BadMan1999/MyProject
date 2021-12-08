package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    TextView ButtonSign_UP;



    private EditText ed_UserName, ed_password;
    private Button button_Login;
    private FirebaseAuth mAuth;
    String Email,Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButtonSign_UP=findViewById(R.id.ButtonSign_UP);

        ed_UserName =findViewById(R.id.UserName);
        ed_password =findViewById(R.id.password);
        button_Login =findViewById(R.id.button_Login);



        ButtonSign_UP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Login.this,Regester.class);

            }
        });

        mAuth = FirebaseAuth.getInstance();


        button_Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                singin();
            }
        });
    }

    private void singin() {
        Email= ed_password.getText().toString().trim();
        Password= ed_UserName.getText().toString().trim();
        if (Email.isEmpty()) {
            ed_password.setError("Email is required");
            ed_password.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(Email).matches()) {
            ed_password.setError("Please enter a valid email");
            ed_password.requestFocus();
            return;
        }

        if (Password.isEmpty()) {
            ed_UserName.setError("Password is required");
            ed_UserName.requestFocus();
            return;
        }

        if (Password.length() < 6) {
            ed_UserName.setError("Minimum lenght of password should be 6");
            ed_UserName.requestFocus();
            return;
        }
        mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {
                    finish();
                    Intent intent = new Intent(Login.this, Home.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (mAuth.getCurrentUser() != null) {
            finish();
            startActivity(new Intent(this, Home.class));
        }
        
        
        




    }
}