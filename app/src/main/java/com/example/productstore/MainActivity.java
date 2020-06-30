package com.example.productstore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.productstore.ViewHolder.ProductViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class MainActivity extends BaseActivity {
    LinearLayout dynamicContent, bottonNavBar;

    private BottomNavigationView bottomNavigationView;

    private DatabaseReference ProductsRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    private RadioGroup cat_rg;
    private RadioButton b1;
    private int categorySelected;

    private RadioButton home_care, cosmetics, apparel;




    //private String flagHomeCare = "Home Care", flagCosmetiics = "Cosmetics", flagApparel = "Apparel";
    private String categoryText, flagHomeCare = null, flagCosmetiics = null, flagApparel = null, category = "Home Care";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);

        dynamicContent = findViewById(R.id.dynamicContent);
        bottonNavBar = findViewById(R.id.bottonNavBar);

        View wizard = getLayoutInflater().inflate(R.layout.activity_main, null);
        dynamicContent.addView(wizard);

        RadioGroup rg = findViewById(R.id.radioGroup1);
        RadioButton rb = findViewById(R.id.store);

        rb.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_store_clicked, 0, 0);
        rb.setTextColor(Color.parseColor("#FFF596BC"));

        cat_rg = findViewById(R.id.cat_rg);

        ProductsRef = FirebaseDatabase.getInstance().getReference().child("Products");

        recyclerView = findViewById(R.id.recycler_menu);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        categorySelected = cat_rg.getCheckedRadioButtonId();
        b1 = (RadioButton) findViewById(categorySelected);
        categoryText = b1.getText().toString();


//        home_care.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });


    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseRecyclerOptions<Products> options =
                new FirebaseRecyclerOptions.Builder<Products>()
                        .setQuery(ProductsRef, Products.class)
                        .build();

        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        holder.txtProductName.setText(model.getProduct_name());
                        holder.txtProductCategory.setText(model.getCategory());
                        holder.txtProductRating.setText("Rating: " + model.getRating() + "/5");
                        Picasso.get().load(model.getImage1()).into(holder.imageView);

                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                                intent.putExtra("pid", model.getPid());
                                startActivity(intent);
                            }
                        });
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

        cat_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.all:{
                        FirebaseRecyclerOptions<Products> options =
                                new FirebaseRecyclerOptions.Builder<Products>()
                                        .setQuery(ProductsRef, Products.class)
                                        .build();

                        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                                        holder.txtProductName.setText(model.getProduct_name());
                                        holder.txtProductCategory.setText(model.getCategory());
                                        holder.txtProductRating.setText("Rating: " + model.getRating() + "/5");
                                        Picasso.get().load(model.getImage1()).into(holder.imageView);

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                                                intent.putExtra("pid", model.getPid());
                                                startActivity(intent);
                                            }
                                        });
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

                        break;
                    }

                    case R.id.home_care:{
                        FirebaseRecyclerOptions<Products> options =
                                new FirebaseRecyclerOptions.Builder<Products>()
                                        .setQuery(ProductsRef.orderByChild("category").equalTo("Home Care"), Products.class)
                                        .build();


                        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        /*if (model.getCategory().equals(flagHomeCare) ||
                                model.getCategory().equals(flagApparel) ||
                                model.getCategory().equals(flagCosmetiics)) {*/
                                        holder.txtProductName.setText(model.getProduct_name());
                                        holder.txtProductCategory.setText(model.getCategory());
                                        holder.txtProductRating.setText("Rating: " + model.getRating() + "/5");
                                        Picasso.get().load(model.getImage1()).into(holder.imageView);

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                                                intent.putExtra("pid", model.getPid());
                                                startActivity(intent);
                                            }
                                        });
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

                        break;
                    }

                    case R.id.cosmetics:{
                        FirebaseRecyclerOptions<Products> options =
                                new FirebaseRecyclerOptions.Builder<Products>()
                                        .setQuery(ProductsRef.orderByChild("category").equalTo("Cosmetics"), Products.class)
                                        .build();


                        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                                        holder.txtProductName.setText(model.getProduct_name());
                                        holder.txtProductCategory.setText(model.getCategory());
                                        holder.txtProductRating.setText("Rating: " + model.getRating() + "/5");
                                        Picasso.get().load(model.getImage1()).into(holder.imageView);

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                                                intent.putExtra("pid", model.getPid());
                                                startActivity(intent);
                                            }
                                        });
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

                        break;
                    }

                    case R.id.apparel:{

                        FirebaseRecyclerOptions<Products> options =
                                new FirebaseRecyclerOptions.Builder<Products>()
                                        .setQuery(ProductsRef.orderByChild("category").equalTo("Apparel"), Products.class)
                                        .build();


                        final FirebaseRecyclerAdapter<Products, ProductViewHolder> adapter =
                                new FirebaseRecyclerAdapter<Products, ProductViewHolder>(options) {
                                    @Override
                                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull final Products model) {
                        /*if (model.getCategory().equals(flagHomeCare) ||
                                model.getCategory().equals(flagApparel) ||
                                model.getCategory().equals(flagCosmetiics)) {*/
                                        holder.txtProductName.setText(model.getProduct_name());
                                        holder.txtProductCategory.setText(model.getCategory());
                                        holder.txtProductRating.setText("Rating: " + model.getRating() + "/5");
                                        Picasso.get().load(model.getImage1()).into(holder.imageView);

                                        holder.itemView.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Intent intent = new Intent(MainActivity.this, ProductDetailsActivity.class);
                                                intent.putExtra("pid", model.getPid());
                                                startActivity(intent);
                                            }
                                        });
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

                        break;
                    }
                }
            }
        });

    }
}
