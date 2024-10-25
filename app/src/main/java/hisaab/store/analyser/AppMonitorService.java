package hisaab.store.analyser;

import android.accessibilityservice.AccessibilityService;
import android.app.ActivityManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;

import java.util.List;
import java.util.Random;

public class AppMonitorService extends AccessibilityService {

    private static final String TAG = "AppMonitorService";
    private String lastPackageName = "";
    private boolean isPhonePeOpened = false;
    private boolean messagedisplay=false;
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        Random random = new Random();
        String packageName = String.valueOf(event.getPackageName());
        SharedPreferences sharedPreferences = getSharedPreferences("hisaab", MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
        String user = spitem.getString("user", "");
        boolean allow = true;

        if (event.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) {
            allow = sharedPreferences.getBoolean(user + "allowapp", true);
            if ((packageName.equals("com.phonepe.app") ||
                    packageName.equals("net.one97.paytm") ||
                    packageName.equals("com.google.android.apps.nbu.paisa.user")) && !isPhonePeOpened) {
                Toast.makeText(getApplicationContext(), "Add upcoming expenses to Hisaab Analyser!", Toast.LENGTH_LONG).show();
                    isPhonePeOpened = true;
                    messagedisplay=true;
            }
            else if (!packageName.equals("hisaab.store.analyser") && isPhonePeOpened && allow && ((lastPackageName.equals("com.android.launcher") || (lastPackageName.equals("com.coloros.smartsidebar"))) && !packageName.equals("com.android.launcher") && (
                    !packageName.equals("com.phonepe.app") &&
                    !packageName.equals("net.one97.paytm") &&
                    !packageName.equals("com.google.android.apps.nbu.paisa.user"))
                    )) {
                isPhonePeOpened = false;
                sharedPreferences.edit().putBoolean("incoming_fromAnotherApp", true).apply();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent launchIntent = getPackageManager().getLaunchIntentForPackage("hisaab.store.analyser");
                        if (launchIntent != null) {
                            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(launchIntent);
                            messagedisplay=false;
                            Toast.makeText(getApplicationContext(), "Add your expenses!", Toast.LENGTH_LONG).show();
                        }
                    }
                }, 100);
            }

        }
        lastPackageName = packageName;
    }


    @Override
    public void onInterrupt() {
        // Handle interrupt
    }

}
