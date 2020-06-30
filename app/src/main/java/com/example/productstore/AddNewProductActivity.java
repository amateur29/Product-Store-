package com.example.productstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddNewProductActivity extends BaseActivity {
    LinearLayout dynamicContent,bottonNavBar;

    private String name, description, category, rating, saveCurrentDate, saveCurrentTime;
    private EditText pname, pdesc;

    private RadioGroup categoryrg, ratingrg;
    private static final String TAG = "MainActivity";

    private ImageView image1, image2, image3;
    private Button b1, b2, b3, addNewProdBtn;
    private int s = 0, categoryid, ratingid;

    private Uri ImageUri1, ImageUri2, ImageUri3;
    private String productRandomKey, downloadImageUrl1, downloadImageUrl2, downloadImageUrl3;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductsRef;
    private ProgressDialog loadingBar;

    private static final int IMAGE_PICK_CODE = 1000;
    private static final int PERMISSION_CODE = 1001;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_add_new_product);

        dynamicContent = (LinearLayout)  findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);
        loadingBar = new ProgressDialog(this);

        View wizard = getLayoutInflater().inflate(R.layout.activity_add_new_product, null);
        dynamicContent.addView(wizard);

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.add);

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_add_clicked, 0,0);
        rb.setTextColor(Color.parseColor("#FFF596BC"));


//        category = getIntent().getExtras().get("category").toString();
//        Toast.makeText(this, category, Toast.LENGTH_SHORT).show();

        ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        pname = (EditText) findViewById(R.id.product_name);
        pdesc = (EditText) findViewById(R.id.product_description);

        categoryrg = (RadioGroup) findViewById(R.id.category);
        ratingrg = (RadioGroup) findViewById(R.id.rating);

        image1 = (ImageView) findViewById(R.id.image1);
        image2 = (ImageView) findViewById(R.id.image2);
        image3 = (ImageView) findViewById(R.id.image3);

        addNewProdBtn = (Button) findViewById(R.id.add_new_product);


        image1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                s=1;
            }
        });

        image2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                s=2;
            }
        });

        image3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check();
                s=3;
            }
        });

        signInAnonymously();
    }

    private void signInAnonymously() {
        mAuth.signInAnonymously().addOnSuccessListener(this, new  OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                // do your stuff
                addNewProdBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        categoryid = categoryrg.getCheckedRadioButtonId();
                        ratingid = ratingrg.getCheckedRadioButtonId();
                        b1 = (RadioButton) findViewById(categoryid);
                        category = b1.getText().toString();
                        b2 = (RadioButton) findViewById(ratingid);
                        rating = b2.getText().toString();
                        ValidateProductData();
                    }
                });
            }
        })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Log.e(TAG, "signInAnonymously:FAILURE", exception);
                    }
                });
    }



    private void check() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_DENIED){
                //permission not granted

                String[] permission = {Manifest.permission.READ_EXTERNAL_STORAGE};
                requestPermissions(permission, PERMISSION_CODE);
            }
            else{
                //permission granted
                pickImageFromGallery();

            }
        }
        else{
            pickImageFromGallery();
        }
    }

    private void pickImageFromGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, IMAGE_PICK_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode){
            case PERMISSION_CODE:{
                if(grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                    pickImageFromGallery();
                }

                else{
                    Toast.makeText(this, "Permission not granted!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == IMAGE_PICK_CODE){
            switch(s){
                case 1:
                    ImageUri1=data.getData();
                    image1.setImageURI(ImageUri1);
                    break;

                case 2:
                    ImageUri2=data.getData();
                    image2.setImageURI(ImageUri2);
                    break;

                case 3:
                    ImageUri3=data.getData();
                    image3.setImageURI(ImageUri3);
                    break;
            }
        }
    }

    private void ValidateProductData() {
        description = pdesc.getText().toString();
        name = pname.getText().toString();


        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "Please write product name...", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(description)){
            Toast.makeText(this, "Please write product description...", Toast.LENGTH_SHORT).show();
        }
        else if(ImageUri1==null || ImageUri2==null || ImageUri3==null){
            Toast.makeText(this, "Please pick three images!", Toast.LENGTH_SHORT).show();
        }
        else{
            StoreProductInformation();
        }

    }

    private void StoreProductInformation() {

        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please wait while we are adding the new product.");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("MMM dd, yyyy");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;

        if(ImageUri1!=null){
            final StorageReference filePath1 = ProductImagesRef.child(ImageUri1.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask1 = filePath1.putFile(ImageUri1);

            uploadTask1.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(AddNewProductActivity.this, "First image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask1.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            downloadImageUrl1 = filePath1.getDownloadUrl().toString();
                            return filePath1.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl1 = task.getResult().toString();

                                Toast.makeText(AddNewProductActivity.this, "Got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                                SaveProductInfoToDatabase();
                            }
                        }
                    });
                }
            });
        }

        if(ImageUri2!=null){
            final StorageReference filePath2 = ProductImagesRef.child(ImageUri2.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask2 = filePath2.putFile(ImageUri2);

            uploadTask2.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(AddNewProductActivity.this, "Second image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask2.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            downloadImageUrl2 = filePath2.getDownloadUrl().toString();
                            return filePath2.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl2 = task.getResult().toString();

                                Toast.makeText(AddNewProductActivity.this, "Got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                                SaveProductInfoToDatabase();
                            }
                        }
                    });
                }
            });
        }


        if(ImageUri3!=null){
            final StorageReference filePath3 = ProductImagesRef.child(ImageUri3.getLastPathSegment() + productRandomKey + ".jpg");
            final UploadTask uploadTask3 = filePath3.putFile(ImageUri3);

            uploadTask3.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e)
                {
                    String message = e.toString();
                    Toast.makeText(AddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
                {
                    Toast.makeText(AddNewProductActivity.this, "Third image uploaded Successfully...", Toast.LENGTH_SHORT).show();

                    Task<Uri> urlTask = uploadTask3.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception
                        {
                            if (!task.isSuccessful())
                            {
                                throw task.getException();
                            }

                            downloadImageUrl3 = filePath3.getDownloadUrl().toString();
                            return filePath3.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task)
                        {
                            if (task.isSuccessful())
                            {
                                downloadImageUrl3 = task.getResult().toString();

                                Toast.makeText(AddNewProductActivity.this, "Got the Product image Url Successfully...", Toast.LENGTH_SHORT).show();

                                SaveProductInfoToDatabase();
                            }
                        }
                    });
                }
            });

            SaveProductInfoToDatabase();
        }
    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();
        productMap.put("pid", productRandomKey);
        productMap.put("product_name", name);
        productMap.put("description", description);
        productMap.put("category", category);
        productMap.put("rating", rating);
        productMap.put("image1", downloadImageUrl1);
        productMap.put("image2", downloadImageUrl2);
        productMap.put("image3", downloadImageUrl3);

        ProductsRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loadingBar.dismiss();
                            Intent intent = new Intent(AddNewProductActivity.this, MainActivity.class);
                            startActivity(intent);

                            Toast.makeText(AddNewProductActivity.this, "Product is added successfully..", Toast.LENGTH_LONG).show();
                        } else {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AddNewProductActivity.this, "Error: " + message, Toast.LENGTH_SHORT).show();
                        }
                    }
        });
    }


}
