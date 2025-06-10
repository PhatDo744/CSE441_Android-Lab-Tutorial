package com.example.karaokeapp;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

public class SubActivity extends Activity {

    TextView txtMaSoChiTiet, txtTenBaiHatChiTiet, txtLoiBaiHatChiTiet, txtTacGiaChiTiet;
    ImageButton btnLikeSub, btnUnlikeSub;
    BaiHat baiHatHienTai;
    boolean trangThaiYeuThichBanDau;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        addControls();
        loadDataFromIntent();
        addEvents();
    }

    private void addControls() {
        txtMaSoChiTiet = findViewById(R.id.txtMaSoChiTiet);
        txtTenBaiHatChiTiet = findViewById(R.id.txtTenBaiHatChiTiet);
        txtLoiBaiHatChiTiet = findViewById(R.id.txtLoiBaiHatChiTiet);
        txtTacGiaChiTiet = findViewById(R.id.txtTacGiaChiTiet);
        btnLikeSub = findViewById(R.id.btnLikeSub);
        btnUnlikeSub = findViewById(R.id.btnUnlikeSub);
    }

    private void loadDataFromIntent() {
        Intent intent = getIntent();
        String maBH = intent.getStringExtra("MABH_CHON");
        if (maBH != null) {
            Cursor cursor = MainActivity.database.query("ArirangSongBook", null, "MABH=?", new String[]{maBH}, null, null, null);
            if (cursor.moveToFirst()) {
                String tenBH = cursor.getString(1);
                String loiBH = cursor.getString(2);
                String tacGia = cursor.getString(3);
                int yeuThichInt = cursor.getInt(4);
                trangThaiYeuThichBanDau = (yeuThichInt == 1);
                baiHatHienTai = new BaiHat(maBH, tenBH, loiBH, tacGia, trangThaiYeuThichBanDau);

                txtMaSoChiTiet.setText("#" + baiHatHienTai.getMaBH());
                txtTenBaiHatChiTiet.setText(baiHatHienTai.getTenBH());
                txtLoiBaiHatChiTiet.setText(baiHatHienTai.getLoiBH());
                txtTacGiaChiTiet.setText("Sáng tác: " + (baiHatHienTai.getTacGia() != null && !baiHatHienTai.getTacGia().isEmpty() ? baiHatHienTai.getTacGia() : "Chưa rõ"));

                capNhatNutYeuThich();
            }
            cursor.close();
        }
    }

    private void capNhatNutYeuThich() {
        if (baiHatHienTai.isYeuThich()) {
            btnLikeSub.setVisibility(View.VISIBLE);
            btnUnlikeSub.setVisibility(View.GONE);
        } else {
            btnLikeSub.setVisibility(View.GONE);
            btnUnlikeSub.setVisibility(View.VISIBLE);
        }
    }

    private void addEvents() {
        btnLikeSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyBoThich();
            }
        });

        btnUnlikeSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                xuLyThich();
            }
        });
    }

    private void xuLyThich() {
        baiHatHienTai.setYeuThich(true);
        ContentValues values = new ContentValues();
        values.put("YEUTHICH", 1);
        int kq = MainActivity.database.update("ArirangSongBook", values, "MABH=?", new String[]{baiHatHienTai.getMaBH()});
        if (kq > 0) {
            Toast.makeText(this, "Đã thích bài hát", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi khi thích bài hát", Toast.LENGTH_SHORT).show();
            baiHatHienTai.setYeuThich(false); // Rollback
        }
        capNhatNutYeuThich();
        baoKetQuaVeMainActivity();
    }

    private void xuLyBoThich() {
        baiHatHienTai.setYeuThich(false);
        ContentValues values = new ContentValues();
        values.put("YEUTHICH", 0);
        int kq = MainActivity.database.update("ArirangSongBook", values, "MABH=?", new String[]{baiHatHienTai.getMaBH()});
        if (kq > 0) {
            Toast.makeText(this, "Đã bỏ thích bài hát", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Lỗi khi bỏ thích bài hát", Toast.LENGTH_SHORT).show();
            baiHatHienTai.setYeuThich(true); // Rollback
        }
        capNhatNutYeuThich();
        baoKetQuaVeMainActivity();
    }

    private void baoKetQuaVeMainActivity() {
        // Chỉ đặt RESULT_OK nếu trạng thái yêu thích thực sự thay đổi
        if (baiHatHienTai.isYeuThich() != trangThaiYeuThichBanDau) {
            Intent intentResult = new Intent();
            // Bạn có thể gửi thêm dữ liệu nếu cần, ví dụ MABH và trạng thái mới
            setResult(Activity.RESULT_OK, intentResult);
        }
    }

    @Override
    public void onBackPressed() {
        baoKetQuaVeMainActivity(); // Đảm bảo kết quả được gửi về khi nhấn back
        super.onBackPressed();
    }
}