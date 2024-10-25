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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);

        try {
            Bundle bb = getIntent().getExtras();
            TextView itempoint=findViewById(R.id.money_point);

            b = findViewById(R.id.mb);
            ButtonEffect buttonEffect=new ButtonEffect(money.this);
            buttonEffect.buttonEffect(b);
            e = findViewById(R.id.me);
            t = findViewById(R.id.mt);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            sp2 = getSharedPreferences("item", MODE_PRIVATE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            n = sp2.getString("user", "");
            SharedPreferences coin=getSharedPreferences(n+"coin",MODE_PRIVATE);
            int curpoint=coin.getInt("point",0);
            itempoint.setText(curpoint+"P");
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
            changetheme();
            try {
                File f = new File(path, n + "balance.txt");
                if (!f.exists()) f.createNewFile();
                String date2 = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
                int d2 = Integer.parseInt(date2);
                File f2 = new File(path, (n+"monthlyGet"+ d2 + ".txt"));
                if (!f2.exists()) f2.createNewFile();
                String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
                int d3 = Integer.parseInt(date3);
                File f3 = new File(path, (n+"yearlyGet"+d3+ ".txt"));
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
                 /*           int point=curpoint-2;
                            if(point<0) point=0;
                            coin.edit().putInt("point",point).apply();
                            itempoint.setText(point+"P");
                            Toast.makeText(money.this,"-2",Toast.LENGTH_SHORT).show();*/
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
            FileOutputStream f=new FileOutputStream(new File(path,n+"monthlyGet"+d+".txt"));
            f.write("".getBytes());

        }
        catch(IOException e){
            Toast.makeText(this,"unable to clear",Toast.LENGTH_LONG).show();
        }
    }

    public String totalBalance() {
        String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        int year = Integer.parseInt(date3);
        SharedPreferences sharedPreferences=getSharedPreferences(n+year,MODE_PRIVATE);
        File path=getApplicationContext().getFilesDir();
        String b3="0";
        try{
            File file=new File(path,n+"balance.txt");
            if(!file.exists()) file.createNewFile();
            FileInputStream f2=new FileInputStream(file);
            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
                b3 = br.readLine();
                if(b3==null) b3="0";
                f2.close();


        }
        catch(IOException e){

            Toast.makeText(this,"empty balance",Toast.LENGTH_LONG).show();
        }
        sharedPreferences.edit().putString("balance",b3).apply();
        return b3;
    }
    public String updateBalance(String e1){
        String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        int year = Integer.parseInt(date3);
        SharedPreferences sharedPreferences=getSharedPreferences(n+year,MODE_PRIVATE);
        File path=getApplicationContext().getFilesDir();
        String b3="0",b4;
        long l;
        try{
File file=new File(path,n+"balance.txt");
if(!file.exists()) file.createNewFile();
            FileInputStream f2=new FileInputStream(file);

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


        sharedPreferences.edit().putString("balance",b3).apply();
        return b3;
    }

    public void monthlyUpdate(String e1,int rem){
        File path=getApplicationContext().getFilesDir();
        String b3,b4;
        long l;
        try{
            String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            String date2 = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
            if(date2.charAt(0)=='0') date2=String.valueOf(date2.charAt(1));
            File file=new File(path,n+"monthlyGet"+date2+date+".txt");
            if(!file.exists()) file.createNewFile();

            FileInputStream f2=new FileInputStream(file);

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
            FileOutputStream f=new FileOutputStream(file);
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
            File file=new File(path,n+"yearlyGet"+d3+".txt");
            if(!file.exists()){
                try {

                    if (file.createNewFile()) {
                    }
                }catch (Exception e){}
            }
            FileInputStream f2=new FileInputStream(file);



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
            FileOutputStream f=new FileOutputStream(file);
            f.write((b3).getBytes());
            f.close();


        }
        catch(IOException ee){
            Toast.makeText(this,ee.toString(),Toast.LENGTH_LONG).show();
            clear3();
        }
    }



    private void changetheme(){
        SharedPreferences sp=getSharedPreferences("theme",MODE_PRIVATE);
        LinearLayout layout=findViewById(R.id.money_container);
        RelativeLayout toolbar=findViewById(R.id.money_toolbar);
        if(sp.getString("theme","dark").equals("pink")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.toolbarpink));
            }
            e.setHintTextColor(getColor(R.color.primarypink));
            b.setBackgroundResource(R.drawable.loginbutpink);
            toolbar.setBackgroundColor(getColor(R.color.toolbarpink));
            layout.setBackgroundColor(getColor(R.color.backgroundpink));




        }

        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            toolbar.setBackgroundColor(getColor(R.color.toolbardark));
            layout.setBackgroundColor(getColor(R.color.backgrounddark));
            b.setBackgroundResource(R.drawable.loginbut);

        }

    }


}