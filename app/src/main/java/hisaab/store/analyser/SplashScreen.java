package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;

public class SplashScreen extends AppCompatActivity {
SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try{
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        catch (Exception e){}
            try {
                sp = getSharedPreferences("login", MODE_PRIVATE);


                boolean islooged = sp.getBoolean("islogged", false);
                if (!islooged) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent i = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }, 3000);

                } else {

                    SharedPreferences sharedPreferences = getSharedPreferences("hisaab", MODE_PRIVATE);
                    if (sharedPreferences.getBoolean("incoming_fromAnotherApp", false)) {
                        sharedPreferences.edit().putBoolean("incoming_fromAnotherApp",false).apply();
                        SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
                        String user = spitem.getString("user","");
                        sharedPreferences.edit().putBoolean(user+"allowapp",false).apply();
                        Intent i = new Intent(SplashScreen.this,item.class);
                        startActivity(i);
                        finishAffinity();
                    } else {


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                /* Create an Intent that will start the Menu-Activity. */
                                Intent i = new Intent(SplashScreen.this, first.class);
                                SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
                                String user = spitem.getString("user","");
                                sharedPreferences.edit().putBoolean(user+"allowapp",true).apply();
                                startActivity(i);
                                finish();
                            }
                        }, 2000);
                    }
                }
            } catch (Exception e) {

            }
        }
    }
