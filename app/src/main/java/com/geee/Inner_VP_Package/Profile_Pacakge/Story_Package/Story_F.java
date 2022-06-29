package com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Animatable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.danikula.videocache.HttpProxyCacheServer;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.controller.ControllerListener;
import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.CircleProgressBarDrawable;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.Adapters.StoryAdapter;
import com.geee.Inner_VP_Package.Home_Package.Share_Bottom_Sheet.SharePostUtil;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Utils.MyApplication;
import com.geee.Volley_Package.API_Calling_Methods;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.geee.tictokcode.Home.Home_Get_Set;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.PlaybackPreparer;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.audio.AudioAttributes;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.StyledPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultAllocator;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.upstream.cache.Cache;
import com.google.android.exoplayer2.upstream.cache.CacheDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.videotrimmer.library.utils.CompressOption;
import com.videotrimmer.library.utils.TrimType;
import com.videotrimmer.library.utils.TrimVideo;
import com.volokh.danylo.hashtaghelper.HashTagHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.shts.android.storiesprogressview.StoriesProgressView;


public class Story_F extends Fragment implements StoriesProgressView.StoriesListener {
    public static String uploadingImageId = "none";
    EditText edittext_id;
    ///Story VIew
    public StoriesProgressView storiesProgressView;
    ArrayList<Story_DataModel> userStory = new ArrayList<>();
    long pressTime = 0L;
    long limit = 1500L;
    int position;
    View view;
    List<ShowStoryDM> list = new ArrayList<>();
    boolean isviewCreated = false;
    TextView usernameId, time;
    ImageView profPhotoId;
    boolean isVisible = false;
    SimpleDraweeView story_iv;
    GenericDraweeHierarchy hierarchy;
    StyledPlayerView exoPlayerView;
    // creating a variable for exoplayer
    SimpleExoPlayer exoPlayer;
    VideoView player;
    boolean is_user_stop_video=false;
    private int counterImage = 0, counterStory = 0;
    private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            int action = motionEvent.getAction();
            if (action == 0) {
                pressTime = System.currentTimeMillis();
                storiesProgressView.pause();
                return false;
            } else if (action != 1) {
                return false;
            } else {
                long currentTimeMillis = System.currentTimeMillis();
                storiesProgressView.resume();
                if (limit < currentTimeMillis - pressTime) {
                    return true;
                }
                return false;
            }
        }
    };

    public Story_F(List<ShowStoryDM> list, int position) {
        this.list = list;
        this.position = position;

    }
    RecyclerView psotRecycleview, storiesRecyclerview;

    DefaultHttpDataSourceFactory dataSourceFactory;
    ExtractorsFactory extractorsFactory;
    List<ShowStoryDM> storyDmList = new ArrayList<>();
    StorybaeAdapter storyAdapter;
    private StorageReference mStorageRef;
    private String imageFilePath;
    private static final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.fragment_story, container, false);
        getActivity().getWindow().addFlags (WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        isviewCreated = true;
        View reverse = view.findViewById(R.id.reverse);
        exoPlayerView= view.findViewById(R.id.playerview);
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelector trackSelector = new DefaultTrackSelector(new AdaptiveTrackSelection.Factory( ));
        exoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), trackSelector);
        dataSourceFactory = new DefaultHttpDataSourceFactory("exoplayer_video");
        extractorsFactory = new DefaultExtractorsFactory();
        exoPlayerView.setPlayer(exoPlayer);
        exoPlayer.setPlayWhenReady(true);
        reverse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                storiesProgressView.reverse();
            }
        });
        reverse.setOnTouchListener(onTouchListener);

        profPhotoId = view.findViewById(R.id.prof_photo_id);
        story_iv = view.findViewById(R.id.story_iv);
        usernameId = view.findViewById(R.id.username_id);
        time = view.findViewById(R.id.time);
        View skip = view.findViewById(R.id.skip);

        storiesProgressView = view.findViewById(R.id.stories);


        profPhotoId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                story_iv.setDrawingCacheEnabled(true);
                Uri imageUri= null;
                try {
                    imageUri = Uri.parse(MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), String.valueOf(story_iv), "title", "discription"));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
                shareIntent.setType("image/*");
                startActivity(Intent.createChooser(shareIntent,"send"));
            }
        });

        skip.setOnClickListener(v -> {
            storiesProgressView.skip();
        });

        String profileUrl = list.get(position).getUserImg();
        String name = list.get(position).getUserName();

        if (profileUrl != null && !profileUrl.equals("")) {
           // profPhotoId.setImageURI(Uri.parse(Constants.BASE_URL + profileUrl));
            Picasso.get().load(Constants.BASE_URL + profileUrl)
                    .placeholder(R.drawable.profile_image_placeholder)
                    .into(profPhotoId);
        }

        hierarchy = GenericDraweeHierarchyBuilder.newInstance(getActivity().getResources())
                .setProgressBarImage(new CircleProgressBarDrawable())
                .build();

        usernameId.setText(name);



        edittext_id = view.findViewById(R.id.edittext_id);
        TextView ic_search = view.findViewById(R.id.ic_search);
        ic_search.setOnClickListener(v -> {

            if (edittext_id.getText().toString().equals("")){
                Toast.makeText(getActivity(), "Message cant be empty", Toast.LENGTH_SHORT).show();
            }else {
                uploadImage("Comment on your story"+edittext_id.getText().toString(),list.get(position).getUserId(),list.get(position).getUserName(),list.get(position).getUserImg());
                Toast.makeText(getActivity(), "comment sent", Toast.LENGTH_SHORT).show();
            }
              /*  SharePostUtil Share_Util = new SharePostUtil();
                Share_Util.sendMessage(
                        SharedPrefrence.getUserNameFromJson(getActivity()),
                        ""
                                + postId, SharedPrefrence.getUserIdFromJson(context),
                        "" + follwer.getUser_id_follower(),
                        "" + follwer.getUser_name_follwer(),
                        "" + Constants.BASE_URL + follwer.getUser_image_follwer()

                );*/


        });
        storiesRecyclerview = view.findViewById(R.id.stories_recyclerview);

        setUpStoriesAdapter();
        getStories();
        return view;
    }
    public void setUpStoriesAdapter() {
        storiesRecyclerview = view.findViewById(R.id.stories_recyclerview);

        storyAdapter = new StorybaeAdapter(getContext(), storyDmList, new StorybaeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(List<ShowStoryDM> dmArrayList, int postion, View view) {
                ShowStoryDM storyDm = dmArrayList.get(postion);
                switch (view.getId()) {
                    case R.id.rv_id:
                        if (storyDm.getType().equals("null")) {
                            selectImage();
                        } else {
                            Intent myIntent = new Intent(getActivity(), ViewStory.class);
                            myIntent.putExtra("user_id", storyDm.getUserId()); //Optional parameters
                            myIntent.putExtra("storyList", (Serializable) storyDmList); //Optional parameters
                            myIntent.putExtra("position", String.valueOf(postion)); //Optional parameters
                            getActivity().startActivity(myIntent);
                        }

                        break;
                    default:
                        break;
                }
            }
        }, new StorybaeAdapter.onItemLongClickListener() {
            @Override
            public void onItemClick(List<ShowStoryDM> dmArrayList, int postion, View view) {
                ShowStoryDM storyDm = dmArrayList.get(postion);
                switch (view.getId()) {
                    case R.id.rv_id:
                        if (storyDm.getUserId().equals(SharedPrefrence.getUserIdFromJson(getApplicationContext()))) {
                            selectImage();
                        }
                        break;

                    default:
                        break;
                }
            }
        });


        storiesRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        storiesRecyclerview.setHasFixedSize(false);
        storiesRecyclerview.setAdapter(storyAdapter);
        //   getStories();


    }


    private void selectImage() {
        try {
            PackageManager pm = getContext().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getContext().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Pick from Camera", "Pick from Gallery","Pick from Videos","Delete status","Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getContext());
                builder.setTitle("Add your Story");
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Pick from Camera")) {
                        dialog.dismiss();
                        openCameraIntent();
                    } else if (options[item].equals("Pick from Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    }else if (options[item].equals("Pick from Videos")) {
                        dialog.dismiss();
                        Intent intent = new Intent(
                                Intent.ACTION_PICK,
                                android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        intent.setType("video/*");
                        startActivityForResult(intent, com.geee.tictokcode.SimpleClasses.Variables.Pick_video_from_gallery);
                    }  else if (options[item].equals("Delete status")) {
                        DeleteFragment comment_f = new DeleteFragment();
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
                        transaction.addToBackStack("commentfrag");
                        transaction.replace(R.id.mainrsd, comment_f).commit();

                    }
                    else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else
                Toast.makeText(getContext(), "Storage and Camera Permission required", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getContext(), "Storage and Camera Permission required", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            //Create a file to store the image
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, PICK_IMAGE_CAMERA);
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,  // prefix /
                ".jpg",         // suffix /
                storageDir      // directory /
        );
        imageFilePath = image.getAbsolutePath();
        return image;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_GALLERY) {
            if (data != null) {
                Uri selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .setAspectRatio(1, 1)
                        .start(getActivity());
            }
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                Functions.logDMsg("resultUri : " + resultUri);
                uploadImageFirebase(resultUri, "Name");
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Functions.logDMsg("resultUri : " + error.toString());
            }
        } else if (requestCode == PICK_IMAGE_CAMERA) {
            Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
            CropImage.activity(selectedImage).start(getActivity());
        }else if (requestCode == com.geee.tictokcode.SimpleClasses.Variables.Pick_video_from_gallery) {
            Uri uri = data.getData();
            try {

                TrimVideo.activity(String.valueOf(uri))
                        .setCompressOption(new CompressOption())
                        .setTrimType(TrimType.FIXED_DURATION)
                        .setFixedDuration(18l)
//         //empty constructor for default compress option
                        .setHideSeekBar(true)
                        .setDestination(com.geee.tictokcode.SimpleClasses.Variables.app_hided_folder)  //default output path /storage/emulated/0/DOWNLOADS
                        .start(this);

              /*  Intent intent=new Intent(getActivity(), ActVideoTrimmer.class);
                intent.putExtra(TrimmerConstants.TRIM_VIDEO_URI, String.valueOf(uri));
                intent.putExtra(TrimmerConstants.DESTINATION, com.geee.tictokcode.SimpleClasses.Variables.app_hided_folder);
                intent.putExtra(TrimmerConstants.TRIM_TYPE,1);
                intent.putExtra(TrimmerConstants.FIXED_GAP_DURATION,18L); //in secs
                startActivityForResult(intent,TrimmerConstants.REQ_CODE_VIDEO_TRIMMER);*/

                    /*
                    File video_file = FileUtils.getFileFromUri(this, uri);

                    if (Functions.getfileduration(this,uri) < Variables.max_recording_duration) {
                        Chnage_Video_size(video_file.getAbsolutePath(), Variables.gallery_resize_video);

                    } else {
                        Intent intent=new Intent(Video_Recoder_A.this,Trim_video_A.class);
                        intent.putExtra("path",video_file.getAbsolutePath());
                        startActivity(intent);
                    }
                    */

            } catch (Exception e) {
                e.printStackTrace();
            }


        }/*else if(requestCode ==TrimmerConstants.REQ_CODE_VIDEO_TRIMMER){
            String path= data.getStringExtra(TrimmerConstants.TRIMMED_VIDEO_PATH);
            Chnage_Video_size(path, com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
        }*/else if (requestCode == TrimVideo.VIDEO_TRIMMER_REQ_CODE && data != null) {
            Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(data));
            Log.d("fdsfs", "Trimmed path:: " + uri);
            String path = TrimVideo.getTrimmedVideoPath(data);
            Chnage_Video_size(path, com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
        }
    }

    ActivityResultLauncher<Intent> startForResult = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK &&
                        result.getData() != null) {
                    Uri uri = Uri.parse(TrimVideo.getTrimmedVideoPath(result.getData()));
                    String path = TrimVideo.getTrimmedVideoPath(result.getData());
                    Chnage_Video_size(path, com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
                    Toast.makeText(getActivity(), "aj", Toast.LENGTH_SHORT).show();
                }
                //  LogMessage.v("videoTrimResultLauncher data is null");
            });

    public void uploadImageFirebase(Uri uri, String filename) {
        //Toast.makeText(getActivity(), "aj", Toast.LENGTH_SHORT).show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        StorageReference storageReference = mStorageRef.child("images/Story_" + timeStamp + ".jpg");
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 ->
                    callingStoryAPI(uri1.toString()));
            //  Toast.makeText(getActivity(), "dff", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e ->
                Functions.toastMsg(getContext(), "Failed " + e.toString())

        );
    }

    public void callingStoryAPI(String downloadUrl) {
        // Toast.makeText(getActivity(), "cx", Toast.LENGTH_SHORT).show();
        API_Calling_Methods.addStoryAPI("" + SharedPrefrence.getUserIdFromJson(getContext()),
                "" + downloadUrl, "" + Variables.typeImg, getContext()
        );

    }
    public void Chnage_Video_size(String src_path, String destination_path){

        try {
            com.geee.tictokcode.SimpleClasses.Functions.copyFile(new File(src_path),
                    new File(destination_path));

            File file=new File(src_path);
            if(file.exists())
                file.delete();

            uploadvideoFirebase(Uri.fromFile(new File(com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video)), "name");
            /*Intent intent=new Intent(getActivity(), GallerySelectedVideo_A.class);
            intent.putExtra("video_path", com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
            startActivity(intent);*/

            //    Uri.fromFile(new File(com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video));

        }
        catch (IOException e) {
            e.printStackTrace();
            Log.d(com.geee.tictokcode.SimpleClasses.Variables.tag,e.toString());
        }
    }
    public void uploadvideoFirebase(Uri uri, String filename) {
        Toast.makeText(getActivity(), "viddeo", Toast.LENGTH_SHORT).show();

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());

        StorageReference storageReference = mStorageRef.child("Video/Story_" + timeStamp + ".mp4");
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 ->
                    callingvideoStoryAPI(uri1.toString()));
            Toast.makeText(getActivity(), "videouplaod", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e ->
                Functions.toastMsg(getContext(), "Failed " + e.toString())

        );
    }
    public void callingvideoStoryAPI(String downloadUrl) {
        // Toast.makeText(getActivity(), "cx", Toast.LENGTH_SHORT).show();
        API_Calling_Methods.addStoryAPI("" + SharedPrefrence.getUserIdFromJson(getContext()),
                "" + downloadUrl, "" + Variables.typeVideo, getContext()
        );

    }


    public void getStories() {

        initVolleyCallbackStories();

        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, MyApplication.getAppContext());
        try {

            JSONObject sendObj = new JSONObject();
            // sendObj.put("user_id", "30");
            sendObj.put("user_id", SharedPrefrence.getUserIdFromJson(MyApplication.getAppContext()));
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_SHOW_USER_himself_STORY, sendObj);
            Log.e("dfsfsdd",sendObj.toString());

        } catch (Exception v) {
            v.printStackTrace();
        }


    } // End method to get home upload

    // Initialize Interface Call Backs
    void initVolleyCallbackStories() {
        Log.e("DAtata201",":0:");
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {

                Log.e("DAtata201",":1:");
                Log.e("DAtata201",":2:"+response.toString());

                Functions.logDMsg("initVolleyCallbackStories : " + response.toString());
                try {
                    // JSONObject jsonObject = response.getJSONObject("msg");
                    //JSONArray userStory = jsonObject.getJSONArray("UserStory");
                    JSONObject  jsonObject = new JSONObject(String.valueOf(response));
                    if (jsonObject.getInt("code")==200) {
                        JSONArray userStory = response.getJSONArray("msg");

                        Log.e("datattarohit","::"+userStory.length());

                        List<ShowStoryDM> userStoryList = new ArrayList<>();
                        if (userStory.length() > 0) {
                            for (int i = 0; i < userStory.length(); i++) {
                                if (i == 0) {
                                    JSONObject obj = userStory.getJSONObject(i);
                                    JSONObject storyObj = obj.getJSONObject("Story");
                                    JSONObject userObj = obj.getJSONObject("User");


                                    ShowStoryDM showStoryDM = new ShowStoryDM();
                                    if (storyObj.has("attachment")) {
                                        showStoryDM.attachment = "" + storyObj.getString("attachment");
                                    }

                                    if (storyObj.has("user_id")) {
                                        showStoryDM.userId = "" + storyObj.getString("user_id");
                                    }

                                    if (storyObj.has("type")) {
                                        showStoryDM.type = "" + storyObj.getString("type");
                                    }

                                    if (userObj.has("image")) {
                                        showStoryDM.userImg = "" + userObj.getString("image");
                                    }

                                    if (userObj.has("username")) {
                                        showStoryDM.userName = "" + userObj.getString("username");
                                    }

                                    userStoryList.add(showStoryDM);
                                } else
                                    break;
                            }
                        }



                        List<ShowStoryDM> tempList = new ArrayList<>();
                        JSONArray friendStory = response.getJSONArray("FriendStory");
                        for (int x = 0; x < friendStory.length(); x++) {
                            JSONObject obj = friendStory.getJSONObject(x);
                            JSONObject storyObj = obj.getJSONObject("Story");
                            JSONObject userObj = obj.getJSONObject("User");
                            // if (x == 0)

                            String username = userObj.getString("username");
                            boolean result = false;
                            for(int i = 0; i < tempList.size(); i++){
                                result = tempList.get(i).getUserName().equals(username);
                                if(result){
                                    break; // Early exit if found
                                }
                            }

                            if (result) {
                            } else {


                                ShowStoryDM showStoryDM = new ShowStoryDM();

                                if (storyObj.has("attachment")) {
                                    showStoryDM.attachment = "" + storyObj.getString("attachment");
                                }

                                if (storyObj.has("user_id")) {
                                    showStoryDM.userId = "" + storyObj.getString("user_id");
                                }
                                if (storyObj.has("type")) {
                                    showStoryDM.type = "" + storyObj.getString("type");
                                }
                                if (userObj.has("image")) {
                                    showStoryDM.userImg = "" + userObj.getString("image");
                                }
                                if (userObj.has("username")) {
                                    showStoryDM.userName = "" + userObj.getString("username");
                                }
                                if (userObj.has("cover_image")) {
                                    showStoryDM.coverimaged = "" + userObj.getString("cover_image");
                                }

                                tempList.add(showStoryDM);
                            }
                            //else
                            //   break;
                        }

                        storyDmList.clear();

                        storyDmList.addAll(userStoryList);
                        storyDmList.addAll(tempList);
                        storyAdapter.notifyDataSetChanged();
                    }else {
                        List<ShowStoryDM> userStoryList = new ArrayList<>();
                        ShowStoryDM showStoryDM = new ShowStoryDM();
                        showStoryDM.attachment = "";
                        showStoryDM.userId = "" + SharedPrefrence.getUserIdFromJson(getActivity());
                        showStoryDM.type = "first";
                        showStoryDM.userImg = "" + SharedPrefrence.getUserImageFromJson(getActivity());
                        showStoryDM.userName = "" + SharedPrefrence.getUserNameFromJson(getActivity());
                        userStoryList.add(0, showStoryDM);

                        storyDmList.clear();
                        storyDmList.addAll(userStoryList);
                        // storyDmList.addAll(tempList);

                        Log.i("dffffgfsfsfs",SharedPrefrence.getUserImageFromJson(getActivity()));
                        storyAdapter.notifyDataSetChanged();

                        //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception v) {
                    v.printStackTrace();
                }

            }


            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
                Log.e("DAtata201",":1:"+error.toString());
                Log.e("DAtata201",":1:"+error.getMessage());
            }
        };
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            isVisible = true;
            Handler handler = new Handler(Looper.getMainLooper());
            handler.postDelayed(() -> {
                if (view != null && menuVisible) {
                    ViewStory.mPager.getCurrentItem();
                    getUserStroty();
                }
            }, 100);
        }
    }

    public void getUserStroty() {
        userStory.clear();
        resetComplete();
        initVolleyCallback();
        Variables.mVolleyService = new VolleyService(Variables.mResultCallback, getActivity());
        try {
            JSONObject sendObj = new JSONObject();
            sendObj.put("user_id", list.get(position).getUserId());
            Variables.mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_SHOW_USER_STORY, sendObj);
        } catch (Exception v) {
            v.printStackTrace();
        }

    } // End method to get home upload

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        Variables.mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                try {
                    JSONArray msgArr = response.getJSONArray("msg");

                    for (int i = 0; i < msgArr.length(); i++) {

                        JSONObject obj = msgArr.getJSONObject(i);
                        JSONObject storyObj = obj.getJSONObject("Story");
                        Story_DataModel post = new Story_DataModel();
                        post.image_url = "" + storyObj.getString("attachment");
                        post.user_id = "" + storyObj.getString("user_id");
                        post.created = "" + storyObj.getString("created");
                        post.type = "" + storyObj.getString("type");
                        userStory.add(post);
                    }

                    setupStories();

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

    private void setupStories() {
        counterImage = 0;

        storiesProgressView.setStoriesCount(userStory.size());
        storiesProgressView.setStoryDuration(16000L);
        storiesProgressView.setStoriesListener(this);
        time.setText("" + Functions.getTimeAgoOrg(userStory.get(counterImage).created));


        if (userStory.get(counterImage).getType().equals("image")){

            ControllerListener listener = new BaseControllerListener<ImageInfo>() {
                @Override
                public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                    if (imageInfo.getQualityInfo().isOfGoodEnoughQuality() == true)
                        storiesProgressView.startStories(0);
                    else
                        storiesProgressView.pause();
                }

                @Override
                public void onFailure(String id, Throwable throwable) {
                    storiesProgressView.pause();
                }

            };
            story_iv.setVisibility(View.VISIBLE);
            exoPlayerView.setVisibility(View.GONE);
            DraweeController controller = Fresco.newDraweeControllerBuilder()
                    .setUri(userStory.get(counterImage).getImage_url())
                    .setControllerListener(listener)
                    .build();
            story_iv.setHierarchy(hierarchy);
            hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
            story_iv.setController(controller);
        }else {
            story_iv.setVisibility(View.GONE);
            exoPlayerView.setVisibility(View.VISIBLE);
            Set_Player(userStory.get(counterImage).getImage_url());
        }


    }

    @Override
    public void onNext() {
        counterImage++;
        if (exoPlayer.isPlaying()){
            exoPlayer.stop();
            exoPlayer.release();
        }
        if (counterImage < userStory.size()) {


            if (userStory.get(counterImage).getType().equals("image")){
                ControllerListener listener = new BaseControllerListener<ImageInfo>() {

                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                        if (imageInfo.getQualityInfo().isOfFullQuality() == true) {
                            storiesProgressView.startStories(counterImage);
                        }
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        storiesProgressView.skip();
                    }

                };
                story_iv.setVisibility(View.VISIBLE);
                exoPlayerView.setVisibility(View.GONE);
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(userStory.get(counterImage).getImage_url())
                        .setControllerListener(listener)
                        .build();
                story_iv.setHierarchy(hierarchy);
                hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
                story_iv.setController(controller);
            }else {
                story_iv.setVisibility(View.GONE);
                exoPlayerView.setVisibility(View.VISIBLE);
                Set_Player(userStory.get(counterImage).getImage_url());
            }
        }
    }


    @Override
    public void onPrev() {
        if (exoPlayer.isPlaying()){
            exoPlayer.stop();
            exoPlayer.release();
        }
        if (counterImage > 0) {
            counterImage--;

            if (userStory.get(counterImage).getType().equals("image")){
                ControllerListener listener = new BaseControllerListener<ImageInfo>() {

                    @Override
                    public void onFinalImageSet(String id, @Nullable ImageInfo imageInfo, @Nullable Animatable animatable) {
                        if (imageInfo.getQualityInfo().isOfFullQuality() == true) {
                            storiesProgressView.startStories(counterImage);
                        }
                    }

                    @Override
                    public void onFailure(String id, Throwable throwable) {
                        storiesProgressView.skip();
                    }

                };
                story_iv.setVisibility(View.VISIBLE);
                exoPlayerView.setVisibility(View.GONE);
                DraweeController controller = Fresco.newDraweeControllerBuilder()
                        .setUri(userStory.get(counterImage).getImage_url())
                        .setControllerListener(listener)
                        .build();
                story_iv.setHierarchy(hierarchy);
                hierarchy.setActualImageScaleType(ScalingUtils.ScaleType.FIT_CENTER);
                story_iv.setController(controller);
            }else {
                story_iv.setVisibility(View.GONE);
                exoPlayerView.setVisibility(View.VISIBLE);
                Set_Player(userStory.get(counterImage).getImage_url());
            }

        } else if (counterStory != 0) {
            counterStory--;
            storiesProgressView.destroy();
            getUserStroty();
        }

    }

    /*private void Set_Player(String image_url) {

        Uri uri = Uri.parse("https://gee-e.com/tictic-gee-e.com/API//upload/video/1632832278_1072214984.mp4");
        player.setVideoURI(uri);
        player.start();
    }*/

    @Override
    public void onComplete() {
        counterStory++;
        if (position != list.size() - 1) {
            ViewStory.mPager.setCurrentItem(position + 1);
            // Functions.showLoader(getActivity(), false, false);
            if (exoPlayer.isPlaying()){
                exoPlayer.stop();
                exoPlayer.release();
            }
        } else {
            if (exoPlayer.isPlaying()){
                exoPlayer.stop();
                exoPlayer.release();
            }
            getActivity().finish();
        }
    }


    @Override
    public void onDestroyView() {
        if (exoPlayer.isPlaying()){
            exoPlayer.stop();
            exoPlayer.release();
        }

        storiesProgressView.destroy();
        super.onDestroyView();
    }


    public void resetComplete() {
        Field field = null;
        try {
            field = storiesProgressView.getClass().getDeclaredField("isComplete");
            // Allow modification on the field
            field.setAccessible(true);
            // Sets the field to the new value for this instance
            field.set(storiesProgressView, false);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void pause() {
        storiesProgressView.pause();
    }


    SimpleExoPlayer privious_player;
    public void Release_Privious_Player(){
        if(privious_player!=null) {
            privious_player.removeListener((Player.EventListener) getActivity());
            privious_player.release();
        }
    }
    Story_F story_f;
    // this will call when swipe for another video and
    // this function will set the player to the current video
    public void Set_Player(String path){
        Functions.showLoader(getActivity(), false, false);
        // storiesProgressView.pause();
        try {
            Uri uri = Uri.parse(path);
            MediaSource mediaSource = new ExtractorMediaSource(uri, dataSourceFactory, extractorsFactory, null, null);
            exoPlayer.prepare(mediaSource);
            exoPlayer.addListener(new Player.EventListener() {
                @Override
                public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                    if (playbackState == ExoPlayer.STATE_BUFFERING){
                        //  storiesProgressView.pause();
                        pause();
                        Functions.showLoader(getActivity(), false, false);
                    } else {
                        if (counterImage!=1){
                            storiesProgressView.startStories(counterImage);
                        }
                        Functions.cancelLoader();
                    }
                    if (playbackState == ExoPlayer.STATE_ENDED){
                        storiesProgressView.pause();
                        exoPlayer.stop();
                        exoPlayer.release();
                        onNext();
                    }
                }
            });


        }catch (Exception e){

        }

    }

    public void uploadImage(String message,String receiverid,String username,String userpic) {
        DatabaseReference rootref;
        DatabaseReference adduserToInbox;
        rootref = FirebaseDatabase.getInstance().getReference();
        adduserToInbox = FirebaseDatabase.getInstance().getReference();
        Date c = Calendar.getInstance().getTime();
        final String formattedDate = Variables.df.format(c);

        final String currentUserRef = "chat" + "/" + Variables.userId + "-" + receiverid;
        final String chatUserRef = "chat" + "/" + receiverid + "-" + Variables.userId;

        DatabaseReference reference = rootref.child("chat").child(Variables.userId + "-" + receiverid).push();
        final String pushid = reference.getKey();
        final HashMap messageUserMap = new HashMap<>();
        messageUserMap.put("chat_id", pushid);
        messageUserMap.put("sender_id", Variables.userId);
        messageUserMap.put("receiver_id", receiverid);
        messageUserMap.put("sender_name", Variables.userName);

        messageUserMap.put("rec_img", "");
        messageUserMap.put("pic_url", userStory.get(counterImage).getImage_url());
        messageUserMap.put("lat", "");
        messageUserMap.put("long", "");

        messageUserMap.put("text", message);
        messageUserMap.put("type", "text");
        messageUserMap.put("status", "0");
        messageUserMap.put("time", "");
        messageUserMap.put("timestamp", formattedDate);

        final HashMap user_map = new HashMap<>();
        user_map.put(currentUserRef + "/" + pushid, messageUserMap);
        user_map.put(chatUserRef + "/" + pushid, messageUserMap);

        rootref.updateChildren(user_map, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                //if first message then set the visibility of whoops layout gone
                String inbox_sender_ref = "Inbox" + "/" + Variables.userId + "/" + receiverid;
                String inbox_receiver_ref = "Inbox" + "/" + receiverid + "/" + Variables.userId;

                HashMap<String, String> sendermap = new HashMap<>();
                sendermap.put("rid", Variables.userId);
                sendermap.put("name", Variables.userName);
                sendermap.put("msg", message);
                sendermap.put("pic", Variables.userPic);
                sendermap.put("timestamp", formattedDate);
                sendermap.put("date", formattedDate);

                sendermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
                sendermap.put("status", "0");
                sendermap.put("block", "0");
                sendermap.put("read", "0");

                HashMap<String, String> receivermap = new HashMap<>();
                receivermap.put("rid", receiverid);
                receivermap.put("name", username);
                receivermap.put("msg", message);
                receivermap.put("pic", userpic);
                receivermap.put("timestamp", formattedDate);
                receivermap.put("date", formattedDate);

                receivermap.put("sort", "" + Calendar.getInstance().getTimeInMillis());
                receivermap.put("status", "0");
                receivermap.put("block", "0");
                receivermap.put("read", "0");


                HashMap bothUserMap = new HashMap<>();
                bothUserMap.put(inbox_sender_ref, receivermap);
                bothUserMap.put(inbox_receiver_ref, sendermap);

                adduserToInbox.updateChildren(bothUserMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        edittext_id.setText("");
                      //  sendPushNotification(message);

                    }
                });
            }
        });
    }

}
