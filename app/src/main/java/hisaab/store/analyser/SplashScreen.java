package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.example.hisaabcalculator.R;

public class SplashScreen extends AppCompatActivity {
SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

            try {
                sp = getSharedPreferences("login", MODE_PRIVATE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().setStatusBarColor(getColor(R.color.primarydark));
                }
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
