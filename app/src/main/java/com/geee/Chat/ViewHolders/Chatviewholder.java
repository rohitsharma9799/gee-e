package com.geee.Chat.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.geee.Chat.ChatAdapter;
import com.geee.Chat.Chat_GetSet;
import com.geee.R;


/**
 * Created by AQEEL on 1/10/2019.
 */

public class Chatviewholder extends RecyclerView.ViewHolder {
    public TextView message,datetxt,message_seen,msg_date;
    public View view;

   public ImageView user_image;
    public TextView user_name,time_txt;
    public LinearLayout upperlayout;

    public Chatviewholder(View itemView) {
        super(itemView);
        view = itemView;

        this.message = view.findViewById(R.id.msgtxt);
        this.datetxt=view.findViewById(R.id.datetxt);
        message_seen=view.findViewById(R.id.message_seen);
        msg_date=view.findViewById(R.id.msg_date);

        user_name=view.findViewById(R.id.tv_tag);
        this.upperlayout=view.findViewById(R.id.upperlayout);
    }

    public void bind(final Chat_GetSet item,
                     final ChatAdapter.OnLongClickListener long_listener) {
        message.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                long_listener.onLongclick(item,v);
                return false;
            }
        });
    }

  }

