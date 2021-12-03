package com.example.myproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class Add_Food_Details_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    String[] co = { " Fruit "," vegetables", "Starchy food", "Dairy"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_details);

        Spinner spin =  findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(Add_Food_Details_Activity.this);

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item, co);


        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spin.setAdapter(aa);
    }
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), co[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }
}