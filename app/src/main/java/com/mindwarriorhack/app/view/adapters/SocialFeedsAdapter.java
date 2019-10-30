package com.mindwarriorhack.app.view.adapters;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mindwarriorhack.app.PostDetailedScreen;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.helper.MySpannable;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.FeedsItem;
import com.mindwarriorhack.app.view.FeedCommentScreen.feedCommentScreen;
import com.mindwarriorhack.app.view.FeedLikeScreen.feedLikeScreen;
import com.mindwarriorhack.app.view.fragments.SocialFeedsFragment.SocialFeedsFragment;
import com.mindwarriorhack.app.view.newPostScreen.newPostScreen;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import io.github.ponnamkarthik.richlinkpreview.RichLinkViewTelegram;
import io.github.ponnamkarthik.richlinkpreview.ViewListener;

public class SocialFeedsAdapter extends RecyclerView.Adapter<SocialFeedsAdapter.ViewHolder> {

    feedInterface feedInterface;
    Bitmap bitmap;
    private Context context;
    private List<FeedsItem> feedList;

    public SocialFeedsAdapter(Context context, List<FeedsItem> feedList, final feedInterface feedInterface) {
        this.context = context;
        this.feedList = feedList;
        this.feedInterface = feedInterface;

    }

    @NonNull
    @Override
    public SocialFeedsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = new View(context);

