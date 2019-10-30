package com.mindwarriorhack.app.view.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.SocialFeeds.Comment.CommentsItem;

import java.util.List;

import io.github.ponnamkarthik.richlinkpreview.RichLinkView;
import io.github.ponnamkarthik.richlinkpreview.RichLinkViewTelegram;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class FeedCommentAdapter extends RecyclerView.Adapter<FeedCommentAdapter.ViewHolder>  {

    Context context;
    List<CommentsItem> commentList;
    feedCommentInterface feedCommentInterface;


    public FeedCommentAdapter(Context context, List<CommentsItem> commentList, feedCommentInterface feedCommentInterface) {
        this.context = context;
        this.commentList = commentList;
        this.feedCommentInterface = feedCommentInterface;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        ImageView profilePic;
        TextView userName, comment;
        RichLinkViewTelegram richLinkView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            userName = (TextView) itemView.findViewById(R.id.name);
            comment = itemView.findViewById(R.id.comment);
            richLinkView = itemView.findViewById(R.id.richLinkView);
        }
    }



    @NonNull
    @Override
    public FeedCommentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = null;
        switch (viewType){

            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_comment_list_item_link,parent,false);
                break;

            case 2:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.feed_comment_list_item,parent,false);
                break;

        }

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedCommentAdapter.ViewHolder holder, final int position) {

        if(getItemViewType(position) == 1){

            if (commentList.get(position).getUserImage() != null && !commentList.get(position).getUserImage().isEmpty()) {
                Glide.with(context).load(commentList.get(position).getUserImage()).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.default_profilepic).into(holder.profilePic);
            } else {
                Glide.with(context).load(R.drawable.default_profilepic).apply(RequestOptions.circleCropTransform()).into(holder.profilePic);
            }

            holder.userName.setText(commentList.get(position).getUserName());
            holder.richLinkView.setLink(commentList.get(position).getMessage(), new ViewListener() {
                    @Override
                    public void onSuccess(boolean status) {

                    }

                    @Override
                    public void onError(Exception e) {

                    }
                });
        }
        else
        {

            if (commentList.get(position).getUserImage() != null && !commentList.get(position).getUserImage().isEmpty()) {
                Glide.with(context).load(commentList.get(position).getUserImage()).apply(RequestOptions.circleCropTransform()).into(holder.profilePic);
            } else {
                Glide.with(context).load(R.drawable.default_profilepic).apply(RequestOptions.circleCropTransform()).into(holder.profilePic);
            }

            holder.userName.setText(commentList.get(position).getUserName());
            holder.comment.setText(commentList.get(position).getMessage());
        }




        if (position >= (commentList.size() - 1)) {
            if(commentList.size() != 0) {
                feedCommentInterface.callFeedPaginationApi();
            }
        }


    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }


    @Override
    public int getItemViewType(int position) {

        String feedText = commentList.get(position).getMessage();
        String keyword = "https://";
        int keyposition = feedText.indexOf(keyword);
        if (keyposition==0){
            return 1;
        }
        else
        {
            return 2;
        }

    }

    public interface feedCommentInterface{

        void callFeedPaginationApi();
    }


}
