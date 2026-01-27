package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.rewarded.RewardedAd;
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
    Button buybutton;
    EditText e1,e2;
    private RewardedAd rewardedAd;

    LinearLayout itemLayout;
    AdjustSizeConfiguration displaysize;
    Utils utils;
    Configuration config;
    ImageView back;
    private final String TAG = "item";
    LinearLayout parentlayout;
    RelativeLayout toolbar;
    String mon,year,head;
    AdView adView;
    SharedPreferences coin,spitem;
    TextView itempoint;
    int point=0;
    Showad ad;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.primarydark));
        }

        try {
            displaysize = new AdjustSizeConfiguration(item.this);
            itemLayout=findViewById(R.id.item_enterlayout);
            utils=new Utils();
            config = getResources().getConfiguration();
            parentlayout=findViewById(R.id.item_container);
            ad=new Showad(item.this);
            ad.loadRewardad();

            Date now = new Date();
            SimpleDateFormat monthFormat = new SimpleDateFormat("MMM", Locale.getDefault());
            mon = monthFormat.format(now);
            if(mon.equals("Jun")) mon="June";
            else if(mon.equals("Jul")) mon="July";
            else if(mon.equals("Sep")) mon="Sept";

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
            toolbar=findViewById(R.id.item_toolbar);
            spitem = getSharedPreferences("item", MODE_PRIVATE);
            head = spitem.getString("user", "");

            t.setText("Available Balance : " + totalBalance());
            buybutton = findViewById(R.id.sb1);

            ButtonEffect buttonEffect=new ButtonEffect(item.this);
            buttonEffect.buttonEffect(buybutton);
            e1 = findViewById(R.id.se1);
            coin=getSharedPreferences(head+"coin",MODE_PRIVATE);
            point=coin.getInt("point",0);
            itempoint.setText(point+"P");

            checkOrientation();
try {
    e1.setOnTouchListener(new View.OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            try {
                buybutton.setText("Proceed");
            } catch (Exception e) {
            }
            return false;
        }
    });
}catch (Exception ee){

}
            changetheme();

            itempoint.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ad.createWindow(item.this,parentlayout,itempoint);
                }
            });
            try {
                File path = getApplicationContext().getFilesDir();
                File f3 = new File(path, (head+"yearlySpend" + year+ ".txt"));
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
            buybutton.setOnClickListener(view -> {
                rewardedAd=ad.getRewardedAd();
                point=coin.getInt("point",0);
                if (point == 0) {
                    ad.createWindow(item.this,parentlayout,itempoint);
                } else {
                    String item = e1.getText().toString().trim();
                    String price = e2.getText().toString().trim();
                    if (!mon.equals("")) {
                        if (!item.equals("") && !price.equals("")){
                            if (isOk(Long.parseLong(price)) == 1) {

                                add_data(item, price);
                                utils.monthUpdate(Long.parseLong(price),this,head,e2);
                                utils.yearUpdate(Long.parseLong(price),this,year,head,e2);
                                t.setText("Available Balance : " + utils.deductMoney(Long.parseLong(price),this));
                                e1.setText("");
                                e2.setText("");
                                spitem.edit().putString("monthlyspent"+head,utils.monthlySpend(mon,this,year)).apply();
                                buybutton.setText("Committed");
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
                            Toast.makeText(this, "Empty Item", Toast.LENGTH_LONG).show();
                        } else if (price.equals("")) {
                            Toast.makeText(this, "Empty Price", Toast.LENGTH_LONG).show();
                        }
                    } else Toast.makeText(this, "Select Month", Toast.LENGTH_LONG).show();

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
            File file=new File(path,head+"balance.txt");
            if(!file.exists())file.createNewFile();
            FileInputStream f=new FileInputStream(file);
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


/*

    public String yearlySpend(String year){
        File path=getApplicationContext().getFilesDir();
        String b4="0";
        try{
            File file=new File(path,head+"yearlySpend"+year+".txt");
            if(!file.exists()) file.createNewFile();
            FileInputStream f2=new FileInputStream(file);

            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if(b4==null) b4="0";
            f2.close();
        }
        catch(IOException e){
            Toast.makeText(this, "something went wrong!", Toast.LENGTH_LONG).show();
        }
        return b4;
    }

*/

    public void add_data(String item, String price) {

        if (!item.isEmpty() && !price.isEmpty()) {

            File path = getApplicationContext().getFilesDir();
            File file = new File(path, head + "hisaab" + mon + year + "data.txt");

            int sno = 1;

            try {
                // Find last S.No if file already has data
                if (file.exists() && file.length() > 0) {
                    FileInputStream fis = new FileInputStream(file);
                    BufferedReader br = new BufferedReader(new InputStreamReader(fis));

                    String lastLine = "", line;
                    while ((line = br.readLine()) != null) {
                        lastLine = line;
                    }
                    br.close();

                    if (!lastLine.isEmpty()) {
                        String[] parts = lastLine.split("#");
                        sno = Integer.parseInt(parts[parts.length - 1]) + 1;
                    }
                }

                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                String k = item + "#Rs." + price + "#" + date + "#" + sno + "\n";

                FileOutputStream fos = new FileOutputStream(file, true);
                fos.write(k.getBytes());
                fos.close();

                buybutton.setText("commit");

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public String totalBalance(){
        File path=getApplicationContext().getFilesDir();
        String b3="0";
        File file=null;
        try{
            file=new File(path,head+"balance.txt");
            if(!file.exists()) file.createNewFile();
            FileInputStream f=new FileInputStream(file);
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


    private void changetheme(){
        SharedPreferences sp=getSharedPreferences("theme",MODE_PRIVATE);
        LinearLayout layout=findViewById(R.id.item_container);
        TextView itemnametxt=findViewById(R.id.item_name);
        TextView itempricetxt=findViewById(R.id.item_price);
        ImageView toolbarback=findViewById(R.id.item_back);
        TextView toolbartxt=findViewById(R.id.item_buytitle);
        getWindow().setNavigationBarColor(getColor(R.color.white));
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        toolbarback.setColorFilter(getColor(R.color.black));
        toolbartxt.setTextColor(getColor(R.color.black));
        itempoint.setTextColor(getColor(R.color.itempointpink));
        String savedHex = sp.getString("background", "#262627");
        if(sp.getString("thememode","default").equals("custom")) {
            layout.setBackgroundColor(Color.parseColor(savedHex));
            toolbar.setBackgroundColor(Color.parseColor(savedHex));
        }
        if(sp.getString("theme","dark").equals("pink")){
            getWindow().setStatusBarColor(getColor(R.color.statusbarcolorpink));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            toolbarback.setColorFilter(getColor(R.color.black));
            itempricetxt.setTextColor(getColor(R.color.black));
            itemnametxt.setTextColor(getColor(R.color.black));
            toolbartxt.setTextColor(getColor(R.color.black));
            buybutton.setBackgroundResource(R.drawable.loginbutpink);

            t.setBackgroundResource(R.drawable.border_light);
            if(!sp.getString("thememode","default").equals("custom")) {
                toolbar.setBackgroundColor(getColor(R.color.backgroundlight));
                layout.setBackgroundColor(getColor(R.color.backgroundlight));
            }
            t.setTextColor(getColor(R.color.white));
            e1.setTextColor(getColor(R.color.black));
           // e2.setTextColor(getColor(R.color.itempink));


        }

        else{
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(0);
            toolbarback.setColorFilter(getColor(R.color.white));
            toolbartxt.setTextColor(getColor(R.color.white));
            t.setTextColor(getColor(R.color.black));
            itempoint.setTextColor(getColor(R.color.itempointdark));
            itempricetxt.setTextColor(getColor(R.color.white));
            itemnametxt.setTextColor(getColor(R.color.white));
            t.setBackgroundResource(R.drawable.border3);
            getWindow().setNavigationBarColor(getColor(R.color.white));
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
            getWindow().setNavigationBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(0);
            //  e1.setHintTextColor(getColor(R.color.gray));
            e1.setTextColor(getColor(R.color.black));
            if(!sp.getString("thememode","default").equals("custom")) {
                toolbar.setBackgroundColor(getColor(R.color.backgrounddark));
                layout.setBackgroundColor(getColor(R.color.backgrounddark));
            }

        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences sharedPreferences=getSharedPreferences("hisaab",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("incoming_fromAnotherApp",false).apply();
        SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
        String user = spitem.getString("user","");
        sharedPreferences.edit().putBoolean(user+"allowapp",true).apply();
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences sharedPreferences=getSharedPreferences("hisaab",MODE_PRIVATE);
        sharedPreferences.edit().putBoolean("incoming_fromAnotherApp",false).apply();
        SharedPreferences spitem = getSharedPreferences("item", MODE_PRIVATE);
        String user = spitem.getString("user","");
        sharedPreferences.edit().putBoolean(user+"allowapp",false).apply();

    }


    public int month(String m) {
        if (m.equals("Jan")) return 1;
        else if (m.equals("Feb")) return 2;
        else if (m.equals("Mar")) return 3;
        else if (m.equals("Apr")) return 4;
        else if (m.equals("May")) return 5;
        else if (m.equals("June")) return 6;
        else if (m.equals("July")) return 7;
        else if (m.equals("Aug")) return 8;
        else if (m.equals("Sept")) return 9;
        else if (m.equals("Oct")) return 10;
        else if (m.equals("Nov")) return 11;
        else return 12;
    }



    private void checkOrientation() {
        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            displaysize.setfixWidth(itemLayout, 60);
            displaysize.setfixWidth(buybutton, 59);


        } else if (config.orientation == Configuration.ORIENTATION_PORTRAIT) {
            displaysize.setfixWidth(itemLayout, 90);
            displaysize.setfixWidth(buybutton, 89);

//            displaysize.setfixWidth(totalspend, PORTRAIT_TABLE_WIDTH);

        }
    }
}