        switch (viewType) {

            case -1:
                view = LayoutInflater.from(context).inflate(R.layout.post_list_item_progressbar, parent, false);
                break;

            case 0:
                view = LayoutInflater.from(context).inflate(R.layout.post_header_list_item, parent, false);
                break;

            case 1:
                view = LayoutInflater.from(context).inflate(R.layout.post_list_item_image_text, parent, false);
                break;

            case 2:
                view = LayoutInflater.from(context).inflate(R.layout.post_list_item_text, parent, false);
                break;

            case 3:
                view = LayoutInflater.from(context).inflate(R.layout.post_list_item_image_text_with_latest_comment, parent, false);
                break;

            case 4:
                view = LayoutInflater.from(context).inflate(R.layout.post_list_item_text_with_latest_comment, parent, false);
                break;
        }
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("Assert")
    @Override
    public void onBindViewHolder(@NonNull final SocialFeedsAdapter.ViewHolder holder, final int position) {

        if (getItemViewType(position) == 0) {

            if (!PreferenceManager.getString(Constant.PROFILE_PIC).isEmpty()) {
                Glide.with(context).load(PreferenceManager.getString(Constant.PROFILE_PIC)).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.default_profilepic).into(holder.profilePic);
            } else {
                Glide.with(context).load(R.drawable.default_profilepic).apply(RequestOptions.circleCropTransform()).into(holder.profilePic);
            }

            holder.postEditText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, newPostScreen.class));
                }
            });


        } else if (getItemViewType(position) == 1) {
            //Image Feed
            Glide.with(context).load(feedList.get(position).getUserImage()).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.default_profilepic).into(holder.profilePic);
            holder.userName.setText(feedList.get(position).getUserName());
            if (feedList.get(position).getMessage().length() > 0) {
                holder.postText.setVisibility(View.VISIBLE);
                holder.postText.setText(feedList.get(position).getMessage());
            }else {
                holder.postText.setVisibility(View.GONE);
            }

            String postedTime = feedList.get(position).getPostDate();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            Date date = new Date();
            try {
                date = dateFormat.parse(postedTime);
               // dateFormat.setTimeZone(TimeZone.getDefault());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            PrettyTime prettyTime = new PrettyTime();
            //prettyTime.setLocale(Locale.getDefault());
            String ago = prettyTime.format(date);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat postFormater = new SimpleDateFormat("hh:mm a");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormater = new SimpleDateFormat("dd MMM");

            String newAgo = "";
            if (ago.contains("moments ago")) {
                newAgo = ago;
            } else if (ago.contains("minutes")) {
                newAgo = ago.replace("minutes", "Min");
            } else if (ago.contains("hour")) {
                newAgo = ago.replace("hour", "hour");
            } else if (ago.contains("1 day ago")) {
                newAgo = ago.replace("1 day ago", "Yesterday" + " at " + postFormater.format(date));
            } else {
                newAgo = dateFormater.format(date) + ", " + postFormater.format(date);
            }

            holder.dateTime.setText(newAgo);
            Glide.with(context).load(feedList.get(position).getImage()).placeholder(R.drawable.image_placeholder).override(600,200).into(holder.postImage);
            /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 250);
            holder.postImage.setLayoutParams(layoutParams);*/
            holder.likesCount.setText(feedList.get(position).getLikeCount() + " Likes");
            holder.commentCounts.setText(feedList.get(position).getCommentCount() + " Comments");
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (feedList.get(position).isLike()) {
                        FeedsItem feedsItem = feedList.get(position);
                        feedsItem.setLike(false);
                        feedsItem.setLikeCount(String.valueOf((Integer.parseInt(feedList.get(position).getLikeCount()) - 1)));
                        feedList.set(position, feedsItem);
                        Glide.with(context).load(R.drawable.like).into(holder.likeView);
                        feedInterface.callLikeApi(feedList.get(position).getPostId(), 0, holder.getAdapterPosition());
                    } else {
                        FeedsItem feedsItem = feedList.get(position);
                        feedsItem.setLike(true);
                        feedsItem.setLikeCount(String.valueOf(Integer.parseInt(feedList.get(position).getLikeCount()) + 1));
                        feedList.set(position, feedsItem);
                        Glide.with(context).load(R.drawable.liked).into(holder.likeView);
                        feedInterface.callLikeApi(feedList.get(position).getPostId(), 1, holder.getAdapterPosition());
                    }
                    notifyDataSetChanged();
                }
            });

            holder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailedScreen.class);
                    intent.putExtra("image", feedList.get(position).getImage());
                    context.startActivity(intent);
                }
            });


            holder.likesCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedLikeScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    context.startActivity(intent);
                }
            });

            if (feedList.get(position).isLike()) {
                Glide.with(context).load(R.drawable.liked).into(holder.likeView);
            } else {
                Glide.with(context).load(R.drawable.like).into(holder.likeView);
            }

            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("feedPosition", position);
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);
                }
            });

            holder.commentCounts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);
                }
            });

            if(feedList.get(position).getUserId().equals(PreferenceManager.getString(Constant.USER_ID))){
                holder.optionMenu.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.optionMenu.setVisibility(View.VISIBLE);
            }

            holder.optionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedInterface.openOptionMenu(feedList.get(position).getPostId());

                }
            });

        } else if (getItemViewType(position) == 2) {
            //Text Feed
            Glide.with(context).load(feedList.get(position).getUserImage()).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.default_profilepic).into(holder.profilePic);
            holder.userName.setText(feedList.get(position).getUserName());
            String feedText = feedList.get(holder.getAdapterPosition()).getMessage();
            String keyword = "https://";
            int keyposition = feedText.indexOf(keyword);
            if (keyposition==0){
                holder.postText.setVisibility(View.GONE);
                holder.preview.setVisibility(View.VISIBLE);
                holder.preview.setLink(feedText, new ViewListener() {
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
                holder.preview.setVisibility(View.GONE);
                holder.postText.setVisibility(View.VISIBLE);
                if (holder.postText.length() > 0) {
                    holder.postText.setVisibility(View.VISIBLE);
                }else if(holder.postText.length()> 120) {
                    holder.postText.setVisibility(View.VISIBLE);
                }
                else if (holder.postText.length() < 120) {
                    holder.postText.setVisibility(View.VISIBLE);
                } else if (holder.postText.length() == 0) {
                    holder.postText.setVisibility(View.GONE);
                }
                holder.postText.setText(feedList.get(position).getMessage());
            }




            String postedTime = feedList.get(position).getPostDate();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            Date date = new Date();
            try {
                date = dateFormat.parse(postedTime);
               // dateFormat.setTimeZone(TimeZone.getDefault());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            PrettyTime prettyTime = new PrettyTime();
            //prettyTime.setLocale(Locale.getDefault());
            String ago = prettyTime.format(date);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat postFormater = new SimpleDateFormat("hh:mm a");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormater = new SimpleDateFormat("dd MMM");

            String newAgo = "";
            if (ago.contains("moments ago")) {
                newAgo = ago;
            } else if (ago.contains("minutes")) {
                newAgo = ago.replace("minutes", "Min");
            } else if (ago.contains("hour")) {
                newAgo = ago.replace("hour", "hour");
            } else if (ago.contains("1 day ago")) {
                newAgo = ago.replace("1 day ago", "Yesterday" + " at " + postFormater.format(date));
            } else {
                newAgo = dateFormater.format(date) + ", " + postFormater.format(date);
            }

            holder.dateTime.setText(newAgo);
            if(feedList.get(position).getUserId().equals(PreferenceManager.getString(Constant.USER_ID))){
                holder.optionMenu.setVisibility(View.INVISIBLE);
            }
            else
            {
                holder.optionMenu.setVisibility(View.VISIBLE);
            }

            holder.likesCount.setText(String.format("%s Likes", feedList.get(position).getLikeCount()));
            holder.commentCounts.setText(String.format("%s Comments", feedList.get(position).getCommentCount()));
            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (feedList.get(position).isLike()) {
                        FeedsItem feedsItem = feedList.get(position);
                        feedsItem.setLike(false);
                        feedsItem.setLikeCount(String.valueOf((Integer.parseInt(feedList.get(position).getLikeCount()) - 1)));
                        feedList.set(position, feedsItem);
                        Glide.with(context).load(R.drawable.like).into(holder.likeView);
                        feedInterface.callLikeApi(feedList.get(position).getPostId(), 0, holder.getAdapterPosition());
                    } else {
                        FeedsItem feedsItem = feedList.get(position);
                        feedsItem.setLike(true);
                        feedsItem.setLikeCount(String.valueOf((Integer.parseInt(feedList.get(position).getLikeCount()) + 1)));
                        feedList.set(position, feedsItem);
                        Glide.with(context).load(R.drawable.liked).into(holder.likeView);
                        feedInterface.callLikeApi(feedList.get(position).getPostId(), 1, holder.getAdapterPosition());
                    }
                    notifyDataSetChanged();
                }
            });


            holder.likesCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedLikeScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    context.startActivity(intent);
                }
            });

            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("feedPosition", position);
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);

                }
            });

            holder.commentCounts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("feedPosition", position);
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);
                }
            });

            holder.optionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedInterface.openOptionMenu(feedList.get(position).getPostId());
                }
            });

            if (feedList.get(position).isLike()) {
                Glide.with(context).load(R.drawable.liked).into(holder.likeView);
            } else {
                Glide.with(context).load(R.drawable.like).into(holder.likeView);
            }

        } else if (getItemViewType(position) == 3) {
            //Image Feed with comments
            Glide.with(context).load(feedList.get(position).getUserImage()).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.default_profilepic).into(holder.profilePic);
            holder.userName.setText(feedList.get(position).getUserName());
            if (!(holder.postText.length() > 0)) {
                holder.postText.setVisibility(View.VISIBLE);
            }else {
                holder.postText.setVisibility(View.GONE);
            }
            holder.postText.setText(feedList.get(position).getMessage());
            String postedTime = feedList.get(position).getPostDate();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            @SuppressLint("SimpleDateFormat")
            Date date = new Date();
            try {
                date = dateFormat.parse(postedTime);
                //dateFormat.setTimeZone(TimeZone.getDefault());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            PrettyTime prettyTime = new PrettyTime();
            //prettyTime.setLocale(Locale.getDefault());
            String ago = prettyTime.format(date);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat postFormater = new SimpleDateFormat("hh:mm a");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormater = new SimpleDateFormat("dd MMM");

            String newAgo = "";
            if (ago.contains("moments ago")) {
                newAgo = ago;
            } else if (ago.contains("minutes")) {
                newAgo = ago.replace("minutes", "Min");
            } else if (ago.contains("hour")) {
                newAgo = ago.replace("hour", "hour");
            } else if (ago.contains("1 day ago")) {
                newAgo = ago.replace("1 day ago", "Yesterday" + " at " + postFormater.format(date));
            } else {
                newAgo = dateFormater.format(date) + ", " + postFormater.format(date);
            }

            holder.dateTime.setText(newAgo);
            Glide.with(context).load(feedList.get(position).getImage()).placeholder(R.drawable.image_placeholder).into(holder.postImage);
            /*LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 250);
            holder.postImage.setLayoutParams(layoutParams);*/
            holder.likesCount.setText(feedList.get(position).getLikeCount() + " Likes");
            holder.commentCounts.setText(feedList.get(position).getCommentCount() + " Comments");
            Glide.with(context).load(feedList.get(position).getLatestComment().get(0).getUserPic()).placeholder(R.drawable.default_profilepic).apply(RequestOptions.circleCropTransform()).into(holder.latestCommentProfilePic);
            holder.latestCommentUserName.setText(feedList.get(position).getLatestComment().get(0).getUserName());
            holder.latestComment.setText(feedList.get(position).getLatestComment().get(0).getComment());
            if (!PreferenceManager.getString(Constant.PROFILE_PIC).isEmpty()) {
                Glide.with(context).load(PreferenceManager.getString(Constant.PROFILE_PIC)).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.default_profilepic).into(holder.addCommentProfilePic);
            } else {
                Glide.with(context).load(R.drawable.default_profilepic).apply(RequestOptions.circleCropTransform()).into(holder.addCommentProfilePic);
            }

            holder.postImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, PostDetailedScreen.class);
                    intent.putExtra("image", feedList.get(position).getImage());
                    context.startActivity(intent);
                }
            });

            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (feedList.get(position).isLike()) {
                        FeedsItem feedsItem = feedList.get(position);
                        feedsItem.setLike(false);
                        feedsItem.setLikeCount(String.valueOf((Integer.parseInt(feedList.get(position).getLikeCount()) - 1)));
                        feedList.set(position, feedsItem);
                        Glide.with(context).load(R.drawable.like).into(holder.likeView);
                        feedInterface.callLikeApi(feedList.get(position).getPostId(), 0, holder.getAdapterPosition());
                    } else {
                        FeedsItem feedsItem = feedList.get(position);
                        feedsItem.setLike(true);
                        feedsItem.setLikeCount(String.valueOf((Integer.parseInt(feedList.get(position).getLikeCount()) + 1)));
                        feedList.set(position, feedsItem);
                        Glide.with(context).load(R.drawable.liked).into(holder.likeView);
                        feedInterface.callLikeApi(feedList.get(position).getPostId(), 1, holder.getAdapterPosition());
                    }
                    notifyDataSetChanged();
                }
            });


            holder.likesCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedLikeScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    context.startActivity(intent);
                }
            });

            holder.commentCounts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("feedPosition", position);
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);
                }
            });

            holder.postLatestComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);
                }
            });


            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("feedPosition", position);
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);
                }
            });


            holder.optionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedInterface.openOptionMenu(feedList.get(position).getPostId());
                }
            });

            if (feedList.get(position).isLike()) {
                Glide.with(context).load(R.drawable.liked).into(holder.likeView);
            } else {
                Glide.with(context).load(R.drawable.like).into(holder.likeView);
            }

            holder.addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("feedPosition", position);
                    context.startActivity(intent);

                }
            });

        } else if (getItemViewType(position) == 4) {
            //Text Feed with comments
            Glide.with(context).load(feedList.get(position).getUserImage()).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.default_profilepic).into(holder.profilePic);
            holder.userName.setText(feedList.get(position).getUserName());
            if (holder.postText.length() > 0 && holder.postText.length() > 120) {
                holder.postText.setVisibility(View.VISIBLE);
            } else if (holder.postText.length() < 120) {
                holder.postText.setVisibility(View.VISIBLE);
            } else if (holder.postText.length() == 0) {
                holder.postText.setVisibility(View.GONE);
            }
            holder.postText.setText(feedList.get(position).getMessage());
            String postedTime = feedList.get(position).getPostDate();

            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
            @SuppressLint("SimpleDateFormat")
            Date date = new Date();
            try {
                date = dateFormat.parse(postedTime);
                //dateFormat.setTimeZone(TimeZone.getDefault());
            } catch (ParseException e) {
                e.printStackTrace();
            }

            PrettyTime prettyTime = new PrettyTime();
            //prettyTime.setLocale(Locale.getDefault());
            String ago = prettyTime.format(date);
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat postFormater = new SimpleDateFormat("hh:mm a");
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormater = new SimpleDateFormat("dd MMM");

            String newAgo = "";
            if (ago.contains("moments ago")) {
                newAgo = ago;
            } else if (ago.contains("minutes")) {
                newAgo = ago.replace("minutes", "Min");
            } else if (ago.contains("hour")) {
                newAgo = ago.replace("hour", "hour");
            } else if (ago.contains("1 day ago")) {
                newAgo = ago.replace("1 day ago", "Yesterday" + " at " + postFormater.format(date));
            } else {
                newAgo = dateFormater.format(date) + ", " + postFormater.format(date);
            }

            holder.dateTime.setText(newAgo);
            holder.likesCount.setText(feedList.get(position).getLikeCount() + " Likes");
            holder.commentCounts.setText(feedList.get(position).getCommentCount() + " Comments");
            Glide.with(context).load(feedList.get(position).getLatestComment().get(0).getUserPic()).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.default_profilepic).into(holder.latestCommentProfilePic);
            holder.latestCommentUserName.setText(feedList.get(position).getLatestComment().get(0).getUserName());
            holder.latestComment.setText(feedList.get(position).getLatestComment().get(0).getComment());
            if (!PreferenceManager.getString(Constant.PROFILE_PIC).isEmpty()) {
                Glide.with(context).load(PreferenceManager.getString(Constant.PROFILE_PIC)).apply(RequestOptions.circleCropTransform()).placeholder(R.drawable.default_profilepic).into(holder.addCommentProfilePic);
            } else {
                Glide.with(context).load(R.drawable.default_profilepic).apply(RequestOptions.circleCropTransform()).into(holder.addCommentProfilePic);
            }

            holder.like.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (feedList.get(position).isLike()) {
                        FeedsItem feedsItem = feedList.get(position);
                        feedsItem.setLike(false);
                        feedsItem.setLikeCount(String.valueOf((Integer.parseInt(feedList.get(position).getLikeCount()) - 1)));
                        feedList.set(position, feedsItem);
                        Glide.with(context).load(R.drawable.like).into(holder.likeView);
                        feedInterface.callLikeApi(feedList.get(position).getPostId(), 0, holder.getAdapterPosition());
                    } else {
                        FeedsItem feedsItem = feedList.get(position);
                        feedsItem.setLike(true);
                        feedsItem.setLikeCount(String.valueOf((Integer.parseInt(feedList.get(position).getLikeCount()) + 1)));
                        feedList.set(position, feedsItem);
                        Glide.with(context).load(R.drawable.liked).into(holder.likeView);
                        feedInterface.callLikeApi(feedList.get(position).getPostId(), 1, holder.getAdapterPosition());
                    }
                    notifyDataSetChanged();
                }
            });

            holder.postLatestComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);
                }
            });


            holder.likesCount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedLikeScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    context.startActivity(intent);
                }
            });

            holder.commentCounts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("feedPosition", position);
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);
                }
            });

            holder.addComment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("feedPosition", position);
                    context.startActivity(intent);

                }
            });

            holder.comment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, feedCommentScreen.class);
                    intent.putExtra("postId", feedList.get(position).getPostId());
                    intent.putExtra("feedPosition", position);
                    intent.putExtra("userName", feedList.get(position).getUserName());
                    context.startActivity(intent);
                }
            });


            if (feedList.get(position).isLike()) {
                Glide.with(context).load(R.drawable.liked).into(holder.likeView);
            } else {
                Glide.with(context).load(R.drawable.like).into(holder.likeView);
            }

            holder.optionMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    feedInterface.openOptionMenu(feedList.get(position).getPostId());

                }
            });

        }


        if (position >= (feedList.size() - 1)) {
            if (feedList.size() != 0) {
                feedInterface.callFeedPaginationApi();
            }
        }

    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    @Override
    public int getItemViewType(int position) {

        if (position == 0) {

            return 0;

        } else if (!feedList.get(position).getImage().isEmpty() && feedList.get(position).getLatestComment().size() == 0) {
            //Image Feed
            return 1;
        } else if (feedList.get(position).getImage().isEmpty() && feedList.get(position).getLatestComment().size() == 0) {
            //Text Feed
            return 2;
        } else if (!feedList.get(position).getImage().isEmpty() && feedList.get(position).getLatestComment().size() > 0) {
            //Image Feed with comments
            return 3;
        } else if (feedList.get(position).getImage().isEmpty() && feedList.get(position).getLatestComment().size() > 0) {
            //Text Feed with comments
            return 4;
        } else {

            return -2;
        }
    }

    public interface feedInterface {

        void callLikeApi(int postId, int flag, int feedPosition);

        void callCommentApi(int postId, String comment);

        void openOptionMenu(int postId);

        void callFeedPaginationApi();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView profilePic, postImage;
        TextView userName, dateTime, postText, likesCount, commentCounts, addComment, postEditText, latestCommentUserName, latestComment, likeViewText,seeMoreText;
        ImageView latestCommentProfilePic, addCommentProfilePic, optionMenu, likeView;
        LinearLayout like, comment;
        RelativeLayout postLatestComment;
        RichLinkViewTelegram preview;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profilePic = (ImageView) itemView.findViewById(R.id.profilePic);
            userName = (TextView) itemView.findViewById(R.id.name);
            dateTime = itemView.findViewById(R.id.dateTime);
            postImage = itemView.findViewById(R.id.imageView);
            postText = itemView.findViewById(R.id.text);
            likesCount = itemView.findViewById(R.id.likeCount);
            commentCounts = itemView.findViewById(R.id.commentCount);
            postEditText = itemView.findViewById(R.id.postEditText);
            latestCommentUserName = itemView.findViewById(R.id.postLatestCommentUserName);
            latestComment = itemView.findViewById(R.id.latestComment);
            latestCommentProfilePic = itemView.findViewById(R.id.postLatestCommentProfilePic);
            addCommentProfilePic = itemView.findViewById(R.id.addCommentProfilePic);
            like = itemView.findViewById(R.id.like);
            comment = itemView.findViewById(R.id.comment);
            optionMenu = itemView.findViewById(R.id.optionMenu);
            likeView = itemView.findViewById(R.id.likeIcon);
            postLatestComment = itemView.findViewById(R.id.postLatestComment);
            preview = itemView.findViewById(R.id.richLinkView);
            addComment = itemView.findViewById(R.id.addComment);
        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {
                String text;
                int lineEndIndex;
                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    lineEndIndex = tv.getLayout().getLineEnd(0);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                } else {
                    lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                }
                tv.setText(text);
                tv.setMovementMethod(LinkMovementMethod.getInstance());
                tv.setText(
                        addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                viewMore), TextView.BufferType.SPANNABLE);
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {
            ssb.setSpan(new MySpannable(false) {

                @Override
                public void onClick(View widget) {
                    tv.setLayoutParams(tv.getLayoutParams());
                    tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                    tv.invalidate();
                    if (viewMore) {
                        makeTextViewResizable(tv, -1, "See More", false);
                    } else {
                        makeTextViewResizable(tv, 3, "See Less", true);
                    }

                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }

}

