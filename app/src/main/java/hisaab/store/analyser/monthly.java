package hisaab.store.analyser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class monthly extends AppCompatActivity {
    TextView t1, t2, t3,totalbalancetitle,datetitle;
    SharedPreferences sp, spitem;

    Utils utils;
    TextView rectitle;
    ImageView downloadpdf;
    ShowInterstitialAd intertitialAd;
    TextView spentmoneytitle;
    ViewGroup root;
    boolean isclicked = false;
    AdView adView;
    AppCompatButton reset, graph;

    AdjustSizeConfiguration displaysize;
    Configuration config;
    ImageView back;
    Button b;
    LinearLayout layout,datespinnerlayout;

    LinearLayout cardtmoney, cardrecmoney, cardspendmoney;
    String mon = "", year = "", head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        try {
            utils=new Utils();
            cardtmoney = findViewById(R.id.totalbalance_layout);
            cardrecmoney = findViewById(R.id.totalincome_layout);
            cardspendmoney = findViewById(R.id.totalspent_layout);
            displaysize = new AdjustSizeConfiguration(monthly.this);
            config = getResources().getConfiguration();
            b = findViewById(R.id.mondb);
            root = (ViewGroup) getWindow().getDecorView().getRootView();
            t1 = findViewById(R.id.monrec);
            rectitle = findViewById(R.id.monrectitle);
            spentmoneytitle = findViewById(R.id.monstitle);
            totalbalancetitle=findViewById(R.id.monavtitle);
            t2 = findViewById(R.id.mons);
            t3 = findViewById(R.id.monav);
            graph = findViewById(R.id.monthly_graph);
            datetitle=findViewById(R.id.monthly_datetitle);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            reset = findViewById(R.id.monthly_reset);
            downloadpdf = findViewById(R.id.monthlypdf);
            datespinnerlayout=findViewById(R.id.display_selection_layout);
            ButtonEffect buttonEffect = new ButtonEffect(monthly.this);
            buttonEffect.buttonEffect(reset);
            buttonEffect.buttonEffect(b);
            buttonEffect.buttonEffect(downloadpdf);
            buttonEffect.buttonEffect(graph);

            intertitialAd = new ShowInterstitialAd(monthly.this);
            intertitialAd.loadAd();

            checkOrientation();

            downloadpdf.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    AlertDialog.Builder a = new AlertDialog.Builder(monthly.this);

                    a.setMessage("Do you want to download the record of month wise analysis of " + year + " ?");

                    a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                            createPdf(year);
                            intertitialAd.showad();
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
                                .setTextColor(ContextCompat.getColor(monthly.this, android.R.color.white));
                        alerts.getButton(DialogInterface.BUTTON_NEGATIVE)
                                .setTextColor(ContextCompat.getColor(monthly.this, android.R.color.white));
                    }

                }
            });

            spitem = getSharedPreferences("item", MODE_PRIVATE);
            head = spitem.getString("user", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            back = findViewById(R.id.monthly_back);

            layout = findViewById(R.id.monthly_container);
            graph.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createWindowGraph();
                }
            });


            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open2();
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            t3.setText("Rs." + totalBalance());
            Spinner monthSpinner = findViewById(R.id.spinm2);
            Spinner yearSpinner = findViewById(R.id.spinm3);
            String[] arr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
            String[] arr2 = {"2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050"};
            SharedPreferences sptheme = getSharedPreferences("theme", MODE_PRIVATE);
            if (sptheme.getString("theme", "dark").equals("pink")) {
                SpinnerAdapter1 adapter = new SpinnerAdapter1(monthly.this, R.layout.spinner_login_pink, arr);
                monthSpinner.setAdapter(adapter);

                SpinnerAdapter1 adapter2 = new SpinnerAdapter1(monthly.this, R.layout.spinner_login_pink, arr2);
                yearSpinner.setAdapter(adapter2);



                yearSpinner.setPopupBackgroundDrawable(
                        new ColorDrawable(ContextCompat.getColor(this, R.color.white))
                );
                yearSpinner.setBackground(getResources().getDrawable(R.drawable.border6));


                monthSpinner.setPopupBackgroundDrawable(
                        new ColorDrawable(ContextCompat.getColor(this, R.color.white))
                );
                monthSpinner.setBackground(getResources().getDrawable(R.drawable.border6));

                downloadpdf.setBackground(getResources().getDrawable(R.drawable.circlebutpink));

            } else {
                downloadpdf.setBackground(getResources().getDrawable(R.drawable.circlebut));
                SpinnerAdapter1 adapter = new SpinnerAdapter1(monthly.this, R.layout.spinner_login, arr);
                monthSpinner.setAdapter(adapter);

                SpinnerAdapter1 adapter2 = new SpinnerAdapter1(monthly.this, R.layout.spinner_login, arr2);
                yearSpinner.setAdapter(adapter2);


                yearSpinner.setPopupBackgroundDrawable(
                        new ColorDrawable(ContextCompat.getColor(this, R.color.spinnerdarkcolor)));
                yearSpinner.setBackground(getResources().getDrawable(R.drawable.loginbut3));


                monthSpinner.setPopupBackgroundDrawable(
                        new ColorDrawable(ContextCompat.getColor(this, R.color.spinnerdarkcolor)));
                monthSpinner.setBackground(getResources().getDrawable(R.drawable.loginbut3));

            }


            Calendar now = Calendar.getInstance();
            monthSpinner.setSelection(now.get(Calendar.MONTH));
            mon = arr[now.get(Calendar.MONTH)];
            yearSpinner.setSelection(now.get(Calendar.YEAR) - 2024);
            year = arr2[now.get(Calendar.YEAR) - 2024];

            back=findViewById(R.id.monthly_back);
            changetheme();
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    year = parent.getItemAtPosition(i).toString();
                    b.setText("Show");
                    isclicked = false;
                    rectitle.setText("Total money received");
                    spentmoneytitle.setText("Total money spent");
                    t1.setText("----");
                    t2.setText("----");
                    reset.setVisibility(View.GONE);
                    b.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    mon = parent.getItemAtPosition(i).toString();
                    b.setText("Show");
                    isclicked = false;
                    b.setVisibility(View.VISIBLE);
                    rectitle.setText("Total money received");
                    spentmoneytitle.setText("Total money spend");
                    t1.setText("----");
                    t2.setText("----");
                    reset.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            b.setOnClickListener(view -> {
                if (!isclicked) {
                    t1.setText("Rs." + utils.monthlyGet(mon,this,year));
                    t2.setText("Rs." + utils.monthlySpend(mon,this,year));
                    rectitle.setText("Total money received in " + mon + " " + year);
                    spentmoneytitle.setText("Total money spent in " + mon + " " + year);
                    b.setText("Showed");
                    isclicked = true;
                    if (year.equals(arr2[now.get(Calendar.YEAR) - 2024]) && mon.equals(arr[now.get(Calendar.MONTH)])) {
                        reset.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {
Toast.makeText(monthly.this,e.toString(),Toast.LENGTH_SHORT).show();
        }


        adView = findViewById(R.id.adViewmonthly);
        //adView.setAdUnitId("ca-app-pub-1079506490540577/7402312139");
        //adView.setAdSize(getAdSize());
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    public int month(String m) {
        if (m.equals("Jan")) return 1;
        else if (m.equals("Feb")) return 2;
        else if (m.equals("Mar")) return 3;
        else if (m.equals("Apr")) return 4;
        else if (m.equals("May")) return 5;
        else if (m.equals("June")) return 6;
        else if (m.equals("July")) return 7;
        else if (m.equals("Aug")) return 8;
        else if (m.equals("Sept")) return 9;
        else if (m.equals("Oct")) return 10;
        else if (m.equals("Nov")) return 11;
        else return 12;
    }

    public String totalBalance() {
        File path = getApplicationContext().getFilesDir();
        String b3 = "0";
        try {
            FileInputStream f2 = new FileInputStream(new File(path, head + "balance.txt"));
            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b3 = br.readLine();
            if (b3 == null) b3 = "0";
            f2.close();


        } catch (IOException e) {

            Toast.makeText(this, "Empty Balance!", Toast.LENGTH_LONG).show();
        }
        return b3;
    }

    public void open2() {
        String monthlyget = utils.monthlyGet(mon, this, year);
        String monthlySpend = utils.monthlySpend(mon, this, year);
        Activity activity = this;

        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setTitle("Reset Monthly Data");
        a.setMessage(
                "Warning!\n\n" +
                        "All the expenses for " + mon + " " + year + " will be deleted.\n" +
                        "The total expenses of this month (" + "Rs." + monthlySpend + ") " +
                        "will be added back to your overall balance.\n\n" +
                        "Do you want to proceed?"
        );
        a.setIcon(R.drawable.baseline_warning_24);

        a.setPositiveButton("Yes", (dialogInterface, i) -> {
            File path = getApplicationContext().getFilesDir();

            // Define all files
            File yearlyGetFile = new File(path, head + "yearlyGet" + year + ".txt");
            File dailyIncomeFile = new File(path, head+mon+year+"money_history.txt");
            File yearlySpendFile = new File(path, head + "yearlySpend" + year + ".txt");
            File hisaabDataFile = new File(path, head + "hisaab" + mon + year + "data.txt");
            File monthlySpendFile = new File(path, head + "monthlySpend" + mon + year + ".txt");
            File monthlyGetFile = new File(path, head + "monthlyGet" + mon + year + ".txt");
            File monthlyHisaabFile = new File(path, head + "hisaab" + mon + year + "data.txt");

            try {
                // Ensure files exist
                if (!yearlyGetFile.exists()) yearlyGetFile.createNewFile();
                if (!dailyIncomeFile.exists()) dailyIncomeFile.createNewFile();
                if (!yearlySpendFile.exists()) yearlySpendFile.createNewFile();
                if (!hisaabDataFile.exists()) hisaabDataFile.createNewFile();
                if (!monthlySpendFile.exists()) monthlySpendFile.createNewFile();
                if (!monthlyGetFile.exists()) monthlyGetFile.createNewFile();
                if (!monthlyHisaabFile.exists()) monthlyHisaabFile.createNewFile();

                // Read yearly get
                FileInputStream f2 = new FileInputStream(yearlyGetFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(f2));
                String initalamt = br.readLine();
                if (initalamt == null) initalamt = "0";
                f2.close();

                // Clear monthly hisaab data
                FileOutputStream f3 = new FileOutputStream(hisaabDataFile);
                f3.write("".getBytes());
                f3.close();

                // Add back monthly received to total balance
                long updatedYearlyGet = Long.parseLong(initalamt) - Long.parseLong(monthlyget);
                FileOutputStream f4 = new FileOutputStream(yearlyGetFile);
                f4.write(String.valueOf(updatedYearlyGet).getBytes());
                f4.close();

                SharedPreferences sharedPreferences = getSharedPreferences(head + year, MODE_PRIVATE);
                sharedPreferences.edit().putString("balance", String.valueOf(updatedYearlyGet)).apply();

                // Update yearly spend
                FileInputStream f5 = new FileInputStream(yearlySpendFile);
                br = new BufferedReader(new InputStreamReader(f5));
                String initalspend = br.readLine();
                if (initalspend == null) initalspend = "0";
                f5.close();

                FileOutputStream f6 = new FileOutputStream(yearlySpendFile);
                f6.write(String.valueOf(Long.parseLong(initalspend) - Long.parseLong(monthlySpend)).getBytes());
                f6.close();

            } catch (IOException e) {
                e.printStackTrace();
            }

            // Reset monthly files
            try {
                FileOutputStream f7 = new FileOutputStream(monthlySpendFile);
                f7.write("0".getBytes());
                f7.close();

                f7 = new FileOutputStream(monthlyGetFile);
                f7.write("0".getBytes());
                f7.close();

                f7 = new FileOutputStream(monthlyHisaabFile);
                f7.write("".getBytes());
                f7.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            try{
                FileOutputStream f8 = new FileOutputStream(dailyIncomeFile);
                f8.write("".getBytes());
                f8.close();
            }
            catch (Exception e){}

            // Update UI titles and amounts
            rectitle.setText("Total money received in " + mon + " " + year);
            spentmoneytitle.setText("Total money spent in " + mon + " " + year);

            // Update remaining balance for display
            long remain = Long.parseLong(monthlyget) - Long.parseLong(monthlySpend);
            t3.setText("Rs." + String.valueOf(utils.deductMoney(remain, activity)));
            t1.setText("Rs.0");
            t2.setText("Rs.0");
            spitem.edit().putString("monthlyspent"+head,"0").apply();
        });

        a.setNegativeButton("No", (dialogInterface, i) -> {});

        AlertDialog alerts = a.create();
        alerts.show();

        // Optional: Dark mode button color handling
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            alerts.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(monthly.this, android.R.color.white));
            alerts.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(monthly.this, android.R.color.white));
        }
    }


    private void changetheme() {
        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        LinearLayout layout = findViewById(R.id.monthly_container);
        LinearLayout toolbar = findViewById(R.id.monthly_toolbar);
        ImageView toolbarback=findViewById(R.id.monthly_back);
        getWindow().setNavigationBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        TextView toolbartxt=findViewById(R.id.monthly_toolbar_txt);
        toolbarback.setColorFilter(getColor(R.color.black));
        toolbartxt.setTextColor(getColor(R.color.black));
        String savedHex = sp.getString("background", "#262627");
        if(sp.getString("thememode","default").equals("custom")) {
            layout.setBackgroundColor(Color.parseColor(savedHex));
            toolbar.setBackgroundColor(Color.parseColor(savedHex));
        }
        if (sp.getString("theme", "dark").equals("pink")) {
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            b.setBackgroundResource(R.drawable.loginbutpink);
            if(!sp.getString("thememode","default").equals("custom")) {
                layout.setBackgroundColor(getColor(R.color.backgroundlight));
                toolbar.setBackgroundColor(getColor(R.color.backgroundlight));
            }


            t1.setTextColor(getColor(R.color.white));
            graph.setBackgroundResource(R.drawable.loginbutpink);
            downloadpdf.setBackgroundResource(R.drawable.circlebutpink);
            cardtmoney.setBackgroundResource(R.drawable.gradient_card_pink);
           cardrecmoney.setBackgroundResource(R.drawable.gradient_card_pink);
            cardspendmoney.setBackgroundResource(R.drawable.gradient_card_pink);
            rectitle.setTextColor(getColor(R.color.monthyearcardtitlecolorlight));
            totalbalancetitle.setTextColor(getColor(R.color.monthyearcardtitlecolorlight));
            spentmoneytitle.setTextColor(getColor(R.color.monthyearcardtitlecolorlight));
            t2.setTextColor(getColor(R.color.monthyearcardtitlecolorlight));
            t3.setTextColor(getColor(R.color.monthyearcardtitlecolorlight));
            t1.setTextColor(getColor(R.color.lgreen));
            datespinnerlayout.setBackground(getResources().getDrawable(R.drawable.spinnerlayoutbg_light));
            datetitle.setTextColor(getResources().getColor(R.color.primarylight));
        } else {
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolordark));
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            if(!sp.getString("thememode","default").equals("custom")) {
                toolbar.setBackgroundColor(getColor(R.color.backgrounddark));
                layout.setBackgroundColor(getColor(R.color.backgrounddark));
            }
            datespinnerlayout.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));
            b.setBackgroundResource(R.drawable.loginbut);
            toolbarback.setColorFilter(getColor(R.color.white));
            toolbartxt.setTextColor(getColor(R.color.white));
            graph.setBackgroundResource(R.drawable.loginbut2);
            cardtmoney.setBackgroundResource(R.drawable.gradient_card);
            downloadpdf.setBackgroundResource(R.drawable.circlebut);
            cardrecmoney.setBackgroundResource(R.drawable.gradient_card);
            cardspendmoney.setBackgroundResource(R.drawable.gradient_card);
            getWindow().setNavigationBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(0);
            rectitle.setTextColor(getColor(R.color.monthyearcardtitlecolordark));
            totalbalancetitle.setTextColor(getColor(R.color.monthyearcardtitlecolordark));
            spentmoneytitle.setTextColor(getColor(R.color.monthyearcardtitlecolordark));
            t2.setTextColor(getColor(R.color.monthyearcardtitlecolordark));
            t3.setTextColor(getColor(R.color.monthyearcardtitlecolordark));
            t1.setTextColor(getColor(R.color.green));
            datetitle.setTextColor(getResources().getColor(R.color.white));

        }

    }


    void setupGraph(View view) {
        LineChart lineChart = view.findViewById(R.id.monthly_line_chart);

        // Prepare data for "Received" and "Spent" money
        List<Entry> receivedEntries = new ArrayList<>();
        List<Entry> spentEntries = new ArrayList<>();
        List<Entry> availableEntries = new ArrayList<>();

        SharedPreferences sp = getSharedPreferences(head + String.valueOf(Integer.parseInt(year) - 1), MODE_PRIVATE);
        String earlierBalance = sp.getString("balance", "0");

        String[] arr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        long[] spentMoney = new long[12];
        long[] receivedMoney = new long[12];
        long[] moneyAvailable = new long[12];

        for (int i = 0; i < 12; i++) {
            spentMoney[i] = Long.parseLong(utils.monthlySpend(arr[i],this,year));
            receivedMoney[i] = Long.parseLong(utils.monthlyGet(arr[i],this,year));
            // Calculating moneyAvailable as receivedMoney minus spentMoney
            if (i == 0) {
                System.out.println("earlymoney : " + earlierBalance + "\nrec : " + receivedMoney[i] + "\nspent : " + spentMoney[i] + "");
                moneyAvailable[i] = Long.parseLong(earlierBalance) + receivedMoney[i] - spentMoney[i];
            } else {
                moneyAvailable[i] = moneyAvailable[i - 1] + receivedMoney[i] - spentMoney[i];
            }

        }

        float maxReceived = getMax(receivedMoney) + 100;
        float maxSpent = getMax(spentMoney) + 100;
        float maxValue = Math.max(Math.max(maxReceived, maxSpent), getMax(moneyAvailable)) + 100;


        // Add entries for the chart (months are 0 to 11 for Jan to Dec)
        for (int i = 0; i < 12; i++) {
            receivedEntries.add(new Entry(i, receivedMoney[i]));
            spentEntries.add(new Entry(i, spentMoney[i]));
            availableEntries.add(new Entry(i, moneyAvailable[i]));
        }

        // Create datasets for the line chart
        LineDataSet receivedDataSet = new LineDataSet(receivedEntries, "Money Received");
        receivedDataSet.setColor(getColor(R.color.green)); // Set line color
        receivedDataSet.setLineWidth(2f);
        receivedDataSet.setCircleRadius(5f);
        receivedDataSet.setDrawValues(true); // Enable drawing values inside the graph
        receivedDataSet.setValueTextSize(10f); // Set label text size

        LineDataSet availableDataSet = new LineDataSet(availableEntries, "Available Money");
        availableDataSet.setColor(Color.YELLOW); // Set line color
        availableDataSet.setLineWidth(2f);
        availableDataSet.setCircleRadius(5f);
        availableDataSet.setDrawValues(true); // Enable drawing values inside the graph

        availableDataSet.setValueTextSize(10f); // Set label text size

        LineDataSet spentDataSet = new LineDataSet(spentEntries, "Money Spent");
        spentDataSet.setColor(Color.RED); // Set line color
        spentDataSet.setLineWidth(2f);
        spentDataSet.setCircleRadius(5f);
        spentDataSet.setDrawValues(true); // Enable drawing values inside the graph
        spentDataSet.setValueTextSize(10f); // Set label text size

        // Create LineData object to combine datasets
        LineData lineData = new LineData(receivedDataSet, spentDataSet, availableDataSet);

        lineData.setValueTextSize(10f); // Set value text size

        // Set data to the LineChart
        lineChart.setData(lineData);

        // Set up horizontal scroll and visible range
        lineChart.setVisibleXRangeMaximum(8); // Show 8 months at a time
        lineChart.setDragEnabled(true); // Enable dragging/panning
        lineChart.setPinchZoom(true); // Enable pinch zooming
        lineChart.setScaleEnabled(true); // Enable scaling/zooming

        lineChart.animateX(1500);
        lineChart.animateY(1500);

        // Customize chart axis labels
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(months));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        xAxis.setGranularity(1f); // Ensure each label corresponds to an entry
        xAxis.setLabelCount(15); // Total number of labels to show (all months)

        // Customize the Y axis
        YAxis leftAxis = lineChart.getAxisLeft();

        leftAxis.setAxisMinimum(0f); // Set minimum value for Y-axis
        leftAxis.setAxisMaximum(maxValue + 100f);

        // Configure granularity based on max value
        if (maxValue < 100) {
            leftAxis.setGranularity(1f);
            leftAxis.setLabelCount(20);
        } else if (maxValue < 1000) {
            leftAxis.setGranularity(10f);
            leftAxis.setLabelCount(50);
        } else if (maxValue < 10000) {
            leftAxis.setGranularity(10f);
            leftAxis.setLabelCount(100);
        } else if (maxValue < 100000) {
            leftAxis.setGranularity(10f);
            leftAxis.setLabelCount(150);
        } else {
            leftAxis.setGranularity(10f);
            leftAxis.setLabelCount(200);
        }

        leftAxis.setGranularityEnabled(true); // Ensure granularity is enabled
        lineChart.getAxisRight().setEnabled(false);

        // Setup custom legend view
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        View legendView = inflater.inflate(R.layout.legend_view, null);
        CheckBox checkboxReceived = legendView.findViewById(R.id.checkbox_received);
        CheckBox checkboxAvailable = legendView.findViewById(R.id.checkbox_available);
        LinearLayout container_legend=legendView.findViewById(R.id.legend_container);
        CheckBox checkboxSpent = legendView.findViewById(R.id.checkbox_spent);


        SharedPreferences sharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        if (!sharedPreferences.getString("theme", "dark").equals("dark")) {
            checkboxAvailable.setTextColor(getColor(R.color.black));
            checkboxReceived.setTextColor(getColor(R.color.black));
            checkboxSpent.setTextColor(getColor(R.color.black));
            container_legend.setBackgroundColor(getResources().getColor(R.color.white));

            receivedDataSet.setValueTextColor(Color.BLACK); // Set data point label color to white
            availableDataSet.setColor(Color.YELLOW); // Set line color
            availableDataSet.setValueTextColor(Color.BLACK); // Set data point label color to white
            xAxis.setTextColor(Color.BLACK); // Set label text color to white
            leftAxis.setTextColor(Color.BLACK); // Set Y-axis labels color to white
            lineData.setValueTextColor(Color.BLACK); // Set value text color for al
            spentDataSet.setValueTextColor(Color.BLACK); // Set data point label color to white


        }else{
            checkboxAvailable.setTextColor(getColor(R.color.white));
            checkboxReceived.setTextColor(getColor(R.color.white));
            checkboxSpent.setTextColor(getColor(R.color.white));
            container_legend.setBackgroundColor(getResources().getColor(R.color.backgrounddark));



            receivedDataSet.setValueTextColor(Color.WHITE); // Set data point label color to white
            availableDataSet.setColor(Color.YELLOW); // Set line color
            availableDataSet.setValueTextColor(Color.WHITE); // Set data point label color to white
            xAxis.setTextColor(Color.WHITE); // Set label text color to white
            leftAxis.setTextColor(Color.WHITE); // Set Y-axis labels color to white
            lineData.setValueTextColor(Color.WHITE); // Set value text color for al
            spentDataSet.setValueTextColor(Color.WHITE); // Set data point label color to white

        }
        // Set initial states for checkboxes and data visibility
        checkboxReceived.setChecked(true);
        checkboxAvailable.setChecked(true);
        checkboxSpent.setChecked(true);

        receivedDataSet.setVisible(true);
        availableDataSet.setVisible(true);
        spentDataSet.setVisible(true);

        // Add listeners for checkboxes
        checkboxReceived.setOnCheckedChangeListener((buttonView, isChecked) -> {
            receivedDataSet.setVisible(isChecked);
            lineChart.invalidate(); // Refresh chart
        });

        checkboxAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            availableDataSet.setVisible(isChecked);
            lineChart.invalidate(); // Refresh chart
        });

        checkboxSpent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spentDataSet.setVisible(isChecked);
            lineChart.invalidate(); // Refresh chart
        });

        // Add the legend view to your layout
        LinearLayout container = view.findViewById(R.id.legend_container);
        container.addView(legendView);

        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.invalidate(); // Refresh chart
    }


    private long getMax(long[] values) {
        long max = Long.MIN_VALUE;
        for (long value : values) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }


    public void createWindowGraph() {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.graphanalysis_design, null);
        PopupWindow popupWindow = new PopupWindow(popup);
        popupWindow.setContentView(popup);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        TextView yeartitle = popup.findViewById(R.id.monthly_line_chart_yeartitle);
        yeartitle.setVisibility(View.VISIBLE);
        yeartitle.setText("Year : " + year);
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        ImageView close = popup.findViewById(R.id.monthlygraph_close);
        LinearLayout graphcontainer = popup.findViewById(R.id.graph_container);
        SharedPreferences sharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        if (sharedPreferences.getString("theme", "dark").equals("dark")) {
            graphcontainer.setBackgroundColor(getColor(R.color.backgrounddark));
        } else {
            graphcontainer.setBackgroundColor(getColor(R.color.backgroundlight));
            close.setColorFilter(getColor(R.color.black));
        }

        setupGraph(popup);

        layout.post(new Runnable() {
            @Override
            public void run() {
                popupWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

/*

        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clearDim(root);
            }
        });
      */
    }


    private String[][] arrData;
    private String pdfFileName, yearStr;

    // Method to check permission and create PDF
    public void createPdf(String year) {
        yearStr = year;
        arrData = new String[12][4];
        SharedPreferences sp = getSharedPreferences(head + String.valueOf(Integer.parseInt(year) - 1), MODE_PRIVATE);
        String earlierBalance = sp.getString("balance", "0");
        String[] montharr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        long[] moneyAvailable = new long[12];
        for (int i = 0; i < 12; i++) {
            String[] innerarr = new String[4];
            innerarr[0] = montharr[i];
            innerarr[1] = utils.monthlySpend(montharr[i],this,year);
            innerarr[2] = utils.monthlyGet(montharr[i],this,year);
            if (i == 0) {
                innerarr[3] = String.valueOf(Long.parseLong(earlierBalance) + Long.parseLong(utils.monthlyGet(montharr[i],this,year)) - Long.parseLong(utils.monthlySpend(montharr[i],this,year)));
                moneyAvailable[0] = Long.parseLong(earlierBalance) + Long.parseLong(utils.monthlyGet(montharr[i],this,year)) - Long.parseLong(utils.monthlySpend(montharr[i],this,year));
            } else {
                moneyAvailable[i] = moneyAvailable[i - 1] + Long.parseLong(utils.monthlyGet(montharr[i],this,year)) - Long.parseLong(utils.monthlySpend(montharr[i],this,year));
                innerarr[3] = String.valueOf(moneyAvailable[i]);
            }

            arrData[i] = innerarr;
        }


        // Android 13 and above do not require WRITE_EXTERNAL_STORAGE permission.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            // Proceed with creating PDF in shared storage using MediaStore
            createPdfFileUsingMediaStore();
        } else {
            // Handle older versions with WRITE_EXTERNAL_STORAGE (if needed)
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            } else {
                createPdfFileLegacy();
            }
        }
    }

    // Handle the result of the permission request for legacy versions
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createPdfFileLegacy();
            } else {
                Toast.makeText(this, "Permission denied, can't create PDF!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Create the PDF file using MediaStore (Android 13+)
    // Create the PDF file using MediaStore (Android 13+)
    private void createPdfFileUsingMediaStore() {
        try {
            ContentValues values = new ContentValues();
            pdfFileName = "MonthlyExpenses_" + year + "_" + spitem.getString("name", "");
            String path = "/HisaabAnalyser/" + spitem.getString("name", "") + "_" + sp.getString("userserial", "").trim() + "/MonthlyAnalysis/" + year;
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName); // The file name
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf"); // The MIME type for PDF
            // Save to a custom folder inside Documents (e.g., "Documents/MyAppPDFs")
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + path);

            // Insert the file into the MediaStore
            Uri externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri fileUri = getContentResolver().insert(externalUri, values);

            if (fileUri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(fileUri)) {
                    if (outputStream != null) {
                        createPdfDocument(outputStream); // Write PDF content
                        Toast.makeText(this, "PDF created and saved in /Documents" + path + "/" + pdfFileName + ".pdf", Toast.LENGTH_LONG).show();
                        intertitialAd.setFlag(1, "PDF saved in /Documents" + path + "/" + pdfFileName + ".pdf");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error creating PDF!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error saving file!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(monthly.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    // Create PDF content (used by both legacy and MediaStore methods)
    private void createPdfDocument(OutputStream outputStream) {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument,PageSize.A4,false);

        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(30, 0, 50, 0);
        Paragraph title = new Paragraph("Hisaab Analyser - Your Monthly Expenses")
                .setBold()
                .setFontSize(25)
                .setTextAlignment(TextAlignment.CENTER);
        Date date = new Date();
        int startmargin = 17;
        Paragraph fullname = new Paragraph("Name : " + sp.getString("fullname", ""))
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(30, 0, 2, startmargin).setFontSize(13);
        Paragraph monthtitle = new Paragraph("Year : " + yearStr)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(1, 0, 2, startmargin).setFontSize(13);

        Paragraph issuedate = new Paragraph("Issue Date : " + date)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(1, 0, 25, startmargin).setFontSize(13);

        float[] width = {100f, 100f, 120f, 120f, 120f};
        Table table = new Table(width).setHorizontalAlignment(HorizontalAlignment.CENTER);

// Define padding value
        float paddingValue = 4f;

// Add header cells
        table.addCell(new Cell()
                .add(new Paragraph("Sno")
                        .setFontSize(15)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER))
                .setMaxWidth(10) // Set max width for Sno
                .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                .setPadding(paddingValue)); // Set padding

        table.addCell(new Cell()
                .add(new Paragraph("Month")
                        .setFontSize(15)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER))
                .setMaxWidth(20) // Set max width for Month
                .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                .setPadding(paddingValue)); // Set padding

        table.addCell(new Cell()
                .add(new Paragraph("Total Received")
                        .setFontSize(15)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER))
                .setMaxWidth(60) // Set max width for Total Received
                .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                .setPadding(paddingValue)); // Set padding

        table.addCell(new Cell()
                .add(new Paragraph("Total Spent")
                        .setFontSize(15)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER))
                .setMaxWidth(60) // Set max width for Total Spent
                .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                .setPadding(paddingValue)); // Set padding

        table.addCell(new Cell()
                .add(new Paragraph("Total Available")
                        .setFontSize(15)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER))
                .setMaxWidth(60) // Set max width for Total Available
                .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                .setPadding(paddingValue)); // Set padding

        long totalspent = 0;
        long totalreceive = 0;
        long totalavailable = 0;
        int count = 1;

