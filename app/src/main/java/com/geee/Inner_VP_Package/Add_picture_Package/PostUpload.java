package com.geee.Inner_VP_Package.Add_picture_Package;

import static com.geee.CodeClasses.Variables.mResultCallback;
import static com.geee.CodeClasses.Variables.mVolleyService;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.graphics.Rect;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.util.Base64;
import android.util.Log;
import android.view.TouchDelegate;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Variables;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.GpsUtils;
import com.geee.Constants;
import com.geee.Interface.CallbackResponse;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.API_Calling_Methods;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PostUpload extends AppCompatActivity  {
    private final String PACKAGE_URL_SCHEME = "package:";
    double lat = 0.0, lng = 0.0;
    String stLocation = "";
    String mediaPath;
    EditText postTextt;
    ImageView imageView, icBack, iv_cross;
    TextView textPostUpload, postShare, tvLocation;
    Spannable mspanable;
    int hashTagIsComing = 0;
    ImageView profileImage;
    FusedLocationProviderClient mFusedLocationProviderClient;
    boolean isFABOpen = false;
    FloatingActionButton fab1;
    FloatingActionButton fab2;
    FloatingActionButton fab3;
    FloatingActionButton fab;
    Uri urim = null;
    public static String bitmapToBase64(Bitmap imagebitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArray = baos.toByteArray();
        String base64 = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }
    private StorageReference mStorageRef;
    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_upload);
        mStorageRef = FirebaseStorage.getInstance().getReference();

        SharedPreferences sharedpreferences = getSharedPreferences("filteruri", Context.MODE_PRIVATE);
        mediaPath = sharedpreferences.getString("filteruri","");
       // mediaPath = getIntent().getExtras().getString("media_path");
        Uri uri = Uri.parse(mediaPath);
        postShare = findViewById(R.id.tv_username);
        postTextt = findViewById(R.id.tv_caption);
        imageView = findViewById(R.id.image);
        textPostUpload = findViewById(R.id.post_text_button);
        tvLocation = findViewById(R.id.tvLocation);
        profileImage = findViewById(R.id.profileImage);
        icBack = findViewById(R.id.ic_back);
        iv_cross = findViewById(R.id.iv_cross);
        fab = findViewById(R.id.fab);
        fab1 = findViewById(R.id.fab1);
        fab2 = findViewById(R.id.fab2);
        getLoggedInUserProfile(SharedPrefrence.getUserIdFromJson(PostUpload.this), "fromCreate");
       /* String profileImageUrl = SharedPrefrence.getUserImageFromJson(PostUpload.this);
        if (profileImageUrl != null && !profileImageUrl.equals(""))
           // profileImage.setImageURI(Uri.parse(profileImageUrl));
        Log.i("dfsfsdf",SharedPrefrence.getUserImageFromJson(PostUpload.this));
            Picasso.get().load(SharedPrefrence.getUserImageFromJson(PostUpload.this)).placeholder(R.drawable.profile_image_placeholder).into(profileImage);
*/
        tvLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enablePermission();
            }
        });


        iv_cross.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvLocation.setText("");
            }
        });

        final View parent = (View) textPostUpload.getParent();  // button: the view you want to enlarge hit area
        parent.post(() -> {
            final Rect rect = new Rect();
            textPostUpload.getHitRect(rect);
            rect.top -= 100;    // increase top hit area
            rect.left -= 200;   // increase left hit area
            rect.bottom += 100; // increase bottom hit area
            rect.right += 100;  // increase right hit area
            parent.setTouchDelegate(new TouchDelegate(rect, textPostUpload));
        });

        textPostUpload.setOnClickListener(view -> {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();


        });

        icBack.setOnClickListener(view -> {
            try {
                InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();


        });

        mspanable = postTextt.getText();

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        postTextt.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String startChar = null;

                try {
                    startChar = Character.toString(s.charAt(start));
                    Log.i(getClass().getSimpleName(), "CHARACTER OF NEW WORD: " + startChar);
                } catch (Exception ex) {
                    startChar = "";
                }

                if (startChar.equals("#")) {
                    changeTheColor(s.toString().substring(start), start, start + count);
                    hashTagIsComing++;
                }

                if (startChar.equals(" ")) {
                    hashTagIsComing = 0;
                }

                if (hashTagIsComing != 0) {
                    changeTheColor(s.toString().substring(start), start, start + count);
                    hashTagIsComing++;
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Auto-generated method stub

            }
        });

        tvLocation.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (tvLocation.getText().length() > 0) {
                    iv_cross.setVisibility(View.VISIBLE);
                } else {
                    iv_cross.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                //Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable s) {
                //Auto-generated method stub

            }
        });




        InputStream imageStream = null;
        try {
            imageStream = getContentResolver().openInputStream(uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Functions.toastMsg(PostUpload.this, "e" + e.toString());
        }
        final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
        Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), null, true);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
        mediaPath = bitmapToBase64(rotatedBitmap);

        try {
            ImageDecoder.Source source = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                source = ImageDecoder.createSource(this.getContentResolver(), uri);
                @SuppressLint("WrongThread") Bitmap bitmap = ImageDecoder.decodeBitmap(source);
                imageView.setImageBitmap(bitmap);
              //  urim = getImageUri(this,bitmap);
            } else {
                Bitmap mBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                imageView.setImageBitmap(mBitmap);
                //urim = getImageUri(this,mBitmap);

            }
        } catch (Exception v) {
            Functions.toastMsg(PostUpload.this, "" + v.toString());
        }

        postShare.setOnClickListener(view -> uploadPost());

        icBack.setOnClickListener(view -> {
            try {
                InputMethodManager imm1 = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                imm1.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
            finish();


        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isFABOpen) {
                    isFABOpen = false;
                    collapseFABMenu();
                } else {
                    isFABOpen = true;
                    expendFABMenu();
                }
            }
        });
        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadPost();
            }
        });
        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Uri uri = Uri.parse(mediaPath);

                Log.i("dfdffddf",uri+"");
                Uri uri = Uri.parse(mediaPath);
                uploadImageFirebase(uri,"name");
            }
        });
    }


    public void getLoggedInUserProfile(String userId, String fromWhere) {
        initVolleyCallback(fromWhere);
        mVolleyService = new VolleyService(mResultCallback, PostUpload.this);
        JSONObject sendObj = null;
        try {
            sendObj = new JSONObject();
            sendObj.put("user_id", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mVolleyService.postDataVolley("POSTCALL", API_LINKS.API_Get_User_detail, sendObj);
    }

    void initVolleyCallback(String fromWhere) {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.logDMsg(response.toString());
                Log.i("Dsdfvvvv",response.toString());
                try {
                    JSONObject msgObj = response.getJSONObject("msg");
                    JSONObject userObj = msgObj.getJSONObject("User");

                    try {
                        if (userObj.getString("image") != null && !userObj.getString("image").equals("")) {
                           /* Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("image"));
                            profilePic.setImageURI(uri);*/
                            Picasso.get().load(Constants.BASE_URL + userObj.getString("image"))
                                    .error(R.drawable.image_placeholder)
                                    .into(profileImage);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

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


    private void enablePermission() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GpsStatus = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!GpsStatus) {
            new GpsUtils(PostUpload.this).turnGPSOn(new GpsUtils.onGpsListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void gpsStatus(boolean isGPSEnable) {
                    if (ContextCompat.checkSelfPermission(PostUpload.this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(
                                new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                123);
                    }

                }
            });
        } else if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            getCurrentLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    123);
        }
    }

    private void getCurrentLocation() {
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(PostUpload.this);
        if (ContextCompat.checkSelfPermission(PostUpload.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(PostUpload.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        } else {
            mFusedLocationProviderClient.getLastLocation().addOnSuccessListener(PostUpload.this, location -> {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {

                    lat = (location.getLatitude());
                    lng = (location.getLongitude());
                    Geocoder geocoder = new Geocoder(PostUpload.this, Locale.getDefault());
                    try {
                        List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (addressList != null && addressList.size() > 0) {
                            String cityName = addressList.get(0).getLocality();
                            stLocation = addressList.get(0).getSubLocality();
                            if (stLocation != null && !stLocation.equals("")) {
                                tvLocation.setText(stLocation +" , "+cityName);
                            } else {
                                stLocation = cityName;
                                tvLocation.setText(cityName);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 202:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCurrentLocation();
                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                            && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                    } else {
                        Functions.customAlertDialogDenied(PostUpload.this,"location" ,new CallbackResponse() {
                            @Override
                            public void Responce(String resp) {
                                Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                intent.setData(Uri.parse(PACKAGE_URL_SCHEME + getPackageName()));
                                startActivity(intent);
                            }
                        });
                    }
                }
                break;
        }
    }


    public void uploadPost() {

        if (TextUtils.isEmpty(tvLocation.getText().toString())) {
            API_Calling_Methods.addNewPost(
                    "" + postTextt.getText().toString(),
                    "",
                    "" + "0.00000",
                    "" + "0.0000",
                    "" + SharedPrefrence.getUserIdFromJson(PostUpload.this),
                    "" + mediaPath,
                    PostUpload.this
            );
        } else {
            API_Calling_Methods.addNewPost(
                    "" + postTextt.getText().toString(),
                    "" + stLocation,
                    "" + lat,
                    "" + lng,
                    "" + SharedPrefrence.getUserIdFromJson(PostUpload.this),
                    "" + mediaPath,
                    PostUpload.this
            );
        }


        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void uploadImageFirebase(Uri uri, String filename) {
        String timeStamp = new SimpleDateFormat("yyyyHHmm",
                Locale.getDefault()).format(new Date());
        StorageReference storageReference = mStorageRef.child("images/Story_" + timeStamp + ".jpg");
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            // Get a URL to the uploaded content

            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 ->
                    callingStoryAPI(uri1.toString()));
            Toast.makeText(this, "dff", Toast.LENGTH_SHORT).show();

        }).addOnFailureListener(e ->
                Functions.toastMsg(this, "Failed " + e.toString())

        );
    }

    public void callingStoryAPI(String downloadUrl) {
        // Toast.makeText(getActivity(), "cx", Toast.LENGTH_SHORT).show();
        API_Calling_Methods.addStoryAPI("" + SharedPrefrence.getUserIdFromJson(this),
                "" + downloadUrl, "" + Variables.typeImg, this
        );

    }


    private void changeTheColor(String s, int start, int end) {
        mspanable.setSpan(new ForegroundColorSpan(ContextCompat.getColor(PostUpload.this, R.color.hashtag_color)), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
    }
    private void expendFABMenu(){
        fab1.setVisibility(View.VISIBLE);
        fab1.animate().translationY(-
                getResources().getDimension(R.dimen.standard_55));
        fab2.setVisibility(View.VISIBLE);
        fab2.animate().translationY(-
                getResources().getDimension(R.dimen.standard_105));
    }

    private void collapseFABMenu(){
        isFABOpen=false;
        fab1.setVisibility(View.GONE);
        fab1.animate().translationY(0);
        fab2.setVisibility(View.GONE);
        fab2.animate().translationY(0);

    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

}
