package com.geee.Inner_VP_Package.Profile_Pacakge.Edit_Profile_Package;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.VolleyError;
import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Home_Package.Adapters.StoryAdapter;
import com.geee.Inner_VP_Package.Profile_Pacakge.Edit_Profile_Package.Edit_Profile_DataModel.Data_model_Edit_profile;
import com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package.ShowStoryDM;
import com.geee.Main_VP_Package.Main;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.SplashActivity;
import com.geee.Volley_Package.API_Calling_Methods;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.geee.tictokcode.SimpleClasses.Variables;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.geee.CodeClasses.Variables.mResultCallback;
import static com.geee.CodeClasses.Variables.mVolleyService;

public class EditProfile extends AppCompatActivity implements View.OnClickListener {

    private final int PICK_IMAGE_CAMERA = 1, PICK_IMAGE_GALLERY = 2,PICK_COVER_CAMERA = 3, PICK_COVER_GALLERY = 4;
    TextView spinner,editEmail;
    String[] list = {"Not specified", "Male", "Female"};
    LinearLayout bio, email;
    String gender;
    TextView ivDone;
    ImageView icBack;
    ImageView profilePic;
    SimpleDraweeView user_cover_img_id;
    Data_model_Edit_profile datamodelEdit;
    String userId;
    EditText editName, editUserName, editWebsite, editBio,mobileedt;

    String userBio, userEmail, userPhone;
    String imageFilePath,coverimageFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        datamodelEdit = new Data_model_Edit_profile();

        spinner = findViewById(R.id.spinner_id);
        spinner.setText("Gender");
        bio = findViewById(R.id.bio_ll_id);
        email = findViewById(R.id.email_ll_id);
        editName = findViewById(R.id.name_et_id);
        profilePic = findViewById(R.id.user_prof_img_id);
        editUserName = findViewById(R.id.uname_et_id);
        editWebsite = findViewById(R.id.website_et_id);
        editBio = findViewById(R.id.bio_et_id);
        editEmail = findViewById(R.id.email_et_id);
        mobileedt = findViewById(R.id.mobileedt);
        user_cover_img_id = findViewById(R.id.user_cover_img_id);

        Intent intent = getIntent();
        userId = intent.getStringExtra("user_id"); //if it's a string you stored.
        userBio = intent.getStringExtra("user_bio");
        userEmail = intent.getStringExtra("user_email");
        userPhone = intent.getStringExtra("user_phone");

        getLoggedInUserProfile("" + SharedPrefrence.getUserIdFromJson(EditProfile.this));
        profilePic.setOnClickListener(v -> selectImage());
        user_cover_img_id.setOnClickListener(v -> selectcoverImage());
        icBack = findViewById(R.id.ic_back);
        ivDone = findViewById(R.id.tick_id);

        final View parent = (View) ivDone.getParent();  // button: the view you want to enlarge hit area
        parent.post(() -> {
            final Rect rect = new Rect();
            icBack.getHitRect(rect);
            rect.top -= 100;    // increase top hit area
            rect.left -= 250;   // increase left hit area
            rect.bottom += 100; // increase bottom hit area
            rect.right += 100;  // increase right hit area
            parent.setTouchDelegate(new TouchDelegate(rect, ivDone));
        });

