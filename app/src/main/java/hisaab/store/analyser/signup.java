package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class signup extends AppCompatActivity {
Button s;
EditText ph, password,user;

TextView dob;
ImageView back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            s = findViewById(R.id.signup);
            dob=findViewById(R.id.registerdob);
            back=findViewById(R.id.register_back);
            user=findViewById(R.id.registerusername);
            ph =findViewById(R.id.registerphone2);
            password = findViewById(R.id.password2);

            ButtonEffect buttonEffect=new ButtonEffect(signup.this);
            buttonEffect.buttonEffect(s);

            s.setOnClickListener(view -> {
                String phonen = ph.getText().toString().trim();
                String pass = password.getText().toString().trim();

                register(phonen, pass,user.getText().toString().trim(),dob.getText().toString().trim());
            });
        }
        catch(Exception e){

        }


        ph.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().contains(" ")) {
                    String updatedText = s.toString().replace(" ", "");

                    // Remove the TextWatcher temporarily to avoid recursion
                    ph.removeTextChangedListener(this);

                    // Update the EditText with the modified text
                    ph.setText(updatedText);

                    // Move the cursor to the end of the text
                    ph.setSelection(updatedText.length());

                    // Reattach the TextWatcher
                    ph.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().contains(" ") ) {
                    String updatedText = s.toString().replace(" ", "");

                    // Remove the TextWatcher temporarily to avoid recursion
                    password.removeTextChangedListener(this);

                    // Update the EditText with the modified text
                    password.setText(updatedText);

                    // Move the cursor to the end of the text
                    password.setSelection(updatedText.length());

                    // Reattach the TextWatcher
                    password.addTextChangedListener(this);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
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

    public void register(String u, String p,String name,String dob) {
        String fullname=name;
        SharedPreferences sp= getSharedPreferences("login", MODE_PRIVATE);
        String date3 = new SimpleDateFormat("ddMMyyyy", Locale.getDefault()).format(new Date());
        int date = Integer.parseInt(date3);

        int serial=sp.getInt("usercount",1);
        name=name.split(" ")[0];
        if (!u.equals("") && !p.equals("") && !name.equals("") && !dob.equals("")) {
                    File path = getApplicationContext().getFilesDir();
                    String k = u + "_" + p + "_" + name + "_" + dob  + "_"+fullname+"_"+date+String.valueOf(serial)+" \n";

                    try {
                        FileOutputStream f = new FileOutputStream(new File(path, "valid2.txt"), true);
                        FileInputStream f1 = new FileInputStream(new File(path, "valid2.txt"));
                        InputStreamReader r = new InputStreamReader(f1);
                        BufferedReader br = new BufferedReader(r);
                        String txt;
                        int ae = 0;
                        while ((txt = br.readLine()) != null) {
                            String[] arr = txt.split("_");
                            if (arr[0].equals(u)) {
                                ae = 1;
                                break;
                            }
                        }
                        if (ae != 1) {
                            f.write(k.getBytes());
                            s.setText("Registered");
                            sp.edit().putInt("usercount",serial+1).apply();
                            SharedPreferences coin = getSharedPreferences(u + "coin", MODE_PRIVATE);
                            coin.edit().putInt("point", 20).apply();
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
            Toast.makeText(this, "empty field!", Toast.LENGTH_LONG).show();
        }
    }}