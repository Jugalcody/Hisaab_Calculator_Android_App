package com.example.hisaabcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ScanQR extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback, DecoratedBarcodeView.TorchListener {
    DecoratedBarcodeView barcodeScannerView;
    Toolbar myToolbar;
    boolean isenable=true;
    String amt="",head="";
    SharedPreferences sp,splogin;
    private static final int CAMERA_PERMISSION_REQUEST = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qr);

        myToolbar = findViewById(R.id.my_toolbar);
        sp=getSharedPreferences("item",MODE_PRIVATE);
        splogin=getSharedPreferences("login",MODE_PRIVATE);
       setSupportActionBar(myToolbar);
       getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.inflateMenu(R.menu.menu);

        head=sp.getString("user","");
       amt=sp.getString("price","0");
        barcodeScannerView = findViewById(R.id.dbar);

        barcodeScannerView.setTorchListener(this);
barcodeScannerView.setVisibility(View.VISIBLE);
                if (ContextCompat.checkSelfPermission(ScanQR.this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(ScanQR.this, new String[]{android.Manifest.permission.CAMERA}, CAMERA_PERMISSION_REQUEST);
                } else {
                    if(!amt.trim().equals("")) {
                            startScanner(amt);

                    }
                    else{
                        Toast.makeText(getApplicationContext(),"invalid price, retry",Toast.LENGTH_SHORT).show();
                        Intent i=new Intent(this, item.class);
                        startActivity(i);
                    }
                }

    }

    public String getfilteredUPI(String upi){
        String[] arr=upi.split("&");
        String newupi="";
        String[] p=arr[arr.length-1].split("=");
        if(p[0].equals("sign")){
            for(int i=0;i<arr.length-1;i++){
                newupi+=arr[i]+"&";
            }
            return newupi;
        }
        else{
            return upi;
        }

    }
    private void startScanner(String amtt) {
        barcodeScannerView.resume();
        barcodeScannerView.setVisibility(View.VISIBLE);
        barcodeScannerView.decodeContinuous(result -> {
            String content = result.getText();
            Intent intent = new Intent();
            barcodeScannerView.setVisibility(View.GONE);
            if(isenable) {
                String msg = "Items brought are : " + sp.getString("items", "") + "\n by "+head.trim();
                intent.setData(Uri.parse(getfilteredUPI(content) + "tn=testing" + "&am=" + amtt + "&cu=INR"));
               // intent.setData(Uri.parse("upi://pay?pa=9101332924@ibl&pn=9101332924&mc=0000&mode=02&purpose=00"+"tn=" + msg + "&am=" + amtt + "&cu=INR"));
                intent.setAction(Intent.ACTION_VIEW);

                Intent chooser = Intent.createChooser(intent, "Pay with...");
isenable=false;
                startActivityForResult(chooser, 1, null);
            }

        });
    }
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==android.R.id.home) {
            this.finish();
        }
        else if(item.getItemId()==R.id.guide) {
            Intent gi = new Intent(this, guide.class);
            startActivity(gi);
        }
        else if(item.getItemId()==R.id.contact){
                Intent gi=new Intent(this,about.class);
                startActivity(gi);
                return true;
        }
        else if(item.getItemId()==R.id.logout_menu){
            open();
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                barcodeScannerView.setVisibility(View.GONE);
                add_data(sp.getString("items", ""),amt);
                deductMoney(Integer.parseInt(amt));
                Toast.makeText(getApplicationContext(),"Paid",Toast.LENGTH_LONG).show();
                Intent i=new Intent(ScanQR.this,item.class);
                startActivity(i);
                monthUpdate(Integer.parseInt(amt));
            }else if (resultCode == RESULT_CANCELED) {
                barcodeScannerView.setVisibility(View.VISIBLE);
                isenable=true;
               Toast.makeText(getApplicationContext(),"Payment cancelled",Toast.LENGTH_SHORT).show();

            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(!amt.trim().equals("")) {
                   // barcodeScannerView.setVisibility(View.VISIBLE);
                        startScanner(amt);

                }
            } else {
                Toast.makeText(this, "Camera permission is required to scan QR codes", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public void onTorchOn() {
        // Handle torch on event
    }

    @Override
    public void onTorchOff() {
        // Handle torch off event
    }

    public int deductMoney(int p){
        File path=getApplicationContext().getFilesDir();
        int bal3=0;
        try{
            FileInputStream f=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r=new InputStreamReader(f);
            BufferedReader br=new BufferedReader(r);
            String bal=br.readLine();
            if (bal==null) bal="0";
            bal3=Integer.parseInt(bal)-p;
            f.close();
            FileOutputStream f2=new FileOutputStream(new File(path,head+"balance.txt"));
            f2.write(Integer.toString(bal3).getBytes());
            f2.close();

        }
        catch(IOException e){
            clear3();
            e.printStackTrace();
        }
        return bal3;
    }

    public void clear3() {
        String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
        int d = Integer.parseInt(date);
        File path=getApplicationContext().getFilesDir();
        try{
            FileOutputStream f=new FileOutputStream(new File(path,head+d+"monthlySpend.txt"));
            f.write("".getBytes());

        }
        catch(IOException e){
            Toast.makeText(this,"unable to clear",Toast.LENGTH_LONG).show();
        }
    }

    public void monthUpdate(int p) {
        File path=getApplicationContext().getFilesDir();
        try {
            String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);

            FileInputStream f3 = new FileInputStream(new File(path, head +d+"monthlySpend.txt"));

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            int l = Integer.parseInt(b5) + p;
            String b3 = Integer.toString(l);
            FileOutputStream f6 = new FileOutputStream(new File(path, head+d+ "monthlySpend.txt"));
            f6.write((b3).getBytes());
            f6.close();
        }catch(IOException e){
            clear3();
            String price=sp.getString("price","0");
            monthUpdate(Integer.parseInt(price));
            e.printStackTrace();
        }
    }

    public void add_data(String item,String price){

        if (!item.equals("") && !price.equals("")){
            File path = getApplicationContext().getFilesDir();
            String date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String k =String.format("%-16s",item)+" Rs."+String.format("%-10s",price)+" "+date+"\n";


            try {
                FileOutputStream f =new FileOutputStream(new File(path,head+sp.getString("mon","")+sp.getString("year","")+".txt"),true);
                f.write(k.getBytes());
                f.close();
            } catch(FileNotFoundException ee) {
                ee.printStackTrace();
            } catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
        }}
    public void open(){
        AlertDialog.Builder a=new AlertDialog.Builder(this);
        a.setMessage("Do you want to logout?");
        a.setPositiveButton("yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent gi=new Intent(ScanQR.this,MainActivity.class);
                splogin.edit().putBoolean("islogged",false).apply();
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

}