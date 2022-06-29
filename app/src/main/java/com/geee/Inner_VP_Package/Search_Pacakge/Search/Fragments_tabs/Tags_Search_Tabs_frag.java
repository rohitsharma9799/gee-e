package com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Adapters.Tags_Search_adp;
import com.geee.Inner_VP_Package.Search_Pacakge.Search.Fragments_tabs.Search_DataModel.Account_search_DM;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Tags_Search_Tabs_frag extends Fragment {
    View view;

    RecyclerView accountRv;
    Tags_Search_adp accountAdp;
    List<Account_search_DM> searchDmArrayList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.tags_search_tabs_frag, container, false);
        String searchKey = SharedPrefrence.getOffline(getContext(), SharedPrefrence.share_search_key);

        accountRv = view.findViewById(R.id.Accounts_RV_id);

        accountRv.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        accountRv.setHasFixedSize(false);

        searchDmArrayList.clear();

        searchTags(searchKey);

        return view;
    }


    public void searchTags(String searchKey) {
        initVolleyCallback();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(getContext()));
            sendObj.put("keyword", searchKey);
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Search_Tag_Post, sendObj);
        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "" + v.toString());
        }
    }


    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Log.i("gcgcvg",response.toString());
                try {
                    JSONObject  jsonObject = new JSONObject(String.valueOf(response));


                        JSONArray msgArr = response.getJSONArray("msg");
                        // Save Response for Offline Showing Data

                        for (int i = 0; i < msgArr.length(); i++) {
                            JSONObject obj = msgArr.getJSONObject(i);
                            JSONObject userObj = obj.getJSONObject("User");
                            JSONObject postObj = obj.getJSONObject("Post");

                            Account_search_DM account = new Account_search_DM();
                            account.id = postObj.getString("id");
                            account.username = "" + userObj.getString("username");
                            account.full_name = "" + userObj.getString("full_name");
                            account.email = "" + userObj.getString("email");
                            account.website = "" + postObj.getString("caption");
                            account.bio = "" + userObj.getString("bio");
                            account.phone = "" + userObj.getString("phone");
                            account.gender = "" + userObj.getString("gender");
                            account.profile = "" + userObj.getString("image");

                            searchDmArrayList.add(account);
                        }  // End for Loop
                        // Setting up adapter
                        accountAdp = new Tags_Search_adp(getContext(), searchDmArrayList);
                        accountRv.setAdapter(accountAdp);

                    // End setting up adapter
                } catch (Exception b) {
                    b.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {


                Functions.toastMsg(getContext(), "Err " + error.toString());

            }
        };
    }
    // ENd Data From API
}
