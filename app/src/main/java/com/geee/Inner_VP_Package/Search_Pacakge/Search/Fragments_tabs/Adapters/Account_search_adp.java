package com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geee.Constants;
import com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Search_DataModel.Account_search_DM;
import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Account_search_adp extends RecyclerView.Adapter<Account_search_adp.ViewHolder> {

    Context context;
    List<Account_search_DM> accounts;

    public Account_search_adp(Context ctx, List<Account_search_DM> accounts) {
        this.context = ctx;
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public Account_search_adp.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.account_search_item, null);
        return new Account_search_adp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Account_search_adp.ViewHolder viewHolder, int i) {
        final Account_search_DM resultAccount = accounts.get(i);

        viewHolder.userName.setText("" + resultAccount.getUsername());
        Uri uri = Uri.parse(Constants.BASE_URL + resultAccount.getProfile());
        viewHolder.profilePic.setImageURI(uri);

        Picasso.get()
                .load(Constants.BASE_URL + resultAccount.getProfile())
                .placeholder(R.drawable.ic_profile_gray)
                .error(R.drawable.ic_profile_gray)
                .into(viewHolder.profilePic);


        viewHolder.rlMain.setOnClickListener(v -> {

            Intent myIntent = new Intent(context, View_user_Profile.class);
            myIntent.putExtra("tictok",  "0"); //Optional parameters
            myIntent.putExtra("user_id", resultAccount.getId()); //Optional parameters
            myIntent.putExtra("full_name", resultAccount.getFull_name()); //Optional parameters
            myIntent.putExtra("username", resultAccount.getUsername()); //Optional parameters
            myIntent.putExtra("image", resultAccount.getProfile()); //Optional parameters
            context.startActivity(myIntent);

        });


    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView profilePic;
        TextView userName;
        LinearLayout rlMain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            profilePic = itemView.findViewById(R.id.profile_pic);
            userName = itemView.findViewById(R.id.tv_tag);
            rlMain = itemView.findViewById(R.id.RL_Main);
        }
    }

}
