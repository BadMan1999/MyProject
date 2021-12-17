package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Complete_Information_Activity extends AppCompatActivity {
    Button button_save_data;

     RadioGroup radioGroup;

     RadioButton radioSexButton;

    int dateOfBirth_year;

    String value,date_Of_Birth;

    EditText ed_Weight, ed_Length, ed_date_Of_Birth;
    int c1, c2;

    private DatabaseReference Reference;

    String uid,name,email,password;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_information);

        mAuth = FirebaseAuth.getInstance();

        name = getIntent().getExtras().getString("name");
        uid = getIntent().getExtras().getString("uid");

        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");

        button_save_data = findViewById(R.id.btn_save_data);
        radioGroup = findViewById(R.id.radio_Group);
        ed_Weight = findViewById(R.id.ed_Weight);

        ed_Length = findViewById(R.id.ed_Length);
        ed_date_Of_Birth = findViewById(R.id.ed_date_Of_Birth);

        button_save_data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addListenerOnButton();
                SaveInfoToDatabase();

            }
        });


        ed_Weight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;

                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (!ed_Weight.getText().toString().isEmpty()) {
                    c1 = Integer.parseInt(ed_Weight.getText().toString());
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (ed_Weight.getRight() - ed_Weight.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        c1++;
                        ed_Weight.setText(c1 +"");
                        return true;
                    } else if (event.getRawX() >= (ed_Weight.getLeft() - ed_Weight.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        c1--;
                        ed_Weight.setText(c1 +"");
                        return true;
                    }else {
                    }
                }
                return false;
            }
        });

        ed_Length.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (!ed_Length.getText().toString().isEmpty()) {
                    c2 = Integer.parseInt(ed_Length.getText().toString());
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (ed_Length.getRight() - ed_Length.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        c2++;
                        ed_Length.setText(c2 +"");
                        return true;
                    } else if (event.getRawX() >= (ed_Length.getLeft() - ed_Length.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        c2--;
                        ed_Length.setText(c2 +"");
                        return true;
                    }else {
                    }
                }
                return false;
            }
        });

        ed_date_Of_Birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment date = new Date();
                date.show(getSupportFragmentManager(), "date");
            }
        });

    }
    public void addListenerOnButton() {
        // get selected radio button from radioGroup
        int selectedId = radioGroup.getCheckedRadioButtonId();

        // find the radiobutton by returned id
        radioSexButton =  findViewById(selectedId);

        value = radioSexButton.getText().toString();
        Toast.makeText(Complete_Information_Activity.this, value, Toast.LENGTH_SHORT).show();
    }




    private void SaveInfoToDatabase() {
        date_Of_Birth = ed_date_Of_Birth.getText().toString();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    uid = mAuth.getCurrentUser().getUid();
                    finish();
                    HashMap Map =new HashMap();
                    Map.put("Uid",uid);
                    Map.put("name",name);
                    Map.put("email",email);
                    Map.put("password",password+"");
                    Map.put("gender", value);
                    Map.put("weight", c1 + " kg");
                    Map.put("length", c2 +" cm");
                    Map.put("date of birth", date_Of_Birth);

                    Reference.child("Users").child(uid).setValue(Map);

                    Intent intent = new Intent(Complete_Information_Activity.this, Home.class);
                    intent.putExtra("name",name);
                    intent.putExtra("year",dateOfBirth_year+"");
                    startActivity(intent);

                }
                else {

                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                        Toast.makeText(getApplicationContext(), "You are already registered", Toast.LENGTH_SHORT).show();

                    } else {
                        Toast.makeText(getApplicationContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }


        });


    }

    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        dateOfBirth_year = year;
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());

        ed_date_Of_Birth.setText(currentDateString);
    }
}