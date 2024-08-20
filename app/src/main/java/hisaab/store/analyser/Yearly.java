package hisaab.store.analyser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import java.io.IOException;
import java.io.InputStreamReader;

public class Yearly extends AppCompatActivity {


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


    TextView t1,t2,t3;
    SharedPreferences sp,spitem;
    ImageView back;
    AdView adView;
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
                });
            }catch(Exception e){

            }




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

    }
