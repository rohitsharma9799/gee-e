package com.geee.tictokcode.Video_Recording.DraftVideos;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coremedia.iso.boxes.Container;
import com.coremedia.iso.boxes.MovieHeaderBox;
import com.googlecode.mp4parser.FileDataSourceImpl;
import com.googlecode.mp4parser.authoring.Movie;
import com.googlecode.mp4parser.authoring.Track;
import com.googlecode.mp4parser.authoring.builder.DefaultMp4Builder;
import com.googlecode.mp4parser.authoring.container.mp4.MovieCreator;
import com.googlecode.mp4parser.authoring.tracks.CroppedTrack;
import com.googlecode.mp4parser.util.Matrix;
import com.googlecode.mp4parser.util.Path;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.WritableByteChannel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.geee.R;
import com.geee.tictokcode.Home.Home_Get_Set;
import com.geee.tictokcode.Profile.MyVideos_Adapter;
import com.geee.tictokcode.Services.Upload_Service;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.geee.tictokcode.SimpleClasses.Functions;
import com.geee.tictokcode.SimpleClasses.Variables;
import com.geee.tictokcode.Video_Recording.GallerySelectedVideo.GallerySelectedVideo_A;
import com.geee.tictokcode.WatchVideos.WatchVideos_F;

public class DraftVideos_A extends AppCompatActivity {

    ArrayList<DraftVideo_Get_Set> data_list;
    public RecyclerView recyclerView;
    DraftVideos_Adapter adapter;

    ProgressBar pbar;


    //*--********Private video

    public RecyclerView recyclerView1;
    ArrayList<Home_Get_Set> data_list1;
    MyVideos_Adapter adapter1;
    View view;
    Context context;

    RelativeLayout no_data_layout;

    NewVideoBroadCast mReceiver;

