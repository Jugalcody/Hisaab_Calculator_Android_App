package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisaabcalculator.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class signup extends AppCompatActivity {
Button s;
EditText ph, password,user;
TextView dob;
ImageView back;
EditText email;

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
                getWindow().setStatusBarColor(getColor(R.color.primary));
            }
            s = findViewById(R.id.signup);
            dob=findViewById(R.id.registerdob);
            back=findViewById(R.id.register_back);
            user=findViewById(R.id.registerusername);
            ph =findViewById(R.id.registerphone2);
            email=findViewById(R.id.registeremail);
            password = findViewById(R.id.password2);
            s.setOnClickListener(view -> {
                String phonen = ph.getText().toString();
                String pass = password.getText().toString();

                register(phonen, pass,user.getText().toString(),dob.getText().toString().trim(),email.getText().toString().trim());
            });
        }
        catch(Exception e){

        }
dob.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        showDatePicker();
    }
});

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showDatePicker() {
        // Get current date
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        // Create DatePickerDialog
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
                        // Set the selected date to the EditText
                        dob.setText(String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear));
                    }
                },
                year,
                month,
                day
        );

        // Show the DatePickerDialog
        datePickerDialog.show();
    }

    public void register(String u, String p,String name,String dob,String email) {
        if (!u.equals("") && !p.equals("") && !name.equals("") && !dob.equals("") && !email.equals("")) {
            if (u.length()>=7) {
                if(email.contains("@gmail.com")) {
                    File path = getApplicationContext().getFilesDir();
                    String k = u + " " + p + " " + name + " " + dob + " " + email + "\n";


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
                            SharedPreferences spitem = getSharedPreferences("allaccount", MODE_PRIVATE);
                            spitem.edit().putString("user", u).apply();
                            spitem.edit().putString("name", name).apply();
                            spitem.edit().putString("dob", dob).apply();
                            spitem.edit().putString("email", email).apply();
                            f.write(k.getBytes());

                            s.setText("Registered");
                            SharedPreferences coin = getSharedPreferences(u + "coin", MODE_PRIVATE);
                            coin.edit().putInt("point", 10).apply();
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
                }
                else{
                    Toast.makeText(this, "invalid email id", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(this, "invalid phone number", Toast.LENGTH_LONG).show();
            }
        }
    else{
            Toast.makeText(this, "empty field!", Toast.LENGTH_LONG).show();
        }
    }}