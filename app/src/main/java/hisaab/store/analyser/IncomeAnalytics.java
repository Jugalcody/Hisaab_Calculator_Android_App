package hisaab.store.analyser;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class IncomeAnalytics extends AppCompatActivity {

    LinearLayout parentlayout;
    TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_analytics);

        ViewPager2 viewPager = findViewById(R.id.viewPagerIncome);
        tabLayout = findViewById(R.id.tabLayoutIncome);
        parentlayout=findViewById(R.id.incomeanalytics_activity_parentlayout);

        IncomePagerAdapter adapter = new IncomePagerAdapter(this);
        viewPager.setAdapter(adapter);
        viewPager.setUserInputEnabled(false);
        changetheme();
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> {
                    switch (position) {
                        case 0: tab.setText("Daily"); break;
                        case 1: tab.setText("Monthly"); break;
                        case 2: tab.setText("Yearly"); break;
                    }
                }).attach();
    }


    private void changetheme() {
        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        if (sp.getString("theme", "dark").equals("pink")) {
                parentlayout.setBackgroundColor(getResources().getColor(R.color.primarylight));
            getWindow().setNavigationBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
               tabLayout.setBackgroundColor(getResources().getColor(R.color.primarylight));
        } else {
            parentlayout.setBackgroundColor(getResources().getColor(R.color.primarydark));
            tabLayout.setBackgroundColor(getResources().getColor(R.color.primarydark));

            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(0);
            Window window = getWindow();
            window.setNavigationBarColor(getColor(R.color.black));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.getDecorView().setSystemUiVisibility(0);
        }
    }


        }
