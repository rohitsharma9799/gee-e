package com.geee.Inner_VP_Package.Profile_Pacakge.Linear_Layout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
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
import com.like.LikeButton;
import com.like.OnLikeListener;
import com.geee.Constants;
import com.geee.Interface.AdapterClickListener;
import com.geee.CodeClasses.Functions;
import com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.DataModel.Grid_Data_Model;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.View_Tag.View_Tag_Detail;
import com.geee.Volley_Package.API_Calling_Methods;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Linear_Adapter extends RecyclerView.Adapter<Linear_Adapter.ViewHolder> {
    AdapterClickListener click;
    List<Grid_Data_Model> postsList;
    Context context;
    private HashTagHelper mTextHashTagHelper;

    public Linear_Adapter(List<Grid_Data_Model> postsList, Context context, AdapterClickListener adapterClickListener) {
        this.click = adapterClickListener;
        this.postsList = postsList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.prof_linear_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Grid_Data_Model post = postsList.get(i);

        viewHolder.userName.setText("" + post.getUser_name());
        viewHolder.username_comment.setText("" + post.getUser_name());

        String boldText = post.getUser_name();
        String normalText = post.getCaption();
        SpannableString str = new SpannableString(boldText + " " + normalText);
        str.setSpan(new StyleSpan(Typeface.BOLD), 0, boldText.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        viewHolder.postDescId.setText(post.getCaption());

        mTextHashTagHelper = HashTagHelper.Creator.create(ContextCompat.getColor(context, R.color.hashtag_color), hashTag -> {
            Intent myIntent = new Intent(context, View_Tag_Detail.class);
            myIntent.putExtra("search", hashTag); //Optional parameters
            context.startActivity(myIntent);
        });

        mTextHashTagHelper.handle(viewHolder.postDescId);

        viewHolder.postLike.setTag("" + post.getTotal_likes());

        try {
            Uri uri = Uri.parse(Constants.BASE_URL + post.getAttachment());
            viewHolder.iv.setImageURI(uri);  // Attachment
            viewHolder.profPhotoId.setImageURI(Constants.BASE_URL + post.getUser_img()); // Post Profile Pic
            viewHolder.commentProfPhotoId.setImageURI(SharedPrefrence.getUserImageFromJson(context));
        } catch (Exception v) {
            v.printStackTrace();
        }

        if (post.getIs_like().equals("1")) {
            // Display post like
            viewHolder.postLike.setLiked(true);
        } else if (post.getIs_like().equals("0")) {
            // If post is not like
            viewHolder.postLike.setLiked(false);
        }

        if (post.getTotal_likes() != null && post.getTotal_likes().equals("0")) {
            viewHolder.likesCountId.setVisibility(View.GONE);
        } else {
            viewHolder.likesCountId.setVisibility(View.VISIBLE);
        }

        if (post.getTotal_likes().equals("1")) {
            viewHolder.likesCountId.setText("" + post.getTotal_likes() + " like");
        } else {
            viewHolder.likesCountId.setText("" + post.getTotal_likes() + " likes");
        }

        viewHolder.timeAgo.setText("" + Functions.getTimeAgoOrg(post.getCreated()));

        if (post.getIs_like().equals("1")) {
            // Display post like

            viewHolder.postLike.setLiked(true);
        } else if (post.getIs_like().equals("0")) {
            // If post is not like
            viewHolder.postLike.setLiked(false);
        }

        if (post.getIs_bookmark().equals("1")) {
            // If book mark
            viewHolder.btnAddCollection.setLiked(true);
        } else if (post.getIs_bookmark().equals("0")) {
            // If Post is not Book mark
            viewHolder.btnAddCollection.setLiked(false);
        }

        if (post.getLocation_string() != null && !post.getLocation_string().equals("")) {
            viewHolder.tvLocation.setText(post.getLocation_string());
            viewHolder.tvLocation.setVisibility(View.VISIBLE);
        }

        if (post.commentCount != null && !post.commentCount.equals("0")) {
            viewHolder.tvViewComment.setText("View all " + post.commentCount + " comments");
        }

        viewHolder.onbind(i, post, click);
    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView menu, comment;
        SimpleDraweeView iv, profPhotoId, commentProfPhotoId;
        CoordinatorLayout corl;
        LikeButton btnAddCollection, postLike;
        TextView postIdComment, tvViewComment, tvLocation, userName, postDescId, likesCountId, timeAgo, latestComment, username_comment;
        EditText edittext_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            iv = itemView.findViewById(R.id.item_id);
            menu = itemView.findViewById(R.id.menu_id);
            comment = itemView.findViewById(R.id.comment_id);
            corl = itemView.findViewById(R.id.corl_id);
            btnAddCollection = itemView.findViewById(R.id.saved_id);
            postLike = itemView.findViewById(R.id.like_id);
            postIdComment = itemView.findViewById(R.id.post_id);
            userName = itemView.findViewById(R.id.username_id);
            profPhotoId = itemView.findViewById(R.id.prof_photo_id);
            commentProfPhotoId = itemView.findViewById(R.id.comment_prof_photo_id);
            postDescId = itemView.findViewById(R.id.post_desc_id);
            likesCountId = itemView.findViewById(R.id.likes_count_id);
            timeAgo = itemView.findViewById(R.id.time_ago);
            edittext_id = itemView.findViewById(R.id.edittext_id);
            latestComment = itemView.findViewById(R.id.latest_comment);
            username_comment = itemView.findViewById(R.id.username_comment);
            tvViewComment = itemView.findViewById(R.id.tvViewComment);
            tvLocation = itemView.findViewById(R.id.tvLocation);
        }

        public void onbind(final int pos, Grid_Data_Model post, final AdapterClickListener listner) {

            final Grid_Data_Model posts = postsList.get(pos);
            final String postsId = posts.getId();


            tvViewComment.setOnClickListener(v -> {
                listner.onItemClick(pos, post, v);
            });


            comment.setOnClickListener(v -> {
                listner.onItemClick(pos, post, v);
            });

            postDescId.setOnClickListener(v -> {
                listner.onItemClick(pos, post, v);
            });

            postIdComment.setOnClickListener(v -> {
                if (edittext_id.getText().toString().length() == 0) {
                    // If user is trying to upload empty comment
                    Functions.toastMsg(context, "Comment cant be empty");
                } else {
                    // If comment is not empty
                    API_Calling_Methods.addPostAction(
                            "" + SharedPrefrence.getUserIdFromJson(context),
                            "" + postsId,
                            "comment",
                            "" + edittext_id.getText().toString(),
                            context

                    );
                    String name = SharedPrefrence.getUserNameFromJson(context);
                    String okComment = "<font color='#000000'> <b>" + name + " </b> </font> " + edittext_id.getText().toString() + "<br>";

                    latestComment.setVisibility(View.VISIBLE);
                    latestComment.append(Html.fromHtml(okComment));

                    edittext_id.setText("");
                    Grid_Data_Model dataModel = postsList.get(pos);
                    int count = Integer.parseInt(dataModel.commentCount);
                    count = count + 1;
                    tvViewComment.setText("View all " + count + " comments");
                }
            });


            postLike.setOnLikeListener(new OnLikeListener() {
                @Override
                public void liked(LikeButton likeButton) {
                    final Grid_Data_Model dataModelHome = postsList.get(pos);
                    dataModelHome.getTotal_likes();
                    int numLikes = Integer.parseInt("" + postLike.getTag());
                    API_Calling_Methods.addPostBookmark(
                            "" + SharedPrefrence.getUserIdFromJson(context),
                            "" + postsId,
                            "like",
                            "1",
                            context

                    );

                    sendFCMPush(dataModelHome.getdevice_token(),
                            SharedPrefrence.getUserIdFromJson(context)+" Like your post",
                            Constants.BASE_URL+dataModelHome.getAttachment());
                    numLikes = numLikes + 1;

                    postLike.setTag("" + numLikes);
                    if (numLikes <= 1) {
                        likesCountId.setText("" + numLikes + " Like");
                    } else {
                        likesCountId.setText("" + numLikes + " Likes");
                    }
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    int numLikes = Integer.parseInt("" + postLike.getTag());
                    API_Calling_Methods.addPostBookmark(
                            "" + SharedPrefrence.getUserIdFromJson(context),
                            "" + postsId,
                            "like",
                            "0",
                            context
                    );

                    if (numLikes == 0) {
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
                    }


                }
            });

            btnAddCollection.setOnLikeListener(new OnLikeListener() {
                @SuppressLint("ResourceType")
                @Override
                public void liked(LikeButton likeButton) {
                    Snackbar snackbar = Snackbar.make(corl, "Post Saved.", Snackbar.LENGTH_SHORT);
                    API_Calling_Methods.addPostBookmark(
                            "" + SharedPrefrence.getUserIdFromJson(context),
                            "" + postsId,
                            "bookmark",
                            "1",
                            context

                    );
                    // End APPly API
                    View view = snackbar.getView();
                    view.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
                    TextView tv = (TextView) view.findViewById(R.id.snackbar_text);
                    tv.setTextColor(R.color.black);
                    snackbar.show();
                }

                @Override
                public void unLiked(LikeButton likeButton) {
                    API_Calling_Methods.addPostBookmark(
                            "" + SharedPrefrence.getUserIdFromJson(context),
                            "" + postsId,
                            "bookmark",
                            "0",
                            context

                    );

                }
            });

            menu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listner.onItemClick(pos, post, v);
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
