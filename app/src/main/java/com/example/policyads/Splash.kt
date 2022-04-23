package com.example.policyads

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class Splash : AppCompatActivity() {
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        loadAds()
        Handler().postDelayed({
            if (mInterstitialAd == null && (!isFinishing || !isDestroyed)) {
                openMain()
            }
        }, 7000)
    }


    private fun loadAds() {
        val adRequest = AdRequest.Builder().build()
        InterstitialAd.load(this, "ca-app-pub-3940256099942544/1033173712", adRequest,
            object : InterstitialAdLoadCallback() {
                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    // The mInterstitialAd reference will be null until
                    // an ad is loaded.
                    mInterstitialAd = interstitialAd
                    callbackInter()
                }

                override fun onAdFailedToLoad(loadAdError: LoadAdError) {
                    Log.d("Huy", "onAdFailedToLoad: ")
                    // Handle the error
                    mInterstitialAd = null
                    openMain()
                }
            })
    }

    private fun callbackInter() {
        mInterstitialAd?.setFullScreenContentCallback(object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                // Called when fullscreen content is dismissed.
                Log.d("TAG", "The ad was dismissed.")
                openMain()
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                // Called when fullscreen content failed to show.
                Log.d("TAG", "The ad failed to show.")
                openMain()
            }

            override fun onAdShowedFullScreenContent() {
                // Called when fullscreen content is shown.
                // Make sure to set your reference to null so you don't
                // show it a second time.
                Log.d("TAG", "The ad was shown.")
            }
        })
        if (mInterstitialAd != null && !isFinishing && !isDestroyed) {
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "The interstitial ad wasn't ready yet.")
        }
    }

    private fun openMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}