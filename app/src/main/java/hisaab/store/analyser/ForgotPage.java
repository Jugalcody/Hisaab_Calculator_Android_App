package hisaab.store.analyser;

import android.app.DatePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.hisaabcalculator.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Calendar;

public class ForgotPage extends AppCompatActivity {

    EditText ph,email;
    TextView result,dob;
    ImageView back;
    String pass="";

    AppCompatButton submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.primary));
        }


        ph=findViewById(R.id.phone_number_forget);
        email=findViewById(R.id.forgotemail);
        submit=findViewById(R.id.submit_button_forget);
        back=findViewById(R.id.back_forgot_);
        result=findViewById(R.id.result_forget);
        dob=findViewById(R.id.dob_forget);
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
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phone=ph.getText().toString().trim();
                String dob_=dob.getText().toString().trim();
                String email_=email.getText().toString().trim();



                isTrue(phone,dob_,email_);

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


    public boolean isTrue(String ph_,String dob_,String email_) {
        int y = 0;
        int v=0;
        int e=0;

        File path = getApplicationContext().getFilesDir();
        FileInputStream f3=null;
        try {
            f3=new FileInputStream(new File(path,"valid2.txt"));
            InputStreamReader r=new InputStreamReader(f3);
            BufferedReader br=new BufferedReader(r);


            String txt;
            while ((txt = br.readLine()) != null) {
                String[] arr = txt.split(" ");

                if (arr[0].equals(ph_)) {
                    v = 1;
                    if (arr[3].equals(dob_)) {
                        y=1;
                        if (arr[4].equals(email_)) {


                            e = 1;
                            pass = arr[1];

                            /*for(int i=2;i<arr.length;i++)
                            name=name+arr[i]+" ";
*/
                        }

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

        if (v == 1 && y==1 && e==1) {
            result.setVisibility(View.VISIBLE);
            submit.setVisibility(View.GONE);
            email.setVisibility(View.GONE);
            dob.setVisibility(View.GONE);
            result.setText("**Your password is "+pass);
            ph.setVisibility(View.GONE);
            return true;
        } else if(v==1 && y==0){
            Toast.makeText(this, "dob not matched", Toast.LENGTH_LONG).show();
            return false;
        }
        else if(v==1 && y==1 && e==0){
            Toast.makeText(this, "email not matched", Toast.LENGTH_LONG).show();
            return false;
        }
        else{
            Toast.makeText(this, "user doesn't exist", Toast.LENGTH_LONG).show();
            return false;
        }
    }

}