package com.geee.Inner_VP_Package.Home_Package.Adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.geee.Interface.AdapterClickListener;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Like_DataModel;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;

import java.util.ArrayList;
import java.util.List;

public class LikesAdp extends RecyclerView.Adapter<LikesAdp.ViewHolder> {

    Context context;
    List<Like_DataModel> likeDataModels;
    AdapterClickListener clickListener;

    public LikesAdp(List<Like_DataModel> likeDataModels, Context context, AdapterClickListener clickListener) {
        this.context = context;
        this.likeDataModels = likeDataModels;
        this.clickListener = clickListener;
    }

    @NonNull
    @Override
    public LikesAdp.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.like_user_item, null);
        return new LikesAdp.ViewHolder(view);
    }

    public void updateList(ArrayList<Like_DataModel> list) {
        likeDataModels = list;
        notifyDataSetChanged();
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Like_DataModel likeDataModel = likeDataModels.get(i);


        if (likeDataModel.getUser_pic().equals("")) {
            Uri uri = Uri.parse(Variables.errorUrl);
            viewHolder.iv.setImageURI(uri);
        } else {
            try {
                Uri uri = Uri.parse(Constants.BASE_URL + likeDataModel.getUser_pic());
                viewHolder.iv.setImageURI(uri);
            } catch (Exception b) {
                b.printStackTrace();
            }
        }
        viewHolder.userName.setText("" + likeDataModel.getUser_name());
        viewHolder.fullName.setText("" + likeDataModel.getFull_name());

        if (likeDataModel.getUser_id().equals(SharedPrefrence.getUserIdFromJson(context))) {
            viewHolder.actionBtn.setVisibility(View.GONE);
        } else {
            Functions.logDMsg("like follow status : " + likeDataModel.getFollow_unfolow());
            Functions.logDMsg("like follow name : " + likeDataModel.getFull_name());
            if (likeDataModel.getFollow_unfolow().contains("Requested")) {
                viewHolder.actionBtn.setBackground(ContextCompat.getDrawable(context,R.drawable.border_line_transparent));
                viewHolder.actionBtn.setText("" + likeDataModel.getFollow_unfolow());
                viewHolder.actionBtn.setTextColor(ContextCompat.getColor(context,R.color.white));
            } else if (likeDataModel.getFollow_unfolow().equals("Following")) {
                viewHolder.actionBtn.setBackground(ContextCompat.getDrawable(context,R.drawable.border_line_transparent));
                viewHolder.actionBtn.setText("Message");
                viewHolder.actionBtn.setTextColor(ContextCompat.getColor(context,R.color.white));
            } else {
                viewHolder.actionBtn.setBackground(ContextCompat.getDrawable(context,R.drawable.follow_bg));
                viewHolder.actionBtn.setText("" + likeDataModel.getFollow_unfolow());
                viewHolder.actionBtn.setTextColor(ContextCompat.getColor(context,R.color.white));
            }
        }

        viewHolder.onbind(i, likeDataModel, clickListener);
    }

    @Override
    public int getItemCount() {
        return likeDataModels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView iv;
        TextView userName, fullName;
        Button actionBtn;
        RelativeLayout relativeLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.profile_pic);
            userName = itemView.findViewById(R.id.tv_tag);
            fullName = itemView.findViewById(R.id.full_name);
            actionBtn = itemView.findViewById(R.id.action_btn);
            relativeLayout = itemView.findViewById(R.id.RL_Main);
        }


        public void onbind(final int pos, Like_DataModel likeDataModel, final AdapterClickListener listner) {

            actionBtn.setOnClickListener(v -> listner.onItemClick(pos, likeDataModel, v));
            relativeLayout.setOnClickListener(v -> listner.onItemClick(pos, likeDataModel, v));

        }
    }
}
