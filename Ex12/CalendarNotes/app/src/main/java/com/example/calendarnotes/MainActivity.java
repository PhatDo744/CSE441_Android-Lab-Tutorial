package com.example.calendarnotes;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    ListView lv;
    ArrayList<String> arrWork;
    ArrayAdapter<String> arrayAdapter;
    EditText edtWork, edtHours, edtMinutes;
    TextView txtDate;
    Button btnWork;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        edtHours = findViewById(R.id.edthour);
        edtMinutes = findViewById(R.id.edtmi);
        edtWork = findViewById(R.id.edtwork);
        btnWork = findViewById(R.id.btnadd);
        lv = findViewById(R.id.listView1);
        txtDate = findViewById(R.id.txtdate);

        arrWork = new ArrayList<>();
        arrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrWork);
        lv.setAdapter(arrayAdapter);
        Date currentDate = Calendar.getInstance().getTime();
        java.text.SimpleDateFormat simpleDateFormat = new java.text.SimpleDateFormat("dd/MM/yyyy");
        txtDate.setText("Hôm nay: " + simpleDateFormat.format(currentDate));
        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edtWork.getText().toString().equals("") || edtHours.getText().toString().equals("") || edtMinutes.getText().toString().equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Info missing");
                    builder.setMessage("Please fill in all fields");
                    builder.setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    builder.show();
                }
                else {
                    String str = edtWork.getText().toString() + " - " + edtHours.getText().toString() + ":" + edtMinutes.getText().toString();
                    arrWork.add(str);
                    arrayAdapter.notifyDataSetChanged();
                    edtHours.setText("");
                    edtMinutes.setText("");
                    edtWork.setText("");
                }
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}