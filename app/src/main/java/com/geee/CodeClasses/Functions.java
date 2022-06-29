package com.geee.CodeClasses;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.geee.Utils.MyApplication;
import com.gmail.samehadar.iosdialog.CamomileSpinner;
import com.geee.Chat.Chat_GetSet;
import com.geee.Constants;
import com.geee.Interface.CallbackResponse;
import com.geee.R;
import com.geee.Utils.TimeAgo2;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import static android.content.Context.DOWNLOAD_SERVICE;

public class Functions {

    static Dialog dialog1;

    public static void logDMsg(String Msg) {
        if (!Constants.isSecureinfo)
            Log.d(Constants.TAG, Msg);
    }

    public static Matrix getOrentation(String path) {
        Matrix matrix = new Matrix();
        ExifInterface exif = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            try {
                exif = new ExifInterface(path);
                int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                switch (orientation) {
                    case ExifInterface.ORIENTATION_ROTATE_90:
                        matrix.postRotate(90);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_180:
                        matrix.postRotate(180);
                        break;
                    case ExifInterface.ORIENTATION_ROTATE_270:
                        matrix.postRotate(270);
                        break;

                    default:
                        return matrix;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return matrix;

    }

    public static void toastMsg(Context context, String msg) {
        Toast.makeText(context, "" + msg, Toast.LENGTH_SHORT).show();
    }

    public static boolean isValidEmail(CharSequence target) {
        return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    //This method will show loader when getting data from server
    public static void showLoader(Context context, boolean outside_touch, boolean cancleable) {

        dialog1 = new Dialog(context);
        dialog1.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.setContentView(R.layout.loader_dialog);
        //Display display = ((Activity) context).getWindowManager().getDefaultDisplay();
     /*   CamomileSpinner loader = dialog1.findViewById(R.id.loader);
        loader.start();*/

        if (!outside_touch)
            dialog1.setCanceledOnTouchOutside(false);

        if (!cancleable)
            dialog1.setCancelable(false);

        dialog1.show();

    }


    //This method will cancel the running loader
    public static void cancelLoader() {
        if (dialog1 != null) {
            dialog1.cancel();
            dialog1.dismiss();
        }
    }


    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("isMyServiceRunning?", true + "");
                return true;
            }
        }
        Log.i("isMyServiceRunning?", false + "");
        return false;
    }


    private static String downloadStatus(Cursor cursor, long DownloadId) {

        //column for download  status
        int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
        int status = cursor.getInt(columnIndex);
        //column for reason code if the download failed or paused
        int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);

