package com.example.hisaabcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
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
Button b,b2;
EditText e;
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
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money);
        Bundle bb=getIntent().getExtras();
        n=bb.getString("head");
        b=findViewById(R.id.mb);
        e=findViewById(R.id.me);
        t=findViewById(R.id.mt);
        b2=findViewById(R.id.mb2);
        File path=getApplicationContext().getFilesDir();
       try{
           File f=new File(path,n+"balance.txt");
           if(!f.exists()) f.createNewFile();
           String date2 = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
           int d2 = Integer.parseInt(date2);
           File f2=new File(path,(n+d2+"monthlyGet.txt"));
           if(!f2.exists()) f2.createNewFile();
       }
       catch (Exception e){
           e.printStackTrace();
       }
        t.setText("Total balance : "+totalBalance());
        b.setOnClickListener(view -> {
            String e1=e.getText().toString();
            if(!e1.equals("")){
                    t.setText("Total balance : " + updateBalance(e1));
                    e.setText("");
                    monthlyUpdate(e1,0);
                }
            else{
              Toast.makeText(this,"nothing to add",Toast.LENGTH_LONG).show();
            }}
        );

        b2.setOnClickListener(this::open
        );

    }
public void open(View v){
    AlertDialog.Builder a=new AlertDialog.Builder(this);
    a.setMessage("Do you want to clear your balance?");
    a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {

            monthlyUpdate("0",Integer.parseInt(totalBalance()));
            clear2();
            t.setText("Total balance : 0");
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
        int l;
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

           if(!e1.equals("")){ l = Integer.parseInt(b4) + Integer.parseInt(e1);}
           else{
               l=Integer.parseInt(b4);
           }
            b3=Integer.toString(l);
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
            finish();
        }
        return b3;
    }

    public void monthlyUpdate(String e1,int rem){
        File path=getApplicationContext().getFilesDir();
        String b3,b4;
        int l;
        try{
            String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);
            FileInputStream f2=new FileInputStream(new File(path,n+d+"monthlyGet.txt"));

            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if(b4==null) b4="0";
            f2.close();

            if(!e1.equals("")){ l = Integer.parseInt(b4) + Integer.parseInt(e1)-rem;}
            else{
                l=Integer.parseInt(b4);
            }
            b3=Integer.toString(l);
            FileOutputStream f=new FileOutputStream(new File(path,n+d+"monthlyGet.txt"));
            f.write((b3).getBytes());
            f.close();


        }
        catch(IOException ee){
            clear3();
        }
    }


    }