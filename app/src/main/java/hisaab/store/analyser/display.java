package hisaab.store.analyser;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.SearchView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import java.util.Objects;


import com.google.android.play.core.review.ReviewInfo;
import com.google.android.play.core.review.ReviewManager;
import com.google.android.play.core.review.ReviewManagerFactory;
import com.google.android.gms.tasks.Task;


public class display extends AppCompatActivity {
    int PORTRAIT_TABLE_WIDTH =89;
    TextView e1, totalspend,datetitle;
    RelativeLayout layout,carddelete;
    SearchView searchview;
    int countnew=0;
    EditText snoedit;
    LinearLayout selector,snolayoutDelete, delete_date_layout,deletetypelayout;
    boolean isrow=false;
    Utils utils;
    TextView toolbartxt;
    String[] montharr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
    String[] arr2 = {"2024", "2025", "2026", "2027", "2028", "2029", "2030", "2031", "2034", "2035", "2036", "2037", "2038", "2039", "2040", "2041", "2042", "2043", "2044", "2045", "2046", "2047", "2048", "2049", "2050"};
    String[] deletetypearr={"Select","Delete data of whole day","Delete a single row data"};
    Configuration config;



    int LANDSCAPE_TABLE_WIDTH=65;

    SearchView.SearchAutoComplete searchText;
    TextView colhead1, colhead2, colhead3,colhead0;
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
    Spinner monthSpinner, yearSpinner, filterspinner,deletespinner;

