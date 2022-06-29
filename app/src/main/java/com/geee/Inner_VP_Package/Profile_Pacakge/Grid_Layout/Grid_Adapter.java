package com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout;

import android.content.Context;
import android.net.Uri;
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

public class Grid_Adapter extends RecyclerView.Adapter<Grid_Adapter.ViewHolder> {

    public Grid_Adapter.onclick itemclick;
    List<Data_Model_Home> postsList;
    Context context;

    public Grid_Adapter(Grid_Adapter.onclick itemclick, List<Data_Model_Home> postsList, Context context) {
        this.itemclick = itemclick;
        this.postsList = postsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prof_grid_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Data_Model_Home post = postsList.get(i);
        Uri uri = Uri.parse(Constants.BASE_URL + post.getAttachment());
        viewHolder.img.setImageURI(uri);
        viewHolder.onbind(i, itemclick);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
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

        public void onbind(final int pos, final Grid_Adapter.onclick listner) {

            itemView.setOnClickListener(view -> listner.onitem(pos));

        }
    }
}
