package com.example.pickleballfr;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickleballfr.Models.Player;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import com.example.pickleballfr.Adapters.PlayerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private FirebaseDatabase database;
    private DatabaseReference myRefRoot; // Tham chiếu đến gốc của database
    private DatabaseReference myRefMessages; // Tham chiếu đến một nút cụ thể, ví dụ "messages"
    private DatabaseReference usersRef;    // Tham chiếu đến nút "users"
    List<Player> players;
    PlayerAdapter adapter;
    RecyclerView recyclerView;
    Button btnAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        // 1. Lấy instance của FirebaseDatabase
        // Firebase tự động sử dụng URL từ tệp google-services.json của bạn
        // URL của bạn là: https://pickleballfr-afadb-default-rtdb.asia-southeast1.firebasedatabase.app/
        database = FirebaseDatabase.getInstance("https://pickleballfr-afadb-default-rtdb.asia-southeast1.firebasedatabase.app/");
        // Nếu bạn chỉ có một database instance trong project, bạn cũng có thể gọi:
        // database = FirebaseDatabase.getInstance();

        // 2. Lấy DatabaseReference
        // Tham chiếu đến gốc của database
        myRefRoot = database.getReference();

        // Tham chiếu đến một nút cụ thể, ví dụ "messages".
        // Nếu nút này chưa tồn tại, nó sẽ được tạo khi bạn ghi dữ liệu vào đó lần đầu.
        myRefMessages = database.getReference("messages");

        // Tham chiếu đến nút "users"
        usersRef = database.getReference("players");

        // Ví dụ: Ghi một giá trị đơn giản để kiểm tra
        // myRefMessages.setValue("Chào mừng đến với PickleballFR Database!");

        // Bạn có thể bắt đầu gọi các hàm ghi/đọc dữ liệu từ đây
        // writeNewUser("user001", "Dotie", "dotie@example.com");
        // readMessages();
        players = new ArrayList<>();
        adapter = new PlayerAdapter(this, players);
        recyclerView = findViewById(R.id.recyclerView);
        btnAdd = findViewById(R.id.btnAdd);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        usersRef.addValueEventListener(new ValueEventListener() {
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                players.clear();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    Player p = snap.getValue(Player.class);
                    players.add(p);
                }
                adapter.notifyDataSetChanged();
            }

            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(MainActivity.this, "Lỗi đọc dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd.setOnClickListener(v -> showAddDialog());
    }

    private void showAddDialog() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Add Player");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);

        final android.widget.EditText inputName = new android.widget.EditText(this);
        inputName.setHint("Name");
        layout.addView(inputName);

        final android.widget.EditText inputHometown = new android.widget.EditText(this);
        inputHometown.setHint("Hometown");
        layout.addView(inputHometown);

        builder.setView(layout);

        builder.setPositiveButton("Add", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            String hometown = inputHometown.getText().toString().trim();
            if (name.isEmpty() || hometown.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            String member_code = "MB" + System.currentTimeMillis();
            Player player = new Player(member_code, name, "", "", hometown, "", 0, 0);
            usersRef.child(member_code).setValue(player);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }


    public void showEditDialog(Player player) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("Edit Player");

        android.widget.LinearLayout layout = new android.widget.LinearLayout(this);
        layout.setOrientation(android.widget.LinearLayout.VERTICAL);

        final android.widget.EditText inputName = new android.widget.EditText(this);
        inputName.setText(player.username);
        layout.addView(inputName);

        final android.widget.EditText inputHometown = new android.widget.EditText(this);
        inputHometown.setText(player.hometown);
        layout.addView(inputHometown);

        builder.setView(layout);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String name = inputName.getText().toString().trim();
            String hometown = inputHometown.getText().toString().trim();
            if (name.isEmpty() || hometown.isEmpty()) {
                Toast.makeText(this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                return;
            }
            player.username = name;
            player.hometown = hometown;
            usersRef.child(player.member_code).setValue(player);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }
}