    long monthtotalmoney = 0L;
    SharedPreferences spCounter;
    SharedPreferences.Editor editorCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);
        try {
            spCounter = getSharedPreferences("counter", MODE_PRIVATE);
            editorCounter = spCounter.edit();

            displaysize=new AdjustSizeConfiguration(display.this);
            config = getResources().getConfiguration();
            hview = findViewById(R.id.presshowhorizon);
            utils=new Utils();
            root = (ViewGroup) getWindow().getDecorView().getRootView();
            totalspend = findViewById(R.id.display_total);
            toolbar = findViewById(R.id.display_toolbar);
            b1 = findViewById(R.id.db);
            deletetypelayout=findViewById(R.id.delete_typelayout_display);
            delete_date_layout = findViewById(R.id.delete_datelayout_display);
            pdfdownloadbut=findViewById(R.id.displayPdfdownload);
            monthSpinner = findViewById(R.id.spin2);
            yearSpinner = findViewById(R.id.spin3);
            filterspinner = findViewById(R.id.display_filter);
            deletespinner=findViewById(R.id.deletetype_filter);
            deldate = findViewById(R.id.date_deletedata);
            snoedit=findViewById(R.id.displaysno);
            snolayoutDelete=findViewById(R.id.deletesno_displaylayout);
            searchview=findViewById(R.id.searchView_display);
            layout = findViewById(R.id.display_layout);
            datetitle=findViewById(R.id.datetitle_display);
            colhead0 = findViewById(R.id.display_showbox_4);
            colhead1 = findViewById(R.id.display_showbox_1);
            colhead2 = findViewById(R.id.display_showbox_2);
            colhead3 = findViewById(R.id.display_showbox_3);
            nodata=findViewById(R.id.nodata_display);
            deletebut = findViewById(R.id.deletedisplaypage_but);
            carddelete = findViewById(R.id.carddisplay2);
            searchText = searchview.findViewById(androidx.appcompat.R.id.search_src_text);
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

            snolayoutDelete.setVisibility(View.GONE);
            delete_date_layout.setVisibility(View.GONE);
            deletetypelayout.setVisibility(View.GONE);
            deletebut.setVisibility(View.GONE);

            ButtonEffect buttonEffect=new ButtonEffect(display.this);
            buttonEffect.buttonEffect(pdfdownloadbut);

            if(getIntent().getStringExtra("key").equals("hisaab")) {
                toolbartxt.setText("View Hisaab");
            }
            else{
                toolbartxt.setText("Income History");
            }

            hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (hidekey == 0) {
                        hide.setImageDrawable(getDrawable(R.drawable.baseline_keyboard_arrow_up_24));
                        deletetypelayout.setVisibility(View.VISIBLE);
                        hidekey = 1;
                    } else {
                        hide.setImageDrawable(getDrawable(R.drawable.addplus));

                        delete_date_layout.setVisibility(View.GONE);
                        snolayoutDelete.setVisibility(View.GONE);
                        deletebut.setVisibility(View.GONE);
                        deletetypelayout.setVisibility(View.GONE);
                        hidekey = 0;
                    }
                }
            });


            searchview.setIconifiedByDefault(false);

            searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    show_data();
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    show_data();
                    return true;
                }
            });

            searchview.setOnClickListener(v -> {

                searchview.setIconified(false);
                searchview.requestFocus();

        });



            pdfdownloadbut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder a = new AlertDialog.Builder(display.this);
                    if(!filterday.toLowerCase().equals("all")){
                        if(searchText.getText().toString().trim().equals(""))
                        a.setMessage("Do you want to download your expenses of " + filterday +" "+mon+" "+year+" ?");
                        else a.setMessage("Do you want to download the search expenses of " + filterday +" "+mon+" "+year+" ?\n\nNote: Only visible expenses will be downloaded.");
                    }
                    else{
                        if(searchText.getText().toString().trim().equals(""))
                        a.setMessage("Do you want to download your expenses of " +mon+" "+year+" ?");
                        else
                            a.setMessage("Do you want to download the search expenses of " +mon+" "+year+" ?\n\nNote: Only visible expenses will be downloaded.");

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
                    int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
                    if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
                        // Change button text colors only in Dark Mode
                        alerts.getButton(DialogInterface.BUTTON_POSITIVE)
                                .setTextColor(ContextCompat.getColor(display.this, android.R.color.white));
                        alerts.getButton(DialogInterface.BUTTON_NEGATIVE)
                                .setTextColor(ContextCompat.getColor(display.this, android.R.color.white));
                    }

                }
            });

            if (!sp.getBoolean("islogged", false)) {
                Intent i = new Intent(display.this, MainActivity.class);
                startActivity(i);
                finishAffinity();
            }


            if (sptheme.getString("theme", "dark").equals("pink")) {
                SpinnerAdapter1 adapter = new SpinnerAdapter1(display.this, R.layout.spinner_login_pink, montharr);
                monthSpinner.setAdapter(adapter);

                SpinnerAdapter1 adapterdelete = new SpinnerAdapter1(display.this, R.layout.spinner_login_pink,deletetypearr);
                deletespinner.setAdapter(adapterdelete);

            } else {
                SpinnerAdapter1 adapter = new SpinnerAdapter1(display.this, R.layout.spinner_login, montharr);
                monthSpinner.setAdapter(adapter);

                SpinnerAdapter1 adapterdelete = new SpinnerAdapter1(display.this, R.layout.spinner_login, deletetypearr);
                deletespinner.setAdapter(adapterdelete);
            }



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

                        if(isrow){
                            if(!snoedit.getText().toString().trim().equals("")){
                                open();
                            }
                            else{
                                Toast.makeText(v.getContext(),"Please enter sno. of row to be deleted!",Toast.LENGTH_SHORT).show();
                            }
                        }
                        else{
                            if(!deldate.getText().toString().trim().equals(""))
                            open();
                            else Toast.makeText(v.getContext(),"Please enter date!",Toast.LENGTH_SHORT).show();
                        }


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
                try {
                    if (!isclicked) {
                        isclicked = false;
                        if (show_data() == 1) {
                            b1.setText("Search");
                            //searchview.setVisibility(View.VISIBLE);
                        } else {
                            e1.setText("");
                            nodata.setVisibility(View.VISIBLE);
                            b1.setVisibility(View.GONE);
                            totalspend.setVisibility(View.GONE);
                            carddelete.setVisibility(View.GONE);
                            searchview.setVisibility(View.GONE);
                            Toast.makeText(this, "No data found!", Toast.LENGTH_LONG).show();
                        }

                        handleButtonClick();
                    }
                }
                catch (Exception e){
                    Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();
                }
            });


            deletespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position==1){
                        snolayoutDelete.setVisibility(View.GONE);
                        delete_date_layout.setVisibility(View.VISIBLE);
                        deletebut.setVisibility(View.VISIBLE);
                        isrow=false;
                    }
                    else if(position==2){
                          snolayoutDelete.setVisibility(View.VISIBLE);
                        delete_date_layout.setVisibility(View.GONE);
                        deletebut.setVisibility(View.VISIBLE);
                          isrow=true;
                    }
                    else{
                        snolayoutDelete.setVisibility(View.GONE);
                        delete_date_layout.setVisibility(View.GONE);
                        deletebut.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });

            yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    year = parent.getItemAtPosition(i).toString();
                    b1.setText("Search");
                    isclicked = false;
                    b1.setVisibility(View.VISIBLE);
                    hview.setVisibility(View.GONE);
                    pdfdownloadbut.setVisibility(View.GONE);
                    nodata.setVisibility(View.GONE);
                    totalspend.setVisibility(View.GONE);
                    carddelete.setVisibility(View.GONE);
                    searchview.setVisibility(View.GONE);
                    setFilter(mon, year);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            filterspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    countnew=0;
                    filterday = parent.getItemAtPosition(position).toString();
                    b1.setVisibility(View.VISIBLE);
                    isclicked = false;
                    nodata.setVisibility(View.GONE);
                    if (i == 1) {
                        b1.setText("Search");
                        pdfdownloadbut.setVisibility(View.GONE);
                        carddelete.setVisibility(View.GONE);
                        searchview.setVisibility(View.GONE);
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
            mon = montharr[now.get(Calendar.MONTH)];
            yearSpinner.setSelection(now.get(Calendar.YEAR) - 2024);
            year = arr2[now.get(Calendar.YEAR) - 2024];
            filterspinner.setSelection(0);

            monthSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    mon = parent.getItemAtPosition(i).toString();
                    b1.setText("Search");
                    b1.setVisibility(View.VISIBLE);
                    //e1.setText("");
                    isclicked = false;
                    nodata.setVisibility(View.GONE);
                    carddelete.setVisibility(View.GONE);
                    searchview.setVisibility(View.GONE);
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
            String[] dateofmontharr = {"All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17","18","19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31"};
            if (sptheme.getString("theme", "dark").equals("pink")) {
                filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_login_pink, dateofmontharr);
            } else {
                filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_login, dateofmontharr);
            }
        } else if (mon.equals("Feb")) {
            if (isLeapYear(Integer.parseInt(year))) {
                String[] dateofmontharr = {"All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17","18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29"};
                if (sptheme.getString("theme", "dark").equals("pink")) {
                    filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_login_pink, dateofmontharr);
                } else {
                    filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_login, dateofmontharr);
                }
            } else {
                String[] dateofmontharr = {"All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17","18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28"};
                if (sptheme.getString("theme", "dark").equals("pink")) {
                    filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_login_pink, dateofmontharr);
                } else {
                    filteradapter = new SpinnerAdapter1(display.this, R.layout.spinner_login, dateofmontharr);
                }
            }
        } else {
            String[] dateofmontharr = {"All", "01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17","18","19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30"};
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
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Change button text colors only in Dark Mode
            datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(display.this, android.R.color.white));
            datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(display.this, android.R.color.white));
        }
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



    public int show_data() {
        int y = 0;
        try {
            LinearLayout tableContent = findViewById(R.id.tableContent);
            tableContent.removeAllViews();
            File path = getApplicationContext().getFilesDir();

            List<List> meddata = new ArrayList<>();

            try {
                TextView item=findViewById(R.id.display_showbox_1);
                TextView price=findViewById(R.id.display_showbox_2);
                TextView datee_=findViewById(R.id.display_showbox_3);
                TextView sno=findViewById(R.id.display_showbox_4);
                File file;
                sno.setText("Sno");

                if(Objects.equals(getIntent().getStringExtra("key"), "hisaab")) {
                    file= new File(path, head + "hisaab" + mon + year + "data.txt");
                    item.setText("Item");
                    price.setText("Price");
                    datee_.setText("Date");


                }
                else{


                    file = new File(path, head +mon+year+"money_history.txt");
                    item.setText("Income Source");
                    price.setText("Total\nAmount");
                    datee_.setText("Date of Receive");


                }

                if (!file.exists()) file.createNewFile();
                FileInputStream f1 = new FileInputStream(file);
                InputStreamReader r = new InputStreamReader(f1);
                BufferedReader br = new BufferedReader(r);
                StringBuilder sb = new StringBuilder();
                int flag=0;
                String txt;
                while ((txt = br.readLine()) != null) {

                    List<String> arr = Arrays.asList(txt.split("#"));

                    if (arr.size() == 4) {

                        if (filterday.equals("All")) {
                            flag = 1;

                            String search = searchText.getText().toString().trim().toLowerCase();

                            if (search.isEmpty() ||
                                    arr.get(0).toLowerCase().contains(search) ||
                                    arr.get(1).toLowerCase().contains(search) ||
                                    arr.get(2).toLowerCase().contains(search)) {

                                meddata.add(arr);
                                flag = 1;
                            }

                        } else {
                            System.out.println("sooarr2"+filterday.trim().equals(arr.get(2).substring(0, 2).trim()));
                            if (filterday.trim().equals(arr.get(2).substring(0, 2).trim())) {
                                flag = 1;
                                String search = searchText.getText().toString().trim().toLowerCase();

                                if (search.isEmpty() ||
                                        arr.get(0).toLowerCase().contains(search) ||
                                        arr.get(1).toLowerCase().contains(search) ||
                                        arr.get(2).toLowerCase().contains(search)) {

                                    meddata.add(arr);
                                    flag = 1;
                                }

                            }
                        }

                        try {
                            sb.append(txt).append("\n");
                        }catch (Exception e){
                            Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
                        }

                    }
                }

                if(flag==0){
                    nodata.setVisibility(View.VISIBLE);
                    carddelete.setVisibility(View.GONE);
                    searchview.setVisibility(View.GONE);
                    hview.setVisibility(View.GONE);
                    totalspend.setVisibility(View.GONE);

                }
                else{
                    nodata.setVisibility(View.GONE);
                    Calendar now = Calendar.getInstance();
                    if(getIntent().getStringExtra("key").equals("hisaab")) {
                        if (year.equals(arr2[now.get(Calendar.YEAR) - 2024]) && mon.equals(montharr[now.get(Calendar.MONTH)])) {
                            carddelete.setVisibility(View.VISIBLE);
                            searchview.setVisibility(View.VISIBLE);
                        } else {
                            carddelete.setVisibility(View.GONE);
                            searchview.setVisibility(View.GONE);
                        }
                    }

                    hview.setVisibility(View.VISIBLE);
                    totalspend.setVisibility(View.VISIBLE);
                }

                if (sb.toString().equals("")) {
                    b1.setVisibility(View.GONE);
                    y = 1;
                } else {

                   hisaabdata = convertToArray(meddata);


                    SharedPreferences sptheme = getSharedPreferences("theme", MODE_PRIVATE);
                    if (sptheme.getString("theme", "dark").equals("pink")) {
                        colhead0.setBackgroundResource(R.drawable.rectangle_box_black);
                        colhead1.setBackgroundResource(R.drawable.rectangle_box_black);
                        colhead2.setBackgroundResource(R.drawable.rectangle_box_black);
                        colhead3.setBackgroundResource(R.drawable.rectangle_box_black);
                    } else {
                        colhead0.setBackgroundResource(R.drawable.rectangle_box);
                        colhead1.setBackgroundResource(R.drawable.rectangle_box);
                        colhead2.setBackgroundResource(R.drawable.rectangle_box);
                        colhead3.setBackgroundResource(R.drawable.rectangle_box);
                    }



               if (hisaabdata!=null && hisaabdata.length > 0 ) {
                   System.out.println("kky"+Arrays.deepToString(hisaabdata));
                   searchview.setVisibility(View.VISIBLE);
                   /*
                        for (String[] row : hisaabdata) {
                            addRow(tableContent, row);

                        }*/
                        String previousDate = "";
                        countnew=0;
                        if(getIntent().getStringExtra("key").equals("hisaab")) {
                            String[] headerRow = {"Sno", "Item", "Price", "Date"};
                            addRow(tableContent, headerRow);
                        }
                        else{
                            String[] headerRow = {"Sno", "Source", "Amount", "Date"};
                            addRow(tableContent, headerRow);
                        }


                        for (String[] row : hisaabdata) {
                            String currentDate = row[2]; // assuming date is at index 2
                            pdfdownloadbut.setVisibility(View.VISIBLE);
                            // If date changes, add a blank row first
                            if (!previousDate.equals("") && !currentDate.equals(previousDate)) {
                                /*String[] blankRow = {"", "", ""};
                                addRow(tableContent, blankRow);*/
                            }



                            addRow(tableContent,row);

                            // Update previousDate
                            previousDate = currentDate;
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
                Toast.makeText(this,ee.toString(),Toast.LENGTH_SHORT).show();
            } catch (IOException ioException) {
                ioException.printStackTrace();
                Toast.makeText(this, "io exception", Toast.LENGTH_LONG).show();
            }

        } catch (Exception error) {
            Toast.makeText(this,error.toString(),Toast.LENGTH_SHORT).show();
        }
        if (y == 0) return 1;
        else return 0;

    }

    public void open() {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        if(!isrow)
        a.setMessage("Do you want to delete the data of " + deldate.getText().toString().trim() + " ?");
        else a.setMessage("Do you want to delete the data with sno " + snoedit.getText().toString().trim() + " ?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(!isrow)
                deleteUserData(deldate.getText().toString().trim());
                else deleteUserData(snoedit.getText().toString().trim());
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
                    .setTextColor(ContextCompat.getColor(display.this, android.R.color.white));
            alerts.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(display.this, android.R.color.white));
        }
    }

    public void deleteUserData(String dateorsno) {

        File path = getApplicationContext().getFilesDir();
        File file;

        if ("hisaab".equals(getIntent().getStringExtra("key"))) {
            file = new File(path, head + "hisaab" + mon + year + "data.txt");
        } else {
            file = new File(path, head + mon + year + "money_history.txt");
        }

        File tempFile = new File(path, "temp.txt");

        if (!file.exists()) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            return;
        }

        boolean isDeleted = false;
        long total = 0;

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)))) {

            String line;

            while ((line = br.readLine()) != null) {

                String[] parts = line.split("#");
 if(!isrow) {
     if (parts.length >= 4 && parts[2].equals(dateorsno)) {
         isDeleted = true;
         try {
             total += Long.parseLong(parts[1].trim().substring(3)); // remove Rs.
         } catch (Exception ignored) {
         }
     } else {
         bw.write(line + "\n");
     }
 }
 else{
     if (parts.length >= 4 && parts[3].equals(dateorsno)) {
         isDeleted = true;
         try {
             total += Long.parseLong(parts[1].trim().substring(3)); // remove Rs.
         } catch (Exception ignored) {
         }
     } else {
         bw.write(line + "\n");
     }
 }
            }
        } catch (Exception e) {
            Toast.makeText(this, "Error: " + e, Toast.LENGTH_LONG).show();
            return;
        }

        if (!isDeleted) {
            tempFile.delete();
            Toast.makeText(this, "No data found on with sno. " + dateorsno, Toast.LENGTH_LONG).show();
            return;
        }

        if (file.delete() && tempFile.renameTo(file)) {

            monthUpdate(total);
            yearUpdate(total);
            addmoney(total);
            show_data();
            totalspend.setText("Total money spent : Rs." + utils.monthlySpend(mon,this,year));

            Toast.makeText(this, "Data successfully deleted", Toast.LENGTH_SHORT).show();


        } else {
            Toast.makeText(this, "Failed to update file", Toast.LENGTH_LONG).show();
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
            String mon = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
            String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            File file = new File(path, head+"monthlySpend" + montharr[Integer.parseInt(mon)-1]+year+ ".txt");

            if (!file.exists()) file.createNewFile();
            FileInputStream f3 = new FileInputStream(file);

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            long l = Long.parseLong(b5) - p;
            String b3 = Long.toString(l);
            FileOutputStream f6 = new FileOutputStream(file);
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


            FileInputStream f3 = new FileInputStream(new File(path, head+"yearlySpend" + date + ".txt"));

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            long l = Long.parseLong(b5) - p;
            String b3 = Long.toString(l);
            FileOutputStream f6 = new FileOutputStream(new File(path, head+"yearlySpend" + date + ".txt"));
            f6.write((b3).getBytes());
            f6.close();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    private void addRow(LinearLayout tableContent, String[] currData) {

        String[] rowData = new String[4];
        if (countnew == 1) {
            rowData[0] = currData[3];
            rowData[1] = currData[0];
            rowData[2] = currData[1];
            rowData[3] = currData[2];
        } else {
            countnew = 1;
            rowData = currData;
        }

        boolean isHeader = (tableContent.getChildCount() == 0);

        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        List<TextView> textViews = new ArrayList<>();
        int maxHeight = 0;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int tableWidth = (screenWidth * 90) / 100; // 90% of screen width
        int snoWidth = dpToPx(40);
        int remainingWidth = tableWidth - snoWidth;
        int otherColWidth = remainingWidth / (rowData.length - 1);

        // LOOP
        for (int i = 0; i < rowData.length; i++) {

            TextView textView = new TextView(this);
            int colWidth = (i == 0) ? snoWidth : otherColWidth;

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    colWidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity = Gravity.CENTER;
            textView.setLayoutParams(params);

            textView.setText(rowData[i]);
            textView.setGravity(Gravity.CENTER);

            // 🔥 REQUIRED FIX 🔥
            textView.setSingleLine(false);
            textView.setMaxLines(Integer.MAX_VALUE);
            textView.setEllipsize(null);

            // Header or normal row
            if (isHeader) {
                textView.setTypeface(null, Typeface.BOLD);
                textView.setTextSize(18);
                textView.setPadding(8, 10, 8, 10);
            } else {
                textView.setTextSize(16);
                textView.setPadding(8, 28, 8, 28);
            }

            // Theme
            SharedPreferences sptheme = getSharedPreferences("theme", MODE_PRIVATE);
            if (sptheme.getString("theme", "dark").equals("pink")) {
                textView.setTextColor(Color.BLACK);
                textView.setBackgroundResource(R.drawable.rectangle_box_black);
            } else {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.rectangle_box);
            }

            rowLayout.addView(textView);
            textViews.add(textView);

            // 🔥 Measure again after allowing wrapping
            int widthSpec = View.MeasureSpec.makeMeasureSpec(colWidth, View.MeasureSpec.EXACTLY);
            int heightSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            textView.measure(widthSpec, heightSpec);

            maxHeight = Math.max(maxHeight, textView.getMeasuredHeight());
        }

        //Make all cells same height
        for (TextView tv : textViews) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
            params.height = maxHeight;
            tv.setLayoutParams(params);
        }

        tableContent.addView(rowLayout);
    }

