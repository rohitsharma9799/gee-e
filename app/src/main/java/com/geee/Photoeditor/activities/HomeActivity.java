/*
package com.geee.Photoeditor.activities;



import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.geee.BuildConfig;
import com.geee.R;

import com.geee.Photoeditor.dialog.DetailsDialog;
import com.geee.Photoeditor.dialog.RateDialog;
import com.geee.Photoeditor.dialog.RemoteUpdateDialog;
import com.geee.Photoeditor.dialog.RemoteUpdateUtil;
import com.geee.Photoeditor.picker.ImageCaptureManager;
import com.geee.Photoeditor.picker.PhotoPicker;
import com.geee.Photoeditor.utils.AdsUtils;
import com.geee.Photoeditor.utils.PreferenceUtil;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

public class HomeActivity extends BaseActivity{
    private ImageCaptureManager imageCaptureManager;
    private static final int IMAGE_GALLERY_REQUEST = 20;
    public static Context contextApp;
    
    FirebaseRemoteConfig firebaseRemoteConfig;
    CoordinatorLayout coordinatorLayout;
    RelativeLayout bottomSheet;
    BottomSheetBehavior behavior;
    private static final int RC_SIGN_IN = 9001;
    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private GoogleSignInClient mGoogleSignInClient;
    private FirebaseAuth mAuth;
    public void onCreate(Bundle bundle) {
        
        super.onCreate(bundle);
        makeFullScreen();
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();
        contextApp = this.getApplicationContext();
        findViewById(R.id.imageViewSettings).setOnClickListener(new View.OnClickListener() {
            public final void onClick(View view) {
                Intent privacy = new Intent(HomeActivity.this, SettingsActivity.class);
                startActivity(privacy);
            }
        });
        findViewById(R.id.loginwithgoogle).setOnClickListener(this.onClickListener);
        findViewById(R.id.relativeLayoutEditor).setOnClickListener(this.onClickListener);
        findViewById(R.id.relativeLayoutCamera).setOnClickListener(this.onClickListener);
        findViewById(R.id.relativeLayoutCreation).setOnClickListener(this.onClickListener);
        this.imageCaptureManager = new ImageCaptureManager(this);
     
// Remote Config
        HashMap<String,Object> map = new HashMap<>();
        map.put(RemoteUpdateUtil.VERSION, BuildConfig.VERSION_CODE);
        firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();
        FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder().setMinimumFetchIntervalInSeconds(1).build();
        firebaseRemoteConfig.setConfigSettingsAsync(configSettings);
        firebaseRemoteConfig.setDefaultsAsync(map);
        firebaseRemoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                dialogUpdateShow();
            }
        });
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordinatorLayout);
        bottomSheet = findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // React to state change
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // React to dragging events
            }
        });
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
       SharedPreferences sharedpreferences = getActivity().getSharedPreferences(RootURL.PREFACCOUNT, MODE_PRIVATE);
       if (!sharedpreferences.getString("id","").equals("")){

            detailsliner.setVisibility(View.VISIBLE);
            editnumber.setText(sharedpreferences.getString("number",""));
            editname.setText(sharedpreferences.getString("username",""));
            editemail.setText(sharedpreferences.getString("email",""));

        }else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }, 500);
        }

        disconnectGoogle();
    }
    public void disconnectGoogle() {
        try {
            GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(HomeActivity.this)
                    .enableAutoManage(this, null)
                    .addConnectionCallbacks(null)
//                    .addApi(LocationServices.API)
                    .build();


            if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                mGoogleApiClient.clearDefaultAccountAndReconnect().setResultCallback(status -> {
                    mGoogleApiClient.disconnect();
                   // Toast.makeText(getApplicationContext(), "Disconnected", Toast.LENGTH_SHORT).show();
                });

            }
        } catch (Exception e) {
            Log.d("DISCONNECT ERROR", e.toString());
        }
    }
    public void dialogUpdateShow() {
        if (firebaseRemoteConfig.getLong(RemoteUpdateUtil.VERSION)<=BuildConfig.VERSION_CODE) return;
        RemoteUpdateDialog remoteUpdateDialog =  new RemoteUpdateDialog(this, firebaseRemoteConfig);
        remoteUpdateDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        remoteUpdateDialog.getWindow().setLayout(-1, -2);
        remoteUpdateDialog.show();
    }


    View.OnClickListener onClickListener = view -> {
        switch (view.getId()) {
            case R.id.relativeLayoutCamera:
                String[] arrPermissionsCamera = {"android.permission.CAMERA", "android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                if (Build.VERSION.SDK_INT >= 29)arrPermissionsCamera=new String[]{"android.permission.CAMERA"};
                Dexter.withContext(HomeActivity.this).withPermissions(arrPermissionsCamera)
                        .withListener(new MultiplePermissionsListener() {
                            public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                                if (multiplePermissionsReport.areAllPermissionsGranted()) {
                                    takePhotoFromCamera();
                                }
                                if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                                    DetailsDialog.showDetailsDialog(HomeActivity.this);
                                }
                            }

                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                                permissionToken.continuePermissionRequest();
                            }
                        }).withErrorListener(dexterError -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();
                return;
            case R.id.relativeLayoutEditor:
                String[] arrPermissionsEdit = {"android.permission.READ_EXTERNAL_STORAGE", "android.permission.WRITE_EXTERNAL_STORAGE"};
                if (Build.VERSION.SDK_INT >= 29)arrPermissionsEdit=new String[]{"android.permission.READ_EXTERNAL_STORAGE"};
                Dexter.withContext(HomeActivity.this).withPermissions(arrPermissionsEdit).withListener(new MultiplePermissionsListener() {
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted()) {
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_PICK);
                            intent.setType("image/*");
                            String[] mimetypes = {"image/*"};
                            intent.putExtra(Intent.EXTRA_MIME_TYPES,  mimetypes);
                            startActivityForResult(intent, IMAGE_GALLERY_REQUEST);
                        }
                        

                        if (multiplePermissionsReport.isAnyPermissionPermanentlyDenied()) {
                            DetailsDialog.showDetailsDialog(HomeActivity.this);
                        }
                    }

                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }

                }).withErrorListener(dexterError -> Toast.makeText(HomeActivity.this, "Error occurred! ", Toast.LENGTH_SHORT).show()).onSameThread().check();

                return;
            case R.id.relativeLayoutCreation:
  Intent intent= new Intent(HomeActivity.this, CreationActivity.class);
                startActivity(intent);

                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);

                return;
            case R.id.loginwithgoogle:
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
                return;
            default:
        }

    };

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getContentResolver().query(contentURI, null, null, null, null);
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


    public void onPostCreate(@Nullable Bundle bundle) {
        super.onPostCreate(bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != -1) {
            super.onActivityResult(requestCode, resultCode, data);
        } else if (data != null&&requestCode!=RC_SIGN_IN) {
            try {
                final Uri imageUri = data.getData();
                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                Intent intent2 = new Intent(getApplicationContext(), PhotoEditorActivity.class);
                intent2.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, getRealPathFromURI(imageUri));
                startActivity(intent2);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
                Toast.makeText(HomeActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            }

        } else if (requestCode == 1){
            if (this.imageCaptureManager == null) {
                this.imageCaptureManager = new ImageCaptureManager(this);
            }
           // new Handler(Looper.getMainLooper()).post(() -> HomeActivity.this.imageCaptureManager.galleryAddPic());
            Intent intent2 = new Intent(getApplicationContext(), PhotoEditorActivity.class);
            intent2.putExtra(PhotoPicker.KEY_SELECTED_PHOTOS, this.imageCaptureManager.getCurrentPhotoPath());
            startActivity(intent2);
        } else if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                //  Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());
                firebaseAuthWithGoogle(account.getIdToken(),account.getEmail());
            } catch (ApiException e) {
                // Utils.INSTANCE.hideProgressDialog();
                // Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
                // Log.w(TAG, "Google sign in failed", e);
            }
        }
    }

    public void onResume() {
        super.onResume();
        if (AdsUtils.isNetworkAvailabel(getApplicationContext())) {
            if (!PreferenceUtil.isRated(getApplicationContext()) && PreferenceUtil.getCounter(getApplicationContext()) % 6 == 0) {
                new RateDialog(this, false).show();
            }
            PreferenceUtil.increateCounter(getApplicationContext());
        }
    }

    public void takePhotoFromCamera() {
        try {
            startActivityForResult(this.imageCaptureManager.dispatchTakePictureIntent(), 1);
        } catch (IOException | ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onBackPressed() {
        showGoodBye();
    }

    public void showGoodBye() {
        startActivity(new Intent(HomeActivity.this, ThankYouActivity.class));
        finish();
    }
    // google


    private void firebaseAuthWithGoogle(String idToken,String email) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                      //  Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        //  Log.i("dfsdfsd",user.getEmail());
                        updateUI(user, "Google","gmail",email);
                    } else {
                        Log.w("", "signInWithCredential:failure", task.getException());
                    }
                });
    }

    private void updateUI(FirebaseUser firebaseUser, String provider,String acc,String email) {
        if (firebaseUser != null) {
            String userName = (firebaseUser.getDisplayName() == null) ? "" : firebaseUser.getDisplayName();
            Log.d("updateUI", "" + userName);
            String[] strArray = firebaseUser.getDisplayName().split(" ");

            // Log.d(" updateUI", "" + firebaseUser.getEmail());
            Log.d(" updateUI", "" + firebaseUser.getUid());
            try {
                String firstName, lastName, profile_url;
                if (strArray.length >= 2) {
                    firstName = "" + strArray[0];
                    lastName = "" + strArray[1];
                } else {
                    firstName = "" + firebaseUser.getDisplayName();
                    lastName = "";
                }

                if (firebaseUser.getPhotoUrl() != null) {
                    profile_url = firebaseUser.getPhotoUrl().toString();
                } else {
                    profile_url = "";
                }

                Log.i("dfdfdfdf",firstName+" :"+lastName+":"+email+":"+profile_url);

                UploadData(firstName+lastName,email,profile_url);
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            Log.e("FB Sign in error", "FB Sign in error");
        }
    }

    private void UploadData(String firstname, String email,String profileurl) {

        SharedPreferences sharedpreferences =getSharedPreferences("Pref", MODE_PRIVATE);
        final ProgressDialog progress = new ProgressDialog(HomeActivity.this);
        progress.setMessage("Please Wait..");
        progress.setCancelable(false);
        progress.show();
        RequestQueue requestQueue= Volley.newRequestQueue(HomeActivity.this);
        final String URLALL= "https://crickcoins.com/camcrows/NewApi/login";
        //----login_status fb -1 gm2 app--0
        Log.d("ABCsds",URLALL);
        StringRequest request=new StringRequest(Request.Method.POST,URLALL,
                new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        progress.dismiss();
                        behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                        Log.d("dsdsds",response);
 try
                        {
                            JSONObject jsonObject=new JSONObject(response);
                            if (jsonObject.getString("status").equals("1"))
                            {

                                JSONObject json = jsonObject.getJSONObject("info");
                                SharedPreferences sharedpreferences = getSharedPreferences("Pref", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedpreferences.edit();
                                editor.putString("username",json.getString("user_name"));
                                editor.putString("email",json.getString("user_email"));
                                editor.putString("number",json.getString("user_phone"));
                                editor.putString("profile" ,json.getString("user_profile_image"));
                                editor.commit();
                                Toast.makeText(HomeActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();


                            }
                            else
                            {
                                progress.dismiss();
                                Toast.makeText(HomeActivity.this,jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {


                    @Override
                    public void onErrorResponse(VolleyError error) {


                        progress.dismiss();
                        Toast.makeText(HomeActivity.this, "No internet connection"+error, Toast.LENGTH_LONG).show();

                    }
                }) {

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Log.i("image", image);

                Map<String, String> params = new Hashtable<String, String>();

                params.put("email", firstname);
                params.put("name", email);
                params.put("photo", profileurl);

                return params;
            }
        };
        requestQueue.add(request);


    }
}
*/
