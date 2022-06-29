package com.geee.Signup_Package;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.geee.CodeClasses.Variables;
import com.geee.R;
import com.geee.Volley_Package.API_Calling_Methods;

public class Username_Password extends AppCompatActivity {

    Button btnContinue;
    String emailOrPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.username_password);
       /*
        Intent intent = getIntent();

        emailOrPhone = intent.getStringExtra("email_or_phone");

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);

        btnContinue = findViewById(R.id.username_pass_btn_id);
        btnContinue.setOnClickListener(this);*/

    }

/*
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.username_pass_btn_id:
                if (checkValidation()) {

                }
                break;

            default:
                break;
        }
    }

    // this will check the validations like none of the field can be the empty
    public boolean checkValidation() {

        final String username = etUsername.getText().toString();
        final String password = etPassword.getText().toString();

        if (TextUtils.isEmpty(username)) {
            etUsername.setError("Invalid Username");
            etUsername.setFocusable(true);
            etUsername.requestFocus();
            return false;
        } else {
            etUsername.setError(null);
        }
        if (TextUtils.isEmpty(password) || password.length() < 8) {
            etPassword.setError("Password should be 8 digits long");
            etPassword.setFocusable(true);
            etPassword.requestFocus();
            return false;
        } else {
            etPassword.setError(null);
        }

        return true;
    }
*/


}
