package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    Button Btn_Add_Record,Btn_Add_Food;
    RecyclerView list;
    Adapter_Home adapter;
    Button btn_add_record,btn_add_food,btn_view_food;
    TextView txt_name_home;
    String name,weight,length,classified;
    Item_Old_Statues oldStatues;
    DatabaseReference Ref;
    TextView txt_logout,currentStateUser;
    int BMI,currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        length = getIntent().getExtras().getString("length");
        weight = getIntent().getExtras().getString("weight");


        Btn_Add_Record = findViewById(R.id.Btn_Add_Record);
        Btn_Add_Food = findViewById(R.id.Btn_Add_Food);


        Ref = FirebaseDatabase.getInstance().getReference().child("BMI").child("Record");
        list = findViewById(R.id.list);
        btn_add_record = findViewById(R.id.Btn_Add_Record);
        btn_add_food = findViewById(R.id.Btn_Add_Food);
        btn_view_food = findViewById(R.id.btn_view_food);
        txt_name_home = findViewById(R.id.txt_name_home);
        txt_logout = findViewById(R.id.txt_logout);
        currentStateUser = findViewById(R.id.currentStateUser);

        name = getIntent().getExtras().getString("name");
        currentDate = getIntent().getExtras().getInt("year");

        try {
            int AgePercent = 2021-currentDate;

            int Cmlength = Integer.parseInt(length);
            int Kgweight = Integer.parseInt(weight);
            int Mlength = Cmlength/100;

            if (Mlength != 0) {
                BMI = (Kgweight / Mlength ^ 2) * AgePercent;
            }

        }catch (Exception e){

            Toast.makeText(Home.this,e.getMessage().toString(), Toast.LENGTH_SHORT).show();        }

        ArrayList valuelist = new ArrayList<String>();
        valuelist.add("Little Changes");
        valuelist.add("Normal (Still Good)");
        valuelist.add("Go Ahead");
        valuelist.add("Be Careful");
        valuelist.add("So Bad");

        if (BMI < 18.5){
            classified = "Underweight";
            currentStateUser.setText(classified + valuelist.get(4).toString());

        }else if (18.5 <= BMI && BMI < 25){
            classified = "Healthy Weight";
            currentStateUser.setText(classified + valuelist.get(3).toString());

        }else if (25 <= BMI && BMI < 30){
            classified = "Overweight";
            currentStateUser.setText(classified + valuelist.get(1).toString());
        }else if (BMI > 30){
            classified = "Obesity";
            currentStateUser.setText(classified + valuelist.get(0).toString());

        }



        txt_name_home.setText("Hi, " + name);

        txt_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this,Login.class);
                startActivity(intent);
                finish();
            }
        });

        btn_add_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AddRecord_activity.class);
                intent.putExtra("currentDate",currentDate);
                startActivity(intent);
            }
        });

        btn_view_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, ViewFood.class);
                startActivity(intent);
            }
        });

        btn_add_food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Add_Food_Details_Activity.class);
                startActivity(intent);
            }
        });


        ArrayList<Item_Old_Statues> models = new ArrayList<>();
        list.setLayoutManager(new LinearLayoutManager(Home.this));


        Ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                models.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    models.add(oldStatues);

                }
                adapter = new Adapter_Home(models, Home.this);
                list.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}