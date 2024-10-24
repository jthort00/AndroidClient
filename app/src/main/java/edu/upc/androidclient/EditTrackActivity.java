package edu.upc.androidclient;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import android.widget.EditText;
import android.widget.Button;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class EditTrackActivity extends AppCompatActivity {

    private EditText titleEditText, singerEditText;
    private Button saveButton, cancelButton;
    private String trackId;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_edit_track);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleEditText = findViewById(R.id.edit_track_title_input);
        singerEditText = findViewById(R.id.edit_track_singer_input);
        saveButton = findViewById(R.id.save_button);
        cancelButton = findViewById(R.id.cancel_edit_button);

        apiService = RetrofitClient.getClient().create(ApiService.class);


        // Obtenim les dades de la cançó de l'intent
        Intent intent = getIntent();
        trackId = intent.getStringExtra("trackId");
        String title = intent.getStringExtra("trackTitle");
        String singer = intent.getStringExtra("trackSinger");

        // Mostrem les dades a la interfície
        titleEditText.setText(title);
        singerEditText.setText(singer);

        // Guardar els canvis quan es clica el botó
        saveButton.setOnClickListener(v -> saveTrackChanges(trackId));

    }

    private void saveTrackChanges(String trackId) {
        String updatedTitle = titleEditText.getText().toString();
        String updatedSinger = singerEditText.getText().toString();


        // Crea l'objecte `Track` actualitzat
        Track updatedTrack = new Track(updatedTitle, updatedSinger);
        updatedTrack.id = trackId;


        // Envia la petició a l'API per actualitzar la cançó
        apiService.updateTrack(updatedTrack).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditTrackActivity.this, "Track updated", Toast.LENGTH_SHORT).show();
                    finish();  // Tornem a la llista
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("API Error", "Failed to update track", t);
            }
        });
    }


}