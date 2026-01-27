package hisaab.store.analyser;

import android.app.Activity;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.ads.AdError;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.FullScreenContentCallback;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;

public class ShowInterstitialAd {

    private InterstitialAd mInterstitialAd;
    Activity context;
    private int flag = 0;
    private String msg = "";
    private final String adId = "ca-app-pub-6363323342247820/5269748362";

    public ShowInterstitialAd(Activity context) {
        this.context = context;
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;

                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        // Called when a click is recorded for an ad
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        mInterstitialAd = null;
                        if (flag == 1) {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                            flag = 0;
                            msg = "";
                        }
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                    }

                    @Override
                    public void onAdImpression() {
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        context.getSharedPreferences("item", Activity.MODE_PRIVATE)
                                .edit()
                                .putInt("adcount", 0)
                                .apply();
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                mInterstitialAd = null;
            }
        });
    }

    public void showad() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(context);
        } else {
            // Ad not loaded
        }
    }

    public void setFlag(int flag, String msg) {
        this.flag = flag;
        this.msg = msg;
    }
}
