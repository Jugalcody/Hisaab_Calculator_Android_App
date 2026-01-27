package hisaab.store.analyser;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class VideoAdpater extends RecyclerView.Adapter<VideoAdpater.MyViewHolder> {
    Context context;
    List<List> arr;

    public VideoAdpater(Context context, List<List> arr) {
        this.context = context;
        this.arr = arr;
    }

    @NonNull
    @Override
    public VideoAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.videodesign, parent, false);
        return new VideoAdpater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String videoUrl = arr.get(position).get(1).toString();
        String thumbnailUrl = "https://img.youtube.com/vi/" + getYouTubeVideoId(videoUrl) + "/0.jpg";  // Generate the thumbnail URL

        // Load YouTube thumbnail image
        Picasso.get().load(thumbnailUrl).into(holder.thumbnailImageView);

        holder.content.setText(arr.get(position).get(0).toString());

        // Set OnClickListener to navigate to ZoomVideo.java
        holder.thumbnailImageView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ZoomVideo.class);
            intent.putExtra("link", videoUrl);  // Pass the video URL to the new activity
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return arr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView;
        TextView content;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.video_thumbnail); // You need to add an ImageView in your layout (videodesign.xml)
            content = itemView.findViewById(R.id.video_text);
        }
    }

    // Helper function to extract the YouTube video ID from the embed URL
    private String getYouTubeVideoId(String url) {
        String videoId = null;
        if (url.contains("embed")) {
            String[] parts = url.split("embed/");
            if (parts.length > 1) {
                videoId = parts[1].split("\\?")[0];  // Get the video ID from the embed URL
            }
        }
        return videoId;
    }
}
