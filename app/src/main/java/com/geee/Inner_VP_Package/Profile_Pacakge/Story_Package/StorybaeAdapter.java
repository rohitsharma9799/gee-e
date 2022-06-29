package com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.geee.Constants;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StorybaeAdapter extends RecyclerView.Adapter<StorybaeAdapter.ViewHolder> {

    public StorybaeAdapter.OnItemClickListener listener;
    public StorybaeAdapter.onItemLongClickListener onItemLongClickListener;
    Context context;
    List<ShowStoryDM> storiesArr;
    String userProfileResponse;
    public StorybaeAdapter(Context ctx, List<ShowStoryDM> arrayList, StorybaeAdapter.OnItemClickListener listener, StorybaeAdapter.onItemLongClickListener onItemLongClickListener) {
        this.context = ctx;
        this.storiesArr = arrayList;
        this.listener = listener;
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @NonNull
    @Override
    public StorybaeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_story_layoutda, null);
        return new StorybaeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StorybaeAdapter.ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {
        final ShowStoryDM stories = storiesArr.get(i);
        //viewHolder.profileImage.setImageURI(Constants.BASE_URL + stories.getUserImg());
        Picasso.get().load(Constants.BASE_URL + stories.getUserImg())
                .placeholder(R.drawable.profile_image_placeholder)
                .into(viewHolder.profileImage);



        if (stories.getType().equals("video")){
            Log.i("Image File Path", ""+stories.getAttachment());

            Uri uri = Uri.parse(stories.getAttachment());

            try {
                long interval = 1*1000;
                RequestOptions options = new RequestOptions().frame(interval);

                Glide.with(context)
                        .asBitmap()
                        .load(uri)
                        .placeholder(R.drawable.blurload)
                        .apply(options)
                        .into(viewHolder.coverattachment);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (stories.getType().equals("image")){
            Picasso.get().load(stories.getAttachment())
                    .placeholder(R.drawable.blurload)
                    .into(viewHolder.coverattachment);
        }else {
            Picasso.get().load(Constants.BASE_URL + stories.getUserImg())
                    .placeholder(R.drawable.blurload)
                    .into(viewHolder.coverattachment);
        }


        if (i == 0) {
            if (stories.getUserImg() != null && !stories.getUserImg().equals("")) {
                Uri img = Uri.parse(stories.getUserImg().replaceAll("http://www.gee-e.com/gee/",""));
               // viewHolder.profileImage.setImageURI(Constants.BASE_URL+img);
                Picasso.get().load(Constants.BASE_URL + img)
                        .placeholder(R.drawable.profile_image_placeholder)
                        .into(viewHolder.profileImage);
                Log.i("dfgdfgd", stories.getUserImg());
            }
            Uri img = Uri.parse(stories.getUserImg().replaceAll("http://www.gee-e.com/gee/",""));
            Picasso.get().load(Constants.BASE_URL + img)
                    .placeholder(R.drawable.profile_image_placeholder)
                    .into(viewHolder.profileImage);
            // viewHolder.profileImage.setImageURI(Constants.BASE_URL+img);
//            viewHolder.userName.setText("" + stories.getUserName());
            Log.i("ouni", stories.getUserImg());
            if (stories.getType() != null && stories.getType().equals("first")) {
                viewHolder.add.setVisibility(View.VISIBLE);
               // Toast.makeText(context, "lig", Toast.LENGTH_SHORT).show();
               /* Uri img = Uri.parse(stories.getUserImg().replaceAll("https://www.gee-e.com/gee/",""));
                viewHolder.profileImage.setImageURI(Constants.BASE_URL+img);*/

              String a = Constants.BASE_URL + stories.getUserImg().replaceAll("http://www.gee-e.com/gee/","");
                Log.i("dfsdfsdf",a);
                Uri imgd = Uri.parse(stories.getUserImg().replaceAll("http://www.gee-e.com/gee/",""));

                Picasso.get().load( Constants.BASE_URL + img)
                        .placeholder(R.drawable.profile_image_placeholder)
                        .into(viewHolder.profileImage);
                userProfileResponse = SharedPrefrence.getOffline(context, SharedPrefrence.share_user_profile_pic);

                if (userProfileResponse != null) {
                    // If user profile is not null
                    try {
                        JSONObject userProfile = new JSONObject(userProfileResponse);
                        JSONObject userObj = userProfile.getJSONObject("msg").getJSONObject("User");
                        userObj.getString("image");

                        if (userObj.getString("image") != null && !userObj.getString("image").equals("")) {
                            //Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("image"));
                           // viewHolder.profileImage.setImageURI(uri);
                            Log.i("ursdhsi", String.valueOf(userObj.getString("image")));
                            //Toast.makeText(context, "mif", Toast.LENGTH_SHORT).show();
                            Picasso.get().load(Constants.BASE_URL + userObj.getString("image"))
                                    .placeholder(R.drawable.profile_image_placeholder)
                                    .into(viewHolder.profileImage);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                //Log.i("Dffsfsd",img.toString());
               // Toast.makeText(context, "f", Toast.LENGTH_SHORT).show();
            } else {
                viewHolder.add.setVisibility(View.VISIBLE);
                Picasso.get().load(Constants.BASE_URL + stories.getUserImg())
                        .placeholder(R.drawable.profile_image_placeholder)
                        .into(viewHolder.profileImage);
                //  viewHolder.profileImage.setImageURI(Constants.BASE_URL + stories.getUserImg());
                Log.i("ursdhsi", stories.getUserImg());
            }
        } else {
            if (stories.getUserImg() != null && !stories.getUserImg().equals("")) {
                //viewHolder.profileImage.setImageURI(Constants.BASE_URL + stories.getUserImg());
                Picasso.get().load(Constants.BASE_URL + stories.getUserImg())
                        .placeholder(R.drawable.profile_image_placeholder)
                        .into(viewHolder.profileImage);
            }
            Picasso.get().load(Constants.BASE_URL + stories.getUserImg())
                    .placeholder(R.drawable.profile_image_placeholder)
                    .into(viewHolder.profileImage);
          //  viewHolder.profileImage.setImageURI(Constants.BASE_URL + stories.getUserImg());
            //viewHolder.userName.setText("" + stories.getUserName());
        }

        viewHolder.rv_id.setOnClickListener(v -> {
            listener.onItemClick(storiesArr, i, v);
        });

        //Long Press
        viewHolder.rv_id.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemClick(storiesArr, i, v);
                return false;
            }
        });


        if (!stories.getUserId().equals("" + SharedPrefrence.getUserIdFromJson(context))) {
            viewHolder.add.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return storiesArr.size();
    }

    public interface OnItemClickListener {
        void onItemClick(List<ShowStoryDM> dmArrayList, int postion, View view);
    }

    public interface onItemLongClickListener {
        void onItemClick(List<ShowStoryDM> dmArrayList, int postion, View view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage;
        ImageView add;
        KenBurnsView coverattachment;
        TextView userName;
        RelativeLayout rv_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.imageview_id);
            add = itemView.findViewById(R.id.add_id);
            userName = itemView.findViewById(R.id.tv_tag);
            rv_id = itemView.findViewById(R.id.rv_id);
            coverattachment = itemView.findViewById(R.id.coverattachment);
        }
    }
}



