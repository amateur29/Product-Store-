package com.example.productstore;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productstore.ViewHolder.ProductViewHolder;
import com.squareup.picasso.Picasso;

public class ProductsAdapter extends RecyclerView.Adapter<ProductViewHolder> {
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context
                = parent.getContext();
        LayoutInflater inflater
                = LayoutInflater.from(context);

        View photoView = inflater.inflate(R.layout.card_view_items, parent, false);

        ProductViewHolder viewHolder = new ProductViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

//        holder.txtProductName.setText(model.getProduct_name());
//        holder.txtProductCategory.setText(model.getCategory());
//        holder.txtProductRating.setText("Rating: " + model.getRating() + "/5");
//        Picasso.get().load(model.getImage1()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
