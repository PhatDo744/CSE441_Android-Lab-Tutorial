package com.example.pickleballapi;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickleballapi.Clients.ApiClient;
import com.example.pickleballapi.adapter.PlayerAdapter;
import com.example.pickleballapi.model.Player;
import com.example.pickleballapi.service.PlayerService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    PlayerService playerService;
    List<Player> players = new ArrayList<>();
    PlayerAdapter adapter;
    RecyclerView recyclerView;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        playerService = ApiClient.getRetrofit().create(PlayerService.class);

        btnAdd = findViewById(R.id.btnAdd);
        recyclerView = findViewById(R.id.recyclerView);
        adapter = new PlayerAdapter(this, players, this::loadPlayers);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnAdd.setOnClickListener(v -> showAddDialog());

        loadPlayers();

    }

    // Trong file MainActivity.java

    public void loadPlayers() {
        // playerService đã được khởi tạo trong onCreate
        // playerService = ApiClient.getRetrofit().create(PlayerService.class); // Không cần khởi tạo lại ở đây nếu đã có trong onCreate

        playerService.getPlayers().enqueue(new Callback<List<Player>>() {
            @Override
            public void onResponse(Call<List<Player>> call, Response<List<Player>> response) {
                if (response.isSuccessful() && response.body() != null) { // Kiểm tra cả response.body() != null
                    players.clear();
                    players.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    Log.d("loadPlayers_SUCCESS", "Lấy dữ liệu người chơi thành công: " + players.size() + " người chơi.");
                } else {
                    // Log lỗi chi tiết nếu response không thành công
                    players.clear(); // Có thể bạn muốn xóa dữ liệu cũ khi có lỗi
                    adapter.notifyDataSetChanged(); // Cập nhật UI để hiển thị danh sách rỗng

                    String errorMessage = "Lỗi lấy danh sách người chơi: ";
                    if (response.errorBody() != null) {
                        try {
                            errorMessage += response.code() + " - " + response.errorBody().string();
                        } catch (java.io.IOException e) {
                            errorMessage += "Không thể đọc nội dung lỗi từ server. Mã lỗi: " + response.code();
                            Log.e("loadPlayers_ERROR", "IOException reading error body", e);
                        }
                    } else {
                        errorMessage += "Phản hồi không thành công hoặc body rỗng. Mã lỗi: " + response.code() + ", Message: " + response.message();
                    }
                    Toast.makeText(MainActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                    Log.e("loadPlayers_ERROR", errorMessage);
                }
            }

            @Override
            public void onFailure(Call<List<Player>> call, Throwable t) {
                players.clear(); // Xóa dữ liệu cũ
                adapter.notifyDataSetChanged(); // Cập nhật UI

                Toast.makeText(MainActivity.this, "Lỗi kết nối khi tải danh sách", Toast.LENGTH_SHORT).show();
                Log.e("loadPlayers_FAILURE", "Error fetching players: " + t.getMessage(), t);
                if (t instanceof java.net.ConnectException) {
                    Log.e("loadPlayers_FAILURE", "Không thể kết nối tới server. Kiểm tra địa chỉ IP, PORT, và trạng thái server.");
                } else if (t instanceof java.net.UnknownHostException) {
                    Log.e("loadPlayers_FAILURE", "Không tìm thấy host. Kiểm tra BASE_URL trong ApiClient.");
                } else if (t instanceof IllegalArgumentException) {
                    Log.e("loadPlayers_FAILURE", "URL không hợp lệ. Kiểm tra BASE_URL (phải kết thúc bằng /) và định nghĩa endpoint trong PlayerService.");
                }
            }
        });
    }

    public void showAddDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm hội viên");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        EditText edtName = new EditText(this);
        edtName.setHint("Tên hội viên");
        layout.addView(edtName);

        builder.setView(layout);
        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String name = edtName.getText().toString().trim();
            if (name.isEmpty()) {
                Toast.makeText(MainActivity.this, "Tên hội viên không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            Player p = new Player();
            p.username = name;
            p.avatar = "default.jpg";
            p.hometown = "Hà Nội";
            p.residence = "TP.HCM";
            p.birthday = "2005-10-10";
            p.rating_single = 0;
            p.rating_double = 0;

            // Trong file MainActivity.java, bên trong hàm showAddDialog

// ... (phần code lấy thông tin player p) ...
            playerService.createPlayer(p).enqueue(new Callback<Player>() {
                @Override
                public void onResponse(Call<Player> call, Response<Player> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        Toast.makeText(MainActivity.this, "Đã thêm thành công!", Toast.LENGTH_SHORT).show();
                        Log.d("showAddDialog_SUCCESS", "Thêm người chơi thành công: " + response.body().username);
                        loadPlayers(); // Tải lại danh sách sau khi thêm thành công
                    } else {
                        String errorMessage = "Lỗi thêm hội viên: ";
                        if (response.errorBody() != null) {
                            try {
                                errorMessage += response.code() + " - " + response.errorBody().string();
                            } catch (java.io.IOException e) {
                                errorMessage += "Không thể đọc nội dung lỗi từ server. Mã lỗi: " + response.code();
                                Log.e("showAddDialog_ERROR", "IOException reading error body", e);
                            }
                        } else {
                            errorMessage += "Phản hồi không thành công hoặc body rỗng. Mã lỗi: " + response.code() + ", Message: " + response.message();
                        }
                        Toast.makeText(MainActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_LONG).show();
                        Log.e("showAddDialog_ERROR", errorMessage);
                        // Không gọi loadPlayers() ở đây để người dùng biết thao tác thêm bị lỗi
                    }
                }

                @Override
                public void onFailure(Call<Player> call, Throwable t) {
                    Toast.makeText(MainActivity.this, "Lỗi kết nối khi thêm", Toast.LENGTH_SHORT).show();
                    Log.e("showAddDialog_FAILURE", "Error creating player: " + t.getMessage(), t);
                }
            });
        });

        builder.setNegativeButton("Hủy", null);
        builder.show();
    }

    public void showEditDialog(Player player) {
        try {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Sửa hội viên");

            LinearLayout layout = new LinearLayout(this);
            layout.setOrientation(LinearLayout.VERTICAL);
            EditText edtName = new EditText(this);
            edtName.setText(player.username);
            layout.addView(edtName);

            builder.setView(layout);
            builder.setPositiveButton("Cập nhật", (dialog, which) -> {
                Log.d("EditDialog_Debug", "Nút Cập nhật đã được nhấn!");
                try {
                    String name = edtName.getText().toString().trim();
                    if (name.isEmpty()) {
                        Toast.makeText(MainActivity.this, "Tên hội viên không được để trống", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    player.username = name;
                    playerService.updatePlayer(player.id, player).enqueue(new Callback<Player>() {
                        // Trong file MainActivity.java, bên trong hàm showEditDialog,
                        // sửa lại phần onResponse của playerService.updatePlayer(...).enqueue(...) như sau:

                        @Override
                        public void onResponse(Call<Player> call, Response<Player> response) {
                            if (response.isSuccessful() && response.body() != null) {
                                Log.d("showEditDialog_Update", "API Update báo thành công từ server.");
                                Log.d("showEditDialog_Update", "Mã phản hồi (Code): " + response.code());
                                Log.d("showEditDialog_Update", "Nội dung phản hồi (Body): " + response.body().toString());
                                // Để xem rõ hơn, bạn có thể log từng trường của response.body(), ví dụ:
                                // Player playerFromResponse = response.body();
                                // Log.d("showEditDialog_Update", "Tên người dùng từ phản hồi: " + playerFromResponse.username);
                                // Log.d("showEditDialog_Update", "Quê quán từ phản hồi: " + playerFromResponse.hometown);
                                // So sánh các giá trị này với giá trị bạn đã cố gắng cập nhật.

                                Toast.makeText(MainActivity.this, "Đã cập nhật thành công! (Client báo)", Toast.LENGTH_SHORT).show();
                                loadPlayers();
                            } else {
                                String errorMessage = "Lỗi cập nhật: ";
                                if (response.errorBody() != null) {
                                    try {
                                        // Cố gắng đọc thông điệp lỗi chi tiết từ server
                                        errorMessage += response.code() + " - " + response.errorBody().string();
                                    } catch (java.io.IOException e) {
                                        errorMessage += "Không thể đọc nội dung lỗi từ server.";
                                        Log.e("showEditDialog", "IOException reading error body", e);
                                    }
                                } else {
                                    errorMessage += "Phản hồi không thành công hoặc body rỗng. Mã lỗi: " + response.code();
                                }
                                Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_LONG).show(); // Hiển thị lỗi chi tiết
                                Log.e("showEditDialog", "Error updating player: " + errorMessage);
                                // Bạn có thể quyết định có nên gọi loadPlayers() ở đây không.
                                // Nếu không gọi, UI sẽ giữ nguyên trạng thái trước khi cố gắng cập nhật.
                                // Nếu gọi, UI sẽ đồng bộ lại với server (hiển thị dữ liệu chưa được cập nhật).
                                // Tạm thời, để dễ debug, có thể không gọi loadPlayers() khi lỗi,
                                // hoặc vẫn gọi để đảm bảo UI nhất quán với trạng thái hiện tại của server.
                                // Ví dụ: vẫn gọi để reset lại nếu người dùng sửa nhưng không thành công:
                                loadPlayers();
                            }
                        }

                        @Override
                        public void onFailure(Call<Player> call, Throwable t) {
                            Toast.makeText(MainActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                            Log.e("showEditDialog", "Error updating player: " + t.getMessage(), t);
                        }
                    });
                } catch (Exception e) {
                    Log.e("showEditDialog", "Exception in update: " + e.getMessage(), e);
                }
            });

            builder.setNegativeButton("Hủy", null);
            builder.show();
        } catch (Exception e) {
            Log.e("showEditDialog", "Exception: " + e.getMessage(), e);
        }
    }
}
