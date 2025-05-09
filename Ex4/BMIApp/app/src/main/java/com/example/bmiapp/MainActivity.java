package com.example.bmiapp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    Button btnChuanDoan;
    EditText edtTen, edtChieuCao, edtCanNang, edtBMI, edtChuanDoan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        btnChuanDoan = findViewById(R.id.btnChuanDoan);
        edtTen = findViewById(R.id.edtten);
        edtChieuCao = findViewById(R.id.edtchieucao);
        edtCanNang = findViewById(R.id.edtcannang);
        edtBMI = findViewById(R.id.edtbmi);
        edtChuanDoan = findViewById(R.id.edtchuandoan);
        btnChuanDoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double H = Double.parseDouble(edtChieuCao.getText().toString());
                double W = Double.parseDouble(edtCanNang.getText().toString());
                double BMI = W / (H * H);
                String chuanDoan = "";
                if (BMI < 18) {
                    chuanDoan = "Bạn gầy";
                }
                else if (BMI<=24.9){
                    chuanDoan = "Bạn bình thường";
                }
                else if (BMI<=29.9){
                    chuanDoan = "Bạn béo phì độ 1";
                }
                else if (BMI <=39.4){
                    chuanDoan = "Bạn béo phì độ 2";
                }
                else {
                    chuanDoan = "Bạn béo phì độ 3";
                }
                DecimalFormat dcf = new DecimalFormat("#.0");
                edtBMI.setText(String.valueOf(BMI));
                edtChuanDoan.setText(chuanDoan);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.TableLayout1), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}