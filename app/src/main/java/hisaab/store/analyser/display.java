package hisaab.store.analyser;

import static com.example.hisaabcalculator.R.color.deletecardbackgrounddark;
import static com.example.hisaabcalculator.R.color.deletecardbackgrounddark2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.hisaabcalculator.R;
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
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class display extends AppCompatActivity {
    int PORTRAIT_TABLE_WIDTH =89;
    TextView e1, totalspend;
    CardView card;
    RelativeLayout layout;
    LinearLayout filterlayout;
    Configuration config;

    int LANDSCAPE_TABLE_WIDTH=65;

    TextView colhead1, colhead2, colhead3;
    ShowInterstitialAd interstitialAd;
    AppCompatButton deletebut;
    ImageView pdfdownloadbut;
    AdjustSizeConfiguration displaysize;
    LottieAnimationView nodata;
    LinearLayout toolbar;

    int i = 0;
    String[][] hisaabdata;
    ViewGroup root;

    TextView deldate;
    Button b1;
    int p = 0;
    SharedPreferences sp, spitem, sptheme,spaccount;
    ImageView back, hide;
    AdView adView;
    boolean isclicked = false;
    HorizontalScrollView hview;
    int hidekey = 0;
    String mon, year, head, filterday = "All";
    Spinner monthSpinner, yearSpinner, filterspinner;

    long monthtotalmoney = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        try {
            displaysize=new AdjustSizeConfiguration(display.this);
            config = getResources().getConfiguration();
            hview = findViewById(R.id.presshowhorizon);
            root = (ViewGroup) getWindow().getDecorView().getRootView();
            totalspend = findViewById(R.id.display_total);
            toolbar = findViewById(R.id.display_toolbar);
            b1 = findViewById(R.id.db);
            pdfdownloadbut=findViewById(R.id.displayPdfdownload);
            monthSpinner = findViewById(R.id.spin2);
            filterlayout = findViewById(R.id.filterlayout_display);
            yearSpinner = findViewById(R.id.spin3);
            filterspinner = findViewById(R.id.display_filter);
            deldate = findViewById(R.id.date_deletedata);
            layout = findViewById(R.id.display_layout);
            colhead1 = findViewById(R.id.display_showbox_1);
            colhead2 = findViewById(R.id.display_showbox_2);
            colhead3 = findViewById(R.id.display_showbox_3);
            nodata=findViewById(R.id.nodata_display);
            deletebut = findViewById(R.id.deletedisplaypage_but);
            card = findViewById(R.id.carddisplay);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            hide = findViewById(R.id.hideunhide);
            spitem = getSharedPreferences("item", MODE_PRIVATE);
            sptheme = getSharedPreferences("theme", MODE_PRIVATE);
            head = spitem.getString("user", "");
            back = findViewById(R.id.display_back);
            checkOrientation();

            changetheme();

            interstitialAd=new ShowInterstitialAd(display.this);
            interstitialAd.loadAd();

            ButtonEffect buttonEffect=new ButtonEffect(display.this);
            buttonEffect.buttonEffect(pdfdownloadbut);
            hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout ll = findViewById(R.id.deletelayout_display);
                    if (hidekey == 0) {
                        hide.setImageDrawable(getDrawable(R.drawable.baseline_keyboard_arrow_up_24));
                        ll.setVisibility(View.VISIBLE);
                        hidekey = 1;
                    } else {
                        hide.setImageDrawable(getDrawable(R.drawable.addplus));

                        ll.setVisibility(View.GONE);
                        hidekey = 0;
                    }
                }
            });


            pdfdownloadbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder a = new AlertDialog.Builder(display.this);
                    if(!filterday.toLowerCase().equals("all")){
                        a.setMessage("Do you want to download your expenses of " + filterday +" "+mon+" "+year+" ?");
                    }
                    else{
                        a.setMessage("Do you want to download your expenses of " +mon+" "+year+" ?");
                    }

                    a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            createPdf(hisaabdata,filterday+mon+""+year,mon,year);
                            interstitialAd.showad();
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

            if (!sp.getBoolean("islogged", false)) {
                Intent i = new Intent(display.this, MainActivity.class);
                startActivity(i);
                finishAffinity();
            }

            String[] arr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
            if (sptheme.getString("theme", "dark").equals("pink")) {
                SpinnerAdapter1 adapter = new SpinnerAdapter1(display.this, R.layout.spinner_login_pink, arr);
                monthSpinner.setAdapter(adapter);
            } else {
                SpinnerAdapter1 adapter = new SpinnerAdapter1(display.this, R.layout.spinner_login, arr);
                monthSpinner.setAdapter(adapter);
            }


            String[] arr2 = {"2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050"};
            if (sptheme.getString("theme", "dark").equals("pink")) {
                SpinnerAdapter1 adapter2 = new SpinnerAdapter1(display.this, R.layout.spinner_login_pink, arr2);
                yearSpinner.setAdapter(adapter2);
            } else {
                SpinnerAdapter1 adapter2 = new SpinnerAdapter1(display.this, R.layout.spinner_login, arr2);
                yearSpinner.setAdapter(adapter2);
            }


            e1 = findViewById(R.id.de);

            e1.setVisibility(View.GONE);
            buttonEffect.buttonEffect(deletebut);
            buttonEffect.buttonEffect(b1);
            deletebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    open();
                }
            });
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            deldate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDatePicker();
                }
            });
            b1.setOnClickListener(view -> {
                if (!isclicked) {
                    isclicked = true;
                    if (show_data() == 1) {
                        b1.setText("Showed");
                    } else {
                        e1.setText("");
                        nodata.setVisibility(View.VISIBLE);
                        b1.setVisibility(View.GONE);
                        totalspend.setVisibility(View.GONE);
                        card.setVisibility(View.GONE);
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }
                }

            });
            yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    year = parent.getItemAtPosition(i).toString();
                    b1.setText("Show");
                    isclicked = false;
                    b1.setVisibility(View.VISIBLE);
                    hview.setVisibility(View.GONE);
                    pdfdownloadbut.setVisibility(View.GONE);
                    nodata.setVisibility(View.GONE);
                    totalspend.setVisibility(View.GONE);
                    card.setVisibility(View.GONE);
                    setFilter(mon, year);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            filterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    filterday = parent.getItemAtPosition(position).toString();
                    b1.setVisibility(View.VISIBLE);
                    isclicked = false;
                    nodata.setVisibility(View.GONE);
                    if (i == 1) {
                        b1.setText("Show");
                        pdfdownloadbut.setVisibility(View.GONE);
                        card.setVisibility(View.GONE);
                        hview.setVisibility(View.GONE);
                        totalspend.setVisibility(View.GONE);
                    }
                    i = 1;
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            Calendar now = Calendar.getInstance();
            monthSpinner.setSelection(now.get(Calendar.MONTH));
            mon = arr[now.get(Calendar.MONTH)];
            yearSpinner.setSelection(now.get(Calendar.YEAR) - 2024);
            year = arr2[now.get(Calendar.YEAR) - 2024];
            filterspinner.setSelection(0);

            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    mon = parent.getItemAtPosition(i).toString();
                    b1.setText("Show");
                    b1.setVisibility(View.VISIBLE);
                    //e1.setText("");
                    isclicked = false;
                    nodata.setVisibility(View.GONE);
                    card.setVisibility(View.GONE);
                    hview.setVisibility(View.GONE);
                    totalspend.setVisibility(View.GONE);
                    setFilter(mon, year);

                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        } catch (Exception e) {

        }


        adView = findViewById(R.id.adViewdisplay);
        // adView.setAdUnitId("ca-app-pub-1079506490540577/7402312139");
        //adView.setAdSize(getAdSize());
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }


    public void setFilter(String mon, String year) {
        SpinnerAdapter1 filteradapter = null;
        if (mon.equals("Jan") || mon.equals("Mar") || mon.equals("May") || mon.equals("Jul") || mon.equals("Aug") || mon.equals("Oct") || mon.equals("Dec")) {
            String[] dateofmontharr = {"All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
            if (sptheme.getString("theme", "dark").equals("pink")) {
                filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_filter_pink, dateofmontharr);
            } else {
                filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_filter, dateofmontharr);
            }
        } else if (mon.equals("Feb")) {
            if (isLeapYear(Integer.parseInt(year))) {
                String[] dateofmontharr = {"All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29"};
                if (sptheme.getString("theme", "dark").equals("pink")) {
                    filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_filter_pink, dateofmontharr);
                } else {
                    filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_filter, dateofmontharr);
                }
            } else {
                String[] dateofmontharr = {"All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};
                if (sptheme.getString("theme", "dark").equals("pink")) {
                    filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_filter_pink, dateofmontharr);
                } else {
                    filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_filter, dateofmontharr);
                }
            }
        } else {
            String[] dateofmontharr = {"All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
            if (sptheme.getString("theme", "dark").equals("pink")) {
                filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_login_pink, dateofmontharr);
            } else {
                filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_login, dateofmontharr);
            }
        }

        filterspinner.setAdapter(filteradapter);
    }


    private void showDatePicker() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Set the selected date to the EditText
                        deldate.setText(String.format("%02d-%02d-%d", selectedDay, selectedMonth + 1, selectedYear));
                    }
                },
                year,
                month,
                day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    public static boolean isLeapYear(int year) {
        // A year is a leap year if:
        // 1. It is divisible by 4, and
        // 2. It is not divisible by 100, unless it is also divisible by 400.

        if (year % 400 == 0) {
            return true;  // Divisible by 400: leap year
        } else if (year % 100 == 0) {
            return false;  // Divisible by 100 but not 400: not a leap year
        } else if (year % 4 == 0) {
            return true;  // Divisible by 4 but not 100: leap year
        } else {
            return false; // Not divisible by 4: not a leap year
        }
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


    public String monthlySpend() {
        File path = getApplicationContext().getFilesDir();
        String b4 = "0";
        int m;
        try {
            m = month(mon);

            FileInputStream f2 = new FileInputStream(new File(path, head+"monthlySpend" + m + year + ".txt"));
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

    public int show_data() {
        int y = 0;
        try {
            LinearLayout tableContent = findViewById(R.id.tableContent);
            tableContent.removeAllViews();
            File path = getApplicationContext().getFilesDir();

            List<List> meddata = new ArrayList<>();

            try {

                File file=new File(path,head+"hisaab"+mon+year+"data.txt");
                if (!file.exists()) file.createNewFile();
                FileInputStream f1 = new FileInputStream(file);
                InputStreamReader r = new InputStreamReader(f1);
                BufferedReader br = new BufferedReader(r);
                StringBuilder sb = new StringBuilder();
                int flag=0;
                String txt;
                while ((txt = br.readLine()) != null) {

                    List<String> arr = Arrays.asList(txt.split(" "));

                    List<String> newarr = new ArrayList<>();

                    for (int i = 0; i < arr.size(); i++) {
                        if (!arr.get(i).trim().equals("")) {

                            if (filterday.equals("All")) {
                                newarr.add(arr.get(i));
                                newarr.add("+");
                                flag=1;
                            } else {
                                if (filterday.trim().equals(arr.get(arr.size() - 1).substring(0, 2).trim())) {
                                    newarr.add(arr.get(i));
                                    newarr.add("+");
                                    flag=1;
                                }
                            }

                        }


                    }

                    meddata.add(newarr);
                    sb.append(txt).append("\n");
                }

                if(flag==0){
                    nodata.setVisibility(View.VISIBLE);
                    card.setVisibility(View.GONE);
                    hview.setVisibility(View.GONE);
                    totalspend.setVisibility(View.GONE);

                }
                else{
                    nodata.setVisibility(View.GONE);
                    card.setVisibility(View.VISIBLE);
                    hview.setVisibility(View.VISIBLE);
                    totalspend.setVisibility(View.VISIBLE);
                }

                if (sb.toString().equals("")) {
                    //  e1.setText(" ");

                    y = 1;
                } else {

                    // e1.setText(sb.toString());
                    hisaabdata = convertToArray(meddata);

                    SharedPreferences sptheme = getSharedPreferences("theme", MODE_PRIVATE);
                    if (sptheme.getString("theme", "dark").equals("pink")) {
                        colhead1.setBackgroundResource(R.drawable.rectangle_box_black);
                        colhead2.setBackgroundResource(R.drawable.rectangle_box_black);
                        colhead3.setBackgroundResource(R.drawable.rectangle_box_black);
                    } else {
                        colhead1.setBackgroundResource(R.drawable.rectangle_box);
                        colhead2.setBackgroundResource(R.drawable.rectangle_box);
                        colhead3.setBackgroundResource(R.drawable.rectangle_box);
                    }
                    if (hisaabdata.length > 0) {
                        for (String[] row : hisaabdata) {
                            addRow(tableContent, row);
                            filterlayout.setVisibility(View.VISIBLE);
                            pdfdownloadbut.setVisibility(View.VISIBLE);
                        }



                    } else {
                        /*hview.setVisibility(View.GONE);
                        filterlayout.setVisibility(View.GONE);
                        card.setVisibility(View.GONE);
                        totalspend.setVisibility(View.VISIBLE);*/
                    }
                }

                f1.close();
            } catch (FileNotFoundException ee) {
                ee.printStackTrace();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                Toast.makeText(this, "io exception", Toast.LENGTH_LONG).show();
            }

        } catch (Exception error) {
        }
        if (y == 0) return 1;
        else return 0;

    }

    public void open() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        a.setMessage("Do you want to delete the data of " + deldate.getText().toString().trim() + " ?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                deleteUserData(deldate.getText().toString().trim());
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

    public void deleteUserData(String date) {
        File path = getApplicationContext().getFilesDir();
        File file=new File(path,head+"hisaab"+mon+year+"data.txt");
        File tempFile = new File(path, "temp.txt");


        if (!file.exists()) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)))) {

            String line;
            boolean isDeleted = false;
            long total = 0;
            for (int i = 0; i < hisaabdata.length; i++) {

                if (!date.equals(hisaabdata[i][3])) {
                    bw.write(hisaabdata[i][1] + " " + hisaabdata[i][2] + " " + hisaabdata[i][3] + "\n");
                } else {
                    try {
                        total += Long.parseLong(hisaabdata[i][2].trim().substring(3));
                    } catch (Exception e) {
                        Toast.makeText(this, e.toString(), Toast.LENGTH_LONG).show();

                    }
                }
            }

            // Close readers/writers
            bw.flush();
            bw.close();
            br.close();

            // Delete the original file and rename the temp file
            if (file.delete()) {
                if (tempFile.renameTo(file)) {
                    Toast.makeText(this, "Data successfully deleted", Toast.LENGTH_SHORT).show();
                    monthUpdate(total);
                    yearUpdate(total);
                    addmoney(total);
                    show_data();
                    totalspend.setText("Total money spent : Rs." + monthlySpend());
                } else {
                    Toast.makeText(this, "Failed to rename temp file", Toast.LENGTH_LONG).show();
                }
            } else {
                //Toast.makeText(this, "Failed to delete original file", Toast.LENGTH_LONG).show();
            }

            if (!isDeleted) {
                //   Toast.makeText(this, "User account not found", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IO exception occurred while deleting user data", Toast.LENGTH_LONG).show();
        }
    }

    public long addmoney(long p) {
        File path = getApplicationContext().getFilesDir();
        long bal3 = 0;
        try {
            FileInputStream f = new FileInputStream(new File(path, head + "balance.txt"));
            InputStreamReader r = new InputStreamReader(f);
            BufferedReader br = new BufferedReader(r);
            String bal = br.readLine();
            if (bal == null) bal = "0";
            bal3 = Long.parseLong(bal) + p;
            f.close();
            FileOutputStream f2 = new FileOutputStream(new File(path, head + "balance.txt"));
            f2.write(Long.toString(bal3).getBytes());
            SharedPreferences sharedPreferences = getSharedPreferences(head + year, MODE_PRIVATE);
            sharedPreferences.edit().putString("balance", String.valueOf(bal3)).apply();
            f2.close();

        } catch (IOException e) {

            e.printStackTrace();
        }
        return bal3;
    }

    public void monthUpdate(long p) {
        File path = getApplicationContext().getFilesDir();
        try {
            String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);
            File file = new File(path, head+"monthlySpend" + d + ".txt");

            if (!file.exists()) file.createNewFile();
            FileInputStream f3 = new FileInputStream(file);

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            long l = Long.parseLong(b5) - p;
            String b3 = Long.toString(l);
            FileOutputStream f6 = new FileOutputStream(new File(path, head+"monthlySpend" + d + ".txt"));
            f6.write((b3).getBytes());
            f6.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }


    public void yearUpdate(long p) {
        File path = getApplicationContext().getFilesDir();
        try {
            String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);

            FileInputStream f3 = new FileInputStream(new File(path, head+"yearlySpend" + d + ".txt"));

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            long l = Long.parseLong(b5) - p;
            String b3 = Long.toString(l);
            FileOutputStream f6 = new FileOutputStream(new File(path, head+"yearlySpend" + d + ".txt"));
            f6.write((b3).getBytes());
            f6.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void addRow(LinearLayout tableContent, String[] rowData) {
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        List<TextView> textViews = new ArrayList<>(); // List to hold TextViews in the row
        // Find maximum height of TextViews and adjust width of columns
        int maxHeight = 0;
        p = 0;
        int maxlength = 0;
        for (String cellData : rowData) {
            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(110), // Set width to 0 initially
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.CENTER;
            params.setMargins(0, 0, 0, 0);
            textView.setLayoutParams(params);
            textView.setText(cellData);
            textView.setTextSize(16);
            if (cellData.length() > maxlength) maxlength = cellData.length();
            textView.setPadding(8, 8, 8, 8);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            SharedPreferences sptheme = getSharedPreferences("theme", MODE_PRIVATE);
            if (sptheme.getString("theme", "dark").equals("pink")) {
                textView.setBackgroundResource(R.drawable.rectangle_box_black);
            } else {
                textView.setBackgroundResource(R.drawable.rectangle_box);
            }

            // Add TextView to the list
            if (p != 0) {
                textViews.add(textView);
                rowLayout.addView(textView);
            }

            // Measure the height of the TextView
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int height = textView.getMeasuredHeight();
            if (height > maxHeight) {
                maxHeight = height;
            }

            p = 1;
        }

        // Set maximum height to all TextViews in the row
        int kk = 0;

        for (TextView textView : textViews) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            if (kk == 1) {
                params.width = dpToPx(110);
            }
            int height = maxHeight + (((maxlength / 16) + 1) * 60);
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            int width = displayMetrics.widthPixels;
            params.height=height;
            if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                params.width = ((width * LANDSCAPE_TABLE_WIDTH) / 100)/3;
            }
            else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
                //displaysize.setwidthTableColoumn(textView, PORTRAIT_TABLE_WIDTH);
                params.width = ((width * PORTRAIT_TABLE_WIDTH) / 100)/3;
            }
            textView.setLayoutParams(params);
            kk = 1;
        }

        // Add the row to the table content layout
        tableContent.addView(rowLayout);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public String[][] convertToArray(List<List> data) {
        monthtotalmoney = 0L;
        String[][] arr = new String[data.size()][4];
        int j = 0;
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).size() == 0) continue;
            arr[j][3] = data.get(i).get(data.get(i).size() - 2).toString();  //date
            arr[j][2] = data.get(i).get(data.get(i).size() - 4).toString();   //price
            String str = "";
            for (int k = 0; k < data.get(i).size() - 5; k++) {
                if (!data.get(i).get(k).toString().trim().equals("+")) {
                    str += data.get(i).get(k).toString().trim() + " ";
                }
            }
            arr[j][1] = str.trim();
            arr[j][0] = String.valueOf(i + 1);

            if (arr[j][2] != null || !arr[j][2].trim().equals("")) {
                try {
                    monthtotalmoney += Long.parseLong(arr[j][2].substring(3));
                } catch (Exception e) {
                    Toast.makeText(display.this, e.toString(), Toast.LENGTH_LONG).show();
                }
            }
            j += 1;
        }

        totalspend.setText("Total money spent : Rs." + monthtotalmoney);
        return arr;
    }

    public void createWindow() {


        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.warn_window, null);

        ImageView pback = popup.findViewById(R.id.popup_back);
        CheckBox checkBox = popup.findViewById(R.id.popup_warn_check);
        AppCompatButton gotit = popup.findViewById(R.id.popup_warn_gotit);
        TextView title = popup.findViewById(R.id.warn_popup_title);
        SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
        title.setText("Hii " + spitem.getString("name", "").trim().substring(0, 1).toUpperCase() + spitem.getString("name", "").trim().substring(1).toLowerCase() + " , hope you are using the app well");
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
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sp.edit().putBoolean("showwarn", false).apply();
                } else {
                    sp.edit().putBoolean("showwarn", true).apply();
                }
            }
        });

        gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clearDim(root);
            }
        });

        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
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


    private void changetheme() {
        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        LinearLayout selector = findViewById(R.id.display_selection_layout);
        if (sp.getString("theme", "dark").equals("pink")) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.toolbarpink));
            }
            b1.setBackgroundResource(R.drawable.loginbutpink);
            card.setCardBackgroundColor(getColor(deletecardbackgrounddark2));
            layout.setBackgroundColor(getColor(R.color.backgroundpink));
            toolbar.setBackgroundColor(getColor(R.color.toolbarpink));
            pdfdownloadbut.setBackgroundResource(R.drawable.circlebutpink);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            card.setCardBackgroundColor(getColor(deletecardbackgrounddark));
            layout.setBackgroundColor(getColor(R.color.backgrounddark));
            toolbar.setBackgroundColor(getColor(R.color.toolbardark));
            pdfdownloadbut.setBackgroundResource(R.drawable.circlebut);


        }

    }

    private String[][] arrData;
    private String pdfFileName, monthStr, yearStr;

    // Method to check permission and create PDF
    public void createPdf(String[][] arr, String pdfname, String month, String year) {
        arrData = arr;
        pdfFileName = pdfname;
        monthStr = month;
        yearStr = year;

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

        }
        try {
            ContentValues values = new ContentValues();
            pdfFileName += "_Expenses_" + spitem.getString("name", "");
            String path = "/HisaabAnalyser/" + spitem.getString("name", "") + "_" + sp.getString("userserial", "").trim() + "/DailyExpenses/" + year + "/" + mon;
            if (!filterday.toLowerCase().equals("all")) {
                path = path + "/DateExpenses/";
            }

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
                        interstitialAd.setFlag(1, "PDF saved in /Documents" + path + "/" + pdfFileName + ".pdf");
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Error creating PDF!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "Error saving file!", Toast.LENGTH_SHORT).show();
            }

        }catch (Exception e){
            Toast.makeText(display.this,e.toString(),Toast.LENGTH_LONG).show();
        }
    }

    // Create PDF content (used by both legacy and MediaStore methods)
    private void createPdfDocument(OutputStream outputStream) {
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdfDocument = new PdfDocument(writer);
        Document document = new Document(pdfDocument);

        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(30, 0, 50, 0);
        float startmargin=52.2f;
        Paragraph title = new Paragraph("Hisaab Analyser - Your Daily Expenses")
                .setBold()
                .setFontSize(26)
                .setTextAlignment(TextAlignment.CENTER);
        Date date=new Date();
        Paragraph fullname = new Paragraph("Name : "+sp.getString("fullname",""))
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(30,0,2,startmargin).setFontSize(13);
        Paragraph monthtitle;
if(filterday.toLowerCase().trim().equals("all")) {
    monthtitle = new Paragraph("Month & Year : " + monthStr + ", " + yearStr)
            .setBold()
            .setTextAlignment(TextAlignment.LEFT).setMargins(1, 0, 2, startmargin).setFontSize(13);
}else{
    monthtitle = new Paragraph("Hisaab Date : "+filterday+" " + monthStr + ", " + yearStr)
            .setBold()
            .setTextAlignment(TextAlignment.LEFT).setMargins(1, 0, 2, startmargin).setFontSize(13);
}

        Paragraph issuedate = new Paragraph("Issue Date : "+date)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT).setMargins(1,0,25,startmargin).setFontSize(13);

        float[] width = {100f, 150f, 120f,120f};
        Table table = new Table(width).setHorizontalAlignment(HorizontalAlignment.CENTER);