        bio.setOnClickListener(this);
        email.setOnClickListener(this);
        icBack.setOnClickListener(this);
        ivDone.setOnClickListener(this);
        spinner.setOnClickListener(this);

        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(EditProfile.this, "You can't change your email", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ic_back:
                finish();
                break;
            case R.id.tick_id:
                updateProfile();
                break;
            case R.id.spinner_id:
                genderUI();
                break;

            default:
                break;
        }
    }

    // End init edit Texts
    // Method to edit profile.
    public void updateProfile() {

        if (editName.getText().toString().trim().equalsIgnoreCase("")) {
            editName.setError(getResources().getString(R.string.ivalid_input));
        } else if (editUserName.getText().toString().trim().equalsIgnoreCase("")) {
            editUserName.setError(getResources().getString(R.string.ivalid_input));
        } else if (editEmail.getText().toString().trim().equalsIgnoreCase("")) {
            editEmail.setError(getResources().getString(R.string.ivalid_input));
        } else if (!Functions.isValidEmail(editEmail.getText().toString())) {
            editEmail.setError(getResources().getString(R.string.invalid_email));
        } else {
            Call_Api_For_Edit_profile();
            /// If all fields are filled up
            // Calling an API to update profile
            API_Calling_Methods.editProfile(
                    "" + editEmail.getText().toString(),
                    "" + editName.getText().toString(),
                    "" + editUserName.getText().toString(),
                    "" + userId,
                    "" + gender,
                    "" + "geee.com" ,
                    "" + editBio.getText().toString(),
                    "" + mobileedt.getText().toString(),
                    EditProfile.this
            );

        }
    }
    public void getLoggedInUserProfile(String userId) {
        initVolleyCallback();
        Functions.showLoader(EditProfile.this, false, false);
        mVolleyService = new VolleyService(mResultCallback, EditProfile.this);
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Get_User_detail, sendObj);
    }

    // Initialize Interface Call Backs
    void initVolleyCallback() {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.cancelLoader();
                Functions.logDMsg("editProfiledata : "+response);
                try {
                    JSONObject msgObj = response.getJSONObject("msg");
                    JSONObject userObj = msgObj.getJSONObject("User");
                    if (userObj.getString("image") != null && !userObj.getString("image").equals("")) {
                        Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("image"));
                        ShowStoryDM showStoryDM = new ShowStoryDM();
                        showStoryDM.setUserImg(userObj.getString("image"));
                       // profilePic.setImageURI(uri);
                        Picasso.get().load(Constants.BASE_URL + userObj.getString("image"))
                                .error(R.drawable.image_placeholder)
                                .into(profilePic);
                    }
                    if (userObj.getString("cover_image") != null && !userObj.getString("cover_image").equals("")) {
                        Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("cover_image"));
                        user_cover_img_id.setImageURI(uri);
                    }
                    editName.setText("" + userObj.getString("full_name"));
                    editUserName.setText("" + userObj.getString("username"));
                    editWebsite.setText("" + userObj.getString("website"));
                    editEmail.setText("" + userObj.getString("email"));
                    editBio.setText(userObj.getString("bio"));
                    mobileedt.setText(userObj.getString("phone"));
                    spinner.setText("" + userObj.getString("gender"));

                } catch (Exception b) {
                    b.printStackTrace();
                }
            }

            @Override
            public void notifyError(String requestType, VolleyError error) {
                error.printStackTrace();
            }
        };
    }

    private void openCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, PICK_IMAGE_CAMERA);
            }
        }
    }
    private void opencoverCameraIntent() {
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(this.getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createcoverImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this, this.getPackageName() + ".provider", photoFile);
                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(pictureIntent, PICK_COVER_CAMERA);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        File image = File.createTempFile("IMG_" + timeStamp + "_", ".jpg", this.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        imageFilePath = image.getAbsolutePath();
        return image;
    }
    private File createcoverImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());
        File coverimage = File.createTempFile("IMG_" + timeStamp + "_", ".jpg", this.getExternalFilesDir(Environment.DIRECTORY_PICTURES));
        coverimageFilePath = coverimage.getAbsolutePath();
        return coverimage;
    }

    // Add profile pic Stuff
    // Select image from camera and gallery
    private void selectImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EditProfile.this);
                builder.setTitle("Select Option");
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        openCameraIntent();
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_IMAGE_GALLERY);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else
                Toast.makeText(EditProfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(EditProfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
    private void selectcoverImage() {
        try {
            PackageManager pm = getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EditProfile.this);
                builder.setTitle("Select Option");
                builder.setItems(options, (dialog, item) -> {
                    if (options[item].equals("Take Photo")) {
                        dialog.dismiss();
                        opencoverCameraIntent();
                    } else if (options[item].equals("Choose From Gallery")) {
                        dialog.dismiss();
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, PICK_COVER_GALLERY);
                    } else if (options[item].equals("Cancel")) {
                        dialog.dismiss();
                    }
                });
                builder.show();
            } else
                Toast.makeText(EditProfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(EditProfile.this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(EditProfile.this, "Please wait...", Toast.LENGTH_SHORT).show();

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_GALLERY) {
            if (data != null) {
                Uri selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .setAspectRatio(1, 1)
                        .start(EditProfile.this);
            }
        }
         if (requestCode == PICK_IMAGE_CAMERA) {
            if (data != null) {
                Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
                //  CropImage.activity(selectedImage).start(EditProfile.this);
                profilePic.setImageURI(selectedImage);
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), null, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                callApiUploadImage(baos);
            }
        }else if (requestCode == PICK_COVER_GALLERY) {
            if (data != null) {
                Uri selectedImage = data.getData();
                user_cover_img_id.setImageURI(selectedImage);
               // user_cover_img_id.setImageURI(selectedImage);
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), null, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                callApiUploadcoverImage(baos);
            }

        } else if (requestCode == PICK_COVER_CAMERA) {
            if (data != null) {
                Uri selectedImage = data.getData();
                user_cover_img_id.setImageURI(selectedImage);
                user_cover_img_id.setImageURI(selectedImage);
                InputStream imageStream = null;
                try {
                    imageStream = getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), null, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                callApiUploadcoverImage(baos);
            }
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = result.getUri();
                Functions.logDMsg("resultUri : " + selectedImage);
                if (data != null) {
                    profilePic.setImageURI(selectedImage);
                    InputStream imageStream = null;
                    try {
                        imageStream = getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), null, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    callApiUploadImage(baos);

                }
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Functions.logDMsg("resultUri : " + error.toString());
            }
        }
    }




    private void callApiUploadImage(ByteArrayOutputStream baos) {

            byte[] imageBytes = baos.toByteArray();

            String base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);

            base64 = base64.replaceAll("\n", "");
            base64 = base64.replace(" ", "").trim();


            // Call_Api_For_image("" + base64);
            JSONObject parameters = null;
            try {
                parameters = new JSONObject();
                JSONObject file_data=new JSONObject();
                parameters.put("fb_id", com.geee.tictokcode.SimpleClasses.Variables.sharedPreferences.getString(com.geee.tictokcode.SimpleClasses.Variables.u_id,"0"));
                file_data.put("file_data",base64);
                parameters.put("image",file_data);
                Log.i("dfsdfsdfsdfsdf",parameters.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                Log.i("dffsdfsfssdfsdfsdfsdf",e.toString());
            }
            String finalBase6 = base64;
            ApiRequest.Call_Api(EditProfile.this, com.geee.tictokcode.SimpleClasses.Variables.uploadImage, parameters, new Callback() {
                @Override
                public void Responce(String resp) {
                    Log.i("dfsfsfsffsf",resp.toString());
                    try {
                        JSONObject response=new JSONObject(resp);
                        String code=response.optString("code");
                        JSONArray msg=response.optJSONArray("msg");
                        if(code.equals("200")){

                            JSONObject data=msg.optJSONObject(0);


                            com.geee.tictokcode.SimpleClasses.Variables.sharedPreferences.edit().putString(com.geee.tictokcode.SimpleClasses.Variables.u_pic,data.optString("profile_pic")).commit();
                            com.geee.tictokcode.SimpleClasses.Variables.user_pic=data.optString("profile_pic");
                            API_Calling_Methods.addUserImage("" + userId, "" + finalBase6, EditProfile.this);
                    /*    if(Variables.user_pic!=null && !Variables.user_pic.equals(""))
                            Picasso.get()
                                    .load(Variables.user_pic)
                                    .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                                    .resize(200,200).centerCrop().into(profile_image);*/



                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }

    private void callApiUploadcoverImage(ByteArrayOutputStream baos) {

        byte[] imageBytes = baos.toByteArray();

        String base64 = Base64.encodeToString(imageBytes, Base64.DEFAULT);

        base64 = base64.replaceAll("\n", "");
        base64 = base64.replace(" ", "").trim();

        API_Calling_Methods.addUsercoverImage("" + userId, "" + base64, EditProfile.this);
        Call_Api_For_coverimage("" + base64);
    }
    public void genderUI() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(EditProfile.this);
        builder.setTitle("Select Gender");
        builder.setItems(list, (dialog, item) -> {
            spinner.setText("" + list[item]);
            gender = list[item];
        });
        builder.show();
    }
    public  void Call_Api_For_image(String s) {
        //  Toast.makeText(EditProfile.this, "callimage", Toast.LENGTH_SHORT).show();
        JSONObject parameters = null;
        try {
            parameters = new JSONObject();
            JSONObject file_data=new JSONObject();
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            file_data.put("file_data",s);
            parameters.put("image",file_data);
            Log.i("dfsdfsdfsdfsdf",parameters.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("dffsdfsfssdfsdfsdfsdf",e.toString());
        }
        ApiRequest.Call_Api(EditProfile.this, Variables.uploadImage, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Log.i("dfsfsfsffsf",resp.toString());
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    JSONArray msg=response.optJSONArray("msg");
                    if(code.equals("200")){

                        JSONObject data=msg.optJSONObject(0);


                        Variables.sharedPreferences.edit().putString(Variables.u_pic,data.optString("profile_pic")).commit();
                        Variables.user_pic=data.optString("profile_pic");

                    /*    if(Variables.user_pic!=null && !Variables.user_pic.equals(""))
                            Picasso.get()
                                    .load(Variables.user_pic)
                                    .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                                    .resize(200,200).centerCrop().into(profile_image);*/

                        Toast.makeText(EditProfile.this, "Image Update Successfully", Toast.LENGTH_SHORT).show();
                        Intent start = new Intent(EditProfile.this, Main.class);
                        startActivity(start);
                        finish();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }
    public  void Call_Api_For_coverimage(String s) {
        JSONObject parameters = null;
        try {
            parameters = new JSONObject();
            JSONObject file_data=new JSONObject();
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            file_data.put("file_data",s);
            parameters.put("image",file_data);
            Log.i("dfsdfsdfsdfsdf",parameters.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("dffsdfsfssdfsdfsdfsdf",e.toString());
        }
        ApiRequest.Call_Api(EditProfile.this, Variables.uploadcoverImage, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Log.i("dfsfsfsffsf",resp.toString());
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    JSONArray msg=response.optJSONArray("msg");
                    if(code.equals("200")){
                        JSONObject data=msg.optJSONObject(0);


                        Variables.sharedPreferences.edit().putString(Variables.u_coverpic,data.optString("cover_pic")).commit();
                        Variables.user_coverpic=data.optString("cover_pic");

                    /*    if(Variables.user_pic!=null && !Variables.user_pic.equals(""))
                            Picasso.get()
                                    .load(Variables.user_pic)
                                    .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                                    .resize(200,200).centerCrop().into(profile_image);*/


                        Toast.makeText(EditProfile.this, "Image Update Successfully", Toast.LENGTH_SHORT).show();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }
    public  void Call_Api_For_Edit_profile() {

        //  Functions.Show_loader(context,false,false);

        String uname=editUserName.getText().toString();
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("username",uname);
            parameters.put("fb_id", Variables.sharedPreferences.getString(Variables.u_id,"0"));
            parameters.put("first_name",editName.getText().toString());
            parameters.put("last_name",editUserName.getText().toString());
            parameters.put("gender",gender);
            parameters.put("bio",bio);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        ApiRequest.Call_Api(this, Variables.edit_profile, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                //  Functions.cancel_loader();
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    JSONArray msg=response.optJSONArray("msg");
                    if(code.equals("200")) {

                        SharedPreferences.Editor editor = Variables.sharedPreferences.edit();

                        String u_name=editUserName.getText().toString();
                        if(!u_name.contains("@"))
                            u_name="@"+u_name;

                        editor.putString(Variables.u_name,u_name);
                        editor.putString(Variables.f_name, editName.getText().toString());
                        editor.putString(Variables.l_name, editName.getText().toString());
                        editor.commit();

                        Variables.user_name = u_name;
                    }else {

                        if(msg!=null){
                            JSONObject jsonObject=msg.optJSONObject(0);
                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }

}