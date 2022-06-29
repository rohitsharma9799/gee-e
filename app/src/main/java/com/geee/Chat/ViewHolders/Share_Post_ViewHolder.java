package com.geee.Chat.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import com.geee.Chat.ChatAdapter;
import com.geee.Chat.Chat_GetSet;
import com.geee.R;

public class Share_Post_ViewHolder extends RecyclerView.ViewHolder {

  //  public ImageView chatimage;
    public TextView datetxt,message_seen;
    public ProgressBar p_bar;
    public ImageView not_send_message_icon;
    public View view;
    public SimpleDraweeView chatimage;


    public ImageView user_image, user_img;
    public TextView user_name,time_txt, user_name_for_post;
    public LinearLayout upperlayout;

    public Share_Post_ViewHolder(View itemView) {
        super(itemView);
        view = itemView;

        this.chatimage = view.findViewById(R.id.chatimage);
        this.datetxt=view.findViewById(R.id.datetxt);
        message_seen=view.findViewById(R.id.message_seen);
        not_send_message_icon=view.findViewById(R.id.not_send_messsage);
        p_bar=view.findViewById(R.id.p_bar);
        user_image = view.findViewById(R.id.user_profile_pic);
        user_name_for_post = view.findViewById(R.id.user_name_post);


        user_name=view.findViewById(R.id.tv_tag);
        this.upperlayout=view.findViewById(R.id.upperlayout);


    }

    public void bind(final Chat_GetSet item, final ChatAdapter.OnItemClickListener listener, final ChatAdapter.OnLongClickListener long_listener) {

        chatimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(item,v);
            }
        });

        chatimage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                long_listener.onLongclick(item,v);
                return false;
            }
        });
    }



}
