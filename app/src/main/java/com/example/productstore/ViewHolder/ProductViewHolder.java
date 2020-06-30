package com.example.productstore.ViewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.productstore.Interface.ItemClickListener;
import com.example.productstore.R;

public class ProductViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName, txtProductCategory, txtProductRating;
    public ImageView imageView;
    public ItemClickListener listener;


    public ProductViewHolder(View itemView)
    {
        super(itemView);

        imageView = (ImageView) itemView.findViewById(R.id.image);
        txtProductName = (TextView) itemView.findViewById(R.id.pname);
        txtProductCategory = (TextView) itemView.findViewById(R.id.pcat);
        txtProductRating = (TextView) itemView.findViewById(R.id.prat);
    }

    public void setItemClickListner(ItemClickListener listener)
    {
        this.listener = listener;
    }

    @Override
    public void onClick(View view)
    {
        listener.onClick(view, getAdapterPosition(), false);
    }
}
