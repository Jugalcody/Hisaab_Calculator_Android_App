package hisaab.store.analyser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisaabcalculator.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class display extends AppCompatActivity {
    TextView e1,totalspend;
   CardView card;
    AppCompatButton deletebut;
    String[][] arr2;

    TextView deldate;
    Button b1;
    int p=0;
    SharedPreferences sp,spitem;
    ImageView back,hide;
    AdView adView;
    HorizontalScrollView hview;
    int hidekey=0;
    String mon,year,head;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        try {

            hview=findViewById(R.id.presshowhorizon);
            totalspend=findViewById(R.id.display_total);
            Spinner s = findViewById(R.id.spin2);
            deldate=findViewById(R.id.date_deletedata);
            deletebut=findViewById(R.id.deletedisplaypage_but);
            hide=findViewById(R.id.hideunhide);
            hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LinearLayout ll=findViewById(R.id.deletelayout_display);
                    if(hidekey==0){
                        hide.setImageDrawable(getDrawable(R.drawable.baseline_keyboard_arrow_up_24));
                        ll.setVisibility(View.VISIBLE);
                        hidekey=1;
                    }
                    else{
                        hide.setImageDrawable(getDrawable(R.drawable.addplus));

                        ll.setVisibility(View.GONE);
                        hidekey=0;
                    }
                }
            });


            card=findViewById(R.id.carddisplay);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            spitem = getSharedPreferences("item", MODE_PRIVATE);
            String[] arr={"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};
            SpinnerAdapter1 adapter = new SpinnerAdapter1(display.this,R.layout.spinner_login,arr);
            s.setAdapter(adapter);
            Bundle e = getIntent().getExtras();
            head = spitem.getString("user", "");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primary));
            }
            Spinner s2 = findViewById(R.id.spin3);
            String[] arr2={"2024","2025","2026","2027","2028","2029","2030","2031","2034","2035","2036","2037","2038","2039","2040","2041","2042","2043","2044","2045","2046","2047","2048","2049","2050"};
            SpinnerAdapter1 adapter2 = new SpinnerAdapter1(display.this,R.layout.spinner_login,arr2);
            s2.setAdapter(adapter2);

            e1 = findViewById(R.id.de);
            e1.setVisibility(View.GONE);
            b1 = findViewById(R.id.db);
            back=findViewById(R.id.display_back);

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
                if (show_data() == 1) {
                    b1.setText("Showed");
                    card.setVisibility(View.VISIBLE);
                } else {
                    e1.setText(" ");
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }

            });
            s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    year = parent.getItemAtPosition(i).toString();
                    b1.setText("Show");
                    hview.setVisibility(View.GONE);
                    totalspend.setVisibility(View.GONE);
                    card.setVisibility(View.GONE);
                    //e1.setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    mon = parent.getItemAtPosition(i).toString();
                    b1.setText("Show");
                    //e1.setText("");
                    card.setVisibility(View.GONE);
                    hview.setVisibility(View.GONE);
                    totalspend.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }catch (Exception e){

        }
        adView=findViewById(R.id.adViewdisplay);
        // adView.setAdUnitId("ca-app-pub-1079506490540577/7402312139");
        //adView.setAdSize(getAdSize());
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

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


    public String monthlySpend(){
        File path=getApplicationContext().getFilesDir();
        String b4="0";
        int m;
        try{
            m=month(mon);
            FileInputStream f2=new FileInputStream(new File(path,head+m+year+"monthlySpend.txt"));
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
    public int show_data(){
        hview.setVisibility(View.GONE);
        totalspend.setVisibility(View.VISIBLE);
        totalspend.setText("Total money spend : Rs."+monthlySpend());
        LinearLayout tableContent=findViewById(R.id.tableContent);
        tableContent.removeAllViews();
        File path = getApplicationContext().getFilesDir();
        int y=0;

        List<List> meddata=new ArrayList<>();

        try {
            FileInputStream f1 = new FileInputStream(new File(path,head+mon+year+".txt"));
            InputStreamReader r = new InputStreamReader(f1);
            BufferedReader br = new BufferedReader(r);
            StringBuilder sb = new StringBuilder();
            String txt;
            while ((txt = br.readLine()) != null) {

                List<String> arr= Arrays.asList(txt.split(" "));

                List<String> newarr=new ArrayList<>();

                for(int i=0;i<arr.size();i++){
                    if(!arr.get(i).trim().equals("")) {
                        newarr.add(arr.get(i));
                        newarr.add("+");
                    }


                }

                meddata.add(newarr);
                System.out.println("kkl "+meddata.toString());
                sb.append(txt).append("\n");
            }

            if(sb.toString().equals("")) {
                //  e1.setText(" ");

                y = 1;
            }
            else {

                // e1.setText(sb.toString());
                arr2=convertToArray(meddata);
                for (String[] row : arr2) {
                    addRow(tableContent,row);
                }
            }
            f1.close();
        } catch(FileNotFoundException ee) {
            ee.printStackTrace();
        } catch(IOException ioException)
        {
            ioException.printStackTrace();
            Toast.makeText(this, "io exception", Toast.LENGTH_LONG).show();
        }
        if(y==0) return 1;
        else return 0;
    }

    public void open(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
        a.setMessage("Do you delete the data of "+deldate.getText().toString().trim()+" ?");
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

        AlertDialog alerts=a.create();
        alerts.show();
    }

    public void deleteUserData(String date) {
        File path = getApplicationContext().getFilesDir();
        File file = new File(path, head+mon+year+".txt");
        File tempFile = new File(path, "temp.txt");


        if (!file.exists()) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)))) {

            String line;
            boolean isDeleted = false;
            long total=0;
            for(int i=0;i<arr2.length;i++){

                if (!arr2[i][3].equals(date)) {
                    bw.write(arr2[i][1]+" "+arr2[i][2]+" "+arr2[i][3]);
                }
                else{
                    try {
                        total += Long.parseLong(arr2[i][2].trim().substring(3));
                    }
                    catch (Exception e){
                        Toast.makeText(this,e.toString(),Toast.LENGTH_LONG).show();

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
                    totalspend.setText("Total money spend : Rs."+monthlySpend());
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
    public long addmoney(long p){
        File path=getApplicationContext().getFilesDir();
        long bal3=0;
        try{
            FileInputStream f=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r=new InputStreamReader(f);
            BufferedReader br=new BufferedReader(r);
            String bal=br.readLine();
            if (bal==null) bal="0";
            bal3=Long.parseLong(bal)+p;
            f.close();
            FileOutputStream f2=new FileOutputStream(new File(path,head+"balance.txt"));
            f2.write(Long.toString(bal3).getBytes());
            f2.close();

        }
        catch(IOException e){

            e.printStackTrace();
        }
        return bal3;
    }

    public void monthUpdate(long p) {
        File path=getApplicationContext().getFilesDir();
        try {
            String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);

            FileInputStream f3 = new FileInputStream(new File(path, head +d+"monthlySpend.txt"));

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            long l = Long.parseLong(b5) - p;
            String b3 =Long.toString(l);
            FileOutputStream f6 = new FileOutputStream(new File(path, head+d+ "monthlySpend.txt"));
            f6.write((b3).getBytes());
            f6.close();
        }catch(IOException e){

            e.printStackTrace();
        }
    }


    public void yearUpdate(long p) {
        File path=getApplicationContext().getFilesDir();
        try {
            String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);

            FileInputStream f3 = new FileInputStream(new File(path, head +d+"yearlySpend.txt"));

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            long l = Long.parseLong(b5) - p;
            String b3 =Long.toString(l);
            FileOutputStream f6 = new FileOutputStream(new File(path, head+d+ "yearlySpend.txt"));
            f6.write((b3).getBytes());
            f6.close();
        }catch(IOException e){

            e.printStackTrace();
        }
    }
    private void addRow(LinearLayout tableContent, String[] rowData) {
        hview.setVisibility(View.VISIBLE);
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        List<TextView> textViews = new ArrayList<>(); // List to hold TextViews in the row

        // Find maximum height of TextViews and adjust width of columns
        int maxHeight = 0;
        p=0;
        int maxlength=0;
        for (String cellData : rowData) {

            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(110), // Set width to 0 initially
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity=Gravity.CENTER;
            params.setMargins(0,0,0,0);
            textView.setLayoutParams(params);
            textView.setText(cellData);
            textView.setTextSize(16);
            if(cellData.length()>maxlength) maxlength=cellData.length();
            textView.setPadding(8, 8, 8, 8);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.rectangle_box); // Add your background drawable here

            // Add TextView to the list
            if(p!=0) {
                textViews.add(textView);
                rowLayout.addView(textView);
            }

            // Measure the height of the TextView
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int height = textView.getMeasuredHeight();
            if (height > maxHeight) {
                maxHeight = height;
            }

            p=1;
        }

        // Set maximum height to all TextViews in the row
        int kk=0;

        for (TextView textView : textViews) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            if(kk==1){
                params.width=dpToPx(110);
            }
            params.height = maxHeight+(((maxlength/16)+1)*60);
            textView.setLayoutParams(params);
            kk=1;
        }

        // Add the row to the table content layout
        tableContent.addView(rowLayout);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    public static String[][] convertToArray(List<List> data) {
        String[][] arr=new String[data.size()][4];


        for(int i=0;i<data.size();i++){
            arr[i][3]=data.get(i).get(data.get(i).size()-2).toString();  //date
            arr[i][2]=data.get(i).get(data.get(i).size()-4).toString();   //price
            String str="";
            for(int k=0;k<data.get(i).size()-5;k++){
                if(!data.get(i).get(k).toString().trim().equals("+")){
                    str+=data.get(i).get(k).toString().trim()+" ";
                }
            }
            arr[i][1]=str.trim();
            arr[i][0]=String.valueOf(i+1);
        }

        return arr;
    }
}

