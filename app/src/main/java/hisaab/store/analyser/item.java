package hisaab.store.analyser;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hisaabcalculator.R;
import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.OnUserEarnedRewardListener;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;


import java.util.Date;
import java.util.Locale;

public class item extends AppCompatActivity {
    Button b1;
    EditText e1,e2;
    private RewardedAd rewardedAd;
    ImageView back;
    private final String TAG = "item";
    String mon,year,head;
    AdView adView;
    SharedPreferences coin;
    TextView itempoint;
    int point=0;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.primary));
        }
        try {

            MobileAds.initialize(this, new OnInitializationCompleteListener() {
                @Override
                public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                }
            });
            load();

            Date now = new Date();
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
            mon = monthFormat.format(now);


            back=findViewById(R.id.item_back);
            back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });


            SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.getDefault());
            year = yearFormat.format(now);
            t = findViewById(R.id.ab);
            Bundle e = getIntent().getExtras();
            itempoint=findViewById(R.id.item_point);
            SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
            head = spitem.getString("user", "");

            t.setText("Available Balance : " + totalBalance());
            b1 = findViewById(R.id.sb1);
            e1 = findViewById(R.id.se1);
            coin=getSharedPreferences(head+"coin",MODE_PRIVATE);
           point=coin.getInt("point",0);
           itempoint.setText(point+"P");




