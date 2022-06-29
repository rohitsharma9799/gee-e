package com.geee.Login_Package;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.geee.CodeClasses.Functions;
import com.geee.R;
import com.geee.Signup_Package.Signup;
import com.geee.Volley_Package.API_Calling_Methods;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.geee.tictokcode.SimpleClasses.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Fogetpassword extends AppCompatActivity {

    EditText etEmailUsername;
    Button signup_email_phn_btn_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogetpassword);
        etEmailUsername = findViewById(R.id.et_email);
        signup_email_phn_btn_id = findViewById(R.id.signup_email_phn_btn_id);

        ImageView  back_btn = findViewById(R.id.back_btn);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              onBackPressed();
            }
        });

      RelativeLayout login_rlt = findViewById(R.id.login_rlt);
        login_rlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login f = new Login();
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.addToBackStack(null);
                ft.replace(R.id.login_parent_RL_id, f).commit();
            }
        });
        signup_email_phn_btn_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkValidation()) {
                    Call_Api_For_coverimage();
                }
            }
        });


    }
    public boolean checkValidation() {

        final String stEmail = etEmailUsername.getText().toString();
        if (TextUtils.isEmpty(stEmail)) {
            etEmailUsername.setError("Please enter valid email");
            etEmailUsername.setFocusable(true);
            etEmailUsername.requestFocus();
            return false;
        } else if (!Functions.isValidEmail(stEmail)) {
            etEmailUsername.setError("Please enter valid email");
            etEmailUsername.setFocusable(true);
            etEmailUsername.requestFocus();
            return false;
        }

        return true;
    }
    public  void Call_Api_For_coverimage() {
        Functions.showLoader(Fogetpassword.this,false,false);
        JSONObject parameters = null;
        try {
            parameters = new JSONObject();
            parameters.put("email",etEmailUsername.getText().toString() );
            Log.i("dfsdfsdfsdfsdf",parameters.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("dffsdfsfssdfsdfsdfsdf",e.toString());
        }
        ApiRequest.Call_Api(Fogetpassword.this, Variables.GEEEFORGETPASS, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancelLoader();
                Log.i("dfsfsfsffsf",resp.toString());
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    JSONArray msg=response.optJSONArray("msg");
                    if(code.equals("200")){
                        Toast.makeText(Fogetpassword.this, "Please check your registered email address we sent you mail to change your password", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Fogetpassword.this, "You enter wrong email address!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });



    }
}