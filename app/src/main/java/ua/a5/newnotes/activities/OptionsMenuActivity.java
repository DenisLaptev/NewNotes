package ua.a5.newnotes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.Scope;

import java.security.PublicKey;
import java.util.Set;

import ua.a5.newnotes.BuildConfig;
import ua.a5.newnotes.R;
import ua.a5.newnotes.interfaces.ServerAuthCodeCallbacks;
import ua.a5.newnotes.util.IabHelper;
import ua.a5.newnotes.util.IabResult;
import ua.a5.newnotes.util.Inventory;
import ua.a5.newnotes.util.Purchase;
import ua.a5.newnotes.util.Security;

import static com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import static com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import static ua.a5.newnotes.activities.StartMenuActivity.mGoogleApiClient;


public class OptionsMenuActivity
        extends AppCompatActivity
        implements
        ConnectionCallbacks,
        OnConnectionFailedListener,
        ServerAuthCodeCallbacks {

    //For Leaderboard
    //public GoogleApiClient mGoogleApiClient;  // initialized in onCreate
    //For Leaderboard

    //переменная указывает, есть ли соединение с интернетом или нет.
    boolean isOnLine = false;

    //For billing.
    String base64EncodedPublicKey;
    IabHelper mHelper;
    private static final String TAG = "billing";
    static final String ITEM_SKU_PREMIUM = "upgrade";
    static final int REQUEST_CODE_INT = 10001;


    // Does the user have the premium upgrade?
    public static boolean mIsPremium = false;
    //For billing.


    Button btnOptionsMenuMoreApps;
    Button btnOptionsMenuUpgrade;
    Button btnOptionsMenuAbout;
    Button btnOptionsMenuBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);

        base64EncodedPublicKey = getString(R.string.billing_pubkey);
        /*
        //For Leaderboard
        // Create the Google Api Client with access to the Play Game and Drive services.
        mGoogleApiClient = new Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                //.addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addApi(Drive.API).addScope(Drive.SCOPE_APPFOLDER) // Drive API
                .build();
        //For Leaderboard
       */
        //For billing.
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " + result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");
                }
            }
        });
        //For billing.

        //Initializing the Google Mobile Ads SDK
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
        //Initializing the Google Mobile Ads SDK

        btnOptionsMenuMoreApps = (Button) findViewById(R.id.btnOptionsMenuMoreApps);
        btnOptionsMenuMoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isOnLine = isOnline();
                if (isOnLine == true) {
                    Toast.makeText(getApplicationContext(), "MoreApps", Toast.LENGTH_SHORT).show();


                    String url = "https://play.google.com/store/apps/developer?id=a5.ua&hl=en";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), "no Internet connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnOptionsMenuUpgrade = (Button) findViewById(R.id.btnOptionsMenuUpgrade);
        btnOptionsMenuUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                isOnLine = isOnline();

                if (isOnLine == true) {

                    Toast.makeText(getApplicationContext(), "Upgrade", Toast.LENGTH_SHORT).show();
                    try {
                        mHelper.launchPurchaseFlow(OptionsMenuActivity.this, ITEM_SKU_PREMIUM, REQUEST_CODE_INT, mPurchaseFinishedListener, "ITEM_SKU_PREMIUM");
                        //mHelper.launchPurchaseFlow(OptionsMenuActivity.this, ITEM_SKU_TEST, REQUEST_CODE_INT,mPurchaseFinishedListener, "ITEM_SKU_TEST");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "no Internet connection",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnOptionsMenuAbout = (Button) findViewById(R.id.btnOptionsMenuAbout);
        btnOptionsMenuAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(OptionsMenuActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });

        btnOptionsMenuBack = (Button) findViewById(R.id.btnOptionsMenuBack);
        btnOptionsMenuBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    IabHelper.QueryInventoryFinishedListener
            mQueryFinishedListener = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {

            Log.d(TAG, "Query inventory finished.");

            // Is it a failure?
            if (result.isFailure()) {
                // handle error
                return;
            }

            Log.d(TAG, "Query inventory was successful.");


            // Do we have the premium upgrade?
            Purchase premiumPurchase = inventory.getPurchase(ITEM_SKU_PREMIUM);
            mIsPremium = (premiumPurchase != null);
            Log.d("Billing", "User is " + (mIsPremium ? "PREMIUM" : "NOT PREMIUM"));

        }
    };


    @Override
    public void onStart() {
        super.onStart();
        /*
//For Leaderboard
        mGoogleApiClient.connect();
//For Leaderboard
        */

    }


    @Override
    public void onResume() {
        super.onResume();

        //set white background to the Activity.
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.options_menu_relative_layout);
        rl.setBackgroundColor(getResources().getColor(R.color.colorBackgroundWhite));
    }


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }


    //For Leaderboard
    @Override
    public void onConnected(Bundle connectionHint) {
        //Toast.makeText(getApplicationContext(), "connected",
        //        Toast.LENGTH_SHORT).show();
    }


    private ConnectionResult mConnectionResult;

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, 0);
            } catch (IntentSender.SendIntentException e) {
                mGoogleApiClient.connect();
            }
        }
        mConnectionResult = connectionResult;
    }


    @Override
    public void onConnectionSuspended(int i) {
    }


    public ServerAuthCodeCallbacks.CheckResult onCheckServerAuthorization(String s, Set<Scope> set) {
        return null;
    }

    public boolean onUploadServerAuthCode(String s, String s1) {
        return false;
    }


    //For billing.

    @Override
    protected void onActivityResult(int requestCode, int responseCode, Intent intent) {
        if (!mHelper.handleActivityResult(requestCode,
                responseCode, intent)) {
            super.onActivityResult(requestCode, responseCode, intent);
        }
        if (requestCode == ConnectionResult.SIGN_IN_REQUIRED && responseCode == RESULT_OK) {
            mConnectionResult = null;
            mGoogleApiClient.connect();
        }

        Log.d("Billing", "onActivityResult(" + requestCode + "," + responseCode + "," + intent + ")");

    }


    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                Log.d(TAG, "Error purchasing: " + result);
                return;
            } else if (purchase.getSku().equals(ITEM_SKU_PREMIUM)) {
                // give user access to premium content and update the UI

                //mIsPremium = true;

                try {
                    //updateUi();
                    consumeItem();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    };


    //Этот метод срабатывает, если был успешный заказ на обновление приложения.
    public void consumeItem() throws Exception {
        mHelper.queryInventoryAsync(mGotInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mGotInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result, Inventory inventory) {
            if (result.isFailure()) {
                // Handle failure
            } else {

                // does the user have the premium upgrade?
                mIsPremium = inventory.hasPurchase(ITEM_SKU_PREMIUM);
            }
        }
    };


    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        //clickButton.setEnabled(true);
                    } else {
                        // handle error
                    }
                }
            };


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mHelper != null) try {
            mHelper.dispose();
        } catch (Exception e) {
            e.printStackTrace();
        }
        mHelper = null;
        finish();
    }


    public static boolean verifyPurchase(String base64PublicKey,
                                         String signedData, String signature) {
        if (TextUtils.isEmpty(signedData) ||
                TextUtils.isEmpty(base64PublicKey) ||
                TextUtils.isEmpty(signature)) {
            Log.e(TAG, "Purchase verification failed: missing data.");
            if (BuildConfig.DEBUG) {
                return true;
            }
            return false;
        }

        PublicKey key = Security.generatePublicKey(base64PublicKey);
        return Security.verify(key, signedData, signature);
    }
//For billing.
}



