try {
    File path = getApplicationContext().getFilesDir();
    File f3 = new File(path, (head + year + "yearlySpend.txt"));
    if (!f3.exists()) f3.createNewFile();
}catch(Exception ex){}

            adView=findViewById(R.id.adView);
            //adView.setAdUnitId("ca-app-pub-1079506490540577/7402312139");
            //adView.setAdSize(getAdSize());
            AdRequest adRequest = new AdRequest.Builder().build();
            adView.loadAd(adRequest);
            e2 = findViewById(R.id.se2);
            /*s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    mon = parent.getItemAtPosition(i).toString();
                    b1.setText("Commit");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            s2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                    year = parent.getItemAtPosition(i).toString();
                    b1.setText("Commit");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/
            b1.setOnClickListener(view -> {
                point=coin.getInt("point",0);
                itempoint.setText(point+"P");
              // Toast.makeText(item.this,String.valueOf(point),Toast.LENGTH_LONG).show();
                if (rewardedAd != null && (point == 0)) {
                    Activity activityContext = item.this;
                    rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
                        @Override
                        public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                            // Handle the reward.

                            int rewardAmount = rewardItem.getAmount();
                            String rewardType = rewardItem.getType();
                            int curpoint=coin.getInt("point",0)+10;
                            coin.edit().putInt("point",curpoint).apply();
                            point=coin.getInt("point",0);
                            itempoint.setText(point+"P");


                        }
                    });


                    rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                        @Override
                        public void onAdClicked() {
                            // Called when a click is recorded for an ad.
                              load();
                        }

                        @Override
                        public void onAdDismissedFullScreenContent() {
                            // Called when ad is dismissed.
                            // Set the ad reference to null so you don't show the ad a second time.
                            Toast.makeText(item.this,"No points earned!",Toast.LENGTH_LONG).show();
                             load();


                        }

                        @Override
                        public void onAdFailedToShowFullScreenContent(AdError adError) {
                            // Called when ad fails to show.
                            load();
                        }

                        @Override
                        public void onAdImpression() {
                            // Called when an impression is recorded for an ad.

                            Toast.makeText(item.this,"Watch ad to earn 10+ reward",Toast.LENGTH_LONG).show();

load();
                        }

                        @Override
                        public void onAdShowedFullScreenContent() {
                            // Called when ad is shown.
load();
                        }
                    });


                } else {





                String item = e1.getText().toString().trim();
                String price = e2.getText().toString().trim();
                if (!mon.equals("")) {
                    if (!item.equals("") && !price.equals("")){
                        if (isOk(Long.parseLong(price)) == 1) {

                            add_data(item, price);
                            monthUpdate(Long.parseLong(price));
                            yearUpdate(Long.parseLong(price));
                            t.setText("Available Balance : " + deductMoney(Long.parseLong(price)));
                            e1.setText("");
                            e2.setText("");
                            b1.setText("Committed");
                            if(point>0){
                                int curpoint=coin.getInt("point",0)-1;
                                coin.edit().putInt("point",curpoint).apply();
                                point=coin.getInt("point",0);
                                itempoint.setText(point+"P");
                            }
                        }
                        else if(isOk(Long.parseLong(price)) == 2){
                            Toast.makeText(item.this,"invalid price",Toast.LENGTH_SHORT).show();
                        }
                        else {
                            Toast.makeText(this, "insufficient balance", Toast.LENGTH_LONG).show();
                        }
                    } else if (item.equals("")) {
                        Toast.makeText(this, "enter item", Toast.LENGTH_LONG).show();
                    } else if (price.equals("")) {
                        Toast.makeText(this, "enter price", Toast.LENGTH_LONG).show();
                    }
                } else Toast.makeText(this, "select month", Toast.LENGTH_LONG).show();

            }});

        }catch (Exception e){

        }
    }
    public int isOk(long p){
        if(p==0){


            return 2;
        }
        File path=getApplicationContext().getFilesDir();
        long bal3 = 0;
        try{
            FileInputStream f=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r=new InputStreamReader(f);
            BufferedReader br=new BufferedReader(r);
            String bal=br.readLine();
            if (bal==null) bal="0";
            bal3=Long.parseLong(bal);
        }
        catch(IOException e){
            e.printStackTrace();
        }
        if(bal3>=p) return 1;
        else return 0;
    }

    public long deductMoney(long p){
        File path=getApplicationContext().getFilesDir();
        long bal3=0;
        try{
            FileInputStream f=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r=new InputStreamReader(f);
            BufferedReader br=new BufferedReader(r);
            String bal=br.readLine();
            if (bal==null) bal="0";
            bal3=Long.parseLong(bal)-p;
            f.close();
            FileOutputStream f2=new FileOutputStream(new File(path,head+"balance.txt"));
            f2.write(Long.toString(bal3).getBytes());
            f2.close();

        }
        catch(IOException e){
            clear3();
            e.printStackTrace();
        }
        return bal3;
    }

    public void monthUpdate(long p) {
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


            long l = Long.parseLong(b5) + p;
            String b3 =Long.toString(l);
            FileOutputStream f6 = new FileOutputStream(new File(path, head+d+ "monthlySpend.txt"));
            f6.write((b3).getBytes());
            f6.close();
        }catch(IOException e){
            clear3();
            String price=e2.getText().toString();
            monthUpdate(Long.parseLong(price));
            e.printStackTrace();
        }
    }


    public void yearUpdate(long p) {
        File path=getApplicationContext().getFilesDir();
        try {
            String date = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);

            FileInputStream f3 = new FileInputStream(new File(path, head +d+"yearlySpend.txt"));

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            long l = Long.parseLong(b5) + p;
            String b3 =Long.toString(l);
            FileOutputStream f6 = new FileOutputStream(new File(path, head+d+ "yearlySpend.txt"));
            f6.write((b3).getBytes());
            f6.close();
        }catch(IOException e){
            clear3();
            String price=e2.getText().toString();
            yearUpdate(Long.parseLong(price));
            e.printStackTrace();
        }
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

    public void add_data(String item,String price){

        if (!item.equals("") && !price.equals("")){
            File path = getApplicationContext().getFilesDir();
            String date=new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
            String k =String.format("%-16s",item)+" Rs."+String.format("%-10s",price)+" "+date+"\n";


            try {
                FileOutputStream f =new FileOutputStream(new File(path,head+mon+year+".txt"),true);
                f.write(k.getBytes());
                b1.setText("committed");
                f.close();
            } catch(FileNotFoundException ee) {
                ee.printStackTrace();
            } catch(IOException ioException)
            {
                ioException.printStackTrace();
            }
        }}


    public String totalBalance(){
        File path=getApplicationContext().getFilesDir();
        String b3="0";
        try{
            FileInputStream f=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r=new InputStreamReader(f);
            BufferedReader br=new BufferedReader(r);
            b3=br.readLine();
            if (b3==null) b3="0";

            f.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
        return b3;

    }

    public void load(){


        AdRequest adRequest = new AdRequest.Builder().build();
        RewardedAd.load(this, "ca-app-pub-1079506490540577/9563671110",
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                     //   Toast.makeText(item.this,loadAdError.toString(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                       // Toast.makeText(item.this,"loaded",Toast.LENGTH_SHORT).show();

                    }
                });

    }
}