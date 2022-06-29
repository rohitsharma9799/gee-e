package com.geee.Inner_VP_Package.Notification_Pacakge.You_Package;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.CodeClasses.Functions;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Notification_Pacakge.DataModel.NotificationModel;
import com.geee.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Notification_Adapters extends RecyclerView.Adapter<Notification_Adapters.ViewHolder> {

    Notification_Adapters.onclick click;
    List<NotificationModel> list;
    Context context;

    public Notification_Adapters(onclick click, List<NotificationModel> list, Context context) {
        this.click = click;
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_you_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final NotificationModel model = list.get(i);

        if (!model.getAttachment().equals(""))
            viewHolder.postImage.setImageURI(Constants.BASE_URL + model.getAttachment());

        if (!model.getUser_img().equals("")) {
           // viewHolder.userimage.setImageURI(Constants.BASE_URL + model.getUser_img());
            Picasso.get().load(Constants.BASE_URL + model.getUser_img())
                    .placeholder(R.drawable.profile_image_placeholder)
                    .into(viewHolder.userimage);
        }

        String text = "" + model.getNoti_msg() + ".";

        if (text.contains("" + model.getUser_name())) {
            text = text.replace(model.getUser_name(), "");
            text = "<b> " + model.getUser_name() + "  </b> " + text;
            viewHolder.tvUsername.setText(Html.fromHtml(text));
        }

        String dateTime = Functions.getTimeAgoOrg(model.getCreated());

        viewHolder.timeAge.setText(dateTime);

        if (dateTime.contains("Seconds") || dateTime.contains("Minutes") ||
                dateTime.contains("Hours") || dateTime.contains("Days")) {
            if (i == 0) {
                viewHolder.timeAgo.setVisibility(View.VISIBLE);
                viewHolder.timeAgo.setText("This Week");
            } else {
                if (dateTime.contains("Week")) {
                    viewHolder.timeAgo.setText("This Month");
                    viewHolder.timeAgo.setVisibility(View.VISIBLE);
                } else if (dateTime.contains("Month")) {
                    viewHolder.timeAgo.setText("Earlier");
                    viewHolder.timeAgo.setVisibility(View.VISIBLE);
                } else {
                    viewHolder.timeAgo.setVisibility(View.GONE);
                }
            }
        } else {
            if (i >= 1) {
                String value = Functions.getTimeAgoOrg(list.get(i - 1).getCreated());
                String current_value = Functions.getTimeAgoOrg(list.get(i).getCreated());
                if (!current_value.equals(value)) {
                    if (current_value.contains("Week")) {
                        viewHolder.timeAgo.setText("This Month");
                        viewHolder.timeAgo.setVisibility(View.VISIBLE);
                    } else if (current_value.contains("Month")) {
                        viewHolder.timeAgo.setText("Earlier");
                        viewHolder.timeAgo.setVisibility(View.VISIBLE);
                    } else {
                        viewHolder.timeAgo.setVisibility(View.GONE);
                    }

                } else {
                    viewHolder.timeAgo.setVisibility(View.GONE);
                }
            }
        }

        viewHolder.onbind(i, click);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public interface onclick {
        void itemclick(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView timeAgo, tvUsername, timeAge;

        SimpleDraweeView  postImage;
        ImageView userimage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userimage = itemView.findViewById(R.id.userimage);
            timeAgo = itemView.findViewById(R.id.time_ago);
            tvUsername = itemView.findViewById(R.id.tv_username);
            postImage = itemView.findViewById(R.id.postImage);
            timeAge = itemView.findViewById(R.id.time_age);
        }

        public void onbind(final int pos, final Notification_Adapters.onclick listner) {
            itemView.setOnClickListener(v -> listner.itemclick(pos));

        }
    }

}
