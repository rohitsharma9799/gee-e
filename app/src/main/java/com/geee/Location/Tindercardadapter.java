//package com.geee.Location;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Adapter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.bumptech.glide.Glide;
//import com.geee.R;
//import com.mindorks.placeholderview.SwipePlaceHolderView;
//import com.mindorks.placeholderview.annotations.swipe.SwipeCancelState;
//import com.mindorks.placeholderview.annotations.swipe.SwipeIn;
//import com.mindorks.placeholderview.annotations.swipe.SwipeInState;
//import com.mindorks.placeholderview.annotations.swipe.SwipeOut;
//import com.mindorks.placeholderview.annotations.swipe.SwipeOutState;
//import com.squareup.picasso.Picasso;
//import java.util.List;
//
//
//public class Tindercardadapter extends RecyclerView.Adapter<Tindercardadapter.MyViewHolder> implements Adapter {
//
//        private List<Profile> moviesList;
//        public SharedPreferences.Editor editor;
//        SharedPreferences sharedpreferences;
//        private String PREFS_NAME = "auth_info";
//        Context context;
//
//
//    public class MyViewHolder extends RecyclerView.ViewHolder {
//            public TextView  nameAgeTxt,locationNameTxt,cost;
//            ImageView profileImageView;
//
//            public MyViewHolder(View view) {
//                super(view);
//                profileImageView = (ImageView) view.findViewById(R.id.profileImageView);
//                nameAgeTxt = (TextView) view.findViewById(R.id.nameAgeTxt);
//                locationNameTxt = (TextView) view.findViewById(R.id.locationNameTxt);}
//            }
//
//    private Profile mProfile;
//    private Context mContext;
//    private SwipePlaceHolderView mSwipeView;
//
//    public Tindercardadapter(Context context, Profile profile, SwipePlaceHolderView swipeView) {
//        mContext = context;
//        mProfile = profile;
//        mSwipeView = swipeView;
//    }
//
//        @Override
//        public Tindercardadapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View itemView = LayoutInflater.from(parent.getContext())
//                    .inflate(R.layout.tinder_card_view, parent, false);
//            return new Tindercardadapter.MyViewHolder(itemView);
//        }
//
//        @Override
//        public void onBindViewHolder(Tindercardadapter.MyViewHolder holder, final int position) {
//            final Profile movie = moviesList.get(position);
//            Glide.with(mContext).load(mProfile.getImageUrl()).into(holder.profileImageView);
//            holder.nameAgeTxt.setText(mProfile.getName() + ", " + mProfile.getAge());
//            holder.locationNameTxt.setText(mProfile.getLocation());
//            /*holder.decription.setText(moviesList.get(position).getdescription());
//            holder.cost.setText(moviesList.get(position).getcost()+"/-Rs");*/
//            //linearLayout.setBackgroundResource(imageData.get(position));
//
//
//
//          /*  holder.imgview.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent intent = new Intent(context, Answer_Details.class);
//                    intent.putExtra("title",moviesList.get(position).getname());
//                    intent.putExtra("tone",moviesList.get(position).gettone());
//                    intent.putExtra("image",moviesList.get(position).getBannerSrc());
//                    intent.putExtra("desc",moviesList.get(position).getdescription());
//                    intent.putExtra("answer",moviesList.get(position).getanswer());
//                    intent.putExtra("cost",moviesList.get(position).getcost());
//                    intent.putExtra("scheduled",moviesList.get(position).getscheduled());
//                    intent.putExtra("scheduled_answer",moviesList.get(position).getscheduled_answer());
//                    intent.putExtra("user_answer",moviesList.get(position).getuser_answer());
//                    intent.putExtra("user_ans_verified",moviesList.get(position).getuser_ans_verified());
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    context.startActivity(intent);
//                }*/
////            });
//        }
//
//        @Override
//        public int getItemCount() {
//            return moviesList.size();
//        }
//    @SwipeOut
//    private void onSwipedOut(){
//        Log.d("EVENT", "onSwipedOut");
//        mSwipeView.addView(this);
//    }
//
//    @SwipeCancelState
//    private void onSwipeCancelState(){
//        Log.d("EVENT", "onSwipeCancelState");
//    }
//
//    @SwipeIn
//    private void onSwipeIn(){
//        Log.d("EVENT", "onSwipedIn");
//    }
//
//    @SwipeInState
//    private void onSwipeInState(){
//        Log.d("EVENT", "onSwipeInState");
//    }
//
//    @SwipeOutState
//    private void onSwipeOutState(){
//        Log.d("EVENT", "onSwipeOutState");
//    }
//
//}
