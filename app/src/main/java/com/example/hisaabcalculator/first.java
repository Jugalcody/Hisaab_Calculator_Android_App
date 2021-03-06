package com.example.hisaabcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class first extends AppCompatActivity {
TextView t1,t2,t3,t4;
    String n;
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
        else if(item.getItemId()==R.id.about){
            Intent gi=new Intent(this,about.class);
            startActivity(gi);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        t1=findViewById(R.id.fb1);
        t2=findViewById(R.id.fb2);
        t3=findViewById(R.id.fb3);
        t4=findViewById(R.id.fb4);
        Bundle extras=getIntent().getExtras();
        n=extras.getString("head");
        t1.setOnClickListener(view -> {
           Intent i1=new Intent(this,item.class);
           i1.putExtra("head",n);
           startActivity(i1);
        });

        t2.setOnClickListener(view -> {
            Intent i2=new Intent(this,display.class);
            i2.putExtra("head",n);
            startActivity(i2);
        });

        t3.setOnClickListener(view -> {
            Intent i3=new Intent(this,monthly.class);
            i3.putExtra("head",n);
            startActivity(i3);
        });

        t4.setOnClickListener(view -> {
            Intent i4=new Intent(this,money.class);
            i4.putExtra("head",n);
            startActivity(i4);
        });
    }
}