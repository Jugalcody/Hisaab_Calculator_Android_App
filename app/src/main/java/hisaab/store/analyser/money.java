package hisaab.store.analyser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class money extends AppCompatActivity {
    String n;
    Button b;
    EditText e, moneySource;
    ImageView back;
    Utils utils;
    TextView itempoint;
    AdView adView;
    AdjustSizeConfiguration displaysize;
    Configuration config;
    SharedPreferences sp, sp2;
    String[] montharr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
    TextView t;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        try {
            displaysize = new AdjustSizeConfiguration(money.this);
            config = getResources().getConfiguration();
            Bundle bb = getIntent().getExtras();
            itempoint = findViewById(R.id.money_point);
            moneySource = findViewById(R.id.money_from);
            utils = new Utils();

            b = findViewById(R.id.mb);
            ButtonEffect buttonEffect = new ButtonEffect(money.this);
            buttonEffect.buttonEffect(b);
            e = findViewById(R.id.me);
            t = findViewById(R.id.mt);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            sp2 = getSharedPreferences("item", MODE_PRIVATE);
            n = sp2.getString("user", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            SharedPreferences coin = getSharedPreferences(n + "coin", MODE_PRIVATE);
            int curpoint = coin.getInt("point", 0);
            itempoint.setText(curpoint + "P");
            File path = getApplicationContext().getFilesDir();
            back = findViewById(R.id.money_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            adView = findViewById(R.id.adViewmoney);
            // adView.setAdUnitId("ca-app-pub-1079506490540577/7402312139");
            //adView.setAdSize(getAdSize());
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            changetheme();
            String mon = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
            String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());

            checkOrientation();
            try {
                File f = new File(path, n + "balance.txt");
                if (!f.exists()) f.createNewFile();
                String date2 = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
                int d2 = Integer.parseInt(date2);
                File f2 = new File(path, (n + "monthlyGet" +montharr[Integer.parseInt(mon)]+year + ".txt"));
                if (!f2.exists()) f2.createNewFile();
                String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                int d3 = Integer.parseInt(date3);
                File f3 = new File(path, (n + "yearlyGet" + d3 + ".txt"));
                if (!f3.exists()) f3.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            t.setText("Total balance : " + totalBalance());
            b.setOnClickListener(view -> {
                        String e1 = e.getText().toString();
                        if (!e1.trim().equals("") && !moneySource.getText().toString().trim().equals("")) {
                            if (Integer.parseInt(e1.trim()) == 0) {
                                Toast.makeText(this, "0 cannot be add!", Toast.LENGTH_SHORT).show();
                            } else {
                                open(e1);
                            }
                 /*           int point=curpoint-2;
                            if(point<0) point=0;
                            coin.edit().putInt("point",point).apply();
                            itempoint.setText(point+"P");
                            Toast.makeText(money.this,"-2",Toast.LENGTH_SHORT).show();*/
                        } else {
                            Toast.makeText(this, "empty field!", Toast.LENGTH_LONG).show();
                        }
                    }
            );

        } catch (Exception e) {

        }

    }

    public void addHistory(String amount, String source) {
        if (amount.isEmpty() || source.isEmpty()) return;

        String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        String month = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        File path = getApplicationContext().getFilesDir();
        String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());

        try {
            File history = new File(path, n + utils.getMonthName(Integer.parseInt(month)) + year + "money_history.txt");
            if (!history.exists()) history.createNewFile();

            int newSno = 1;

            // Read last Sno if exists
            BufferedReader br = new BufferedReader(new FileReader(history));
            String line, lastLine = null;
            while ((line = br.readLine()) != null) {
                lastLine = line;
            }
            br.close();

            if (lastLine != null) {
                String[] arr = lastLine.split("#");
                if (arr.length == 4) {
                    try {
                        newSno = Integer.parseInt(arr[3]) + 1;
                    } catch (Exception ignored) {}
                }
            }

            // New entry with Sno appended at the end
            String entry = source + "#" + "Rs." + amount + "#" + date + "#" + newSno + "\n";

            FileOutputStream fos = new FileOutputStream(history, true);
            fos.write(entry.getBytes());
            fos.close();

            moneySource.setText("");

        } catch (Exception e) {
            Toast.makeText(this, "History update failed!", Toast.LENGTH_SHORT).show();
        }
    }


    public void open2() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setMessage("Do you want to clear your balance?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                clear2();
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

    public void open() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setMessage("Do you want to clear your balance?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

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

    public void clear2() {
        File path = getApplicationContext().getFilesDir();
        try {
            FileOutputStream f = new FileOutputStream(new File(path, n + "balance.txt"));
            f.write("".getBytes());
            t.setText("Total balance : 0");
        } catch (IOException e) {
            Toast.makeText(this, "unable to clear", Toast.LENGTH_LONG).show();
        }
    }

    public void clear3() {
        File path = getApplicationContext().getFilesDir();
        try {
            String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);
            FileOutputStream f = new FileOutputStream(new File(path, n + "monthlyGet" + d + ".txt"));
            f.write("".getBytes());

        } catch (IOException e) {
            Toast.makeText(this, "unable to clear", Toast.LENGTH_LONG).show();
        }
    }

    public String totalBalance() {
        String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        int year = Integer.parseInt(date3);
        SharedPreferences sharedPreferences = getSharedPreferences(n + year, MODE_PRIVATE);
        File path = getApplicationContext().getFilesDir();
        String b3 = "0";
        try {
            File file = new File(path, n + "balance.txt");
            if (!file.exists()) file.createNewFile();
            FileInputStream f2 = new FileInputStream(file);
            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b3 = br.readLine();
            if (b3 == null) b3 = "0";
            f2.close();


        } catch (IOException e) {

            Toast.makeText(this, "empty balance", Toast.LENGTH_LONG).show();
        }
        sharedPreferences.edit().putString("balance", b3).apply();
        return b3;
    }

    public String updateBalance(String e1) {
        String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        int year = Integer.parseInt(date3);
        SharedPreferences sharedPreferences = getSharedPreferences(n + year, MODE_PRIVATE);
        File path = getApplicationContext().getFilesDir();
        String b3 = "0", b4;
        long l;
        try {
            File file = new File(path, n + "balance.txt");
            if (!file.exists()) file.createNewFile();
            FileInputStream f2 = new FileInputStream(file);

            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if (b4 == null) b4 = "0";
            f2.close();

            if (!e1.equals("")) {
                l = Long.parseLong(b4) + Long.parseLong(e1);
            } else {
                l = Long.parseLong(b4);
            }
            b3 = Long.toString(l);
            FileOutputStream f = new FileOutputStream(new File(path, n + "balance.txt"));
            f.write((b3).getBytes());
            f.close();


        } catch (IOException ee) {
            clear2();
            String e0 = e.getText().toString();
            t.setText("Total balance : " + updateBalance(e0));
            Toast.makeText(this, "Rs." + e0 + " added", Toast.LENGTH_LONG).show();
            e.setText("");
            monthlyUpdate(e0, 0);
            yearlyUpdate(e0, 0);
            finish();
        }


        sharedPreferences.edit().putString("balance", b3).apply();
        return b3;
    }

    public void monthlyUpdate(String e1, int rem) {
        File path = getApplicationContext().getFilesDir();
        String b3, b4;
        long l;
        try {
            String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            String date2 = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
          //  if (date2.charAt(0) == '0') date2 = String.valueOf(date2.charAt(1));
            File file = new File(path, n + "monthlyGet" + montharr[Integer.parseInt(date2)-1] + date + ".txt");
            if (!file.exists()) file.createNewFile();

            FileInputStream f2 = new FileInputStream(file);

            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if (b4 == null) b4 = "0";
            f2.close();

            if (!e1.equals("")) {
                l = Long.parseLong(b4) + Long.parseLong(e1) - rem;
            } else {
                l = Long.parseLong(b4);
            }
            b3 = Long.toString(l);
            FileOutputStream f = new FileOutputStream(file);
            f.write((b3).getBytes());
            f.close();


        } catch (IOException ee) {
            clear3();
        }
    }


    public void yearlyUpdate(String e1, int rem) {
        File path = getApplicationContext().getFilesDir();
        String b3, b4;
        long l;
        try {
            String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            int d3 = Integer.parseInt(date3);
            File file = new File(path, n + "yearlyGet" + d3 + ".txt");
            if (!file.exists()) {
                try {

                    if (file.createNewFile()) {
                    }
                } catch (Exception e) {
                }
            }
            FileInputStream f2 = new FileInputStream(file);


            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);

            b4 = br.readLine();

            if (b4 == null) b4 = "0";
            f2.close();

            if (!e1.equals("")) {
                l = Long.parseLong(b4) + Long.parseLong(e1) - rem;
            } else {
                l = Long.parseLong(b4);
            }
            b3 = Long.toString(l);
            FileOutputStream f = new FileOutputStream(file);
            f.write((b3).getBytes());
            f.close();


        } catch (IOException ee) {
            Toast.makeText(this, ee.toString(), Toast.LENGTH_LONG).show();
            clear3();
        }
    }


    private void changetheme() {
        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        LinearLayout layout = findViewById(R.id.money_container);
        RelativeLayout toolbar = findViewById(R.id.money_toolbar);
        TextView note1 = findViewById(R.id.money_note1);
        TextView note2 = findViewById(R.id.money_note2);
        TextView note3 = findViewById(R.id.money_note3);
        ImageView toolbarback = findViewById(R.id.money_back);
        TextView toolbartxt = findViewById(R.id.money_buytitle);
        /*String savedHex = sp.getString("background", "#262627");
        if(sp.getString("thememode","default").equals("custom")) {
            layout.setBackgroundColor(Color.parseColor(savedHex));
            toolbar.setBackgroundColor(Color.parseColor(savedHex));
        }*/
        if (sp.getString("theme", "dark").equals("pink")) {
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            itempoint.setTextColor(getColor(R.color.itempointpink));
            t.setTextColor(getColor(R.color.primarylight));
            toolbarback.setColorFilter(getColor(R.color.black));
            toolbartxt.setTextColor(getColor(R.color.black));
            note1.setTextColor(getColor(R.color.notecolorpink));
            note2.setTextColor(getColor(R.color.notecolorpink));
            note3.setTextColor(getColor(R.color.notecolorpink));
            e.setTextColor(getColor(R.color.green));
            b.setBackgroundResource(R.drawable.loginbutpink);
            e.setBackground(getResources().getDrawable(R.drawable.border6));
            moneySource.setBackground(getResources().getDrawable(R.drawable.border6));
            moneySource.setTextColor(getColor(R.color.black));
            toolbar.setBackgroundColor(getColor(R.color.backgroundlight));
            layout.setBackgroundColor(getColor(R.color.backgroundlight));
            /*if(!sp.getString("thememode","default").equals("custom")) {

                layout.setBackgroundColor(getColor(R.color.backgroundlight));
            }*/

        } else {
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolordark));
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            t.setTextColor(getColor(R.color.white));
            itempoint.setTextColor(getColor(R.color.itempointdark));
            toolbarback.setColorFilter(getColor(R.color.white));
            toolbartxt.setTextColor(getColor(R.color.white));
            note1.setTextColor(getColor(R.color.notecolordark));
            note2.setTextColor(getColor(R.color.notecolordark));
            note3.setTextColor(getColor(R.color.notecolordark));
            e.setBackground(getResources().getDrawable(R.drawable.itemlayoutbg_dark));
            moneySource.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));
            moneySource.setTextColor(getColor(R.color.white));
            b.setBackgroundResource(R.drawable.loginbut);
            e.setTextColor(getColor(R.color.green));


            layout.setBackgroundColor(getColor(R.color.backgrounddark));

            toolbar.setBackgroundColor(getColor(R.color.backgrounddark));
            /*if(!sp.getString("thememode","default").equals("custom")) {
                toolbar.setBackgroundColor(getColor(R.color.toolbardark));
                layout.setBackgroundColor(getColor(R.color.backgrounddark));
            }*/

        }

    }


    private void checkOrientation() {
        LinearLayout moneylayout = findViewById(R.id.moneylayoutcontent);
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(moneylayout, 70);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            displaysize.setfixWidth(moneylayout, 95);

//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }


    public void open(String value) {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setMessage("Are you sure you want to add Rs." + value + " to your balance?\n\nNote: Unlike expenses , individual income is not allowed to delete. So please confirm before add the balance.");
        a.setIcon(R.drawable.rs);
        a.setTitle("Add Balance");

        a.setPositiveButton("Yes", (dialogInterface, i) -> {
            String total = updateBalance(value);
            t.setText("Total balance : " + total);
            addHistory(value, moneySource.getText().toString());
            e.setText("");
            monthlyUpdate(value, 0);
            yearlyUpdate(value, 0);

            new Handler().postDelayed(() -> {
                AlertDialog.Builder success = new AlertDialog.Builder(this);
                success.setMessage("\nRs." + value + " is successfully added!\n\nYour total balance is now Rs." + total + "\n");
                success.setIcon(R.drawable.rs);
                success.setPositiveButton("Ok", null);
                success.setIcon(R.drawable.rs);
                success.setTitle("Congratulations!");

                AlertDialog successDialog = success.create();
                successDialog.show();

                utils.applyDialogTheme(successDialog,this,"balance");
            }, 250);
        });

        a.setNegativeButton("No", null);

        AlertDialog alerts = a.create();
        alerts.show();

        utils.applyDialogTheme(alerts,this,"balance"); // 🟣 Apply same theme
    }

    @Override
    protected void onResume() {
        super.onResume();
        changetheme();
        checkOrientation();
    }

}