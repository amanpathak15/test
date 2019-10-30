package com.mindwarriorhack.app.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.SocialFeeds.Likes.LikesItem;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.FeedsItem;
import com.mindwarriorhack.app.view.newPostScreen.newPostScreen;

import java.util.List;

public class FeedLikeAdapter extends RecyclerView.Adapter<FeedLikeAdapter.ViewHolder>  {

    Context context;
    List<LikesItem> likeList;
    feedLikeInterface feedLikeInterface;


    public FeedLikeAdapter(Context context, List<LikesItem> likeList, feedLikeInterface feedLikeInterface) {
        this.context = context;
        this.likeList = likeList;
        this.feedLikeInterface = feedLikeInterface;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profilePic;
        TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            userName = (TextView) itemView.findViewById(R.id.name);
        }
    }



    @NonNull
    @Override
    public FeedLikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_like_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedLikeAdapter.ViewHolder holder, final int position) {

        if (!likeList.get(position).getUserImage().isEmpty()) {
            Glide.with(context).load(likeList.get(position).getUserImage()).apply(RequestOptions.circleCropTransform()).into(holder.profilePic);
        } else {
            Glide.with(context).load(R.drawable.default_profilepic).apply(RequestOptions.circleCropTransform()).into(holder.profilePic);
        }


        holder.userName.setText(likeList.get(position).getUserName());


        if (position >= (likeList.size() - 1)) {
            if(likeList.size() != 0) {
                feedLikeInterface.callFeedPaginationApi();
            }
        }


    }

    @Override
    public int getItemCount() {
        return likeList.size();
    }


    public interface feedLikeInterface{

        void callFeedPaginationApi();
    }


}
