package edu.upc.androidclient;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.annotation.NonNull;
import android.util.Log;
import android.content.Intent;
import android.content.Context;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.ViewHolder> {

    private List<Track> trackList;
    private ApiService apiService;
    private Context context;

    public TrackAdapter(List<Track> tracks, ApiService apiService, Context context){

        this.trackList = tracks;
        this.apiService = apiService;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate item layout for each track
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int pos) {
        // Bind data to the view elements
        int position = holder.getAdapterPosition();
        Track track = trackList.get(position);
        holder.titleTextView.setText(track.getTitle());
        holder.singerTextView.setText(track.getSinger());
        holder.trackId.setText("Track ID: " + track.getId());

        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteTrack(track.getId(), position);
            }
        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditTrackActivity.class);
            intent.putExtra("trackId", track.getId());
            intent.putExtra("trackTitle", track.getTitle());
            intent.putExtra("trackSinger", track.getSinger());
            context.startActivity(intent);
        });

    }

    @Override
    public int getItemCount() {
        return trackList.size();
    }

    private void deleteTrack(String trackId, int position) {
        apiService.deleteTrack(trackId).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    trackList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, getItemCount());

                } else {
                    Log.e("MyAdapter", "Failed to delete track");
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("MyAdapter", "API call failed", t);

            }
        });
    }


    // ViewHolder to hold references to the views
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView;
        TextView singerTextView;
        TextView trackId;
        Button deleteButton;


        public ViewHolder(View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.track_title);
            singerTextView = itemView.findViewById(R.id.track_singer);
            trackId = itemView.findViewById(R.id.track_id);
            deleteButton = itemView.findViewById(R.id.delete_button);

        }
    }

}

