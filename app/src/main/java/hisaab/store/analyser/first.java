package hisaab.store.analyser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisaabcalculator.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class first extends AppCompatActivity {
TextView t1,t2,t3,t4,t5,user_text,yearly;
Toolbar toolbar;
ShapeableImageView user_img;
SharedPreferences sp,spitem;
int REQUEST_IMAGE_PICKER=1;
    String n;
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
        else if(item.getItemId()==R.id.reset_menu){
            reset("all");
        }
        else if(item.getItemId()==R.id.reset_photo){
            reset("photo");
        }
        else if(item.getItemId()==R.id.logout_menu){
          open();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        try {
            t1 = findViewById(R.id.fb1);
            t2 = findViewById(R.id.fb2);
            user_text = findViewById(R.id.first_toolbar_txt);
            user_img = findViewById(R.id.first_toolbar_img);
            t3 = findViewById(R.id.fb3);
            t4 = findViewById(R.id.fb4);
            t5 = findViewById(R.id.delete_but_home);
            yearly=findViewById(R.id.fbyear);

//ca-app-pub-1079506490540577/9563671110
        }
        catch(Exception e){

        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.primary));
        }
        try {
            sp = getSharedPreferences("login", MODE_PRIVATE);
            spitem = getSharedPreferences("item", MODE_PRIVATE);
            n = spitem.getString("name", "");
            user_text.setText("Hii " + n + ",");

            toolbar = findViewById(R.id.first_toolbar);
            setSupportActionBar(toolbar);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                toolbar.setTitleTextColor(getColor(R.color.white));
            }

        }
        catch(Exception e){

        }
try{
    if(!sp.getString(spitem.getString("user","")+"img", "").equals("")) {
        user_img.setPadding(0, 0, 0, 0);
        user_img.setImageBitmap(decodeStringToBitmap(sp.getString(spitem.getString("user", "") + "img", "")));
    }
else{
    user_img.setImageResource(R.drawable.baseline_person_24_white);
        user_img.setPadding(0,0,0,0);
}}
catch (Exception e){
    user_img.setPadding(0,0,0,0);
    user_img.setImageResource(R.drawable.baseline_person_24_white);
}
user_img.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
try {
    openImagePicker();
}
catch (Exception e){

}
    }
});
        t1.setOnClickListener(view -> {
           Intent i1=new Intent(this,item.class);
           i1.putExtra("head",n);
           startActivity(i1);
        });

        t2.setOnClickListener(view -> {
            Intent i2=new Intent(this,display.class);
            i2.putExtra("head",n);
            startActivity(i2);
        });
        yearly.setOnClickListener(view -> {
            Intent i2=new Intent(this,Yearly.class);
            i2.putExtra("head",n);
            startActivity(i2);
        });
        t3.setOnClickListener(view -> {
            Intent i3=new Intent(this,monthly.class);
            i3.putExtra("head",n);
            startActivity(i3);
        });

        t4.setOnClickListener(view -> {
            Intent i4=new Intent(this,money.class);
            i4.putExtra("head",n);
            startActivity(i4);
        });
        t5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    open2();
                }catch (Exception e){

                }


            }
        });
    }
    public void open(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
        a.setMessage("Do you want to logout?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gi=new Intent(first.this,MainActivity.class);
                sp.edit().putBoolean("islogged",false).apply();
                startActivity(gi);
            }
        });

        a.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alerts=a.create();
        alerts.show();
    }

    public void open2(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
        a.setMessage("Do you want to delete your account?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gi=new Intent(first.this,MainActivity.class);
                sp.edit().putBoolean("islogged",false).apply();
                spitem = getSharedPreferences("item", MODE_PRIVATE);
                n = spitem.getString("user", "");
                deleteUserFile(n);
                deleteUserData(n);
                startActivity(gi);
                finishAffinity();

            }
        });

        a.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        AlertDialog alerts=a.create();
        alerts.show();
    }
    @Override
    public void onBackPressed() {
        finishAffinity();
        super.onBackPressed();
    }


    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_IMAGE_PICKER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICKER && resultCode == RESULT_OK && data != null) {
            try {
                // Get the URI of the selected image
                assert data.getData() != null;
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());

                // Display the selected image in ImageView
                user_img.setPadding(0,0,0,0);
                sp.edit().putString(spitem.getString("user","")+"img",encodeBitmapToString(bitmap)).apply();
                user_img.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error loading image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private static String encodeBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] byteArray = baos.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);

    }

    private static Bitmap decodeStringToBitmap(String encodedBitmap) {
        byte[] decodedByteArray = Base64.decode(encodedBitmap, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedByteArray, 0, decodedByteArray.length);
    }

private boolean isNightMode(Context context){
        int nightModeFlags=context.getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return nightModeFlags==Configuration.UI_MODE_NIGHT_YES;
}


    public void deleteUserFile(String head) {
        File path = getApplicationContext().getFilesDir();
        File[] files =path.listFiles();
        if (files != null) {
            for (File file : files) {
                // Check if the file name contains the phone number
                if (file.getName().contains(head)) {
                    // Attempt to delete the file
                    if (file.delete()) {
                      //  System.out.println("Deleted file: " + file.getName());
                    }
                }
            }

    }
    }
    public void deleteUserData(String phoneNumber) {
        File path = getApplicationContext().getFilesDir();
        File file = new File(path, "valid2.txt");
        File tempFile = new File(path, "valid2_temp.txt");

        // Remove shared preferences entry
        try {
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(phoneNumber + "img");
            editor.apply();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Error removing image", Toast.LENGTH_LONG).show();
        }

        if (!file.exists()) {
            Toast.makeText(this, "Something went wrong", Toast.LENGTH_LONG).show();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)))) {

            String line;
            boolean isDeleted = false;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split(" ");
                if (!arr[0].equals(phoneNumber)) {
                    bw.write(line);
                    bw.newLine();
                } else {
                    isDeleted = true;
                }
            }

            // Close readers/writers
            bw.flush();
            bw.close();
            br.close();

            // Delete the original file and rename the temp file
            if (file.delete()) {
                if (tempFile.renameTo(file)) {
                    Toast.makeText(this, "Account successfully deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Failed to rename temp file", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(this, "Failed to delete original file", Toast.LENGTH_LONG).show();
            }

            if (!isDeleted) {
                Toast.makeText(this, "User account not found", Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IO exception occurred while deleting user data", Toast.LENGTH_LONG).show();
        }
    }
    public void reset(String key) {
        AlertDialog.Builder a = new AlertDialog.Builder(this);
        if (key.equals("all")) {
            a.setMessage("Do you want to reset your account?");
            a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    spitem = getSharedPreferences("item", MODE_PRIVATE);
                    n = spitem.getString("user", "");
                    deleteUserFile(n);
                    Toast.makeText(first.this, "Account reset successfully ", Toast.LENGTH_SHORT).show();

                }
            });

            a.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });

        }else{

            a.setMessage("Do you want to reset your photo?");
            a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    try {


                        sp.edit().putString(spitem.getString("user","")+"img","").apply();
                         user_img.setImageResource(R.drawable.baseline_person_24_white);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            user_img.setStrokeColor(ColorStateList.valueOf(getColor(R.color.strokecolor)));

                            //user_img.setBackgroundColor(getColor(R.color.transparent));
                        }

                        Toast.makeText(first.this, "photo removed ", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {

                    }


                }
            });

            a.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                }
            });


        }

        AlertDialog alerts = a.create();
        alerts.show();

    }
}