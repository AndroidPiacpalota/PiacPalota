package com.example.piacpalota.u.i.buylist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.piacpalota.R;

import java.util.List;

public class BuyAdapter extends RecyclerView.Adapter<BuyAdapter.BuyViewHolder> {

    private Context context;
    private List<Product> productList;
    private OnProductClickListener onProductClickListener;

    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    public BuyAdapter(Context context, List<Product> productList, OnProductClickListener onProductClickListener) {
        this.context = context;
        this.productList = productList;
        this.onProductClickListener = onProductClickListener;
    }

    @NonNull
    @Override
    public BuyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_buy, parent, false);
        return new BuyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyViewHolder holder, int position) {
        Product product = productList.get(position);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(product.getPrice());
        holder.productLocation.setText(product.getLocation());

        // Kép betöltése Glide használatával
        Glide.with(context)
                .load(product.getImageUrl()) // Kép URL
                .placeholder(R.drawable.placeholder) // Alapértelmezett kép
                .into(holder.productImage);

        // Kattintás esemény: termék bővebb információinak megjelenítése
        holder.cardView.setOnClickListener(v -> {
            boolean isExpanded = product.isExpanded();
            product.setExpanded(!isExpanded); // Állapot váltás

            // Bővebb információk láthatóságának váltása
            holder.productDetails.setVisibility(isExpanded ? View.GONE : View.VISIBLE);
            notifyItemChanged(position); // Frissítjük a listát
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class BuyViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;
        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView productLocation;
        LinearLayout productDetails; // Bővebb információk szekciója

        public BuyViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.card_view);
            productImage = itemView.findViewById(R.id.product_image);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            productLocation = itemView.findViewById(R.id.product_location);
            productDetails = itemView.findViewById(R.id.product_details); // Bővebb információk szekciója
        }
    }
}
