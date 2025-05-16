package com.example.learnbundle;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.text.DecimalFormat;

public class ketqua extends AppCompatActivity {
    EditText edtKQ;
    Button btnBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ketqua);
        edtKQ = findViewById(R.id.edtKQ);
        btnBack = findViewById(R.id.button);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mypackage");
        int a = bundle.getInt("soa");
        int b = bundle.getInt("sob");
        String kq = "";
        if (a == 0 && b == 0) {
            kq = "vô số nghiệm";
        } else if (a == 0 && b != 0) {
            kq = "vô nghiệm";
        } else {
            DecimalFormat dcf = new DecimalFormat("0.##");
            kq = "x = " + dcf.format((-b * 1.0/ a));
        }
        edtKQ.setText(kq);
    }
}
