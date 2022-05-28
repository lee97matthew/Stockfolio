package com.example.stockfolio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FavoritedStocksRecycleViewAdapter extends RecyclerView.Adapter<FavoritedStocksRecycleViewAdapter.MyViewHolder> {

    List<Stock.StockPreview> favStocks;
    Context context;
    private FavoritedStockListener mFavoritedStockListener;

    public FavoritedStocksRecycleViewAdapter(List<Stock.StockPreview> favStocks, Context context, FavoritedStockListener favoritedStockListener) {
        this.favStocks = favStocks;
        this.context = context;
        this.mFavoritedStockListener = favoritedStockListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_stock, parent, false);
        MyViewHolder holder = new MyViewHolder(view, mFavoritedStockListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.symbol.setText(favStocks.get(position).getSymbol());
        holder.shortName.setText(favStocks.get(position).getShortName());
        holder.typeDisp.setText(favStocks.get(position).getTypeDisp());
    }

    @Override
    public int getItemCount() {
        return favStocks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView symbol;
        TextView shortName;
        TextView typeDisp;
        FavoritedStockListener favoritedStockListener;

        public MyViewHolder(@NonNull View itemView, FavoritedStockListener favoritedStockListener) {
            super(itemView);
            symbol = itemView.findViewById(R.id.text_symbol);
            shortName = itemView.findViewById(R.id.text_shortName);
            typeDisp = itemView.findViewById(R.id.text_typeDisp);
            this.favoritedStockListener = favoritedStockListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            favoritedStockListener.favoritedStockClick(getAdapterPosition());
        }
    }

    public interface FavoritedStockListener {
        void favoritedStockClick(int position);
    }
}
