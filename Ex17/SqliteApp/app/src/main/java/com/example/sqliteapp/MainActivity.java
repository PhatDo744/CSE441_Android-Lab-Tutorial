package com.example.sqliteapp;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    // String DB_PATH_SUFFIX = "/databases/"; // Removed
    SQLiteDatabase database = null;
    String DATABASE_NAME = "qlsach.db";
    //Khai báo ListView
    ListView lv;
    ArrayList<String> mylist;
    ArrayAdapter<String> myadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Khởi tạo ListView và Adapter trước để tránh NullPointerException nếu DB không sẵn sàng
        lv = findViewById(R.id.lv);
        mylist = new ArrayList<>();
        myadapter = new ArrayAdapter<>(MainActivity.this,
                android.R.layout.simple_list_item_1, mylist);
        lv.setAdapter(myadapter);

        if (processCopy()) { // Kiểm tra xem DB đã sẵn sàng chưa (tồn tại hoặc đã sao chép)
            //Mở CSDL lên để dùng
            database = openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);

            // Truy vấn CSDL và cập nhật hiển thị lên ListView
            Cursor c = null;
            try {
                c = database.query("tbsach", null, null, null, null, null, null);
                if (c != null && c.moveToFirst()) {
                    String data;
                    do {
                        data = c.getString(0) + "-" + c.getString(1) + "-" + c.getString(2);
                        mylist.add(data);
                    } while (c.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error querying database: " + e.getMessage(), Toast.LENGTH_LONG).show();
            } finally {
                if (c != null) {
                    c.close();
                }
            }
            myadapter.notifyDataSetChanged();

            if (mylist.isEmpty()) {
                Toast.makeText(this, "No data found in the database.", Toast.LENGTH_SHORT).show();
            }

        } else {
            // Cơ sở dữ liệu không thể chuẩn bị (không tồn tại hoặc sao chép không thành công)
            Toast.makeText(this, "Database is not available. Application cannot load data.", Toast.LENGTH_LONG).show();
            // Bạn có thể muốn vô hiệu hóa các thành phần UI hoặc hiển thị một thông báo lỗi
        }
    }

    private boolean processCopy() {
        File dbFile = getDatabasePath(DATABASE_NAME); // Sử dụng phương thức của Context
        if (dbFile.exists()) {
            // Toast.makeText(this, "Database already exists.", Toast.LENGTH_SHORT).show(); // Tùy chọn: cho mục đích gỡ lỗi
            return true; // Cơ sở dữ liệu đã tồn tại, không cần sao chép
        }

        // Cơ sở dữ liệu không tồn tại, thử sao chép từ assets
        try {
            if (CopyDataBaseFromAsset()) { // Gọi phương thức đã sửa đổi
                Toast.makeText(this, "Database copied successfully from Assets.", Toast.LENGTH_LONG).show();
                return true; // Sao chép thành công
            } else {
                // Toast cho việc sao chép thất bại đã có trong CopyDataBaseFromAsset hoặc ở đây
                Toast.makeText(this, "Failed to copy database from Assets.", Toast.LENGTH_LONG).show();
                return false; // Sao chép thất bại
            }
        } catch (Exception e) { // Bắt các lỗi không mong muốn khác trong quá trình thiết lập
            Toast.makeText(this, "Error during database setup: " + e.toString(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
            return false; // Lỗi chung trong quá trình thiết lập
        }
    }

    // private String getDatabasePath() { // Removed
    // return getApplicationInfo().dataDir + DB_PATH_SUFFIX + DATABASE_NAME;
    // }

    public boolean CopyDataBaseFromAsset() {
        InputStream myInput = null;
        OutputStream myOutput = null;
        try {
            myInput = getAssets().open(DATABASE_NAME);
            File outFile = getDatabasePath(DATABASE_NAME); // Lấy đường dẫn file DB từ Context

            // Đảm bảo thư mục databases tồn tại
            File parentDir = outFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs(); // Tạo các thư mục cha nếu chúng không tồn tại
            }

            myOutput = new FileOutputStream(outFile);

            byte[] buffer = new byte[1024]; // Sử dụng buffer để sao chép
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }

            myOutput.flush();
            return true; // Báo hiệu thành công
        } catch (IOException e) {
            e.printStackTrace();
            // Toast.makeText(this, "Failed to copy database: " + e.getMessage(), Toast.LENGTH_LONG).show(); // Tùy chọn
            return false; // Báo hiệu thất bại
        } finally {
            try {
                if (myOutput != null) {
                    myOutput.close();
                }
                if (myInput != null) {
                    myInput.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}