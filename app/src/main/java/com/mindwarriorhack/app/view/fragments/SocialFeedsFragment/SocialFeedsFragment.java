package com.mindwarriorhack.app.view.fragments.SocialFeedsFragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonElement;
import com.ligl.android.widget.iosdialog.IOSDialog;
import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.helper.Constant;
import com.mindwarriorhack.app.manager.PreferenceManager;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.CommentItem;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.FeedsItem;
import com.mindwarriorhack.app.model.SocialFeeds.SocialFeedResponse.SocialFeedResponse;
import com.mindwarriorhack.app.presenter.socialFeedsPresenter;
import com.mindwarriorhack.app.view.FeedCommentScreen.feedCommentScreen;
import com.mindwarriorhack.app.view.adapters.SocialFeedsAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class SocialFeedsFragment extends Fragment implements View.OnClickListener, socialFeedsView, SwipeRefreshLayout.OnRefreshListener, SocialFeedsAdapter.feedInterface {

    private AppCompatActivity context;
    private socialFeedsPresenter presenter;
    private RecyclerView socialFeedRecyclerview;
    private SocialFeedsAdapter adapter;
    private List<FeedsItem> feedsItemList;
    private ShimmerFrameLayout mShimmerViewContainer;
    private SwipeRefreshLayout swipeRefresh;
    private View optionsMenu;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = (AppCompatActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.socialfeeds_fragment, container, false);
        mShimmerViewContainer = view.findViewById(R.id.shimmer_view_container);
        initViews(view);

        return view;
    }

    private void initViews(View v) {
        feedsItemList = new ArrayList<>();
        presenter = new socialFeedsPresenter(context, this);
        mShimmerViewContainer.startShimmerAnimation();
        PreferenceManager.setString(Constant.FEED_PAGINATION_URL, "https://mindwarriorhacks.app/uat/api/Api/getPost?page=1");
        presenter.getSocialFeeds();
        socialFeedRecyclerview = v.findViewById(R.id.postRecyclerview);
        socialFeedRecyclerview.setLayoutManager(new LinearLayoutManager(context));
        swipeRefresh = (SwipeRefreshLayout) v.findViewById(R.id.swipe_refresh);
        swipeRefresh.setOnRefreshListener(this);
        optionsMenu=v.findViewById(R.id.optionMenu);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onRefresh() {
        PreferenceManager.setString(Constant.FEED_PAGINATION_URL, "https://mindwarriorhacks.app/uat/api/Api/getPost?page=1");
        presenter.getSocialFeeds();
    }

    @Override
    public void onGetFeedsSuccess(Response<SocialFeedResponse> response) {

        swipeRefresh.setRefreshing(false);
        if (response.body().getResponseData().getNext() != null) {
            PreferenceManager.setString(Constant.FEED_PAGINATION_URL, response.body().getResponseData().getNext());
        } else {
            PreferenceManager.setString(Constant.FEED_PAGINATION_URL, "");
        }

        if (adapter != null) {

            if (PreferenceManager.getString(Constant.IS_FEED_PAGINATION_PROCESSING).equals("true")) {
                PreferenceManager.setString(Constant.IS_FEED_PAGINATION_PROCESSING, "false");
                if (response.body().getResponseCode() == 0) {
                    feedsItemList.addAll(response.body().getResponseData().getFeeds());
                }
                adapter.notifyDataSetChanged();
            } else {
                feedsItemList.clear();
                feedsItemList.add(new FeedsItem());
                if (response.body().getResponseCode() == 0) {
                    feedsItemList.addAll(response.body().getResponseData().getFeeds());
                }
                adapter.notifyDataSetChanged();
            }
        } else {
            mShimmerViewContainer.stopShimmerAnimation();
            mShimmerViewContainer.setVisibility(View.GONE);
            feedsItemList.clear();
            feedsItemList.add(new FeedsItem());
            if (response.body().getResponseCode() == 0) {
                feedsItemList.addAll(response.body().getResponseData().getFeeds());
            }
            adapter = new SocialFeedsAdapter(context, feedsItemList, this);
            socialFeedRecyclerview.setAdapter(adapter);
        }
    }

    @Override
    public void onGetFeedsFailure(String error) {
        swipeRefresh.setRefreshing(false);
    }

    @Override
    public void onFeedLikeSuccess(Response<JsonElement> response) {

    }

    @Override
    public void onFeedLikeFailure(String error) {

    }

    @Override
    public void onReportFeedSuccess(Response<JsonElement> response) {
        customDialog("Mind warrior team will investigate the post and delete it if found to be offensive.");
    }

    @Override
    public void onReportFeedFailure(String error) {

    }

    @Override
    public void callLikeApi(int postId, int flag, int feedPosition) {
        presenter.likesCallApi(flag, postId, feedPosition);
    }

    @Override
    public void callCommentApi(int postId, String comment) {

    }

    @Override
    public void openOptionMenu(final int postId) {

        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.feed_option_menu);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        TextView reportFeed = dialog.findViewById(R.id.reportFeed);
        reportFeed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.reportFeedCallApi(postId);
                dialog.dismiss();
            }
        });
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();

    }

    @Override
    public void callFeedPaginationApi() {

        if (!PreferenceManager.getString(Constant.FEED_PAGINATION_URL).isEmpty()) {
            PreferenceManager.setString(Constant.IS_FEED_PAGINATION_PROCESSING, "true");
            presenter.getSocialFeeds();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!PreferenceManager.getString(Constant.COMMENT_COUNT).isEmpty() && !PreferenceManager.getString(Constant.FEED_POSITION).isEmpty()) {
            List<CommentItem> commentItems = new ArrayList<>();
            CommentItem commentItem = new CommentItem();
            commentItem.setComment(PreferenceManager.getPojo("latestComment").getMessage());
            commentItem.setCommentId(PreferenceManager.getPojo("latestComment").getCommentId() + "");
            commentItem.setUserPic(PreferenceManager.getPojo("latestComment").getUserImage());
            commentItem.setUserName(PreferenceManager.getPojo("latestComment").getUserName());
            commentItems.add(commentItem);

            try {
                FeedsItem feedsItem = feedsItemList.get(Integer.parseInt(PreferenceManager.getString(Constant.FEED_POSITION)));
                feedsItem.setCommentCount(PreferenceManager.getString(Constant.COMMENT_COUNT));
                feedsItem.setLatestComment(commentItems);
                feedsItemList.set(Integer.parseInt(PreferenceManager.getString(Constant.FEED_POSITION)), feedsItem);
                adapter.notifyDataSetChanged();
                PreferenceManager.setString(Constant.COMMENT_COUNT, "");
                PreferenceManager.setString(Constant.FEED_POSITION, "");
            }
            catch (Exception e){

            }
        }

        if (PreferenceManager.getString(Constant.IS_POST_CALLED).equals("true")) {
            PreferenceManager.setString(Constant.FEED_PAGINATION_URL, "https://mindwarriorhacks.app/uat/api/Api/getPost?page=1");
            presenter.getSocialFeeds();
            PreferenceManager.setString(Constant.IS_POST_CALLED, "false");
        }
    }

    private void customDialog(String msg) {
        new IOSDialog.Builder(getContext())
                .setTitle("Reported successfully")
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).show();
    }
}