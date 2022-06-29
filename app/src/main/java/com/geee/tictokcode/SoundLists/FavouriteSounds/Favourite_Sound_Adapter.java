package com.geee.tictokcode.SoundLists.FavouriteSounds;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;

import com.geee.R;
import com.geee.tictokcode.SimpleClasses.Variables;
import com.geee.tictokcode.SoundLists.Sounds_GetSet;

/**
 * Created by AQEEL on 3/19/2019.
 */


class Favourite_Sound_Adapter extends RecyclerView.Adapter<Favourite_Sound_Adapter.CustomViewHolder > {
    public Context context;

    ArrayList<Sounds_GetSet> datalist;
    public interface OnItemClickListener {
        void onItemClick(View view, int postion, Sounds_GetSet item);
    }

    public OnItemClickListener listener;


    public Favourite_Sound_Adapter(Context context, ArrayList<Sounds_GetSet> arrayList, OnItemClickListener listener) {
        this.context = context;
        datalist= arrayList;
        this.listener=listener;
    }

    @Override
    public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_sound_layout,viewGroup,false);
        view.setLayoutParams(new RecyclerView.LayoutParams(Variables.screen_width-50, RecyclerView.LayoutParams.WRAP_CONTENT));
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public int getItemCount() {
        return datalist.size();
    }




    @Override
    public void onBindViewHolder(final CustomViewHolder holder, final int i) {
        holder.setIsRecyclable(false);

        Sounds_GetSet item=datalist.get(i);
        try {

            holder.sound_name.setText(item.sound_name);
            holder.description_txt.setText(item.description);

            if(item.thum!=null && !item.thum.equals("")) {
                Log.d(Variables.tag,item.thum);
                Uri uri = Uri.parse(item.thum);
                holder.sound_image.setImageURI(uri);
            }
            holder.fav_btn.setImageDrawable(context.getDrawable(R.drawable.ic_my_favourite));
            holder.bind(i, datalist.get(i), listener);


        }catch (Exception e){

        }

    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        ImageButton done,fav_btn;
        TextView sound_name,description_txt;
        SimpleDraweeView sound_image;

        public CustomViewHolder(View view) {
            super(view);
            done=view.findViewById(R.id.done);
            fav_btn=view.findViewById(R.id.fav_btn);


            sound_name=view.findViewById(R.id.sound_name);
            description_txt=view.findViewById(R.id.description_txt);
            sound_image=view.findViewById(R.id.sound_image);

        }

        public void bind(final int pos , final Sounds_GetSet item, final OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });

            done.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });

            fav_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(v,pos,item);
                }
            });

        }


    }




}

