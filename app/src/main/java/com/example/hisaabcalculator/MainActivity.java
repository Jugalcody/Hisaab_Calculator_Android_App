package com.example.hisaabcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.*;
public class MainActivity extends AppCompatActivity {
    Button l;
    SharedPreferences sp,spitem;
    EditText u, p;

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
        setContentView(R.layout.activity_main);
        l = findViewById(R.id.login);
        u = findViewById(R.id.username);
        p = findViewById(R.id.password);
        sp=getSharedPreferences("login",MODE_PRIVATE);
        spitem=getSharedPreferences("item",MODE_PRIVATE);
        l.setOnClickListener(view -> {

            String user = u.getText().toString();
            String pass = p.getText().toString();
        if(!user.equals("") && !pass.equals("")){
            if (isAuthenticate(user, pass)) {
                Toast.makeText(this, "Welcome " + user, Toast.LENGTH_LONG).show();
                Intent p1=new Intent(this,first.class);
                spitem.edit().putString("user",user).apply();
                sp.edit().putBoolean("islogged",true).apply();
                startActivity(p1);

            }}
        else if(user.equals("")){
            Toast.makeText(this,"enter username", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
        }

        });


    }


    public boolean isAuthenticate(String u,String p) {
        int y = 0;
        int v=0;
      File path = getApplicationContext().getFilesDir();
        FileInputStream f3=null;
            try {
                f3=new FileInputStream(new File(path,"valid2.txt"));
                InputStreamReader r=new InputStreamReader(f3);
                BufferedReader br=new BufferedReader(r);


                String txt;
                while ((txt = br.readLine()) != null) {
                    String[] arr = txt.split(" ");

                    if (arr[0].equals(u)) {
                             v=1;
                        if (arr[1].equals(p)) {

                            y = 1;

                        }
                    }
                }


            } catch (IOException fileNotFoundException) {
                fileNotFoundException.printStackTrace();
            }
            finally{
                if(f3!=null) {
                    try {
                        f3.close();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }

            }

        if (v == 1 && y==1) {
            return true;
        } else if(v==1 && y==0){
            Toast.makeText(this, "wrong password", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            Toast.makeText(this, "user doesn't exist", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public void register(View view) {
        Intent i=new Intent(MainActivity.this,signup.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }
}