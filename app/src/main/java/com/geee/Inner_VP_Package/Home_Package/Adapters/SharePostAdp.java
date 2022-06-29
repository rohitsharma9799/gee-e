package com.geee.Inner_VP_Package.Home_Package.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.Share_Bottom_Sheet.SharePostUtil;
import com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.DataModel.FollwerDataModel;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SharePostAdp extends RecyclerView.Adapter<SharePostAdp.ViewHolder> {

    Context context;
    List<FollwerDataModel> follwerLists = new ArrayList<>();
    String postId;

    public SharePostAdp(Context ctx, List<FollwerDataModel> follwerLists, String postId) {
        this.context = ctx;
        this.follwerLists = follwerLists;
        this.postId = postId;
    }

    public void updateList(ArrayList<FollwerDataModel> list) {
        follwerLists = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SharePostAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.send_msg_to_follwer, null);
        return new SharePostAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SharePostAdp.ViewHolder viewHolder, int i) {
        // viewHolder.imageView.setImageResource(list[i]);

        final FollwerDataModel follwer = follwerLists.get(i);
        // Setting Up Image

        Picasso.get()
                .load(Constants.BASE_URL + follwer.getUser_image_follwer())
                .placeholder(R.drawable.ic_profile_gray)
                .error(R.drawable.ic_profile_gray)
                .into(viewHolder.imageView);

        //Uri uri = Uri.parse(Constants.BASE_URL + follwer.getUser_image_follwer());
        //viewHolder.imageView.setImageURI(uri);

        viewHolder.user_name.setText("" + follwer.getUser_name_follwer());
        viewHolder.full_name.setText("" + follwer.getUser_full_name());


        viewHolder.btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewHolder.btn_send.setVisibility(View.GONE);
                viewHolder.btn_after_send.setVisibility(View.VISIBLE);
                try {

                    SharePostUtil Share_Util = new SharePostUtil();
                    Share_Util.sendMessage(
                            SharedPrefrence.getUserNameFromJson(context),
                            ""
                                    + postId, SharedPrefrence.getUserIdFromJson(context),
                            "" + follwer.getUser_id_follower(),
                            "" + follwer.getUser_name_follwer(),
                            "" + Constants.BASE_URL + follwer.getUser_image_follwer()

                    );

                } catch (Exception b) {
                    b.printStackTrace();
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return follwerLists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView user_name, full_name;
        Button btn_send, btn_after_send;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.profileImage);
            user_name = itemView.findViewById(R.id.tv_username);
            full_name = itemView.findViewById(R.id.tv_fullname);
            btn_send = itemView.findViewById(R.id.btn_send);
            btn_after_send = itemView.findViewById(R.id.btn_after_send);


        }
    }


}
