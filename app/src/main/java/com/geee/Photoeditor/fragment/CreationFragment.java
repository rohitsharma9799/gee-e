package com.geee.Photoeditor.fragment;



import static com.geee.Photoeditor.utils.SaveFileUtils.FolderPathShow;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.geee.R;
import com.geee.Photoeditor.activities.PreviewActivity;
import com.geee.Photoeditor.adapters.FileAdapter;
import com.geee.Photoeditor.listener.FileClickInterface;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.ArrayList;

public class CreationFragment extends Fragment implements FileClickInterface {
    private FileAdapter fileAdapter;
    private ArrayList<File> fileArrayList;
    LinearLayout linearLayoutHolder;
    RecyclerView recyclerViewStory;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    public void onAttach(@NotNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onResume() {
        super.onResume();
        getAllFiles();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.fragment_downloaded, container, false);
        recyclerViewStory = inflate.findViewById(R.id.recyclerViewStory);
        swipeRefreshLayout = inflate.findViewById(R.id.swipeRefreshLayout);
        linearLayoutHolder = inflate.findViewById(R.id.linearLayoutPlaceHold);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getAllFiles();
            swipeRefreshLayout.setRefreshing(false);
        });

        return inflate;
    }

    private void getAllFiles() {
        fileArrayList = new ArrayList<>();
        fileAdapter = new FileAdapter(getActivity(), fileArrayList, CreationFragment.this);
        recyclerViewStory.setAdapter(fileAdapter);

        File[] files = FolderPathShow.listFiles();
        if (files != null) {
            for (File file : files) {
                if (Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg")) {
                    fileArrayList.add(file);
                }
            }
            if (fileArrayList.size() > 0) {
                linearLayoutHolder.setVisibility(View.GONE);

                fileAdapter.notifyDataSetChanged();
            } else {
                linearLayoutHolder.setVisibility(View.VISIBLE);
                fileAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void getPosition(int position) {
        Intent inNext = new Intent(getActivity(), PreviewActivity.class);
        inNext.putExtra("ImageDataFile", fileArrayList);
        inNext.putExtra("Position", position);
        startActivity(inNext);
    }
}