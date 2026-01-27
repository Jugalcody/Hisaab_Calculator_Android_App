package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Objects;

public class AboutPage extends AppCompatActivity {
    ImageView back,insta,webpage;
    LinearLayout toolbar,parentlayout;
    TextView title,header,content1,content2,content3,content4,content5;
    AppCompatButton videoTutorial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_page);

        try {
            back = findViewById(R.id.about_back);
            videoTutorial = findViewById(R.id.video_tutorial_button);
            toolbar = findViewById(R.id.about_toolbar);
            insta = findViewById(R.id.instagramimg);
            title=findViewById(R.id.abouttitle);
            content1=findViewById(R.id.aboutcontent1);
            content2=findViewById(R.id.aboutcontent2);
            parentlayout=findViewById(R.id.aboutparent);
            header=findViewById(R.id.aboutheader1);
            content3=findViewById(R.id.aboutcontent3);
            content4=findViewById(R.id.aboutcontent4);
            content5=findViewById(R.id.aboutcontent5);
            webpage = findViewById(R.id.webpageimg);
            SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            getWindow().setStatusBarColor(getColor(R.color.toolbarpink));
            changetheme();
            if (sp.getString("theme", "dark").equals("pink")) {

                if (!Objects.equals(getIntent().getStringExtra("key"), "outside")) {

                    toolbar.setBackgroundColor(getColor(R.color.toolbarpink));
                    title.setTextColor(getColor(R.color.black));
                    back.setBackgroundColor(getColor(R.color.white)); // background
                    back.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));
                }
                else{
             /*       getWindow().setStatusBarColor(getColor(R.color.primarydark));
                    toolbar.setBackgroundColor(getColor(R.color.primarydark));
                    title.setTextColor(getColor(R.color.white));
                    getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                    back.setBackgroundColor(getColor(R.color.backgrounddark)); // background
                    back.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));*/
                }
            }
            videoTutorial.setOnClickListener(v -> {
                try {
                    Intent intent = new Intent(AboutPage.this, VideoTutorialPage.class);
                    intent.putExtra("key", getIntent().getStringExtra("key"));
                    startActivity(intent);
                }catch (Exception e){
                    Toast.makeText(AboutPage.this,e.toString(),Toast.LENGTH_SHORT).show();
                }
            });


            insta.setOnClickListener(v -> {
                String instagramUrl = "https://www.instagram.com/techproductionltd/"; // Replace with your actual Instagram URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(instagramUrl));
                startActivity(intent);
            });

            webpage.setOnClickListener(v -> {
                String webUrl = "https://techproductionlimited.blogspot.com/2024/10/app-ad-txt.html?lr=1738926074805"; // Replace with your actual Instagram URL
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(webUrl));
                startActivity(intent);
            });



            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        catch (Exception e){
            Toast.makeText(AboutPage.this,e.toString(),Toast.LENGTH_SHORT).show();
        }
    }


    private void changetheme() {

        SharedPreferences sptheme = getSharedPreferences("theme",MODE_PRIVATE);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            content1.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            content3.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
            content5.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
        } else {
            content1.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            content3.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
            content5.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        }

        if (sptheme.getString("theme", "dark").equals("pink")) {

            content1.setTextColor(getResources().getColor(android.R.color.black));
            content3.setTextColor(getResources().getColor(android.R.color.black));
            content5.setTextColor(getResources().getColor(android.R.color.black));
            parentlayout.setBackgroundColor(getResources().getColor(R.color.backgroundlight));

            back.setImageTintList(ContextCompat.getColorStateList(this, R.color.backgrounddark));
            title.setTextColor(getColor(R.color.black));
            content2.setTextColor(getResources().getColor(android.R.color.black));
            content4.setTextColor(getResources().getColor(android.R.color.black));
            header.setTextColor(getColor(R.color.backgrounddark));



            content1.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.settingiconbackgroundlight));
            content3.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.settingiconbackgroundlight));
            content5.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.settingiconbackgroundlight));
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);


        }
        else{

            content1.setTextColor(getResources().getColor(android.R.color.white));
            content3.setTextColor(getResources().getColor(android.R.color.white));
            back.setImageTintList(ContextCompat.getColorStateList(this, R.color.backgroundlight));
            content5.setTextColor(getResources().getColor(android.R.color.white));
            content2.setTextColor(getResources().getColor(android.R.color.white));
            content4.setTextColor(getResources().getColor(android.R.color.white));
            title.setTextColor(getColor(R.color.white));
            header.setTextColor(getColor(R.color.white));
            parentlayout.setBackgroundColor(getResources().getColor(R.color.backgrounddark));

            getWindow().setStatusBarColor(getColor(R.color.statusbarcolordark));
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            content1.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.bg2));
            content3.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.bg2));
            content5.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.bg2));


        }
        }
    }