package com.geee.Inner_VP_Package.Add_picture_Package;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

//import com.geee.Photoeditor.activities.HomeActivity;
import com.geee.Photoeditor.activities.PhotoEditorActivity;
import com.geee.Photoeditor.picker.ImageCaptureManager;
import com.geee.Photoeditor.picker.PhotoPicker;
import com.geee.tictokcode.Video_Recording.Video_Recoder_A;
import com.google.android.material.appbar.AppBarLayout;
import com.geee.BackStack.RootFragment;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.RecyclerItemClickListener;
import com.geee.Interface.CallbackResponse;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Utils.FiltersA;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;

public class GalleryF extends RootFragment {
    File selcteddfole;
    public static final int REQUEST_IMAGE_CAPTURE = 1;
    public static final int PICK_IMAGES = 20;
    public static final int STORAGE_PERMISSION = 100;
    private final String PACKAGE_URL_SCHEME = "package:";
    ArrayList<ImageModel> imageList = new ArrayList<>();
    View view;
    RecyclerView recyclerView;
    GalleryAdapter galleryAdp;
    ImageView myImage;
    String path;
    TextView next;
    String[] projection = {MediaStore.Images.Media.DATA};
    AppBarLayout appBarLayout;
    String[] title = {"Camera"/*, "Folder"*/,"G Star"};
    File image;
    String mCurrentPhotoPath;
    int[] resImg = {R.drawable.ic_camera_white_30dp/*, R.drawable.ic_folder_white_30dp*/, R.drawable.ic_video};
    String imageFilePath;
    private CropImageView mCropImageView;
    Uri imagefd ;
    private ImageCaptureManager imageCaptureManager;

