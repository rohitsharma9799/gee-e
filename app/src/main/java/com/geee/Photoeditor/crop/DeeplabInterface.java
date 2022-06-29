package com.geee.Photoeditor.crop;

import android.content.Context;
import android.graphics.Bitmap;


public interface DeeplabInterface {
    int getInputSize();

    boolean initialize(Context context);

    boolean isInitialized();

    Bitmap segment(Bitmap bitmap);
}
