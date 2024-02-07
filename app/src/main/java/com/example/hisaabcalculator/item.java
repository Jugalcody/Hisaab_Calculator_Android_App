package com.example.hisaabcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import java.time.LocalDate;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;


import java.util.Date;
import java.util.Locale;

public class item extends AppCompatActivity {
AppCompatButton b1;
EditText e1,e2;
SharedPreferences sp,splogin;

String mon,year,head;
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
        setContentView(R.layout.activity_item);

        t=findViewById(R.id.ab);
        sp=getSharedPreferences("item",MODE_PRIVATE);
        splogin=getSharedPreferences("login",MODE_PRIVATE);
        head=sp.getString("user","");

        t.setText("Available Balance : "+totalBalance());
        b1=findViewById(R.id.sb1);
        e1=findViewById(R.id.se1);
        e2=findViewById(R.id.se2);


        b1.setOnClickListener(view ->{
            String item=e1.getText().toString().trim();
            String price=e2.getText().toString().trim();
            LocalDate currentDate = null;

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                currentDate = LocalDate.now();
               mon =getMonth(currentDate.getMonthValue());
                year= String.valueOf(currentDate.getYear());
            }

            if(!price.trim().equals("")) {
                if (!item.trim().equals("")) {
                    if (isOk(Integer.parseInt(price)) == 1) {


                        Intent i=new Intent(item.this,ScanQR.class);
                        sp.edit().putString("price",price).apply();
                        sp.edit().putString("items",item).apply();
                        sp.edit().putString("mon",mon).apply();
                        sp.edit().putString("year",year).apply();

                        e1.setText("");
                        e2.setText("");
                        startActivity(i);

                    } else {
                        Toast.makeText(this, "insufficient balance", Toast.LENGTH_LONG).show();
                    }
                }
            else if(item.equals("")){
                    Toast.makeText(this, "enter item names", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(getApplicationContext(), "empty price", Toast.LENGTH_SHORT).show();
            }
            });
    }

    public int isOk(int p){
        File path=getApplicationContext().getFilesDir();
        int bal3 = 0;
        try{
            FileInputStream f=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r=new InputStreamReader(f);
            BufferedReader br=new BufferedReader(r);
            String bal=br.readLine();
            if (bal==null) bal="0";
            bal3=Integer.parseInt(bal);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        if(bal3>=p) return 1;
        else return 0;
    }

    public int deductMoney(int p){
        File path=getApplicationContext().getFilesDir();
        int bal3=0;
        try{
            FileInputStream f=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r=new InputStreamReader(f);
            BufferedReader br=new BufferedReader(r);
            String bal=br.readLine();
            if (bal==null) bal="0";
            bal3=Integer.parseInt(bal)-p;
            f.close();
            FileOutputStream f2=new FileOutputStream(new File(path,head+"balance.txt"));
            f2.write(Integer.toString(bal3).getBytes());
            f2.close();

        }
        catch(IOException e){
            clear3();
            e.printStackTrace();
        }
return bal3;
    }

    public void monthUpdate(int p) {
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


            int l = Integer.parseInt(b5) + p;
            String b3 = Integer.toString(l);
            FileOutputStream f6 = new FileOutputStream(new File(path, head+d+ "monthlySpend.txt"));
            f6.write((b3).getBytes());
            f6.close();
        }catch(IOException e){
            clear3();
            String price=e2.getText().toString();
            monthUpdate(Integer.parseInt(price));
            e.printStackTrace();
        }
    }
    public void clear3() {
        String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
        int d = Integer.parseInt(date);
        File path=getApplicationContext().getFilesDir();
        try{
            FileOutputStream f=new FileOutputStream(new File(path,head+d+"monthlySpend.txt"));
            f.write("".getBytes());

        }
        catch(IOException e){
            Toast.makeText(this,"unable to clear",Toast.LENGTH_LONG).show();
        }
    }

    public void add_data(String item,String price){

        if (!item.equals("") && !price.equals("")){
            File path = getApplicationContext().getFilesDir();
            String date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String k =String.format("%-16s",item)+" Rs."+String.format("%-10s",price)+" "+date+"\n";


            try {
                FileOutputStream f =new FileOutputStream(new File(path,head+mon+year+".txt"),true);
                    f.write(k.getBytes());
                f.close();
            } catch(FileNotFoundException ee) {
                ee.printStackTrace();
            } catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
        }}


    public String totalBalance() {
        File path=getApplicationContext().getFilesDir();
        String b3="0";
        try{
            FileInputStream f2=new FileInputStream(new File(path,sp.getString("user","")+"balance.txt"));
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
    public void open(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
        a.setMessage("Do you want to logout?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gi=new Intent(item.this,MainActivity.class);
                splogin.edit().putBoolean("islogged",false).apply();
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

    public String getMonth(int mon){
        if(mon==1) return "Jan";
        else if(mon==2) return "Feb";
        else if(mon==3) return "Mar";
        else if(mon==4) return "Apr";
        else if(mon==5) return "May";
        else if(mon==6) return "June";
        else if(mon==7) return "July";
        else if(mon==8) return "Aug";
        else if(mon==9) return "Sept";
        else if(mon==10) return "Oct";
        else if(mon==11) return "Nov";
        else if(mon==12) return "Dec";
        else return "";

    }

    @Override
    public void onBackPressed() {
        Intent i=new Intent(item.this,first.class);
        startActivity(i);
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        t.setText("Available Balance : "+totalBalance());
        super.onResume();
    }

    @Override
    protected void onRestart() {
        t.setText("Available Balance : "+totalBalance());
        super.onRestart();
    }
}
