package hisaab.store.analyser;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class signup extends AppCompatActivity {
Button s;
EditText ph, password,user;

TextView dob;
ImageView back;
ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            s = findViewById(R.id.signup);
            progressBar=findViewById(R.id.registerprogress);
            dob=findViewById(R.id.registerdob);
            back=findViewById(R.id.register_back);
            user=findViewById(R.id.registerusername);
            ph =findViewById(R.id.registerphone2);
            password = findViewById(R.id.password2);
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            ButtonEffect buttonEffect=new ButtonEffect(signup.this);
            buttonEffect.buttonEffect(s);
            changetheme(s);
            checkOrientation();
            s.setOnClickListener(view -> {
                String phonen = ph.getText().toString().trim();
                String pass = password.getText().toString().trim();
                if(!phonen.equals("") && !pass.equals("") && !dob.getText().toString().trim().equals("") && !user.getText().toString().trim().equals("")) {
                    if(phonen.length()==10) {
                        progressBar.setVisibility(View.VISIBLE);
                        addUserToFirestore(phonen, pass, user.getText().toString().trim(), dob.getText().toString().trim());
                    }
                    else{
                        Toast.makeText(signup.this,"invalid phone!",Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(signup.this,"empty field!",Toast.LENGTH_SHORT).show();
                }
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

        datePickerDialog.show();
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Apply custom colors only in Dark Mode
            datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(signup.this, R.color.white));
            datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(signup.this, R.color.white));
        }
        else{
            datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(signup.this, R.color.black));
            datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(signup.this, R.color.black));
    }
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
                    String k = u + "_" + p + "_" + name + "_" + dob  + "_"+fullname+"_"+date+"_"+ serial +" \n";

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
                            progressBar.setVisibility(View.GONE);
                            coin.edit().putInt("point", 20).apply();
                            Toast.makeText(this, "Registered Successfully!", Toast.LENGTH_SHORT).show();
                            ph.setText("");
                            user.setText("");
                            password.setText("");
                            onBackPressed();

                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(this, "user already registered", Toast.LENGTH_SHORT).show();
                        }


                        f1.close();
                        f.close();
                        //File p=getApplicationContext().getFilesDir();

                        //}


                    } catch (FileNotFoundException ee) {
                        progressBar.setVisibility(View.GONE);
                        ee.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(this, ioException.toString(), Toast.LENGTH_LONG).show();
                    }


        }
    else{
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Empty field!", Toast.LENGTH_SHORT).show();
        }
    }

    public void addUserToFirestore(String phone, String pass, String name, String dob) {
        try {
            if(isInternetAvailable()) {
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String currentDate = sdf.format(new Date());

                // Reference to the user document
                DocumentReference userDocRef = db.collection("HisaabAnalyser")
                        .document("users")  // Parent document
                        .collection("userList")  // Collection inside "users"
                        .document(phone); // Phone number as document ID

                userDocRef.get().addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(signup.this, "User already exists!", Toast.LENGTH_LONG).show();
                    } else {
                        // If user does not exist, proceed with registration
                        Map<String, Object> userData = new HashMap<>();
                        userData.put("name", name);
                        userData.put("phone", phone);
                        userData.put("password", pass);
                        userData.put("lastLoggedOutDate", "new");
                        userData.put("dob", dob);
                        userData.put("currentDate", currentDate);

                        userDocRef.set(userData)
                                .addOnSuccessListener(avoid -> {
                                    register(phone, pass, name, dob);
                                })
                                .addOnFailureListener(e -> {
                                    progressBar.setVisibility(View.GONE);
                                    System.out.println("Error adding user data: " + e.getMessage());
                                });
                    }
                }).addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
//                    Toast.makeText(signup.this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    register(phone, pass, name, dob);
                });
            }
            else{
                //Toast.makeText(signup.this, "Check your internet connection!", Toast.LENGTH_LONG).show();
                progressBar.setVisibility(View.GONE);
                register(phone, pass, name, dob);
            }
        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(signup.this, e.toString(), Toast.LENGTH_LONG).show();
        }
    }

    private void checkOrientation() {
        AdjustSizeConfiguration displaysize = new AdjustSizeConfiguration(signup.this);
        Configuration config = getResources().getConfiguration();;
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(user, 55);
            displaysize.setfixWidth(password, 55);
            displaysize.setfixWidth(s, 55);
            displaysize.setfixWidth(ph, 55);
            displaysize.setfixWidth(dob, 55);

        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            displaysize.setfixWidth(user, 85);
            displaysize.setfixWidth(password, 85);
            displaysize.setfixWidth(s, 85);
            displaysize.setfixWidth(ph, 85);
            displaysize.setfixWidth(dob, 85);

//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }
    private boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm != null) {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return capabilities != null &&
                    (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR));
        }
        return false;
    }


    private void changetheme(Button l) {
        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        if (sp.getString("theme", "dark").equals("pink")) {
            l.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.primarylight)
            );

        } else {

            l.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.primarydark)
            );


        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            l.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
        } else {
            l.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        }
    }

}