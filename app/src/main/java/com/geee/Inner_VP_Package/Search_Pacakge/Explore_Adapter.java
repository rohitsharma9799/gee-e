package com.geee.Inner_VP_Package.Search_Pacakge;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.DataModel.Grid_Data_Model;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.ButterKnife;

public class Explore_Adapter extends RecyclerView.Adapter<Explore_Adapter.ViewHolder> {

    List<Grid_Data_Model> postsList;
    private Context mContext;
    private static final int TYPE_SMALL_SQUARE = 0;
    private static final int TYPE_LARGE_SQUARE = 1;
    private static final int TYPE_LREACT = 2;
    private static final int TYPE_HREACT = 3;
    public Explore_Adapter(Context context, List<Grid_Data_Model> postsList) {
        this.mContext = context;
        this.postsList = postsList;
    }
    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_LARGE_SQUARE;
            case 1:
                return TYPE_HREACT;
            case 2:
                return TYPE_LREACT;
            case 3:
                return TYPE_LREACT;
            case 4:
                return TYPE_SMALL_SQUARE;
        }
        return super.getItemViewType(position);
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item, null);

      /*  ButterKnife.bind(this, view);
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                final int type = viewType;
                final ViewGroup.LayoutParams lp = view.getLayoutParams();
                final StaggeredGridLayoutManager lm =
                        (StaggeredGridLayoutManager) ((RecyclerView) parent).getLayoutManager();
                if (lp instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams sglp =
                            (StaggeredGridLayoutManager.LayoutParams) lp;
                    switch (type) {
                        case TYPE_LARGE_SQUARE:
                            sglp.width = view.getWidth() ;
                            sglp.height = view.getHeight();
                            break;
                        case TYPE_HREACT:
                            sglp.width = view.getWidth() ;
                            sglp.height = view.getHeight() ;
                            break;
                        case TYPE_LREACT:
                            sglp.width = view.getWidth() ;
                            sglp.height = view.getHeight() ;
                            break;
                        case TYPE_SMALL_SQUARE:
                            sglp.width = view.getWidth();
                            sglp.height = view.getHeight();
                            break;
                    }
                    view.setLayoutParams(sglp);
                    lm.invalidateSpanAssignments();
                }
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                return true;
            }
        });*/
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {








        final Grid_Data_Model post = postsList.get(position);

        Uri uri = Uri.parse(Constants.BASE_URL + post.getAttachment());
        if (uri != null)
            holder.title.setImageURI(uri);
        Uri uri2 = Uri.parse(Constants.BASE_URL + post.getUser_img());
        if (uri2 != null)
            //holder.imageview_id.setImageURI(uri2);
            Picasso.get().load(uri2).placeholder(R.drawable.ic_profile_gray).into(holder.imageview_id);
        holder.itemView.setOnClickListener(v -> ((MainActivity) mContext).openPostDetail(post.getId(), "" + SharedPrefrence.getUserIdFromJson(mContext)));

    }

    @Override
    public int getItemCount() {
        return postsList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final SimpleDraweeView title;
        ImageView imageview_id;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            imageview_id = itemView.findViewById(R.id.imageview_id);

        }

    }
}