package hisaab.store.analyser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisaabcalculator.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class display extends AppCompatActivity {
TextView e1;
Button b1;
int p=0;
SharedPreferences sp,spitem;
AdView adView;
HorizontalScrollView hview;
String mon,year,head;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.guide){

            Intent gi=new Intent(this,guide.class);
            startActivity(gi);
        }
        else if(item.getItemId()==R.id.contact){
            Intent gi=new Intent(this,about.class);
            startActivity(gi);
        }
        else if(item.getItemId()==R.id.logout_menu){
        open();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        try {

            hview=findViewById(R.id.presshowhorizon);

            Spinner s = findViewById(R.id.spin2);

            sp = getSharedPreferences("login", MODE_PRIVATE);
            spitem = getSharedPreferences("item", MODE_PRIVATE);
            String[] arr={"Jan","Feb","Mar","Apr","May","June","July","Aug","Sept","Oct","Nov","Dec"};
            SpinnerAdapter1 adapter = new SpinnerAdapter1(display.this,R.layout.spinner_login,arr);
            s.setAdapter(adapter);
            Bundle e = getIntent().getExtras();
            head = spitem.getString("user", "");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primary));
            }
            Spinner s2 = findViewById(R.id.spin3);
            String[] arr2={"2024","2025","2026","2027","2028","2029","2030","2031","2034","2035","2036","2037","2038","2039","2040","2041","2042","2043","2044","2045","2046","2047","2048","2049","2050"};
            SpinnerAdapter1 adapter2 = new SpinnerAdapter1(display.this,R.layout.spinner_login,arr2);
            s2.setAdapter(adapter2);

            e1 = findViewById(R.id.de);
            e1.setVisibility(View.GONE);
            b1 = findViewById(R.id.db);

            b1.setOnClickListener(view -> {
                if (show_data() == 1) {
                    b1.setText("Showed");
                } else {
                    e1.setText(" ");
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }

            });
            s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    year = parent.getItemAtPosition(i).toString();
                    b1.setText("Show");
                    hview.setVisibility(View.GONE);
                    //e1.setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    mon = parent.getItemAtPosition(i).toString();
                    b1.setText("Show");
                    //e1.setText("");
                    hview.setVisibility(View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

        }catch (Exception e){

        }
        adView=findViewById(R.id.adViewdisplay);
       // adView.setAdUnitId("ca-app-pub-1079506490540577/7402312139");
        //adView.setAdSize(getAdSize());
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

    }



    public int show_data(){
        hview.setVisibility(View.GONE);
        LinearLayout tableContent=findViewById(R.id.tableContent);
        tableContent.removeAllViews();
            File path = getApplicationContext().getFilesDir();
            int y=0;

        List<List> meddata=new ArrayList<>();

            try {
                FileInputStream f1 = new FileInputStream(new File(path,head+mon+year+".txt"));
                InputStreamReader r = new InputStreamReader(f1);
                BufferedReader br = new BufferedReader(r);
                StringBuilder sb = new StringBuilder();
                String txt;
                while ((txt = br.readLine()) != null) {

                    List<String> arr= Arrays.asList(txt.split(" "));

                    List<String> newarr=new ArrayList<>();

                    for(int i=0;i<arr.size();i++){
                        if(!arr.get(i).trim().equals("")) {
                            newarr.add(arr.get(i));
                            newarr.add("+");
                        }


                    }

                    meddata.add(newarr);
                    System.out.println("kkl "+meddata.toString());
                    sb.append(txt).append("\n");
                }

                if(sb.toString().equals("")) {
                  //  e1.setText(" ");

                    y = 1;
                }
                else {

                  // e1.setText(sb.toString());
                    String[][] arr2=convertToArray(meddata);
                    for (String[] row : arr2) {
                        addRow(tableContent,row);
                    }
                }
                f1.close();
            } catch(FileNotFoundException ee) {
                ee.printStackTrace();
            } catch(IOException ioException)
            {
                ioException.printStackTrace();
                Toast.makeText(this, "io exception", Toast.LENGTH_LONG).show();
            }
            if(y==0) return 1;
            else return 0;
        }

    public void open(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
        a.setMessage("Do you want to logout?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gi=new Intent(display.this,MainActivity.class);
                sp.edit().putBoolean("islogged",false).apply();
                startActivity(gi);
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



    private void addRow(LinearLayout tableContent, String[] rowData) {
        hview.setVisibility(View.VISIBLE);
        LinearLayout rowLayout = new LinearLayout(this);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT));
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);

        List<TextView> textViews = new ArrayList<>(); // List to hold TextViews in the row

        // Find maximum height of TextViews and adjust width of columns
        int maxHeight = 0;
        p=0;
        int maxlength=0;
        for (String cellData : rowData) {

            TextView textView = new TextView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    dpToPx(110), // Set width to 0 initially
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            params.gravity=Gravity.CENTER;
            params.setMargins(0,0,0,0);
            textView.setLayoutParams(params);
            textView.setText(cellData);
            textView.setTextSize(16);
            if(cellData.length()>maxlength) maxlength=cellData.length();
            textView.setPadding(8, 8, 8, 8);
            textView.setTextColor(Color.WHITE);
            textView.setGravity(Gravity.CENTER);
            textView.setBackgroundResource(R.drawable.rectangle_box); // Add your background drawable here

            // Add TextView to the list
            if(p!=0) {
                textViews.add(textView);
                rowLayout.addView(textView);
            }

            // Measure the height of the TextView
            textView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            int height = textView.getMeasuredHeight();
            if (height > maxHeight) {
                maxHeight = height;
            }

            p=1;
        }

        // Set maximum height to all TextViews in the row
        int kk=0;

        for (TextView textView : textViews) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) textView.getLayoutParams();
            if(kk==1){
                params.width=dpToPx(110);
            }
            params.height = maxHeight+(((maxlength/16)+1)*60);
            textView.setLayoutParams(params);
            kk=1;
        }

        // Add the row to the table content layout
        tableContent.addView(rowLayout);
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
    public static String[][] convertToArray(List<List> data) {
        String[][] arr=new String[data.size()][4];
        for(int i=0;i<data.size();i++){
            arr[i][3]=data.get(i).get(data.get(i).size()-2).toString();  //date
            System.out.println("jjk"+data.get(i).get(2).toString()+"  --  "+" 0000 "+data.size()+"0000"+data.get(i).toString());
            arr[i][2]=data.get(i).get(data.get(i).size()-4).toString();   //price

            String str="";
            for(int k=0;k<data.get(i).size()-5;k++){
                if(!data.get(i).get(k).toString().trim().equals("+")){
                    str+=data.get(i).get(k).toString().trim()+" ";
                }
            }
            arr[i][1]=str.trim();
            arr[i][0]=String.valueOf(i+1);
        }

        return arr;
    }
}
