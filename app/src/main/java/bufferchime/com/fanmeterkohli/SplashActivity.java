package bufferchime.com.fanmeterkohli;

import android.app.Activity;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by kesha on 21-07-2017.
 */

public class SplashActivity extends Activity {


    Thread splashTread;

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        Window window = getWindow();
        window.setFormat(PixelFormat.RGBA_8888);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);

        final TextView splash_text = (TextView) findViewById(R.id.splash_text);
        final ImageView splash_image = (ImageView) findViewById(R.id.splash_image);
        final Animation animation_1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate);
        final Animation animation_2 = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.fade_in);

        animation_1.reset();
        animation_2.reset();

        splash_image.clearAnimation();
        splash_image.startAnimation(animation_1);

        splash_text.clearAnimation();
        splash_text.startAnimation(animation_2);


        splashTread = new Thread() {
            @Override
            public void run() {
                try {
                    int waited = 0;
                    // Splash screen pause time
                    while (waited < 3500) {
                        sleep(100);
                        waited += 100;
                    }
                    Intent intent = new Intent(SplashActivity.this,
                            HomeScreen.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                    SplashActivity.this.finish();
                } catch (InterruptedException e) {
                    // do nothing
                } finally {
                    SplashActivity.this.finish();
                }

            }
        };
        splashTread.start();
    }}