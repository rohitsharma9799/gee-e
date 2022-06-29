package com.geee.Inner_VP_Package.Profile_Pacakge.SavedPackage;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Data_Model_Home;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;

import java.util.List;

public class Saved_Adapter extends RecyclerView.Adapter<Saved_Adapter.ViewHolder> {

    public onclick itemclick;
    List<Data_Model_Home> user_posts;
    Context context;

    public Saved_Adapter(List<Data_Model_Home> user_posts, Context context) {

        this.user_posts = user_posts;
        this.context = context;
    }

    @NonNull
    @Override
    public Saved_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prof_grid_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Saved_Adapter.ViewHolder viewHolder, int i) {
        final Data_Model_Home post = user_posts.get(i);

        if (post.getAttachment() != null && !post.getAttachment().equals("")) {
            viewHolder.img.setImageURI(Constants.BASE_URL + post.getAttachment());

        }

        Log.i("Fdddfsfdsf",post.getAttachment());
        viewHolder.img.setOnClickListener(v -> ((SavedActivity) context).openPostDetail(post.getId(), SharedPrefrence.getUserIdFromJson(context)));

        viewHolder.onbind(i, itemclick);
    }

    @Override
    public int getItemCount() {
        return user_posts.size();
    }


    public interface onclick {
        void onitem(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.grid_item_id);
        }

        public void onbind(final int pos, final onclick listner) {

            itemView.setOnClickListener(view -> listner.onitem(pos));

        }
    }


}
