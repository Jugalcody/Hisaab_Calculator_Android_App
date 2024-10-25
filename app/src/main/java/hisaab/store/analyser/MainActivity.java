package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisaabcalculator.R;

import java.io.*;
public class MainActivity extends AppCompatActivity {
    Button l;
    SharedPreferences sp,spitem;
    ButtonEffect buttonEffect;
    String name="";
    EditText user, password;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            l = findViewById(R.id.login);
            user = findViewById(R.id.username);
            buttonEffect=new ButtonEffect(MainActivity.this);
            password = findViewById(R.id.password);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            buttonEffect.buttonEffect(l);
            sp = getSharedPreferences("login", MODE_PRIVATE);
            spitem = getSharedPreferences("item", MODE_PRIVATE);

            TextView forget=findViewById(R.id.forgetpass);
            forget.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i=new Intent(MainActivity.this,ForgotPage.class);
                    startActivity(i);
                }
            });

            user.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().contains(" ")) {
                        String updatedText = s.toString().replace(" ", "");

                        // Remove the TextWatcher temporarily to avoid recursion
                        user.removeTextChangedListener(this);

                        // Update the EditText with the modified text
                        user.setText(updatedText);

                        // Move the cursor to the end of the text
                        user.setSelection(updatedText.length());

                        // Reattach the TextWatcher
                        user.addTextChangedListener(this);
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
                    if (s.toString().contains(" ")) {
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
            l.setOnClickListener(view -> {

                String user = this.user.getText().toString();
                String pass = password.getText().toString();
                if (!user.equals("") && !pass.equals("")) {

                        if (isAuthenticate(user, pass)) {
                            Intent p1 = new Intent(this, first.class);
                            Toast.makeText(this, "Welcome " + name, Toast.LENGTH_LONG).show();
                            SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
                            spitem.edit().putString("user", user).apply();
                            spitem.edit().putString("name", name).apply();
                            sp.edit().putBoolean("islogged", true).apply();
                            startActivity(p1);

                        }



                } else if (user.equals("")) {
                    Toast.makeText(this,"empty field!", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(this, "enter password", Toast.LENGTH_SHORT).show();
                }

            });

        }catch (Exception e){

        }
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
                            name=arr[2];
                            //dob=arr[3];

                            /*for(int i=2;i<arr.length;i++)
                            name=name+arr[i]+" ";
*/
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