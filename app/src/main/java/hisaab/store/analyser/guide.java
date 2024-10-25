package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.hisaabcalculator.R;

public class guide extends AppCompatActivity {
ImageView back;
LinearLayout toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        toolbar=findViewById(R.id.about_toolbar);
        SharedPreferences sp=getSharedPreferences("theme",MODE_PRIVATE);
        if(sp.getString("theme","dark").equals("pink")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.toolbarpink));
            }
            toolbar.setBackgroundColor(getColor(R.color.toolbarpink));

        }
        else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            toolbar.setBackgroundColor(getColor(R.color.toolbardark));

        }
        back=findViewById(R.id.about_back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}