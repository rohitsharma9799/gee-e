package com.geee.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.geee.Inner_VP_Package.Add_picture_Package.PostUpload;
import com.geee.R;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class FiltersA extends AppCompatActivity implements FiltersListFragment.FiltersListFragmentListener, EditImageFragment.EditImageFragmentListener {

    public static final String IMAGE_NAME = "";

    public static final int SELECT_GALLERY_IMAGE = 101;
    public static Bitmap croppedImage;

    // load native image filters library
    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @BindView(R.id.image_preview)
    ImageView imagePreview;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewpager)
    ViewPager viewPager;
    @BindView(R.id.coordinator_layout)
    CoordinatorLayout coordinatorLayout;
    @BindView(R.id.profileImage)
    ImageView back;
    @BindView(R.id.postImage)
    TextView nextText;
    @BindView(R.id.CropImageView)
    CropImageView mCropImageView;
    Bitmap originalImage;
    // to backup image with filter applied
    Bitmap filteredImage;
    // the final image after applying
    // brightness, saturation, contrast
    Bitmap finalImage;
    FiltersListFragment filtersListFragment;
    EditImageFragment editImageFragment;
    // modified image values
    int brightnessFinal = 0;
    float saturationFinal = 1.0f;
    float contrastFinal = 1.0f;
    TextView filterText;
    String image;

    @SuppressLint("WrongThread")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);
        ButterKnife.bind(this);
      /*  SharedPreferences sharedpreferences = getSharedPreferences("imageipload", Context.MODE_PRIVATE);
        image = sharedpreferences.getString("image","");*/

        /*Bitmap cropped = mCropImageView.getCroppedImage(1500,1500);
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        cropped.compress(Bitmap.CompressFormat.PNG, 100, bStream);
        byte[] byteArrayd = bStream.toByteArray();*/

        String filePath=getIntent().getStringExtra("image");

//loads the file
        File file = new File(filePath);
        Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());


      /*  String[] split = image.substring(1, image.length()-1).split(", ");
        byte[] byteArray = new byte[split.length];
        for (int i = 0; i < split.length; i++) {
            byteArray[i] = Byte.parseByte(split[i]);
        }*/
//        byte[] byteArray = getIntent().getByteArrayExtra("image");
        croppedImage = bitmap;/*BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);*/

      //  Log.i("dsfsdf",byteArray.toString());
        filterText = findViewById(R.id.tv_id_2);
        filterText.setOnClickListener(v -> finish());

        imagePreview.setImageBitmap(croppedImage);

        back.setOnClickListener(v -> finish());

        loadImage();
        nextText.setOnClickListener(v -> goToPostUpload());

        setupViewPager(viewPager);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        // adding filter list fragment
        filtersListFragment = new FiltersListFragment();
        filtersListFragment.setListener(this);

        // adding edit image fragment
        editImageFragment = new EditImageFragment();
        editImageFragment.setListener(this);

        adapter.addFragment(filtersListFragment, getString(R.string.tab_filters));
        adapter.addFragment(editImageFragment, getString(R.string.tab_edit));

        viewPager.setAdapter(adapter);
    }


    @Override
    public void onBrightnessChanged(final int brightness) {
        brightnessFinal = brightness;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightness));
        imagePreview.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onSaturationChanged(final float saturation) {
        saturationFinal = saturation;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new SaturationSubfilter(saturation));
        imagePreview.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onContrastChanged(final float contrast) {
        contrastFinal = contrast;
        Filter myFilter = new Filter();
        myFilter.addSubFilter(new ContrastSubFilter(contrast));
        imagePreview.setImageBitmap(myFilter.processFilter(finalImage.copy(Bitmap.Config.ARGB_8888, true)));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {
        // once the editing is done i.e seekbar is drag is completed,
        // apply the values on to filtered image
        final Bitmap bitmap = croppedImage.copy(Bitmap.Config.ARGB_8888, true);

        Filter myFilter = new Filter();
        myFilter.addSubFilter(new BrightnessSubFilter(brightnessFinal));
        myFilter.addSubFilter(new ContrastSubFilter(contrastFinal));
        myFilter.addSubFilter(new SaturationSubfilter(saturationFinal));
        finalImage = myFilter.processFilter(bitmap);
    }

    /**
     * Resets image edit controls to normal when new filter
     * is selected
     */

    private void resetControls() {
        if (editImageFragment != null) {
            editImageFragment.resetControls();
        }
        brightnessFinal = 0;
        saturationFinal = 1.0f;
        contrastFinal = 1.0f;
    }

    @Override
    public void onFilterSelected(Filter filter) {
        resetControls();

        // applying the selected filter
        filteredImage = croppedImage.copy(Bitmap.Config.ARGB_8888, true);
        // preview filtered image
        imagePreview.setImageBitmap(filter.processFilter(filteredImage));

        finalImage = filteredImage.copy(Bitmap.Config.ARGB_8888, true);
    }

    // load the default image from assets on app launch
    private void loadImage() {
        filteredImage = croppedImage.copy(Bitmap.Config.ARGB_8888, true);
        finalImage = croppedImage.copy(Bitmap.Config.ARGB_8888, true);
        imagePreview.setImageBitmap(croppedImage);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == SELECT_GALLERY_IMAGE) {
          //  Bitmap bitmap = BitmapUtils.getBitmapFromGallery(this, data.getData(), 800, 800);
            Uri imageUri = data.getData();
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }


            finalImage.recycle();


            finalImage.recycle();


            filteredImage = croppedImage.copy(Bitmap.Config.ARGB_8888, true);


            finalImage = croppedImage.copy(Bitmap.Config.ARGB_8888, true);


            imagePreview.setImageBitmap(croppedImage);


            bitmap.recycle();


            // render selected image thumbnails
            filtersListFragment.prepareThumbnail(croppedImage);


        }
    }

    private void goToPostUpload() {
        Uri uri = getImageUri(FiltersA.this, finalImage);
        SharedPreferences sharedpreferences = getSharedPreferences("filteruri", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("filteruri", uri.toString());
        editor.clear();
        editor.apply();
        Intent sendIntent = new Intent(FiltersA.this, PostUpload.class);
       // sendIntent.putExtra("media_path", uri.toString());
        startActivity(sendIntent);
        finish();


    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage,"IMG_" + Calendar.getInstance().getTime(),null);
        return Uri.parse(path);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }


}