        switch (status) {
            case DownloadManager.STATUS_FAILED:
                return "STATUS_FAILED";

            case DownloadManager.STATUS_PAUSED:
                return "";

            case DownloadManager.STATUS_PENDING:
                return "";

            case DownloadManager.STATUS_RUNNING:
                return "";

            case DownloadManager.STATUS_SUCCESSFUL:
                return "STATUS_SUCCESSFUL";
            default:
                return "none";
        }


    }


    public static String checkImageStatus(Context context, long Image_DownloadId) {
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Query ImageDownloadQuery = new DownloadManager.Query();

        //set the query filter to our previously Enqueued download
        ImageDownloadQuery.setFilterById(Image_DownloadId);

        //Query the download manager about downloads that have been requested.
        Cursor cursor = downloadManager.query(ImageDownloadQuery);
        if (cursor.moveToFirst()) {
            return downloadStatus(cursor, Image_DownloadId);
        }
        return "";
    }


    public static long downloadDataForChat(Context context, Chat_GetSet item) {

        long downloadReference;
        // Create request for android download manager
        DownloadManager downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(Uri.parse(item.getPic_url()));

        //Setting title of request
        request.setTitle(item.getSender_name());

        //Setting description of request
        if (item.getType().equals("video")) {
            request.setDescription("Downloading Video");
            request.setDestinationUri(Uri.fromFile(new File(MyApplication.getAppContext().getExternalFilesDir(null).getAbsolutePath() + "/Chatbuzz/" + item.getChat_id() + ".mp4")));
        } else if (item.getType().equals("audio")) {
            request.setDescription("Downloading Audio");
            request.setDestinationUri(Uri.fromFile(new File(MyApplication.getAppContext().getExternalFilesDir(null).getAbsolutePath() + "/Chatbuzz/" + item.getChat_id() + ".mp3")));
        } else if (item.getType().equals("pdf")) {

            request.setDescription("Downloading Pdf Document");
            request.setDestinationUri(Uri.fromFile(new File(MyApplication.getAppContext().getExternalFilesDir(null).getAbsolutePath() + "/Chatbuzz/" + item.getChat_id() + ".pdf")));

        }


        downloadReference = downloadManager.enqueue(request);

        return downloadReference;
    }


    /// Alert Dialogue
    public static void alertDialogue(final Context context, String title, String msg) {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
        builder1.setTitle("" + title);
        builder1.setMessage(msg);
        builder1.setCancelable(true);

        builder1.setPositiveButton("Ok", (dialog, id) -> dialog.cancel());

        AlertDialog alert11 = builder1.create();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            alert11.show();
        } else {
            alert11.show();
        }


    }

    public static void customAlertDialogDenied(Context context,String fromWhere, final CallbackResponse callbackResponse) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_alert_dialouge_location_denied);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.d_round_corner_white_bkg));

        Button okBtn = dialog.findViewById(R.id.ok_btn);
        Button okCancel = dialog.findViewById(R.id.cancel_btn);
        TextView tvTwo = dialog.findViewById(R.id.tvTwo);

        if(fromWhere.equals("location")){
            tvTwo.setText(context.getResources().getString(R.string.location_heading_denied_step_2));
        }else {
            tvTwo.setText(context.getResources().getString(R.string.location_heading_denied_step_2_gallery));
        }

        okBtn.setOnClickListener(view -> {
            callbackResponse.Responce("okay");
            dialog.dismiss();
        });

        okCancel.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }


    //get the permission to record audio
    public static boolean checkPermissions(Activity context) {

        String[] PERMISSIONS = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        if (!hasPermissions(context, PERMISSIONS)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                context.requestPermissions(PERMISSIONS, 2);
            }
        } else {

            return true;
        }

        return false;
    }


    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }


    //////////////Open Soft Keyboard
    public static void showSoftKeyboard(View view, Context context) {
        if (view.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    ///////// Hide Soft Keyboard
    public static void hideSoftKeyboard(FragmentActivity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    // this function will print the keyhash of your project
    public static void printKeyHash(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("keyhashold", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            cursor.close();
            return Uri.withAppendedPath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "" + id);
        } else {
            if (imageFile.exists()) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    ContentResolver resolver = context.getContentResolver();
                    Uri picCollection = MediaStore.Images.Media
                            .getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
                    ContentValues picDetail = new ContentValues();
                    picDetail.put(MediaStore.Images.Media.DISPLAY_NAME, imageFile.getName());
                    picDetail.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
                    picDetail.put(MediaStore.Images.Media.RELATIVE_PATH, "DCIM/" + UUID.randomUUID().toString());
                    picDetail.put(MediaStore.Images.Media.IS_PENDING, 1);
                    Uri finaluri = resolver.insert(picCollection, picDetail);
                    picDetail.clear();
                    picDetail.put(MediaStore.Images.Media.IS_PENDING, 0);
                    resolver.update(picCollection, picDetail, null, null);
                    return finaluri;
                } else {
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, filePath);
                    return context.getContentResolver().insert(
                            MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                }

            } else {
                return null;
            }
        }
    }


    public static String getTimeAgoOrg(String date_time) {
        TimeAgo2 timeAgo2 = new TimeAgo2();
        String MyFinalValue = timeAgo2.covertTimeToText(date_time);
        return MyFinalValue;
    }

    public static String getCommentsTimeAgo(String date_time) {
        TimeAgo2 timeAgo2 = new TimeAgo2();
        String MyFinalValue = timeAgo2.covertTimeToTextWithPrefix(date_time);
        return MyFinalValue;
    }

    public static String parseDateToddMMyyyy(String time, Context context) {
        String inputPattern = "dd-MM-yyyy HH:mm:ss";
        String outputPattern = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();

        }
        return str;
    }

    public static void customAlertDialog(Context context, final CallbackResponse callbackResponse) {
        final Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.item_alert_dialouge);
        dialog.getWindow().setBackgroundDrawable(context.getResources().getDrawable(R.drawable.d_round_corner_white_bkg));

        Button okBtn = dialog.findViewById(R.id.ok_btn);
        Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
        okBtn.setOnClickListener(view -> {
            callbackResponse.Responce("okay");
            dialog.dismiss();
        });
        cancelBtn.setOnClickListener(view -> {
            dialog.dismiss();
        });
        dialog.show();
    }
    public static String Bitmap_to_base64(Activity activity, Bitmap imagebitmap){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imagebitmap.compress(Bitmap.CompressFormat.JPEG, 70, baos);
        byte[] byteArray = baos .toByteArray();
        String base64= Base64.encodeToString(byteArray, Base64.DEFAULT);
        return base64;
    }

    public static Bitmap Base64_to_bitmap(String base_64){
        Bitmap decodedByte=null;
        try {

            byte[] decodedString = Base64.decode(base_64, Base64.DEFAULT);
            decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        }catch (Exception e){

        }
        return decodedByte;
    }


}
