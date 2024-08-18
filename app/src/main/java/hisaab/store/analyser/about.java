package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import com.example.hisaabcalculator.R;

public class about extends AppCompatActivity {
  TextView t,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        t=findViewById(R.id.at);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.primary));
        }
        t.setOnClickListener(view->{
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL, new String[]{ "jugalkd2000@gmail.com" });
            startActivity(i);
        });

        t2=findViewById(R.id.at2);
        t2.setOnClickListener(view->{
            Intent i2=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/jugal-kishore-das-323443234/?originalSubdomain=in"));
            startActivity(i2);
        });
    }
}