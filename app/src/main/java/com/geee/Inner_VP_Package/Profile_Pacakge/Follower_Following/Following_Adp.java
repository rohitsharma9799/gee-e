package com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.geee.Interface.AdapterClickListener;
import com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.DataModel.FollwerDataModel;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Following_Adp extends RecyclerView.Adapter<Following_Adp.ViewHolder> {

    Context context;
    List<FollwerDataModel> follwerLists = new ArrayList<>();
    AdapterClickListener clickListener;

    public Following_Adp(Context context, List<FollwerDataModel> followingLists, AdapterClickListener clickListener) {
        this.context = context;
        this.follwerLists = followingLists;
        this.clickListener = clickListener;
    }

    public void updateList(ArrayList<FollwerDataModel> list) {
        follwerLists = list;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.following_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final FollwerDataModel follwer = follwerLists.get(i);
        // Setting Up Image
      //  viewHolder.imageView.setImageURI(Constants.BASE_URL + follwer.getUser_image_follwer());


        Picasso.get().load(Constants.BASE_URL + follwer.getUser_image_follwer())
                .placeholder(R.drawable.profile_image_placeholder)
                .into(viewHolder.imageView);
        viewHolder.userName.setText("" + follwer.getUser_name_follwer());
        viewHolder.fullName.setText("" + follwer.getUser_full_name());

        if (follwer.getUser_id_follower().equals(SharedPrefrence.getUserIdFromJson(context))) {
            viewHolder.btnFollowUnfollow.setVisibility(View.GONE);
        } else {
            if (follwer.getUser_id_follower().equals(SharedPrefrence.getUserIdFromJson(context))) {
                viewHolder.btnFollowUnfollow.setVisibility(View.GONE);
            } else {
                if (follwer.followstatus.contains("Requested")) {
                    viewHolder.btnFollowUnfollow.setBackground(ContextCompat.getDrawable(context, R.drawable.border_line_transparent));
                    viewHolder.btnFollowUnfollow.setText("" + follwer.followstatus);
                    viewHolder.btnFollowUnfollow.setTextColor(ContextCompat.getColor(context, R.color.black));
                } else if (follwer.followstatus.equalsIgnoreCase("Following") || follwer.followstatus.equalsIgnoreCase("Friends")) {
                    viewHolder.btnFollowUnfollow.setBackground(ContextCompat.getDrawable(context, R.drawable.border_line_transparent));
                    viewHolder.btnFollowUnfollow.setText("Following");
                    viewHolder.btnFollowUnfollow.setTextColor(ContextCompat.getColor(context, R.color.black));
                } else {
                    viewHolder.btnFollowUnfollow.setBackground(ContextCompat.getDrawable(context, R.drawable.follow_bg));
                    viewHolder.btnFollowUnfollow.setText("FRIENDS" + follwer.followstatus);
                    viewHolder.btnFollowUnfollow.setTextColor(ContextCompat.getColor(context, R.color.white));
                }

            }
        }

        viewHolder.bind(i, follwer, clickListener);
    }

    @Override
    public int getItemCount() {
        return follwerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView userName, fullName;
        Button btnFollowUnfollow;
        RelativeLayout profileRlt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.tv_username);
            fullName = itemView.findViewById(R.id.tv_fullname);
            btnFollowUnfollow = itemView.findViewById(R.id.btn_id);
            profileRlt = itemView.findViewById(R.id.profile_rlt);
        }

        public void bind(final int item, final FollwerDataModel follwerDataModel, final AdapterClickListener listener) {

            profileRlt.setOnClickListener(v -> {
                listener.onItemClick(item, follwerDataModel, v);
            });

            btnFollowUnfollow.setOnClickListener(v -> {

                listener.onItemClick(item, follwerDataModel, v);
            });

        }
    }
}
