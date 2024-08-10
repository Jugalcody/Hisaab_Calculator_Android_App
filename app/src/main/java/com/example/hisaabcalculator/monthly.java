package com.example.hisaabcalculator;

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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
public class monthly extends AppCompatActivity {
TextView t1,t2,t3;
SharedPreferences sp,spitem;
Button b;
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
        setContentView(R.layout.activity_monthly);
        try {
            Bundle e = getIntent().getExtras();
            b = findViewById(R.id.mondb);
            t1 = findViewById(R.id.monrec);
            t2 = findViewById(R.id.mons);
            t3 = findViewById(R.id.monav);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            spitem = getSharedPreferences("item", MODE_PRIVATE);
            head = spitem.getString("user", "");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primary));
            }
            t3.setText("Total money available : Rs." + totalBalance());
            Spinner s = findViewById(R.id.spinm2);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.month, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            s.setAdapter(adapter);

            Spinner s2 = findViewById(R.id.spinm3);
            ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.year, android.R.layout.simple_spinner_item);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
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
            s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    mon = parent.getItemAtPosition(i).toString();
                    b.setText("Show");
                    t1.setText("Total money received : ");
                    t2.setText("Total money spend : ");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });


            b.setOnClickListener(view -> {
                t1.setText("Total money received in " + mon + "(" + year + ") :  Rs." + monthlyGet());
                t2.setText("Total money spend in " + mon + "(" + year + ") :  Rs." + monthlySpend());
                b.setText("Showed");
            });
        }catch(Exception e){

        }
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
    public String monthlyGet(){
        File path=getApplicationContext().getFilesDir();
        String b4="0";
        int m;
        try{
             m=month(mon);
            FileInputStream f2=new FileInputStream(new File(path,head+m+year+"monthlyGet.txt"));

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
    public void open(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
        a.setMessage("Do you want to logout?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gi=new Intent(monthly.this,MainActivity.class);
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
