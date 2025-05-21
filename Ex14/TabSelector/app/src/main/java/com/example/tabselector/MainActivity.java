package com.example.tabselector;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edtA, edtB;
    Button btnCong;
    ListView lv1;
    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        addControls();
        addEvent();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    private void addEvent(){
        btnCong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XyLyCong();
            }
            private void XyLyCong(){
                int a = Integer.parseInt(edtA.getText().toString());
                int b = Integer.parseInt(edtB.getText().toString());
                String c = a + " + " + b + " = " + (a + b);
                list.add(c);
                adapter.notifyDataSetChanged();
                edtA.setText("");
                edtB.setText("");
            }
        });
    }
    private void addControls(){
        TabHost tab = findViewById(R.id.tabhost);
        tab.setup();
        TabHost.TabSpec tab1 = tab.newTabSpec("Tab 1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("", getResources().getDrawable(R.drawable.ic_launcher_background));
        tab.addTab(tab1);
        TabHost.TabSpec tab2 = tab.newTabSpec("Tab 2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("", getResources().getDrawable(R.drawable.ic_launcher_background));
        tab.addTab(tab2);
        LinearLayout tab1Layout = findViewById(R.id.tab1);
        edtA = tab1Layout.findViewById(R.id.edta);
        edtB = tab1Layout.findViewById(R.id.edtb);
        btnCong = tab1Layout.findViewById(R.id.button);
        LinearLayout tab2Layout = findViewById(R.id.tab2);
        lv1 = tab2Layout.findViewById(R.id.lv1);
        list = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        lv1.setAdapter(adapter);
    }
}