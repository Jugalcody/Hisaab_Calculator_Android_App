package hisaab.store.analyser;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.SharedPreferences;
import android.content.res.Configuration;
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

import com.example.hisaabcalculator.R;
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
            config = getResources().getConfiguration();
            parentlayout=findViewById(R.id.item_container);
            ad=new Showad(item.this);
            ad.loadRewardad();

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
                                monthUpdate(Long.parseLong(price));
                                yearUpdate(Long.parseLong(price));
                                t.setText("Available Balance : " + deductMoney(Long.parseLong(price)));
                                e1.setText("");
                                e2.setText("");
                                spitem.edit().putString("monthlyspent"+head,monthlySpend()).apply();

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

    public long deductMoney(long p){
        String date3 = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        int year = Integer.parseInt(date3);
        SharedPreferences sharedPreferences=getSharedPreferences(head+year,MODE_PRIVATE);
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
        sharedPreferences.edit().putString("balance",String.valueOf(bal3)).apply();
        return bal3;
    }

    public void monthUpdate(long p) {
        File path=getApplicationContext().getFilesDir();
        try {
            String date = new SimpleDateFormat("MMyyyy", Locale.getDefault()).format(new Date());
            int d = Integer.parseInt(date);
            File file=new File(path, head+"monthlySpend"+d+".txt");
            if(!file.exists()) file.createNewFile();

            FileInputStream f3 = new FileInputStream(file);

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            long l = Long.parseLong(b5) + p;
            String b3 =Long.toString(l);
            FileOutputStream f6 = new FileOutputStream(file);
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
            File f =null;
            f=new File(path, (head+"yearlySpend" + year+ ".txt"));
            if (!f.exists()) f.createNewFile();
            FileInputStream f3 = new FileInputStream(f);

            InputStreamReader rr = new InputStreamReader(f3);
            BufferedReader brr = new BufferedReader(rr);
            String b5 = brr.readLine();
            if (b5 == null) b5 = "0";
            f3.close();


            long l = Long.parseLong(b5) + p;
            String b3 =Long.toString(l);
            f =new File(path, (head+"yearlySpend" + year+ ".txt"));
            if (!f.exists()) f.createNewFile();
            FileOutputStream f6 = new FileOutputStream(f);
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
            File file= new File(path,head+"monthlySpend"+d+".txt");
            FileOutputStream f=new FileOutputStream(file);
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

                File file=new File(path,head+"hisaab"+mon+year+"data.txt");
                if(!file.exists()) file.createNewFile();
                FileOutputStream f =new FileOutputStream(file,true);
                f.write(k.getBytes());
                buybutton.setText("commit");
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
        if(sp.getString("theme","dark").equals("pink")){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.toolbarpink));
            }
            buybutton.setBackgroundResource(R.drawable.loginbutpink);
            t.setBackgroundResource(R.drawable.border_light);
            toolbar.setBackgroundColor(getColor(R.color.toolbarpink));
            layout.setBackgroundColor(getColor(R.color.backgroundpink));
            t.setTextColor(getColor(R.color.white));
            e1.setTextColor(getColor(R.color.itempink));
            e1.setHintTextColor(getColor(R.color.backgroundpink));


        }

        else{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getColor(R.color.primarydark));
            }
            t.setTextColor(getColor(R.color.black));
            t.setBackgroundResource(R.drawable.border3);
            toolbar.setBackgroundColor(getColor(R.color.toolbardark));
            e1.setTextColor(getColor(R.color.black));
            e1.setHintTextColor(getColor(R.color.gray));
            layout.setBackgroundColor(getColor(R.color.backgrounddark));

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

    public String monthlySpend() {
        File path = getApplicationContext().getFilesDir();
        String b4 = "0";
        int m;
        try {
            m = month(mon);

            FileInputStream f2 = new FileInputStream(new File(path, head+"monthlySpend" + m + year + ".txt"));
            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if (b4 == null) b4 = "0";
            f2.close();
        } catch (IOException e) {
            Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
        }
        return b4;
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