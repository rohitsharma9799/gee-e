package com.geee;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Demotest extends AppCompatActivity {

    Integer UPI_PAYMENT_REQUEST_CODE = 2222;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demotest);
        Button btn1 = findViewById(R.id.btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upiIntent = new Intent(Intent.ACTION_VIEW);
                String uriString = "upi://pay?pa=9799569458@paytm&pn=amazon&tr=" + "123545td" + "&mc=9799569458@paytm&am="+10+"&tn=paymentForAmazon";

                upiIntent.setData(Uri.parse(uriString));
                Intent chooser = Intent.createChooser(upiIntent, "Pay with...");

                startActivityForResult(chooser, UPI_PAYMENT_REQUEST_CODE, null);
            }
        });
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == UPI_PAYMENT_REQUEST_CODE) {
                String paymentResponse =data.getStringExtra("response");

                Log.i("dfsdfdsf",paymentResponse);
            }
        }
    }
}