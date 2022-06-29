package com.geee.CodeClasses;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

public class RV_item_Decoration extends RecyclerView.ItemDecoration {
    private int space;

    public RV_item_Decoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.top = 0;
        outRect.bottom = space;

    }
}