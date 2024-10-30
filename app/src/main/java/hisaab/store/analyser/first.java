package hisaab.store.analyser;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hisaabcalculator.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class first extends AppCompatActivity {
    TextView t1, t2, t3, t4, delbut, user_text, yearly;
    Toolbar toolbar;
    CoordinatorLayout layout;
    String currentVersion;
    String head = "";
    AdView adView;
    ShapeableImageView user_img;
    boolean back = false;
    AlarmManager alarmManager;
    AdjustSizeConfiguration displaysize;
    Configuration config;
    AppBarLayout appBarLayout;
    ViewGroup root;
    LinearLayout innerlayout;
    SharedPreferences sp, spitem;
    int REQUEST_IMAGE_PICKER = 1;
    String n;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);


        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.rateapp) {
            final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
            }
        } else if (item.getItemId() == R.id.support) {
            Intent i = new Intent(first.this, Contact.class);
            startActivity(i);
        } else if (item.getItemId() == R.id.detectpayment) {

            showAccessibilityWindow();
        } else if (item.getItemId() == R.id.tellfriend) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "Try 'Hisaab Analyser : Daily Expense Tracker' app.\nIt helps you track your daily, monthly, and yearly expenses with a clear graphical view.\nIt shows how much you spent, where, and on what items or services very clearly.\n" +
                    "You can store items with their prices ,add expenses for services like transportation,medical,etc and keep the record forever in your phone safely.\n\nThe app provides clear graph of your spending, available money, and money received. It is customizable and really useful!\n\n" +
                    "Check it out here: https://play.google.com/store/apps/details?id=hisaab.store.analyser\n" +
                    "\n\nNote : The app only stores records , no real money involves."
            );
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        } else if (item.getItemId() == R.id.reset_menu) {
            sp = getSharedPreferences("login", MODE_PRIVATE);
            sp.edit().putInt("count", 0).apply();
            sp.edit().putBoolean("showwarn", true).apply();
            reset("all");
        } else if (item.getItemId() == R.id.reset_photo) {
            reset("photo");
        } else if (item.getItemId() == R.id.reset_name) {
            createWindowname();
        } else if (item.getItemId() == R.id.theme) {
            createWindowTheme();
        } else if (item.getItemId() == R.id.reset_warn) {
            WarnMessage warnMessage = new WarnMessage();
            warnMessage.createWindow(first.this);


        } else if (item.getItemId() == R.id.logout_menu) {
            open();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        try {
            displaysize = new AdjustSizeConfiguration(first.this);
            config = getResources().getConfiguration();
            t1 = findViewById(R.id.fb1);
            t2 = findViewById(R.id.fb2);
            appBarLayout = findViewById(R.id.first_appbar);

            user_text = findViewById(R.id.first_toolbar_txt);
            user_img = findViewById(R.id.first_toolbar_img);

            adView = findViewById(R.id.adViewfirst);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

            t3 = findViewById(R.id.fb3);
            innerlayout = findViewById(R.id.innerlayout_first);
            t4 = findViewById(R.id.fb4);


            alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

                    checkAndRequestNotificationPermission();
                }

                createNotificationChannel(first.this);
                schedule();

            } catch (Exception e) {
                Toast.makeText(first.this, e.toString(), Toast.LENGTH_SHORT).show();
            }
            delbut = findViewById(R.id.delete_but_home);
            yearly = findViewById(R.id.fbyear);
            root = (ViewGroup) getWindow().getDecorView().getRootView();
