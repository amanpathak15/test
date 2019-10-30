package com.mindwarriorhack.app.view.adapters;

import android.content.Context;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.Packages.PackageListItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PackagesAdapter extends RecyclerView.Adapter<PackagesAdapter.ViewHolder> {

    private List<PackageListItem> packageList;
    private PackageInterface packageInterface;
    private SparseBooleanArray selectionArray = new SparseBooleanArray();
    private final static int FADE_DURATION = 400;

    public PackagesAdapter(List<PackageListItem> list, Context context, PackageInterface packageInterface) {
        this.packageList = list;
        this.packageInterface = packageInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = layoutInflater.inflate(R.layout.packages_list, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.bind(holder.getAdapterPosition());
        holder.level_title.setText(packageList.get(position).getTitle());
        holder.level_price.setText("£ " + packageList.get(position).getLevelPrice() + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectionArray.get(holder.getAdapterPosition(), false)) {
                    holder.tick.setVisibility(View.VISIBLE);
                    selectionArray.put(holder.getAdapterPosition(), true);
                    packageInterface.addToList(packageList.get(holder.getAdapterPosition()));
                } else {
                    holder.tick.setVisibility(View.INVISIBLE);
                    selectionArray.put(holder.getAdapterPosition(), false);
                    packageInterface.removeFromList(packageList.get(holder.getAdapterPosition()).getId());
                }
            }
        });
    }


    @Override
    public int getItemCount() {
        return packageList.size();
    }

    public interface PackageInterface {

        void addToList(PackageListItem item);

        void removeFromList(String levelId);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView level_title, level_price;
        ImageView tick;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            level_title = itemView.findViewById(R.id.level_ids);
            level_price = itemView.findViewById(R.id.level_price);
            tick = itemView.findViewById(R.id.tick);

        }

        void bind(int position) {
            if (!selectionArray.get(position, false)) {
                tick.setVisibility(View.INVISIBLE);
            } else {
                tick.setVisibility(View.VISIBLE);
            }
        }
    }




}

