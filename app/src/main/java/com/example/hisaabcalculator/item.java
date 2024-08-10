package com.example.hisaabcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
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
    Button b1;
    EditText e1,e2;
    String mon,year,head;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.primary));
        }
        try {
            Date now = new Date();


            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
            mon = monthFormat.format(now);


            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
            year = yearFormat.format(now);
            t = findViewById(R.id.ab);
            Bundle e = getIntent().getExtras();
            head = e.getString("head");

            t.setText("Available Balance : " + totalBalance());
            b1 = findViewById(R.id.sb1);
            e1 = findViewById(R.id.se1);
            e2 = findViewById(R.id.se2);
            /*s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    mon = parent.getItemAtPosition(i).toString();
                    b1.setText("Commit");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    year = parent.getItemAtPosition(i).toString();
                    b1.setText("Commit");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/
            b1.setOnClickListener(view -> {
                String item = e1.getText().toString();
                String price = e2.getText().toString();
                if (!mon.equals("")) {
                    if (!item.equals("") && !price.equals("")) {
                        if (isOk(Integer.parseInt(price)) == 1) {

                            add_data(item, price);
                            monthUpdate(Integer.parseInt(price));
                            t.setText("Available Balance : " + deductMoney(Integer.parseInt(price)));
                            e1.setText("");
                            e2.setText("");
                            b1.setText("Committed");
                        } else {
                            Toast.makeText(this, "insufficient balance", Toast.LENGTH_LONG).show();
                        }
                    } else if (item.equals("")) {
                        Toast.makeText(this, "enter item", Toast.LENGTH_LONG).show();
                    } else if (price.equals("")) {
                        Toast.makeText(this, "enter price", Toast.LENGTH_LONG).show();
                    }
                } else Toast.makeText(this, "select month", Toast.LENGTH_LONG).show();

            });
        }catch (Exception e){

        }
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
                b1.setText("committed");
                f.close();
            } catch(FileNotFoundException ee) {
                ee.printStackTrace();
            } catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
        }}


    public String totalBalance(){
        File path=getApplicationContext().getFilesDir();
        String b3="0";
        try{
            FileInputStream f=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r=new InputStreamReader(f);
            BufferedReader br=new BufferedReader(r);
            b3=br.readLine();
            if (b3==null) b3="0";

            f.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return b3;

    }
}