// Add data rows
        for (String[] arrDatum : arrData) {
            try {
                totalspent += Long.parseLong(arrDatum[1]);
                totalreceive += Long.parseLong(arrDatum[2]);
                totalavailable = Long.parseLong(arrDatum[3]);

                // Add cells with specified max widths and vertical alignment
                table.addCell(new Cell()
                        .add(new Paragraph(String.valueOf(count))
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(20) // Set max width for Sno
                        .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                        .setPadding(paddingValue)); // Set padding

                table.addCell(new Cell()
                        .add(new Paragraph(arrDatum[0])
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(30) // Set max width for Month
                        .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                        .setPadding(paddingValue)); // Set padding

                table.addCell(new Cell()
                        .add(new Paragraph("Rs." + arrDatum[2])
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(60) // Set max width for Total Received
                        .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                        .setPadding(paddingValue)); // Set padding

                table.addCell(new Cell()
                        .add(new Paragraph("Rs." + arrDatum[1])
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(60) // Set max width for Total Spent
                        .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                        .setPadding(paddingValue)); // Set padding

                table.addCell(new Cell()
                        .add(new Paragraph("Rs." + arrDatum[3])
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(60) // Set max width for Total Available
                        .setVerticalAlignment(VerticalAlignment.MIDDLE) // Center vertically
                        .setPadding(paddingValue)); // Set padding

                count += 1;
            } catch (Exception e) {
                // Handle exception if needed
            }
        }


        Paragraph spentpara = new Paragraph("Total Money Spent : Rs." + totalspent)
                .setFontSize(13).setMarginTop(30)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(42, 0, 0, startmargin);

        Paragraph receivepara = new Paragraph("Total Money Received : Rs." + totalreceive)
                .setFontSize(13).setMarginTop(3)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(25, 0, 0, startmargin);

        Paragraph availablepara = new Paragraph("Total Money Available : Rs." + totalavailable)
                .setFontSize(13).setMarginTop(3)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(25, 0, 0, startmargin);
        document.add(title);
        document.add(fullname);
        document.add(monthtitle);
        document.add(issuedate);
        document.add(table);
        document.add(spentpara);
        document.add(receivepara);
        document.add(availablepara);
        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            // Create a footer paragraph
            Paragraph footer = new Paragraph("Powered by Hisaab Analyser - Your Trusted Expense Tracker app.\n")
                    .setFontSize(13)
                    .setTextAlignment(TextAlignment.CENTER);

            // Add the footer to each page, positioned 10 units above the bottom edge
            document.showTextAligned(footer,
                    PageSize.A4.getWidth() / 2,  // Center horizontally
                    16,                         // Position 20 units above the bottom
                    i,                          // Page number to apply the footer to
                    TextAlignment.CENTER,
                    VerticalAlignment.BOTTOM,
                    0);                          // No rotation
        }

        document.close();
    }

    // Legacy method to create the PDF using WRITE_EXTERNAL_STORAGE for Android versions below 13
    private void createPdfFileLegacy() {
        try {
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "HisaabAnalyser");
            if (!pdfDir.exists()) {
                boolean isDirCreated = pdfDir.mkdirs(); // Use mkdirs() to create necessary directories in the path
                if (!isDirCreated) {
                    Toast.makeText(this, "Failed to create directory!", Toast.LENGTH_SHORT).show();
                }
                else{
                    createPdfFileUsingMediaStore();
                }
            }
            else{
                createPdfFileUsingMediaStore();
            }

        } catch (Exception e) {
            Toast.makeText(monthly.this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }


    private void checkOrientation() {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(cardtmoney, 60);
            displaysize.setfixWidth(cardrecmoney, 60);
            displaysize.setfixWidth(cardspendmoney, 60);
            displaysize.setfixWidth(b,55);
            displaysize.setfixWidth(datespinnerlayout,55);
            displaysize.setfixHeight(cardtmoney,35);
            displaysize.setfixHeight(cardrecmoney,35);
            displaysize.setfixHeight(cardspendmoney,35);


        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            displaysize.setfixWidth(cardtmoney, 90);
            displaysize.setfixWidth(cardrecmoney, 90);
            displaysize.setfixWidth(cardspendmoney, 90);
            displaysize.setfixHeight(cardtmoney,14);
            displaysize.setfixHeight(cardrecmoney,14);
            displaysize.setfixHeight(cardspendmoney,14);
            displaysize.setfixWidth(b,90);
            displaysize.setfixWidth(datespinnerlayout,90);

//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }

}

