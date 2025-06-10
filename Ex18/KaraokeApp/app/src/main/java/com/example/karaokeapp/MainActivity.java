package com.example.karaokeapp;

import android.app.Activity;
import android.app.TabActivity; // Sử dụng TabActivity theo hướng dẫn PDF
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends TabActivity { // Sử dụng TabActivity

    public static SQLiteDatabase database = null;
    public static String DATABASE_NAME = "Arirang.sqlite";
    String DB_PATH_SUFFIX = "/databases/";

    TabHost tabHost;

    EditText edtTimKiem;
    ListView lvBaiHatTimKiem;
    ArrayList<BaiHat> dsBaiHatTimKiem;
    AdapterBaiHat adapterBaiHatTimKiem;

    ListView lvToanBoBaiHat;
    ArrayList<BaiHat> dsToanBoBaiHat;
    AdapterBaiHat adapterToanBoBaiHat;

    ListView lvBaiHatYeuThich;
    ArrayList<BaiHat> dsBaiHatYeuThich;
    AdapterBaiHat adapterBaiHatYeuThich;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        xuLySaoChepCSDLTuAssetsVaoHeThong();
        database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

        setupTabHost();
        addControls();
        loadData();
        addEvents();
    }

    private void setupTabHost() {
        tabHost = getTabHost(); // Lấy TabHost từ TabActivity

        TabHost.TabSpec tabTimKiem = tabHost.newTabSpec("t1");
        tabTimKiem.setIndicator("", getResources().getDrawable(R.drawable.ic_tab_search));
        tabTimKiem.setContent(R.id.tab_tim_kiem);
        tabHost.addTab(tabTimKiem);

        TabHost.TabSpec tabDanhSach = tabHost.newTabSpec("t2");
        tabDanhSach.setIndicator("", getResources().getDrawable(R.drawable.ic_tab_list));
        tabDanhSach.setContent(R.id.tab_danh_sach);
        tabHost.addTab(tabDanhSach);

        TabHost.TabSpec tabYeuThich = tabHost.newTabSpec("t3");
        tabYeuThich.setIndicator("", getResources().getDrawable(R.drawable.ic_tab_favorite));
        tabYeuThich.setContent(R.id.tab_yeu_thich);
        tabHost.addTab(tabYeuThich);
    }

    private void addControls() {
        edtTimKiem = findViewById(R.id.edtTimKiem);
        lvBaiHatTimKiem = findViewById(R.id.lvBaiHatTimKiem);
        dsBaiHatTimKiem = new ArrayList<>();
        adapterBaiHatTimKiem = new AdapterBaiHat(MainActivity.this, R.layout.item_bai_hat, dsBaiHatTimKiem);
        lvBaiHatTimKiem.setAdapter(adapterBaiHatTimKiem);

        lvToanBoBaiHat = findViewById(R.id.lvToanBoBaiHat);
        dsToanBoBaiHat = new ArrayList<>();
        adapterToanBoBaiHat = new AdapterBaiHat(MainActivity.this, R.layout.item_bai_hat, dsToanBoBaiHat);
        lvToanBoBaiHat.setAdapter(adapterToanBoBaiHat);

        lvBaiHatYeuThich = findViewById(R.id.lvBaiHatYeuThich);
        dsBaiHatYeuThich = new ArrayList<>();
        adapterBaiHatYeuThich = new AdapterBaiHat(MainActivity.this, R.layout.item_bai_hat, dsBaiHatYeuThich);
        lvBaiHatYeuThich.setAdapter(adapterBaiHatYeuThich);
    }

    private void loadData() {
        // Load toàn bộ bài hát cho tab danh sách và ban đầu cho tab tìm kiếm
        loadToanBoBaiHat();
        // Load danh sách yêu thích
        loadBaiHatYeuThich();
    }

    private void loadToanBoBaiHat() {
        dsToanBoBaiHat.clear();
        // Ban đầu tab tìm kiếm cũng hiển thị toàn bộ
        dsBaiHatTimKiem.clear();

        Cursor cursor = database.query("ArirangSongBook", null, null, null, null, null, "MABH ASC");
        while (cursor.moveToNext()) {
            String maBH = cursor.getString(0);
            String tenBH = cursor.getString(1);
            String loiBH = cursor.getString(2); // Lấy cả lời bài hát để truyền đi
            String tacGia = cursor.getString(3);
            int yeuThichInt = cursor.getInt(4);
            BaiHat baiHat = new BaiHat(maBH, tenBH, loiBH, tacGia, yeuThichInt == 1);
            dsToanBoBaiHat.add(baiHat);
            dsBaiHatTimKiem.add(baiHat); // Thêm vào ds tìm kiếm ban đầu
        }
        cursor.close();
        adapterToanBoBaiHat.notifyDataSetChanged();
        adapterBaiHatTimKiem.notifyDataSetChanged(); // Cập nhật adapter tìm kiếm
    }

    private void loadBaiHatYeuThich() {
        dsBaiHatYeuThich.clear();
        Cursor cursor = database.query("ArirangSongBook", null, "YEUTHICH=?", new String[]{"1"}, null, null, "MABH ASC");
        while (cursor.moveToNext()) {
            String maBH = cursor.getString(0);
            String tenBH = cursor.getString(1);
            String loiBH = cursor.getString(2);
            String tacGia = cursor.getString(3);
            BaiHat baiHat = new BaiHat(maBH, tenBH, loiBH, tacGia, true);
            dsBaiHatYeuThich.add(baiHat);
        }
        cursor.close();
        adapterBaiHatYeuThich.notifyDataSetChanged();
    }

    private void addEvents() {
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if (tabId.equalsIgnoreCase("t1")) { // Tab Tìm kiếm
                    // Không cần load lại gì đặc biệt, việc tìm kiếm xử lý qua EditText
                } else if (tabId.equalsIgnoreCase("t2")) { // Tab Danh sách
                    loadToanBoBaiHat(); // Load lại toàn bộ nếu cần
                } else if (tabId.equalsIgnoreCase("t3")) { // Tab Yêu thích
                    loadBaiHatYeuThich();
                }
            }
        });

        edtTimKiem.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                xuLyTimKiem(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Sự kiện click item cho cả 3 ListView
        AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                BaiHat baiHatDuocChon = null;
                if (parent.getId() == R.id.lvBaiHatTimKiem) {
                    baiHatDuocChon = adapterBaiHatTimKiem.getItem(position);
                } else if (parent.getId() == R.id.lvToanBoBaiHat) {
                    baiHatDuocChon = adapterToanBoBaiHat.getItem(position);
                } else if (parent.getId() == R.id.lvBaiHatYeuThich) {
                    baiHatDuocChon = adapterBaiHatYeuThich.getItem(position);
                }

                if (baiHatDuocChon != null) {
                    Intent intent = new Intent(MainActivity.this, SubActivity.class);
                    intent.putExtra("MABH_CHON", baiHatDuocChon.getMaBH());
                    startActivityForResult(intent, 100); // Dùng startActivityForResult để cập nhật nếu trạng thái like thay đổi
                }
            }
        };

        lvBaiHatTimKiem.setOnItemClickListener(itemClickListener);
        lvToanBoBaiHat.setOnItemClickListener(itemClickListener);
        lvBaiHatYeuThich.setOnItemClickListener(itemClickListener);
    }

    private void xuLyTimKiem(String query) {
        dsBaiHatTimKiem.clear();
        if (query.isEmpty()) {
            // Nếu query rỗng, có thể hiển thị lại toàn bộ danh sách hoặc để trống
            // Theo PDF thì khi search sẽ lọc từ ds gốc, ở đây tôi sẽ lọc từ CSDL
            Cursor cursor = database.query("ArirangSongBook", null, null, null, null, null, "MABH ASC");
            while (cursor.moveToNext()) {
                String maBH = cursor.getString(0);
                String tenBH = cursor.getString(1);
                String loiBH = cursor.getString(2);
                String tacGia = cursor.getString(3);
                int yeuThichInt = cursor.getInt(4);
                dsBaiHatTimKiem.add(new BaiHat(maBH, tenBH, loiBH, tacGia, yeuThichInt == 1));
            }
            cursor.close();

        } else {
            // Thực hiện truy vấn CSDL với điều kiện LIKE
            // Chuyển query và TENBH về chữ thường để tìm kiếm không phân biệt hoa thường
            String selection = "lower(TENBH) LIKE ?";
            String[] selectionArgs = new String[]{"%" + query.toLowerCase(Locale.getDefault()) + "%"};
            Cursor cursor = database.query("ArirangSongBook", null, selection, selectionArgs, null, null, "MABH ASC");
            while (cursor.moveToNext()) {
                String maBH = cursor.getString(0);
                String tenBH = cursor.getString(1);
                String loiBH = cursor.getString(2);
                String tacGia = cursor.getString(3);
                int yeuThichInt = cursor.getInt(4);
                dsBaiHatTimKiem.add(new BaiHat(maBH, tenBH, loiBH, tacGia, yeuThichInt == 1));
            }
            cursor.close();
        }
        adapterBaiHatTimKiem.notifyDataSetChanged();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            // Trạng thái yêu thích có thể đã thay đổi trong SubActivity
            // Load lại dữ liệu cho các tab để đảm bảo tính nhất quán
            loadToanBoBaiHat();
            loadBaiHatYeuThich();
            // Nếu đang ở tab tìm kiếm, có thể cần cập nhật lại kết quả tìm kiếm
            // hoặc đơn giản là gọi lại hàm tìm kiếm với query hiện tại
            if(edtTimKiem.getText().length() > 0){
                xuLyTimKiem(edtTimKiem.getText().toString());
            }
        }
    }


    // Phương thức này được gọi từ Adapter hoặc SubActivity để cập nhật trạng thái
    // (Theo PDF, các hàm này nằm trong MainActivity)
    // Tuy nhiên, việc gọi trực tiếp từ Adapter vào MainActivity không phải là cách tốt nhất.
    // Tốt hơn là dùng interface callback. Nhưng ở đây tôi làm theo hướng dẫn của PDF.

    public void capNhatLaiDanhSachYeuThichKhiThaoTac() {
        // Được gọi khi có thay đổi trạng thái like/unlike từ adapter hoặc subactivity
        // để cập nhật lại danh sách ở tab YeuThich nếu đang mở tab đó
        if (tabHost.getCurrentTabTag() != null && tabHost.getCurrentTabTag().equalsIgnoreCase("t3")) {
            loadBaiHatYeuThich();
        }
        // Cập nhật lại trạng thái trong dsToanBoBaiHat và dsBaiHatTimKiem
        // Cách đơn giản là load lại, hoặc tìm và cập nhật item
        loadToanBoBaiHat(); // Tải lại để đồng bộ trạng thái yêu thích
        if(edtTimKiem.getText().length() > 0) {
            xuLyTimKiem(edtTimKiem.getText().toString());
        } else {
            // Nếu không có query tìm kiếm, dsBaiHatTimKiem đã được cập nhật bởi loadToanBoBaiHat
            adapterBaiHatTimKiem.notifyDataSetChanged();
        }
    }


    private void xuLySaoChepCSDLTuAssetsVaoHeThong() {
        File dbFile = getDatabasePath(DATABASE_NAME);
        if (!dbFile.exists()) {
            try {
                saoChepCSDLTuAssets();
                Toast.makeText(this, "Sao chép CSDL thành công!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(this, "Lỗi sao chép CSDL: " + e.toString(), Toast.LENGTH_LONG).show();
                Log.e("DB_COPY_ERROR", "Lỗi sao chép CSDL: ", e);
            }
        }
    }

    private void saoChepCSDLTuAssets() {
        try {
            InputStream myInput = getAssets().open(DATABASE_NAME);
            String outFileName = layDuongDanLuuTruCSDL();
            File f = new File(getApplicationInfo().dataDir + DB_PATH_SUFFIX);
            if (!f.exists()) {
                f.mkdir();
            }
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (Exception ex) {
            Log.e("LOI_SAO_CHEP", ex.toString());
        }
    }

    private String layDuongDanLuuTruCSDL() {
        return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    }
}