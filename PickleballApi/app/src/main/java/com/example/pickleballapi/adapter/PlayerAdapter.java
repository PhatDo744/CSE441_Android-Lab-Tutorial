package com.example.pickleballapi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.pickleballapi.Clients.ApiClient;
import com.example.pickleballapi.MainActivity;
import com.example.pickleballapi.R;
import com.example.pickleballapi.model.Player;
import com.example.pickleballapi.service.PlayerService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlayerAdapter extends RecyclerView.Adapter<PlayerAdapter.ViewHolder> {

    private List<Player> players;
    private Context context;
    private PlayerService playerService;
    private Runnable refreshCallback;

    public PlayerAdapter(Context context, List<Player> players, Runnable refreshCallback) {
        this.context = context;
        this.players = players;
        this.refreshCallback = refreshCallback;
        this.playerService = ApiClient.getRetrofit().create(PlayerService.class);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item_player, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Player p = players.get(position);
        holder.txtName.setText(p.username + " (" + p.member_code + ")");
        holder.txtHometown.setText("Quê quán: " + p.hometown);

        holder.btnDelete.setOnClickListener(v -> {
            playerService.deletePlayer(p.id).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show();
                    refreshCallback.run();
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(context, "Lỗi xóa", Toast.LENGTH_SHORT).show();
                }
            });
        });

        holder.btnEdit.setOnClickListener(v -> {
            if (context instanceof MainActivity) {
                try {
                    ((MainActivity) context).showEditDialog(p);
                } catch (Exception e) {
                    android.util.Log.e("PlayerAdapter", "Exception in showEditDialog: " + e.getMessage(), e);
                    Toast.makeText(context, "Lỗi khi mở hộp thoại sửa", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtName, txtHometown;
        Button btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtName = itemView.findViewById(R.id.txtName);
            txtHometown = itemView.findViewById(R.id.txtHometown);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
