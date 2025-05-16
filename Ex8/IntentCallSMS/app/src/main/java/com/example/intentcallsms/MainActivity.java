package com.example.intentcallsms;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btnCall, btnSMS;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btnCall = findViewById(R.id.btnCallPhone);
        btnSMS = findViewById(R.id.btnSendSms);
        btnCall.setOnClickListener(v -> {
            Intent intent1 = new Intent(MainActivity.this, CallPhoneActivity.class);
            startActivity(intent1);
        });
        btnSMS.setOnClickListener(v -> {
            Intent intent2 = new Intent(MainActivity.this, SendSMSActivity.class);
            startActivity(intent2);
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}