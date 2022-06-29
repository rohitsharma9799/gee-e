package com.geee.Photoeditor.adapters;

import android.content.Context;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.geee.R;
import com.geee.Photoeditor.activities.PreviewActivity;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class PreviewAdapter extends PagerAdapter {
    private Context context;
    private ArrayList<File> files;
    private LayoutInflater inflater;
    PreviewActivity fullViewActivity;

    public PreviewAdapter(Context context, ArrayList<File> imageList, PreviewActivity fullViewActivity) {
        this.context = context;
        this.files = imageList;
        this.fullViewActivity=fullViewActivity;
        inflater = LayoutInflater.from(context);
    }


    @Override
    public int getItemPosition(Object object){
        return PagerAdapter.POSITION_NONE;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, @NotNull Object object) {
        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup view, final int position) {
        View imageLayout = inflater.inflate(R.layout.item_preview, view, false);
        assert imageLayout != null;
        final ImageView imageViewCover = imageLayout.findViewById(R.id.imageViewCover);
        Glide.with(context).load(files.get(position).getPath()).into(imageViewCover);
        view.addView(imageLayout, 0);

        return imageLayout;
    }

    @Override
    public int getCount() {
        return files.size();
    }

    @Override
    public boolean isViewFromObject(View view, @NotNull Object object) {
        return view.equals(object);
    }

    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
    }

    @Override
    public Parcelable saveState() {
        return null;
    }
}
