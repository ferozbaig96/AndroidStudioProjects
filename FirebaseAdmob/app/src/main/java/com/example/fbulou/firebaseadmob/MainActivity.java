package com.example.fbulou.firebaseadmob;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class MainActivity extends AppCompatActivity {

    AdView mAdView;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        MobileAds.initialize(getApplicationContext(), "ca-app-pub-3330667214297818~9762297080");

        mAdView = (AdView) findViewById(R.id.adView);

        //Real todo
        /*AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        //Testing
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("76A4FF6E29CECF3C463CD4720D239706")  // My Genymotion Emulator
                .addTestDevice("A94AC3FA9FD96F6C489431C392DD31D7")  // My HTC Desire 310 Real device
                .build();

        mAdView.loadAd(adRequest);

        setupMyInterstitialAd();
    }

    private void setupMyInterstitialAd() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3330667214297818/3480261089");

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                requestNewInterstitial();
                beginPlayingGame();
            }
        });

        requestNewInterstitial();

        findViewById(R.id.btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    beginPlayingGame();
                }
            }
        });

    }

    private void requestNewInterstitial() {
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)        // All emulators
                .addTestDevice("76A4FF6E29CECF3C463CD4720D239706")  // My Genymotion Emulator
                .addTestDevice("A94AC3FA9FD96F6C489431C392DD31D7")  // My HTC Desire 310 Real device
                .build();

        mInterstitialAd.loadAd(adRequest);
    }

    private void beginPlayingGame() {
        // Play for a while, then display the New Game Button

        //or call second Activity
    }

    /** Called when leaving the activity */
    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    /** Called when returning to the activity */
    @Override
    public void onResume() {
        super.onResume();

        if (mAdView != null) {
            mAdView.resume();
        }

        if (!mInterstitialAd.isLoaded()) {
            requestNewInterstitial();
        }
    }

    /** Called before the activity is destroyed */
    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

}
