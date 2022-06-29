package com.geee.Inner_VP_Package.Profile_Pacakge;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.geee.CodeClasses.Functions;
import com.geee.R;
import com.geee.SharedPrefs.SharedPrefrence;
import com.geee.tictokcode.SimpleClasses.ApiRequest;
import com.geee.tictokcode.SimpleClasses.Callback;
import com.geee.tictokcode.SimpleClasses.Variables;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Changepassword extends AppCompatActivity {

    EditText etEmailUsername;
    Button signup_email_phn_btn_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.changepasss);
        etEmailUsername = findViewById(R.id.et_email);
        signup_email_phn_btn_id = findViewById(R.id.signup_email_phn_btn_id);

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

        final String password = etEmailUsername.getText().toString();
        if (password.isEmpty() || password.length() < 8) {
            etEmailUsername.setError("Please enter valid password");
            etEmailUsername.setFocusable(true);
            etEmailUsername.requestFocus();
            return false;
        }

        return true;
    }
    public  void Call_Api_For_coverimage() {
        Functions.showLoader(Changepassword.this,false,false);
        JSONObject parameters = null;
        try {
            parameters = new JSONObject();
            parameters.put("user_id", "3" );
            parameters.put("password",etEmailUsername.getText().toString() );
            Log.i("dfsdfsdfsdfsdf",parameters.toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Log.i("dffsdfsfssdfsdfsdfsdf",e.toString());
        }
        ApiRequest.Call_Api(Changepassword.this, Variables.CHANGEPASS, parameters, new Callback() {
            @Override
            public void Responce(String resp) {
                Functions.cancelLoader();
                Log.i("dfsfsfsffsf",resp.toString());
                try {
                    JSONObject response=new JSONObject(resp);
                    String code=response.optString("code");
                    String msg=response.optString("msg");
                    if(code.equals("200")){
                        Toast.makeText(Changepassword.this, msg, Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(Changepassword.this, msg, Toast.LENGTH_SHORT).show();
                    }
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