    ImageView rec;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.gallery_f, container, false);
        this.imageCaptureManager = new ImageCaptureManager(getActivity());

        //checkPermissions();
        return view;
    }

    public void init() {
        myImage = view.findViewById(R.id.image);
        mCropImageView = view.findViewById(R.id.CropImageView);
        appBarLayout = view.findViewById(R.id.app_bar_layout);
        rec = view.findViewById(R.id.rec);
        mCropImageView.setAspectRatio(1, 1);
        mCropImageView.setFixedAspectRatio(true);
        next = view.findViewById(R.id.tv_username);

        next.setOnClickListener(v -> {
          //  Functions.showLoader(getActivity(),false,false);
            try {
                getCropBitmap();
            } catch (Exception b) {
                b.printStackTrace();
            }
        });
        rec.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), Video_Recoder_A.class);
            startActivity(intent);
        });

        recyclerView = view.findViewById(R.id.rv_id);
        galleryAdp = new GalleryAdapter(getContext(), imageList);

        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 4));
        recyclerView.setHasFixedSize(false);
        recyclerView.setAdapter(galleryAdp);
        galleryAdp.notifyDataSetChanged();

        recyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(getContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        recyclerView.smoothScrollToPosition(position);
                        if (position == 0) {
                            if (checkPermissions())
                                openCameraIntent();

                        } else if (position == 1) {
                            Intent intent = new Intent(getActivity(), Video_Recoder_A.class);
                            startActivity(intent);
                        }  /*else if (position == 2) {

                        }*/ else {
                            appBarLayout.setExpanded(true, true);
                            try {
                                path = imageList.get(position).getImage();
                                mCropImageView.setImageUriAsync(Functions.getImageContentUri(getActivity(), new File(path)));
                              /*  SharedPrefrence.saveInfoShare(getContext(), "" + path,
                                        "" + SharedPrefrence.share_media_upload_path_key
                                );*/

                                Intent intent2 = new Intent(getApplicationContext(), PhotoEditorActivity.class);
                                intent2.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, path);
                                startActivity(intent2);

                            } catch (Exception v) {
                                v.printStackTrace();
                            }

                          //  getPickImageIntent();

                        }
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {
                        //auto generated method
                    }
                })
        );
    }

    // get all images from external storage
    public void getAllImages() {
        imageList.clear();
        Cursor cursor = getActivity().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, null);
        while (cursor.moveToNext()) {
            @SuppressLint("Range") String absolutePathOfImage = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));

            ImageModel imageModel = new ImageModel();
            imageModel.setImage(absolutePathOfImage);
            imageList.add(imageModel);
        }
        cursor.close();
    }

    public void setImageList() {
        GridLayoutManager gridLayoutManager3 = new GridLayoutManager(getActivity(),4,GridLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(gridLayoutManager3);
        recyclerView.setNestedScrollingEnabled(false);
        Collections.reverse(imageList);
        galleryAdp = new GalleryAdapter(getActivity(), imageList);
        recyclerView.setAdapter(galleryAdp);
        setImagePickerList();
    }

    // Add Camera and Folder in ArrayList
    public void setImagePickerList() {
        for (int i = 0; i < resImg.length; i++) {
            ImageModel imageModel = new ImageModel();
            imageModel.setResImg(resImg[i]);
            imageModel.setTitle(title[i]);
            imageList.add(i, imageModel);
        }
        galleryAdp.notifyDataSetChanged();
    }

    public void getPickImageIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGES);
    }

    private void openCameraIntent() {
        /*Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
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
                startActivityForResult(pictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }*/
        takePhotoFromCamera();

    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "IMG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix /
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
        if (requestCode == PICK_IMAGES) {
            Log.i("dfdfdf",resultCode+"");
            if (data != null) {
                try {
                    final Uri imageUri = data.getData();
                    final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    Intent intent2 = new Intent(getApplicationContext(), PhotoEditorActivity.class);
                    intent2.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, getRealPathFromURI(imageUri));
                    startActivity(intent2);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                }
            }
        } else if (requestCode == 1) {
        /*    Functions.logDMsg("imageFilePath : " + imageFilePath);
            Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
            Functions.logDMsg("selectedImage : " + selectedImage);
            if (selectedImage != null)
                mCropImageView.setImageUriAsync(selectedImage);
             selcteddfole = new File(selectedImage.getPath());

            imagefd = selectedImage;*/
            if (this.imageCaptureManager == null) {
                this.imageCaptureManager = new ImageCaptureManager(getActivity());
            }
            // new Handler(Looper.getMainLooper()).post(() -> HomeActivity.this.imageCaptureManager.galleryAddPic());
            Intent intent2 = new Intent(getApplicationContext(), PhotoEditorActivity.class);
            intent2.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, this.imageCaptureManager.getCurrentPhotoPath());
            startActivity(intent2);

        }
    }

    public void takePhotoFromCamera() {
        try {
            startActivityForResult(this.imageCaptureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }
    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }

    public void getCropBitmap() {
        Bitmap cropped = mCropImageView.getCroppedImage();
       /* ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        cropped.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArray = bStream.toByteArray();*/

      //  String strFileName = selcteddfole.getName();
        String filePath= tempFileImage(getActivity(),cropped,"absa");



        try {
           /* SharedPreferences sharedpreferences = getActivity().getSharedPreferences("imageipload", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("image", Arrays.toString(byteArray));
            editor.clear();
            editor.apply();*/
            Intent anotherIntent = new Intent(getContext(), FiltersA.class);
            anotherIntent.putExtra("image", filePath);
            startActivity(anotherIntent);
           // Functions.cancelLoader();
//            Intent anotherIntent = new Intent(getContext(), FiltersA.class);
//            anotherIntent.putExtra("image", byteArray);
//            startActivity(anotherIntent);
        } catch (Exception v) {
            v.printStackTrace();
        }
    }
    public static String tempFileImage(Context context, Bitmap bitmap, String name) {

        File outputDir = context.getCacheDir();
        File imageFile = new File(outputDir, name + ".jpg");

        OutputStream os;
        try {
            os = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, os);
            os.flush();
            os.close();
        } catch (Exception e) {
            Log.e(context.getClass().getSimpleName(), "Error writing file", e);
        }

        return imageFile.getAbsolutePath();
    }
    public boolean checkPermissions() {
        String[] permissions = {
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
        };

        if (!hasPermissions(getActivity(), permissions)) {
            requestPermissions(permissions, STORAGE_PERMISSION);
        } else {
            init();
            getAllImages();
            setImageList();
            if (!imageList.isEmpty()) {
                String mpath = imageList.get(3).getImage();
                if (mpath != null && !mpath.equals(""))
                    mCropImageView.setImageUriAsync(Functions.getImageContentUri(getActivity(), new File(mpath)));
            }
            return true;
        }
        return false;
    }

    public boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getAllImages();
        } else {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)) {

            } else {
                Functions.customAlertDialogDenied(getActivity(), "gallery", new CallbackResponse() {
                    @Override
                    public void Responce(String resp) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getActivity().getPackageName()));
                        startActivity(intent);
                    }
                });
            }
        }
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    checkPermissions();
                }
            }, 200);
        }
    }



}