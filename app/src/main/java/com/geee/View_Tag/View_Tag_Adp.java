package com.geee.View_Tag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Data_Model_Home;
import com.geee.R;

import java.util.List;

public class View_Tag_Adp extends RecyclerView.Adapter<View_Tag_Adp.ViewHolder> {

    public View_Tag_Adp.onclick itemclick;
    List<Data_Model_Home> userPosts;
    Context context;

    public View_Tag_Adp(Context context, List<Data_Model_Home> userPosts, View_Tag_Adp.onclick itemclick) {
        this.itemclick = itemclick;
        this.userPosts = userPosts;
        this.context = context;
    }

    @NonNull
    @Override
    public View_Tag_Adp.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prof_grid_item, null);
        return new View_Tag_Adp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull View_Tag_Adp.ViewHolder viewHolder, int i) {
        Data_Model_Home post = userPosts.get(i);

        viewHolder.img.setImageURI(Constants.BASE_URL + post.getAttachment());
        viewHolder.onbind(i, post, itemclick);
    }

    @Override
    public int getItemCount() {
        return userPosts.size();
    }

    public interface onclick {
        void onitem(int pos, Object model, View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.grid_item_id);
        }

        public void onbind(final int pos, Data_Model_Home post, final onclick listner) {

            itemView.setOnClickListener(view -> listner.onitem(pos, post, view));

        }
    }


}
