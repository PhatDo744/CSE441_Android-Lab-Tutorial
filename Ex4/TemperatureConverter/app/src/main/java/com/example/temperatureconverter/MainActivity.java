package com.example.temperatureconverter;

import android.os.Bundle;
import android.text.Editable;
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

    EditText edtCelsius, edtFahrenheit;
    Button btnConvertCelsius, btnConvertFahrenheit, btnClear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        edtFahrenheit = findViewById(R.id.edtf);
        edtCelsius = findViewById(R.id.edtc);
        btnConvertCelsius = findViewById(R.id.btnToC);
        btnConvertFahrenheit = findViewById(R.id.btnToF);
        btnClear = findViewById(R.id.btnClear);
        btnConvertFahrenheit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DecimalFormat dcf = new DecimalFormat("#.00");
                String doC = edtCelsius.getText()+"";

                int C = Integer.parseInt(doC);
                edtFahrenheit.setText("" + dcf.format((C * 9 / 5) + 32));
            }
        });
        btnConvertCelsius.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DecimalFormat dcf = new DecimalFormat("#.00");
                String doF = edtFahrenheit.getText()+"";

                int F = Integer.parseInt(doF);
                edtCelsius.setText("" + dcf.format((F - 32) * 5 / 9));
            }
        });
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtCelsius.setText("");
                edtFahrenheit.setText("");
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}