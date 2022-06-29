package com.geee.Inner_VP_Package.Profile_Pacakge.Tag_Layout;

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

public class Tag_Adapter extends RecyclerView.Adapter<Tag_Adapter.ViewHolder> {

    public Tag_Adapter.onclick itemclick;
    List<Data_Model_Home> postsList;
    Context context;

    public Tag_Adapter(onclick itemclick, List<Data_Model_Home> postsList, Context context) {
        this.itemclick = itemclick;
        this.postsList = postsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prof_tag_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Data_Model_Home post = postsList.get(i);


        if(post.getAttachment() != null && !post.getAttachment().equals("")){
            viewHolder.img.setImageURI(Uri.parse(Constants.BASE_URL + post.getAttachment()));
        }


        viewHolder.onbind(i, itemclick);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }

    public interface onclick {
        void itemclick(int pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img =  itemView.findViewById(R.id.tv_tagpost);
        }

        public void onbind(final int pos, final Tag_Adapter.onclick listner) {
            itemView.setOnClickListener(v -> listner.itemclick(pos));
        }

    }
}
