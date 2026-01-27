package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import android.content.Context;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button l;
    SharedPreferences sp, spitem;
    ButtonEffect buttonEffect;
    String name = "";
    ImageView aboutbut;

    ProgressBar progressBar;
    FirebaseFirestore db;
    EditText username, password;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi = getMenuInflater();
        mi.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            l = findViewById(R.id.login);
            username = findViewById(R.id.username);
            buttonEffect = new ButtonEffect(MainActivity.this);
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            password = findViewById(R.id.password);
            progressBar = findViewById(R.id.loginprogress);
            aboutbut = findViewById(R.id.about_login);
            changetheme(l);
            buttonEffect.buttonEffect(l);
            db = FirebaseFirestore.getInstance();
         /*   FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                    .setPersistenceEnabled(false) // Disable offline persistence
                    .build();*/
           // db.setFirestoreSettings(settings);

            sp = getSharedPreferences("login", MODE_PRIVATE);
            spitem = getSharedPreferences("item", MODE_PRIVATE);

            TextView forget = findViewById(R.id.forgetpass);





            username.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (s.toString().contains(" ")) {
                        String updatedText = s.toString().replace(" ", "");

                        // Remove the TextWatcher temporarily to avoid recursion
                        username.removeTextChangedListener(this);

                        // Update the EditText with the modified text
                        username.setText(updatedText);

                        // Move the cursor to the end of the text
                        username.setSelection(updatedText.length());

                        // Reattach the TextWatcher
                        username.addTextChangedListener(this);
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

            checkOrientation();

        } catch (Exception e) {
            progressBar.setVisibility(View.GONE);
        }
    }



    public boolean isAuthenticate(String u, String p) {
        progressBar.setVisibility(View.VISIBLE);
        String dob="";
        int y = 0;
        int v = 0;
        File path = getApplicationContext().getFilesDir();
        FileInputStream f3 = null;
        try {
            f3 = new FileInputStream(new File(path, "valid2.txt"));
            InputStreamReader r = new InputStreamReader(f3);
            BufferedReader br = new BufferedReader(r);

            String txt;
            while ((txt = br.readLine()) != null) {
                String[] arr = txt.split("_");

                if (arr[0].equals(u)) {
                    v = 1;
                    if (arr[1].equals(p)) {

                        y = 1;
                        name = arr[2];
                        sp.edit().putString("fullname", arr[4].trim()).apply();
                        sp.edit().putString("userserial", arr[5].trim()).apply();
                        dob=arr[3];
                        spitem.edit().putString("name", name).apply();
                        spitem.edit().putString("dob", dob).apply();

                        break;
                    }
                }
            }
        } catch (IOException fileNotFoundException) {
            fileNotFoundException.printStackTrace();
            progressBar.setVisibility(View.GONE);
        } finally {
            if (f3 != null) {
                try {
                    f3.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }

        if (v == 1 && y == 1) {
            try {
                checkUserInFirestore(u, p, dob,true,name);
            }catch (Exception e){

            }
            return true;
        } else if (v == 1 && y == 0) {
            Toast.makeText(this, "wrong password", Toast.LENGTH_LONG).show();
            progressBar.setVisibility(View.GONE);
            return false;
        } else {
            try {
                checkUserInFirestore(u, p, dob,false,name);
            }catch (Exception e){

            }
            return false;
        }
    }

    private void checkUserInFirestore(String u, String p, String dob,boolean isfoundinfile,String name_fromfile) {
        progressBar.setVisibility(View.VISIBLE);

        // Check if user exists in Firestore
        db.collection("HisaabAnalyser")
                .document("users")
                .collection("userList")
                .document(u)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        if(!isfoundinfile) {
                            // If user exists, check password
                            String storedPassword = documentSnapshot.getString("password");
                            if (storedPassword != null && storedPassword.equals(p)) {
                                name = documentSnapshot.getString("name");
                                if(name.trim().equals("")){
                                    name="N/A";
                                }
                                sp.edit().putString("fullname", name).apply();
                                spitem.edit().putString("name", name.split(" ")[0]).apply();
                                saveUserOffline(u, p, name, documentSnapshot.getString("dob"), name);
                            } else {
                                Toast.makeText(this, "Wrong password", Toast.LENGTH_LONG).show();
                                progressBar.setVisibility(View.GONE);
                            }
                        }
                        onAuthenticationSuccess(u, Objects.requireNonNull(documentSnapshot.getString("lastLoggedOutDate")).split("&")[0].trim());
                    } else {
                        if(!isfoundinfile) {
                            Toast.makeText(this, "User doesn't exist!", Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                        else{
                            addUserToFirestore(u,p,dob,name);
                        }

                    }
                })
                .addOnFailureListener(e -> {
                    if(isInternetAvailable()){
                        Toast.makeText(this, "Something went wrong!", Toast.LENGTH_LONG).show();
                    }

                    progressBar.setVisibility(View.GONE);
                });

    }

    private void addUserToFirestore(String u, String p, String dob,String name) {

        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());

        // Prepare the user data to be saved
        Map<String, Object> userData = new HashMap<>();
        userData.put("name", name); // Replace with actual name if available
        userData.put("phone", u);
        userData.put("password", p);
        userData.put("lastLoggedOutDate", "new");
        userData.put("dob", dob);  // Passing the dob
        userData.put("currentDate", currentDate);

        // Add to Firestore
        db.collection("HisaabAnalyser")
                .document("users")
                .collection("userList")
                .document(u)
                .set(userData)
                .addOnSuccessListener(aVoid -> {
                    onAuthenticationSuccess(u, "new");
                })
                .addOnFailureListener(e -> {
                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(MainActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
                });
    }


    public void saveUserOffline(String u, String p,String name,String dob,String fullname) {
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
                    try {
                        f.write(k.getBytes());
                        sp.edit().putInt("usercount", serial + 1).apply();
                        Intent p1 = new Intent(this, first.class);
                        updateUserLoginStatus(username.getText().toString());
                        checkLastLogin(MainActivity.this, sp.getString("logoutdate", ""));
                        sp.edit().putString("name", name).apply();
                        spitem.edit().putString("user", username.getText().toString()).apply();
                        spitem.edit().putString("name", name).apply();
                        spitem.edit().putString("dob", dob).apply();
                        sp.edit().putBoolean("islogged", true).apply();
                        username.setText("");
                        password.setText("");
                        SharedPreferences sp2 = getSharedPreferences("theme", MODE_PRIVATE);
                        sp2.edit().putString("thememode","default").apply();

                        startActivity(p1);
                        Toast.makeText(MainActivity.this, "LoggedIn Successfully!", Toast.LENGTH_LONG).show();
                        Toast.makeText(this, "Welcome " + name, Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);
                    }catch (Exception e){
                        Toast.makeText(this,e.toString(), Toast.LENGTH_SHORT).show();
                    }

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

    private void onAuthenticationSuccess(String phone, String lastLogoutDate) {
        updateUserLoginStatus(phone);
        checkLastLogin(MainActivity.this, lastLogoutDate);
        spitem.edit().putString("user", phone).apply();
        spitem.edit().putString("name", name).apply();
        sp.edit().putBoolean("islogged", true).apply();
        progressBar.setVisibility(View.GONE);
    }

    public void register(View view) {
        Intent i = new Intent(MainActivity.this, signup.class);
        startActivity(i);
    }

    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }

    public void updateUserLoginStatus(String phone) {
        if (isInternetAvailable()) {


            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy & HH:mm:ss", Locale.getDefault());
            String currentDateTime = sdf.format(new Date());
            Map<String, Object> updates = new HashMap<>();
            updates.put("isLoggedIn", true);
            if(!spitem.getString("name", name).trim().equals("")) {
                updates.put("name", spitem.getString("name", name));
            }
            updates.put("phone", phone);
            updates.put("lastLoggedInDate", currentDateTime);
            db.collection("HisaabAnalyser")
                    .document("users")
                    .collection("userList")
                    .document(phone)
                    .update(updates)
                    .addOnFailureListener(e -> System.out.println(e.getMessage()));
        } else {
            SharedPreferences sp = getSharedPreferences("login", MODE_PRIVATE);
            sp.edit().putBoolean("isLoginstatusSent", false).apply();
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


    public static void checkLastLogin(Context context, String lastLogoutDate) {
        // Define the date format (same format used while saving the date)
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        String msg="";
        if (lastLogoutDate.equals("new")) {

        } else {
            try {
                // Parse last login date from string
                Date lastDate = sdf.parse(lastLogoutDate);
                Date currentDate = new Date(); // Get today's date

                // Calculate the difference in milliseconds
                long diffInMillis = currentDate.getTime() - lastDate.getTime();

                // Convert milliseconds to days
                long daysDifference = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                if (daysDifference > 2) {
                    // Show toast if the gap is more than 7 days

                    if(daysDifference/30>0){
                        if(daysDifference/30==1)
                        msg="Great comeback after " + daysDifference/30 + " months and "+daysDifference%30+" days!";
                        else msg="Great comeback after " + daysDifference/30 + " month and "+daysDifference%30+" days!";
                    }
                    else if(daysDifference/31>0){
                        if(daysDifference/31>1)
                            msg="Great comeback after " + daysDifference/31 + " months and "+daysDifference%30+" days!";
                        else msg="Great comeback after " + daysDifference/31 + " month and "+daysDifference%30+" days!";
                    }
                    else
                    msg="Great comeback after " + daysDifference + " days!";

                }

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences sp = context.getSharedPreferences("login", MODE_PRIVATE);
        sp.edit().putString("comebackmsg",msg).apply();
    }

    private void checkOrientation() {
        AdjustSizeConfiguration displaysize = new AdjustSizeConfiguration(MainActivity.this);
        Configuration config = getResources().getConfiguration();
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(username, 55);
            displaysize.setfixWidth(password, 55);
            displaysize.setfixWidth(l, 55);
        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            displaysize.setfixWidth(username, 85);
            displaysize.setfixWidth(password, 85);
            displaysize.setfixWidth(l, 85);
        }
    }

    public void login(View view) {

                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (!user.equals("") && !pass.equals("")) {
                    if (user.trim().length() == 10) {
                        progressBar.setVisibility(View.VISIBLE);
                        if (isAuthenticate(user, pass)) {
                            Intent p1 = new Intent(view.getContext(), first.class);
                            Toast.makeText(view.getContext(), "Welcome " + name, Toast.LENGTH_SHORT).show();
                            updateUserLoginStatus(user);
                            checkLastLogin(MainActivity.this, sp.getString("logoutdate", ""));
                            sp.edit().putString("name", name).apply();
                            spitem.edit().putString("user", user).apply();
                            progressBar.setVisibility(View.GONE);
                            spitem.edit().putString("name", name).apply();
                            sp.edit().putBoolean("islogged", true).apply();
                            startActivity(p1);
                        }
                    } else {
                        Toast.makeText(view.getContext(), "invalid phone number!", Toast.LENGTH_SHORT).show();
                    }
                } else if (user.equals("")) {
                    Toast.makeText(view.getContext(), "Empty phone number!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                } else {
                    Toast.makeText(view.getContext(), "Empty password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

    public void aboutfunction(View view) {
        Intent i = new Intent(MainActivity.this, AboutPage.class);
        i.putExtra("key", "outside");
        startActivity(i);
    }

    public void forgetclick(View view) {
        Intent i = new Intent(MainActivity.this, ForgotPage.class);
        startActivity(i);
    }



    private void changetheme(Button l) {
        SharedPreferences sp = getSharedPreferences("theme", MODE_PRIVATE);
        sp.edit().putString("theme","pink").apply();
        if (sp.getString("theme", "dark").equals("pink")) {
            l.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.primarylight)
            );

            aboutbut.setImageTintList(ContextCompat.getColorStateList(this, R.color.primarylight));

        } else {

            l.setBackgroundTintList(
                    ContextCompat.getColorStateList(this, R.color.primarydark)
            );
            aboutbut.setImageTintList(ContextCompat.getColorStateList(this, R.color.primarydark));

        }


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            l.setBackgroundTintBlendMode(BlendMode.SRC_ATOP);
        } else {
            l.setBackgroundTintMode(PorterDuff.Mode.SRC_ATOP);
        }
    }


}
