package hisaab.store.analyser;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.appupdate.AppUpdateOptions;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class first extends AppCompatActivity {

    Toolbar toolbar;
    boolean back = false;

    BottomNavigationView bottomNavigationView;
    Fragment homeFragment = new HomeFragment();
    View hr;
    Fragment analyticsFragment = new Analytics_fragment();
    Fragment profileFragment = new ProfileFragment();
    Fragment activeFragment = homeFragment;
    SharedPreferences sharedPreferences;
    public static UserViewModel viewModel;

    Utils utils;
    ImageView setting;
    private ActivityResultLauncher activityResultLauncher;
    String currentVersion;
    String head = "";
    ShapeableImageView user_img;

    AlarmManager alarmManager;

    ViewGroup root;
    SharedPreferences splogin, spitem;

    RelativeLayout layout;
    String n;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);


        try {

            sharedPreferences = getSharedPreferences("hisaab",MODE_PRIVATE);

            layout = findViewById(R.id.first_parent_layout);
            user_img = findViewById(R.id.home_user_img);
            utils=new Utils();
            hr=findViewById(R.id.first_view);
            bottomNavigationView =findViewById(R.id.bottomNavigation_home);
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            root = (ViewGroup) getWindow().getDecorView().getRootView();

            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            /*getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);*/
            try {


            splogin = getSharedPreferences("login", MODE_PRIVATE);

            spitem = getSharedPreferences("item", MODE_PRIVATE);
            n = spitem.getString("name", "").split(" ")[0];
            head = spitem.getString("user", "");
           if(splogin.getString("new"+head,"yes").equals("yes")) {
               createWindowWelcome();
               splogin.edit().putString("new"+head,"no").apply();
           }
           else{

               if (spitem.getBoolean("allowalarm", false)) {
                   createNotificationChannel(this);
                   schedule();
               }
           }


            String comebackmsg = splogin.getString("comebackmsg", "").toString();
            if (!comebackmsg.equals("")) {
               // createWindowComeBack(comebackmsg);
            }
            try {

                if (!splogin.getBoolean("islogged", false)) {
                    Intent i = new Intent( this,MainActivity.class);
                    startActivity(i);
                   finishAffinity();
                }


                String hname = "";
                for (int i = 0; i < n.length(); i++) {
                    if (i == 0) {
                        hname += n.charAt(i);
                        hname = hname.toUpperCase();
                    } else {
                        hname += n.charAt(i);
                    }
                }



            } catch (Exception e) {

            }
                      if (!splogin.getBoolean("isLoginstatusSent", false)) {
                updateUserLoginStatus(spitem.getString("user", ""), true);
                splogin.edit().putBoolean("isLoginstatusSent", true).apply();
            }




            try {
                checkForUpdate(first.this);
            } catch (Exception e) {

            }
        } catch (Exception e) {
            Toast.makeText(first.this,"Allow to get daily expense remainder", Toast.LENGTH_LONG).show();
        }

            FragmentManager fm = getSupportFragmentManager();


            fm.beginTransaction()
                    .add(R.id.containerFrame_home, profileFragment, "PROFILE")
                    .hide(profileFragment)
                    .commit();

            fm.beginTransaction()
                    .add(R.id.containerFrame_home, analyticsFragment, "ANALYTICS")
                    .hide(analyticsFragment)
                    .commit();


            if (savedInstanceState != null) {
                String tag = savedInstanceState.getString("ACTIVE_FRAGMENT_TAG");
                activeFragment = fm.findFragmentByTag(tag);

                fm.beginTransaction()
                        .add(R.id.containerFrame_home, homeFragment, "HOME")
                        .commit(); // Home visible by default
                // Ensure correct fragment visibility
                fm.beginTransaction()
                        .hide(homeFragment)
                        .hide(analyticsFragment)
                        .hide(profileFragment)
                        .show(activeFragment)
                        .commit();
            }




            if(savedInstanceState==null) {



                fm.beginTransaction()
                        .add(R.id.containerFrame_home, homeFragment, "HOME")
                        .commit(); // Home visible by default
                // Make sure activeFragment points to the visible fragment instance
                    activeFragment = homeFragment;

            }

            //  loadFragment(new HomeFragment(),"HOME");
        bottomNavigationView.setOnItemSelectedListener(item -> {

            int id = item.getItemId();
            if (id == R.id.nav_home) {
                loadFragment(homeFragment);
            } else if (id == R.id.nav_analytics) {
                loadFragment(analyticsFragment);
            } else if (id == R.id.nav_profile) {
                loadFragment(profileFragment);
            }


            return true;
        });

        } catch (Exception e) {
            Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
        }



    }


    private void loadFragment(Fragment fragmentToShow) {
        if (fragmentToShow == null || activeFragment == fragmentToShow) return;

        getSupportFragmentManager().beginTransaction()
                .hide(activeFragment)
                .show(fragmentToShow)
                .commitNow();  // executes immediately

        activeFragment = fragmentToShow;
    }




    public void updateUserLoginStatus(String phone, Boolean status) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy & HH:mm:ss", Locale.getDefault());
        String currentDateTime = sdf.format(new Date());

        Map<String, Object> updates = new HashMap<>();
        updates.put("isLoggedIn", status);
        if (!status) {
            updates.put("lastLoggedOutDate", currentDateTime);
        } else {
            updates.put("lastLoggedInDate", currentDateTime);
        }

        db.collection("HisaabAnalyser")
                .document("users")  // Fetching from "users" document
                .collection("userList")  // Collection inside "users"
                .document(phone)
                .update(updates)
                .addOnFailureListener(e ->
                        System.out.println(e.getMessage())
                );
    }


    private boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }


    public void createWindowWelcome() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.welcome_widnow, null);
        try {

            AppCompatButton start=popup.findViewById(R.id.welcomeGotIt);
            ProgressBar progressBar=popup.findViewById(R.id.startprogress);


            Display display = getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - 55;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;

            PopupWindow popupWindow = new PopupWindow(popup);
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getY() < height || motionEvent.getY() > view.getBottom())
                        return true;
                    return false;
                }
            });
            popupWindow.update();
            start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    progressBar.setVisibility(View.VISIBLE);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            popupWindow.dismiss();
                        }
                    }, 3000);

                }
            });


            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    utils.clearDim(root);
                }
            });

            RelativeLayout layout = findViewById(R.id.first_parent_layout);
            layout.post(new Runnable() {
                @Override
                public void run() {

                   utils.applyDim(root, 0.5f);
                    popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }


            });
            splogin.edit().putString("comebackmsg", "").apply();
        } catch (Exception e) {
            Toast.makeText(first.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }






    private void scheduleAlarm(int hour, int min, int rcode) {
        // Get the current time
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, min);
        calendar.set(Calendar.SECOND, 0);

        // If the scheduled time is in the past, set it for the next day
        if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Get the AlarmManager service
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        // Create an intent for the AlarmReceiver
        // Create an intent for the AlarmReceiver
        Intent intent = new Intent(first.this, AlarmReceiver.class);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this,
                rcode,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        // Set a repeating alarm to trigger at the same time every day

        alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                pendingIntent
        );
    }
