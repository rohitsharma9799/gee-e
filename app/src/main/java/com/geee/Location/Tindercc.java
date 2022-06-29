package com.geee.Location;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.geee.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class Tindercc extends RecyclerView.Adapter<Tindercc.MyViewHolder> implements Adapter {

        private List<CourseModal> moviesList;
        public SharedPreferences.Editor editor;
        SharedPreferences sharedpreferences;
        private String PREFS_NAME = "auth_info";
        Context context;

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView  title,decription,cost;
            ImageView imgview;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.idTVCourseName);
            }
            }

            public Tindercc(Context context, List<CourseModal> moviesList) {
              this.context=context;
              this.moviesList = moviesList;
            }

        @Override
        public Tindercc.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.course_rv_item, parent, false);
            return new Tindercc.MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(Tindercc.MyViewHolder holder, final int position) {
            final CourseModal movie = moviesList.get(position);
            holder.title.setText(moviesList.get(position).getCourseName());
            /*holder.decription.setText(moviesList.get(position).getdescription());
            holder.cost.setText(moviesList.get(position).getcost()+"/-Rs");*/
            //linearLayout.setBackgroundResource(imageData.get(position));



           /* holder.imgview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, Answer_Details.class);
                    intent.putExtra("title",moviesList.get(position).getname());
                    intent.putExtra("tone",moviesList.get(position).gettone());
                    intent.putExtra("image",moviesList.get(position).getBannerSrc());
                    intent.putExtra("desc",moviesList.get(position).getdescription());
                    intent.putExtra("answer",moviesList.get(position).getanswer());
                    intent.putExtra("cost",moviesList.get(position).getcost());
                    intent.putExtra("scheduled",moviesList.get(position).getscheduled());
                    intent.putExtra("scheduled_answer",moviesList.get(position).getscheduled_answer());
                    intent.putExtra("user_answer",moviesList.get(position).getuser_answer());
                    intent.putExtra("user_ans_verified",moviesList.get(position).getuser_ans_verified());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    context.startActivity(intent);
                }
            });*/
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

    }
