package com.example.tabselector2;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tabselector2.Adapters.MyArrayAdapter;
import com.example.tabselector2.Models.Item;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    EditText edttim;
    ListView lv1,lv2,lv3;
    TabHost tab;
    ArrayList<Item> list1, list2, list3;
    ArrayList<Item> allSongs; // List to hold all songs for searching
    MyArrayAdapter myarray1, myarray2, myarray3;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        addControl();
        addEvent();
        
        // Initialize data for the first tab when app starts
        loadTab1Data();
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    
    private void addEvent() {
        // Tab change listener
        tab.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equalsIgnoreCase("t1")) {
                    loadTab1Data();
                }
                else if (tabId.equalsIgnoreCase("t2")) {
                    loadTab2Data();
                }
                else if (tabId.equalsIgnoreCase("t3")) {
                    loadTab3Data();
                }
            }
        });
        
        // Add search functionality
        edttim.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchSongs(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    
    private void addControl() {
        // Setup TabHost
        tab = (TabHost) findViewById(R.id.tabhost);
        tab.setup();

        TabHost.TabSpec tab1 = tab.newTabSpec("t1");
        tab1.setContent(R.id.tab1);
        tab1.setIndicator("Tìm kiếm", getResources().getDrawable(R.drawable.glass_2025715));
        tab.addTab(tab1);

        TabHost.TabSpec tab2 = tab.newTabSpec("t2");
        tab2.setContent(R.id.tab2);
        tab2.setIndicator("Danh sách", getResources().getDrawable(R.drawable.list_1778593));
        tab.addTab(tab2);

        TabHost.TabSpec tab3 = tab.newTabSpec("t3");
        tab3.setContent(R.id.tab3);
        tab3.setIndicator("Yêu thích", getResources().getDrawable(R.drawable.heart_147246));
        tab.addTab(tab3);

        edttim = (EditText) findViewById(R.id.edttim);
        lv1 = (ListView) findViewById(R.id.lv1);
        lv2 = (ListView) findViewById(R.id.lv2);
        lv3 = (ListView) findViewById(R.id.lv3);

        // Initialize lists
        list1 = new ArrayList<Item>();
        list2 = new ArrayList<Item>();
        list3 = new ArrayList<Item>();
        allSongs = new ArrayList<Item>();
        
        // Create adapters
        myarray1 = new MyArrayAdapter(MainActivity.this, R.layout.listitem, list1);
        myarray2 = new MyArrayAdapter(MainActivity.this, R.layout.listitem, list2);
        myarray3 = new MyArrayAdapter(MainActivity.this, R.layout.listitem, list3);

        // Set adapters to ListViews
        lv1.setAdapter(myarray1);
        lv2.setAdapter(myarray2);
        lv3.setAdapter(myarray3);
        
        // Initialize all songs
        initializeSongDatabase();
    }
    
    // Initialize song database
    private void initializeSongDatabase() {
        allSongs.add(new Item("52300", "Em là ai Tôi là ai", 0));
        allSongs.add(new Item("52600", "Chén Đắng", 1));
        allSongs.add(new Item("57236", "Gởi em ở cuối sông hồng", 0));
        allSongs.add(new Item("51548", "Say tình", 0));
        allSongs.add(new Item("57689", "Hát với dòng sông", 1));
        allSongs.add(new Item("58716", "Say tình _ Remix", 0));
    }
    
    // Load data for each tab
    private void loadTab1Data() {
        list1.clear();
        // For search tab, initially display all songs
        list1.addAll(allSongs);
        myarray1.notifyDataSetChanged();
    }
    
    private void loadTab2Data() {
        list2.clear();
        // For song list tab, display all songs
        list2.addAll(allSongs);
        myarray2.notifyDataSetChanged();
    }
    
    private void loadTab3Data() {
        list3.clear();
        // For favorites tab, display only liked songs
        for (Item item : allSongs) {
            if (item.getThich() == 1) {
                list3.add(item);
            }
        }
        myarray3.notifyDataSetChanged();
    }
    
    // Search functionality
    private void searchSongs(String searchText) {
        list1.clear();
        if (searchText.isEmpty()) {
            // If search is empty, show all songs
            list1.addAll(allSongs);
        } else {
            // Filter songs by title or ID
            for (Item item : allSongs) {
                if (item.getTieude().toLowerCase().contains(searchText.toLowerCase()) || 
                    item.getMaso().contains(searchText)) {
                    list1.add(item);
                }
            }
        }
        myarray1.notifyDataSetChanged();
    }
}
