package edu.upc.androidclient;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.content.Intent;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class AddTrackActivity extends AppCompatActivity {

    private EditText titleInput, singerInput;
    private Button saveButton;
    private Button cancelButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_add_track);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        titleInput = findViewById(R.id.track_title_input);
        singerInput = findViewById(R.id.track_singer_input);
        saveButton = findViewById(R.id.add_button);
        cancelButton = findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Capturar les dades introduïdes per l'usuari
                String title = titleInput.getText().toString();
                String singer = singerInput.getText().toString();
                if (!title.isBlank() && !singer.isBlank()) {
                    // Enviar les dades de tornada a la MainActivity
                    Intent resultIntent = new Intent();
                    resultIntent.putExtra("title", title);
                    resultIntent.putExtra("singer", singer);
                    setResult(RESULT_OK, resultIntent);
                    finish(); // Tornar a la MainActivity
                } else {
                    Toast.makeText(AddTrackActivity.this, "Song and singer can't be blank", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

}