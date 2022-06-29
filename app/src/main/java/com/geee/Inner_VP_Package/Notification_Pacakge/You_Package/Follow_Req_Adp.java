package com.geee.Inner_VP_Package.Notification_Pacakge.You_Package;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.DataModel.FollowReqModel;
import com.geee.Interface.AdapterClickListener;
import com.geee.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class Follow_Req_Adp extends RecyclerView.Adapter<Follow_Req_Adp.ViewHolder> {

    Context context;
    List<FollowReqModel> followReqLists = new ArrayList<>();
    AdapterClickListener clickListener;

    public Follow_Req_Adp(Context ctx, List<FollowReqModel> followReqLists, AdapterClickListener clickListener) {
        this.context = ctx;
        this.followReqLists = followReqLists;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public Follow_Req_Adp.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.following_request_item, null);
        return new Follow_Req_Adp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final Follow_Req_Adp.ViewHolder viewHolder, int i) {
        final FollowReqModel followReqModel = followReqLists.get(i);

        //Uri uri = Uri.parse(Constants.BASE_URL + followReqModel.getUser_image_follwer());
        //viewHolder.imageView.setImageURI(uri);
        Picasso.get().load(Constants.BASE_URL + followReqModel.getUser_image_follwer())
                .placeholder(R.drawable.profile_image_placeholder)
                .into(viewHolder.imageView);
        viewHolder.userName.setText("" + followReqModel.getUser_name_follwer());
        viewHolder.fullName.setText("" + followReqModel.getUser_full_name());
        viewHolder.btnFollowUnfollow.setText("Confirm");
        viewHolder.btnFollowUnfollow.setBackground(ContextCompat.getDrawable(context, R.drawable.follow_bg));
        viewHolder.btnFollowUnfollow.setTextColor(ContextCompat.getColor(context, R.color.white));


        viewHolder.bind(i, followReqModel, clickListener);

    }

    @Override
    public int getItemCount() {
        return followReqLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        TextView userName, fullName;
        Button btnFollowUnfollow, deleteRequest;
        RelativeLayout rlMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.tv_username);
            fullName = itemView.findViewById(R.id.tv_fullname);
            btnFollowUnfollow = itemView.findViewById(R.id.btn_id);
            rlMain = itemView.findViewById(R.id.RL_main);
            deleteRequest = itemView.findViewById(R.id.delete_request);

        }


        public void bind(final int item, final FollowReqModel follwerDataModel, final AdapterClickListener listener) {

            deleteRequest.setOnClickListener(v -> {

                listener.onItemClick(item, follwerDataModel, v);


            });

            btnFollowUnfollow.setOnClickListener(v -> {
                listener.onItemClick(item, follwerDataModel, v);

            });

            rlMain.setOnClickListener(v -> {
                listener.onItemClick(item, follwerDataModel, v);

            });


        }
    }
}
