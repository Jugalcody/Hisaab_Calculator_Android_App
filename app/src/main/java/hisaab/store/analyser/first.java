package hisaab.store.analyser;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;


public class first extends AppCompatActivity {
TextView t1,t2,t3,t4,t5,user_text,yearly;
Toolbar toolbar;
ImageView user_img;
SharedPreferences sp,spitem;
int REQUEST_IMAGE_PICKER=1;
    String n;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mi=getMenuInflater();
        mi.inflate(R.menu.menu,menu);

        /*// Set showAsAction for each menu item
        MenuItem item1 = menu.findItem(R.id.guide);

        MenuItem item2 = menu.findItem(R.id.contact);
        item1.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        item2.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);*/
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
            reset();
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
        if(!isNightMode(first.this)){

        }
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
            n = spitem.getString("user", "");
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
    user_img.setPadding(0,0,0,0);
    user_img.setImageBitmap(decodeStringToBitmap(sp.getString(spitem.getString("user","")+"img", "")));
}
catch (Exception e){
    user_img.setPadding(20,20,20,20);
    user_img.setImageResource(R.drawable.plus2);
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
                Toast.makeText(first.this, "Account successfully deleted", Toast.LENGTH_SHORT).show();
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
try{
    sp.edit().remove(phoneNumber+"img").apply();
}
catch (Exception eee){

}
        if (!file.exists()) {
            Toast.makeText(this, "File not found", Toast.LENGTH_LONG).show();
            return;
        }

        try {
            // Read the existing data
            FileInputStream fis = new FileInputStream(file);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) {
                // Check if the line contains the phone number
                if (!line.contains(phoneNumber)) {
                    sb.append(line).append("\n");
                }
            }

            br.close();
            fis.close();

            // Write the updated data back to the file
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(sb.toString().trim().getBytes()); // Remove trailing newline
            fos.close();

            Toast.makeText(this, "User data deleted successfully", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "IO exception", Toast.LENGTH_LONG).show();
        }
    }
    public void reset(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
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

        AlertDialog alerts=a.create();
        alerts.show();
    }
}