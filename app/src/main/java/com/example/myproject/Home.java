package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    Button Btn_Add_Record,Btn_Add_Food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Btn_Add_Record = findViewById(R.id.Btn_Add_Record);
        Btn_Add_Food = findViewById(R.id.Btn_Add_Food);

        RecyclerView list;
        Adapter_Home adapter;


        list = findViewById(R.id.list);
        ArrayList<Item_Old_Statues> models = new ArrayList<>();
        models.add(new Item_Old_Statues("20/1/2020","60 Kg","170 Cm"));
        models.add(new Item_Old_Statues("20/1/2020","60 Kg","170 Cm"));
        models.add(new Item_Old_Statues("20/1/2020","60 Kg","170 Cm"));
        list.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Adapter_Home(models, this);
        list.setAdapter(adapter);


        Btn_Add_Record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AddRecord_activity.class);
                startActivity(intent);
            }
        });



        Btn_Add_Food.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Add_Food_Details_Activity.class);
                startActivity(intent);
            }
        });



    }
}