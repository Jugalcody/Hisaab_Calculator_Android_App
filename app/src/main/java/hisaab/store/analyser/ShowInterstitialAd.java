package hisaab.store.analyser;

import android.app.Activity;

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
    private final String adId = "ca-app-pub-1079506490540577/1788293269";

    public ShowInterstitialAd(Activity context) {
        this.context = context;
    }

    public void loadAd() {
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, adId, adRequest, new InterstitialAdLoadCallback() {
            @Override
            public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                mInterstitialAd = interstitialAd;

                // Set FullScreenContentCallback to handle ad events
                mInterstitialAd.setFullScreenContentCallback(new FullScreenContentCallback() {
                    @Override
                    public void onAdClicked() {
                        // Called when a click is recorded for an ad
                    }

                    @Override
                    public void onAdDismissedFullScreenContent() {
                        // Set the ad reference to null so it can be reloaded
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdFailedToShowFullScreenContent(AdError adError) {
                        mInterstitialAd = null;
                    }

                    @Override
                    public void onAdImpression() {
                        // Called when an impression is recorded for an ad
                    }

                    @Override
                    public void onAdShowedFullScreenContent() {
                        // Called when ad is shown
                    }
                });
            }

            @Override
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                // Handle the error
                mInterstitialAd = null;
            }
        });
    }

    public void showad() {
        if (mInterstitialAd != null) {
            mInterstitialAd.show(context);
        } else {
            // Optionally, you can log or notify that the ad wasn't loaded
        }
    }
}
