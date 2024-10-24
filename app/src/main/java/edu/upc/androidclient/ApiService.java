package edu.upc.androidclient;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import java.util.List;

public interface ApiService {
    @GET("tracks") // Petició GET
    Call<List<Track>> getAllTracks();

    @DELETE("tracks/{id}") // Petició DELETE
    Call<Void> deleteTrack(@Path("id") String trackId);

    @POST("tracks")
    Call<Void> addTrack(@Body Track track);

    @PUT("tracks")
    Call<Void> updateTrack(@Body Track track);

}
