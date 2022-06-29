package com.geee.tictokcode.Comments;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import com.geee.R;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Comments_Adapter extends RecyclerView.Adapter<Comments_Adapter.CustomViewHolder > {

    public Context context;
    private OnItemClickListener listener;
    private ArrayList<Comment_Get_Set> dataList;



    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, Comment_Get_Set item, View view);
    }

    public Comments_Adapter(Context context, ArrayList<Comment_Get_Set> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_comment_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        final Comment_Get_Set item= dataList.get(i);


        holder.username.setText(item.last_name);

        try{
            File file = new File(item.profile_pic);
            String strname = file.getName();
            Log.i("Dfsdfsdf",strname);

            //Uri uri = Uri.parse("https://gee-e.com//tictic//API//upload//images//"+strname);
            //holder.user_pic.setImageURI(uri);
            Picasso.get().
                    load("https://gee-e.com//gee_tictok//API//upload//images//"+strname)
                    .placeholder(R.drawable.profile_image_placeholder)
                    .into(holder.user_pic);

        }catch (Exception e){

        }

        holder.message.setText(item.comments);


       // holder.bind(i,item,listener);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(context, HomeActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(Constant.SHOW_FRAGMENT, Constant.OTHER_PROFILE);
                intent.putExtra(Constant.ARGUMENT_ID, item.hopeuserid);
                intent.putExtra("ticktokfbid", item.fb_id);
                context.startActivity(intent);*/
            }
        });

    }



    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView username,message;
        ImageView user_pic;


        public CustomViewHolder(View view) {
            super(view);

            username=view.findViewById(R.id.username);
            user_pic=view.findViewById(R.id.user_pic);
            message=view.findViewById(R.id.message);

        }

       /* public void bind(final int postion,final Comment_Get_Set item, final Comments_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    *//*listener.onItemClick(postion,item,v);*//*

                    Log.i("dfsdfsdfdsfs",item.hopeuserid);

                    Intent intent = new Intent(context, HomeActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra(Constant.SHOW_FRAGMENT, Constant.OTHER_PROFILE);
                    intent.putExtra(Constant.ARGUMENT_ID, item.hopeuserid);
                    intent.putExtra("ticktokfbid", item.fb_id);
                    context.startActivity(intent);
                }
            });

        }*/


    }
}