/*
    private void scheduleAlarmWithSafety(Context context, Calendar calendar, int requestCode, Intent intent) {
        try {
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null) {
                PendingIntent pendingIntent = PendingIntent.getBroadcast(
                        context,
                        requestCode,
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
                );

                // Set repeating alarm
                alarmManager.setExactAndAllowWhileIdle(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis(),
                        pendingIntent
                );

                // Optional: Force the first alarm immediately if time already passed
                if (calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    alarmManager.setExactAndAllowWhileIdle(
                            AlarmManager.RTC_WAKEUP,
                            System.currentTimeMillis() + 1000, // 1 second later
                            pendingIntent
                    );
                }

                // Debug
                Log.d("Alarm", "Alarm scheduled for " + calendar.getTime());
            } else {
                Log.e("Alarm", "AlarmManager is null!");
            }
        } catch (SecurityException se) {
            // Handle the security exception gracefully
            Log.e("Alarm", "Cannot set alarm due to security restrictions: " + se.getMessage());
            Toast.makeText(context, se.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            Log.e("Alarm", "Error setting alarm: " + e.getMessage());
        }
    }*/

    private void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = context.getString(R.string.channel_name1);
            String description = context.getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel channel =
                    new NotificationChannel("notify1", name, importance);

            channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC); // Correct ✔
            channel.setDescription(description);

            NotificationManager notificationManager =
                    context.getSystemService(NotificationManager.class);

            notificationManager.createNotificationChannel(channel);
        }
    }



    private void checkAndRequestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!am.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);


                Intent i=new Intent(this,SettingActivity.class);
                startActivity(i);
            }

        }

        if (ContextCompat.checkSelfPermission(first.this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(first.this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    1001);
Toast.makeText(this,"Expense Reminder is off",Toast.LENGTH_LONG).show();
        } else {

        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1001) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    createNotificationChannel(first.this);
                    schedule();
                } catch (Exception e) {

                }
                Toast.makeText(first.this, "Notification Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, notify user or handle it
                Toast.makeText(first.this, "Notification Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void schedule() {

        // Format: scheduleAlarm(hour, minute, requestCode);
        if (spitem.getBoolean("allowalarm", false)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                checkAndRequestNotificationPermission();
            }
                scheduleAlarm(0, 40, 34);   // Hour 0 -> requestCode 34
            scheduleAlarm(6, 0, 1);     // Hour 6 -> requestCode 1
                //  scheduleAlarm(1, 3, 7);     // Hour 1 -> requestCode 7
      /*  scheduleAlarm(1, 4, 28);    // Hour 1 alternative -> requestCode 28
        scheduleAlarm(2, 33, 17);   // Hour 2 -> requestCode 17
        scheduleAlarm(4, 35, 23);   // Hour 4 -> requestCode 23
        scheduleAlarm(5, 0, 18);    // Hour 5 -> requestCode 18

      */
             scheduleAlarm(12, 0, 7);    // Hour 12 -> requestCode 7
                scheduleAlarm(7, 0, 2);     // Hour 7 -> requestCode 2
                scheduleAlarm(8, 0, 3);     // Hour 8 -> requestCode 3
                // scheduleAlarm(9, 19, 4);    // Hour 9 -> requestCode 4
                scheduleAlarm(10, 0, 5);    // Hour 10 -> requestCode 5
                // scheduleAlarm(11, 0, 6);    // Hour 11 -> requestCode 6

                scheduleAlarm(13, 0, 28);   // Hour 13 -> requestCode 28
                scheduleAlarm(14, 52, 8);   // Hour 14 -> requestCode 8
                scheduleAlarm(15, 43, 21);    // Hour 15 -> requestCode 9
                scheduleAlarm(16, 46, 31);   // Hour 16 -> requestCode 31
                scheduleAlarm(17, 0, 10);   // Hour 17 -> requestCode 10
                // scheduleAlarm(17, 30, 24);  // Hour 17 alternative -> requestCode 24
                 scheduleAlarm(18, 12, 19);   // Hour 18 -> requestCode 11
                //   scheduleAlarm(18, 30, 19);  // Hour 18 alternative -> requestCode 19
                //scheduleAlarm(19, 0, 12);   // Hour 19 -> requestCode 12
                 scheduleAlarm(20, 0, 20);   // Hour 20 -> requestCode 20
                //scheduleAlarm(21, 0, 14);   // Hour 21 -> requestCode 14
                 scheduleAlarm(21, 40, 23);  // Hour 21 alternative -> requestCode 23
                //scheduleAlarm(22, 27, 15);  // Hour 22 -> requestCode 15
                scheduleAlarm(23, 20, 16);  // Hour 23 -> requestCode 16

            }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sharedPreferences.edit().putBoolean("incoming_fromAnotherApp", false).apply();
        String user = spitem.getString("user", "");
        sharedPreferences.edit().putBoolean(user + "allowapp", true).apply();
    }

    @Override
    public void onPause() {
        super.onPause();
        sharedPreferences.edit().putBoolean("incoming_fromAnotherApp", false).apply();
        String user = spitem.getString("user", "");
        sharedPreferences.edit().putBoolean(user + "allowapp", false).apply();
    }


    public void checkForUpdate(Activity context) {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(context);

// Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

// Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                appUpdateManager.startUpdateFlowForResult(
                        // Pass the intent that is returned by 'getAppUpdateInfo()'.
                        appUpdateInfo,
                        // an activity result launcher registered via registerForActivityResult
                        activityResultLauncher,
                        // Or pass 'AppUpdateType.FLEXIBLE' to newBuilder() for
                        // flexible updates.
                        AppUpdateOptions.newBuilder(AppUpdateType.IMMEDIATE).build());
            }
        });

        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartIntentSenderForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        // handle callback
                        if (result.getResultCode() != RESULT_OK) {

                            // If the update is canceled or fails,
                            // you can request to start the update again.
                        }
                    }
                });

    }



    @Override
    public void onBackPressed() {
        if (!back) {
            back = true;
            Toast.makeText(first.this, "Press back again to exit", Toast.LENGTH_SHORT).show();
        } else if (back) {
            finishAffinity();

        } else {
            super.onBackPressed();
        }
    }

