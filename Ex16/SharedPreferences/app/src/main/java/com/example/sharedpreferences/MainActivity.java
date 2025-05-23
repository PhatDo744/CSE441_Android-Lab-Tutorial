package com.example.sharedpreferences;

import android.content.SharedPreferences;
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

public class MainActivity extends AppCompatActivity {
    EditText edta, edtb, edtkq;
    Button btntong, btnclear;
    TextView txtlichsu;
    String lichsu = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edta = findViewById(R.id.edta);
        edtb = findViewById(R.id.edtb);
        edtkq = findViewById(R.id.edtkq);
        btntong = findViewById(R.id.btntong);
        btnclear = findViewById(R.id.btnclear);
        txtlichsu = findViewById(R.id.txtlichsu);
        //----Lấy lại dữ liệu trong SharedPreferences-----
        SharedPreferences myprefs =
                getSharedPreferences("mysave",MODE_PRIVATE);
        lichsu = myprefs.getString("ls","");
        txtlichsu.setText(lichsu);
        //-------------------------------------------------
        btntong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int a = Integer.parseInt(edta.getText().toString());
                int b = Integer.parseInt(edtb.getText().toString());
                int kq = a + b;
                edtkq.setText(kq+"");
                lichsu += a+"+"+b+" = "+kq;
                txtlichsu.setText(lichsu);
                lichsu += "\n"; // Xuống dòng
            }
        });

        btnclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lichsu ="";
                txtlichsu.setText(lichsu);
            }
        });

    } // Đóng phương thức onCreate

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences myprefs =
                getSharedPreferences("mysave",MODE_PRIVATE);
        SharedPreferences.Editor myedit = myprefs.edit();
        myedit.putString("ls",lichsu);
        myedit.commit();
    }
} // Đóng class MainActivity