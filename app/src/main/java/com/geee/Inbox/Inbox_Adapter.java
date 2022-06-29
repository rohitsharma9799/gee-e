package com.geee.Inbox;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.geee.CodeClasses.Functions;
import com.geee.R;

/**
 * Created by AQEEL on 3/20/2018.
 */

public class Inbox_Adapter extends RecyclerView.Adapter<Inbox_Adapter.CustomViewHolder > implements Filterable {
    public Context context;
    ArrayList<Inbox_Get_Set> inbox_dataList = new ArrayList<>();
    ArrayList<Inbox_Get_Set> inbox_dataList_filter = new ArrayList<>();
    private Inbox_Adapter.OnItemClickListener listener;

    Integer today_day=0;

    // meker the onitemclick listener interface and this interface is impliment in Chatinbox activity
    // for to do action when user click on item
    public interface OnItemClickListener {
        void onItemClick(Inbox_Get_Set item);
    }

    public void updateList(ArrayList<Inbox_Get_Set> list){
        inbox_dataList_filter = list;
        notifyDataSetChanged();
    }

    public Inbox_Adapter(Context context, ArrayList<Inbox_Get_Set> user_dataList, Inbox_Adapter.OnItemClickListener listener) {
        this.context = context;
        this.inbox_dataList=user_dataList;
        this.inbox_dataList_filter=user_dataList;
        this.listener = listener;
        // get the today as a integer number to make the dicision the chat date is today or yesterday
        Calendar cal = Calendar.getInstance();
        today_day = cal.get(Calendar.DAY_OF_MONTH);
    }


    @Override
    public Inbox_Adapter.CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewtype) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_inbox_list,null);
        view.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        Inbox_Adapter.CustomViewHolder viewHolder = new Inbox_Adapter.CustomViewHolder(view);
        return viewHolder;
    }

    @Override
    public int getItemCount() {
       return inbox_dataList_filter.size();
    }


    class CustomViewHolder extends RecyclerView.ViewHolder {
        TextView user_name,last_message,date_created;
        ImageView user_image;
        RelativeLayout mainlayout;

        public CustomViewHolder(View view) {
            super(view);
            this.mainlayout=view.findViewById(R.id.mainlayout);
            this.user_name=view.findViewById(R.id.username);
            this.last_message=view.findViewById(R.id.message);
            this.date_created=view.findViewById(R.id.datetxt);
            this.user_image=view.findViewById(R.id.userimage);

        }

        public void bind(final Inbox_Get_Set item, final Inbox_Adapter.OnItemClickListener listener) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(item);
                }
            });

        }

    }


    @Override
    public void onBindViewHolder(final Inbox_Adapter.CustomViewHolder holder, final int i) {
        final Inbox_Get_Set item=inbox_dataList_filter.get(i);

        holder.bind(item,listener);

        String check = Functions.parseDateToddMMyyyy(item.getDate(),context);

        holder.date_created.setText(Functions.getTimeAgoOrg(check));

        //  check
        holder.last_message.setText(item.getMsg());
        holder.user_name.setText(item.getName());
        Picasso.get().load(item.getPic())
                .placeholder(R.drawable.ic_profile_gray)
                .error(R.drawable.ic_profile_gray)
                .resize(100, 100)
                .into(holder.user_image);
     //  holder.user_image.setImageURI(Uri.parse(item.getPic()));


        Log.i("dfdfsfdssds",item.getName());
        Log.i("dfdfsfds",item.getPic());
   }



    // this method will cahnge the date to  "today", "yesterday" or date
    public String ChangeDate(String date){
        try {
            //current date in millisecond
            long currenttime = System.currentTimeMillis();

            //database date in millisecond
            SimpleDateFormat f = new SimpleDateFormat("dd-MM-yyyy hh:mm:ssZZ");
            long databasedate = 0;
            Date d = null;
            try {
                d = f.parse(date);
                databasedate = d.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            long difference = currenttime - databasedate;
            if (difference < 86400000) {
                int chatday = Integer.parseInt(date.substring(0, 2));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if (today_day == chatday)
                    return sdf.format(d);
                else if ((today_day - chatday) == 1)
                    return "Yesterday";
            } else if (difference < 172800000) {
                int chatday = Integer.parseInt(date.substring(0, 2));
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
                if ((today_day - chatday) == 1)
                    return "Yesterday";
            }

            SimpleDateFormat sdf = new SimpleDateFormat("MMM-dd-yyyy");
            return sdf.format(d);
        }catch (Exception e){

        }finally {
            return "";
        }
    }


    // that function will filter the result
    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    inbox_dataList_filter = inbox_dataList;
                } else {
                    ArrayList<Inbox_Get_Set> filteredList = new ArrayList<>();
                    for (Inbox_Get_Set row : inbox_dataList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    inbox_dataList_filter = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = inbox_dataList_filter;
                return filterResults;

            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                inbox_dataList_filter = (ArrayList<Inbox_Get_Set>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }



}