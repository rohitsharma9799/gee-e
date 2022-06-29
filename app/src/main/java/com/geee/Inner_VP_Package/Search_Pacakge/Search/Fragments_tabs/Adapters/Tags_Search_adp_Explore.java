package com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Search_DataModel.Account_search_DM;
import com.geee.R;
import com.geee.View_Tag.View_Tag_Detail;

import java.util.List;

public class Tags_Search_adp_Explore extends RecyclerView.Adapter<Tags_Search_adp_Explore.ViewHolder> {

    Context context;
    List<Account_search_DM> accounts;

    public Tags_Search_adp_Explore(Context ctx, List<Account_search_DM> accounts) {
        this.context = ctx;
        this.accounts = accounts;
    }

    @NonNull
    @Override
    public Tags_Search_adp_Explore.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.tag_search_item_adapter, null);
        return new Tags_Search_adp_Explore.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Tags_Search_adp_Explore.ViewHolder viewHolder, int i) {
        final Account_search_DM resultAccount = accounts.get(i);

        String hashText = resultAccount.getWebsite() + " ";

        int hashIndex = hashText.indexOf("#");

        int spaceIndex = hashText.indexOf(" ", hashIndex);


        final String final_hash_Tag = hashText.substring(hashIndex, spaceIndex);
        try {

            viewHolder.tvTag.setText("" + final_hash_Tag);

        } catch (Exception v) {
            v.printStackTrace();
        }

        viewHolder.rlmain.setOnClickListener(v -> {
            Intent myIntent = new Intent(context, View_Tag_Detail.class);
            myIntent.putExtra("search", final_hash_Tag.replace("#", "")); //Optional parameters
            context.startActivity(myIntent);

        });


    }

    @Override
    public int getItemCount() {
        return accounts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTag;
        LinearLayout rlmain;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTag = itemView.findViewById(R.id.tv_tag);
            rlmain = itemView.findViewById(R.id.RL_Main);
        }
    }


}
