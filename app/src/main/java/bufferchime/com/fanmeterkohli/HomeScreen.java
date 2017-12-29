package bufferchime.com.fanmeterkohli;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import info.hoang8f.widget.FButton;

public class HomeScreen extends AppCompatActivity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    FButton playGame, quit,lbd,rules;


    // Client used to interact with Google APIs
    public static GoogleApiClient mGoogleApiClient;

    // Are we currently resolving a connection failure?
    private boolean mResolvingConnectionFailure = false;

    // Has the user clicked the sign-in button?
    private boolean mSignInClicked = false;

    // Automatically start the sign-in flow when the Activity starts
    private boolean mAutoStartSignInFlow = true;

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    // tag for debug logging
    final boolean ENABLE_DEBUG = true;
    final String TAG = "TanC";

    // playing on hard mode?
    boolean mHardMode = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homescreen);

        TextView marque = (TextView) this.findViewById(R.id.marque_scrolling_text);
        //the same one line of code
        marque.setSelected(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();


        playGame = (FButton) findViewById(R.id.playGame);
        lbd = (FButton) findViewById(R.id.lbdt);
        rules = (FButton) findViewById(R.id.howtoplay);

        quit = (FButton) findViewById(R.id.quit);
       // tQ = (TextView) findViewById(R.id.tQ);
       // PlayGame button - it will take you to the MainGameActivity
        playGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeScreen.this, MainGameActivity.class);
                startActivity(intent);
                finish();
            }
        });

        lbd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               ShowLeaderboard();
            }
        });

        //Quit button - This will quit the game
        quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Typeface - this is for fonts style
        Typeface typeface = Typeface.createFromAsset(getAssets(), "fonts/shablagooital.ttf");
        playGame.setTypeface(typeface);
        quit.setTypeface(typeface);
        lbd.setTypeface(typeface);
        rules.setTypeface(typeface);

        findViewById(R.id.sign_in_button).setOnClickListener(this);
    }


    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop(): disconnecting");
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onClick(View view) {
        mSignInClicked = true;
        mGoogleApiClient.connect();
        Toast.makeText(this, "clicked", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart(): connecting");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String displayName;
        if (p == null) {
            Log.w(TAG, "mGamesClient.getCurrentPlayer() is NULL!");
            displayName = "???";
        } else {
            displayName = p.getDisplayName();
        }
        Toast.makeText(this, "display name" + displayName, Toast.LENGTH_LONG).show();
        findViewById(R.id.sign_in_button).setVisibility(View.GONE);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
        Toast.makeText(this, "connection suspend", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed(): attempting to resolve");
        Toast.makeText(this, "connection failed", Toast.LENGTH_LONG);
        if (mResolvingConnectionFailure) {
            Log.d(TAG, "onConnectionFailed(): already resolving");
            return;
        }

        if (mSignInClicked || mAutoStartSignInFlow) {
            mAutoStartSignInFlow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        Toast.makeText(this, "request code" + requestCode + " resultcode " + resultCode, Toast.LENGTH_LONG).show();

        if (requestCode == RC_SIGN_IN) {
            mSignInClicked = false;
            mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                Toast.makeText(this, "request code" + requestCode + " resultcode " + resultCode, Toast.LENGTH_LONG).show();
                //BaseGameUtils.showActivityResultError(this, requestCode, resultCode, R.string.signin_other_error);
            }
        }
    }


    public void submitScore(int score){

        Games.Leaderboards.submitScore(mGoogleApiClient,
                getString(R.string.leaderboard_score),score);
    }


    public void ShowLeaderboard(){

        if(!mGoogleApiClient.isConnected())
        {
            Toast.makeText(this,"You have to sign in first", Toast.LENGTH_LONG).show();
            return;
        }
            startActivityForResult(
                Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient,
                        getString(R.string.leaderboard_score)), 0);
    }
}
