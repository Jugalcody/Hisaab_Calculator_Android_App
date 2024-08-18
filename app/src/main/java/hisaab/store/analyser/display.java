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
TextView e1,totalspend;
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
            totalspend=findViewById(R.id.display_total);
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
                    totalspend.setVisibility(View.GONE);
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
