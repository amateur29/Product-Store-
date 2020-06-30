package com.example.productstore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductDetailsActivity extends AppCompatActivity {

    private ImageView product_img1, product_img2, product_img3, wishlistImg;
    private TextView product_name, product_desc, product_cat, product_rat;
    private String product_id = "";

//    private RecyclerView recyclerView;
//    private ImageRecyclerViewAdapter imageAdapter;

    private Button shareBtn;
    private int flag=0;

//    List<String> imageList = new ArrayList<String>();

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);

        product_img1 = findViewById(R.id.product_img1);
//        product_img2 = findViewById(R.id.product_img2);
//        product_img3 = findViewById(R.id.product_img3);
        product_name = findViewById(R.id.product_name);
        product_desc = findViewById(R.id.product_desc);
        product_cat = findViewById(R.id.product_cat);
        product_rat = findViewById(R.id.product_rat);

//        recyclerView = findViewById(R.id.recycler_view);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        shareBtn = findViewById(R.id.share);
        wishlistImg = findViewById(R.id.wishlist);

        product_id = getIntent().getStringExtra("pid");

        sharedpreferences = getSharedPreferences("WishlistSP", Context.MODE_PRIVATE);

        if(sharedpreferences.contains(product_id)){
            wishlistImg.setImageResource(R.drawable.ic_wishlist_clicked);
            flag=1;
        }
        else{
            wishlistImg.setImageResource(R.drawable.ic_wishlist);
            flag=0;
        }

        getProductDetails(product_id);

//        imageAdapter = new ImageRecyclerViewAdapter(imageList);
//        recyclerView.setAdapter(imageAdapter);
    }

    private void getProductDetails(final String product_id) {

        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
        productRef.child(product_id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(  DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    Products products = dataSnapshot.getValue(Products.class);

                    product_name.setText(products.getProduct_name());
                    product_desc.setText(products.getDescription());
                    product_cat.setText(products.getCategory());
                    product_rat.setText(products.getRating()+"/5");
                    Picasso.get().load(products.getImage1()).into(product_img1);
//                    Picasso.get().load(products.getImage2()).into(product_img2);
//                    Picasso.get().load(products.getImage3()).into(product_img3);

//                    imageList.add(products.getImage1());
//                    imageList.add(products.getImage2());
//                    imageList.add(products.getImage3());


                    shareBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            String shareBody = "Product Name: " + product_name.getText().toString() + "\nDescription: " + product_desc.getText().toString();
                            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject Here");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                            startActivity(Intent.createChooser(sharingIntent, "Share via"));
                        }
                    });

                    wishlistImg.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            SharedPreferences.Editor editor = sharedpreferences.edit();
                            if(flag==0){
                                editor.putString(product_id,"pid");
                                editor.commit();
                                wishlistImg.setImageResource(R.drawable.ic_wishlist_clicked);
                                flag=1;
                            }
                            else if(flag==1){
                                editor.remove(product_id);
                                editor.commit();
                                wishlistImg.setImageResource(R.drawable.ic_wishlist);
                                flag=0;
                            }
                        }
                    });

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


}
