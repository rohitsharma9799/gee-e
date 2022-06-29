package com.geee.Menu;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.geee.CodeClasses.API_LINKS;
import com.geee.CodeClasses.Functions;
import com.geee.Main_VP_Package.MainActivity;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;

import org.json.JSONException;
import org.json.JSONObject;

public class Feedback extends AppCompatActivity {

    EditText subject,message;
    Button submitas;
    private TextSwitcher textSwitcher;
    private int stringIndex = 0;
    private String[] row = {"Your Feed Back is very important to us", "May i help you", "G2 want to know your suggestion","Please share your value feed back here."};
    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        subject = findViewById(R.id.subject);
        message = findViewById(R.id.message);
        submitas = findViewById(R.id.submitas);

        textSwitcher = findViewById(R.id.textSwitcher);
        final Handler handler = new Handler();
        final int delay = 4500; // 1000 milliseconds == 1 second
        handler.postDelayed(new Runnable() {
            public void run() {
                // System.out.println("myHandler: here!"); // Do your work here
                if (stringIndex == row.length - 1) {
                    stringIndex = 0;
                    textSwitcher.setText(row[stringIndex]);
                } else {
                    textSwitcher.setText(row[++stringIndex]);
                }

                handler.postDelayed(this, delay);
            }
        }, delay);
        textSwitcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                textView = new TextView(Feedback.this);
                textView.setTextColor(Color.BLACK);
                textView.setTextSize(14);
                textView.setGravity(Gravity.CENTER_HORIZONTAL);
                return textView;
            }
        });
        textSwitcher.setText(row[stringIndex]);

        submitas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!subject.getText().toString().equals("") &&!message.getText().toString().equals("")){
                    final Dialog dialog = new Dialog(Feedback.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.item_yesno_dialouge);
                    dialog.getWindow().setBackgroundDrawable(getResources().getDrawable(R.drawable.d_round_corner_white_bkg));
                    Button okBtn = dialog.findViewById(R.id.ok_btn);
                    okBtn.setText("Continue");
                    Button cancelBtn = dialog.findViewById(R.id.cancel_btn);
                    okBtn.setOnClickListener(view -> {
                      sendfeedback();
                    });
                    cancelBtn.setOnClickListener(view -> {
                        dialog.dismiss();
                    });
                    dialog.show();
                }else {
                    Toast.makeText(Feedback.this, "Please enter subject and message first", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void sendfeedback() {
//        SharedPreferences sharedPreferences = getSharedPreferences(Constant.PREFACCOUNT, MODE_PRIVATE);
        JSONObject parameters = new JSONObject();
        try {
            parameters.put("user_id", SharedPrefrence.getUserIdFromJson(Feedback.this));
            parameters.put("subject",subject.getText().toString());
            parameters.put("feedback",message.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("req" , API_LINKS.API_FEEDBACK+parameters);
        Functions.showLoader(this,false,false);
        ApiRequest.Call_Api(this, API_LINKS.API_FEEDBACK, parameters, new Callback() {
            @Override
            public void Responce(String response) {
                Log.i("response",response.toString());
                Functions.cancelLoader();
                try {
                    JSONObject jsonObject = new JSONObject((response));
                    Toast.makeText(Feedback.this, jsonObject.getString("message"), Toast.LENGTH_SHORT).show();
                    Intent strt = new Intent(Feedback.this, MainActivity.class);
                    startActivity(strt);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    public void back(View view) {
        onBackPressed();
    }
}