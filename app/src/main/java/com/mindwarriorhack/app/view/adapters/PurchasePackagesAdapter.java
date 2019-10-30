package com.mindwarriorhack.app.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.Packages.PackageListItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PurchasePackagesAdapter extends RecyclerView.Adapter<PurchasePackagesAdapter.ViewHolder> {

    List<PackageListItem> requestList;
    Context context;

    public PurchasePackagesAdapter(List<PackageListItem> list, Context context) {
        this.requestList = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_packages_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.level_title.setText(requestList.get(position).getTitle());
        holder.level_price.setText("£ "+requestList.get(position).getLevelPrice() + "");
    }


    @Override
    public int getItemCount() {
        return requestList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView level_title, level_price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            level_title = itemView.findViewById(R.id.level_ids);
            level_price = itemView.findViewById(R.id.level_price);
        }
    }

    public void removeItem(int position) {
        requestList.remove(position);
        notifyItemRemoved(position);

    }
}