// Add header cells
        table.addCell(new Cell()
                .add(new Paragraph("Sno")
                        .setFontSize(15)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPaddingTop(5)
                        .setPaddingBottom(5))
                .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering for headers
                .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering

        table.addCell(new Cell()
                .add(new Paragraph("Item/Service")
                        .setFontSize(15)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPaddingTop(5)
                        .setPaddingBottom(5))
                .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering for headers
                .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering

        table.addCell(new Cell()
                .add(new Paragraph("Price")
                        .setFontSize(15)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPaddingTop(5)
                        .setPaddingBottom(5))
                .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering for headers
                .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering

        table.addCell(new Cell()
                .add(new Paragraph("Date")
                        .setFontSize(15)
                        .setBold()
                        .setTextAlignment(TextAlignment.CENTER)
                        .setPaddingTop(5)
                        .setPaddingBottom(5))
                .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering for headers
                .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering

// Initialize total spent
        int totalspent = 0;

// Add data rows
        for (String[] arrDatum : arrData) {
            totalspent += Integer.parseInt(arrDatum[2].substring(3));

            // Cell for "Sno"
            table.addCell(new Cell()
                    .add(new Paragraph(arrDatum[0])
                            .setFontSize(14)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering
                    .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering

            // Cell for "Item/Service" with maximum width
            Cell itemServiceCell = new Cell()
                    .add(new Paragraph(arrDatum[1])
                            .setFontSize(14)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMaxWidth(60) // Set maximum width for the cell
                    .setHorizontalAlignment(HorizontalAlignment.CENTER); // Center horizontally

            table.addCell(itemServiceCell);

            // Cell for "Price"
            table.addCell(new Cell()
                    .add(new Paragraph(arrDatum[2])
                            .setFontSize(14)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering
                    .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering

            // Cell for "Date"
            table.addCell(new Cell()
                    .add(new Paragraph(arrDatum[3])
                            .setFontSize(14)
                            .setTextAlignment(TextAlignment.CENTER))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering
                    .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering
        }

// Add total spent paragraph
        Paragraph total = new Paragraph("Your total spent is : Rs." + totalspent)
                .setFontSize(14)
                .setMarginTop(30)
                .setBold()
                .setTextAlignment(TextAlignment.LEFT)
                .setMargins(30, 0, 25, startmargin);

        document.add(title);
        document.add(fullname);
        document.add(monthtitle);
        document.add(issuedate);
        document.add(table);
        document.add(total);

// Add footer to each page
        for (int i = 1; i <= pdfDocument.getNumberOfPages(); i++) {
            // Create a footer paragraph
            Paragraph footer = new Paragraph("Powered by Hisaab Analyser - Your Trusted Expense Tracker app.\n")
                    .setFontSize(13)
                    .setTextAlignment(TextAlignment.CENTER);

            // Add the footer to each page, positioned 16 units above the bottom edge
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
                    Toast.makeText(display.this, "Failed to create directory!", Toast.LENGTH_SHORT).show();
                }
                else{
                    createPdfFileUsingMediaStore();
                }
            }
            else{
                createPdfFileUsingMediaStore();
            }

        } catch (Exception e) {
            Toast.makeText(display.this, e.toString(), Toast.LENGTH_LONG).show();
        }

    }

    private void checkOrientation() {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(b1,50);
            displaysize.setfixHeight(card,11);
            displaysize.setfixHeight(b1,7);
            displaysize.setfixWidth(totalspend,65);
            displaysize.setfixWidth(card,65);
            displaysize.setwidthTableColoumn(colhead1,LANDSCAPE_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead2,LANDSCAPE_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead3,LANDSCAPE_TABLE_WIDTH);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {

            displaysize.setwidthTableColoumn(colhead1,PORTRAIT_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead2,PORTRAIT_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead3,PORTRAIT_TABLE_WIDTH);
            displaysize.setfixWidth(totalspend,PORTRAIT_TABLE_WIDTH);
            displaysize.setfixWidth(card,PORTRAIT_TABLE_WIDTH);
            displaysize.setfixWidth(b1,PORTRAIT_TABLE_WIDTH);

        }
    }

}

