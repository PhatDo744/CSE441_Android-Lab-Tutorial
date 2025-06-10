package com.example.pickleballapi.service;
import com.example.pickleballapi.model.Player;

import retrofit2.Call;
import retrofit2.http.*;

import java.util.List;

public interface PlayerService {
    @GET("players")
    Call<List<Player>> getPlayers();

    @GET("players/{id}")
    Call<Player> getPlayer(@Path("id") int id);

    @POST("players")
    Call<Player> createPlayer(@Body Player player);

    @PUT("players/{id}")
    Call<Player> updatePlayer(@Path("id") int id, @Body Player player);

    @DELETE("players/{id}")
    Call<Void> deletePlayer(@Path("id") int id);
}
