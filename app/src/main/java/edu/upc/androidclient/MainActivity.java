package edu.upc.androidclient;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;


import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button update_button;
    private ApiService apiService;
    private List<Track> trackList = new ArrayList<>();
    private TrackAdapter trackAdapter;
    private ActivityResultLauncher<Intent> addTrackLauncher;



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
        recyclerView = findViewById(R.id.my_recycler_view);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        update_button = findViewById(R.id.update_button);
        // Adreça del localhost http://10.0.2.2:8080/dsaApp/
        apiService = RetrofitClient.getClient().create(ApiService.class);
        updateList(update_button);
        trackAdapter = new TrackAdapter(trackList, apiService, this);
        recyclerView.setAdapter(trackAdapter);

        Button addButton = findViewById(R.id.add_button);

        addTrackLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        String title = result.getData().getStringExtra("title");
                        String singer = result.getData().getStringExtra("singer");

                        if (title != null && singer != null) {
                            Track newTrack = new Track(title, singer);
                            apiService.addTrack(newTrack).enqueue(new Callback<Void>() {
                                @Override
                                public void onResponse(Call<Void> call, Response<Void> response) {
                                    if (response.isSuccessful()) {
                                        updateList(findViewById(R.id.update_button));
                                    }
                                }

                                @Override
                                public void onFailure(Call<Void> call, Throwable t) {
                                    Log.e("API Error", "Failed to add track", t);
                                }
                            });
                        } else {
                            Log.e("Data Error", "Received null title or singer");
                        }
                    }
                }
        );

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Obrir la AddTrackActivity
                Intent intent = new Intent(MainActivity.this, AddTrackActivity.class);
                addTrackLauncher.launch(intent);
            }
        });



    }

    public void updateList (View v) {
        Call<List<Track>> call = apiService.getAllTracks();
        call.enqueue(new Callback<List<Track>>() {
            @Override
            public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    trackList.clear();
                    trackList.addAll(response.body());
                    trackAdapter.notifyDataSetChanged();
                    Toast.makeText(MainActivity.this, "Tracks loaded", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("API_ERROR", "Error: " + response.code());
                }
            }

            public void onFailure(Call<List<Track>> call, Throwable t) {
                Log.e("API_ERROR", "Failed to fetch tracks", t);
                Toast.makeText(MainActivity.this, "Failed to fetch tracks", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String title = data.getStringExtra("title");
            String singer = data.getStringExtra("singer");

            // Aquí pots fer la petició a l'API per afegir la nova track
            Track newTrack = new Track(title, singer);
            apiService.addTrack(newTrack).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        // Refresca la llista després d'afegir la cançó
                        updateList(findViewById(android.R.id.content));
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    // Gestiona l'error aquí
                    Log.e("API Error", "Failed to add track", t);
                }
            });
        }
    }

}