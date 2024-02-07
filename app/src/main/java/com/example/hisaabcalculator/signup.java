package com.example.hisaabcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class signup extends AppCompatActivity {
Button s;
EditText u,p;

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
        setContentView(R.layout.activity_signup);

        s=findViewById(R.id.signup);
        u = (EditText) findViewById(R.id.username2);
        p = (EditText) findViewById(R.id.password2);
        s.setOnClickListener(view -> {
            String user = u.getText().toString();
            String pass = p.getText().toString();

            register(user,pass);
        });

    }

    public void register(String u, String p) {
        if (!u.equals("") && !p.equals("")){
        File path = getApplicationContext().getFilesDir();
        String k = u + " " + p+"\n";


        try {
            FileOutputStream f =new FileOutputStream(new File(path,"valid2.txt"),true);

                FileInputStream f1 = new FileInputStream(new File(path,"valid2.txt"));
                InputStreamReader r = new InputStreamReader(f1);
                BufferedReader br = new BufferedReader(r);
                String txt;
                int ae=0;
                while ((txt = br.readLine()) != null) {
                    String[] arr=txt.split(" ");
                    if(arr[0].equals(u)){
                        ae=1;
                        break;
                    }
                }
                if(ae!=1) {
                    f.write(k.getBytes());
                    s.setText("Registered");

                }
                else{
                    Toast.makeText(this, "user already registered", Toast.LENGTH_LONG).show();
                }
                f1.close();
                f.close();
                //File p=getApplicationContext().getFilesDir();

            //}


        } catch(FileNotFoundException ee) {
            ee.printStackTrace();
        } catch(IOException ioException)
        {
            ioException.printStackTrace();
            Toast.makeText(this, "io exception", Toast.LENGTH_LONG).show();
        }
            }}}