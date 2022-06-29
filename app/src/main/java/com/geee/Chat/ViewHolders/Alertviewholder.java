package com.geee.Chat.ViewHolders;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.geee.R;


/**
 * Created by AQEEL on 1/10/2019.
 */

public class Alertviewholder extends RecyclerView.ViewHolder {
   public TextView message,datetxt;
   public View view;
    public Alertviewholder(View itemView) {
        super(itemView);
        view = itemView;
        this.message = view.findViewById(R.id.message);
        this.datetxt = view.findViewById(R.id.datetxt);
    }

}