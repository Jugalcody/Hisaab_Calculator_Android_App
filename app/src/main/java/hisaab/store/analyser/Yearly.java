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
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
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

import com.example.hisaabcalculator.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Yearly extends AppCompatActivity {

    TextView t1,t2,t3;
    SharedPreferences sp,spitem;
    TextView rectitle;
    ShowInterstitialAd interstitialAd;
    TextView spendmoney;
    AppCompatButton graph;
    ImageView downloadpdf;
    AdjustSizeConfiguration displaysize;
    Configuration config;
    ImageView back;
    boolean isclicked=false;
    AdView adView;
    AppCompatButton reset;
    Button b;
    String mon,year,head;
    CardView cardtmoney,cardrecmoney,cardspendmoney;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly);
            try {
                Bundle e = getIntent().getExtras();
                cardtmoney=findViewById(R.id.monthly_cardtmoney);
                cardrecmoney=findViewById(R.id.monthly_cardreceivedmoney);
                cardspendmoney=findViewById(R.id.monthly_cardspendmoney);
                b = findViewById(R.id.mondb);
                t1 = findViewById(R.id.monrec);
                displaysize = new AdjustSizeConfiguration(Yearly.this);
                config = getResources().getConfiguration();
                t2 = findViewById(R.id.mons);
                t3 = findViewById(R.id.monav);
                graph=findViewById(R.id.yearly_graph);
                reset=findViewById(R.id.yearly_reset);
                rectitle=findViewById(R.id.yearrectitle);
                spendmoney=findViewById(R.id.yearstitle);
                ButtonEffect buttonEffect=new ButtonEffect(Yearly.this);
                buttonEffect.buttonEffect(reset);
                buttonEffect.buttonEffect(b);
                sp = getSharedPreferences("login", MODE_PRIVATE);
                spitem = getSharedPreferences("item", MODE_PRIVATE);
                head = spitem.getString("user", "");
                downloadpdf=findViewById(R.id.yearlypdf);

                buttonEffect=new ButtonEffect(Yearly.this);
                buttonEffect.buttonEffect(downloadpdf);

                interstitialAd=new ShowInterstitialAd(Yearly.this);
                interstitialAd.loadAd();

                checkOrientation();
                downloadpdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder a = new AlertDialog.Builder(Yearly.this);

                        a.setMessage("Do you want to download the record of year wise analysis?");

                        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                interstitialAd.showad();
                                createPdf();
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


                back=findViewById(R.id.yearly_back);
                changetheme();
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });

                graph.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        createWindowGraph();
                    }
                });

                t3.setText("Rs." + totalBalance());
                Spinner s2 = findViewById(R.id.spinm3);
                String[] arr2={"2024","2025","2026","2027","2028","2029","2030","2031","2034","2035","2036","2037","2038","2039","2040","2041","2042","2043","2044","2045","2046","2047","2048","2049","2050"};

                SharedPreferences sptheme=getSharedPreferences("theme",MODE_PRIVATE);
                if(sptheme.getString("theme","dark").equals("pink")) {
                    SpinnerAdapter1 adapter2 = new SpinnerAdapter1(Yearly.this, R.layout.spinner_login_pink, arr2);
                    s2.setAdapter(adapter2);
                }
                else{
                    SpinnerAdapter1 adapter2 = new SpinnerAdapter1(Yearly.this, R.layout.spinner_login, arr2);
                    s2.setAdapter(adapter2);
                }

                Calendar now=Calendar.getInstance();
                s2.setSelection(now.get(Calendar.YEAR)-2024);
                year=arr2[now.get(Calendar.YEAR)-2024];

                s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        year = parent.getItemAtPosition(i).toString();
                        b.setText("Show");
                        isclicked=false;
                        b.setVisibility(View.VISIBLE);
                        rectitle.setText("Total money received");
                        spendmoney.setText("Total money spent");
                        reset.setVisibility(View.GONE);
                    }




                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });



                b.setOnClickListener(view -> {

                    /*File path = getApplicationContext().getFilesDir();
                    File f3 = new File(path, (head +year+ "yearlyGet.txt"));
                    if (!f3.exists()) {
                        try {
                            f3.createNewFile();
                        } catch (IOException ex) {
                            throw new RuntimeException(ex);
                        }
                    }*/
         if(!isclicked) {
             rectitle.setText("Total money received in " + year);
             spendmoney.setText("Total money spent in " + year);
             t1.setText("Rs." + yearlyGet(year));
             t2.setText("Rs." + yearlySpend(year));
             b.setText("Showed");
             if(year.equals(arr2[now.get(Calendar.YEAR)-2024])) {
                 reset.setVisibility(View.VISIBLE);
             }
         }
                });
            }catch(Exception e){

            }

