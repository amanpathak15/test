package com.mindwarriorhack.app.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;

import android.widget.ImageView;
import android.widget.TextView;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.Levels.LevelsListItem;

import java.util.List;

public class LevelAdapter extends RecyclerView.Adapter<LevelAdapter.ViewHolder> {

    private List<LevelsListItem> levelsList;
    private Context context;
    private AdapterCallBack adapterCallBack;

    public LevelAdapter(List<LevelsListItem> levelsList, Context context, AdapterCallBack callBack) {
        this.levelsList = levelsList;
        this.context = context;
        this.adapterCallBack = callBack;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View view = new View(context);
        //free,paid,subscribed
        switch (viewType) {
            case 1:
                view = layoutInflater.inflate(R.layout.free_list_items, viewGroup, false);
                break;

            case 2:
                view = layoutInflater.inflate(R.layout.paid_list_items, viewGroup, false);
                break;

            case 3:
                view = layoutInflater.inflate(R.layout.subscribed_list_items, viewGroup, false);
                break;
        }

        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {

        Glide.with(context).load(levelsList.get(position).getLevelLogo()).thumbnail(0.2f).into(holder.levelLogo);
        holder.levelTitle.setText(levelsList.get(position).getTitle());
        holder.level_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapterCallBack.OpenLevelPopup(levelsList.get(position).getDescription(),levelsList.get(position).getTitle());
            }
        });

        switch (getItemViewType(position)) {

            case 1:
                holder.types.setText("Free");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterCallBack.openVideoListActivity(levelsList.get(position).getLevelId(), levelsList.get(position).getTitle());
                    }
                });
                break;

            case 2:
                holder.types.setText("£" + levelsList.get(position).getLevelPrice());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterCallBack.OpenSubscribePopup(levelsList.get(position).getTitle(), levelsList.get(position));
                    }
                });
                break;

            case 3:
                holder.types.setText("Purchased");
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        adapterCallBack.openVideoListActivity(levelsList.get(position).getLevelId(), levelsList.get(position).getTitle());
                    }
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return levelsList.size();
    }

    @Override
    public int getItemViewType(int position) {
        //free,paid,subscribed

        if (levelsList.get(position).getIsPaidLevel() == 0) {
            return 1;
        } else if (levelsList.get(position).getIsPaidLevel() == 1 && levelsList.get(position).getSubscribed() == 0) {
            return 2;
        } else if (levelsList.get(position).getIsPaidLevel() == 1 && levelsList.get(position).getSubscribed() == 1) {
            return 3;
        }

        return position;
    }


    public interface AdapterCallBack {
        void OpenSubscribePopup(String levelName, LevelsListItem item);

        void openVideoListActivity(String levelId, String title);

        void OpenLevelPopup(String description,String title);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        TextView types, levelTitle;
        ImageView levelLogo,level_info;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = itemView.findViewById(R.id.free_card_view);
            this.types = itemView.findViewById(R.id.types);
            this.levelTitle = itemView.findViewById(R.id.levelTitle);
            this.levelLogo = itemView.findViewById(R.id.levelLogo);
            this.level_info=itemView.findViewById(R.id.level_info);
        }
    }


}
