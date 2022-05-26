package com.example.stockfolio;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TrendingStocksRecycleViewAdapter extends RecyclerView.Adapter<TrendingStocksRecycleViewAdapter.MyViewHolder> {

    List<Stock.StockPreview> trendingStocks;
    Context context;
    private OnTrendingStockListener mOnTrendingStockListener;

    public TrendingStocksRecycleViewAdapter(List<Stock.StockPreview> trendingStocks, Context context, OnTrendingStockListener onTrendingStockListener) {
        this.trendingStocks = trendingStocks;
        this.context = context;
        this.mOnTrendingStockListener = onTrendingStockListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.one_line_stock, parent, false);
        MyViewHolder holder = new MyViewHolder(view, mOnTrendingStockListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.symbol.setText(trendingStocks.get(position).getSymbol());
        holder.shortName.setText(trendingStocks.get(position).getShortName());
        holder.typeDisp.setText(trendingStocks.get(position).getTypeDisp());
    }

    @Override
    public int getItemCount() {
        return trendingStocks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView symbol;
        TextView shortName;
        TextView typeDisp;
        OnTrendingStockListener onTrendingStockListener;

        public MyViewHolder(@NonNull View itemView, OnTrendingStockListener onTrendingStockListener) {
            super(itemView);
            symbol = itemView.findViewById(R.id.text_symbol);
            shortName = itemView.findViewById(R.id.text_shortName);
            typeDisp = itemView.findViewById(R.id.text_typeDisp);
            this.onTrendingStockListener = onTrendingStockListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTrendingStockListener.onTrendingStockClick(getAdapterPosition());
        }
    }

    public interface OnTrendingStockListener {
        void onTrendingStockClick(int position);
    }
}
