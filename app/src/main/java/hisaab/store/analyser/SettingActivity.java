package hisaab.store.analyser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.AlarmManager;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.skydoves.colorpickerview.ColorPickerDialog;
import com.skydoves.colorpickerview.listeners.ColorEnvelopeListener;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class SettingActivity extends AppCompatActivity {
    TextView rateapp,resetall,changename,changetheme_,tellfriends,removephoto,deleteacc,cautionmsg,about,themetitle;
    SharedPreferences splogin,spitem;
    Switch allowremainderswitch;
    LinearLayout toolbar;
    public static UserViewModel viewModel;
    ImageView back;
    RelativeLayout parentlayout;
    ProgressBar progressBar;
    ViewGroup root;

    String n="";
    String head="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        try {
            rateapp = findViewById(R.id.setting_rateapp);
            about=findViewById(R.id.setting_about);
            viewModel = new ViewModelProvider(this).get(UserViewModel.class);
            resetall = findViewById(R.id.setting_resetall);
            back=findViewById(R.id.setting_back);
            allowremainderswitch=findViewById(R.id.settingremaindercheckbox);
            themetitle=findViewById(R.id.setting_theme_title);
            changename = findViewById(R.id.setting_changename);
            cautionmsg=findViewById(R.id.setting_cautionmsg);
            changetheme_ = findViewById(R.id.setting_changetheme);
            parentlayout = findViewById(R.id.settingfragment_layout);
            spitem =getSharedPreferences("item", MODE_PRIVATE);
            toolbar = findViewById(R.id.setting_toolbar);
            tellfriends = findViewById(R.id.setting_tell_your_friends);
            removephoto = findViewById(R.id.seting_removephoto);
            deleteacc = findViewById(R.id.setting_deleteaccount);
            splogin = getSharedPreferences("login",MODE_PRIVATE);
            root = (ViewGroup) getWindow().getDecorView().getRootView();
            progressBar = findViewById(R.id.first_progress_);

            head = spitem.getString("user", "");
            n = head;
            if (!spitem.getBoolean("allowalarm", true)) {
                allowremainderswitch.setChecked(false);
            }
            else{
                checkAndRequestNotificationPermission();
            }

            allowremainderswitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                   if(isChecked)
                    checkAndRequestNotificationPermission();
                   else{
                       spitem.edit().putBoolean("allowalarm",false).apply();
                   }

                }
            });


            TextView pickColorBtn = findViewById(R.id.btnPickColor);
            RelativeLayout layout = findViewById(R.id.settingfragment_layout);

            pickColorBtn.setOnClickListener(v -> {
                new ColorPickerDialog.Builder(SettingActivity.this)
                        .setTitle("Pick a Color")
                        .setPreferenceName("MyColorPicker")
                        .setPositiveButton("Select", (ColorEnvelopeListener) (envelope, fromUser) -> {
                            int selectedColor = envelope.getColor();
                            String hexCode = "#" + envelope.getHexCode();
                            SharedPreferences sp2 = getSharedPreferences("theme", MODE_PRIVATE);
                            sp2.edit().putString("background", hexCode).apply();
                            sp2.edit().putString("thememode","custom").apply();
                            changetheme();
                            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                            ClipData clip = ClipData.newPlainText("Color Code", hexCode);
                            clipboard.setPrimaryClip(clip);

                            Toast.makeText(this, "Selected color " + hexCode, Toast.LENGTH_SHORT).show();

                        })
                        .setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss())
                        .attachAlphaSlideBar(true)
                        .attachBrightnessSlideBar(true)
                        .show();
            });



            changetheme();

            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();

                }
            });

            about.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(SettingActivity.this,AboutPage.class);
                    startActivity(intent);
                }
            });
            rateapp.setOnClickListener(v -> {
                final String appPackageName = getPackageName();
                try {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                } catch (android.content.ActivityNotFoundException anfe) {
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=" + appPackageName)));
                }
            });


            tellfriends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Try 'Hisaab Analyser : Daily Expense Tracker' app.\n\nIt helps you track your daily, monthly, and yearly expenses with a very clear graphical view.\nIt shows how much you spent, where, and on what items or services. Later you can download your record into PDF.\n\nAlso view your expenses for any date,month or year and analyse deeply.\n\n" +
                            "Check it out here: https://play.google.com/store/apps/details?id=hisaab.store.analyser" +
                            "\n\nNote : The app only stores records , no real money involves. And it is completely free."
                    );
                    sendIntent.setType("text/plain");
                    startActivity(sendIntent);
                }
            });


            resetall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    splogin.edit().putInt("count", 0).apply();
                    splogin.edit().putBoolean("showwarn", true).apply();
                    reset("all");
                }
            });

            removephoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    reset("photo");
                }
            });

            changename.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createWindowname();
                }
            });

            deleteacc.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open2();
                }
            });





            changetheme_.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createWindowTheme();
                }
            });
        }catch (Exception e){
            Toast.makeText(SettingActivity.this,e.toString(),Toast.LENGTH_LONG).show();
        }

    }

    public void reset(String key) {
        AlertDialog.Builder a = new AlertDialog.Builder(SettingActivity.this);
        if (key.equals("all")) {
            a.setMessage("This action will erase all your hisaab data! Do you want to proceed?");
            a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                    int year = Integer.parseInt(date3);
                    SharedPreferences sharedPreferences = getSharedPreferences(head + year, MODE_PRIVATE);
                    sharedPreferences.edit().putString("balance", "0").apply();
                    String n= spitem.getString("user", "");
                    deleteUserFile(n);
                    Toast.makeText(SettingActivity.this, "Account reset successfully ", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
            });

            a.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

        } else {

            a.setMessage("Do you want to remove your photo?");
            a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {


                        splogin.edit().putString(spitem.getString("user", "") + "img", "").apply();
                        first.viewModel.setProfileImage("");
                        Toast.makeText(SettingActivity.this, "photo removed ", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    } catch (Exception e) {

                    }


                }
            });

            a.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });


        }

        AlertDialog alerts = a.create();
        alerts.show();
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Change button text colors only in Dark Mode
            alerts.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(SettingActivity.this, android.R.color.white));
            alerts.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(SettingActivity.this, android.R.color.white));
        }
    }

    public void deleteUserFile(String head) {
        File path =getApplicationContext().getFilesDir();
        File[] files = path.listFiles();


        if (files != null) {
            for (File file : files) {
                // Check if the file name contains the phone number
                if (file.getName().startsWith(head + "balance") || file.getName().startsWith(head + "monthlySpend") || file.getName().startsWith(head + "hisaab") || file.getName().startsWith(head + "monthlyGet") || file.getName().startsWith(head + "yearlyGet") || file.getName().startsWith(head + "yearlySpend") || file.getName().startsWith(head)) {
                    // Attempt to delete the file
                    if (file.delete()) {
                    }
                }
            }

        }
    }

    public void deleteUserData(String phoneNumber) {
        File path = getApplicationContext().getFilesDir();
        File file = new File(path, "valid2.txt");
        File tempFile = new File(path, "valid2_temp.txt");

        // Remove shared preferences entry
        try {
            SharedPreferences.Editor editor = splogin.edit();
            editor.remove(phoneNumber + "img");
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "Error removing image", Toast.LENGTH_LONG).show();
        }

        if (!file.exists()) {
            Toast.makeText(SettingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)))) {

            String line;
            boolean isDeleted = false;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split("_");
                if (!arr[0].equals(phoneNumber)) {
                    bw.write(line);
                    bw.newLine();
                } else {
                    isDeleted = true;
                }
            }

            // Close readers/writers
            bw.flush();
            bw.close();
            br.close();

            // Delete the original file and rename the temp file
            if (file.delete()) {
                if (tempFile.renameTo(file)) {
                    Toast.makeText(SettingActivity.this, "User deleted successfully!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SettingActivity.this, "Failed to rename temp file", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(SettingActivity.this, "Failed to delete original file", Toast.LENGTH_LONG).show();
            }

            if (!isDeleted) {
                Toast.makeText(SettingActivity.this, "User account not found", Toast.LENGTH_LONG).show();
            }
            clearDim(root);
            progressBar.setVisibility(View.GONE);

        } catch (IOException e) {
            e.printStackTrace();
            clearDim(root);
            Toast.makeText(SettingActivity.this, "IO exception occurred while deleting user data", Toast.LENGTH_LONG).show();
        }
    }
    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }



    public void open2() {
        AlertDialog.Builder a = new AlertDialog.Builder(SettingActivity.this);
        a.setMessage("Do you want to delete your account?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                applyDim(root, 0.5f);
                progressBar.setVisibility(View.VISIBLE);
                deleteUserFromFirestore(spitem.getString("user", "00000000"));
            }
        });

        a.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alerts = a.create();
        alerts.show();
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Change button text colors only in Dark Mode
            alerts.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(SettingActivity.this, android.R.color.white));
            alerts.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(SettingActivity.this, android.R.color.white));
        }
    }
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }
    public void deleteUserFromFirestore(String phone) {
        if(isInternetAvailable()) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("HisaabAnalyser")
                    .document("users")  // Fetching from "users" document
                    .collection("userList")  // Collection inside "users"
                    .document(phone)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                        int year = Integer.parseInt(date3);
                        SharedPreferences sharedPreferences = getSharedPreferences(head + year, MODE_PRIVATE);
                        sharedPreferences.edit().putString("balance", "0").apply();
                        Intent gi = new Intent(SettingActivity.this, MainActivity.class);
                        splogin.edit().putBoolean("islogged", false).apply();
                        n = spitem.getString("user", "");
                        deleteUserFile(n);
                        deleteUserData(n);
                        splogin.edit().putInt("count", 0).apply();
                        splogin.edit().putBoolean("showwarn", true).apply();

                        SharedPreferences spusercount = getSharedPreferences("login", MODE_PRIVATE);
                        int serial = spusercount.getInt("usercount", 2) - 1;
                        spusercount.edit().putInt("usercount", serial).apply();


                        for (int j = 0; j < 28; j++) {
                            SharedPreferences sp = getSharedPreferences(head + String.valueOf(Integer.parseInt(String.valueOf(2024 + j))), MODE_PRIVATE);
                            sp.edit().putString("balance", "0").apply();
                        }

                        startActivity(gi);
                        finishAffinity();


                    })
                    .addOnFailureListener(e -> {
                        if (isInternetAvailable()) {
                            String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                            int year = Integer.parseInt(date3);
                            SharedPreferences sharedPreferences = getSharedPreferences(head + year,MODE_PRIVATE);
                            sharedPreferences.edit().putString("balance", "0").apply();
                            Intent gi = new Intent(SettingActivity.this, MainActivity.class);
                            splogin.edit().putBoolean("islogged", false).apply();
                            spitem = getSharedPreferences("item", MODE_PRIVATE);
                            n = spitem.getString("user", "");
                            deleteUserFile(n);
                            deleteUserData(n);
                            splogin.edit().putInt("count", 0).apply();
                            splogin.edit().putBoolean("showwarn", true).apply();

                            SharedPreferences spusercount = getSharedPreferences("login", MODE_PRIVATE);
                            int serial = spusercount.getInt("usercount", 2) - 1;
                            spusercount.edit().putInt("usercount", serial).apply();


                            for (int j = 0; j < 28; j++) {
                                SharedPreferences sp = getSharedPreferences(head + String.valueOf(Integer.parseInt(String.valueOf(2024 + j))), MODE_PRIVATE);
                                sp.edit().putString("balance", "0").apply();
                            }

                            startActivity(gi);
                            finishAffinity();
                            progressBar.setVisibility(View.GONE);
                            clearDim(root);
                        }
                        else{
                            Toast.makeText(SettingActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            clearDim(root);
                        }

                    });
        }
        else{
            Toast.makeText(SettingActivity.this, "Check your internet connection!", Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
            clearDim(root);
        }

    }
    public void applyDim(@NonNull ViewGroup parent, float dimAmount) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public void createWindowname() {
        try {
            LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popup = inflater.inflate(R.layout.change_namelayoutdesign, null);

            AppCompatButton changebut = popup.findViewById(R.id.changename_but);
            EditText newname = popup.findViewById(R.id.changename_edit);
            ImageView close = popup.findViewById(R.id.changename_close);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdView adView = popup.findViewById(R.id.adViewchangename);
            adView.loadAd(adRequest);

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
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            changebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nm = newname.getText().toString().trim();
                    newname.setText("");
                    changeName(head, nm);
                    popupWindow.dismiss();
                }
            });


            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    clearDim(root);
                }
            });
            RelativeLayout layout = findViewById(R.id.settingfragment_layout);
            layout.post(new Runnable() {
                @Override
                public void run() {

                    applyDim(root, 0.5f);
                    popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            });
        }catch (Exception e){
            Toast.makeText(SettingActivity.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }
    public void changeName(String head, String newname) {
        File path = getApplicationContext().getFilesDir();
        File file = new File(path, "valid2.txt");
        File tempFile = new File(path, "valid2_temp.txt");

        if (!file.exists()) {
            Toast.makeText(SettingActivity.this, "Something went wrong", Toast.LENGTH_LONG).show();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)))) {

            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split("_");

                if (arr.length >= 5 && arr[0].equals(head)) { // Ensure array has enough elements
                    arr[2] = newname;
                    arr[4] = newname;
                    bw.write(String.join("_", arr));  // Use "_" instead of space
                    found = true;
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }

            // Close readers/writers before renaming files
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(SettingActivity.this, "IO exception occurred: " + e.toString(), Toast.LENGTH_LONG).show();
            return;
        }

        // Delete original file and rename temp file
        if (file.delete() && tempFile.renameTo(file)) {
            Toast.makeText(SettingActivity.this, "Name successfully changed", Toast.LENGTH_SHORT).show();
            spitem.edit().putString("name", newname).apply();
            splogin.edit().putString("fullname", newname).apply();
            onBackPressed();
        } else {
            Toast.makeText(SettingActivity.this, "Failed to change username", Toast.LENGTH_LONG).show();
        }
    }
    public void createWindowTheme() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.change_themelayoutdesign, null);

        ImageView close = popup.findViewById(R.id.changetheme_close);
        TextView dark = popup.findViewById(R.id.changetheme_dark);
        TextView pink = popup.findViewById(R.id.changetheme_pink);

        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = popup.findViewById(R.id.adViewchangetheme);
        adView.loadAd(adRequest);

        Display display =getWindowManager().getDefaultDisplay();
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
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sptheme = getSharedPreferences("theme",MODE_PRIVATE);
                sptheme.edit().putString("theme", "dark").apply();
                sptheme.edit().putString("thememode","default").apply();
                changetheme();

                popupWindow.dismiss();
            }
        });

        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sptheme = getSharedPreferences("theme",MODE_PRIVATE);
                sptheme.edit().putString("theme", "pink").apply();
                sptheme.edit().putString("thememode","default").apply();
                changetheme();

                popupWindow.dismiss();
            }
        });

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clearDim(root);
            }
        });
        RelativeLayout layout =findViewById(R.id.settingfragment_layout);
        layout.post(new Runnable() {
            @Override
            public void run() {

                applyDim(root, 0.5f);
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });
    }


    private void changetheme() {
        LinearLayout remianderlayout=findViewById(R.id.settingremainderlayout);
        View[] buttons = new View[]{
                removephoto, changename, changetheme_, deleteacc, tellfriends,
                rateapp, resetall, cautionmsg, about,remianderlayout
        };
        SharedPreferences sptheme = getSharedPreferences("theme",MODE_PRIVATE);
        TextView toolbartext=findViewById(R.id.settings_toolbar_txt);
        TextView remainderheader=findViewById(R.id.setting_reminder_header);
        TextView remiandertitle=findViewById(R.id.setting_remaindertitle);

        String savedHex = sptheme.getString("background", "#262627");
        if(sptheme.getString("thememode","default").equals("custom")) {
            parentlayout.setBackgroundColor(Color.parseColor(savedHex));
            toolbar.setBackgroundColor(Color.parseColor(savedHex));
        } //ffe969ff

        if (sptheme.getString("theme", "dark").equals("pink")) {
            allowremainderswitch.setTrackTintList(
                    ContextCompat.getColorStateList(this, R.color.switch_track_color)
            );

            allowremainderswitch.setThumbTintList(
                    ContextCompat.getColorStateList(this, R.color.switch_thumb_color)
            );

           getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
           getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            if(!sptheme.getString("thememode","default").equals("custom")) {
                parentlayout.setBackgroundColor(getColor(R.color.backgroundlight));
                toolbar.setBackgroundColor(getColor(R.color.backgroundlight));
            }

            toolbartext.setTextColor(getColor(R.color.black));
            removephoto.setTextColor(getColor(R.color.black));
            changename.setTextColor(getColor(R.color.black));
            remiandertitle.setTextColor(getColor(R.color.black));
            changetheme_.setTextColor(getColor(R.color.black));
            deleteacc.setTextColor(getColor(R.color.black));
            tellfriends.setTextColor(getColor(R.color.black));
            rateapp.setTextColor(getColor(R.color.black));
            remainderheader.setTextColor(getColor(R.color.black));
            resetall.setTextColor(getColor(R.color.black));
            cautionmsg.setTextColor(getColor(R.color.black));
            about.setTextColor(getColor(R.color.black));
            back.setImageTintList(ColorStateList.valueOf(getColor(R.color.black)));

            for (View btn : buttons) {
                if (btn != null) {
                    btn.setBackgroundTintList(
                            ContextCompat.getColorStateList(this, R.color.settingiconbackgroundlight)
                    );

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        btn.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
                    } else {
                        btn.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }
            themetitle.setTextColor(getColor(R.color.black));
            //msg.setBackground(getActivity().getDrawable(R.drawable.background_selector_light));;


        } else {
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolordark));
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if(!sptheme.getString("thememode","default").equals("custom")) {
                parentlayout.setBackgroundColor(getColor(R.color.backgrounddark));
                toolbar.setBackgroundColor(getColor(R.color.backgrounddark));
            }
            toolbartext.setTextColor(getColor(R.color.white));
            back.setImageTintList(ColorStateList.valueOf(getColor(R.color.white)));
            removephoto.setTextColor(getColor(R.color.white));
            changename.setTextColor(getColor(R.color.white));
            changetheme_.setTextColor(getColor(R.color.white));
            deleteacc.setTextColor(getColor(R.color.white));
            tellfriends.setTextColor(getColor(R.color.white));
            remiandertitle.setTextColor(getColor(R.color.white));
            remainderheader.setTextColor(getColor(R.color.white));
            rateapp.setTextColor(getColor(R.color.white));
            resetall.setTextColor(getColor(R.color.white));
            cautionmsg.setTextColor(getColor(R.color.white));
            about.setTextColor(getColor(R.color.white));
            themetitle.setTextColor(getColor(R.color.white));

            allowremainderswitch.setTrackTintList(
                    ContextCompat.getColorStateList(this, R.color.switch_track_color_dark)
            );

            allowremainderswitch.setThumbTintList(
                    ContextCompat.getColorStateList(this, R.color.switch_thumb_color_dark)
            );


            for (View btn : buttons) {
                if (btn != null) {
                    btn.setBackgroundTintList(
                            ContextCompat.getColorStateList(this, R.color.primarydark)
                    );

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        btn.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
                    } else {
                        btn.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
                    }
                }
            }

        }

    }



    SharedPreferences.Editor editor;

    private void checkAndRequestNotificationPermission() {

        editor = spitem.edit();

        boolean isPermitted = true;
        // Default true, will set false if denied

        // Android 12+ → Exact Alarm Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!am.canScheduleExactAlarms()) {
                isPermitted = false;
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivity(intent);
            }
        }

        // Android 13+ → Notification Runtime Permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, android.Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {


                isPermitted = false;
             Toast.makeText(this,"Allow notification to get reminder",Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(
                        this,
                        new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                        1001
                );
            }
        }

        // Save result to SharedPreferences
        editor.putBoolean("allowalarm", isPermitted);
        editor.apply();
        if(isPermitted) allowremainderswitch.setChecked(true);
        else allowremainderswitch.setChecked(false);
    }


    }

