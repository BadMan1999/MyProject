package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class AddRecord_activity extends AppCompatActivity {
    private String weight,length,date,time;

    private DatabaseReference Reference;
    FirebaseAuth mAuth;
    String key;

    EditText ed_weight,ed_length,ed_date,ed_time;

    Button btn_save_data_record;
    String classified, result;
    int currentDate;

    int c1, c2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_record);
        mAuth = FirebaseAuth.getInstance();

        Reference = FirebaseDatabase.getInstance().getReference("BMI");

        currentDate = getIntent().getExtras().getInt("currentDate");



        btn_save_data_record =  findViewById(R.id.button_save_data_record);

        ed_weight =  findViewById(R.id.ed_weight);
        ed_length =  findViewById(R.id.ed_length);
        ed_date =  findViewById(R.id.ed_date);
        ed_time =  findViewById(R.id.ed_time);




        ed_length.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (!ed_length.getText().toString().isEmpty()) {
                    c2 = Integer.parseInt(ed_length.getText().toString());
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (ed_length.getRight() - ed_length.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        c2++;
                        ed_length.setText(c2 +"");
                        return true;
                    } else if (event.getRawX() >= (ed_length.getLeft() - ed_length.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        c2--;
                        ed_length.setText(c2 +"");
                        return true;
                    }else {
                    }
                }
                return false;
            }
        });

        ed_weight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (!ed_weight.getText().toString().isEmpty()) {
                    c1 = Integer.parseInt(ed_weight.getText().toString());
                }

                if(event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (ed_weight.getRight() - ed_weight.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        c1++;
                        ed_weight.setText(c1 +"");
                        return true;
                    } else if (event.getRawX() >= (ed_weight.getLeft() - ed_weight.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        c1--;
                        ed_weight.setText(c1 +"");
                        return true;
                    }else {
                    }
                }
                return false;
            }
        });

        btn_save_data_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateRecordData();
            }
        });



    }


    private void ValidateRecordData() {
        date = ed_date.getText().toString();

        time = ed_time.getText().toString();




        int AgePercent = 2021-currentDate;

        int Cmlength = Integer.parseInt(length);

        int Kgweight = Integer.parseInt(weight);

        int Mlength = Cmlength/100;


        int BMI = (Kgweight/Mlength^2)*AgePercent;

        ArrayList valuelist = new ArrayList<String>();
        valuelist.add("Little Changes");
        valuelist.add("Normal (Still Good)");
        valuelist.add("Go Ahead");
        valuelist.add("Be Careful");
        valuelist.add("So Bad");

        if (BMI < 18.5){
            classified = "Underweight";
            result = classified +" "+ valuelist.get(4).toString();

        }else if (18.5 <= BMI && BMI < 25){
            classified = "Healthy Weight";
            result = classified +" "+ valuelist.get(3).toString();

        }else if (25 <= BMI && BMI < 30){
            classified = "Overweight";
            result = classified +" "+valuelist.get(1).toString();
        }else if (BMI > 30){
            classified = "Obesity";
            result = classified +" "+ valuelist.get(0).toString();

        }
        SaveRecordInfoToDatabase();

    }




    private void SaveRecordInfoToDatabase() {
        HashMap<String, Object> ChaletMap = new HashMap<>();
        ChaletMap.put("weight", c1 +" kg");
        ChaletMap.put("length", c2 +" cm");
        ChaletMap.put("date", date);
        ChaletMap.put("time", time);
        ChaletMap.put("curren status", result);


         key = FirebaseDatabase.getInstance().getReference("Users").push().getKey();
        Reference.child("Record").child(key).updateChildren(ChaletMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent idToList = new Intent(AddRecord_activity.this, Home.class);
                            startActivity(idToList);

                            Toast.makeText(AddRecord_activity.this, "Food is added successfully..", Toast.LENGTH_SHORT).show();

                        } else {
                            String message = task.getException().toString();
                            Toast.makeText(AddRecord_activity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

