package com.geee.Photoeditor.event;

import android.view.MotionEvent;

import com.geee.Photoeditor.sticker.StickerView;

public interface StickerIconEvent {
    void onActionDown(StickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionMove(StickerView paramStickerView, MotionEvent paramMotionEvent);

    void onActionUp(StickerView paramStickerView, MotionEvent paramMotionEvent);
}