/*

    private void addRow(LinearLayout tableContent, String[] rowData) {

        boolean isHeader = tableContent.getChildCount() == 0; // first row is header

        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        List<TextView> textViews = new ArrayList<>();
        int maxHeight = 0;

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        int tableWidth = (screenWidth * 90) / 100; // 90% of screen width
        int snoWidth = dpToPx(30); // fixed width for S.No
        int remainingWidth = tableWidth - snoWidth;
        int otherColWidth = remainingWidth / (rowData.length - 1);

        for (int i = 0; i < rowData.length; i++) {
            TextView textView = new TextView(this);

            int colWidth = (i == 0) ? snoWidth : otherColWidth;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    colWidth,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.setMargins(0, 0, 0, 0); // no margin
            textView.setLayoutParams(params);
            textView.setGravity(Gravity.CENTER);

            textView.setText(rowData[i]);
            textView.setSingleLine(false);        // allow multiple lines

            // Set text size and bold for header
            if (isHeader) {
                textView.setTypeface(null, android.graphics.Typeface.BOLD);
                textView.setTextSize(17);
            } else {
                textView.setTextSize(16);
                textView.setPadding(4, 8, 4, 8); // slight padding for body rows
            }

            // Background and text color based on theme
            SharedPreferences sptheme = getSharedPreferences("theme", MODE_PRIVATE);
            if (sptheme.getString("theme", "dark").equals("pink")) {
                textView.setTextColor(Color.BLACK);
                textView.setBackgroundResource(R.drawable.rectangle_box_black);
            } else {
                textView.setTextColor(Color.WHITE);
                textView.setBackgroundResource(R.drawable.rectangle_box);
            }

            // Measure height
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int height = textView.getMeasuredHeight();
            if (height > maxHeight) maxHeight = height;

            textViews.add(textView);
            rowLayout.addView(textView);
        }

        // Set all cells in the row to the max height of that row
        for (TextView tv : textViews) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) tv.getLayoutParams();
            params.height = maxHeight+5;
            tv.setLayoutParams(params);
        }

        tableContent.addView(rowLayout);
    }
*/



    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }

    public String[][] convertToArray(List<List> data) {
        monthtotalmoney = 0L;
        String[][] arr = new String[data.size()][4];

        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).size() == 0) continue;
            arr[i][2] = data.get(i).get(2).toString();  //date
            arr[i][1] = data.get(i).get(1).toString();   //price
            arr[i][0]=data.get(i).get(0).toString();
            arr[i][3]=data.get(i).get(3).toString();
            //item
             if(getIntent().getStringExtra("key").equals("hisaab")) {
                 if (arr[i][1] != null || !arr[i][1].trim().equals("")) {
                     try {
                         monthtotalmoney += Long.parseLong(arr[i][1].substring(3));
                     } catch (Exception e) {
                         Toast.makeText(display.this, e.toString(), Toast.LENGTH_LONG).show();
                     }
                 }
             }
            else{

                 if (arr[i][1] != null || !arr[i][1].trim().equals("")) {
                     try {
                         monthtotalmoney += Long.parseLong(arr[i][1].substring(3));
                     } catch (Exception e) {
                         Toast.makeText(display.this, e.toString(), Toast.LENGTH_LONG).show();
                     }
                 }

            }
            }



        if(getIntent().getStringExtra("key").equals("hisaab")) {
            totalspend.setText("Total money spent : Rs." + monthtotalmoney);
        }else{
            totalspend.setText("Total Amount : Rs." + monthtotalmoney);
        }
        return arr;
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
        selector = findViewById(R.id.display_selection_layout);
        TextView selectypedelete=findViewById(R.id.datetypetitle_display);
        TextView snotitle=findViewById(R.id.sno_deletedata_display_title);
        TextView deletedatetitle=findViewById(R.id.deletedate_displaytitle);
        ImageView toolbarback=findViewById(R.id.display_back);
        toolbartxt=findViewById(R.id.display_toolbar_txt);

     /*   String savedHex = sp.getString("background", "#262627");
        if(sp.getString("thememode","default").equals("custom")) {
            layout.setBackgroundColor(Color.parseColor(savedHex));
            toolbar.setBackgroundColor(Color.parseColor(savedHex));
        }*/
        if (sp.getString("theme", "dark").equals("pink")) {
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            toolbarback.setColorFilter(getColor(R.color.black));
            toolbartxt.setTextColor(getColor(R.color.black));
            b1.setBackgroundResource(R.drawable.loginbutpink);
            colhead1.setTextColor(getColor(R.color.backgrounddark));
            totalspend.setTextColor(getColor(R.color.black));
            nodata.setAnimation(R.raw.nodata);



            ImageView searchIcon = searchview.findViewById(
                    androidx.appcompat.R.id.search_mag_icon);
            searchIcon.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN);

