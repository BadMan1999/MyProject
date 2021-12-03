package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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



    }
}