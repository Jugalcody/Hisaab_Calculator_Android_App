package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.hisaabcalculator.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class signup extends AppCompatActivity {
Button s;
EditText ph, password,user;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()== R.id.guide){

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
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.violet));
            }
            s = findViewById(R.id.signup);
            user=findViewById(R.id.registerusername);
            ph =findViewById(R.id.registerphone2);
            password = findViewById(R.id.password2);
            s.setOnClickListener(view -> {
                String phonen = ph.getText().toString();
                String pass = password.getText().toString();

                register(phonen, pass,user.getText().toString());
            });
        }
        catch(Exception e){

        }
    }

    public void register(String u, String p,String name) {
        if (!u.equals("") && !p.equals("") && !name.equals("")) {
            if (p.length()==10) {
                File path = getApplicationContext().getFilesDir();
                String k = u + " " + p + " " + name + "\n";


                try {
                    FileOutputStream f = new FileOutputStream(new File(path, "valid2.txt"), true);

                    FileInputStream f1 = new FileInputStream(new File(path, "valid2.txt"));
                    InputStreamReader r = new InputStreamReader(f1);
                    BufferedReader br = new BufferedReader(r);
                    String txt;
                    int ae = 0;
                    while ((txt = br.readLine()) != null) {
                        String[] arr = txt.split(" ");
                        if (arr[0].equals(u)) {
                            ae = 1;
                            break;
                        }
                    }
                    if (ae != 1) {
                        f.write(k.getBytes());
                        s.setText("Registered");
                        Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_LONG).show();
                        ph.setText("");
                        user.setText("");
                        password.setText("");
                        onBackPressed();

                    } else {
                        Toast.makeText(this, "user already registered", Toast.LENGTH_LONG).show();
                    }
                    f1.close();
                    f.close();
                    //File p=getApplicationContext().getFilesDir();

                    //}


                } catch (FileNotFoundException ee) {
                    ee.printStackTrace();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    Toast.makeText(this, ioException.toString(), Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "invalid phone number", Toast.LENGTH_LONG).show();
            }
        }
    else{
            Toast.makeText(this, "empty field!", Toast.LENGTH_LONG).show();
        }
    }}