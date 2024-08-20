package hisaab.store.analyser;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisaabcalculator.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class money extends AppCompatActivity {
String n;
Button b;
EditText e;
ImageView back;
AdView adView;
SharedPreferences sp,sp2;
TextView t;
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
        setContentView(R.layout.activity_money);

        try {
            Bundle bb = getIntent().getExtras();

            b = findViewById(R.id.mb);
            e = findViewById(R.id.me);
            t = findViewById(R.id.mt);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            sp2 = getSharedPreferences("item", MODE_PRIVATE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primary));
            }
            n = sp2.getString("user", "");
            File path = getApplicationContext().getFilesDir();
            back=findViewById(R.id.money_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
            adView=findViewById(R.id.adViewmoney);
           // adView.setAdUnitId("ca-app-pub-1079506490540577/7402312139");
            //adView.setAdSize(getAdSize());
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            try {
                File f = new File(path, n + "balance.txt");
                if (!f.exists()) f.createNewFile();
                String date2 = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
                int d2 = Integer.parseInt(date2);
                File f2 = new File(path, (n + d2 + "monthlyGet.txt"));
                if (!f2.exists()) f2.createNewFile();
                String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                int d3 = Integer.parseInt(date3);
                File f3 = new File(path, (n +d3+ "yearlyGet.txt"));
                if (!f3.exists()) f3.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
            t.setText("Total balance : " + totalBalance());
            b.setOnClickListener(view -> {
                        String e1 = e.getText().toString();
                        if (!e1.equals("")) {
                            t.setText("Total balance : " + updateBalance(e1));
                            e.setText("");
                            monthlyUpdate(e1, 0);
                            yearlyUpdate(e1,0);
                        } else {
                            Toast.makeText(this, "nothing to add", Toast.LENGTH_LONG).show();
                        }
                    }
            );

        }catch (Exception e){

        }

    }

    public void open2(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
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

        AlertDialog alerts=a.create();
        alerts.show();
    }
public void open(){
    AlertDialog.Builder a=new AlertDialog.Builder(this);
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

    AlertDialog alerts=a.create();
    alerts.show();
}
    public void clear2() {
        File path=getApplicationContext().getFilesDir();
        try{
            FileOutputStream f=new FileOutputStream(new File(path,n+"balance.txt"));
            f.write("".getBytes());
            t.setText("Total balance : 0");
        }
        catch(IOException e){
            Toast.makeText(this,"unable to clear",Toast.LENGTH_LONG).show();
        }
    }

    public void clear3() {
        File path=getApplicationContext().getFilesDir();
        try{
            String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);
            FileOutputStream f=new FileOutputStream(new File(path,n+d+"monthlyGet.txt"));
            f.write("".getBytes());

        }
        catch(IOException e){
            Toast.makeText(this,"unable to clear",Toast.LENGTH_LONG).show();
        }
    }

    public String totalBalance() {
        File path=getApplicationContext().getFilesDir();
        String b3="0";
        try{
            FileInputStream f2=new FileInputStream(new File(path,n+"balance.txt"));
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
    public String updateBalance(String e1){
        File path=getApplicationContext().getFilesDir();
        String b3="0",b4;
        long l;
        try{
            new File(path,n+"balance.txt");

        }catch (Exception e){
            e.printStackTrace();
        }
        try{

            FileInputStream f2=new FileInputStream(new File(path,n+"balance.txt"));

            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if(b4==null) b4="0";
            f2.close();

           if(!e1.equals("")){ l = Long.parseLong(b4) + Long.parseLong(e1);}
           else{
               l=Long.parseLong(b4);
           }
            b3=Long.toString(l);
            FileOutputStream f=new FileOutputStream(new File(path,n+"balance.txt"));
            f.write((b3).getBytes());
            f.close();


        }
        catch(IOException ee){
            clear2();
            String e0=e.getText().toString();
            t.setText("Total balance : " + updateBalance(e0));
            Toast.makeText(this,"Rs."+e0+" added",Toast.LENGTH_LONG).show();
            e.setText("");
            monthlyUpdate(e0,0);
            yearlyUpdate(e0,0);
            finish();
        }
        return b3;
    }

    public void monthlyUpdate(String e1,int rem){
        File path=getApplicationContext().getFilesDir();
        String b3,b4;
        long l;
        try{
            String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);
            FileInputStream f2=new FileInputStream(new File(path,n+d+"monthlyGet.txt"));

            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if(b4==null) b4="0";
            f2.close();

            if(!e1.equals("")){ l = Long.parseLong(b4) + Long.parseLong(e1)-rem;}
            else{
                l=Long.parseLong(b4);
            }
            b3=Long.toString(l);
            FileOutputStream f=new FileOutputStream(new File(path,n+d+"monthlyGet.txt"));
            f.write((b3).getBytes());
            f.close();


        }
        catch(IOException ee){
            clear3();
        }
    }


    public void yearlyUpdate(String e1,int rem){
        File path=getApplicationContext().getFilesDir();
        String b3,b4;
        long l;
        try{
            String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            int d3 = Integer.parseInt(date3);
            FileInputStream f2=new FileInputStream(new File(path,n+d3+"yearlyGet.txt"));
            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if(b4==null) b4="0";
            f2.close();

            if(!e1.equals("")){ l = Long.parseLong(b4) + Long.parseLong(e1)-rem;}
            else{
                l=Long.parseLong(b4);
            }
            b3=Long.toString(l);
            FileOutputStream f=new FileOutputStream(new File(path,n+d3+"yearlyGet.txt"));
            f.write((b3).getBytes());
            f.close();


        }
        catch(IOException ee){
            clear3();
        }
    }

    }