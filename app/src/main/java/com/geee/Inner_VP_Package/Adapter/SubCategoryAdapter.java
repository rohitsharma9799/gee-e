package com.geee.Inner_VP_Package.Adapter;

import static com.geee.CodeClasses.Variables.mResultCallback;
import static com.geee.CodeClasses.Variables.mVolleyService;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.Constants;
import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.Menu.Category;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.MyViewHolder> {

        private List<Category> moviesList;
        public SharedPreferences.Editor editor;
        SharedPreferences sharedpreferences;
        private String PREFS_NAME = "auth_info";
        Context context;


    public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title,followed,cost;
            ImageView imgview;

            public MyViewHolder(View view) {
                super(view);
                imgview = (ImageView) view.findViewById(R.id.imgcategory);
                title = (TextView) view.findViewById(R.id.catname);
                followed = (TextView) view.findViewById(R.id.followed);
            }
        }


        public SubCategoryAdapter(Context context, List<Category> moviesList) {
            this.context=context;
            this.moviesList = moviesList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.categoryadpter, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position) {
            final Category movie = moviesList.get(position);
            Picasso.get().load(Constants.BASE_URL+moviesList.get(position).getBannerSrc())
                    .placeholder(R.drawable.ic_profile_gray)
                    .into(holder.imgview);
         //   Uri uri = Uri.parse(Constants.BASE_URL+moviesList.get(position).getBannerSrc());
           // holder.imgview.setImageURI(uri);

            Log.i("Fdfdfdf",context.toString());
            holder.title.setText(moviesList.get(position).getName());
            //linearLayout.setBackgroundResource(imageData.get(position));

            holder.followed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Functions.showLoader(context, false, false);
                    mVolleyService = new VolleyService(mResultCallback, context);
                    JSONObject sendObj = null;
                    try {
                        sendObj = new JSONObject();
                        sendObj.put("sender_id", SharedPrefrence.getUserIdFromJson(context));
                        sendObj.put("receiver_id",  movie.getBannerId());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i("fdfbbbbbb",sendObj+API_LINKS.API_Send_Follow_Request);

                    mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Send_Follow_Request, sendObj);

                    mResultCallback = new IResult() {
                        @Override
                        public void notifySuccess(String requestType, JSONObject response) {

                            Log.i("fdfdfd",response.toString());
                            Functions.cancelLoader();
                            try {
                                if (response.getString("code").equals("200")) {
                                    moviesList.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, moviesList.size());
                                }
                            } catch (Exception b) {
                                b.printStackTrace();
                                Functions.cancelLoader();
                            }
                        }


                        @Override
                        public void notifyError(String requestType, VolleyError error) {
                            error.printStackTrace();
                            Functions.cancelLoader();
                        }
                    };


                }
            });

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent myIntent = new Intent(context, View_user_Profile.class);
                    myIntent.putExtra("tictok",  "0"); //Optional parameters

                    myIntent.putExtra("user_id", movie.getBannerId()); //Optional parameters
                    myIntent.putExtra("full_name",movie.getName()); //Optional parameters
                    myIntent.putExtra("username",  movie.getDummy_cost()); //Optional parameters
                    myIntent.putExtra("image",movie.getBannerSrc()); //Optional parameters
                    context.startActivity(myIntent);
                   // otherUserId = intent.getStringExtra("user_id");
//                                otherUserName = intent.getStringExtra("username");
//                                otherUserImage = intent.getStringExtra("image");
//                                stFullname = intent.getStringExtra("full_name");
//                                stUserBio = intent.getStringExtra("bio");
//                                stwebsite = intent.getStringExtra("website");
//                    Fragment fragment= new View_user_Profile();
//                    FragmentManager manager = ((AppCompatActivity)
//                            context).getSupportFragmentManager();
//                    final Bundle bundle = new Bundle();
//                   // bundle.putString("subcatid",moviesList.get(position).getBannerId());
//                    fragment.setArguments(bundle);
//                    FragmentTransaction transaction = manager.beginTransaction();
//                    transaction.replace(R.id.containerView, fragment);
//                    transaction.commit();
                }
            });
        }

        @Override
        public int getItemCount() {
            return moviesList.size();
        }

    }
