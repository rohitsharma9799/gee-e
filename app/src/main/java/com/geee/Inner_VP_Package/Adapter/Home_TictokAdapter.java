package com.geee.Inner_VP_Package.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Data_Model_Home;
import com.geee.Inner_VP_Package.Home_Package.GeeeFeed;
import com.geee.Inner_VP_Package.Home_Package.Home;
import com.geee.Inner_VP_Package.Main_F;
import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.Main_VP_Package.Main;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.API_Calling_Methods;
import com.geee.tictokcode.Comments.Comment_F;
import com.geee.tictokcode.Home.Home_F;
import com.geee.tictokcode.Home.Home_Get_Set;
import com.geee.tictokcode.Main_Menu.MainMenuActivity;
import com.geee.tictokcode.SimpleClasses.API_CallBack;
import com.geee.tictokcode.SimpleClasses.Fragment_Data_Send;
import com.geee.tictokcode.SimpleClasses.Functions;
import com.geee.tictokcode.SimpleClasses.Variables;
import com.geee.tictokcode.WatchVideos.WatchVideos_F;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Home_TictokAdapter extends RecyclerView.Adapter<Home_TictokAdapter.CustomViewHolder > {

    public Context context;
    private OnItemClickListener listener;
    private ArrayList<Home_Get_Set> dataList;


    Home_TictokAdapter home_tictokAdapter;
      public interface OnItemClickListener {
        void onItemClick(int postion, Home_Get_Set item, View view);
    }

    public Home_TictokAdapter(Context context, ArrayList<Home_Get_Set> dataList, OnItemClickListener listener) {
        this.context = context;
        this.dataList = dataList;
        this.listener = listener;

    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_myvideo_layoutsds,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.WRAP_CONTENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return dataList.size();
    }



    class CustomViewHolder extends RecyclerView.ViewHolder {


        ImageView thumb_image,prof_photo_id,share_id;

        LinearLayout wholecard;
        RelativeLayout profileidf;
        LikeButton like_id;
        ImageView comment_id;
        TextView view_txt,username_id,post_desc_id,likes_count_id;

        public CustomViewHolder(View view) {
            super(view);

            thumb_image=view.findViewById(R.id.thumb_image);
            view_txt=view.findViewById(R.id.view_txt);

            prof_photo_id=view.findViewById(R.id.prof_photo_id);
            username_id=view.findViewById(R.id.username_id);
            post_desc_id=view.findViewById(R.id.post_desc_id);
            likes_count_id=view.findViewById(R.id.likes_count_id);
            share_id=view.findViewById(R.id.share_id);
            like_id=view.findViewById(R.id.like_id);
            comment_id = view.findViewById(R.id.comment_id);
            profileidf = view.findViewById(R.id.profileidf);
            wholecard = view.findViewById(R.id.wholecard);

        }

        public void bind(final int position,final Home_Get_Set item, final OnItemClickListener listener) {
            wholecard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,item,v);
                }
            });
            comment_id.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(position,item,v);
                }
            });
        }

    }




    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        final Home_Get_Set item= dataList.get(i);
        holder.setIsRecyclable(false);


        Uri u = Uri.parse(item.profile_pic);
        File f = new File("" + u);
        f.getName();
        Log.i("dfdfvvvdfd", f.getName());
        Picasso.get().
                load("https://gee-e.com//gee_tictok//API//upload//images//"+f.getName())
                .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                .resize(100,100).into(holder.prof_photo_id);


        holder.post_desc_id.setText(item.video_description);
     //   String  firstname1 = item.username.replaceAll("[0-9]","");
        holder.username_id.setText(item.last_name);

          /*  Glide.with(context)
                    .asGif()
                    .load(item.gif)
                    .skipMemoryCache(true)
                    .thumbnail(new RequestBuilder[]{Glide
                            .with(context)
                            .load(item.thum)})
                    .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.NONE)
                            .placeholder(context.getResources().getDrawable(R.drawable.image_placeholder)).centerCrop())
                    .into(holder.thumb_image);*/
            Picasso.get().
                    load(item.thum)
                    .into(holder.thumb_image);
        Log.i("dfdsfsf",item.profile_pic);

            /*if(item.thum!=null && !item.thum.equals("")) {
                Uri uri = Uri.parse(item.thum);
                holder.thumb_image.setImageURI(uri);
            }*/

        if(item.liked.equals("1")){
            // Toast.makeText(context, "liked", Toast.LENGTH_SHORT).show();
            holder.like_id.setLiked(true);
        }
        else {
            //   Toast.makeText(context, "unlike", Toast.LENGTH_SHORT).show();
            holder.like_id.setLiked(false);

            //holder.like_id.setImageDrawable(context.getResources().getDrawable(R.drawable.heart_off));
        }

        Integer a = Integer.valueOf(item.artilike);
        Integer b = Integer.valueOf(item.like_count);
        int c = a+b;
        holder.likes_count_id.setText(Functions.GetSuffix(String.valueOf(c)));
        holder.view_txt.setText(item.views+" Views");
        holder.view_txt.setText(Functions.GetSuffix(item.views)+" Views");
      /*  holder.comment_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onItemClick(i,item,view);
                *//*AppCompatActivity activity = (AppCompatActivity) view.getContext();

                Comment_F fragmentB=new Comment_F();
                String backStateName = fragmentB.getClass().getName();
                Bundle args=new Bundle();
                args.putString("video_id",item.video_id);
                args.putString("user_id",item.fb_id);
                fragmentB.setArguments(args);
                FragmentManager manager=activity.getFragmentManager();
                FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.abhit,fragmentB).commit();
                transaction.addToBackStack(backStateName);*//*
            }
        });*/

        holder.profileidf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("gccccccfhg",item.hopeuserid);
                Uri u = Uri.parse(item.profile_pic);
                File f = new File("" + u);
                f.getName();
                Intent intent = new Intent(context, View_user_Profile.class);
                intent.putExtra("user_id", item.hopeuserid); //Optional parameters
                intent.putExtra("full_name",item.first_name); //Optional parameters
                intent.putExtra("username",  item.username); //Optional parameters
                intent.putExtra("tictok",  "1"); //Optional parameters
                intent.putExtra("image", "https://gee-e.com//gee_tictok//API//upload//images//"+f.getName()); //Optional parameters
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);

            }
        });
        holder.like_id.setOnLikeListener(new OnLikeListener() {
            @Override
            public void liked(LikeButton likeButton) {
             String action=item.liked;

                if(action.equals("1")){
                    action="0";
                    item.like_count=""+(Integer.parseInt(item.like_count) -1);
                }else {
                    action="1";
                    item.like_count=""+(Integer.parseInt(item.like_count) +1);
                }


                dataList.remove(i);
                item.liked=action;
                dataList.add(i,item);
                notifyDataSetChanged();
               // notifyItemChanged(i);


                Functions.Call_Api_For_like_video((Activity) context, item.video_id,action ,new API_CallBack() {

                    @Override
                    public void ArrayData(ArrayList arrayList) {

                    }

                    @Override
                    public void OnSuccess(String responce) {

                    }

                    @Override
                    public void OnFail(String responce) {

                    }
                });
            }

            @Override
            public void unLiked(LikeButton likeButton) {
                String action=item.liked;

                if(action.equals("1")){
                    action="0";
                    item.like_count=""+(Integer.parseInt(item.like_count) -1);
                }else {
                    action="1";
                    item.like_count=""+(Integer.parseInt(item.like_count) +1);
                }


                dataList.remove(i);
                item.liked=action;
                dataList.add(i,item);
                notifyDataSetChanged();
                // notifyItemChanged(i);


                Functions.Call_Api_For_like_video((Activity) context, item.video_id,action ,new API_CallBack() {

                    @Override
                    public void ArrayData(ArrayList arrayList) {

                    }

                    @Override
                    public void OnSuccess(String responce) {

                    }

                    @Override
                    public void OnFail(String responce) {

                    }
                });

            }
        });


        holder.bind(i,item,listener);

   }

}