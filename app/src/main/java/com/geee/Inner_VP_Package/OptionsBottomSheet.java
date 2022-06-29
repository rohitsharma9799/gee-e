package com.geee.Inner_VP_Package;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.FragmentTransaction;

import com.android.volley.VolleyError;
import com.geee.Main_VP_Package.Main;
import com.geee.Main_VP_Package.MainActivity;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Inner_VP_Package.Home_Package.Share_Bottom_Sheet.ShareBottomSheet;
import com.geee.Interface.Fragment_CallBack;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;

import org.json.JSONException;
import org.json.JSONObject;

import static com.geee.CodeClasses.Variables.mResultCallback;

public class OptionsBottomSheet extends BottomSheetDialogFragment {

    View view;
    String followStatus;
    LinearLayout myOptions, otherOptions;
    TextView delete, edit, report, tvFollow, tvShare;
    String fromWhere, fromWhichActivity;
    String postId, userId, created, username, attachment, caption, profileImage;
    Fragment_CallBack fragmentCallback;

    public OptionsBottomSheet(String fromWhere, Fragment_CallBack fragmentCallback) {
        this.fromWhere = fromWhere;
        this.fragmentCallback = fragmentCallback;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.bottom_sheet_options_post, container, false);

        postId = getArguments().getString("post_id");
        userId = getArguments().getString("userId");
        created = getArguments().getString("created");
        username = getArguments().getString("username");
        attachment = getArguments().getString("attachment");
        caption = getArguments().getString("caption");
        profileImage = getArguments().getString("profileImage");

        if (getArguments().getString("fromWhichActivity") != null)
            fromWhichActivity = getArguments().getString("fromWhichActivity");

        if (getArguments().getString("button") != null)
            followStatus = getArguments().getString("button");

        myOptions = view.findViewById(R.id.myoption);
        otherOptions = view.findViewById(R.id.otherOptions);
        delete = view.findViewById(R.id.delete);
        edit = view.findViewById(R.id.edit);
        report = view.findViewById(R.id.report);
        tvFollow = view.findViewById(R.id.follow_unfollow);
        tvShare = view.findViewById(R.id.share);


        if (userId.equals(SharedPrefrence.getUserIdFromJson(getActivity()))) {
            myOptions.setVisibility(View.VISIBLE);
            otherOptions.setVisibility(View.GONE);
        } else {
            myOptions.setVisibility(View.GONE);
            otherOptions.setVisibility(View.VISIBLE);
            if (fromWhere.equals("fromHome")) {
                tvFollow.setText("Unfollow");
            } else if (followStatus != null && !followStatus.equals("") && followStatus.equalsIgnoreCase("Following")) {
                tvFollow.setText("Unfollow");
            } else if (followStatus != null && !followStatus.equals("") && followStatus.equalsIgnoreCase("Requested")) {
                tvFollow.setText("Requested");
            } else {
                tvFollow.setText("Follow");
            }
        }


        delete.setOnClickListener(v -> {
            deletePost();
        });

        tvShare.setOnClickListener(v -> {
            Bundle bundleLinearPosts = new Bundle();
            bundleLinearPosts.putString("post_id", "" + postId);
            ShareBottomSheet signUp = new ShareBottomSheet();
            signUp.setArguments(bundleLinearPosts);
            signUp.show(getActivity().getSupportFragmentManager(), signUp.getTag());
            dismiss();
        });

        edit.setOnClickListener(v -> {
            PostEditF editF = new PostEditF();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            Bundle bundle = new Bundle();
            bundle.putString("post_id", postId);
            bundle.putString("userId", userId);
            bundle.putString("attachment", attachment);
            bundle.putString("caption", caption);
            bundle.putString("created", created);
            bundle.putString("username", username);
            bundle.putString("profileImage", profileImage);
            editF.setArguments(bundle);
            transaction.addToBackStack(null);
            if (fromWhichActivity != null && fromWhichActivity.equals("profile"))
                transaction.replace(R.id.prof_fl_id, editF).commit();
            else
                transaction.replace(R.id.main_container, editF).commit();

            dismiss();
        });

        report.setOnClickListener(v -> {
            methodReportPost();

        });

