package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class VideoTutorialPage extends AppCompatActivity {
    ImageView back;
    RecyclerView recyclerView;
    VideoAdpater adapter;
    ProgressBar progressBar;
    LinearLayout toolbar;

    TextView title;
    List<List> arr = new ArrayList<>();

    LottieAnimationView nodata_animation;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.videotutorial_activity);
try {
    // Initialize Views
    toolbar = findViewById(R.id.about_toolbar);
    recyclerView = findViewById(R.id.aboutrec);
    title = findViewById(R.id.video_title);
    back = findViewById(R.id.about_back);
    db = FirebaseFirestore.getInstance();
    nodata_animation = findViewById(R.id.nodata_videotutorial);
    progressBar = findViewById(R.id.progress_about);

    // Theme Handling
    SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
    String key = "";
    try {

        key = getIntent().getStringExtra("key");
    } catch (Exception e) {

    }

    getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    if (!(key != null && key.trim().equals("outside"))) {
        if (sp.getString("theme", "dark").equals("pink")) {


        } else {
                /*getWindow().setStatusBarColor(getColor(R.color.primarydark));
                toolbar.setBackgroundColor(getColor(R.color.backgroundpink));
                getWindow().setStatusBarColor(getColor(R.color.statusbarcolordark));
                title.setTextColor(getColor(R.color.black));
                getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                back.setBackgroundColor(getColor(R.color.white)); // background
                back.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));*/

        }
    } else {


    }

    // Back Button
    back.setOnClickListener(v -> onBackPressed());

    // Set RecyclerView
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    adapter = new VideoAdpater(this, arr);
    recyclerView.setAdapter(adapter);

    // Fetch Data from Firestore
    fetchVideoContent();
}catch (Exception e){
    Toast.makeText(VideoTutorialPage.this,e.toString(),Toast.LENGTH_SHORT).show();
}
    }

    private void fetchVideoContent() {
        CollectionReference videoCollection = db.collection("HisaabAnalyser").document("videoContent").collection("videos");

        videoCollection.get().addOnSuccessListener(queryDocumentSnapshots -> {
            arr.clear(); // Clear previous data

            for (DocumentSnapshot document : queryDocumentSnapshots) {
                String title = document.getString("content");  // Fetch content
                String videoUrl = document.getString("video"); // Fetch video URL

                if (title != null && videoUrl != null) {
                    arr.add(Arrays.asList(title, videoUrl));
                }
            }

            // Notify Adapter after Data Fetch
            progressBar.setVisibility(View.GONE);
            if(arr.size()==0){
                nodata_animation.setVisibility(View.VISIBLE);
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(VideoTutorialPage.this, "Failed to fetch video data", Toast.LENGTH_SHORT).show();
        });
    }
}
