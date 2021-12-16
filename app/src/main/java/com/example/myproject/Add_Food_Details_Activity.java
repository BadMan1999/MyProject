package com.example.myproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class Add_Food_Details_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{




    Button btn_save,btn_upload_photo;
    ImageView imageView;


    private String saveCurrentDate;
     String Name;
     String Category;
     String Calory;
     String key;

    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String FoodRandomKey, downloadImageUrl;
    private StorageReference FoodImagesRef;
    private DatabaseReference Reference;
    private ProgressDialog loadingBar;
    EditText ed_Name, ed_Category, ed_Calory;
    FirebaseAuth mAuth;


    String[] co = { " Fruit "," vegetables", "Starchy food", "Dairy"};
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_food_details);

        mAuth = FirebaseAuth.getInstance();

        FoodImagesRef = FirebaseStorage.getInstance().getReference().child("Food Images");
        Reference = FirebaseDatabase.getInstance().getReference("BMI");




        loadingBar = new ProgressDialog(this);


        Spinner spin =  findViewById(R.id.spinner_category);
        ed_Name =  findViewById(R.id.ed_Name);

        ed_Calory=findViewById(R.id.ed_Calory);

        btn_save =  findViewById(R.id.btn_save);
        imageView =  findViewById(R.id.img_food);
        btn_upload_photo =  findViewById(R.id.btn_upload_photo);

        spin.setOnItemSelectedListener(Add_Food_Details_Activity.this);
        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Category = spin.getItemAtPosition(position).toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,co);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spin.setAdapter(aa);



        btn_upload_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OpenGallery();
            }
        });


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ValidateProductData();
            }
        });



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


    private void ValidateProductData() {
        Name = ed_Name.getText().toString();

        Calory = ed_Calory.getText().toString();



        if (ImageUri == null) {
            Toast.makeText(this, "Food image is Mandatory", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Name)) {
            Toast.makeText(this, "Please write Name", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Category)) {
            Toast.makeText(this, "Please write Category food", Toast.LENGTH_SHORT).show();
        } else if (TextUtils.isEmpty(Calory)) {
            Toast.makeText(this, "Please write Calory food", Toast.LENGTH_SHORT).show();
        } else {
            StoreFoodInformation();
        }
    }


    private void StoreFoodInformation() {
        loadingBar.setTitle("Add New Food");
        loadingBar.setMessage("Dear user, please wait while we are adding the new Food.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();





        final StorageReference filePath = FoodImagesRef.child(ImageUri.getLastPathSegment() + FoodRandomKey + ".jpg");

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
        ChaletMap.put("name", Name);
        ChaletMap.put("calory", Calory+" cal /g");
        ChaletMap.put("image", downloadImageUrl);
        ChaletMap.put("category", Category);

        ChaletMap.put("key", key);



        key = FirebaseDatabase.getInstance().getReference("Users").push().getKey();
        Reference.child("Food").child(key).updateChildren(ChaletMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            loadingBar.dismiss();
                            Toast.makeText(Add_Food_Details_Activity.this, "Food is added successfully..", Toast.LENGTH_SHORT).show();

                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(Add_Food_Details_Activity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getApplicationContext(),co[position] , Toast.LENGTH_LONG).show();
    }
    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

}