// Fix voice search icon also if needed
            ImageView closeIcon = searchview.findViewById(
                    androidx.appcompat.R.id.search_close_btn);
            closeIcon.setColorFilter(Color.BLACK, android.graphics.PorterDuff.Mode.SRC_IN);

            searchview.setBackground(getResources().getDrawable(R.drawable.searchbar_light));


            searchText.setTextColor(Color.BLACK);
            snotitle.setTextColor(getResources().getColor(R.color.primarylight));
            searchText.setHintTextColor(Color.GRAY);
            searchText.setHint("Search Item");


            datetitle.setTextColor(getResources().getColor(R.color.primarylight));

            monthSpinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.white))
            );
            monthSpinner.setBackground(getResources().getDrawable(R.drawable.border6));


            deletespinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.white))
            );
            deletedatetitle.setTextColor(getResources().getColor(R.color.primarylight));
           deletespinner.setBackground(getResources().getDrawable(R.drawable.border6));

           selectypedelete.setTextColor(getResources().getColor(R.color.primarylight));
            selector.setBackground(getResources().getDrawable(R.drawable.spinnerlayoutbg_light));

            yearSpinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.white))
            );
            yearSpinner.setBackground(getResources().getDrawable(R.drawable.border6));


            filterspinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.white))
            );
            filterspinner.setBackground(getResources().getDrawable(R.drawable.border6));

            colhead2.setTextColor(getColor(R.color.backgrounddark));
            colhead3.setTextColor(getColor(R.color.backgrounddark));
            colhead0.setTextColor(getColor(R.color.backgrounddark));


            if(!sp.getString("thememode","default").equals("custom")) {
                layout.setBackgroundColor(getColor(R.color.backgroundlight));
                toolbar.setBackgroundColor(getColor(R.color.backgroundlight));
            }
           delete_date_layout.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.lightspinnerbg)
            );
            snolayoutDelete.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.lightspinnerbg)
            );

                deletetypelayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.lightspinnerbg)
            );



            pdfdownloadbut.setBackgroundResource(R.drawable.circlebutpink);
            carddelete.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.lightviolet));
        } else {

            getWindow().setStatusBarColor(getColor(R.color.statusbarcolordark));
            getWindow().getDecorView().setSystemUiVisibility(getWindow().getDecorView().getSystemUiVisibility() & ~View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

            monthSpinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.spinnerdarkcolor))
            );
            monthSpinner.setBackground(getResources().getDrawable(R.drawable.loginbut3));




            snotitle.setTextColor(getResources().getColor(R.color.white));

            deletespinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.spinnerdarkcolor))
            );
            deletespinner.setBackground(getResources().getDrawable(R.drawable.loginbut3));
            selectypedelete.setTextColor(Color.WHITE);
            filterspinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.spinnerdarkcolor))
            );
            filterspinner.setBackground(getResources().getDrawable(R.drawable.loginbut3));
            deletedatetitle.setTextColor(getResources().getColor(R.color.white));


            ImageView searchIcon = searchview.findViewById(
                    androidx.appcompat.R.id.search_mag_icon);
            searchIcon.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);

