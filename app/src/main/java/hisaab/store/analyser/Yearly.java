package hisaab.store.analyser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Yearly extends AppCompatActivity {

    TextView t1,t2,t3;
    SharedPreferences sp,spitem;
    ImageView back;
    AdView adView;
    AppCompatButton reset;
    Button b;
    String mon,year,head;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_yearly);
            try {
                Bundle e = getIntent().getExtras();
                b = findViewById(R.id.mondb);
                t1 = findViewById(R.id.monrec);
                t2 = findViewById(R.id.mons);
                t3 = findViewById(R.id.monav);
                reset=findViewById(R.id.yearly_reset);
                sp = getSharedPreferences("login", MODE_PRIVATE);
                spitem = getSharedPreferences("item", MODE_PRIVATE);
                head = spitem.getString("user", "");

                back=findViewById(R.id.yearly_back);
                back.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
                });
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    getWindow().setStatusBarColor(getColor(R.color.primary));
                }
                t3.setText("Total money available : Rs." + totalBalance());
                Spinner s2 = findViewById(R.id.spinm3);
                String[] arr2={"2024","2025","2026","2027","2028","2029","2030","2031","2034","2035","2036","2037","2038","2039","2040","2041","2042","2043","2044","2045","2046","2047","2048","2049","2050"};
                SpinnerAdapter1 adapter2 = new SpinnerAdapter1(Yearly.this,R.layout.spinner_login,arr2);
                s2.setAdapter(adapter2);

                s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                        year = parent.getItemAtPosition(i).toString();
                        b.setText("Show");
                        t1.setText("Total money received : ");
                        t2.setText("Total money spend : ");
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

                    t1.setText("Total money received in "+ year + " :  Rs." + yearlyGet());
                    t2.setText("Total money spend in "+ year +" :  Rs." + yearlySpend());
                    b.setText("Showed");
                    reset.setVisibility(View.VISIBLE);
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
            //adView.setAdUnitId("ca-app-pub-1079506490540577/7402312139");
            //adView.setAdSize(getAdSize());
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
        public String yearlyGet(){
            File path=getApplicationContext().getFilesDir();
            String b4="0";

            try{
                FileInputStream f2=new FileInputStream(new File(path,head+year+"yearlyGet.txt"));

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

        public String yearlySpend(){
            File path=getApplicationContext().getFilesDir();
            String b4="0";
            try{
                FileInputStream f2=new FileInputStream(new File(path,head+year+"yearlySpend.txt"));

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
        public void open(){
            AlertDialog.Builder a=new AlertDialog.Builder(this);
            a.setMessage("Do you want to logout?");
            a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent gi=new Intent(Yearly.this,MainActivity.class);
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

                    f2=new FileOutputStream(new File(path,head+year+"yearlyGet.txt"));
                    f2.write("".getBytes());
                    f2.close();
                    f2=new FileOutputStream(new File(path,head+year+"yearlySpend.txt"));
                    f2.write(("").getBytes());
                    f2.close();


                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
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
    f4 = new FileInputStream(new File(path, head + month(arr[(k)]) + year + "monthlySpend.txt"));
    InputStreamReader rr = new InputStreamReader(f4);
    BufferedReader brr = new BufferedReader(rr);

    String initalspend = brr.readLine();
    if (initalspend == null) initalspend = "0";
    tspend = Long.parseLong(initalspend);
    f4.close();


    f4 = new FileInputStream(new File(path, head +  month(arr[(k)])+ year + "monthlyGet.txt"));
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
                            f6 = new FileOutputStream(new File(path, head + (k + 1) + year + "monthlySpend.txt"));
                            f6.write("".getBytes());
                            f6.close();
                        } catch (Exception e) {
                        }
                        try {
                            f6 = new FileOutputStream(new File(path, head + (k + 1) + year + "monthlyGet.txt"));
                            f6.write("".getBytes());
                            f6.close();
                        } catch (Exception e) {
                        }
                        try {
                            f2 = new FileOutputStream(new File(path, head + arr[k] + year + ".txt"));
                            f2.write(("").getBytes());
                            f2.close();
                        } catch (Exception e) {
                        }
                    }
                }catch (Exception e){

                }
                t1.setText("Total money received in "+ year + " :  Rs." + yearlyGet());
                t2.setText("Total money spend in "+ year +" :  Rs." + yearlySpend());

               t3.setText("Total money available : Rs." + String.valueOf(deductMoney(tremain)));
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

}
