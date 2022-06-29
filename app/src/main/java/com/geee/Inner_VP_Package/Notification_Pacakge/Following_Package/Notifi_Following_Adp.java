package com.geee.Inner_VP_Package.Notification_Pacakge.Following_Package;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

public class Notifi_Following_Adp extends RecyclerView.Adapter<Notifi_Following_Adp.ViewHolder> {

    Context ctx;
    Notifi_Following_Adp.onclick click;
    List<NotificationModel> list;

    public Notifi_Following_Adp(Context ctx, Notifi_Following_Adp.onclick click, List<NotificationModel> list) {
        this.ctx = ctx;
        this.click = click;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notification_following_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final NotificationModel notiGetSet = list.get(i);


        if (!notiGetSet.getAttachment().equals("")) {
            viewHolder.postImage.setImageURI(Constants.BASE_URL + notiGetSet.getAttachment());
        }

        if (!notiGetSet.getUser_img().equals("")) {
            Picasso.get().load(Constants.BASE_URL + notiGetSet.getUser_img())
                    .placeholder(R.drawable.profile_image_placeholder)
                    .into(viewHolder.userImg);
          //  viewHolder.userImg.setImageURI(Constants.BASE_URL + notiGetSet.getUser_img());
        }

        String text = "" + notiGetSet.getNoti_msg() + ".";
        if (text.contains("" + notiGetSet.getUser_name())) {
            text = text.replace(notiGetSet.getUser_name(), "");
            viewHolder.tv.setText(text);
        }

        viewHolder.timeAge.setText(Functions.getTimeAgoOrg(notiGetSet.getCreated()));
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
        ImageView  userImg;
        SimpleDraweeView postImage;
        TextView tv, timeAge, timeAgo;
        LinearLayout mainRL;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            postImage = itemView.findViewById(R.id.postImage);
            tv = itemView.findViewById(R.id.tv_username);
            userImg = itemView.findViewById(R.id.profileImage);
            timeAge = itemView.findViewById(R.id.time_age);
            mainRL = itemView.findViewById(R.id.mainrlsd);
            timeAgo = itemView.findViewById(R.id.time_ago);

        }

        public void onbind(final int pos, final Notifi_Following_Adp.onclick onclick) {

            itemView.setOnClickListener(v -> onclick.itemclick(pos));

        }
    }
}
