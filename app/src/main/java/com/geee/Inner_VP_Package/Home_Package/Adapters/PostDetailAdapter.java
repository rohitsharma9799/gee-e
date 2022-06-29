package com.geee.Inner_VP_Package.Home_Package.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.android.material.snackbar.Snackbar;
import com.jsibbold.zoomage.ZoomageView;
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.geee.CodeClasses.Functions;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Data_Model_Home;
import com.geee.Interface.AdapterClickListener;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.View_Tag.View_Tag_Detail;
import com.geee.Volley_Package.API_Calling_Methods;
import com.squareup.picasso.Picasso;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PostDetailAdapter extends RecyclerView.Adapter<PostDetailAdapter.ViewHolder> {
    public boolean isSelected = false;
    Context context;
    List<Data_Model_Home> dataModelHomeList;
    AdapterClickListener clickListener;
    private HashTagHelper mTextHashTagHelper;

    public PostDetailAdapter(Context ctx, List<Data_Model_Home> dataModelHomeList, AdapterClickListener itemclick) {
        this.context = ctx;
        this.clickListener = itemclick;
        this.dataModelHomeList = dataModelHomeList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post_detail, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final Data_Model_Home dataModelHome = dataModelHomeList.get(i);

        viewHolder.userName.setText("" + dataModelHome.getUser_name());
        viewHolder.usernameComment.setText("" + dataModelHome.getUser_name());
        viewHolder.postDescId.setText(dataModelHome.getCaption());

        mTextHashTagHelper = HashTagHelper.Creator.create(ContextCompat.getColor(context, R.color.colorPrimary), hashTag -> {
            Intent myIntent = new Intent(context, View_Tag_Detail.class);
            myIntent.putExtra("search", hashTag); //Optional parameters
            context.startActivity(myIntent);
        });

        mTextHashTagHelper.handle(viewHolder.postDescId);

        if (dataModelHome.getTotal_likes() != null && dataModelHome.getTotal_likes().equals("0")) {
            viewHolder.likesCountId.setVisibility(View.GONE);
        } else {
            viewHolder.likesCountId.setVisibility(View.VISIBLE);
        }

        if (dataModelHome.getIs_like().equals("1")) {
            viewHolder.postLike.setLiked(true);
        } else if (dataModelHome.getIs_like().equals("0")) {
            viewHolder.postLike.setLiked(false);
        }
        if (dataModelHome.getTotal_likes() != null && dataModelHome.getTotal_likes().equals("1")) {
            viewHolder.likesCountId.setText("" + dataModelHome.getTotal_likes() + " like");
        } else {
            viewHolder.likesCountId.setText("" + dataModelHome.getTotal_likes() + " likes");
        }
        try {
            Uri uri = Uri.parse(Constants.BASE_URL + dataModelHome.getAttachment());
           // viewHolder.attachment.setImageURI(uri);  // Attachment
            Picasso.get().load(uri)
                    .placeholder(R.drawable.image_placeholder)
                    .into(viewHolder.attachment);
            viewHolder.commentProfPhotoId.setImageURI(SharedPrefrence.getUserImageFromJson(context));
        } catch (Exception v) {
            v.printStackTrace();
        }
       // viewHolder.profPhotoId.setImageURI(Constants.BASE_URL+dataModelHome.getUser_img());
        Picasso.get()
                .load(Constants.BASE_URL + dataModelHome.getUser_img())
                .placeholder(R.drawable.ic_profile_gray)
                .error(R.drawable.ic_profile_gray)
                .into(viewHolder.profPhotoId);
      /*  Picasso.get()
                .load(Constants.BASE_URL + dataModelHome.getUser_img())
                .placeholder(R.drawable.ic_profile_gray)
                .error(R.drawable.ic_profile_gray)
                .into(viewHolder.profPhotoId);*/
//
//        if (dataModelHome.getIs_like().equals("1")) {
//            viewHolder.postLike.setLiked(true);
//        } else if (dataModelHome.getIs_like().equals("0")) {
//            viewHolder.postLike.setLiked(false);
//        }

//
//        if (dataModelHome.getIs_bookmark().equals("1")) {
//            // If book mark
//            viewHolder.btnAddCollection.setLiked(true);
//        } else if (dataModelHome.getIs_bookmark().equals("0")) {
//            // If Post is not Book mark
//            viewHolder.btnAddCollection.setLiked(false);
//        }

//
//        if (dataModelHome.commentCount != null && !dataModelHome.commentCount.equals("0")) {
//            viewHolder.tvViewComment.setText("View all " + dataModelHome.commentCount + " comments");
//        }

        if (dataModelHome.getLocation_string() != null && !dataModelHome.getLocation_string().equals("")) {
            viewHolder.tvLocation.setText(dataModelHome.getLocation_string());
            viewHolder.tvLocation.setVisibility(View.VISIBLE);
        }

//        viewHolder.postLike.setTag("" + dataModelHome.getTotal_likes());

        viewHolder.timeAgo.setText("" + Functions.getTimeAgoOrg(dataModelHome.getCreated()));

        viewHolder.onbind(i, dataModelHome, clickListener);
    }

    @Override
    public int getItemCount() {
        return dataModelHomeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        ImageView menu, comment, sendMessageId,profPhotoId;
        SimpleDraweeView  commentProfPhotoId;
        ZoomageView attachment;
        CoordinatorLayout coordinatorLayout;
        LikeButton btnAddCollection, postLike;
        TextView tvViewComment, postIdComment, tvLocation, userName, postDescId, likesCountId, timeAgo, latestComment, usernameComment;
        EditText commentOnPost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            menu = itemView.findViewById(R.id.menu_id);
            comment = itemView.findViewById(R.id.comment_id);
            coordinatorLayout = itemView.findViewById(R.id.corl_id);
            btnAddCollection = itemView.findViewById(R.id.saved_id);
            tvViewComment = itemView.findViewById(R.id.view_comment_id);
            postLike = itemView.findViewById(R.id.like_id);
            postIdComment = itemView.findViewById(R.id.post_id);
            commentOnPost = itemView.findViewById(R.id.edittext_id);
            userName = itemView.findViewById(R.id.username_id);
            profPhotoId = itemView.findViewById(R.id.prof_photo_id);
            attachment = itemView.findViewById(R.id.item_id);
            commentProfPhotoId = itemView.findViewById(R.id.comment_prof_photo_id);
            sendMessageId = itemView.findViewById(R.id.share_id);
            postDescId = itemView.findViewById(R.id.post_desc_id);
            likesCountId = itemView.findViewById(R.id.likes_count_id);
            timeAgo = itemView.findViewById(R.id.time_ago);
            latestComment = itemView.findViewById(R.id.latest_comment);
            usernameComment = itemView.findViewById(R.id.username_comment);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }


        public void onbind(final int pos, Data_Model_Home dataModelHome, final AdapterClickListener listner) {

            final Data_Model_Home posts = dataModelHomeList.get(pos);
            final String postId = posts.getId();

            comment.setOnClickListener(v -> clickListener.onItemClick(pos, dataModelHome, v));
            tvViewComment.setOnClickListener(v -> clickListener.onItemClick(pos, dataModelHome, v));
            menu.setOnClickListener(v -> clickListener.onItemClick(pos, dataModelHome, v));
            userName.setOnClickListener(v -> clickListener.onItemClick(pos, dataModelHome, v));
            profPhotoId.setOnClickListener(v -> clickListener.onItemClick(pos, dataModelHome, v));
            likesCountId.setOnClickListener(v -> clickListener.onItemClick(pos, dataModelHome, v));
            sendMessageId.setOnClickListener(v -> clickListener.onItemClick(pos, dataModelHome, v));
            postDescId.setOnClickListener(v -> clickListener.onItemClick(pos, dataModelHome, v));
            usernameComment.setOnClickListener(v -> clickListener.onItemClick(pos, dataModelHome, v));


            postIdComment.setOnClickListener(v -> {
                if (commentOnPost.getText().toString().length() == 0) {
                    // If user is trying to upload empty comment
                    Functions.toastMsg(context, "Comment cant be empty");
                } else {
                    // If comment is not empty
                    API_Calling_Methods.addPostAction(
                            "" + SharedPrefrence.getUserIdFromJson(context),
                            "" + postId,
                            "comment",
                            "" + commentOnPost.getText().toString(),
                            context
                    );
                    String name = SharedPrefrence.getUserNameFromJson(context);
                    String okComment = "<font color='#000000'> <b>" + name + " </b> </font> " + commentOnPost.getText().toString() + "<br>";
                    latestComment.setVisibility(View.VISIBLE);
                    latestComment.append(Html.fromHtml(okComment));
                    commentOnPost.setText("");
                    final Data_Model_Home modelHome = dataModelHomeList.get(pos);
                    int count = Integer.parseInt(modelHome.commentCount);
                    count = count + 1;
                    tvViewComment.setText("View all " + count + " comments");
                }
            });


            postLike.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    final Data_Model_Home dataModelHome = dataModelHomeList.get(pos);
                    dataModelHome.getTotal_likes();
                    //int numLikes = Integer.parseInt("" + postLike.getTag());
                    API_Calling_Methods.addPostBookmark(
                            "" + SharedPrefrence.getUserIdFromJson(context), "" + postId,
                            "like", "1", context);
                    sendFCMPush(dataModelHome.getdevice_token(),
                            SharedPrefrence.getUserIdFromJson(context)+" Like your post",
                            Constants.BASE_URL+dataModelHome.getAttachment());
                    /*numLikes = numLikes + 1;
                    postLike.setTag("" + numLikes);
                    likesCountId.setVisibility(View.VISIBLE);
                    if (numLikes == 1) {
                        likesCountId.setText("" + numLikes + " Like");
                    } else {
                        likesCountId.setText("" + numLikes + " Likes");
                    }*/
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                   // int numLikes = Integer.parseInt("" + postLike.getTag());
                    API_Calling_Methods.addPostBookmark("" + SharedPrefrence.getUserIdFromJson(context),
                            "" + postId, "like", "0", context
                    );

                   /* if (numLikes == 0) {
                        likesCountId.setVisibility(View.GONE);
                    } else {
                        likesCountId.setVisibility(View.VISIBLE);
                    }

                    if (numLikes > 0) {
                        numLikes = numLikes - 1;
                        postLike.setTag("" + numLikes);
                        if (numLikes == 0) {
                            likesCountId.setVisibility(View.GONE);
                        } else if (numLikes == 1) {
                            likesCountId.setText("" + numLikes + " Like");
                        } else {
                            likesCountId.setText("" + numLikes + " Likes");
                        }
                    } else {
                        likesCountId.setVisibility(View.GONE);
                    }*/


                }
            });


            btnAddCollection.setOnLikeListener(new OnLikeListener() {
                @SuppressLint("ResourceType")
                @Override
                public void liked(LikeButton likeButton) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Post Saved.", Snackbar.LENGTH_SHORT);
                    isSelected = true;
                    // Apply API   bookmark
                    API_Calling_Methods.addPostBookmark(
                            "" + SharedPrefrence.getUserIdFromJson(context),
                            "" + postId, "bookmark", "1", context);

                    // End APPly API
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    TextView textView = view.findViewById(R.id.snackbar_text);
                    textView.setTextColor(R.color.black);
                    snackbar.show();

                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    isSelected = true;
                    API_Calling_Methods.addPostBookmark(
                            "" + SharedPrefrence.getUserIdFromJson(context),
                            "" + postId, "bookmark", "0", context);


                }
            });


        }
    }
    private void sendFCMPush(String token, String message,String attachment) {

        // Log.i("fdddfffff",token);
        final JSONObject[] obj = {null};
        final JSONObject[] objData = {null};
        final JSONObject[] dataobjData = {null};
        // TO get device token of other user id

        try {
            obj[0] = new JSONObject();
            objData[0] = new JSONObject();

            objData[0].put("title", SharedPrefrence.getUserNameFromJson(context));
            objData[0].put("body",message);
            objData[0].put("sound", "default");
            objData[0].put("image", attachment); //   icon_name image must be there in drawable
            objData[0].put("tag", token);
            objData[0].put("priority", "high");
            objData[0].put("click_action", "OPEN_ACTIVITY_1");
            dataobjData[0] = new JSONObject();
            dataobjData[0].put("title", SharedPrefrence.getUserNameFromJson(context));
            dataobjData[0].put("text",message );
            obj[0].put("to", token);
            //obj.put("priority", "high");
            obj[0].put("notification", objData[0]);
            obj[0].put("data", dataobjData[0]);
            Log.e("fdfsdfdsfg", obj[0].toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsObjRequest = new JsonObjectRequest(Request.Method.POST, "https://fcm.googleapis.com/fcm/send", obj[0],
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("!_@@_SUCESS", response + "");
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("!_@@_Errors--", error + "");
                    }
                }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Authorization", "key=" + Constants.FIREBASEKEY);
                params.put("Content-Type", "application/json");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        int socketTimeout = 0;// 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        jsObjRequest.setRetryPolicy(policy);
        requestQueue.add(jsObjRequest);
    }
}













