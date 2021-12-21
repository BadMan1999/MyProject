package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
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
    String Xdate;
    int Xyear;

    DatePickerDialog.OnDateSetListener dateSetListener1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_information);

        mAuth = FirebaseAuth.getInstance();
        Reference = FirebaseDatabase.getInstance().getReference().child("BMI");
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

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/mm/yyyy");
        String date = simpleDateFormat.format(Calendar.getInstance().getTime());

        ed_date_Of_Birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog datePickerDialog = new DatePickerDialog(Complete_Information_Activity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateSetListener1, year, month, day
                );
                // to set background for datepicker
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        // it is used to set teh date which user selects
        dateSetListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // here month+1 is used so that
                // actual month number can be displayed
                // otherwise it starts from 0 and it shows
                // 1 number less for every month
                // example- for january month=0
                month = month + 1;
                 Xdate = day + "/" + month + "/" + year;
                 ed_date_Of_Birth.setText(Xdate);
                Xyear = year;

            }
        };

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
                    Map.put("date of birth", Xdate);
                    Map.put("year", Xyear);

                    Reference.child("Users").child(name).setValue(Map);
                    Reference= FirebaseDatabase.getInstance().getReference().child("BMI");
                    Intent intent = new Intent(Complete_Information_Activity.this, Home.class);
                    intent.putExtra("name",name);
                    intent.putExtra("year",Xyear+"");
                    intent.putExtra("length",c2+"");
                    intent.putExtra("weight",c1+"");
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

}