    private class NewVideoBroadCast extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Variables.Reload_my_videos_inner=false;
            Call_Api_For_get_Allvideos();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_videos);
        context = DraftVideos_A.this;
        Variables.sharedPreferences = getSharedPreferences(Variables.pref_name, MODE_PRIVATE);
        pbar=findViewById(R.id.pbar);

        data_list = new ArrayList();


        recyclerView=findViewById(R.id.recylerview);
        final GridLayoutManager layoutManager = new GridLayoutManager(this,3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);


        data_list=new ArrayList<>();
        adapter=new DraftVideos_Adapter(this, data_list, new DraftVideos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion, DraftVideo_Get_Set item, View view) {

                if (view.getId() == R.id.cross_btn) {
                    File file_data = new File(item.video_path);
                    if(file_data.exists()){
                        file_data.delete();
                    }
                    data_list.remove(postion);
                    adapter.notifyItemRemoved(postion);
                    adapter.notifyItemChanged(postion);

                }
                else {

                    MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                    Bitmap bmp = null;
                    try {
                        retriever.setDataSource(item.video_path);
                        bmp = retriever.getFrameAtTime();
                        int videoHeight = bmp.getHeight();
                        int videoWidth = bmp.getWidth();

                        Log.d("resp", "" + videoWidth + "---" + videoHeight);

                    } catch (Exception e) {

                    }

                    if (item.video_duration_ms <= Variables.max_recording_duration) {

                        if (!Functions.isMyServiceRunning(DraftVideos_A.this,new Upload_Service().getClass())) {

                            Chnage_Video_size(item.video_path, Variables.gallery_resize_video);
                        }
                        else {
                            Toast.makeText(DraftVideos_A.this, "Please wait video already in uploading progress", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        try {
                            startTrim(new File(item.video_path), new File(Variables.gallery_trimed_video), 1000, 18000);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });

        recyclerView.setAdapter(adapter);
        getAllVideoPathDraft();



        findViewById(R.id.Goback).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.in_from_top,R.anim.out_from_bottom);

            }
        });




        //--------------------private


    /*    recyclerView1= findViewById(R.id.recylerviewds);
        final GridLayoutManager layoutManager1 = new GridLayoutManager(context,3);
        recyclerView1.setLayoutManager(layoutManager1);
        recyclerView1.setHasFixedSize(true);*/


        data_list1=new ArrayList<>();
        adapter1=new MyVideos_Adapter(context, data_list1, new MyVideos_Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int postion,Home_Get_Set item, View view) {

                OpenWatchVideo(postion);

            }
        });

   /*     recyclerView1.setAdapter(adapter1);*/

        no_data_layout=findViewById(R.id.no_data_layout);


        Call_Api_For_get_Allvideos();


        mReceiver = new NewVideoBroadCast();
        registerReceiver(mReceiver, new IntentFilter("newVideo"));

    }


     public  void  getAllVideoPathDraft() {


         String path = Variables.draft_app_folder;
         File directory = new File(path);
         File[] files = directory.listFiles();
         for (int i = 0; i < files.length; i++)
         {
             File file=files[i];
             DraftVideo_Get_Set item=new DraftVideo_Get_Set();
             item.video_path=file.getAbsolutePath();
             item.video_duration_ms=getfileduration(Uri.parse(file.getAbsolutePath()));

             Log.d("resp",""+item.video_duration_ms);

             if(item.video_duration_ms>5000){
                 item.video_time=change_sec_to_time(item.video_duration_ms);
                 data_list.add(item);
             }
         }

    }



    // get the audio file duration that is store in our directory
    public long getfileduration(Uri uri) {
        try {

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(this, uri);
            String durationStr = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
            final int file_duration = Integer.parseInt(durationStr);

            return file_duration;
            }
        catch (Exception e){

        }
        return 0;
    }


    public String change_sec_to_time(long file_duration){
        long second = (file_duration / 1000) % 60;
        long minute = (file_duration / (1000 * 60)) % 60;

        return String.format("%02d:%02d", minute, second);

    }


    public void Chnage_Video_size(String src_path, String destination_path){

        File source = new File(src_path);
        File destination = new File(destination_path);
        try
        {
            if(source.exists()){

                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(destination);

                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();

                Intent intent=new Intent(DraftVideos_A.this, GallerySelectedVideo_A.class);
                intent.putExtra("video_path",Variables.gallery_resize_video);
                intent.putExtra("draft_file",src_path);
                startActivity(intent);

            }else{
                Toast.makeText(DraftVideos_A.this, "Failed to get video from Draft", Toast.LENGTH_SHORT).show();

            }

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public  void startTrim(final File src, final File dst, final int startMs, final int endMs) throws IOException {

        new AsyncTask<String, Void, String>() {
            @Override
            protected String doInBackground(String... strings) {
                try {

                    FileDataSourceImpl file = new FileDataSourceImpl(src);
                    Movie movie = MovieCreator.build(file);
                    List<Track> tracks = movie.getTracks();
                    movie.setTracks(new LinkedList<Track>());
                    double startTime = startMs / 1000;
                    double endTime = endMs / 1000;
                    boolean timeCorrected = false;

                    for (Track track : tracks) {
                        if (track.getSyncSamples() != null && track.getSyncSamples().length > 0) {
                            if (timeCorrected) {
                                throw new RuntimeException("The startTime has already been corrected by another track with SyncSample. Not Supported.");
                            }
                            startTime = Functions.correctTimeToSyncSample(track, startTime, false);
                            endTime = Functions.correctTimeToSyncSample(track, endTime, true);
                            timeCorrected = true;
                        }
                    }
                    for (Track track : tracks) {
                        long currentSample = 0;
                        double currentTime = 0;
                        long startSample = -1;
                        long endSample = -1;

                        for (int i = 0; i < track.getSampleDurations().length; i++) {
                            if (currentTime <= startTime) {
                                startSample = currentSample;
                            }
                            if (currentTime <= endTime) {
                                endSample = currentSample;
                            } else {
                                break;
                            }
                            currentTime += (double) track.getSampleDurations()[i] / (double) track.getTrackMetaData().getTimescale();
                            currentSample++;
                        }
                        movie.addTrack(new CroppedTrack(track, startSample, endSample));
                    }

                    Container out = new DefaultMp4Builder().build(movie);
                    MovieHeaderBox mvhd = Path.getPath(out, "moov/mvhd");
                    mvhd.setMatrix(Matrix.ROTATE_180);
                    if (!dst.exists()) {
                        dst.createNewFile();
                    }
                    FileOutputStream fos = new FileOutputStream(dst);
                    WritableByteChannel fc = fos.getChannel();
                    try {
                        out.writeContainer(fc);
                    } finally {
                        fc.close();
                        fos.close();
                        file.close();
                    }

                    file.close();
                    return "Ok";
                }catch (IOException e){
                    return "error";
                }

            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                Functions.Show_indeterminent_loader(DraftVideos_A.this,true,true);
            }

            @Override
            protected void onPostExecute(String result) {
                if(result.equals("error")){
                    Toast.makeText(DraftVideos_A.this, "Try Again", Toast.LENGTH_SHORT).show();
                }else {
                    Functions.cancel_indeterminent_loader();
                    Chnage_Video_size(Variables.gallery_trimed_video, Variables.gallery_resize_video);
                }
            }


        }.execute();

    }


    @Override
    protected void onStart() {
        super.onStart();
        DeleteFile();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
       DeleteFile();
    }


    public void DeleteFile(){
        File output = new File(Variables.outputfile);
        File output2 = new File(Variables.outputfile2);
        File gallery_trim_video = new File(Variables.gallery_trimed_video);
        File gallery_resize_video = new File(Variables.gallery_resize_video);

        if(output.exists()){
            output.delete();
        }
        if(output2.exists()){
            output2.delete();
        }


        if(gallery_trim_video.exists()){
            gallery_trim_video.delete();
        }

        if(gallery_resize_video.exists()){
            gallery_resize_video.delete();
        }



    }

//-----------------private video
Boolean isVisibleToUser=false;

    @Override
    public void onResume() {
        super.onResume();
        if((view!=null && isVisibleToUser) &&  !is_api_run){
            Call_Api_For_get_Allvideos();
        }

        else if((view!=null && Variables.Reload_my_videos_inner) &&  !is_api_run){
            Variables.Reload_my_videos_inner=false;
            Call_Api_For_get_Allvideos();
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        DeleteFile();
        if(mReceiver!=null) {
            unregisterReceiver(mReceiver);
            mReceiver = null;
        }
    }



    Boolean is_api_run=false;
    //this will get the all videos data of user and then parse the data
    private void Call_Api_For_get_Allvideos() {
        is_api_run=true;
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("my_fb_id",Variables.sharedPreferences.getString(Variables.u_id,""));
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,""));

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(this, Variables.showMyAllVideos, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                is_api_run=false;
                Parse_data(resp);
            }
        });


    }

    public void Parse_data(String responce){

        data_list1.clear();

        try {
            JSONObject jsonObject=new JSONObject(responce);
            String code=jsonObject.optString("code");
            if(code.equals("200")){
                JSONArray msgArray=jsonObject.getJSONArray("msg");
                JSONObject data=msgArray.getJSONObject(0);
                JSONObject user_info=data.optJSONObject("user_info");



                JSONArray user_videos=data.getJSONArray("pricate_video");

                Log.d(Variables.tag,user_videos.toString());
                if(user_videos.length()>0){

                    no_data_layout.setVisibility(View.GONE);

                    for (int i=0;i<user_videos.length();i++) {
                        JSONObject itemdata = user_videos.optJSONObject(i);

                        Home_Get_Set item=new Home_Get_Set();
                        item.fb_id=user_info.optString("fb_id");

                        item.first_name=user_info.optString("first_name");
                        item.last_name=user_info.optString("last_name");
                        item.profile_pic=user_info.optString("profile_pic");
                        item.verified=user_info.optString("verified");

                        Log.d("resp", item.fb_id+" "+item.first_name);

                        JSONObject count=itemdata.optJSONObject("count");
                        item.like_count=count.optString("like_count");
                        item.video_comment_count=count.optString("video_comment_count");
                        item.views=count.optString("view");

                        JSONObject sound_data=itemdata.optJSONObject("sound");
                        item.sound_id=sound_data.optString("id");
                        item.sound_name=sound_data.optString("sound_name");
                        item.sound_pic=sound_data.optString("thum");
                        if(sound_data!=null) {
                            JSONObject audio_path = sound_data.optJSONObject("audio_path");
                            item.sound_url_mp3 = audio_path.optString("mp3");
                            item.sound_url_acc = audio_path.optString("acc");
                        }



                        item.privacy_type=itemdata.optString("privacy_type");
                        item.allow_comments=itemdata.optString("allow_comments");
                        item.video_id=itemdata.optString("id");
                        item.liked=itemdata.optString("liked");
                        item.gif=itemdata.optString("gif");
                        item.video_url=itemdata.optString("video");
                        item.thum=itemdata.optString("thum");
                        item.created_date=itemdata.optString("created");

                        item.video_description=itemdata.optString("description");


                        data_list1.add(item);
                    }


                }else {
                    no_data_layout.setVisibility(View.VISIBLE);
                }




                adapter1.notifyDataSetChanged();

            }else {
                Toast.makeText(context, ""+jsonObject.optString("msg"), Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }





    private void OpenWatchVideo(int postion) {
        Toast.makeText(context, "p", Toast.LENGTH_SHORT).show();

        Intent intent=new Intent(this, WatchVideos_F.class);
        intent.putExtra("arraylist", data_list1);
        intent.putExtra("position",postion);
        startActivity(intent);

    }

}
