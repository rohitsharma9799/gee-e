package com.geee.tictokcode.SoundLists;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.MediaItem;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.util.Log;
import com.google.android.exoplayer2.util.Util;

import java.io.File;
import java.io.IOException;

import com.geee.R;
import com.geee.tictokcode.Home.Home_Get_Set;
import com.geee.tictokcode.SimpleClasses.Functions;
import com.geee.tictokcode.SimpleClasses.Variables;
import com.geee.tictokcode.Video_Recording.Video_Recoder_A;

public class VideoSound_A extends AppCompatActivity implements View.OnClickListener {

    Home_Get_Set item;
    TextView sound_name,description_txt;
    ImageView sound_image;

    File audio_file;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_sound);
        Functions.make_directry(Variables.app_hided_folder);
        Functions.make_directry(Variables.app_showing_folder);
        Functions.make_directry(Variables.draft_app_folder);

        Intent intent=getIntent();
        if(intent.hasExtra("data")){
            item= (Home_Get_Set) intent.getSerializableExtra("data");
        }


        sound_name=findViewById(R.id.sound_name);
        description_txt=findViewById(R.id.description_txt);
        sound_image=findViewById(R.id.sound_image);

        if((item.sound_name==null || item.sound_name.equals("") || item.sound_name.equals("null"))){
           sound_name.setText("original sound - "+item.first_name+" "+item.last_name);
        }else {
           sound_name.setText(item.sound_name);
        }
        description_txt.setText(item.video_description);


        findViewById(R.id.back_btn).setOnClickListener(this);

        findViewById(R.id.save_btn).setOnClickListener(this);
        findViewById(R.id.create_btn).setOnClickListener(this);

        findViewById(R.id.play_btn).setOnClickListener(this);
        findViewById(R.id.pause_btn).setOnClickListener(this);


        Uri uri = Uri.parse(item.thum);
        sound_image.setImageURI(uri);

        Log.d("fdfdfdf",item.thum);
        Log.d(Variables.tag,item.sound_url_acc);

        Save_Audio();

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.back_btn:
                onBackPressed();
                break;
            case R.id.save_btn:
                if(audio_file!=null && audio_file.exists()) {
                try {
                    Functions.copyFile(audio_file,
                            new File(Variables.app_showing_folder +item.video_id+".acc"));
                    Toast.makeText(this, "Audio Saved", Toast.LENGTH_SHORT).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                }else {
                    Toast.makeText(this, "This video does not contain Geee original sound Please select Geee original sound video", Toast.LENGTH_SHORT).show();

                }

                break;

            case R.id.create_btn:
                if(audio_file!=null && audio_file.exists()) {
                    Toast.makeText(this, "toast1", Toast.LENGTH_SHORT).show();
                    StopPlaying();
                    Intent intent = new Intent(VideoSound_A.this, Video_Recoder_A.class);
                    intent.putExtra("sound_name",sound_name.getText().toString());
                    intent.putExtra("sound_id",item.sound_id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.in_from_bottom, R.anim.out_to_top);
                }
                else {
                    Toast.makeText(this, "This video does not contain Geee original sound Please select Geee original sound video", Toast.LENGTH_SHORT).show();

                }                break;

            case R.id.play_btn:
                if(audio_file!=null && audio_file.exists()){
                    playaudio();

                }
                else {
                    Toast.makeText(this, "This video does not contain Geee original sound Please select Geee original sound video", Toast.LENGTH_SHORT).show();

                }
                break;

            case R.id.pause_btn:
                StopPlaying();
                break;
        }
    }



    SimpleExoPlayer player;
    public void playaudio(){

        Uri uri = Uri.parse(String.valueOf(audio_file));
      /*  // Create a data source factory.
        DataSource.Factory dataSourceFactory = new DefaultHttpDataSource.Factory();
// Create a progressive media source pointing to a stream uri.
        MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                .createMediaSource(MediaItem.fromUri(uri));
// Create a player instance.
        ExoPlayer player = new ExoPlayer.Builder(this).build();
// Set the media source to be played.
        player.setMediaSource(mediaSource);
// Prepare the player.
        player.prepare();*/
            DefaultTrackSelector trackSelector = new DefaultTrackSelector();
            player = ExoPlayerFactory.newSimpleInstance(this, trackSelector);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this,
                    Util.getUserAgent(this, "Geee"));

            MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.fromFile(audio_file));


            player.prepare(videoSource);
            player.setPlayWhenReady(true);

          Show_playing_state();
        }


    public void StopPlaying(){
        if(player!=null){
            player.setPlayWhenReady(false);
        }
         Show_pause_state();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        StopPlaying();
    }

    @Override
    protected void onStop() {
        super.onStop();
        StopPlaying();
        Log.d(Variables.tag,"onStop");

    }



    public void Show_playing_state(){
        findViewById(R.id.play_btn).setVisibility(View.GONE);
        findViewById(R.id.pause_btn).setVisibility(View.VISIBLE);
    }

    public void Show_pause_state(){
        findViewById(R.id.play_btn).setVisibility(View.VISIBLE);
        findViewById(R.id.pause_btn).setVisibility(View.GONE);
    }

    DownloadRequest prDownloader;
    ProgressDialog progressDialog;
    public void Save_Audio(){
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();

        prDownloader= PRDownloader.download(item.sound_url_acc, Variables.app_hided_folder, Variables.SelectedAudio_AAC)
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {

                    }
                });

        prDownloader.start(new OnDownloadListener() {
            @Override
            public void onDownloadComplete() {
                progressDialog.dismiss();
                audio_file=new File( Variables.app_hided_folder + Variables.SelectedAudio_AAC);
                 }

            @Override
            public void onError(Error error) {
                progressDialog.dismiss();
            }
        });


    }


    public void Open_video_recording(){


    }


}
