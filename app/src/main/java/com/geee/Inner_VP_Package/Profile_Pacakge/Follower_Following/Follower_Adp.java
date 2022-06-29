package com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following;

import android.content.Context;
import android.net.Uri;
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
import com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.DataModel.FollwerDataModel;
import com.geee.Interface.AdapterClickListener;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Follower_Adp extends RecyclerView.Adapter<Follower_Adp.ViewHolder> {

    Context context;
    List<FollwerDataModel> follwerLists = new ArrayList<>();
    AdapterClickListener clickListener;

    public Follower_Adp(Context ctx, List<FollwerDataModel> follwerLists, AdapterClickListener clickListener) {
        this.context = ctx;
        this.follwerLists = follwerLists;
        this.clickListener = clickListener;
    }

    public void updateList(ArrayList<FollwerDataModel> list) {
        follwerLists = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.follower_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final FollwerDataModel follwer = follwerLists.get(position);
        // Setting Up Image
       /* Picasso.get()
                .load(Constants.BASE_URL + follwer.getUser_image_follwer())
                .placeholder(R.drawable.ic_profile_gray)
                .error(R.drawable.ic_profile_gray)
                .into(viewHolder.imageView);*/
        Picasso.get().load(Constants.BASE_URL + follwer.getUser_image_follwer())
                .placeholder(R.drawable.profile_image_placeholder)
                .into(viewHolder.imageView);
        //Uri uri = Uri.parse(Constants.BASE_URL + follwer.getUser_image_follwer());
       // viewHolder.imageView.setImageURI(uri);

        viewHolder.userName.setText("" + follwer.getUser_name_follwer());
        viewHolder.fullName.setText("" + follwer.getUser_full_name());


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
                viewHolder.btnFollowUnfollow.setText("Friends" + follwer.followstatus);
                viewHolder.btnFollowUnfollow.setTextColor(ContextCompat.getColor(context, R.color.white));
            }

        }
        viewHolder.bind(position, follwer, clickListener);
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
            imageView=itemView.findViewById(R.id.profileImage);
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
