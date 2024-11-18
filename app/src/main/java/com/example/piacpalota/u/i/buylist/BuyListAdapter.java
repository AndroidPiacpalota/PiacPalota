package com.example.piacpalota.u.i.buylist;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.Filter;
import android.widget.Filterable;
import com.example.piacpalota.R;
import com.example.piacpalota.domain.Buy;

import java.util.ArrayList;
import java.util.List;

public class BuyListAdapter extends RecyclerView.Adapter<BuyListAdapter.BuyHolder> implements Filterable {

    private List<Buy> buyList;
    private List<Buy> buyListFull; // A teljes lista másolat a szűréshez
    private BuyFragment fragment;

    public BuyListAdapter(List<Buy> buyList, BuyFragment fragment) {
        this.buyList = buyList;
        this.fragment = fragment;
        buyListFull = new ArrayList<>(buyList); // Eredeti lista másolat készítése
    }

    @NonNull
    @Override
    public BuyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.buy_list_row, parent, false);
        return new BuyHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull BuyHolder holder, int position) {
        Buy buy = buyList.get(position);
        holder.termekTextView.setText(buy.getTermek());
        holder.kategoriaTextView.setText(buy.getKategoria());
        holder.helyTextView.setText(buy.getHely());

        holder.itemView.setOnClickListener(v -> {
            fragment.showBuyDetails(buy);
        });
    }

    @Override
    public int getItemCount() {
        return buyList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Buy> filteredList = new ArrayList<>();

                if (constraint == null || constraint.length() == 0) {
                    filteredList.addAll(buyListFull);
                } else {
                    String filterPattern = constraint.toString().toLowerCase().trim();

                    for (Buy item : buyListFull) {
                        if (item.getTermek().toLowerCase().contains(filterPattern) ||
                                item.getKategoria().toLowerCase().contains(filterPattern) ||
                                item.getHely().toLowerCase().contains(filterPattern)) {
                            filteredList.add(item);
                        }
                    }
                }

                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                buyList.clear();
                buyList.addAll((List) results.values);
                notifyDataSetChanged();
            }
        };
    }

    public class BuyHolder extends RecyclerView.ViewHolder {
        public TextView termekTextView;
        public TextView kategoriaTextView;
        public TextView helyTextView;

        public BuyHolder(@NonNull View itemView) {
            super(itemView);
            termekTextView = itemView.findViewById(R.id.termekTextView);
            kategoriaTextView = itemView.findViewById(R.id.kategoriaTextView);
            helyTextView = itemView.findViewById(R.id.helyTextView);
        }
    }
}
