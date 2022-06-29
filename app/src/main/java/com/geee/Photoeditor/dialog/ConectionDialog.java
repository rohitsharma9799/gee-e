package com.geee.Photoeditor.dialog;

import static android.content.Context.CONNECTIVITY_SERVICE;

import android.app.Activity;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConectionDialog  {

    private boolean isConnectedNetwork(Activity activity){
        boolean isWIFI = false;
        boolean isDATA = false;

        ConnectivityManager connectivityManager = (ConnectivityManager) activity.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        for(NetworkInfo info:networkInfos){
            if (info.getTypeName().equalsIgnoreCase("WIFI"))
                if (info.isConnected())
                    isWIFI = true;
            if (info.getTypeName().equalsIgnoreCase("MOBILE"))
                if (info.isConnected())
                    isDATA = true;
        }
        return isDATA||isWIFI;
    }


}