        tvFollow.setOnClickListener(v -> {
            if (tvFollow.getText().toString().equalsIgnoreCase("follow")) {
                followRequest(SharedPrefrence.getUserIdFromJson(getActivity()), userId);
            } else if (tvFollow.getText().toString().equalsIgnoreCase("unfollow")) {
                methodCallUnFollow(SharedPrefrence.getUserIdFromJson(getActivity()), userId);
            }
        });


        return view;
    }


    private void methodCallUnFollow(String userSenderId, String userReceiverId) {
        initVolleyCallbackForUnFollow();
        Variables.mVolleyService = new VolleyService(mResultCallback, getActivity());
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("sender_id", userSenderId);
            sendObj.put("receiver_id", userReceiverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.logDMsg("params at methodCallUnFollow : " + sendObj.toString());

        Functions.showLoader(getActivity(), false, false);
        Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Send_unFollow, sendObj);
    }


    // Initialize Interface Call Backs
    void initVolleyCallbackForUnFollow() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                Functions.logDMsg("params at methodCallUnFollow : " + response.toString());
                try {
                    if (response.getString("code").equals("200")) {
                        tvFollow.setText("Follow");
                        Bundle bundle = new Bundle();
                        bundle.putString("option", "done");
                        fragmentCallback.onItemClick(bundle);
                        dismiss();
                    }
                } catch (Exception b) {
                    b.printStackTrace();
                    Functions.cancelLoader();
                }
            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.cancelLoader();
            }
        };
    }


    // Follow request
    public void followRequest(String userSenderId, String userReceiverId) {
        initVolleyCallbackForFollow();
        Variables.mVolleyService = new VolleyService(mResultCallback, getActivity());
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("sender_id", userSenderId);
            sendObj.put("receiver_id", userReceiverId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Functions.logDMsg("params at follow bottom : " + sendObj);
        Functions.showLoader(getActivity(), false, false);
        Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Send_Follow_Request, sendObj);
    }

    // Initialize Interface Call Backs
    void initVolleyCallbackForFollow() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Functions.logDMsg("params at follow bottom : " + response);

                Functions.cancelLoader();
                try {
                    if (response.getString("code").equals("200")) {
                        tvFollow.setText("Requested");
                        Bundle bundle = new Bundle();
                        bundle.putString("option", "done");
                        fragmentCallback.onItemClick(bundle);
                        dismiss();
                    }
                } catch (Exception b) {
                    b.printStackTrace();
                    Functions.cancelLoader();
                }
            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.cancelLoader();
            }
        };
    }


    private void methodReportPost() {
        initVolleyCallbackReport();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("post_id", postId);
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(getActivity()));
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_User_reportPost, sendObj);
            Functions.showLoader(getActivity(), false, false);
            Functions.logDMsg("params at report Post : " + sendObj);
        } catch (Exception v) {
            v.printStackTrace();
            Functions.toastMsg(getContext(), "Linear " + v.toString());
        }
    }

    void initVolleyCallbackReport() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                Functions.logDMsg("response at Post action : " + response.toString());
                try {
                    if (response.getString("code").equals("200")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("option", "done");
                        fragmentCallback.onItemClick(bundle);
                        Toast.makeText(getActivity(), "Report Submit Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getActivity(), "" + response.getString("msg"), Toast.LENGTH_SHORT).show();
                    }

                } catch (Exception v) {
                    Functions.cancelLoader();
                }

                dismiss();
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.cancelLoader();
                dismiss();
            }
        };
    }


    public void deletePost() {
        Functions.customAlertDialog(getActivity(), resp -> {
            if (resp.equals("okay")) {
                initVolleyCallbackDelete();
                Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getContext());
                try {
                    JSONObject sendObj = new JSONObject();
                    sendObj.put("id", postId);
                    Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_User_deletePost, sendObj);
                    Functions.showLoader(getActivity(), false, false);
                    Functions.logDMsg("params at delete Post : " + postId);
                } catch (Exception v) {
                    v.printStackTrace();
                    Functions.toastMsg(getContext(), "Linear " + v.toString());
                }
            }
        });

    }

    void initVolleyCallbackDelete() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                Functions.logDMsg("response at Post action : " + response.toString());
                try {
                    if (response.getString("code").equals("200")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("option", "done");
                        fragmentCallback.onItemClick(bundle);
                        Intent start = new Intent(getContext(), MainActivity.class);
                        startActivity(start);
                        dismiss();
                    }
                } catch (Exception v) {
                    Functions.cancelLoader();
                }

            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                Functions.cancelLoader();
            }
        };
    }


}
