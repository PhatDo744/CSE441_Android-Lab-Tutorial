package com.example.giaiphuongtrinhbac2;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Button btnTiepTuc, btnThoat, btnGiaiPhuongTrinh;
    EditText edta, edtb, edtc;
    TextView txtkq;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btnThoat = findViewById(R.id.btnthoat);
        btnTiepTuc = findViewById(R.id.btntieptuc);
        btnGiaiPhuongTrinh = findViewById(R.id.btngiaipt);
        edta = findViewById(R.id.edta);
        edtb = findViewById(R.id.edtb);
        edtc = findViewById(R.id.edtc);
        txtkq = findViewById(R.id.txtkq);
        btnGiaiPhuongTrinh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sa = edta.getText() + "";
                String sb = edtb.getText() + "";
                String sc = edtc.getText() + "";
                int a = Integer.parseInt(sa);
                int b = Integer.parseInt(sb);
                int c = Integer.parseInt(sc);
                String txtkq = "";
                DecimalFormat dcf = new DecimalFormat("0.00");
                if (a==0){
                    if (b==0){
                        if (c==0){
                            txtkq = "Phương trình vô số nghiệm";
                        }else {
                            txtkq = "Phương trình vô nghiệm";
                        }
                    }else {
                        txtkq = "Phương trình có 1 nghiệm: x = " + dcf.format((double) -c / b);
                    }
                }else {
                    double delta = b * b - 4 * a * c;
                    if (delta<0){
                        txtkq = "Phương trình vô nghiệm";
                    }else if (delta==0){
                        txtkq = "Phương trình có 1 nghiệm: x = " + dcf.format((double) -b / (2 * a));
                    }
                    else {
                        txtkq = "Phương trình có 2 nghiệm phân biệt: " +
                                "x1 = " + dcf.format((double) (-b + Math.sqrt(delta)) / (2 * a)) + " " +
                                "x2 = " + dcf.format((double) (-b - Math.sqrt(delta)) / (2 * a));
                    }
                }
            }
        });
        btnTiepTuc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edta.setText("");
                edtb.setText("");
                edtc.setText("");
                edta.requestFocus();
            }
        });
        btnThoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}