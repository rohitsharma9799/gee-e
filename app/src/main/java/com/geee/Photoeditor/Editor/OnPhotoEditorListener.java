package com.geee.Photoeditor.Editor;

public interface OnPhotoEditorListener {
    void onAddViewListener(ViewType viewType, int i);


    void onRemoveViewListener(int i);

    void onRemoveViewListener(ViewType viewType, int i);

    void onStartViewChangeListener(ViewType viewType);

    void onStopViewChangeListener(ViewType viewType);
}
