package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Add_Food_Details_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{




    Button btn_save;
    ImageView imageView;


    private String saveCurrentDate, Name, Category, Calory;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String FoodRandomKey, downloadImageUrl;
    private StorageReference FoodImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;
    EditText ed_Name, ed_Category, ed_Calory;



    String[] co = { " Fruit "," vegetables", "Starchy food", "Dairy"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_details);


        btn_save = (Button) findViewById(R.id.btn_save);
        imageView = (ImageView) findViewById(R.id.img_food);
        ed_Name = (EditText) findViewById(R.id.ed_Name);
        ed_Category = (EditText) findViewById(R.id.ed_Category);
        ed_Calory = (EditText) findViewById(R.id.ed_Calory);



//        Spinner s =  findViewById(R.id.spinner);
//        s.setOnItemSelectedListener(Add_Food_Details_Activity.this);
//
//        ArrayAdapter a = new ArrayAdapter(this,android.R.layout.simple_spinner_item, co);
//
//
//        a.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//
//        s.setAdapter(a);





















    }


    private void OpenGallery() {
        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, GalleryPick);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GalleryPick && resultCode == RESULT_OK && data != null) {
            ImageUri = data.getData();
            imageView.setImageURI(ImageUri);
        }
    }


    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(), co[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void ValidateProductData() {
        Name = ed_Name.getText().toString();
        Category = ed_Category.getText().toString();
        Calory = ed_Calory.getText().toString();


        if (ImageUri == null) {
            Toast.makeText(this, "Product image is mandatory...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Please write Name ...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Category)) {
            Toast.makeText(this, "Please write Category...", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Calory)) {
            Toast.makeText(this, "Please write Calory...", Toast.LENGTH_SHORT).show();
        } else {
            StoreProductInformation();
        }
    }


    private void StoreProductInformation() {
        loadingBar.setTitle("Add ");
        loadingBar.setMessage("Dear Admin, please wait while we are adding the new food.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");




        final StorageReference filePath = FoodImagesRef.child(ImageUri.getLastPathSegment() +  ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);


        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(Add_Food_Details_Activity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(Add_Food_Details_Activity.this, "Food Image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(Add_Food_Details_Activity.this, "got the Food image Url Successfully...", Toast.LENGTH_SHORT).show();

                            SaveFoodInfoToDatabase();
                        }
                    }
                });
            }
        });
    }


    private void SaveFoodInfoToDatabase() {
        HashMap<String, Object> ChaletMap = new HashMap<>();
        ChaletMap.put("pid", FoodRandomKey);
        ChaletMap.put("date", saveCurrentDate);
        ChaletMap.put("name", Name);
        ChaletMap.put("image", downloadImageUrl);
        ChaletMap.put("Category", Category);
        ChaletMap.put("Calory", Calory);



        ProductsRef.child(FoodRandomKey).updateChildren(ChaletMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(Add_Food_Details_Activity.this, Home.class);
                            startActivity(intent);

                            loadingBar.dismiss();
                            Toast.makeText(Add_Food_Details_Activity.this, " added successfully..", Toast.LENGTH_SHORT).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(Add_Food_Details_Activity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }





}