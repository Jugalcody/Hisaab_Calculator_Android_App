package hisaab.store.analyser;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {


    public void applyDim(@NonNull ViewGroup parent, float dimAmount) {
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, 0, parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * dimAmount));
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.add(dim);
    }

    public static void clearDim(@NonNull ViewGroup parent) {
        ViewGroupOverlay overlay = parent.getOverlay();
        overlay.clear();
    }
    public static Bitmap decodeStringToBitmap(String encodedBitmap) {
        byte[] decoded = Base64.decode(encodedBitmap, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decoded, 0, decoded.length);
    }




    public void monthUpdate(long p,Activity activity,String head,EditText e2) {
        File path=activity.getApplicationContext().getFilesDir();
        String[] arr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        try {
            String mon = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
            String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
            File file = new File(path, head+"monthlySpend" + arr[Integer.parseInt(mon)-1]+year+ ".txt");
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
            clear3(activity,head);
            String price=e2.getText().toString();
            monthUpdate(Long.parseLong(price),activity,head,e2);
            e.printStackTrace();
        }
    }



    public void yearUpdate(long p,Activity activity,String year,String head,EditText e2) {
        File path=activity.getApplicationContext().getFilesDir();
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
            clear3(activity,head);
            String price=e2.getText().toString();
            yearUpdate(Long.parseLong(price),activity,year,head,e2);
            e.printStackTrace();
        }
    }


    public void clear3(Activity activity,String head) {
        String[] arr = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
        String mon = new SimpleDateFormat("MM", Locale.getDefault()).format(new Date());
        String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(new Date());
        File path=activity.getApplicationContext().getFilesDir();
        try{

            File file = new File(path, head+"monthlySpend" + arr[Integer.parseInt(mon)-1]+year+ ".txt");
            FileOutputStream f=new FileOutputStream(file);
            f.write("".getBytes());

        }
        catch(IOException e){
            Toast.makeText(activity,"unable to clear",Toast.LENGTH_LONG).show();
        }
    }


    public void createWindowname(Activity activity,ViewGroup root,TextView textView) {
        try {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View popup = inflater.inflate(R.layout.change_namelayoutdesign, null);

            AppCompatButton changebut = popup.findViewById(R.id.changename_but);
            EditText newname = popup.findViewById(R.id.changename_edit);
            ImageView close = popup.findViewById(R.id.changename_close);
            AdRequest adRequest = new AdRequest.Builder().build();
            AdView adView = popup.findViewById(R.id.adViewchangename);
            adView.loadAd(adRequest);

            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            int width = size.x - 55;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;

            PopupWindow popupWindow = new PopupWindow(popup);
            popupWindow.setWidth(width);
            popupWindow.setHeight(height);
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getY() < height || motionEvent.getY() > view.getBottom())
                        return true;
                    return false;
                }
            });
            popupWindow.update();
            close.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });
            changebut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String nm = newname.getText().toString().trim();
                    newname.setText("");
                    changeName(nm,activity,textView);
                    popupWindow.dismiss();
                }
            });


            popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    clearDim(root);
                }
            });

            root.post(() -> {
                applyDim((ViewGroup) root, 0.5f);
                popupWindow.showAtLocation(root, Gravity.CENTER, 0, 0);
            });

        } catch (Exception e) {
            Toast.makeText(activity, e.toString(), Toast.LENGTH_LONG).show();
        }
    }


    public void changeName(String newname,Activity activity,TextView username) {
        SharedPreferences sptheme = activity.getSharedPreferences("theme", Context.MODE_PRIVATE);
        SharedPreferences splogin = activity.getSharedPreferences("login", Context.MODE_PRIVATE);
        SharedPreferences spitem = activity.getSharedPreferences("item", Context.MODE_PRIVATE);
        Context context = activity;
        File path = context.getFilesDir();
        File file = new File(path, "valid2.txt");
        File tempFile = new File(path, "valid2_temp.txt");

        if (!file.exists()) {
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
            return;
        }

        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(tempFile)))) {

            String line;
            boolean found = false;

            while ((line = br.readLine()) != null) {
                String[] arr = line.split("_");

                if (arr.length >= 5 && arr[0].equals(splogin.getString("user",""))) {
                    arr[2] = newname;
                    arr[4] = newname;
                    bw.write(String.join("_", arr));
                    found = true;
                } else {
                    bw.write(line);
                }
                bw.newLine();
            }

        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(context, "IO exception: " + e, Toast.LENGTH_LONG).show();
            return;
        }

        if (file.delete() && tempFile.renameTo(file)) {
            Toast.makeText(context, "Name successfully changed", Toast.LENGTH_SHORT).show();



            spitem.edit().putString("name", newname).apply();
            splogin.edit().putString("fullname", newname).apply();

            if(username!=null)
            username.setText("Hii " + newname + ",");

            first.viewModel.setUserName(newname);

        } else {
            Toast.makeText(context, "Failed to change username", Toast.LENGTH_LONG).show();
        }
    }


    public int getMonthIndex(String m){
        if(m.equals("Jan")) return 1;
        else if(m.equals("Feb")) return 2;
        else if(m.equals("Mar")) return 3;
        else if(m.equals("Apr")) return 4;
        else if(m.equals("May")) return 5;
        else if(m.equals("June")) return 6;
        else if(m.equals("July")) return 7;
        else if(m.equals("Aug")) return 8;
        else if(m.equals("Sept")) return 9;
        else if(m.equals("Oct")) return 10;
        else if(m.equals("Nov")) return 11;
        else return 12;
    }


    public String getMonthName(int m) {
        switch (m) {
            case 1 : return "Jan";
            case 2: return "Feb";
            case 3: return "Mar";
            case 4: return "Apr";
            case 5: return "May";
            case 6: return "June";
            case 7: return "July";
            case 8: return "Aug";
            case 9: return "Sept";
            case 10: return "Oct";
            case 11: return "Nov";
            case 12: return "Dec";
            default: return "";
        }
    }




    public static void buttonEffect(View button) {
        button.setOnTouchListener(new View.OnTouchListener() {

            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        v.getBackground().setColorFilter(Color.green(R.color.icontapeffect), PorterDuff.Mode.SRC_OVER);
                        v.invalidate();
                        v.animate();
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getBackground().clearColorFilter();
                        v.invalidate();
                        break;
                    }
                    default:
                        v.getBackground().clearColorFilter();
                }
                return false;
            }
        });
    }

    public String monthlyGet(String m,Activity activity,String year) {
        SharedPreferences spitem = activity.getSharedPreferences("item", Context.MODE_PRIVATE);
        String head=spitem.getString("user","");
        File path = activity.getApplicationContext().getFilesDir();
        String b4 = "0";

        try {
            File file = new File(path, head + "monthlyGet" + m + year + ".txt");
            if (!file.exists()) file.createNewFile();
            FileInputStream f2 = new FileInputStream(file);

            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if (b4 == null) b4 = "0";
            f2.close();
        } catch (IOException e) {
            Toast.makeText(activity, "no data found", Toast.LENGTH_LONG).show();
        }
        return b4;
    }



    public long deductMoney(long p,Activity activity) {
        SharedPreferences spitem = activity.getSharedPreferences("item", Context.MODE_PRIVATE);
        String head=spitem.getString("user","");
        File path = activity.getApplicationContext().getFilesDir();
        long bal3 = 0;
        try {
            FileInputStream f = new FileInputStream(new File(path, head + "balance.txt"));
            InputStreamReader r = new InputStreamReader(f);
            BufferedReader br = new BufferedReader(r);
            String bal = br.readLine();
            if (bal == null) bal = "0";
            bal3 = Long.parseLong(bal) - p;
            f.close();
            FileOutputStream f2 = new FileOutputStream(new File(path, head + "balance.txt"));
            f2.write(Long.toString(bal3).getBytes());
            f2.close();

        } catch (IOException e) {
            //e.printStackTrace();
        }
        return bal3;
    }


    public String yearlyGet(String year,Activity activity){
        SharedPreferences spitem = activity.getSharedPreferences("item", Context.MODE_PRIVATE);
        String head=spitem.getString("user","");
        File path=activity.getApplicationContext().getFilesDir();
        String b4="0";

        try{
            File file=new File(path,head+"yearlyGet"+year+".txt");
            if(!file.exists()) file.createNewFile();
            FileInputStream f2=new FileInputStream(file);

            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if(b4==null) b4="0";
            f2.close();
        }
        catch(IOException e){
            Toast.makeText(activity,"no data found ",Toast.LENGTH_LONG).show();
        }
        return b4;
    }

    public String yearlySpend(String year,Activity activity){
        SharedPreferences spitem = activity.getSharedPreferences("item", Context.MODE_PRIVATE);
        String head=spitem.getString("user","");
        File path=activity.getApplicationContext().getFilesDir();
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
            Toast.makeText(activity,"no data found",Toast.LENGTH_LONG).show();
        }
        return b4;
    }


    public String totalBalance(Activity activity) {
        SharedPreferences spitem = activity.getSharedPreferences("item", Context.MODE_PRIVATE);
        String head=spitem.getString("user","");
        File path=activity.getApplicationContext().getFilesDir();
        String b3="0";
        try{
            FileInputStream f2=new FileInputStream(new File(path,head+"balance.txt"));
            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b3 = br.readLine();
            if(b3==null) b3="0";
            f2.close();


        }
        catch(IOException e){

            Toast.makeText(activity,"empty balance",Toast.LENGTH_LONG).show();
        }
        return b3;
    }


    public void applyDialogTheme(AlertDialog alerts,Activity activity,String key) {
        SharedPreferences sp = activity.getSharedPreferences("theme", activity.MODE_PRIVATE);

        if (sp.getString("theme", "dark").equals("pink")) {

            alerts.getWindow().setBackgroundDrawableResource(R.drawable.border6);

            int white = ContextCompat.getColor(activity, R.color.primarylight);
            alerts.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(white);
            alerts.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(white);



            try {
                ImageView icon = alerts.findViewById(android.R.id.icon);
                if (icon != null) {
                    if(key.equals("balance")) {
                        icon.setImageTintList(ColorStateList.valueOf(
                                ContextCompat.getColor(activity, android.R.color.holo_green_dark)
                        ));
                    }
                }
            } catch (Exception ignored) {
            }

            TextView alertTitle;
            int titleId = activity.getResources().getIdentifier("alertTitle", "id", activity.getPackageName());
            alertTitle = alerts.findViewById(titleId);
            if (alertTitle != null) {
                alertTitle.setTextColor(Color.BLACK);
            }


            TextView message = alerts.findViewById(android.R.id.message);
            if (message != null) message.setTextColor(Color.BLACK);

        } else {

            try {
                alerts.getWindow().setBackgroundDrawableResource(R.drawable.border6_darkgray);
                ImageView icon = alerts.findViewById(android.R.id.icon);
                if (icon != null) {
                    if(key.equals("balance")) {
                        icon.setImageTintList(ColorStateList.valueOf(
                                ContextCompat.getColor(activity, android.R.color.holo_green_dark)
                        ));
                    }
                }
            } catch (Exception ignored) {
            }

            int white = ContextCompat.getColor(activity, android.R.color.white);
            alerts.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(white);
            alerts.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(white);

            TextView title = alerts.findViewById(android.R.id.title);
            if (title != null) title.setTextColor(white);

            TextView message = alerts.findViewById(android.R.id.message);
            if (message != null) message.setTextColor(white);
        }
    }




    public String monthlySpend(String m,Activity activity,String year) {
        SharedPreferences spitem = activity.getSharedPreferences("item", Context.MODE_PRIVATE);
        String head=spitem.getString("user","");
        File path = activity.getApplicationContext().getFilesDir();
        String b4 = "0";
        try {

            File monthlyfile = new File(path, head + "monthlySpend" + m + year + ".txt");
            if (!monthlyfile.exists()) monthlyfile.createNewFile();
            FileInputStream f2 = new FileInputStream(monthlyfile);
            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if (b4 == null) b4 = "0";
            f2.close();
        } catch (IOException e) {
            Toast.makeText(activity, "no data found", Toast.LENGTH_LONG).show();
        }
        return b4;
    }


    public String monthlySpend2(String m,Context activity,String year) {
        SharedPreferences spitem = activity.getSharedPreferences("item", Context.MODE_PRIVATE);
        String head=spitem.getString("user","");
        File path = activity.getApplicationContext().getFilesDir();
        String b4 = "0";
        try {

            File monthlyfile = new File(path, head + "monthlySpend" + m + year + ".txt");
            if (!monthlyfile.exists()) monthlyfile.createNewFile();
            FileInputStream f2 = new FileInputStream(monthlyfile);
            InputStreamReader r = new InputStreamReader(f2);
            BufferedReader br = new BufferedReader(r);
            b4 = br.readLine();
            if (b4 == null) b4 = "0";
            f2.close();
        } catch (IOException e) {
            Toast.makeText(activity, "no data found", Toast.LENGTH_LONG).show();
        }
        return b4;
    }

}




