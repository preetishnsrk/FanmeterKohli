package bufferchime.com.fanmeterkohli;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;


public class PlayAgain extends Activity {

    Button playAgain, rewardVideo,endgme;
    TextView wrongAnsText;
    private RewardedVideoAd mRewardedVideoAd;
    private boolean adRewarded = false;
    SharedPreferences sharedPreferences;
    int coinValue = 0;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.play_again);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        editor = sharedPreferences.edit();
        coinValue = sharedPreferences.getInt("coinValue", coinValue);

        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
        playAgain = (Button) findViewById(R.id.playAgainButton);
        rewardVideo = (Button) findViewById(R.id.showRewardedButton);
        endgme = (Button) findViewById(R.id.endgame);
        wrongAnsText = (TextView) findViewById(R.id.wrongAns);
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.loadAd(getString(R.string.rewarded_app_id), new AdRequest.Builder().build());

        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(),
                        "Ad loaded.", Toast.LENGTH_SHORT).show();
                mRewardedVideoAd.show();
                rewardVideo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(getBaseContext(),
                        "Ad opened.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(getBaseContext(),
                        "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(getBaseContext(),
                        "Ad closed.", Toast.LENGTH_SHORT).show();

                if (adRewarded) {
                    editor.putInt("coinValue", coinValue + 1);
                    editor.commit();
                    startActivity(new Intent(PlayAgain.this, MainGameActivity.class));
                } else {
                    if (mRewardedVideoAd.isLoaded()) {
                        rewardVideo.setVisibility(View.VISIBLE);
                    } else {
                        mRewardedVideoAd.loadAd(getString(R.string.rewarded_app_id), new AdRequest.Builder().build());
                        rewardVideo.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                rewardVideo.setVisibility(View.GONE);
                adRewarded = true;

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(getBaseContext(),
                        "Ad left application.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getBaseContext(),
                        "Ad failed to load.", Toast.LENGTH_SHORT).show();
            }

        });
        //Initialize


        rewardVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mRewardedVideoAd.isLoaded()) {
                    mRewardedVideoAd.show();
                }
            }
        });
        //play again button onclick listener
        playAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putInt("coinValue", 0);
                editor.apply();
                editor.commit();
                Intent intent = new Intent(PlayAgain.this, MainGameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        //end game button onclick listener
        endgme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PlayAgain.this, HomeScreen.class);
                startActivity(intent);
                finish();
            }
        });


        //Setting typefaces for textview and button - this will give stylish fonts on textview and button
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/shablagooital.ttf");
        playAgain.setTypeface(typeface);
        wrongAnsText.setTypeface(typeface);
        rewardVideo.setTypeface(typeface);
        endgme.setTypeface(typeface);
    }

    @Override
    public void onResume() {
        mRewardedVideoAd.setRewardedVideoAdListener(new RewardedVideoAdListener() {
            @Override
            public void onRewardedVideoAdLoaded() {
                Toast.makeText(getBaseContext(),
                        "Ad loaded.", Toast.LENGTH_SHORT).show();
                //mRewardedVideoAd.show();
                rewardVideo.setVisibility(View.VISIBLE);
            }

            @Override
            public void onRewardedVideoAdOpened() {
                Toast.makeText(getBaseContext(),
                        "Ad opened.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoStarted() {
                Toast.makeText(getBaseContext(),
                        "Ad started.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdClosed() {
                Toast.makeText(getBaseContext(),
                        "Ad closed.", Toast.LENGTH_SHORT).show();

                if (adRewarded) {
                    startActivity(new Intent(PlayAgain.this, MainGameActivity.class));
                } else {
                    if (mRewardedVideoAd.isLoaded()) {
                        rewardVideo.setVisibility(View.VISIBLE);
                    } else {
                        rewardVideo.setVisibility(View.GONE);
                    }
                }

            }

            @Override
            public void onRewarded(RewardItem rewardItem) {
                rewardVideo.setVisibility(View.GONE);
                adRewarded = true;

            }

            @Override
            public void onRewardedVideoAdLeftApplication() {
                Toast.makeText(getBaseContext(),
                        "Ad left application.", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onRewardedVideoAdFailedToLoad(int i) {
                Toast.makeText(getBaseContext(),
                        "Ad failed to load.", Toast.LENGTH_SHORT).show();
            }

        });
        mRewardedVideoAd.resume(this);
        super.onResume();
    }

    @Override
    public void onPause() {
        mRewardedVideoAd.pause(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        mRewardedVideoAd.destroy(this);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
