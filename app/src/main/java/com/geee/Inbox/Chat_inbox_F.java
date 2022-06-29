package com.geee.Inbox;


import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.TouchDelegate;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.geee.Inner_VP_Package.Adapter.SubCategoryAdapter;
import com.geee.Inner_VP_Package.View_User_profile.View_user_Profile;
import com.geee.Menu.Category;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.geee.Chat.Chat_Activity;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class Chat_inbox_F extends Fragment {


    View view;
    Context context;

    ArrayList<Inbox_Get_Set> inboxArraylist;
    RecyclerView inboxList;
    Inbox_Adapter adapter;
    ImageView ivId, icSearchToolBar, icCross;

    ProgressBar progress_loader;
    DatabaseReference rootref;

    EditText editSearchNew;

    LinearLayout noDataLayout;
    TextView inboxText;
    // on start we get the all inbox data from database
    ValueEventListener eventListener;
    Query inbox_query;


    public Chat_inbox_F() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_chat_inbox, container, false);
        context = getContext();
        ivId = view.findViewById(R.id.profileImage);
        icCross = view.findViewById(R.id.ic_cross);


        final View parent = (View) ivId.getParent();  // button: the view you want to enlarge hit area
        parent.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                ivId.getHitRect(rect);
                rect.top -= Variables.clickAreaTop100;    // increase top hit area
                rect.left -= Variables.clickAreaLeft100;   // increase left hit area
                rect.bottom += Variables.clickAreaBottom100; // increase bottom hit area
                rect.right += Variables.clickAreaRight200;  // increase right hit area
                parent.setTouchDelegate(new TouchDelegate(rect, ivId));
            }
        });


        icSearchToolBar = view.findViewById(R.id.ic_search_tool_bar);
        final View parent_1 = (View) icSearchToolBar.getParent();  // button: the view you want to enlarge hit area
        parent_1.post(new Runnable() {
            public void run() {
                final Rect rect = new Rect();
                icSearchToolBar.getHitRect(rect);
                rect.top -= Variables.clickAreaTop100;    // increase top hit area
                rect.left -= Variables.clickAreaLeft100;   // increase left hit area
                rect.bottom += Variables.clickAreaBottom100; // increase bottom hit area
                rect.right += Variables.clickAreaRight200;  // increase right hit area
                parent_1.setTouchDelegate(new TouchDelegate(rect, icSearchToolBar));
            }
        });


        inboxText = view.findViewById(R.id.inbox_text);

        editSearchNew = view.findViewById(R.id.edittext_id);

        editSearchNew.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
        });


        inboxText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();


            }
        });

        ImageView backme = view.findViewById(R.id.backme);
        backme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();


            }
        });

        icSearchToolBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivId.setVisibility(View.GONE);
                inboxText.setVisibility(View.GONE);
                icSearchToolBar.setVisibility(View.GONE);

                // Visible
                icCross.setVisibility(View.VISIBLE);
                editSearchNew.setVisibility(View.VISIBLE);
                editSearchNew.requestFocus();
                Functions.showSoftKeyboard(editSearchNew, getContext());


            }
        });

        icCross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivId.setVisibility(View.VISIBLE);
                inboxText.setVisibility(View.VISIBLE);
                icSearchToolBar.setVisibility(View.VISIBLE);

                // Gone
                icCross.setVisibility(View.GONE);
                editSearchNew.setVisibility(View.GONE);
                Functions.hideSoftKeyboard(getActivity());


            }
        });


        Variables.userId = SharedPrefrence.getUserIdFromJson(getContext());
        Variables.userName = SharedPrefrence.getUserNameFromJson(getContext());
        Variables.userPic = SharedPrefrence.getUserImgFromJson(getContext());


        ivId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getFragmentManager().popBackStack();

            }
        });


        rootref = FirebaseDatabase.getInstance().getReference();

        progress_loader = view.findViewById(R.id.progress_loader);
        noDataLayout = view.findViewById(R.id.no_data_layout);

        // intialize the arraylist and recylerview
        inboxArraylist = new ArrayList<>();
        inboxList = (RecyclerView) view.findViewById(R.id.inboxlist);
        LinearLayoutManager layout = new LinearLayoutManager(context);
        inboxList.addItemDecoration(new DividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL));

        layout.setReverseLayout(true);
        layout.setStackFromEnd(true);

        inboxList.setLayoutManager(layout);
        inboxList.setHasFixedSize(false);

        // inntialize the adapter and set the adapter to recylerview
        adapter = new Inbox_Adapter(context, inboxArraylist, new Inbox_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(Inbox_Get_Set item) {
                Functions.hideSoftKeyboard(getActivity());
                chatFragment(item.getRid(), item.getName(), item.getPic());

            }
        });

        inboxList.setAdapter(adapter);


        alluser(view);
        return view;

    }

    RecyclerView recyclerView;
    private List<Category> imageData2 = new ArrayList<>();

    private void alluser(View view) {
        recyclerView = view.findViewById(R.id.recycle_category);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(), 1, GridLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(gridLayoutManager3);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        fetchplan();
    }

    private void fetchplan() {
        JSONObject parameters = new JSONObject();
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.DEPRECATED_GET_OR_POST, com.geee.tictokcode.SimpleClasses.Variables.GEEEALLUSER, parameters,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.d("ghg", response.toString());
                            Log.i("gdfgvgbnn", com.geee.tictokcode.SimpleClasses.Variables.GEEEALLUSER + parameters);
                            JSONObject jsonObject = new JSONObject(String.valueOf(response));
                            imageData2.clear();
                            if (jsonObject.getString("code").equals("200")) {
                                JSONArray accArr = jsonObject.getJSONArray("msg");
                                for (int i = 0; i < accArr.length(); i++) {
                                    JSONObject obj = accArr.getJSONObject(i);
                                    JSONObject userObj = obj.getJSONObject("user");
                                    Category id = new Category();
                                    id.setBannerId(userObj.getString("id"));
                                    id.setBannerSrc(userObj.getString("image"));
                                    id.setName(userObj.getString("full_name"));
                                    id.setDescription(userObj.getString("bio"));
                                    id.setCost(userObj.getString("website"));
                                    id.setDummy_cost(userObj.getString("email"));
                                    imageData2.add(id);
                                }
//                                otherUserId = intent.getStringExtra("user_id");
//                                otherUserName = intent.getStringExtra("username");
//                                otherUserImage = intent.getStringExtra("image");
//                                stFullname = intent.getStringExtra("full_name");
//                                stUserBio = intent.getStringExtra("bio");
//                                stwebsite = intent.getStringExtra("website");
                                SubCategoryAdapterInbox donationAdapter = new SubCategoryAdapterInbox(getActivity(), imageData2, Chat_inbox_F.this);
                                donationAdapter.notifyDataSetChanged();
                                recyclerView.setAdapter(donationAdapter);


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_LONG).show();

                Log.i("Dfsdfsdf", error.toString());
            }
        }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                return params;
            }
        };
        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(200 * 30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        Volley.newRequestQueue(getActivity()).add(jsonObjectRequest);
    }

    void filter(String text) {
        List<Inbox_Get_Set> temp = new ArrayList();

        for (Inbox_Get_Set d : inboxArraylist) {
            if (d.getName().toLowerCase().contains(text.toLowerCase())) {
                temp.add(d);
            }
        }
        //update recyclerview
        adapter.updateList((ArrayList<Inbox_Get_Set>) temp);
    }

    @Override
    public void onStart() {
        super.onStart();

        progress_loader.setVisibility(View.VISIBLE);
        inbox_query = rootref.child("Inbox").child(Variables.userId).orderByChild("timestamp");
        inbox_query.keepSynced(true);

        eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                inboxArraylist.clear();
                progress_loader.setVisibility(View.GONE);

                if (!dataSnapshot.exists()) {
                    noDataLayout.setVisibility(View.VISIBLE);
                } else {
                    noDataLayout.setVisibility(View.GONE);


                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Inbox_Get_Set model = ds.getValue(Inbox_Get_Set.class);
                        inboxArraylist.add(model);
                        adapter.notifyDataSetChanged();
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };

        inbox_query.addValueEventListener(eventListener);


    }


    @Override
    public void onStop() {
        super.onStop();
        inbox_query.removeEventListener(eventListener);

    }


    public void chatFragment(String receiverid, String name, String receiver_pic) {
        Chat_Activity chat_activity = new Chat_Activity();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("receiver_id", receiverid);
        args.putString("receiver_name", name);
        args.putString("receiver_pic", receiver_pic);
        chat_activity.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Inbox_F, chat_activity).commit();
    }

    public void onclick(String bannerId, String name, String bannerSrc) {
        Chat_Activity chat_activity = new Chat_Activity();
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        Bundle args = new Bundle();
        args.putString("receiver_id", bannerId);
        args.putString("receiver_name", name);
        args.putString("receiver_pic", bannerSrc);
        chat_activity.setArguments(args);
        transaction.addToBackStack(null);
        transaction.replace(R.id.Inbox_F, chat_activity).commit();
    }
}