reset.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        open2();
    }
});


            adView=findViewById(R.id.adViewyearly);
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);

        }

        public String totalBalance() {
            File path=getApplicationContext().getFilesDir();
            String b3="0";
            try{
                FileInputStream f2=new FileInputStream(new File(path,head+"balance.txt"));
                InputStreamReader r = new InputStreamReader(f2);
                BufferedReader br = new BufferedReader(r);
                b3 = br.readLine();
                if(b3==null) b3="0";
                f2.close();


            }
            catch(IOException e){

                Toast.makeText(this,"empty balance",Toast.LENGTH_LONG).show();
            }
            return b3;
        }
        public String yearlyGet(String year){
            File path=getApplicationContext().getFilesDir();
            String b4="0";

            try{
                File file=new File(path,head+"yearlyGet"+year+".txt");
                if(!file.exists()) file.createNewFile();
                FileInputStream f2=new FileInputStream(file);

                InputStreamReader r = new InputStreamReader(f2);
                BufferedReader br = new BufferedReader(r);
                b4 = br.readLine();
                if(b4==null) b4="0";
                f2.close();
            }
            catch(IOException e){
                Toast.makeText(this,"no data found ",Toast.LENGTH_LONG).show();
            }
            return b4;
        }

        public String yearlySpend(String year){
            File path=getApplicationContext().getFilesDir();
            String b4="0";
            try{
                File file=new File(path,head+"yearlySpend"+year+".txt");
                if(!file.exists()) file.createNewFile();
                FileInputStream f2=new FileInputStream(file);

                InputStreamReader r = new InputStreamReader(f2);
                BufferedReader br = new BufferedReader(r);
                b4 = br.readLine();
                if(b4==null) b4="0";
                f2.close();
            }
            catch(IOException e){
                Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
            }
            return b4;
        }

    public int month(String m){
        if(m.equals("Jan")) return 1;
        else if(m.equals("Feb")) return 2;
        else if(m.equals("Mar")) return 3;
        else if(m.equals("Apr")) return 4;
        else if(m.equals("May")) return 5;
        else if(m.equals("June")) return 6;
        else if(m.equals("July")) return 7;
        else if(m.equals("Aug")) return 8;
        else if(m.equals("Sept")) return 9;
        else if(m.equals("Oct")) return 10;
        else if(m.equals("Nov")) return 11;
        else return 12;
    }
    public void open2(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
        a.setMessage("Do you want to reset the data of "+year+"?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                File path=getApplicationContext().getFilesDir();

                FileOutputStream f2= null;
                try {

                    f2=new FileOutputStream(new File(path,head+"yearlyGet"+year+".txt"));
                    f2.write("".getBytes());
                    SharedPreferences sharedPreferences=getSharedPreferences(head+year,MODE_PRIVATE);
                    sharedPreferences.edit().putString("balance","0").apply();
                    f2.close();
                    f2=new FileOutputStream(new File(path,head+"yearlySpend"+year+".txt"));
                    f2.write(("").getBytes());
                    f2.close();


                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                FileOutputStream f6 = null;
                FileInputStream f4=null;
              //  InputStreamReader r = null;
                //BufferedReader br = null;
                long tspend=0,tget=0;
                long tremain=0;
                try {

                    String[] arr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
                    for (int k = 0; k < 12; k++) {

try {
    f4 = new FileInputStream(new File(path, head +"monthlySpend"+ month(arr[(k)]) + year + ".txt"));
    InputStreamReader rr = new InputStreamReader(f4);
    BufferedReader brr = new BufferedReader(rr);

    String initalspend = brr.readLine();
    if (initalspend == null) initalspend = "0";
    tspend = Long.parseLong(initalspend);
    f4.close();


    f4 = new FileInputStream(new File(path, head +"monthlyGet"+  month(arr[(k)])+ year + ".txt"));
    InputStreamReader r = new InputStreamReader(f4);
    BufferedReader br = new BufferedReader(r);

    String initalget = br.readLine();
    if (initalget == null) initalget = "0";
    tget = Long.parseLong(initalget);
    f4.close();

    tremain += tget - tspend;
}
catch (Exception e){
}
                        try {
                            f6 = new FileOutputStream(new File(path, head+"monthlySpend"+ (k + 1) + year + ".txt"));
                            f6.write("".getBytes());
                            f6.close();
                        } catch (Exception e) {
                        }
                        try {
                            f6 = new FileOutputStream(new File(path, head+"monthlyGet" + (k + 1) + year + ".txt"));
                            f6.write("".getBytes());
                            f6.close();
                        } catch (Exception e) {
                        }
                        try {
                            f2 = new FileOutputStream(new File(path, head+"hisaab" + arr[k] + year + "data.txt"));
                            f2.write(("").getBytes());
                            f2.close();
                        } catch (Exception e) {
                        }
                    }
                }catch (Exception e){

                }
                rectitle.setText("Total money received in " + year);
                spendmoney.setText("Total money spent in " + year);
                t1.setText("Rs." + yearlyGet(year));
                t2.setText("Rs." + yearlySpend(year));

               t3.setText("Rs." + String.valueOf(deductMoney(tremain)));
            }
        });

        a.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alerts=a.create();
        alerts.show();
    }
    public long deductMoney(long p){
        File path=getApplicationContext().getFilesDir();
        long bal3=0;
        try{
            FileInputStream f=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r=new InputStreamReader(f);
            BufferedReader br=new BufferedReader(r);
            String bal=br.readLine();
            if (bal==null) bal="0";
            bal3=Long.parseLong(bal)-p;
            f.close();
            FileOutputStream f2=new FileOutputStream(new File(path,head+"balance.txt"));
            f2.write(Long.toString(bal3).getBytes());
            f2.close();

        }
        catch(IOException e){
            //e.printStackTrace();
        }
        return bal3;
    }


    private void changetheme(){
        SharedPreferences sp=getSharedPreferences("theme",MODE_PRIVATE);
        LinearLayout layout=findViewById(R.id.yearly_container);
        LinearLayout toolbar=findViewById(R.id.yearly_toolbar);

        View v1,v2,v3;
        v1=findViewById(R.id.monthly_v1);
        v2=findViewById(R.id.monthly_v2);
        v3=findViewById(R.id.monthly_v3);

        if(sp.getString("theme","dark").equals("pink")){
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
            downloadpdf.setBackgroundResource(R.drawable.circlebutpink);
            graph.setBackgroundResource(R.drawable.loginbutpink);
            cardtmoney.setCardBackgroundColor(getColor(R.color.deletecardbackgrounddark2));
            cardrecmoney.setCardBackgroundColor(getColor(R.color.deletecardbackgrounddark2));
            cardspendmoney.setCardBackgroundColor(getColor(R.color.deletecardbackgrounddark2));


        }

        else{
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
            downloadpdf.setBackgroundResource(R.drawable.circlebut);
            cardtmoney.setCardBackgroundColor(getColor(R.color.carddark));
            cardrecmoney.setCardBackgroundColor(getColor(R.color.carddark));
            cardspendmoney.setCardBackgroundColor(getColor(R.color.carddark));

            t1.setTextColor(getColor(R.color.green));

        }

    }


    public void createWindowGraph() {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.graphanalysis_design, null);
        PopupWindow popupWindow = new PopupWindow(popup);
        popupWindow.setContentView(popup);
        int width = ViewGroup.LayoutParams.MATCH_PARENT;
        int height = ViewGroup.LayoutParams.MATCH_PARENT;
        popupWindow.setWidth(width);
        popupWindow.setHeight(height);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        LinearLayout graphcontainer=popup.findViewById(R.id.graph_container);
        SharedPreferences sharedPreferences=getSharedPreferences("theme",MODE_PRIVATE);
        if(sharedPreferences.getString("theme","dark").equals("dark")){
            graphcontainer.setBackgroundColor(getColor(R.color.backgrounddark));
        }
        else{
            graphcontainer.setBackgroundColor(getColor(R.color.graphpink));
        }
        ImageView close = popup.findViewById(R.id.monthlygraph_close);
        setupGraph(popup);
     LinearLayout layout=findViewById(R.id.yearly_container);
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



    void setupGraph(View view) {
        LineChart lineChart = view.findViewById(R.id.monthly_line_chart);

        // Prepare data for "Received," "Spent," and "Available" money
        List<Entry> receivedEntries = new ArrayList<>();
        List<Entry> spentEntries = new ArrayList<>();
        List<Entry> availableEntries = new ArrayList<>(); // Correct spelling

        long leftmoney=0L,available=0L;
        for (int i = 0; i < 28; i++) {
            long received = Long.parseLong(yearlyGet(String.valueOf(2024 + i)));
            long spent = Long.parseLong(yearlySpend(String.valueOf(2024 + i)));
            SharedPreferences sp = getSharedPreferences(head + String.valueOf(Integer.parseInt(String.valueOf(2024 + i))), MODE_PRIVATE);
            available = Long.parseLong(sp.getString("balance", "-1"));
            if(available!=-1){
                leftmoney=available;
            }
            receivedEntries.add(new Entry(i, received));
            spentEntries.add(new Entry(i, spent));
            availableEntries.add(new Entry(i,leftmoney)); // Populate availableEntries
        }

        float maxReceived = getMax(receivedEntries);
        float maxSpent = getMax(spentEntries);
        float maxAvailable = getMax(availableEntries);
        float maxValue = Math.max(Math.max(maxReceived, maxSpent), maxAvailable);

        // Create datasets for the line chart
        LineDataSet receivedDataSet = new LineDataSet(receivedEntries, "Money Received");
        receivedDataSet.setColor(getColor(R.color.green));
        receivedDataSet.setLineWidth(2f);
        receivedDataSet.setCircleRadius(5f);
        receivedDataSet.setDrawValues(true);
        receivedDataSet.setValueTextColor(Color.WHITE);
        receivedDataSet.setValueTextSize(10f);

        LineDataSet availableDataSet = new LineDataSet(availableEntries, "Available Money");
        availableDataSet.setColor(Color.YELLOW);
        availableDataSet.setLineWidth(2f);
        availableDataSet.setCircleRadius(5f);
        availableDataSet.setDrawValues(true);
        availableDataSet.setValueTextColor(Color.WHITE);
        availableDataSet.setValueTextSize(10f);

        LineDataSet spentDataSet = new LineDataSet(spentEntries, "Money Spent");
        spentDataSet.setColor(Color.RED);
        spentDataSet.setLineWidth(2f);
        spentDataSet.setCircleRadius(5f);
        spentDataSet.setDrawValues(true);
        spentDataSet.setValueTextColor(Color.WHITE);
        spentDataSet.setValueTextSize(10f);

        // Create LineData object to combine datasets
        LineData lineData = new LineData(receivedDataSet, spentDataSet, availableDataSet);
        lineData.setValueTextColor(Color.WHITE);
        lineData.setValueTextSize(10f);

        // Set data to the LineChart
        lineChart.setData(lineData);

        // Set up horizontal scroll and visible range
        lineChart.setVisibleXRangeMaximum(8);
        lineChart.setDragEnabled(true);
        lineChart.setPinchZoom(true);
        lineChart.setScaleEnabled(true);

        lineChart.animateX(1500);
        lineChart.animateY(1500);

        // Customize chart axis labels
        String[] years = {
                "2024", "2025", "2026", "2027", "2028", "2029", "2030",
                "2031", "2032", "2033", "2034", "2035", "2036", "2037",
                "2038", "2039", "2040", "2041", "2042", "2043", "2044",
                "2045", "2046", "2047", "2048", "2049", "2050"
        };
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setValueFormatter(new IndexAxisValueFormatter(years));
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setGranularity(1f);
        xAxis.setLabelCount(26);

        // Customize the Y axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextColor(Color.WHITE);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(maxValue + 100f);

        // Adjust granularity and label count based on maxValue
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

        leftAxis.setGranularityEnabled(true);
        lineChart.getAxisRight().setEnabled(false);

        // Setup custom legend view
        LayoutInflater inflater = LayoutInflater.from(view.getContext());
        View legendView = inflater.inflate(R.layout.legend_view, null);
        CheckBox checkboxReceived = legendView.findViewById(R.id.checkbox_received);
        CheckBox checkboxAvailable = legendView.findViewById(R.id.checkbox_available);
        CheckBox checkboxSpent = legendView.findViewById(R.id.checkbox_spent);

        checkboxReceived.setChecked(true);
        checkboxAvailable.setChecked(true);
        checkboxSpent.setChecked(true);

        receivedDataSet.setVisible(true);
        availableDataSet.setVisible(true);
        spentDataSet.setVisible(true);

        // Add listeners for checkboxes
        checkboxReceived.setOnCheckedChangeListener((buttonView, isChecked) -> {
            receivedDataSet.setVisible(isChecked);
            lineChart.invalidate();
        });

        checkboxAvailable.setOnCheckedChangeListener((buttonView, isChecked) -> {
            availableDataSet.setVisible(isChecked);
            lineChart.invalidate();
        });

        checkboxSpent.setOnCheckedChangeListener((buttonView, isChecked) -> {
            spentDataSet.setVisible(isChecked);
            lineChart.invalidate();
        });

        // Add the legend view to your layout
        LinearLayout container = view.findViewById(R.id.legend_container);
        container.addView(legendView);
        Legend legend = lineChart.getLegend();
        legend.setTextColor(Color.WHITE);

        lineChart.getLegend().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.invalidate();
    }

    private float getMax(List<Entry> entries) {
        float max = Float.MIN_VALUE;
        for (Entry entry : entries) {
            if (entry.getY() > max) {
                max = entry.getY();
            }
        }
        return max;
    }




    private String[][] arrData;
    private String pdfFileName;

    // Method to check permission and create PDF
    public void createPdf() {
        String[] years = {
                "2024", "2025", "2026", "2027", "2028", "2029", "2030",
                "2031", "2032", "2033", "2034", "2035", "2036", "2037",
                "2038", "2039", "2040", "2041", "2042", "2043", "2044",
                "2045", "2046", "2047", "2048", "2049", "2050"
        };
        arrData=new String[27][4];
        String leftmoney="";
        for (int i = 0; i <27; i++) {
            String[] innerarr=new String[4];
            try {
                innerarr[0] = years[i];
                innerarr[1] = yearlySpend(String.valueOf(2024 + i));
                innerarr[2] = yearlyGet(String.valueOf(2024 + i));
                SharedPreferences sp = getSharedPreferences(head + String.valueOf(Integer.parseInt(String.valueOf(2024 + i))), MODE_PRIVATE);

                String avail = sp.getString("balance", "-1");
                if(!avail.trim().equals("-1")) {
                    leftmoney=avail;
                }
                innerarr[3] = leftmoney;
                arrData[i]=innerarr;

            }
            catch (Exception e){
                Toast.makeText(Yearly.this,e.toString(),Toast.LENGTH_SHORT).show();
            }

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
        try{
            File pdfDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS), "HisaabAnalyser");
            if (!pdfDir.exists()) {
                boolean isDirCreated = pdfDir.mkdirs(); // Use mkdirs() to create necessary directories in the path
                if (!isDirCreated) {
                    Toast.makeText(this, "Failed to create directory!", Toast.LENGTH_SHORT).show();
                }
            }
        }
        catch (Exception e){
            Toast.makeText(Yearly.this,e.toString(),Toast.LENGTH_LONG).show();
        }

        try {
            ContentValues values = new ContentValues();
            pdfFileName = "YearlyExpenses_" + spitem.getString("name", "");
            String path = "/HisaabAnalyser/" + spitem.getString("name", "") + "_" + sp.getString("userserial", "").trim() + "/YearlyAnalysis/";
            values.put(MediaStore.MediaColumns.DISPLAY_NAME, pdfFileName);
            values.put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf");
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + path);

            // Insert the file into the MediaStore
            Uri externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
            Uri fileUri = getContentResolver().insert(externalUri, values);

            if (fileUri != null) {
                try (OutputStream outputStream = getContentResolver().openOutputStream(fileUri)) {
                    if (outputStream != null) {
                        createPdfDocument(outputStream);
                        Toast.makeText(this, "PDF created and saved in /Documents" + path + pdfFileName + ".pdf", Toast.LENGTH_LONG).show();
                        interstitialAd.setFlag(1, "PDF saved in /Documents" + path + pdfFileName + ".pdf");
                    }
                } catch (IOException e) {
                    Toast.makeText(this, "Error creating PDF!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error saving file!", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            Toast.makeText(Yearly.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    // Create PDF content (used by both legacy and MediaStore methods)
    private void createPdfDocument(OutputStream outputStream) {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(30, 0, 50, 0);
        Paragraph title = new Paragraph("Hisaab Analyser - Your Yearly Expenses")
                .setBold()
                .setFontSize(25)
                .setTextAlignment(TextAlignment.CENTER);
        Date date=new Date();
        int startmargin=17;
        Paragraph fullname = new Paragraph("Name : "+sp.getString("fullname",""))
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(30,0,2,startmargin).setFontSize(13);
        Paragraph issuedate = new Paragraph("Issue Date : "+date)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(1,0,25,startmargin).setFontSize(13);

        float[] width = {100f, 100f, 120f,120f,120f};
        Table table = new Table(width).setHorizontalAlignment(HorizontalAlignment.CENTER);

        table.addCell(new Cell().add(new Paragraph("Sno").setFontSize(15).setBold().setTextAlignment(TextAlignment.CENTER).setPaddingTop(5).setPaddingBottom(5)));
        table.addCell(new Cell().add(new Paragraph("Year").setFontSize(15).setBold().setTextAlignment(TextAlignment.CENTER).setPaddingTop(5).setPaddingBottom(5)));
        table.addCell(new Cell().add(new Paragraph("Total Received").setFontSize(15).setBold().setTextAlignment(TextAlignment.CENTER).setPaddingTop(5).setPaddingBottom(5)));
        table.addCell(new Cell().add(new Paragraph("Total Spent").setFontSize(15).setBold().setTextAlignment(TextAlignment.CENTER).setPaddingTop(5).setPaddingBottom(5)));
        table.addCell(new Cell().add(new Paragraph("Total Available").setFontSize(15).setBold().setTextAlignment(TextAlignment.CENTER).setPaddingTop(5).setPaddingBottom(5)));

        int count=1;

        for (int i=0;i<arrData.length;i++) {
            try {
                table.addCell(new Cell()
                        .add(new Paragraph(String.valueOf(count))
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(60)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(2)); // Center horizontally, vertically, and add padding for "Sno"

                table.addCell(new Cell()
                        .add(new Paragraph(arrData[i][0].toString())
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(60)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(2)); // Center horizontally, vertically, and add padding for "Year"

                table.addCell(new Cell()
                        .add(new Paragraph("Rs." + arrData[i][2])
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(60)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(2)); // Center horizontally, vertically, and add padding for "Total Received"

                table.addCell(new Cell()
                        .add(new Paragraph("Rs." + arrData[i][1])
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(60)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(2)); // Center horizontally, vertically, and add padding for "Total Spent"

                table.addCell(new Cell()
                        .add(new Paragraph("Rs." + arrData[i][3])
                                .setFontSize(14)
                                .setTextAlignment(TextAlignment.CENTER))
                        .setMaxWidth(60)
                        .setVerticalAlignment(VerticalAlignment.MIDDLE)
                        .setPadding(2)); // Center horizontally, vertically, and add padding for "Total Available"

                count += 1;
            }

            catch (Exception e){
Toast.makeText(Yearly.this,e.toString(),Toast.LENGTH_LONG).show();
            }

        }

        document.add(title);
        document.add(fullname);
        document.add(issuedate);
        document.add(table);
        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            // Create a footer paragraph
            Paragraph footer = new Paragraph("Powered by Hisaab Analyser - Your Trusted Expense Tracker app.\n")
                    .setFontSize(13)
                    .setTextAlignment(TextAlignment.CENTER);

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
            Toast.makeText(Yearly.this, e.toString(), Toast.LENGTH_LONG).show();
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

