package com.example.hisaabcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.TextView;

public class about extends AppCompatActivity {
  TextView t,t2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        t=findViewById(R.id.at);
        t.setOnClickListener(view->{
            Intent i=new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/Jugalcody"));
            startActivity(i);
        });

        t2=findViewById(R.id.at2);
        t2.setOnClickListener(view->{
            Intent i2=new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.linkedin.com/in/jugal-kishore-das-323443234/?originalSubdomain=in"));
            startActivity(i2);
        });
    }
}