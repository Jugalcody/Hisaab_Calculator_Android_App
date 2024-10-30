package hisaab.store.analyser;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.hisaabcalculator.R;

import java.util.Calendar;
import java.util.Random;

public class AlarmReceiver extends BroadcastReceiver {

int randomNumber=0;
String month,year;
    SharedPreferences spitem ;
    String user="";
    @Override
    public void onReceive(Context context, Intent intent) {
        displayNotification(context);
    }

    private void displayNotification(Context context) {
        // Get the current time
        Calendar now = Calendar.getInstance();
        int hour = now.get(Calendar.HOUR_OF_DAY);
        int min=now.get(Calendar.MINUTE);
        spitem= context.getSharedPreferences("item", context.MODE_PRIVATE);
        user = spitem.getString("user", "");
        Random random = new Random();
        randomNumber = random.nextInt(3) + 1;

        String[] arr={"January","February","March","April","May","June","July","August","Septempber","October","November","December"};
        month=arr[now.get(Calendar.MONTH)];
        year=String.valueOf(now.get(Calendar.YEAR));
        // Create an intent to open the app
        SharedPreferences sp = context.getSharedPreferences("login", Context.MODE_PRIVATE);
        Intent openAppIntent =null;

        if(sp.getBoolean("islogged", false)) {

            openAppIntent = new Intent(context, first.class);
        }else {
            openAppIntent=new Intent(context, MainActivity.class);
        }
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);  // Clear stack to open activity as a fresh task

