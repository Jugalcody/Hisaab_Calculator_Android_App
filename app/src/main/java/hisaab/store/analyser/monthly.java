package hisaab.store.analyser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisaabcalculator.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class monthly extends AppCompatActivity {
    TextView t1, t2, t3;
    SharedPreferences sp, spitem;
    TextView rectitle;
    ImageView downloadpdf;
    ShowInterstitialAd intertitialAd;
    TextView spendmoney;
    ViewGroup root;
    boolean isclicked = false;
    AdView adView;
    AppCompatButton reset, graph;

    AdjustSizeConfiguration displaysize;
    Configuration config;
    ImageView back;
    Button b;
    LinearLayout layout;

    CardView cardtmoney, cardrecmoney, cardspendmoney;
    String mon = "", year = "", head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monthly);
        try {
            cardtmoney = findViewById(R.id.monthly_cardtmoney);
            cardrecmoney = findViewById(R.id.monthly_cardreceivedmoney);
            cardspendmoney = findViewById(R.id.monthly_cardspendmoney);
            displaysize = new AdjustSizeConfiguration(monthly.this);
            config = getResources().getConfiguration();
            b = findViewById(R.id.mondb);
            root = (ViewGroup) getWindow().getDecorView().getRootView();
            t1 = findViewById(R.id.monrec);
            rectitle = findViewById(R.id.monrectitle);
            spendmoney = findViewById(R.id.monstitle);
            t2 = findViewById(R.id.mons);
            t3 = findViewById(R.id.monav);
            graph = findViewById(R.id.monthly_graph);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            reset = findViewById(R.id.monthly_reset);
            downloadpdf = findViewById(R.id.monthlypdf);

            ButtonEffect buttonEffect = new ButtonEffect(monthly.this);
            buttonEffect.buttonEffect(reset);
            buttonEffect.buttonEffect(b);
            buttonEffect.buttonEffect(downloadpdf);

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
            Spinner s = findViewById(R.id.spinm2);
            Spinner s2 = findViewById(R.id.spinm3);
            String[] arr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
            String[] arr2 = {"2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050"};
            SharedPreferences sptheme = getSharedPreferences("theme", MODE_PRIVATE);
            if (sptheme.getString("theme", "dark").equals("pink")) {
                SpinnerAdapter1 adapter = new SpinnerAdapter1(monthly.this, R.layout.spinner_login_pink, arr);
                s.setAdapter(adapter);


                SpinnerAdapter1 adapter2 = new SpinnerAdapter1(monthly.this, R.layout.spinner_login_pink, arr2);
                s2.setAdapter(adapter2);

            } else {

                SpinnerAdapter1 adapter = new SpinnerAdapter1(monthly.this, R.layout.spinner_login, arr);
                s.setAdapter(adapter);

                SpinnerAdapter1 adapter2 = new SpinnerAdapter1(monthly.this, R.layout.spinner_login, arr2);
                s2.setAdapter(adapter2);
            }


            Calendar now = Calendar.getInstance();
            s.setSelection(now.get(Calendar.MONTH));
            mon = arr[now.get(Calendar.MONTH)];
            s2.setSelection(now.get(Calendar.YEAR) - 2024);
            year = arr2[now.get(Calendar.YEAR) - 2024];


            changetheme();
            s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    year = parent.getItemAtPosition(i).toString();
                    b.setText("Show");
                    isclicked = false;
                    rectitle.setText("Total money received");
                    spendmoney.setText("Total money spent");
                    t1.setText("----");
                    t2.setText("----");
                    reset.setVisibility(View.GONE);
                    b.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    mon = parent.getItemAtPosition(i).toString();
                    b.setText("Show");
                    isclicked = false;
                    b.setVisibility(View.VISIBLE);
                    rectitle.setText("Total money received");
                    spendmoney.setText("Total money spend");
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
                    t1.setText("Rs." + monthlyGet(month(mon)));
                    t2.setText("Rs." + monthlySpend(month(mon)));
                    rectitle.setText("Total money received in " + mon + " " + year);
                    spendmoney.setText("Total money spent in " + mon + " " + year);
                    b.setText("Showed");
                    isclicked = true;
                    if (year.equals(arr2[now.get(Calendar.YEAR) - 2024]) && mon.equals(arr[now.get(Calendar.MONTH)])) {
                        reset.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {

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

            Toast.makeText(this, "empty balance", Toast.LENGTH_LONG).show();
        }
        return b3;
    }

    public String monthlyGet(int m) {
        File path = getApplicationContext().getFilesDir();
        String b4 = "0";

        try {
            File file = new File(path, head + "monthlyGet" + m + year + ".txt");
            if (!file.exists()) file.createNewFile();
            FileInputStream f2 = new FileInputStream(file);

            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if (b4 == null) b4 = "0";
            f2.close();
        } catch (IOException e) {
            Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
        }
        return b4;
    }

    public String monthlySpend(int m) {
        File path = getApplicationContext().getFilesDir();
        String b4 = "0";
        try {

            File monthlyfile = new File(path, head + "monthlySpend" + m + year + ".txt");
            if (!monthlyfile.exists()) monthlyfile.createNewFile();
            FileInputStream f2 = new FileInputStream(monthlyfile);
            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if (b4 == null) b4 = "0";
            f2.close();
        } catch (IOException e) {
            Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
        }
        return b4;
    }

    public long deductMoney(long p) {
        File path = getApplicationContext().getFilesDir();
        long bal3 = 0;
        try {
            FileInputStream f = new FileInputStream(new File(path, head + "balance.txt"));
            InputStreamReader r = new InputStreamReader(f);
            BufferedReader br = new BufferedReader(r);
            String bal = br.readLine();
            if (bal == null) bal = "0";
            bal3 = Long.parseLong(bal) - p;
            f.close();
            FileOutputStream f2 = new FileOutputStream(new File(path, head + "balance.txt"));
            f2.write(Long.toString(bal3).getBytes());
            f2.close();

        } catch (IOException e) {
            //e.printStackTrace();
        }
        return bal3;
    }

    public void open2() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setMessage("Do you want to reset the data of " + mon + "," + year + "?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File path = getApplicationContext().getFilesDir();

                FileInputStream f2 = null;
                FileOutputStream f3 = null;
                try {
                    f2 = new FileInputStream(new File(path, head + "yearlyGet" + year + ".txt"));

                    InputStreamReader r = new InputStreamReader(f2);
                    BufferedReader br = new BufferedReader(r);

                    String initalamt = br.readLine();
                    if (initalamt == null) initalamt = "0";
                    f2.close();

                    try {
                        f3 = new FileOutputStream(new File(path, head + "hisaab" + mon + year + "data.txt"));
                        f3.write(("").getBytes());
                        f3.close();


                    } catch (Exception e) {
                    }

                    f3 = new FileOutputStream(new File(path, head + "yearlyGet" + year + ".txt"));
                    f3.write(String.valueOf(Long.parseLong(initalamt) - Long.parseLong(monthlyGet(month(mon)))).getBytes());
                    SharedPreferences sharedPreferences = getSharedPreferences(head + year, MODE_PRIVATE);
                    sharedPreferences.edit().putString("balance", String.valueOf(Long.parseLong(initalamt) - Long.parseLong(monthlyGet(month(mon))))).apply();
                    f3.close();
                    FileInputStream f4 = new FileInputStream(new File(path, head + "yearlySpend" + year + ".txt"));
                    r = new InputStreamReader(f4);
                    br = new BufferedReader(r);

                    String initalspend = br.readLine();
                    if (initalspend == null) initalspend = "0";
                    f4.close();
                    FileOutputStream f5 = new FileOutputStream(new File(path, head + "yearlySpend" + year + ".txt"));
                    f5.write(String.valueOf(Long.parseLong(initalspend) - Long.parseLong(monthlySpend(month(mon)))).getBytes());
                    f5.close();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                long remain = Long.parseLong(monthlyGet(month(mon))) - Long.parseLong(monthlySpend(month(mon)));
                t3.setText("Rs." + String.valueOf(deductMoney(remain)));

                FileOutputStream f6 = null;
                try {


                    f6 = new FileOutputStream(new File(path, head + "monthlySpend" + month(mon) + year + ".txt"));
                    f6.write(("0").getBytes());
                    f6.close();
                    f6 = new FileOutputStream(new File(path, head + "monthlyGet" + month(mon) + year + ".txt"));
                    f6.write(("0").getBytes());
                    f6.close();
                    FileOutputStream f1 = new FileOutputStream(new File(path, head + "hisaab" + month(mon) + year + "data.txt"));
                    f1.write("".getBytes());
                    f1.close();
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                rectitle.setText("Total money received in " + mon + " " + year);
                spendmoney.setText("Total money spent in " + mon + " " + year);
                t1.setText("Rs. " + monthlyGet(month(mon)));
                t2.setText("Rs." + monthlySpend(month(mon)));

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


    private void changetheme() {
        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        LinearLayout layout = findViewById(R.id.monthly_container);
        LinearLayout toolbar = findViewById(R.id.monthly_toolbar);

        View v1, v2, v3;
        v1 = findViewById(R.id.monthly_v1);
        v2 = findViewById(R.id.monthly_v2);
        v3 = findViewById(R.id.monthly_v3);
        if (sp.getString("theme", "dark").equals("pink")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.toolbarpink));
            }
            b.setBackgroundResource(R.drawable.loginbutpink);
            toolbar.setBackgroundColor(getColor(R.color.toolbarpink));
            layout.setBackgroundColor(getColor(R.color.backgroundpink));
            t1.setTextColor(getColor(R.color.white));
            v1.setBackgroundColor(getColor(R.color.backgroundpink));
            v2.setBackgroundColor(getColor(R.color.backgroundpink));
            v3.setBackgroundColor(getColor(R.color.backgroundpink));
            graph.setBackgroundResource(R.drawable.loginbutpink);
            downloadpdf.setBackgroundResource(R.drawable.circlebutpink);
            cardtmoney.setCardBackgroundColor(getColor(R.color.deletecardbackgrounddark2));
            cardrecmoney.setCardBackgroundColor(getColor(R.color.deletecardbackgrounddark2));
            cardspendmoney.setCardBackgroundColor(getColor(R.color.deletecardbackgrounddark2));


        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            toolbar.setBackgroundColor(getColor(R.color.toolbardark));
            layout.setBackgroundColor(getColor(R.color.backgrounddark));
            b.setBackgroundResource(R.drawable.loginbut);
            graph.setBackgroundResource(R.drawable.loginbut2);
            v1.setBackgroundColor(getColor(R.color.viewhrcolor));
            v2.setBackgroundColor(getColor(R.color.viewhrcolor));
            v3.setBackgroundColor(getColor(R.color.viewhrcolor));
            cardtmoney.setCardBackgroundColor(getColor(R.color.carddark));
            downloadpdf.setBackgroundResource(R.drawable.circlebut);
            cardrecmoney.setCardBackgroundColor(getColor(R.color.carddark));
            cardspendmoney.setCardBackgroundColor(getColor(R.color.carddark));

            t1.setTextColor(getColor(R.color.green));

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
            spentMoney[i] = Long.parseLong(monthlySpend(month(arr[i])));
            receivedMoney[i] = Long.parseLong(monthlyGet(month(arr[i])));
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
        receivedDataSet.setValueTextColor(Color.WHITE); // Set data point label color to white
        receivedDataSet.setValueTextSize(10f); // Set label text size

        LineDataSet availableDataSet = new LineDataSet(availableEntries, "Available Money");
        availableDataSet.setColor(Color.YELLOW); // Set line color
        availableDataSet.setLineWidth(2f);
        availableDataSet.setCircleRadius(5f);
        availableDataSet.setDrawValues(true); // Enable drawing values inside the graph
        availableDataSet.setValueTextColor(Color.WHITE); // Set data point label color to white
        availableDataSet.setValueTextSize(10f); // Set label text size

        LineDataSet spentDataSet = new LineDataSet(spentEntries, "Money Spent");
        spentDataSet.setColor(Color.RED); // Set line color
        spentDataSet.setLineWidth(2f);
        spentDataSet.setCircleRadius(5f);
        spentDataSet.setDrawValues(true); // Enable drawing values inside the graph
        spentDataSet.setValueTextColor(Color.WHITE); // Set data point label color to white
        spentDataSet.setValueTextSize(10f); // Set label text size

        // Create LineData object to combine datasets
        LineData lineData = new LineData(receivedDataSet, spentDataSet, availableDataSet);
        lineData.setValueTextColor(Color.WHITE); // Set value text color for all data sets
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
        xAxis.setTextColor(Color.WHITE); // Set label text color to white
        xAxis.setGranularity(1f); // Ensure each label corresponds to an entry
        xAxis.setLabelCount(15); // Total number of labels to show (all months)

        // Customize the Y axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE); // Set Y-axis labels color to white
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
        CheckBox checkboxSpent = legendView.findViewById(R.id.checkbox_spent);

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
        LinearLayout graphcontainer = popup.findViewById(R.id.graph_container);
        SharedPreferences sharedPreferences = getSharedPreferences("theme", MODE_PRIVATE);
        if (sharedPreferences.getString("theme", "dark").equals("dark")) {
            graphcontainer.setBackgroundColor(getColor(R.color.backgrounddark));
        } else {
            graphcontainer.setBackgroundColor(getColor(R.color.graphpink));
        }
        ImageView close = popup.findViewById(R.id.monthlygraph_close);
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
            innerarr[1] = monthlySpend(month(montharr[i]));
            innerarr[2] = monthlyGet(month(montharr[i]));
            if (i == 0) {
                innerarr[3] = String.valueOf(Long.parseLong(earlierBalance) + Long.parseLong(monthlyGet(month(montharr[i]))) - Long.parseLong(monthlySpend(month(montharr[i]))));
                moneyAvailable[0] = Long.parseLong(earlierBalance) + Long.parseLong(monthlyGet(month(montharr[i]))) - Long.parseLong(monthlySpend(month(montharr[i])));
            } else {
                moneyAvailable[i] = moneyAvailable[i - 1] + Long.parseLong(monthlyGet(month(montharr[i]))) - Long.parseLong(monthlySpend(month(montharr[i])));
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
        Document document = new Document(pdfDocument);

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

//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }

}

