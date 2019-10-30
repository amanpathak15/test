package com.mindwarriorhack.app.view.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mindwarriorhack.app.R;
import com.mindwarriorhack.app.model.Notification.BroadcastListItem;

import org.ocpsoft.prettytime.PrettyTime;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private Context context;
    private List<BroadcastListItem> notificationListItems;

    public NotificationAdapter(List<BroadcastListItem> notificationListItems, Context context) {
        this.notificationListItems = notificationListItems;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.notification_items, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.notication_title.setText(notificationListItems.get(position).getTitle());
        String postedTime = notificationListItems.get(position).getDateTime();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        Date date = new Date();
        try {
            date = dateFormat.parse(postedTime);
           // dateFormat.setTimeZone(TimeZone.getDefault());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        SimpleDateFormat postFormater = new SimpleDateFormat("dd MMM, yyyy - hh:mm a");


       /* PrettyTime prettyTime = new PrettyTime();
        String ago = prettyTime.format(date);

        SimpleDateFormat dateFormater = new SimpleDateFormat("dd MMMM, yyyy");

        String newAgo = "";
        newAgo = dateFormater.format(date) + " - " + postFormater.format(date);*/

        holder.date_time.setText(postFormater.format(date));
       // holder.date_time.setText(date.toString());
        holder.notification_subject.setText(notificationListItems.get(position).getMsg());
    }


    @Override
    public int getItemCount() {
        return notificationListItems.size();
    }

    public void removeItem(int position) {
        notificationListItems.remove(position);
        notifyItemRemoved(position);

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView notication_title, date_time, notification_subject;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.notication_title = itemView.findViewById(R.id.notification_title);
            this.date_time = itemView.findViewById(R.id.date_time);
            this.notification_subject = itemView.findViewById(R.id.notification_subject);
        }
    }
}
