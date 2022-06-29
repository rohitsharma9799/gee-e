package com.geee.Signup_Package;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.geee.CodeClasses.Functions;
import com.geee.CodeClasses.Variables;
import com.geee.Main_VP_Package.Main;
import com.geee.R;
import com.geee.Utils.KeyboardUtils;
import com.geee.Volley_Package.API_Calling_Methods;

public class Signup_Email extends AppCompatActivity implements View.OnClickListener {

    RelativeLayout emailRl, loginRlt;
    EditText emailEt;
    ImageView iv2;
    RelativeLayout emailNextBtn;
    Button btn;
    EditText etUsername, etPassword,confirmpas;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_email_phn);

        loginRlt = findViewById(R.id.login_rlt);
        KeyboardUtils.addKeyboardToggleListener(this, isVisible -> {
            if (isVisible) {
                loginRlt.setVisibility(View.GONE);
            } else {
                loginRlt.setVisibility(View.VISIBLE);
            }

        });

        ImageView  backarrow = findViewById(R.id.backarrow);
        backarrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        emailEt = findViewById(R.id.et_email);
        confirmpas = findViewById(R.id.confirmpas);
        iv2 = findViewById(R.id.iv_cross_email);

        emailNextBtn = findViewById(R.id.emailNextBtn);

        btn = findViewById(R.id.signup_email_phn_btn_id);


        emailRl = findViewById(R.id.signup_email_phn_RL2_id);

        emailEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //auto generated method
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int len = emailEt.length();
                if (len > 0) {
                    iv2.setVisibility(View.VISIBLE);
                    btn.setOnClickListener(Signup_Email.this);
                } else {
                    iv2.setVisibility(View.GONE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                //auto generated method
            }
        });

        iv2.setOnClickListener(this);
        loginRlt.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.login_rlt:
                methodOpenlogin();
                break;

            case R.id.signup_email_phn_btn_id:
                if (checkValidation()) {
                    API_Calling_Methods.signUp(emailEt.getText().toString(),
                            "" + etPassword.getText().toString(),
                            "" + etUsername.getText().toString(),
                            Signup_Email.this,
                            "" + Variables.flagLogin
                    );
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
        final String stEmail = emailEt.getText().toString();
        if (TextUtils.isEmpty(stEmail)) {
            emailEt.setError("Please enter valid email");
            emailEt.setFocusable(true);
            emailEt.requestFocus();
            return false;
        } else if (!Functions.isValidEmail(stEmail)) {
            emailEt.setError("Please enter valid email");
            emailEt.setFocusable(true);
            emailEt.requestFocus();
            return false;
        } else {
            emailEt.setError(null);
        }


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
      /*  if (!etPassword.equals(confirmpas.getText().toString())) {
            etPassword.setError("Password is not match!!");
            etPassword.setFocusable(true);
            etPassword.requestFocus();
            return false;
        } else {
            etPassword.setError(null);
        }*/
        return true;
    }


    private void methodOpenlogin() {
        Variables.check = false;
        startActivity(new Intent(Signup_Email.this, Main.class));
    }

    private void methodOpennewscreen() {
        Intent sendIntent = new Intent(Signup_Email.this, Username_Password.class);
        sendIntent.putExtra("email_or_phone", emailEt.getText().toString());
        startActivity(sendIntent);

    }

}
