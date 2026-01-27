package hisaab.store.analyser;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.*;
import java.util.Calendar;

public class ForgotPage extends AppCompatActivity {

    EditText ph;
    TextView result, dob;
    ImageView back;
    String pass = "";
    AppCompatButton submit;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.primarydark));
        }

        ph = findViewById(R.id.username_forget);
        submit = findViewById(R.id.submit_button_forget);
        back = findViewById(R.id.back_forgot_);
        result = findViewById(R.id.result_forget);
        dob = findViewById(R.id.dob_forget);
        db = FirebaseFirestore.getInstance();

        ButtonEffect buttonEffect = new ButtonEffect(ForgotPage.this);
        buttonEffect.buttonEffect(submit);
        changetheme(submit);
        dob.setOnClickListener(v -> showDatePicker());
        back.setOnClickListener(v -> onBackPressed());
        getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        submit.setOnClickListener(v -> {
            String phone = ph.getText().toString().trim();
            String dob_ = dob.getText().toString().trim();

            if (!phone.isEmpty() && !dob_.isEmpty()) {
                if (!isTrue(phone, dob_)) {
                    checkFirebaseForPassword(phone, dob_);
                }
            } else {
                Toast.makeText(this, "Please enter both phone number and DOB", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) ->
                        dob.setText(String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear)),
                year, month, day
        );

        datePickerDialog.show();
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;

        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            // Apply custom colors only in Dark Mode
            datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(ForgotPage.this, R.color.white));
            datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(ForgotPage.this, R.color.white));
        }
        else{
            datePickerDialog.getButton(DialogInterface.BUTTON_POSITIVE)
                    .setTextColor(ContextCompat.getColor(ForgotPage.this, R.color.black));
            datePickerDialog.getButton(DialogInterface.BUTTON_NEGATIVE)
                    .setTextColor(ContextCompat.getColor(ForgotPage.this, R.color.black));
        }
    }

    public boolean isTrue(String ph_, String dob_) {
        boolean found = false;
        File path = getApplicationContext().getFilesDir();
        FileInputStream f3 = null;

        try {
            f3 = new FileInputStream(new File(path, "valid2.txt"));
            BufferedReader br = new BufferedReader(new InputStreamReader(f3));
            String txt;

            while ((txt = br.readLine()) != null) {
                String[] arr = txt.split("_");

                if (arr.length >= 4 && arr[0].equals(ph_)) {
                    if (arr[3].equals(dob_)) {
                        pass = arr[1];
                        showPassword(pass);
                        found = true;
                        break;
                    } else {
                        Toast.makeText(this, "DOB not matched", Toast.LENGTH_LONG).show();
                        return true; // Found user but DOB mismatch
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (f3 != null) f3.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return found;
    }

    private void checkFirebaseForPassword(String phone, String dob) {
        db.collection("HisaabAnalyser")
                .document("users")  // Fetching from "users" document
                .collection("userList")  // Collection inside "users"
                .document(phone) // Phone number as document ID
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String storedDob = documentSnapshot.getString("dob");
                        if (storedDob != null && storedDob.equals(dob)) {
                            String storedPass = documentSnapshot.getString("password");
                            if (storedPass != null) {
                                showPassword(storedPass);
                            } else {
                                Toast.makeText(this, "Password not found in Firestore", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(this, "DOB not matched", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(this, "User doesn't exist in Firestore", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Error fetching data: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private void showPassword(String password) {
        result.setVisibility(View.VISIBLE);
        submit.setVisibility(View.GONE);
        dob.setVisibility(View.GONE);
        ph.setVisibility(View.GONE);
        result.setText("**Your password is " + password);
    }


    private void changetheme(AppCompatButton l) {
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
