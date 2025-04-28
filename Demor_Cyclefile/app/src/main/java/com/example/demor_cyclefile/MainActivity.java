package com.example.demor_cyclefile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btnCall;

    //Được gọi khi Activity bị hủy
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this,"CR424 - onDestroy()",Toast.LENGTH_LONG).show();
    }

    //Được gọi khi Activity bị tạm dừng (khi chuyển sang Activity khác)
    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this,"CR424 - onPause()",Toast.LENGTH_LONG).show();
    }
    //Được gọi khi Activity quay lại từ trạng thái bị dừng, như làm mới dữ liệu hoặc giao diện
    //Luồng sự kiện:
    //Activity bị dừng (onStop()).
    //Người dùng quay lại Activity.
    //onRestart() được gọi, sau đó là onStart() và onResume().
    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this,"CR424 - onRestart()",Toast.LENGTH_LONG).show();
    }

    //Được gọi khi Activity bắt đầu tương tác với người dùng
    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this,"CR424 - onResume()",Toast.LENGTH_LONG).show();
    }

    //Được gọi khi Activity đang ở trạng thái hiển thị
    @Override
    protected void onStart() {
        super.onStart();
        //Thông báo Toast để minh họa trạng thái
        Toast.makeText(this,"CR424 - onStart()",Toast.LENGTH_LONG).show();
    }
    // Được gọi khi Activity không còn hiển thị
    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        //Thiết lập giao diện
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "CR424 - onCreate()", Toast.LENGTH_LONG).show();
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        //Ánh xạ các thành phần giao diện
        btnCall = findViewById(R.id.btnCall);
        //Đăng kí sự kiện click cho nút btnCall => Subactivity
        btnCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(MainActivity.this, Subactivity.class);
                startActivity(intent1);
            }
        });
    }
}