//ca-app-pub-1079506490540577/9563671110
        } catch (Exception e) {

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.primarydark));
        }
        try {
            sp = getSharedPreferences("login", MODE_PRIVATE);
            spitem = getSharedPreferences("item", MODE_PRIVATE);
            n = spitem.getString("name", "").split(" ")[0];

            //  changetheme();

            if (!sp.getBoolean("islogged", false)) {
                Intent i = new Intent(first.this, MainActivity.class);
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
            user_text.setText("Hii " + hname + ",");
            head = spitem.getString("user", "");
            toolbar = findViewById(R.id.first_toolbar);
            setSupportActionBar(toolbar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toolbar.setTitleTextColor(getColor(R.color.white));
            }

        } catch (Exception e) {

        }
        try {
            if (!sp.getString(spitem.getString("user", "") + "img", "").equals("")) {
                user_img.setPadding(0, 0, 0, 0);
                user_img.setImageBitmap(decodeStringToBitmap(sp.getString(spitem.getString("user", "") + "img", "")));
            } else {
                user_img.setImageResource(R.drawable.baseline_person_24_white);
                user_img.setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
            user_img.setPadding(0, 0, 0, 0);
            user_img.setImageResource(R.drawable.baseline_person_24_white);
        }

        changetheme();
        checkOrientation();
        user_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    openImagePicker();
                } catch (Exception e) {

                }
            }
        });


        t1.setOnClickListener(view -> {
            Intent i1 = new Intent(this, item.class);
            i1.putExtra("head", n);
            startActivity(i1);
        });

        t2.setOnClickListener(view -> {
            Intent i2 = new Intent(this, display.class);
            i2.putExtra("head", n);
            startActivity(i2);
        });
        yearly.setOnClickListener(view -> {
            Intent i2 = new Intent(this, Yearly.class);
            i2.putExtra("head", n);
            startActivity(i2);
        });
        t3.setOnClickListener(view -> {
            Intent i3 = new Intent(this, monthly.class);
            i3.putExtra("head", n);
            startActivity(i3);
        });

        ButtonEffect buttonEffect = new ButtonEffect(first.this);
        buttonEffect.buttonEffect(t1);
        buttonEffect.buttonEffect(t2);
        buttonEffect.buttonEffect(t3);
        buttonEffect.buttonEffect(t4);

        buttonEffect.buttonEffect(yearly);
        buttonEffect.buttonEffect(delbut);

        t4.setOnClickListener(view -> {
            Intent i4 = new Intent(this, money.class);
            i4.putExtra("head", n);
            startActivity(i4);
        });
        delbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    open2();
                } catch (Exception e) {

                }


            }
        });
    }


    public static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.CYAN, PorterDuff.Mode.SRC_OVER);
                        v.invalidate();
                        v.animate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                    default:
                        v.getBackground().clearColorFilter();
                }
                return false;
            }
        });
    }

    public void open() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setMessage("Do you want to logout?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gi = new Intent(first.this, MainActivity.class);
                sp.edit().putBoolean("islogged", false).apply();
                startActivity(gi);
            }
        });

        a.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alerts = a.create();
        alerts.show();
    }

    public void open2() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setMessage("Do you want to delete your account?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                int year = Integer.parseInt(date3);
                SharedPreferences sharedPreferences = getSharedPreferences(head + year, MODE_PRIVATE);
                sharedPreferences.edit().putString("balance", "0").apply();
                Intent gi = new Intent(first.this, MainActivity.class);
                sp.edit().putBoolean("islogged", false).apply();
                spitem = getSharedPreferences("item", MODE_PRIVATE);
                n = spitem.getString("user", "");
                deleteUserFile(n);
                deleteUserData(n);
                sp.edit().putInt("count", 0).apply();
                sp.edit().putBoolean("showwarn", true).apply();

                SharedPreferences spusercount = getSharedPreferences("login", MODE_PRIVATE);
                int serial = spusercount.getInt("usercount", 2) - 1;
                spusercount.edit().putInt("usercount", serial).apply();


                for (int j = 0; j < 28; j++) {
                    SharedPreferences sp = getSharedPreferences(head + String.valueOf(Integer.parseInt(String.valueOf(2024 + j))), MODE_PRIVATE);
                    sp.edit().putString("balance", "0").apply();
                }

                startActivity(gi);
                finishAffinity();


            }
        });

        a.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alerts = a.create();
        alerts.show();
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


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            try {
                // Get the URI of the selected image
                assert data.getData() != null;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                // Display the selected image in ImageView
                user_img.setPadding(0, 0, 0, 0);
                sp.edit().putString(spitem.getString("user", "") + "img", encodeBitmapToString(bitmap)).apply();
                user_img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private static String encodeBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    private static Bitmap decodeStringToBitmap(String encodedBitmap) {
        byte[] decodedByteArray = Base64.decode(encodedBitmap, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

    private boolean isNightMode(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES;
    }


    public void deleteUserFile(String head) {
        File path = getApplicationContext().getFilesDir();
        File[] files = path.listFiles();


        if (files != null) {
            for (File file : files) {
                // Check if the file name contains the phone number
                if (file.getName().startsWith(head + "balance") || file.getName().startsWith(head + "monthlySpend") || file.getName().startsWith(head + "hisaab") || file.getName().startsWith(head + "monthlyGet") || file.getName().startsWith(head + "yearlyGet") || file.getName().startsWith(head + "yearlySpend")) {
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
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(phoneNumber + "img");
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error removing image", Toast.LENGTH_LONG).show();
        }

        if (!file.exists()) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
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
                    Toast.makeText(this, "Account successfully deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to rename temp file", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Failed to delete original file", Toast.LENGTH_LONG).show();
            }

            if (!isDeleted) {
                Toast.makeText(this, "User account not found", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IO exception occurred while deleting user data", Toast.LENGTH_LONG).show();
        }
    }


    public void changeName(String head, String newname) {
        File path = getApplicationContext().getFilesDir();
        File file = new File(path, "valid2.txt");
        File tempFile = new File(path, "valid2_temp.txt");


        if (!file.exists()) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)))) {

            String line;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split(" ");
                if (arr[0].equals(head)) {
                    arr[2] = newname;
                    bw.write(String.join(" ", arr));
                    bw.newLine();
                } else {
                    bw.write(line);
                    bw.newLine();
                }
            }

            // Close readers/writers
            bw.flush();
            bw.close();
            br.close();

            // Delete the original file and rename the temp file
            if (file.delete()) {
                if (tempFile.renameTo(file)) {
                    Toast.makeText(this, "Name successfully changed", Toast.LENGTH_SHORT).show();
                    spitem.edit().putString("name", newname).apply();
                    user_text.setText("Hii " + newname + ",");

                } else {
                    Toast.makeText(this, "Failed to change username", Toast.LENGTH_LONG).show();
                }
            } else {
                //Toast.makeText(this, "Failed to delete original file", Toast.LENGTH_LONG).show();
            }


        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IO exception occurred while deleting user data - " + e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void reset(String key) {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        if (key.equals("all")) {
            a.setMessage("This action will erase all your hisaab data! Do you want to proceed?");
            a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                    int year = Integer.parseInt(date3);
                    SharedPreferences sharedPreferences = getSharedPreferences(head + year, MODE_PRIVATE);
                    sharedPreferences.edit().putString("balance", "0").apply();
                    spitem = getSharedPreferences("item", MODE_PRIVATE);
                    n = spitem.getString("user", "");
                    deleteUserFile(n);
                    Toast.makeText(first.this, "Account reset successfully ", Toast.LENGTH_SHORT).show();

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


                        sp.edit().putString(spitem.getString("user", "") + "img", "").apply();
                        user_img.setImageResource(R.drawable.baseline_person_24_white);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            user_img.setStrokeColor(ColorStateList.valueOf(getColor(R.color.strokecolor)));

                            //user_img.setBackgroundColor(getColor(R.color.transparent));
                        }

                        Toast.makeText(first.this, "photo removed ", Toast.LENGTH_SHORT).show();
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

        dark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
                sp.edit().putString("theme", "dark").apply();
                changetheme();

                popupWindow.dismiss();
            }
        });

        pink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
                sp.edit().putString("theme", "pink").apply();
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
        layout = findViewById(R.id.first_parent_layout);
        layout.post(new Runnable() {
            @Override
            public void run() {

                applyDim(root, 0.5f);
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });
    }


    public void createWindowname() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        layout = findViewById(R.id.first_parent_layout);
        layout.post(new Runnable() {
            @Override
            public void run() {

                applyDim(root, 0.5f);
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });
    }

    public void applyDim(@NonNull ViewGroup parent, float dimAmount) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
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
        Intent intent = new Intent(this, AlarmReceiver.class);


        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, rcode, intent, PendingIntent.FLAG_IMMUTABLE);

        // Set a repeating alarm to trigger at the same time every day
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(),
                AlarmManager.INTERVAL_DAY, // Repeat every 24 hours
                pendingIntent
        );
    }


    private void createNotificationChannel(Context context) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name1);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("notify1", name, importance);
            channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_PUBLIC);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }


    }


    private void checkAndRequestNotificationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.POST_NOTIFICATIONS},
                    1001);
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
                Toast.makeText(this, "Notification Permission Granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, notify user or handle it
                Toast.makeText(this, "Notification Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void schedule() {
        scheduleAlarm(6, 0, 1);
        scheduleAlarm(7, 0, 2);
        scheduleAlarm(8, 0, 3);
        // scheduleAlarm(8, 30,26);

        scheduleAlarm(9, 0, 4);
//        scheduleAlarm(9, 30,27);
        scheduleAlarm(10, 0, 5);

        //  scheduleAlarm(10, 30,22);

        scheduleAlarm(11, 0, 6);

        scheduleAlarm(12, 2, 19);
        scheduleAlarm(12, 52, 7);
        scheduleAlarm(13, 14, 28);

        scheduleAlarm(14, 0, 8);
        // scheduleAlarm(7, 33,30);

        scheduleAlarm(15, 0, 9);
        //scheduleAlarm(15, 30,21);
        scheduleAlarm(16, 0, 31);
        //scheduleAlarm(17, 0,10);
        scheduleAlarm(17, 0, 24);
        scheduleAlarm(18, 0, 11);
        //scheduleAlarm(18, 30,19);

        scheduleAlarm(19, 0, 12);
        scheduleAlarm(20, 0, 20);

        // scheduleAlarm(20, 40,13);
        scheduleAlarm(21, 0, 14);
        scheduleAlarm(3, 35, 23);

        scheduleAlarm(22, 27, 15);
        scheduleAlarm(23, 20, 16);

        scheduleAlarm(1, 8, 17);
        scheduleAlarm(0, 23, 34);
        scheduleAlarm(5, 0, 18);
    }


    private void changetheme() {
        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        CollapsingToolbarLayout collapsingbar = findViewById(R.id.collapsing_first_page);
        CoordinatorLayout layout1 = findViewById(R.id.first_parent_layout);
        LottieAnimationView bubble = findViewById(R.id.s2);
        if (sp.getString("theme", "dark").equals("pink")) {

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarypink));
            }
            // background
            //   innerlayout.setBackgroundColor(getColor(R.color.backgroundpink));
            layout1.setBackgroundResource(R.color.backgroundpink);
            appBarLayout.setBackgroundResource(R.drawable.collapsebackgroundpink);
            collapsingbar.setContentScrimColor(getColor(R.color.primarypink));
            t1.setTextColor(getColor(R.color.white));
            t2.setTextColor(getColor(R.color.white));
            t3.setTextColor(getColor(R.color.white));
            t4.setTextColor(getColor(R.color.white));
            delbut.setTextColor(getColor(R.color.white));
            yearly.setTextColor(getColor(R.color.white));
            bubble.setVisibility(View.VISIBLE);
            t1.setBackgroundResource(R.drawable.border5);
            t2.setBackgroundResource(R.drawable.border5);
            t3.setBackgroundResource(R.drawable.border5);
            t4.setBackgroundResource(R.drawable.border5);
            delbut.setBackgroundResource(R.drawable.border5);
            yearly.setBackgroundResource(R.drawable.border5);

            user_img.setStrokeColorResource(R.color.strokecolorpink);


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }

            layout1.setBackgroundColor(getColor(R.color.backgrounddark));
            collapsingbar.setContentScrimColor(getColor(R.color.primarypink));
            appBarLayout.setBackground(getDrawable(R.drawable.collapsebackground));
            collapsingbar.setContentScrimColor(getColor(R.color.primarydark));
            t1.setTextColor(getColor(R.color.black));
            bubble.setVisibility(View.GONE);
            t2.setTextColor(getColor(R.color.black));
            t3.setTextColor(getColor(R.color.black));
            t4.setTextColor(getColor(R.color.black));
            delbut.setTextColor(getColor(R.color.black));
            yearly.setTextColor(getColor(R.color.black));
            user_img.setStrokeColorResource(R.color.strokecolordark);
            t1.setBackgroundResource(R.drawable.border2);
            t2.setBackgroundResource(R.drawable.border2);
            t3.setBackgroundResource(R.drawable.border2);
            t4.setBackgroundResource(R.drawable.border2);
            delbut.setBackgroundResource(R.drawable.border2);
            yearly.setBackgroundResource(R.drawable.border2);
        }

    }


    // Method to prompt user to enable the Accessibility Service
    private void promptEnableAccessibilityService() {
        if (!isAccessibilityServiceEnabled()) {
            sp.edit().putBoolean("accessibility_feature", true).apply();
        }
        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
        startActivity(intent); // This will open the Accessibility settings screen

    }

    private boolean isAccessibilityServiceEnabled() {
        String service = getPackageName() + "/" + AppMonitorService.class.getCanonicalName();
        int accessibilityEnabled = 0;
        try {
            accessibilityEnabled = Settings.Secure.getInt(
                    getContentResolver(),
                    Settings.Secure.ACCESSIBILITY_ENABLED);
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        TextUtils.SimpleStringSplitter splitter = new TextUtils.SimpleStringSplitter(':');
        if (accessibilityEnabled == 1) {
            String enabledServices = Settings.Secure.getString(
                    getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (enabledServices != null) {
                splitter.setString(enabledServices);
                while (splitter.hasNext()) {
                    String componentName = splitter.next();
                    if (componentName.equalsIgnoreCase(service)) {
                        return true; // Service is enabled
                    }
                }
            }
        }

        return false; // Service is not enabled
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences = getSharedPreferences("hisaab", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("incoming_fromAnotherApp", false).apply();
        SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
        String user = spitem.getString("user", "");
        sharedPreferences.edit().putBoolean(user + "allowapp", true).apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences = getSharedPreferences("hisaab", MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("incoming_fromAnotherApp", false).apply();
        SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
        String user = spitem.getString("user", "");
        sharedPreferences.edit().putBoolean(user + "allowapp", false).apply();

    }


    public void showAccessibilityWindow() {

        ViewGroup root = (ViewGroup) getWindow().getDecorView().getRootView();

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.accessibility_permission_window, null);

        ImageView pback = popup.findViewById(R.id.popup_accessibility_close);
        CheckBox checkBox = popup.findViewById(R.id.popup_accessibility_check);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView = popup.findViewById(R.id.adView);
        adView.loadAd(adRequest);
        AppCompatButton enable = popup.findViewById(R.id.popup_accessibility_enable);
        AppCompatButton disable = popup.findViewById(R.id.popup_accessibility_disable);
        AppCompatButton cancel = popup.findViewById(R.id.popup_accessibility_cancel);
        SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
        if (!isAccessibilityServiceEnabled()) {
            sp.edit().putBoolean("accessibility_feature", false).apply();
        }
        if (sp.getBoolean("accessibility_feature", false)) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 55;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // Create and show the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popup,
                width,
                height,
                true);
        popupWindow.setOutsideTouchable(false); // Disable dismiss on outside touch
        popupWindow.setFocusable(true); // Ensure popup can receive touch events
        popupWindow.setBackgroundDrawable(null);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getY() < height || motionEvent.getY() > view.getBottom())
                    return true;
                return false;
            }
        });
        popupWindow.update();


        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    if (!isAccessibilityServiceEnabled()) {
                        sp.edit().putBoolean("accessibility_feature", true).apply();
                        Toast.makeText(first.this, "enable accessibility service!", Toast.LENGTH_SHORT).show();
                    } else {
                        sp.edit().putBoolean("accessibility_feature", true).apply();
                    }
                } else {
                    if (isAccessibilityServiceEnabled()) {
                        sp.edit().putBoolean("accessibility_feature", false).apply();
                    }
                }
            }
        });

        enable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox.isChecked()) {
                    if (isAccessibilityServiceEnabled()) {
                        // If already enabled, show a toast
                        Toast.makeText(getApplicationContext(), "Accessibility Service is already enabled.", Toast.LENGTH_SHORT).show();
                    } else {

                        promptEnableAccessibilityService();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "Check to agree this feature", Toast.LENGTH_SHORT).show();
                }
            }
        });

        disable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkBox.isChecked()) {
                    if (!isAccessibilityServiceEnabled()) {
                        // If already enabled, show a toast
                        Toast.makeText(getApplicationContext(), "Accessibility Service is already disabled.", Toast.LENGTH_SHORT).show();
                    } else {

                        promptEnableAccessibilityService();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Uncheck to proceed", Toast.LENGTH_SHORT).show();
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                if (checkBox.isChecked()) {
                    if (!isAccessibilityServiceEnabled()) {
                        Toast.makeText(first.this, "Feature not in use, enable accessibility service to use the feature", Toast.LENGTH_SHORT).show();
                    }
                }
                clearDim(root);
            }
        });

        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        CoordinatorLayout layout = findViewById(R.id.first_parent_layout);


        popupWindow.setOutsideTouchable(false);


        applyDim(root, 0.5f);
        popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

    }
    private void checkOrientation() {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(t1, 67);
            displaysize.setfixWidth(t2, 67);
            displaysize.setfixWidth(t3, 67);
            displaysize.setfixWidth(t4, 67);
            displaysize.setfixWidth(yearly, 67);
            displaysize.setfixWidth(delbut, 67);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            displaysize.setfixWidth(t1, 93);
            displaysize.setfixWidth(t2, 93);
            displaysize.setfixWidth(t3, 93);
            displaysize.setfixWidth(t4, 93);
            displaysize.setfixWidth(yearly, 93);
            displaysize.setfixWidth(delbut, 93);

//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }

}
