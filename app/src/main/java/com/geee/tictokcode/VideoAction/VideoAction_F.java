package com.geee.tictokcode.VideoAction;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import com.geee.R;
import com.geee.tictokcode.Home.Home_Get_Set;
import com.geee.tictokcode.SimpleClasses.Fragment_Callback;
import com.geee.tictokcode.SimpleClasses.Functions;
import com.geee.tictokcode.SimpleClasses.Variables;


/**
 * A simple {@link Fragment} subclass.
 */
public class VideoAction_F extends BottomSheetDialogFragment implements View.OnClickListener {

    View view;
    Context context;
    RecyclerView recyclerView;

    Fragment_Callback fragment_callback;

    String video_id,user_id;
    ProgressBar progressBar;
    Home_Get_Set item;

    public VideoAction_F() {
    }

    @SuppressLint("ValidFragment")
    public VideoAction_F(String id, Fragment_Callback fragment_callback) {
        video_id=id;
        this.fragment_callback=fragment_callback;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_video_action, container, false);
        context=getContext();

        Bundle bundle=getArguments();
        if(bundle!=null){
            item=(Home_Get_Set) bundle.getSerializable("data");
            video_id=bundle.getString("video_id");
            user_id=bundle.getString("user_id");
        }

        progressBar=view.findViewById(R.id.progress_bar);

        view.findViewById(R.id.save_video_layout).setOnClickListener(this);
        view.findViewById(R.id.copy_layout).setOnClickListener(this);
        view.findViewById(R.id.delete_layout).setOnClickListener(this);

        if(user_id!=null && user_id.equals(Variables.sharedPreferences.getString(Variables.u_id,"")))
            view.findViewById(R.id.delete_layout).setVisibility(View.VISIBLE);
        else
            view.findViewById(R.id.delete_layout).setVisibility(View.GONE);


        if(Variables.is_secure_info){
            view.findViewById(R.id.share_notice_txt).setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);
            view.findViewById(R.id.copy_layout).setVisibility(View.GONE);
        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    Get_Shared_app();

                }
            }, 1000);
        }


        if(Variables.is_enable_duet && (item.allow_duet!=null && item.allow_duet.equalsIgnoreCase("1"))) {
            view.findViewById(R.id.duet_layout).setVisibility(View.VISIBLE);
            view.findViewById(R.id.duet_layout).setOnClickListener(this);
        }
        else {
            view.findViewById(R.id.duet_layout).setVisibility(View.GONE);
        }

        return view;
    }


    VideoSharingApps_Adapter adapter;
    public void Get_Shared_app(){
        recyclerView = (RecyclerView) view.findViewById(R.id.recylerview);
        final GridLayoutManager layoutManager = new GridLayoutManager(context, 5);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);

        new Thread(new Runnable() {
            @Override
            public void run() {

                try {

                    PackageManager pm=getActivity().getPackageManager();
                    Intent intent = new Intent(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, item.video_url);
                    startActivity(Intent.createChooser(intent, "choose one"));

                    Log.i("XFdfdx",item.video_url);



                  //  List<ResolveInfo> launchables=pm.queryIntentActivities(intent, 0);

                  /*  for (int i=0; i<launchables.size(); i++){

                        if(launchables.get(i).activityInfo.name.contains("SendTextToClipboardActivity")){
                            launchables.remove(i);
                            break;
                        }

                    }

                    Collections.sort(launchables,
                            new ResolveInfo.DisplayNameComparator(pm));

                    adapter=new VideoSharingApps_Adapter(context, launchables, new VideoSharingApps_Adapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(int positon, ResolveInfo item, View view) {
                            Open_App(item);
                        }
                    });


                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            recyclerView.setAdapter(adapter);
                            progressBar.setVisibility(View.GONE);
                        }
                    });*/


                }
                catch (Exception e){

                }
            }
        }).start();



    }


    public void Open_App(ResolveInfo resolveInfo) {
        try {

            ActivityInfo activity = resolveInfo.activityInfo;
            ComponentName name = new ComponentName(activity.applicationInfo.packageName,
                    activity.name);
            Intent i = new Intent(Intent.ACTION_MAIN);

            i.addCategory(Intent.CATEGORY_LAUNCHER);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |
                    Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
            i.setComponent(name);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, item.video_url);
            intent.setComponent(name);
            startActivity(Intent.createChooser(intent, "choose one"));
        }catch (Exception e){

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.save_video_layout:

                if(Functions.Checkstoragepermision(getActivity())) {

                    Bundle bundle = new Bundle();
                    bundle.putString("action", "save");
                    dismiss();

                    if(fragment_callback!=null)
                        fragment_callback.Responce(bundle);
                }

                break;


            case R.id.duet_layout:
                if(!Variables.sharedPreferences.getBoolean(Variables.islogin,false)) {
                    Open_Login();
                }
                else if(Functions.check_permissions(getActivity())) {
                    Bundle duet_bundle = new Bundle();
                    duet_bundle.putString("action", "duet");
                    dismiss();
                    if (fragment_callback != null)
                        fragment_callback.Responce(duet_bundle);

                }

                break;

            case R.id.copy_layout:
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Copied Text", Variables.main_domain+video_id);
                clipboard.setPrimaryClip(clip);

                Toast.makeText(context, "Link Copy in clipboard", Toast.LENGTH_SHORT).show();
                break;

            case R.id.delete_layout:
                if(Variables.is_secure_info){
                    Toast.makeText(context, getString(R.string.delete_function_not_available_in_demo), Toast.LENGTH_SHORT).show();
                }

                else {
                    Bundle bundle = new Bundle();
                    bundle.putString("action", "delete");
                    dismiss();

                    if(fragment_callback!=null)
                        fragment_callback.Responce(bundle);
                }
                break;

        }



    }


    public void Open_Login(){
        /*Intent intent = new Intent(getActivity(), Login_A.class);
        startActivity(intent);
        getActivity().overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);*/
    }


}