        PendingIntent pendingIntent = null;
        Intent noIntent = new Intent(context, DismissNotificationReceiver.class);  // Custom receiver to dismiss notification
        PendingIntent noPendingIntent = PendingIntent.getBroadcast(context, 99, noIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Create Notification Builder
        NotificationCompat.Builder builder;
        String channelId = "notify1";  // Use the same channel ID for notifications

        // Determine the notification content based on the current time
        if (hour==6) {
            pendingIntent = PendingIntent.getActivity(context, 1, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }
        else if (hour==7) {
            pendingIntent = PendingIntent.getActivity(context, 2, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }
        else if (hour==8) {
            if(min==0) {
                pendingIntent = PendingIntent.getActivity(context, 3, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
            else{
                pendingIntent = PendingIntent.getActivity(context, 26, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
        }


        else if (hour==9) {
            if(min==0) {
                pendingIntent = PendingIntent.getActivity(context, 4, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }else{
                pendingIntent = PendingIntent.getActivity(context, 27, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
        }
        else if (hour==10) {
            if(min==0) {
                pendingIntent = PendingIntent.getActivity(context, 5, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }else{
                pendingIntent = PendingIntent.getActivity(context, 22, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
        }


        else if (hour == 13) {
            pendingIntent = PendingIntent.getActivity(context, 28, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }


        else if (hour==14) {
            pendingIntent = PendingIntent.getActivity(context, 8, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }


        else if (hour==12) {
            pendingIntent = PendingIntent.getActivity(context, 7, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        else if (hour==17) {
            if(min==0) {
                pendingIntent = PendingIntent.getActivity(context, 10, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
            else{
                pendingIntent = PendingIntent.getActivity(context, 24, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
        }
        else if (hour==18) {
            if(min==0) {
                pendingIntent = PendingIntent.getActivity(context, 11, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }else{
                pendingIntent = PendingIntent.getActivity(context, 19, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
        }



        else if (hour==16 || hour==11 || hour==20 || hour==23) {

            if(sp.getBoolean("islogged", false)) {

                openAppIntent = new Intent(context, display.class);
            }else {
                openAppIntent=new Intent(context, MainActivity.class);
            }

            if(hour==11) {
                pendingIntent = PendingIntent.getActivity(context, 6, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
            else if(hour==16){
                pendingIntent = PendingIntent.getActivity(context, 31, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
            else if(hour==23){
                pendingIntent = PendingIntent.getActivity(context, 16, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
            else {
                pendingIntent = PendingIntent.getActivity(context, 20, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
        }
        else if (hour==21) {

            if(sp.getBoolean("islogged", false)) {

                openAppIntent = new Intent(context, display.class);
            }else {
                openAppIntent=new Intent(context, MainActivity.class);
            }
            if(min==0) {
                pendingIntent = PendingIntent.getActivity(context, 14, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }else{
                pendingIntent = PendingIntent.getActivity(context, 23, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }
        }
        else if (hour==22) {
            pendingIntent = PendingIntent.getActivity(context, 15, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }




        else if (hour==1) {
            pendingIntent = PendingIntent.getActivity(context, 17, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        else if (hour==5) {
            pendingIntent = PendingIntent.getActivity(context, 18, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }
else if(hour==0){
            pendingIntent = PendingIntent.getActivity(context, 34, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        else{

            if(sp.getBoolean("islogged", false)) {

                openAppIntent = new Intent(context, display.class);
            }else {
                openAppIntent=new Intent(context, MainActivity.class);
            }
            pendingIntent = PendingIntent.getActivity(context, 18, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
        }

        builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.icon2)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_ALARM)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setAutoCancel(false);

        if(hour==19 || hour==15){
            if(randomNumber==1){
                openAppIntent = new Intent(context,monthly.class);
                if(hour==15) {
                    if (min == 0) {
                        pendingIntent = PendingIntent.getActivity(context, 9, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    } else {
                        pendingIntent = PendingIntent.getActivity(context, 21, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    }
                }
                else{
                    pendingIntent = PendingIntent.getActivity(context, 12, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                }
                builder.setContentText("Tap to view your total expenses in this month.")
                        .setContentTitle("You spent Rs."+spitem.getString("monthlyspent"+user,"0")+" in this month.")
                        .setContentIntent(pendingIntent);
            }
            else if(randomNumber==2){
                openAppIntent = new Intent(context,Yearly.class);
                if(hour==15) {
                    if (min == 0) {
                        pendingIntent = PendingIntent.getActivity(context, 9, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    } else {
                        pendingIntent = PendingIntent.getActivity(context, 21, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    }
                }
                else{
                    pendingIntent = PendingIntent.getActivity(context, 12, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                }

                SharedPreferences sharedPreferences=context.getSharedPreferences(user+year, Context.MODE_PRIVATE);
                String balyear=sharedPreferences.getString("balance","0");
                builder.setContentText("Tap to view your total expenses in "+year)
                        .setContentTitle("Your total yearly spent is Rs."+balyear)
                        .setContentIntent(pendingIntent);
            }
            else {
                openAppIntent = new Intent(context,display.class);
                if(hour==15) {
                    if (min == 0) {
                        pendingIntent = PendingIntent.getActivity(context, 9, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    } else {
                        pendingIntent = PendingIntent.getActivity(context, 21, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                    }
                }
                else{
                    pendingIntent = PendingIntent.getActivity(context, 12, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
                }
                builder.setContentTitle("Check how much money you spent today!")
                        .setContentText("Tap to view and verify your data")
                        .setContentIntent(pendingIntent);
            }
        }
       else if (hour >= 8 && hour <11) {
            if (randomNumber == 1) {
                builder.setContentTitle("Have you spent money on any services?")
                        .setContentText("If yes then add the amount to the list")
                        .addAction(R.drawable.baseline_close_24, "Yes I spent", pendingIntent)
                        .addAction(R.drawable.baseline_close_24, "No", noPendingIntent);

            } else if (randomNumber == 2) {
                builder.setContentTitle("You spent Rs." + spitem.getString("monthlyspent" + user, "0") + " in this month!");

            } else {
                builder.setContentTitle("Hisaab Analyser is here for you!")
                        .setContentText("Please add daily expenses so that it can provide accurate hisaab to you.")
                        .addAction(R.drawable.baseline_close_24, "Yes I brought", pendingIntent)
                        .addAction(R.drawable.baseline_close_24, "No", noPendingIntent);
            }
        }
          else if (hour == 11 || hour==16 || hour==20 || hour==23) {
              builder.setContentText("Tap to view your monthly expenses!")
            .setContentTitle("You spent Rs."+spitem.getString("monthlyspent"+user,"0")+" in this month.");
            }
         else if ((hour >= 12 && hour < 16) || (hour>=17 && hour<20)) {

            if(randomNumber==0){
                builder.setContentTitle("Have you bought any item today?")
                        .setContentText("Add items to the list for the correct analysis of your expenses")
                        .addAction(R.drawable.baseline_close_24, "Yes I brought", pendingIntent)
                        .addAction(R.drawable.baseline_close_24, "No", noPendingIntent);
            }
            else if(randomNumber==1){
                builder.setContentText("Tap to view your monthly expenses!")
                        .setContentTitle("You spent Rs."+spitem.getString("monthlyspent"+user,"0")+" in this month.");

            }
            else if(randomNumber==2){
                builder.setContentTitle("Update your daily expenses!")
                        .setContentText("Add items to the list for the correct analysis of your expenses")
                        .addAction(R.drawable.baseline_close_24, "Yes", pendingIntent)
                        .addAction(R.drawable.baseline_close_24, "No", noPendingIntent);
            }
            else{
                builder.setContentTitle("Have you spent money on any services?")
                        .setContentText("If yes then add the amount to the list")
                        .addAction(R.drawable.baseline_close_24, "Yes I spent", pendingIntent)
                        .addAction(R.drawable.baseline_close_24, "No", noPendingIntent);
            }

        } else if (hour==21 ) {
            // Between 5 PM and 6 AM

            if(randomNumber==0){
                builder.setContentTitle("What items you bought today?")
                        .setContentText("Tap to view and verify your data");
            }
            else{
                builder.setContentTitle("View your today's expenses")
                        .setContentText("Check and verify the list of items you bought today");


            }

    }/* else if (hour >= 20 && hour < 22) {
        // Between 5 PM and 6 AM

        builder = new NotificationCompat.Builder(context, channelId)
                .setSmallIcon(R.mipmap.icon2)
                .setContentTitle("Do you know your total expenses?")
                .setContentText("View your daily,monthly and yearly expenses")
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(false);
    }*/
        else{


            if(randomNumber==0){
                builder.setContentTitle("Check How Much Money You Spent Today!")
                        .setContentText("Tap to view and verify your data")
                        .setContentIntent(pendingIntent);
            }
            else{
                builder.setContentTitle("Check your total expenses!")
                        .setContentText("Tap to view your daily, monthly and yearly expenses")
                        .setContentIntent(pendingIntent);
            }
        }
builder.setFullScreenIntent(pendingIntent,true);
        // Create or update the notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId, "Notification Channel", NotificationManager.IMPORTANCE_HIGH);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }

        // Send the notification
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // Permission is not granted, so we do nothing
            return;
        }
        notificationManager.notify(1, builder.build());
    }

    public static class DismissNotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.cancel(1);  // Dismiss the notification with ID 1
        }
    }
}
