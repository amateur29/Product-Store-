package com.example.productstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.productstore.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class WishlistActivity extends BaseActivity {
    LinearLayout dynamicContent,bottonNavBar;
    SharedPreferences sharedpreferences;

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    private String product_id;

    private int flag=0;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_wishlist);

        dynamicContent = (LinearLayout) findViewById(R.id.dynamicContent);
        bottonNavBar= (LinearLayout) findViewById(R.id.bottonNavBar);

        View wizard = getLayoutInflater().inflate(R.layout.activity_wishlist, null);
        dynamicContent.addView(wizard);

        RadioGroup rg=(RadioGroup)findViewById(R.id.radioGroup1);
        RadioButton rb=(RadioButton)findViewById(R.id.wishlist);

        rb.setCompoundDrawablesWithIntrinsicBounds( 0,R.drawable.ic_wishlist_clicked, 0,0);
        rb.setTextColor(Color.parseColor("#FFF596BC"));

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        sharedpreferences = getSharedPreferences("WishlistSP",
                Context.MODE_PRIVATE);

//        Map<String, String> mPids = new HashMap<>();
//        mPids = (Map<String, String>) sharedpreferences.getAll();
//
//        for (Map.Entry<String, String> entry : mPids.entrySet()) {
//            product_id = entry.getKey();
//            getProductDetails(product_id);
//        }
    }

//    private void getProductDetails(String product_id) {
//        DatabaseReference productRef = FirebaseDatabase.getInstance().getReference().child("Products");
//
//        productRef.child(product_id).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(  DataSnapshot dataSnapshot) {
//
//                if (dataSnapshot.exists()){
//                    Products products = dataSnapshot.getValue(Products.class);
//
////                    product_name.setText(products.getProduct_name());
////                    product_desc.setText(products.getDescription());
////                    product_cat.setText(products.getCategory());
////                    product_rat.setText(products.getRating()+"/5");
////                    Picasso.get().load(products.getImage1()).into(product_img);
//
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//    }


    @Override
    protected void onStart() {
        super.onStart();

        sharedpreferences = getSharedPreferences("WishlistSP", Context.MODE_PRIVATE);

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();

        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        if(sharedpreferences.contains(model.getPid())){
                            flag=1;
                            holder.txtProductName.setText(model.getProduct_name());
                            holder.txtProductCategory.setText(model.getCategory());
                            holder.txtProductRating.setText("Rating: " + model.getRating() + "/5");
                            Picasso.get().load(model.getImage1()).into(holder.imageView);

                            holder.itemView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(WishlistActivity.this, ProductDetailsActivity.class);
                                    intent.putExtra("pid", model.getPid());
                                    startActivity(intent);
                                }
                            });
                        }
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_items, parent, false);
                        ProductViewHolder holder = new ProductViewHolder(view);
                        return holder;
                    }
                };

            recyclerView.setAdapter(adapter);
            adapter.startListening();
    }
}
