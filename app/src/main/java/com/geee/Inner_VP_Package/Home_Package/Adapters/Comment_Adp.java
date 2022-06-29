package com.geee.Inner_VP_Package.Home_Package.Adapters;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Comment_Data_Model;
import com.geee.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Comment_Adp extends RecyclerView.Adapter<Comment_Adp.ViewHolder> {

    public Comment_Adp.onItemLongClickListener onItemLongClickListener;
    List<Comment_Data_Model> commentDataModels;
    Context context;
    private List<Integer> selectedIds = new ArrayList<>();

    public Comment_Adp(Context ctx, List<Comment_Data_Model> commentDataModels, Comment_Adp.onItemLongClickListener onItemLongClickListener) {
        this.context = ctx;
        this.commentDataModels = commentDataModels;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_chat_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        final Comment_Data_Model comment = commentDataModels.get(i);

        viewHolder.comment.setText("" + comment.getTime_ago());

      //  Uri uri = Uri.parse(Constants.BASE_URL + comment.getUser_img());
      //  viewHolder.profileImage.setImageURI(uri);

        Uri uri = Uri.parse(Constants.BASE_URL + comment.getUser_img());
      //  viewHolder.profileImage.setImageURI(uri);
        Picasso.get().load(uri).placeholder(R.drawable.profile_image_placeholder).into(viewHolder.profileImage);
        viewHolder.userName.setText(Html.fromHtml("<font color='#ffffff'> <b>" + comment.getUser_name() + "</b>" ));
        viewHolder.tv_commetn.setText(Html.fromHtml(comment.getPA_comment()));
        //Long Press
        viewHolder.mainRl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemClick(commentDataModels, i, v);
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return commentDataModels.size();
    }

    public Comment_Data_Model getItem(int position) {
        return commentDataModels.get(position);
    }

    public void setSelectedIds(List<Integer> selectedIds) {
        this.selectedIds = selectedIds;
        notifyDataSetChanged();
    }

    public interface onItemLongClickListener {
        void onItemClick(List<Comment_Data_Model> dmArrayList, int postion, View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        TextView userName, comment,tv_commetn;
        FrameLayout mainRl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.tv_username);
            tv_commetn = itemView.findViewById(R.id.tv_commetn);
            comment = itemView.findViewById(R.id.tv_fullname);
            mainRl = itemView.findViewById(R.id.main_RL_1);
        }
    }

}
