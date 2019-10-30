package com.mindwarriorhack.app.view.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterInside;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.view.VimeoPlayer.VimeoPlayerActivity;
import com.mindwarriorhack.app.model.VideoList.VideoListItem;
import com.mindwarriorhack.app.view.rateThisVideo.rateThisVideoActivity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class VideoListAdapter extends RecyclerView.Adapter<VideoListAdapter.ViewHolder> {

    private List<VideoListItem> videoList;
    private Context context;
    private VideoAdapterCallback adapterCallback;
    private int isWatched = 0;

    public VideoListAdapter(List<VideoListItem> videoList, Context context, VideoAdapterCallback callback) {
        this.videoList = videoList;
        this.context = context;
        this.adapterCallback = callback;

    }

    @NonNull
    @Override
    public VideoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.video_list, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final VideoListAdapter.ViewHolder holder, final int position) {
        holder.videoTitle.setText(videoList.get(position).getTitle());
        holder.videoDuration.setText(videoList.get(position).getDuration());
        if (videoList.get(position).getIsWatched().equals("0")) {
            holder.videoWatched.setVisibility(View.GONE);
        } else {
            holder.videoWatched.setVisibility(View.VISIBLE);
        }

        if(videoList.get(position).getIsAvailableToView()){
            holder.disabledLayout.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.disabledLayout.setVisibility(View.VISIBLE);
        }


        if(videoList.get(position).isRated()){
            holder.videoRate.setVisibility(View.INVISIBLE);
            holder.ratingView.setVisibility(View.VISIBLE);

            switch (videoList.get(position).getRating()){

                case 1:
                    holder.ratingView.setText(context.getText(R.string.poor));
                    break;

                case 2:
                    holder.ratingView.setText(context.getText(R.string.bad));
                    break;

                case 3:
                    holder.ratingView.setText(context.getText(R.string.ok));
                    break;

                case 4:
                    holder.ratingView.setText(context.getText(R.string.good));
                    break;

                case 5:
                    holder.ratingView.setText(context.getText(R.string.awesome));
                    break;

            }
        }
        else
        {
            holder.videoRate.setVisibility(View.VISIBLE);
        }




        RequestOptions requestOptions = new RequestOptions();
        requestOptions = requestOptions.transforms(new CenterInside(), new RoundedCorners(30));

        Glide.with(context).load(videoList.get(position).getThumbNail()).apply(requestOptions).into(holder.videoThumbnail);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(videoList.get(position).getIsAvailableToView()) {
                        Intent intent = new Intent(context, VimeoPlayerActivity.class);
                        intent.putExtra("link", videoList.get(holder.getAdapterPosition()).getVideoUrl());
                        intent.putExtra("title", videoList.get(holder.getAdapterPosition()).getTitle());
                        intent.putExtra("imageThumbnail", videoList.get(position).getThumbNail());
                        intent.putExtra("videoId", videoList.get(position).getVideoId());
                        intent.putExtra("isUserRated", videoList.get(position).isRated());
                        context.startActivity(intent);
                    }
            }
        });

        holder.videoThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if(videoList.get(position).getIsAvailableToView()) {
                        Intent intent = new Intent(context, VimeoPlayerActivity.class);
                        intent.putExtra("link", videoList.get(holder.getAdapterPosition()).getVideoUrl());
                        intent.putExtra("title", videoList.get(holder.getAdapterPosition()).getTitle());
                        intent.putExtra("imageThumbnail", videoList.get(position).getThumbNail());
                        intent.putExtra("videoId", videoList.get(position).getVideoId());
                        intent.putExtra("isUserRated", videoList.get(position).isRated());
                        context.startActivity(intent);
                    }
            }
        });


        holder.ratingView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(videoList.get(position).getIsAvailableToView()) {
                    Intent intent = new Intent(context, rateThisVideoActivity.class);
                    intent.putExtra("imageThumbnail", videoList.get(position).getThumbNail());
                    intent.putExtra("videoName", videoList.get(position).getTitle());
                    intent.putExtra("videoId", videoList.get(position).getVideoId());
                    context.startActivity(intent);
                }
            }
        });


        holder.videoRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(videoList.get(position).getIsAvailableToView()) {
                    Intent intent = new Intent(context, rateThisVideoActivity.class);
                    intent.putExtra("imageThumbnail", videoList.get(position).getThumbNail());
                    intent.putExtra("videoName", videoList.get(position).getTitle());
                    intent.putExtra("videoId", videoList.get(position).getVideoId());
                    context.startActivity(intent);
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }


    public interface VideoAdapterCallback {

        void onVideoLiked(String videoId, int like, int videoPosition);

        void onVideoWatched(String videoId, int position, int isWatched);

        void getVideoDetails(String title, String url);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView videoThumbnail;
        TextView videoTitle, videoDuration, videoWatched, ratingView;
        ImageView videoLiked, videoRate;
        RelativeLayout disabledLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.videoThumbnail = itemView.findViewById(R.id.videoThumbnail);
            this.videoTitle = itemView.findViewById(R.id.videoTitle);
            this.videoDuration = itemView.findViewById(R.id.videoDuration);
            this.videoWatched = itemView.findViewById(R.id.videoWatched);
            this.videoLiked = itemView.findViewById(R.id.videoLiked);
            this.videoRate = itemView.findViewById(R.id.videoRate);
            this.ratingView = itemView.findViewById(R.id.rating);
            this.disabledLayout = itemView.findViewById(R.id.disabledLayout);

        }
    }
}
