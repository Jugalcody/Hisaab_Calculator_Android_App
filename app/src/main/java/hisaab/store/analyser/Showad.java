package hisaab.store.analyser;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

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

import java.util.Random;

public class Showad {
    private RewardedAd rewardedAd;
    Activity context;
    int point=0;

    Showad(Activity context){
        this.context=context;
    }



    public void load(){
        Random random = new Random();
        int randomNumber = random.nextInt(2);
        AdRequest adRequest = new AdRequest.Builder().build();
        String adunit="";
        if(randomNumber==0){
            adunit="ca-app-pub-1079506490540577/9563671110";
        }
        else{
            adunit="ca-app-pub-1079506490540577/9051232858";
        }
        RewardedAd.load(context, adunit,
                adRequest, new RewardedAdLoadCallback() {
                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        //   Toast.makeText(item.this,loadAdError.toString(),Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAdLoaded(@NonNull RewardedAd ad) {
                        // Toast.makeText(item.this,"loaded",Toast.LENGTH_SHORT).show();
                        rewardedAd=ad;

                    }
                });

    }


    public void loadRewardad(){
        MobileAds.initialize(context, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(@NonNull InitializationStatus initializationStatus) {
                load();

            }
        });

    }


    public RewardedAd getRewardedAd() {
        return rewardedAd;
    }

    public int getPoint() {
        return point;
    }

    public void showRewardedAd(TextView itempoint){
        Activity activityContext = context;
        rewardedAd.show(activityContext, new OnUserEarnedRewardListener() {
            @Override
            public void onUserEarnedReward(@NonNull RewardItem rewardItem) {
                SharedPreferences spitem = context.getSharedPreferences("item", context.MODE_PRIVATE);
                String head = spitem.getString("user", "");
                SharedPreferences coin=context.getSharedPreferences(head+"coin",context.MODE_PRIVATE);
                int curpoint=coin.getInt("point",0)+10;

                coin.edit().putInt("point",curpoint).apply();
                point=coin.getInt("point",0);

                Toast.makeText(context,"You got 10+ rewards",Toast.LENGTH_LONG).show();
                itempoint.setText(point+"P");

            }
        });


        rewardedAd.setFullScreenContentCallback(new FullScreenContentCallback() {
            @Override
            public void onAdClicked() {

            }

            @Override
            public void onAdDismissedFullScreenContent() {
                // Called when ad is dismissed.
                // Set the ad reference to null so you don't show the ad a second time.

                load();


            }

            @Override
            public void onAdFailedToShowFullScreenContent(AdError adError) {
                // Called when ad fails to show.
            }

            @Override
            public void onAdImpression() {
            }

            @Override
            public void onAdShowedFullScreenContent() {
                // Called when ad is shown.
                Toast.makeText(context,"Wait for 30 sec to earn reward!",Toast.LENGTH_LONG).show();
            }
        });


    }



    public void createWindow(Activity context,LinearLayout layout,TextView parentpoint) {


        ViewGroup root=(ViewGroup) context.getWindow().getDecorView().getRootView();

        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.clickforad_design, null);

        ImageView pback=popup.findViewById(R.id.popup_back);
        AppCompatButton gotit=popup.findViewById(R.id.popup_warn_gotit);
        SharedPreferences spitem = context.getSharedPreferences("item", context.MODE_PRIVATE);
        ProgressBar progressBar=popup.findViewById(R.id.adprogress);
        TextView adbut=popup.findViewById(R.id.adbut);
        TextView itempoint=popup.findViewById(R.id.item_point_window);
        AppCompatButton watchad=popup.findViewById(R.id.watchad);
        AdRequest adRequest = new AdRequest.Builder().build();
        AdView adView=popup.findViewById(R.id.adView);
        adView.loadAd(adRequest);
        String adtxt="Get 10+ points after watching ads >";
        SpannableString content = new SpannableString(adtxt);
        content.setSpan(new UnderlineSpan(), 0,adtxt.length(), 0);
        adbut.setText(content);

        String head = spitem.getString("user", "");
        SharedPreferences coin=context.getSharedPreferences(head+"coin",context.MODE_PRIVATE);
        itempoint.setText(coin.getInt("point",0)+"P");



        Display display = context.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x - 55;
        int height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // Create and show the PopupWindow
        PopupWindow popupWindow = new PopupWindow(popup,
                width,
                height,
                true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (motionEvent.getY() < height || motionEvent.getY() >view.getBottom()) return true;
                return false;
            }
        });
        popupWindow.update();

        adbut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coin.getInt("point",0)<=50) {
                    if (rewardedAd != null) {
                        showRewardedAd(itempoint);

                    } else {
                        loadRewardad();
                        if (rewardedAd != null) {
                            showRewardedAd(itempoint);
                        } else {
                            if (!isInternetAvailable()) {
                                Toast.makeText(context, "No internet to load ads!", Toast.LENGTH_LONG).show();
                            } else {
                                if (coin.getInt("point", 0) == 0) {
                                    loadRewardad();
                                    progressBar.setVisibility(View.VISIBLE);
                                    if (rewardedAd == null) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                progressBar.setVisibility(View.GONE);
                                                if(rewardedAd==null) {
                                                    Toast.makeText(context, "Unable to load ad! granted 10+ reward", Toast.LENGTH_LONG).show();
                                                    coin.edit().putInt("point", 10).apply();
                                                    parentpoint.setText(coin.getInt("point", 0) + "P");
                                                    itempoint.setText(coin.getInt("point", 0) + "P");
                                                    popupWindow.dismiss();
                                                }
                                                else{
                                                    showRewardedAd(itempoint);
                                                }
                                            }
                                        }, 6000);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        showRewardedAd(itempoint);
                                    }
                                } else {
                                    if (!isInternetAvailable()) {
                                        Toast.makeText(context, "No internet to load ads!", Toast.LENGTH_LONG).show();
                                    }
                                    else {

                                        if (rewardedAd == null) {
                                            loadRewardad();
                                            progressBar.setVisibility(View.VISIBLE);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar.setVisibility(View.GONE);
                                                    if(rewardedAd==null){
                                                        Toast.makeText(context, "Unable to load try again!", Toast.LENGTH_LONG).show();
                                                    }
                                                    else{
                                                        showRewardedAd(itempoint);
                                                    }
                                                }
                                            }, 3000);

                                        } else {
                                            showRewardedAd(itempoint);
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                else{
                        Toast.makeText(context, "You have enough points!", Toast.LENGTH_SHORT).show();
                    }



            }
        });
        watchad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(coin.getInt("point",0)<=50) {
                    if (rewardedAd != null) {
                        showRewardedAd(itempoint);

                    } else {
                        loadRewardad();
                        if (rewardedAd != null) {
                            showRewardedAd(itempoint);
                        } else {
                            if (!isInternetAvailable()) {
                                Toast.makeText(context, "No internet to load ads!", Toast.LENGTH_LONG).show();
                            } else {
                                if (coin.getInt("point", 0) == 0) {
                                    loadRewardad();
                                    progressBar.setVisibility(View.VISIBLE);
                                    if (rewardedAd == null) {
                                        loadRewardad();
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(rewardedAd==null) {
                                                    Toast.makeText(context, "Unable to load ad! granted 10+ reward", Toast.LENGTH_LONG).show();
                                                    coin.edit().putInt("point", 10).apply();
                                                    parentpoint.setText(coin.getInt("point", 0) + "P");
                                                    itempoint.setText(coin.getInt("point", 0) + "P");
                                                    popupWindow.dismiss();
                                                }
                                                else{
                                                    showRewardedAd(itempoint);
                                                }
                                            }
                                        }, 6000);
                                    } else {
                                        progressBar.setVisibility(View.GONE);
                                        showRewardedAd(itempoint);
                                    }
                                } else {
                                    if (!isInternetAvailable()) {
                                        Toast.makeText(context, "No internet to load ads!", Toast.LENGTH_LONG).show();
                                    }
                                    else {

                                        if (rewardedAd == null) {
                                            loadRewardad();
                                            progressBar.setVisibility(View.VISIBLE);
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    progressBar.setVisibility(View.GONE);
                                                    if(rewardedAd==null){
                                                        Toast.makeText(context, "Unable to load try again!", Toast.LENGTH_LONG).show();
                                                    }
                                                    else{
                                                        showRewardedAd(itempoint);
                                                    }
                                                }
                                            }, 3000);

                                        } else {
                                            showRewardedAd(itempoint);
                                        }
                                    }
                                }

                            }
                        }
                    }
                }
                else{
                    Toast.makeText(context, "You have enough points!", Toast.LENGTH_SHORT).show();
                }



            }
        });
        gotit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                clearDim(root);
                parentpoint.setText(coin.getInt("point",0)+"P");
            }
        });

        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

        layout.post(new Runnable() {
            @Override
            public void run() {

                applyDim(root,0.5f);
                popupWindow.showAtLocation(layout, Gravity.CENTER,0,0);


            }
        });



    }


    public  void applyDim(@NonNull ViewGroup parent, float dimAmount){
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

    public boolean isInternetAvailable() {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
