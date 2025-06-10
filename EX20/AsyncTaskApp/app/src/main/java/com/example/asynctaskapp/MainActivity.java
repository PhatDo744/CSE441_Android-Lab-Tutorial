package com.example.asynctaskapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView txtPercentage;
    ProgressBar progressBar;
    Button btnStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtPercentage = findViewById(R.id.txtPercentage);
        progressBar = findViewById(R.id.progressBar);
        btnStart = findViewById(R.id.btnStart);

        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Khởi chạy AsyncTask
                new UpdateTask().execute();
            }
        });
    }

    // Lớp AsyncTask để thực hiện công việc nền
    // Params: Kiểu dữ liệu đầu vào cho doInBackground (không dùng nên Void)
    // Progress: Kiểu dữ liệu cho onProgressUpdate (Integer cho phần trăm)
    // Result: Kiểu dữ liệu trả về của doInBackground và đầu vào của onPostExecute (String cho thông báo kết thúc)
    private class UpdateTask extends AsyncTask<Void, Integer, String> {

        // Được gọi trước khi doInBackground bắt đầu
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            txtPercentage.setText("0%");
            progressBar.setProgress(0);
            btnStart.setEnabled(false); // Vô hiệu hóa nút Start khi task đang chạy
        }

        // Thực hiện công việc nền ở đây
        @Override
        protected String doInBackground(Void... voids) {
            for (int i = 0; i <= 100; i++) {
                SystemClock.sleep(100); // Giả lập công việc tốn thời gian (100ms mỗi bước)
                publishProgress(i); // Gọi onProgressUpdate để cập nhật UI
                if (isCancelled()) { // Kiểm tra nếu task bị hủy
                    break;
                }
            }
            return "Hoàn thành!"; // Kết quả trả về cho onPostExecute
        }

        // Được gọi trên UI thread để cập nhật tiến trình
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress = values[0];
            txtPercentage.setText(progress + "%");
            progressBar.setProgress(progress);
        }

        // Được gọi trên UI thread sau khi doInBackground hoàn thành
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            txtPercentage.setText(result); // Hiển thị thông báo hoàn thành
            btnStart.setEnabled(true); // Kích hoạt lại nút Start
        }

        // (Tùy chọn) Được gọi nếu AsyncTask bị hủy
        @Override
        protected void onCancelled() {
            super.onCancelled();
            txtPercentage.setText("Đã hủy!");
            btnStart.setEnabled(true);
        }
    }
}