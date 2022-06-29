package com.geee.Inner_VP_Package.Profile_Pacakge;

import android.Manifest;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.core.widget.NestedScrollView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.login.LoginManager;
import com.flaviofaria.kenburnsview.KenBurnsView;
import com.geee.BuildConfig;
import com.geee.Inner_VP_Package.Profile_Pacakge.Story_Package.DeleteFragment;
import com.geee.Menu.AboutUs;
import com.geee.Menu.Contactus;
import com.geee.Menu.Feedback;
import com.geee.Menu.HelpDesk;
import com.geee.Menu.PrivacyPolicy;
import com.geee.Menu.TermsCondition;
import com.geee.tictokcode.Profile.UserVideos.UserVideo_F;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.geee.tictokcode.Video_Recording.GallerySelectedVideo.GallerySelectedVideo_A;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.geee.BackStack.RootFragment;
import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.CustomViewPagerProfile;
import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.CodeClasses.ViewPager_Adp;
import com.geee.Constants;
import com.geee.Inner_VP_Package.Profile_Pacakge.Edit_Profile_Package.EditProfile;
import com.geee.Inner_VP_Package.Profile_Pacakge.Follower_Following.FollowersFollowingF;
import com.geee.Inner_VP_Package.Profile_Pacakge.Grid_Layout.Grid_Posts;
import com.geee.Inner_VP_Package.Profile_Pacakge.Linear_Layout.MyPostLinear;
import com.geee.Inner_VP_Package.Profile_Pacakge.SavedPackage.SavedActivity;
import com.geee.Inner_VP_Package.Profile_Pacakge.Tag_Layout.Tag_Posts;
import com.geee.Inner_VP_Package.Web_View;
import com.geee.Main_VP_Package.Main;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.Volley_Package.API_Calling_Methods;
import com.geee.Volley_Package.IResult;
import com.geee.Volley_Package.VolleyService;
/*import com.gowtham.library.ui.ActVideoTrimmer;
import com.gowtham.library.utils.TrimmerConstants;*/
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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.facebook.FacebookSdk.getApplicationId;
import static com.geee.CodeClasses.Variables.mResultCallback;
import static com.geee.CodeClasses.Variables.mVolleyService;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends RootFragment {

    private final int PICK_IMAGE_CAMERA = 25, PICK_IMAGE_GALLERY = 28,PICK_COVER_CAMERA = 3, PICK_COVER_GALLERY = 4;

    public static RelativeLayout profRl;
    public static DrawerLayout drawerLayout;
    View view;
    TabLayout tl;
    CustomViewPagerProfile customViewPager;
    ViewPager_Adp viewPagerAdp;
    Button btn;
    NestedScrollView nestedScrollView;
    LinearLayout followersLayout, followingLayout,editprofilesd;
    Boolean isvisible = true;
    ImageView iv;
    CircleImageView profilePic;
    FrameLayout frameLayout;
    LinearLayout savedLl, logoutMe, postLayout;
    TextView drawerUserName, userWebsite;
    String userProfileResponse, userNameText;
    TextView numOfPosts, numOfFollowrs, numOfFollowing, userName, userNameTop, userInfo,username;
    AppBarLayout mainAppbarLayout;
    private StorageReference mStorageRef;
    private String imageFilePath, userId;
    private InputStream inputStreamImg;
    KenBurnsView coverimage;
    String tictokid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getContext()).inflate(R.layout.profile, null);

        mStorageRef = FirebaseStorage.getInstance().getReference();
        getLoggedInUserProfile(SharedPrefrence.getUserIdFromJson(getActivity()), "fromCreate");
        numOfPosts = view.findViewById(R.id.num_of_posts);
        coverimage = view.findViewById(R.id.coverimage);
        numOfFollowing = view.findViewById(R.id.num_of_following);
        numOfFollowrs = view.findViewById(R.id.num_of_followers);
        userName = view.findViewById(R.id.tv_tag);
        userNameTop = view.findViewById(R.id.tv_fullname);
        userInfo = view.findViewById(R.id.user_info);
        postLayout = view.findViewById(R.id.postLayout);
        mainAppbarLayout = view.findViewById(R.id.mainAppbarLayout);

        //Menu
        drawerLayout = view.findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        username = view.findViewById(R.id.username);
        drawerUserName = view.findViewById(R.id.drawer_user_name);
        logoutMe = view.findViewById(R.id.logout_me);
        savedLl = view.findViewById(R.id.saved_ll_id);
        userWebsite = view.findViewById(R.id.user_website);
        drawerUserName.setText("" + SharedPrefrence.getUserNameFromJson(getContext()));
        editprofilesd = view.findViewById(R.id.editprofilesd);

        userWebsite.setOnClickListener(v -> {
            Web_View webview_f = new Web_View();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.setCustomAnimations(R.anim.in_from_bottom, R.anim.out_to_top, R.anim.in_from_top, R.anim.out_from_bottom);
            Bundle bundle = new Bundle();
            bundle.putString("url", userWebsite.getText().toString());
            bundle.putString("title", userWebsite.getText().toString());
            webview_f.setArguments(bundle);
            transaction.addToBackStack(null);
            transaction.replace(R.id.main_container, webview_f).commit();
        });

        editprofilesd.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), EditProfile.class);
            myIntent.putExtra("user_id", SharedPrefrence.getUserIdFromJson(getContext())); //Optional parameters
            startActivity(myIntent);
            drawerLayout.closeDrawers();
        });

        logoutMe.setOnClickListener(v -> {
            final Dialog dialog = new Dialog(getActivity());

            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.item_yesno_dialouge);
            dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.d_round_corner_white_bkg));
            TextView title = dialog.findViewById(R.id.title);
            TextView des = dialog.findViewById(R.id.des);
            title.setText("LogOut");
            des.setText("Do you want to LogOut your account?");
            Button okBtn = dialog.findViewById(R.id.ok_btn);
            okBtn.setText("Continue");
            Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
            okBtn.setOnClickListener(view -> {
                dialog.dismiss();
              //  AccessToken.getCurrentAccessToken().getToken();
               // LoginManager.getInstance().logOut();

                FacebookSdk.sdkInitialize(getActivity());
                new GraphRequest(AccessToken.getCurrentAccessToken(), "/me/permissions/", null, HttpMethod.DELETE, new GraphRequest
                        .Callback() {
                    @Override
                    public void onCompleted(GraphResponse graphResponse) {
                        LoginManager.getInstance().logOut();

                    }
                }).executeAsync();
                SharedPrefrence.logoutUser(getContext());
                Intent intent = new Intent(getContext(), Main.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                drawerLayout.closeDrawers();
            });
            cancelBtn.setOnClickListener(view -> {
                dialog.dismiss();
            });
            dialog.show();

        });

        postLayout.setOnClickListener(v -> {
            if (mainAppbarLayout.getTop() < 0)
                mainAppbarLayout.setExpanded(false);
            else
                mainAppbarLayout.setExpanded(false);
        });

        savedLl.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), SavedActivity.class);
            startActivity(myIntent);
            drawerLayout.closeDrawers();

        });
        LinearLayout helpdesk = view.findViewById(R.id.helpdesk);

        helpdesk.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), Contactus.class);
            startActivity(myIntent);
            drawerLayout.closeDrawers();

        });
        LinearLayout terms = view.findViewById(R.id.terms);

        terms.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), TermsCondition.class);
            startActivity(myIntent);
            drawerLayout.closeDrawers();

        });
        LinearLayout about = view.findViewById(R.id.aboutus);

        about.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), AboutUs.class);
            startActivity(myIntent);
            drawerLayout.closeDrawers();

        });
        LinearLayout feedback = view.findViewById(R.id.feedback);

        feedback.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), Feedback.class);
            startActivity(myIntent);
            drawerLayout.closeDrawers();

        });
        LinearLayout changepass = view.findViewById(R.id.changepass);

        changepass.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), Changepassword.class);
            startActivity(myIntent);
            drawerLayout.closeDrawers();

        });


        LinearLayout privacy = view.findViewById(R.id.privacy);

        privacy.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), PrivacyPolicy.class);
            startActivity(myIntent);
            drawerLayout.closeDrawers();

        });
        LinearLayout rate = view.findViewById(R.id.rate);

        rate.setOnClickListener(v -> {
            final String appPackageName = getActivity().getPackageName();

            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }

        });
        LinearLayout website = view.findViewById(R.id.website);

        website.setOnClickListener(v -> {
            Uri uri = Uri.parse("http://www.gee-e.com");
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);

        });
        LinearLayout share = view.findViewById(R.id.share);

        share.setOnClickListener(v -> {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT,
                    "Hey check out my app at: https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

        });
        userId = SharedPrefrence.getUserIdFromJson(getActivity());
        iv = view.findViewById(R.id.postImage);
        frameLayout = view.findViewById(R.id.prof_fl_id);
        isvisible = false;

        profilePic = view.findViewById(R.id.user_prof_img_id);


        userProfileResponse = SharedPrefrence.getOffline(getContext(),""+ SharedPrefrence.share_user_profile_pic);

        if (userProfileResponse != null) {
            // If user profile is not null
            try {
                JSONObject userProfile = new JSONObject(userProfileResponse);
                JSONObject userObj = userProfile.getJSONObject("msg").getJSONObject("User");
                userObj.getString("image");

                if (userObj.getString("image") != null && !userObj.getString("image").equals("")) {
               /*     Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("image"));
                    profilePic.setImageURI(uri);*/
                    Picasso.get().load(Constants.BASE_URL + userObj.getString("image"))
                            .error(R.drawable.image_placeholder)
                            .into(profilePic);
                    Log.i("ursdhsi",profilePic.toString());
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        //OnbtnSelectImage click event...
        profilePic.setOnClickListener(v -> selectImaged());

        tl = view.findViewById(R.id.tablayout_id);
        customViewPager = view.findViewById(R.id.prof_viewpager_id);
        nestedScrollView = view.findViewById(R.id.prof_nestedscrollview_id);
        viewPagerAdp = new ViewPager_Adp(getChildFragmentManager());

        profRl = view.findViewById(R.id.prof_tb_rl_id);
       // epRl = view.findViewById(R.id.ep_tb_rl_id);

        followersLayout = view.findViewById(R.id.followers_layout);
        followingLayout = view.findViewById(R.id.followingLayout);

        btn = view.findViewById(R.id.message_btn);
        iv.setOnClickListener(v -> methodDrawerLayout());

        drawerLayout.setDrawerElevation(0);

        followersLayout.setOnClickListener(v -> {

            Bundle bundleLinearPosts = new Bundle();
            bundleLinearPosts.putString("user_id", "" + SharedPrefrence.getUserIdFromJson(getContext()));
            bundleLinearPosts.putString("fromWhere", "followers");

            // set Fragmentclass Arguments
            FollowersFollowingF followersFollowing = new FollowersFollowingF();
            followersFollowing.setArguments(bundleLinearPosts);

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.main_container, followersFollowing).commit();
        });

        followingLayout.setOnClickListener(v -> {
            Bundle bundleLinearPosts = new Bundle();
            bundleLinearPosts.putString("user_id", "" + SharedPrefrence.getUserIdFromJson(getContext()));
            bundleLinearPosts.putString("fromWhere", "following");
            // set Fragmentclass Arguments
            FollowersFollowingF followersFollowing = new FollowersFollowingF();
            followersFollowing.setArguments(bundleLinearPosts);

            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.addToBackStack(null);
            ft.replace(R.id.main_container, followersFollowing).commit();
        });

        btn.setOnClickListener(v -> {
            Intent myIntent = new Intent(getContext(), EditProfile.class);
            myIntent.putExtra("user_id", SharedPrefrence.getUserIdFromJson(getContext())); //Optional parameters
            startActivity(myIntent);
        });

        final String userId = "" + SharedPrefrence.getUserIdFromJson(getContext());


        Bundle bundle = new Bundle();
        bundle.putString("user_id", "" + userId);
        bundle.putString("fromWhere", "" + "profile");
        Grid_Posts gridPosts = new Grid_Posts();
        gridPosts.setArguments(bundle);
        // End Send parameters to Grid Layout

        // Send values to Linear posts fragmwnt
        Bundle bundleLinearPosts = new Bundle();
        bundleLinearPosts.putString("user_id", "" + tictokid );
        bundleLinearPosts.putString("fromWhere", "" + "profile");
        UserVideo_F objLinearPosts = new UserVideo_F();
        objLinearPosts.setArguments(bundle);
        // End Send parameters to Linear posts

        // Send values to Tag Posts
        Bundle bundleTagsPosts = new Bundle();
        bundleTagsPosts.putString("user_id", "" + userId);
        bundleTagsPosts.putString("fromWhere", "" + "profile");
        Tag_Posts objTagPosts = new Tag_Posts();
        objTagPosts.setArguments(bundle);
        // End Send parameters to Linear posts


        nestedScrollView.setFillViewport(true);


        viewPagerAdp.add_fragment(gridPosts, "Grid");
        viewPagerAdp.add_fragment(objLinearPosts, "Videos");
        viewPagerAdp.add_fragment(objTagPosts, "Tag");
        customViewPager.setAdapter(viewPagerAdp);
        customViewPager.setOffscreenPageLimit(3);

        tl.setupWithViewPager(customViewPager);

        final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView1 = (ImageView) view1.findViewById(R.id.imageview_id);
        imageView1.setColorFilter(getActivity().getResources().getColor(R.color.white));
        imageView1.setImageResource(R.drawable.galleryslect);
        tl.getTabAt(0).setCustomView(view1);

        final View view2 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView2 = (ImageView) view2.findViewById(R.id.imageview_id);
        imageView2.setColorFilter(getActivity().getResources().getColor(R.color.white));
        imageView2.setImageResource(R.drawable.play);
        tl.getTabAt(1).setCustomView(view2);

        final View view3 = LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
        ImageView imageView3 = (ImageView) view3.findViewById(R.id.imageview_id);
        imageView3.setColorFilter(getActivity().getResources().getColor(R.color.white));
        imageView3.setImageResource(R.drawable.tag);
        tl.getTabAt(2).setCustomView(view3);

        tl.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.imageview_id);
                switch (tab.getPosition()) {
                    case 0:
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.galleryslect);
                        break;
                    case 1:
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.playselct);
                        break;
                    case 2:
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.tagselect);
                        drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                        break;

                    default:
                        break;
                }
                tab.setCustomView(view);
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                View view = tab.getCustomView();
                ImageView image = view.findViewById(R.id.imageview_id);
                switch (tab.getPosition()) {
                    case 0:
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.gallerycam);
                        break;
                    case 1:
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.play);
                        break;
                    case 2:
                        image.setColorFilter(getActivity().getResources().getColor(R.color.white));
                        image.setImageResource(R.drawable.tag);
                        break;

                    default:
                        break;
                }
                tab.setCustomView(view);
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                //auto generated method
            }
        });

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
                methodDrawerLayout();
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //auto generated method
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                //auto generated method
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                //auto generated method
            }
        });


        customViewPager.setOnSwipeOutListener(new CustomViewPagerProfile.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {
            }

            @Override
            public void onSwipeOutAtEnd() {
                Log.d("TagTest", "position : end");
                methodDrawerLayout();
            }
        });

      /*  ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        coverimage.setColorFilter(new ColorMatrixColorFilter(matrix));*/
        return view;
    }

    private void selectImaged() {
        try {
            PackageManager pm = getActivity().getPackageManager();
            int hasPerm = pm.checkPermission(Manifest.permission.CAMERA, getActivity().getPackageName());
            if (hasPerm == PackageManager.PERMISSION_GRANTED) {
                final CharSequence[] options = {"Take Photo", "Choose From Gallery", "Cancel"};
                androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(getActivity());
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
                Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(getActivity(), "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);
        if (menuVisible) {
            int count = customViewPager.getCurrentItem();
            Fragment fragment = viewPagerAdp.getRegisteredFragment(count);
            if (fragment instanceof Grid_Posts) {
                ((Grid_Posts) fragment).getUserPostsMedia();
            }
            if (fragment instanceof MyPostLinear) {
                ((MyPostLinear) fragment).getUserPosts();
            }
            getLoggedInUserProfile(userId, "fromMenu");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLoggedInUserProfile(userId, "fromCreate");
    }

    private void methodDrawerLayout() {
        if (drawerLayout.isDrawerOpen(GravityCompat.END)) {
            drawerLayout.closeDrawer(GravityCompat.END);
        } else {
            drawerLayout.openDrawer(GravityCompat.END);
        }
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, R.string.open, R.string.close) {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                float slideX = drawerView.getWidth() * slideOffset;
                frameLayout.setTranslationX(slideX * -1);
            }
        };
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
    }


    // Select image from camera and gallery
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
                        transaction.add(R.id.mainrsd, comment_f).commit();

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
        Intent pictureIntent = new Intent(
                MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
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
        Functions.showLoader(getActivity(),false,true);
        Toast.makeText(getActivity(), "Please wait...", Toast.LENGTH_SHORT).show();

        if (resultCode == RESULT_CANCELED) {
            return;
        }
        if (requestCode == PICK_IMAGE_GALLERY) {
            if (data != null) {
                Uri selectedImage = data.getData();
                CropImage.activity(selectedImage)
                        .setAspectRatio(1, 1)
                        .start(getActivity(),Profile.this);
            }
        }
        if (requestCode == PICK_IMAGE_CAMERA) {

           // if (data != null) {
                Uri selectedImage = (Uri.fromFile(new File(imageFilePath)));
                //  CropImage.activity(selectedImage).start(EditProfile.this);
               // profilePic.setImageURI(selectedImage);
                Picasso.get().load(selectedImage).into(profilePic);
                InputStream imageStream = null;
                try {
                    imageStream =getActivity().getContentResolver().openInputStream(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), null, true);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                callApiUploadImage(baos);
            //}
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri selectedImage = result.getUri();
                Functions.logDMsg("resultUri : " + selectedImage);
                if (data != null) {
                   // profilePic.setImageURI(selectedImage);
                    Picasso.get().load(selectedImage).into(profilePic);
                    InputStream imageStream = null;
                    try {
                        imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    final Bitmap imagebitmap = BitmapFactory.decodeStream(imageStream);
                    Bitmap rotatedBitmap = Bitmap.createBitmap(imagebitmap, 0, 0, imagebitmap.getWidth(), imagebitmap.getHeight(), null, true);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    rotatedBitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    callApiUploadImage(baos);

                }
            } }

        }



       /* if (resultCode == getActivity().RESULT_CANCELED) {
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
        } else if (requestCode == com.geee.tictokcode.SimpleClasses.Variables.Pick_video_from_gallery) {
            Uri uri = data.getData();
            try {


              *//*  Intent intent=new Intent(getActivity(), ActVideoTrimmer.class);
                intent.putExtra(TrimmerConstants.TRIM_VIDEO_URI, String.valueOf(uri));
                intent.putExtra(TrimmerConstants.DESTINATION, com.geee.tictokcode.SimpleClasses.Variables.app_hided_folder);
                intent.putExtra(TrimmerConstants.TRIM_TYPE,1);
                intent.putExtra(TrimmerConstants.FIXED_GAP_DURATION,18L); //in secs
                startActivityForResult(intent,TrimmerConstants.REQ_CODE_VIDEO_TRIMMER);*//*

                    *//*
                    File video_file = FileUtils.getFileFromUri(this, uri);

                    if (Functions.getfileduration(this,uri) < Variables.max_recording_duration) {
                        Chnage_Video_size(video_file.getAbsolutePath(), Variables.gallery_resize_video);

                    } else {
                        Intent intent=new Intent(Video_Recoder_A.this,Trim_video_A.class);
                        intent.putExtra("path",video_file.getAbsolutePath());
                        startActivity(intent);
                    }
                    *//*

            } catch (Exception e) {
                e.printStackTrace();
            }*/


        //}
        /*else if(requestCode ==TrimmerConstants.REQ_CODE_VIDEO_TRIMMER){
            String path= data.getStringExtra(TrimmerConstants.TRIMMED_VIDEO_PATH);
            Chnage_Video_size(path, com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
        }*/

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
        ApiRequest.Call_Api(getActivity(), com.geee.tictokcode.SimpleClasses.Variables.uploadImage, parameters, new Callback() {
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
                        API_Calling_Methods.addUserImage("" + userId, "" + finalBase6, getActivity());
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

 /*   public  void Call_Api_For_image(String s) {
          Toast.makeText(getActivity(), "callimage", Toast.LENGTH_SHORT).show();
        JSONObject parameters = null;
        try {
            parameters = new JSONObject();
            JSONObject file_data=new JSONObject();
            parameters.put("fb_id", com.geee.tictokcode.SimpleClasses.Variables.sharedPreferences.getString(com.geee.tictokcode.SimpleClasses.Variables.u_id,"0"));
            file_data.put("file_data",s);
            parameters.put("image",file_data);
            Log.i("dfsdfsdfsdfsdf",parameters.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("dffsdfsfssdfsdfsdfsdf",e.toString());
        }
        ApiRequest.Call_Api(getActivity(), com.geee.tictokcode.SimpleClasses.Variables.uploadImage, parameters, new Callback() {
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

                    *//*    if(Variables.user_pic!=null && !Variables.user_pic.equals(""))
                            Picasso.get()
                                    .load(Variables.user_pic)
                                    .placeholder(context.getResources().getDrawable(R.drawable.profile_image_placeholder))
                                    .resize(200,200).centerCrop().into(profile_image);*//*

                        Toast.makeText(getActivity(), "Image Update Successfully", Toast.LENGTH_SHORT).show();
                        Intent start = new Intent(getActivity(), Main.class);
                        startActivity(start);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }*/
    public void getLoggedInUserProfile(String userId, String fromWhere) {
        initVolleyCallback(fromWhere);
        mVolleyService = new VolleyService(mResultCallback, getContext());
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
    void initVolleyCallback(String fromWhere) {
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType, JSONObject response) {
                Functions.logDMsg(response.toString());
                Log.i("Dsdfvvvv",response.toString());
                try {
                    JSONObject msgObj = response.getJSONObject("msg");
                    JSONObject userObj = msgObj.getJSONObject("User");

                    userNameText = userObj.getString("full_name");

                    numOfPosts.setText("" + userObj.getString("posts"));
                    numOfFollowrs.setText("" + userObj.getString("followers"));

                    numOfFollowing.setText("" + userObj.getString("following"));
                    username.setText("" + userObj.getString("username"));
                    if (fromWhere.equals("fromCreate")) {
                        userName.setText("" + userObj.getString("full_name"));
                        userNameTop.setText("" + userObj.getString("username"));
                        if (userObj.getString("bio") != null && !userObj.getString("bio").equals("")) {
                            userInfo.setText("" + userObj.getString("bio"));
                            userInfo.setVisibility(View.VISIBLE);
                        }


                        if (userObj.getString("website") != null && !userObj.getString("website").equals("")) {
                            userWebsite.setText("" + userObj.getString("website"));
                        //    userWebsite.setVisibility(View.VISIBLE);
                        }

                        if (userObj.getString("image") != null && !userObj.getString("image").equals("")) {
                           /* Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("image"));
                            profilePic.setImageURI(uri);*/
                            Picasso.get().load(Constants.BASE_URL + userObj.getString("image"))
                                    .error(R.drawable.image_placeholder)
                                    .into(profilePic);
                        }

                        if (userObj.getString("cover_image") != null && !userObj.getString("cover_image").equals("")) {
//                            Picasso.get().load(userObj.getString("cover_image")).into(coverimage);
                            Uri uri = Uri.parse(Constants.BASE_URL + userObj.getString("cover_image"));
                            Glide.with(getActivity()).load(uri).into(coverimage);
                        }
                        tictokid = userObj.getString("tictic_id");

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

    // End get Logged In user Profile


    public void uploadImageFirebase(Uri uri, String filename) {
        Functions.logDMsg("params at addStoryAPI  uploadImageFirebase");
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        StorageReference storageReference = mStorageRef.child("images/Story_" + timeStamp + ".jpg");
        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {

            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(uri1 ->
                    callingStoryAPI(uri1.toString()));
        }).addOnFailureListener(e -> Functions.toastMsg(getContext(), "Failed " + e.toString()));
    }

    public void callingStoryAPI(String downloadUrl) {
        Functions.logDMsg("params at addStoryAPI  callingStoryAPI");
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

           /* Intent intent=new Intent(getActivity(), GallerySelectedVideo_A.class);
            intent.putExtra("video_path", com.geee.tictokcode.SimpleClasses.Variables.gallery_resize_video);
            startActivity(intent);*/

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
}
