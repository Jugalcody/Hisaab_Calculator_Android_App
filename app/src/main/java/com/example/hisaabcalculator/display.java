package com.example.hisaabcalculator;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
public class display extends AppCompatActivity {
TextView e1;
Button b1;
SharedPreferences sp,spitem;
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
        Spinner s=findViewById(R.id.spin2);
        sp=getSharedPreferences("login",MODE_PRIVATE);
        spitem=getSharedPreferences("item",MODE_PRIVATE);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this,R.array.month, android.R.layout.simple_spinner_item);
        adapter .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s.setAdapter(adapter);
        Bundle e=getIntent().getExtras();
        head=spitem.getString("user","");


        Spinner s2=findViewById(R.id.spin3);
        ArrayAdapter<CharSequence> adapter2=ArrayAdapter.createFromResource(this,R.array.year, android.R.layout.simple_spinner_item);
        adapter2 .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        s2.setAdapter(adapter2);

        e1=findViewById(R.id.de);
        b1=findViewById(R.id.db);

        b1.setOnClickListener(view -> {
            if(show_data()==1) {b1.setText("Showed");}
            else {
                e1.setText(" ");
                Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
            }

        });
s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        year=parent.getItemAtPosition(i).toString();
        b1.setText("Show");
        e1.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});
s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
        mon=parent.getItemAtPosition(i).toString();
        b1.setText("Show");
        e1.setText("");
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
});
    }



    public int show_data(){
            File path = getApplicationContext().getFilesDir();
            int y=0;
            try {
                FileInputStream f1 = new FileInputStream(new File(path,head+mon+year+".txt"));
                InputStreamReader r = new InputStreamReader(f1);
                BufferedReader br = new BufferedReader(r);
                StringBuilder sb = new StringBuilder();
                String txt;
                while ((txt = br.readLine()) != null) {
                    sb.append(txt).append("\n");
                }

                if(sb.toString().equals("")) {
                    e1.setText(" ");
                    y = 1;
                }
                else {e1.setText(sb.toString());}
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
}
