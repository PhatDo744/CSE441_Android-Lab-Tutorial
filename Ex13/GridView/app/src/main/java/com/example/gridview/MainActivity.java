package com.example.gridview;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    String arr[]={"Ipad", "Iphone", "New Ipad",
            "SamSung", "Nokia", "Sony Ericson",
            "LG", "Q-Mobile", "HTC", "Blackberry",
            "G Phone", "FPT - Phone", "HK Phone"
    };
    TextView textView;
    GridView gridView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        gridView = findViewById(R.id.gridView);
        ArrayAdapter<String> da=new ArrayAdapter<String>
                (
                        this,
                        android.R.layout.simple_list_item_1,
                        arr
                );
        gridView.setAdapter(da);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> arg0,
                                    View arg1, int arg2,
                                    long arg3) {
                //Hiển thị phần tử được chọn trong GridView lên TextView
                textView.setText(arr[arg2]);
            }
        });
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}