package com.geee.Volley_Package;

import android.content.Context;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.geee.CodeClasses.Variables;

import org.json.JSONObject;

public class VolleyService {

    IResult mResultCallback = null;
    Context mContext;

    public VolleyService(IResult resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }


    public void postDataVolley(final String requestType, String url, JSONObject sendObj) {
        try {
            RequestQueue queue = Volley.newRequestQueue(mContext);

            JsonObjectRequest jsonObj = new JsonObjectRequest(url, sendObj, response -> {
                if (mResultCallback != null)
                    mResultCallback.notifySuccess(requestType, response);
            }, error -> {
                if (mResultCallback != null)
                    mResultCallback.notifyError(requestType, error);
            });

            queue.add(jsonObj);
            jsonObj.setRetryPolicy(new DefaultRetryPolicy(
                    Variables.MY_SOCKET_TIMEOUT_MS,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
