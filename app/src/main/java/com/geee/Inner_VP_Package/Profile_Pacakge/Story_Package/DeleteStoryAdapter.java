package com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.DataModel.Comment_Data_Model;
import com.geee.R;
import com.geee.Utils.MyApplication;
import com.geee.tictokcode.Main_Menu.RelateToFragment_OnBack.RootFragment;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class DeleteStoryAdapter extends RecyclerView.Adapter<DeleteStoryAdapter.ViewHolder> {

    List<Story_DataModel> commentDataModels;
    Context context;
    private List<Integer> selectedIds = new ArrayList<>();

    DeleteFragment deleteFragment;
    public DeleteStoryAdapter(Context ctx, List<Story_DataModel> commentDataModels,DeleteFragment deleteFragmentd){
        this.context = ctx;
        this.commentDataModels = commentDataModels;
        this.deleteFragment = deleteFragmentd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.comment_chat_item, null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, @SuppressLint("RecyclerView") int i) {

        final Story_DataModel comment = commentDataModels.get(i);
        viewHolder.userName.setText("#" + comment.getType());

        Uri uri = Uri.parse(comment.getImage_url());
      //  viewHolder.profileImage.setImageURI(uri);

        viewHolder.delete.setVisibility(View.VISIBLE);

        if (comment.getType().equals("image")){
            Picasso.get().
                    load(comment.getImage_url())
                    .into(viewHolder.profileImage);
        }else {
            Glide.with(context).
                    load(uri).
                    thumbnail(0.1f)
                    .into(viewHolder.profileImage);
        }
        viewHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.DatePickerDialogTheme);
                    builder.setTitle("Are you sure want to delete");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Functions.showLoader(context,false,false);
                            final JSONObject json1 = new JSONObject();
                            try {
                                json1.put("id", comment.getUser_id());

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Log.i("Dfsdfsdf",API_LINKS.API_DELETE+json1);


                            final String url = API_LINKS.API_DELETE;
                            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DEPRECATED_GET_OR_POST, url, json1,
                                    new Response.Listener<JSONObject>() {
                                        @Override
                                        public void onResponse(JSONObject response) {
                                            Functions.cancelLoader();
                                            commentDataModels.remove(i);
                                            notifyItemRemoved(i);
                                            notifyItemRangeChanged(i, commentDataModels.size());
                                            if (commentDataModels.size()==0){
                                                ((DeleteFragment)deleteFragment).back();
                                            }
                                        }
                                    }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Toast.makeText(context, error.toString(), Toast.LENGTH_LONG).show();
                                    Functions.cancelLoader();
                                    Log.i("Dfsdfsdf",error.toString());
                                }
                            }){

                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    //imageEncoded=getStringImage(bitmap);
                                    //Log.i("image", encodedImageList.toString());
                                    Map<String, String> params = new Hashtable<String, String>();
                                    //params.put("imagefile", encodedImageList.toString());
                                    return params;
                                }
                            };
                            jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy( 200*30000,
                                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                            Volley.newRequestQueue(context).add(jsonObjectRequest);
                        }
                    });
                    builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    final AlertDialog alertDialog = builder.create();
                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(context, R.color.colorPrimary));
                            alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(context, R.color.black));
                        }
                    });
                    alertDialog.show();
                }

        });
       /* viewHolder.comment.setText("" + comment.getTime_ago());

        Uri uri = Uri.parse(Constants.BASE_URL + comment.getUser_img());
        viewHolder.profileImage.setImageURI(uri);

        viewHolder.userName.setText(Html.fromHtml("<font color='#000000'> <b>" + comment.getUser_name() + "</b> " + comment.getPA_comment()));
*/
        //Long Press
        /*viewHolder.mainRl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                onItemLongClickListener.onItemClick(commentDataModels, i, v);
                return false;
            }
        });*/
    }

    @Override
    public int getItemCount() {
        return commentDataModels.size();
    }

    public Story_DataModel getItem(int position) {
        return commentDataModels.get(position);
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImage,delete;
        TextView userName, comment;
        FrameLayout mainRl;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage = itemView.findViewById(R.id.profileImage);
            userName = itemView.findViewById(R.id.tv_username);
            comment = itemView.findViewById(R.id.tv_fullname);
            mainRl = itemView.findViewById(R.id.main_RL_1);
            delete = itemView.findViewById(R.id.delete);
        }
    }

}