// Fix voice search icon also if needed
            ImageView closeIcon = searchview.findViewById(
                    androidx.appcompat.R.id.search_close_btn);
            closeIcon.setColorFilter(Color.WHITE, android.graphics.PorterDuff.Mode.SRC_IN);


            searchText.setTextColor(Color.WHITE);
            searchText.setHintTextColor(Color.GRAY);
            searchText.setHint("Search Item");

            searchview.setBackground(getResources().getDrawable(R.drawable.searchbar));

            selector.setBackground(getResources().getDrawable(R.drawable.border6_darkgray));



            //delete layout

            snolayoutDelete.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.search_dark2)
            );
               delete_date_layout.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.search_dark2)
            );

                  deletetypelayout.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.search_dark2)
            );




            datetitle.setTextColor(getResources().getColor(R.color.white));
            yearSpinner.setPopupBackgroundDrawable(
                    new ColorDrawable(ContextCompat.getColor(this, R.color.spinnerdarkcolor)));
            yearSpinner.setBackground(getResources().getDrawable(R.drawable.loginbut3));
            nodata.setAnimation(R.raw.nograph);
            toolbarback.setColorFilter(getColor(R.color.white));
            toolbartxt.setTextColor(getColor(R.color.white));
            colhead1.setTextColor(getColor(R.color.white));
            colhead2.setTextColor(getColor(R.color.white));
            totalspend.setTextColor(getColor(R.color.white));
            colhead3.setTextColor(getColor(R.color.white));
            colhead0.setTextColor(getColor(R.color.white));

            carddelete.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.bg2)
            );


            if(!sp.getString("thememode","default").equals("custom")) {
                layout.setBackgroundColor(getColor(R.color.backgrounddark));
                toolbar.setBackgroundColor(getColor(R.color.backgrounddark));
            }

            pdfdownloadbut.setBackgroundResource(R.drawable.circlebut);


        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            carddelete.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
        } else {
            carddelete.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
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
            String path;
            if(getIntent().getStringExtra("key").equals("hisaab")) {
                pdfFileName += "_Expenses_" + spitem.getString("name", "");
                path = "/HisaabAnalyser/" + spitem.getString("name", "") + "_" + sp.getString("userserial", "").trim() + "/DailyExpenses/" + year + "/" + mon;
                if (!filterday.toLowerCase().equals("all")) {
                    path = path + "/DateExpenses/";
                }
            }
            else{
                pdfFileName += "_IncomeHistory_" + spitem.getString("name", "");
                path = "/HisaabAnalyser/" + spitem.getString("name", "") + "_" + sp.getString("userserial", "").trim() + "/Income/" + year + "/" + mon;
                if (!filterday.toLowerCase().equals("all")) {
                    path = path + "/Date/";
                }
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
        Document document = new Document(pdfDocument,PageSize.A4,false);

        pdfDocument.setDefaultPageSize(PageSize.A4);
        document.setMargins(30, 0, 50, 0);
        float startmargin=52.2f;
        Paragraph title;

        if(Objects.equals(getIntent().getStringExtra("key"), "hisaab")) {

            title = new Paragraph("Hisaab Analyser - Your Daily Expenses")
                    .setBold()
                    .setFontSize(26)
                    .setTextAlignment(TextAlignment.CENTER);
        }
        else{
            title = new Paragraph("Hisaab Analyser - Your Income History")
                    .setBold()
                    .setFontSize(26)
                    .setTextAlignment(TextAlignment.CENTER);
        }
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


        if(getIntent().getStringExtra("key").equals("hisaab")) {
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
        }
        else{
            table.addCell(new Cell()
                    .add(new Paragraph("Income Source")
                            .setFontSize(15)
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPaddingTop(5)
                            .setPaddingBottom(5))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering for headers
                    .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering


            table.addCell(new Cell()
                    .add(new Paragraph("Amount")
                            .setFontSize(15)
                            .setBold()
                            .setTextAlignment(TextAlignment.CENTER)
                            .setPaddingTop(5)
                            .setPaddingBottom(5))
                    .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering for headers
                    .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering
        }


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
int j=0;
        for (String[] arrDatum : arrData) {
            j++;
try {
    totalspent += Integer.parseInt(arrDatum[1].substring(3));


    table.addCell(new Cell()
            .add(new Paragraph(String.valueOf(j))
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER))
            .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering
            .setTextAlignment(TextAlignment.CENTER));
    // Cell for "Item/Service" with maximum width

    Cell itemServiceCell = new Cell()
            .add(new Paragraph(arrDatum[0])
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER))
            .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering
            .setTextAlignment(TextAlignment.CENTER)
            .setMaxWidth(60) // Set maximum width for the cell
            .setHorizontalAlignment(HorizontalAlignment.CENTER); // Center horizontally

    table.addCell(itemServiceCell);

    // Cell for "Price"
    table.addCell(new Cell()
            .add(new Paragraph(arrDatum[1])
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER))
            .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering
            .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering

    // Cell for "Date"
    table.addCell(new Cell()
            .add(new Paragraph(arrDatum[2])
                    .setFontSize(14)
                    .setTextAlignment(TextAlignment.CENTER))
            .setVerticalAlignment(VerticalAlignment.MIDDLE) // Vertical centering
            .setTextAlignment(TextAlignment.CENTER)); // Ensure horizontal centering

}catch (Exception e){}
        }

        Paragraph total;
        if(getIntent().getStringExtra("key").equals("hisaab")) {
             total= new Paragraph("Your total spent is : Rs." + totalspent)
                    .setFontSize(14)
                    .setMarginTop(30)
                    .setBold()
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMargins(30, 0, 25, startmargin);
        }
        else{
            total= new Paragraph("Your total Income is : Rs." + totalspent)
                    .setFontSize(14)
                    .setMarginTop(30)
                    .setBold()
                    .setTextAlignment(TextAlignment.LEFT)
                    .setMargins(30, 0, 25, startmargin);
        }

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
            displaysize.setfixWidth(selector,50);
            displaysize.setfixHeight(carddelete,11);
            displaysize.setfixHeight(b1,7);
            displaysize.setfixWidth(totalspend,65);
            displaysize.setfixWidth(carddelete,65);
            displaysize.setwidthTableColoumn(colhead1,LANDSCAPE_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead2,LANDSCAPE_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead3,LANDSCAPE_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead0,LANDSCAPE_TABLE_WIDTH);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {

            displaysize.setwidthTableColoumn(colhead1,PORTRAIT_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead2,PORTRAIT_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead3,PORTRAIT_TABLE_WIDTH);
            displaysize.setwidthTableColoumn(colhead0,PORTRAIT_TABLE_WIDTH);
            displaysize.setfixWidth(totalspend,PORTRAIT_TABLE_WIDTH);
            displaysize.setfixWidth(carddelete,PORTRAIT_TABLE_WIDTH);
            displaysize.setfixWidth(b1,PORTRAIT_TABLE_WIDTH);
            displaysize.setfixWidth(searchview,PORTRAIT_TABLE_WIDTH);


        }
    }





    @Override
    protected void onResume() {
        super.onResume();
        changetheme();
        checkOrientation();
    }



    private void handleButtonClick() {
        SharedPreferences sp = getSharedPreferences("counter", MODE_PRIVATE);
        boolean hasRated = sp.getBoolean("hasRated", false);
        int clickCount = sp.getInt("clickCount", 0);

        if (hasRated) return; // already rated, do nothing

        clickCount++;
        sp.edit().putInt("clickCount", clickCount).apply();

        if (clickCount >= 100) {
            requestReview(); // show review
        }
    }

    private void requestReview() {
        ReviewManager manager = ReviewManagerFactory.create(this);
        Task<ReviewInfo> request = manager.requestReviewFlow();
        request.addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ReviewInfo reviewInfo = task.getResult();
                Task<Void> flow = manager.launchReviewFlow(this, reviewInfo);

                flow.addOnCompleteListener(flowTask -> {
                    SharedPreferences sp = getSharedPreferences("counter", MODE_PRIVATE);
                    sp.edit().putBoolean("hasRated", true).apply();
                    sp.edit().putInt("clickCount", 0).apply();
                });

            } else {
                SharedPreferences sp = getSharedPreferences("counter", MODE_PRIVATE);
                sp.edit().putInt("clickCount", 0).apply(); // reset if request fails
            }
        });
    }



}
