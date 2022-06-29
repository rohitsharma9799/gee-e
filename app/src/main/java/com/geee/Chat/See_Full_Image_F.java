package com.geee.Chat;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import androidx.fragment.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.downloader.Error;
import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.Progress;
import com.downloader.request.DownloadRequest;
import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.File;

import com.geee.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class See_Full_Image_F extends Fragment {


    View view;
    Context context;
    ImageButton savebtn,sharebtn, closeGallery;


    ZoomageView singleImage;

    String imageUrl, chatId;

    ProgressBar p_bar;

    ProgressDialog progressDialog;

    // this is the third party library that will download the image
    DownloadRequest prDownloader;

    File direct;
    File fullpath;
    int width,height;

    public See_Full_Image_F() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.fragment_see_full_image, container, false);
        context=getContext();

        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
         height = displayMetrics.heightPixels;
         width = displayMetrics.widthPixels;

        imageUrl =getArguments().getString("image_url");
        chatId =getArguments().getString("chat_id");

        closeGallery =view.findViewById(R.id.close_gallery);
        closeGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });



        progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Please Wait");

        PRDownloader.initialize(getActivity().getApplicationContext());


        // get the full path of image in database
        fullpath = new File(Environment.getExternalStorageDirectory() +"/Chatbuzz/"+ chatId +".jpg");

        // if the image file is exits then we will hide the save btn
        savebtn=view.findViewById(R.id.savebtn);
        if(fullpath.exists()){
            savebtn.setVisibility(View.GONE);
        }


        // get  the directory inwhich we want to save the image
        direct = new File(Environment.getExternalStorageDirectory() +"/Chatbuzz/");

        // this code will download the image
        prDownloader= PRDownloader.download(imageUrl, direct.getPath(), chatId +".jpg")
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


        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savepicture(false);
            }
        });




        p_bar=view.findViewById(R.id.p_bar);

        singleImage =view.findViewById(R.id.single_image);


        // if the image is already save then we will show the image from directory otherwise
        // we will show the image by using picasso
        if(fullpath.exists()){
            Uri uri= Uri.parse(fullpath.getAbsolutePath());
            singleImage.setImageURI(uri);
        }else {
            p_bar.setVisibility(View.VISIBLE);
            Picasso.get().load(imageUrl).placeholder(R.drawable.image_placeholder)
                    .into(singleImage, new Callback() {
                        @Override
                        public void onSuccess() {

                            p_bar.setVisibility(View.GONE);
                        }

                        @Override
                        public void onError(Exception e) {
                            p_bar.setVisibility(View.GONE);

                        }

                    });
        }

        sharebtn=view.findViewById(R.id.sharebtn);
        sharebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharePicture();
            }
        });


        return view;
    }



    // this method will share the picture to other user
    public void sharePicture(){
        if(checkstoragepermision()) {
            Uri bitmapuri;
            if(fullpath.exists()){
                bitmapuri= Uri.parse(fullpath.getAbsolutePath());
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/png");
                intent.putExtra(Intent.EXTRA_STREAM, bitmapuri);
                startActivity(Intent.createChooser(intent, ""));
            }
            else {
                savepicture(true);
            }

        }
    }


    // this funtion will save the picture but we have to give tht permision to right the storage
    public void savepicture(final boolean isfromshare){
        if(checkstoragepermision()) {

            final File direct = new File(Environment.getExternalStorageDirectory() + "/DCIM/Binder/");
            progressDialog.show();
            prDownloader.start(new OnDownloadListener() {
                @Override
                public void onDownloadComplete() {
                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.parse(direct.getPath() + chatId + ".jpg"));
                    context.sendBroadcast(intent);
                    progressDialog.dismiss();
                    if (isfromshare) {
                        sharePicture();
                    } else {
                        AlertDialog alertDialog = new AlertDialog.Builder(context)
                                //set title
                                .setTitle("Image Saved")
                                //set message
                                .setMessage(fullpath.getAbsolutePath())
                                //set negative button
                                .setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                    }
                                })
                                .show();
                    }
                }

                @Override
                public void onError(Error error) {
                    progressDialog.dismiss();
                    Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show();

                }

            });

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(grantResults[0]== PackageManager.PERMISSION_GRANTED){
            Toast.makeText(context, "Click Again", Toast.LENGTH_LONG).show();
        }
    }

    public boolean checkstoragepermision(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;

            } else {

                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                return false;
            }
        }else {

            return true;
        }
    }


}