/*
    @Override
    protected void onResume() {
        super.onResume();

*//*
        try {
            SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
            if (sp.getString("theme", "dark").equals("pink")) {
                if (!splogin.getString(spitem.getString("user", "") + "img", "").equals("")) {
                    user_img.setImageBitmap(decodeStringToBitmap(splogin.getString(spitem.getString("user", "") + "img", "")));
                    user_img.setBackgroundColor(getColor(R.color.white));
                } else {
                    user_img.setImageResource(R.drawable.baseline_person_24);
                    //user_img.setBackgroundColor(getColor(R.color.primarypink2));
                }
            }
            else {
                if (!splogin.getString(spitem.getString("user", "") + "img", "").equals("")) {
                    user_img.setImageBitmap(decodeStringToBitmap(splogin.getString(spitem.getString("user", "") + "img", "")));
                    user_img.setBackgroundColor(getColor(R.color.white));
                } else {
                    user_img.setImageResource(R.drawable.baseline_person_24);
                 //   user_img.setBackgroundColor(getColor(R.color.primarydark2));
                }
            }
*//*



*//*

        } catch (Exception e) {
            user_img.setImageResource(R.drawable.baseline_person_24);
        }
*//*

        try{
            String hname = "";
            for (int i = 0; i < n.length(); i++) {
                if (i == 0) {
                    hname += n.charAt(i);
                    hname = hname.toUpperCase();
                } else {
                    hname += n.charAt(i);
                }
            }
            //user_text.setText("Hi " + spitem.getString("name", hname) + ",");
            head = spitem.getString("user", "");

        }
        catch (Exception e){}



    }*/

    private void changetheme() {
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_checked },    // active
                new int[] { -android.R.attr.state_checked }    // inactive
        };

        int[] colors;


        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        if (sp.getString("theme", "dark").equals("pink")) {
            getWindow().setNavigationBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            bottomNavigationView.setBackgroundTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.backgroundlight)));

            colors = new int[] {
                    getResources().getColor(R.color.primarylight), // active icon color
                    getResources().getColor(R.color.gray)          // inactive icon color
            };

            bottomNavigationView.setBackgroundColor(
                    getResources().getColor(R.color.backgroundlight));


            hr.setBackgroundColor(getResources().getColor(R.color.first_view_bg_light));

            bottomNavigationView.setItemActiveIndicatorColor(
                    ColorStateList.valueOf(getResources().getColor(R.color.primarylight)));

            bottomNavigationView.setItemBackground(getResources().getDrawable(R.drawable.temp_light));

           // layout.setBackgroundColor(getResources().getColor(R.color.primarylight));
            layout.setBackground(getResources().getDrawable(R.drawable.violetbackground));
            bottomNavigationView.setItemIconTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.primarylight)));
            getWindow().setNavigationBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        } else {
            bottomNavigationView.setBackgroundTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.backgrounddark)));
            hr.setBackgroundColor(getResources().getColor(R.color.first_view_bg_dark));

            bottomNavigationView.setBackgroundColor(
                    getResources().getColor(R.color.backgrounddark));

            bottomNavigationView.setItemBackground(getResources().getDrawable(R.drawable.temp_dark));



            Window window = getWindow();
            window.setNavigationBarColor(getColor(R.color.black));
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            window.getDecorView().setSystemUiVisibility(0);

            bottomNavigationView.setItemActiveIndicatorColor(
                    ColorStateList.valueOf(getResources().getColor(R.color.white)));

            layout.setBackgroundColor(getResources().getColor(R.color.primarydark));





            bottomNavigationView.setItemIconTintList(
                    ColorStateList.valueOf(getResources().getColor(R.color.white)));

            colors = new int[] {
                    getResources().getColor(R.color.white), // active icon color
                    getResources().getColor(R.color.gray)   // inactive icon color
            };




        }

        ColorStateList iconColors = new ColorStateList(states, colors);
        bottomNavigationView.setItemIconTintList(iconColors);
        bottomNavigationView.setItemTextColor(iconColors);

    }


    @Override
    protected void onResume() {
        super.onResume();

        changetheme();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("ACTIVE_FRAGMENT_TAG", activeFragment.getTag());
    }

}