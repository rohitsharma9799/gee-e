package com.geee.tictokcode.WatchVideos;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.tictokcode.Video_Recording.Video_Recoder_A;
import com.google.android.exoplayer2.ui.PlayerView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

import com.geee.R;
import com.geee.tictokcode.Home.Home_Get_Set;
import com.geee.tictokcode.SimpleClasses.Functions;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Watch_Videos_Adapter extends RecyclerView.Adapter<Watch_Videos_Adapter.CustomViewHolder > {

    public Context context;
    private OnItemClickListener listener;
    private ArrayList<Home_Get_Set> dataList;



    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(int positon, Home_Get_Set item, View view);
    }

    public Watch_Videos_Adapter(Context context, ArrayList<Home_Get_Set> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_watch_layout,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.MATCH_PARENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }


    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        final Home_Get_Set item= dataList.get(i);

        try{

            holder.bind(i,item,listener);
            String  firstname1 = item.last_name.replaceAll("[0-9]","");
            holder.username.setText(firstname1);

            if((item.sound_name==null || item.sound_name.equals("") || item.sound_name.equals("null"))){
                holder.sound_name.setText("original sound - "+item.first_name+" "+item.last_name);
            }else {
                holder.sound_name.setText(item.sound_name);
            }
        holder.sound_name.setSelected(true);
        holder.desc_txt.setText(""+item.video_description);


            Uri u = Uri.parse(item.profile_pic);
            File f = new File("" + u);
            f.getName();
            Log.i("dfdfvvvdfd", f.getName());
        Picasso.get().
                load("https://gee-e.com//gee_tictok//API//upload//images//"+f.getName())
                .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                .resize(100,100).into(holder.user_pic);


            if((item.sound_name==null || item.sound_name.equals(""))
                    || item.sound_name.equals("null")){

                    item.sound_pic=item.profile_pic;

            }
            else if(item.sound_pic.equals(""))
                item.sound_pic="Null";

            Picasso.get().
                    load(item.sound_pic)
                    .placeholder(context.getResources().getDrawable(R.drawable.geeelogotransparent))
                    .resize(100,100).into(holder.sound_image);


            if(item.liked.equals("1")){
               // Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();
                holder.like_image.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_on));
            }
           else {
             //   Toast.makeText(context, "unlike", Toast.LENGTH_SHORT).show();

                holder.like_image.setImageDrawable(context.getResources().getDrawable(R.drawable.heartwhitds));
            }


            if(item.allow_comments!=null && item.allow_comments.equalsIgnoreCase("false"))
                holder.comment_layout.setVisibility(View.GONE);
            else
                holder.comment_layout.setVisibility(View.VISIBLE);



            Integer a = Integer.valueOf(item.artilike);
            Integer b = Integer.valueOf(item.like_count);
            int c = a+b;
            holder.like_txt.setText(Functions.GetSuffix(String.valueOf(c)));
            holder.comment_txt.setText(Functions.GetSuffix(item.video_comment_count));


            if(item.verified!=null && item.verified.equalsIgnoreCase("1")){
                holder.varified_btn.setVisibility(View.VISIBLE);
            }else {
                holder.varified_btn.setVisibility(View.GONE);
            }

        }catch (Exception e){

        }
   }



    class CustomViewHolder extends RecyclerView.ViewHolder {

        TextView username,desc_txt,sound_name;
        ImageView user_pic,sound_image,varified_btn;

        ImageView follow_this_user;
        LinearLayout like_layout,shared_layout,sound_image_layout,comment_layout;
        RelativeLayout sdi;
        ImageView like_image,comment_image;
        TextView like_txt,comment_txt;





        public CustomViewHolder(View view) {
            super(view);
            sdi=view.findViewById(R.id.sdi);
            username=view.findViewById(R.id.username);
            user_pic=view.findViewById(R.id.user_pic);
            sound_name=view.findViewById(R.id.sound_name);
            sound_image=view.findViewById(R.id.sound_image);
            varified_btn=view.findViewById(R.id.varified_btn);

            like_layout=view.findViewById(R.id.like_layout);
            like_image=view.findViewById(R.id.like_image);
            like_txt=view.findViewById(R.id.like_txt);


            desc_txt=view.findViewById(R.id.desc_txt);

            comment_layout=view.findViewById(R.id.comment_layout);
            comment_image=view.findViewById(R.id.comment_image);
            comment_txt=view.findViewById(R.id.comment_txt);

            follow_this_user=view.findViewById(R.id.follow_this_user);

            sound_image_layout=view.findViewById(R.id.sound_image_layout);
            shared_layout=view.findViewById(R.id.shared_layout);
        }

        public void bind(final int postion,final Home_Get_Set item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });


            sdi.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, View_user_Profile.class);
                    intent.putExtra("tictok",  "1");

                    intent.putExtra("user_id", item.hopeuserid); //Optional parameters
                    intent.putExtra("full_name",item.first_name); //Optional parameters
                    intent.putExtra("username",  item.username); //Optional parameters
                    intent.putExtra("image", item.profile_pic); //Optional parameters
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });

            username.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri u = Uri.parse(item.profile_pic);
                    File f = new File("" + u);
                    f.getName();
                    Intent intent = new Intent(context, View_user_Profile.class);
                    intent.putExtra("tictok",  "1");

                    intent.putExtra("user_id", item.hopeuserid); //Optional parameters
                    intent.putExtra("full_name",item.first_name); //Optional parameters
                    intent.putExtra("username",  item.username); //Optional parameters
                    intent.putExtra("image", "https://gee-e.com//gee_tictok//API//upload//images//"+f.getName()); //Optional parameters
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }
            });

            user_pic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Uri u = Uri.parse(item.profile_pic);
                    File f = new File("" + u);
                    f.getName();
                    Intent intent = new Intent(context, View_user_Profile.class);
                    intent.putExtra("tictok",  "1");

                    intent.putExtra("user_id", item.hopeuserid); //Optional parameters
                    intent.putExtra("full_name",item.first_name); //Optional parameters
                    intent.putExtra("username",  item.username); //Optional parameters
                    intent.putExtra("image", "https://gee-e.com//gee_tictok//API//upload//images//"+f.getName()); //Optional parameters
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);


                }
            });

            like_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });

            follow_this_user.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, Video_Recoder_A.class);
                    context.startActivity(intent);
                }
            });
            comment_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });

            shared_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    listener.onItemClick(postion,item,v);
                }
            });

            sound_image_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(postion,item,v);
                }
            });


        }


    }


}