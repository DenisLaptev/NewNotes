package ua.a5.newnotes.activities;

import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.games.Games;
import com.google.android.gms.plus.Plus;

import java.security.PublicKey;
import java.util.Set;

import ua.a5.newnotes.BuildConfig;
import ua.a5.newnotes.R;
import ua.a5.newnotes.interfaces.ServerAuthCodeCallbacks;
import ua.a5.newnotes.util_billing.IabHelper;
import ua.a5.newnotes.util_billing.IabResult;
import ua.a5.newnotes.util_billing.Inventory;
import ua.a5.newnotes.util_billing.Purchase;
import ua.a5.newnotes.util_billing.Security;

public class OptionsMenuActivity extends AppCompatActivity
        //implements GoogleApiClient.ConnectionCallbacks,
        //GoogleApiClient.OnConnectionFailedListener,
        //ServerAuthCodeCallbacks
{
    Button btnOptionsMenuMoreApps;
    Button btnOptionsMenuUpgrade;
    Button btnOptionsMenuAbout;
    Button btnOptionsMenuBack;


    //переменная указывает, есть ли соединение с интернетом или нет.
    boolean isOnLine = false;

/*
    //For Leaderboard
    public GoogleApiClient mGoogleApiClient;  // initialized in onCreate
    //For Leaderboard
*/
    //For billing.
    String base64EncodedPublicKey;
    IabHelper mHelper;
    private static final String TAG = "billing";
    static final String ITEM_SKU_PREMIUM = "upgrade";

    // Does the user have the premium upgrade?
    public static boolean mIsPremium = false;
    //public static boolean mIsPremium = true;
    //For billing.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);
/*
//Initializing the Google Mobile Ads SDK
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
//Initializing the Google Mobile Ads SDK
*/
        /*

        /////////////////////////////////////////////////////////////////////////////////////
        //for Chartboost (для MoreApps):
        String appId = "594d1bdc04b0167501ca87f3";
        String appSignature = "648416c21b4f1029bb343933a0f7be89b10fc1f4";
        Chartboost.startWithAppId(this, appId, appSignature);
        Chartboost.setImpressionsUseActivities(true);
        Chartboost.onCreate(this);
        /////////////////////////////////////////////////////////////////////////////////////

*/
        //TODO for Billing
        base64EncodedPublicKey = getString(R.string.billing_pubkey);

/*
        //For Leaderboard
        // Create the Google Api Client with access to the Play Game and Drive services.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Plus.API, Plus.PlusOptions.builder().build())
                .addScope(Plus.SCOPE_PLUS_LOGIN)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .addScope(Drive.SCOPE_FILE)
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

        btnOptionsMenuMoreApps = (Button) findViewById(R.id.btnOptionsMenuMoreApps);
        btnOptionsMenuMoreApps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "MoreApps", Toast.LENGTH_SHORT).show();

                isOnLine = isOnline();
                if (isOnLine == true) {

                    //на экране появляется список MoreApps.
                    Chartboost.cacheMoreApps(CBLocation.LOCATION_DEFAULT);
                    Chartboost.showMoreApps(CBLocation.LOCATION_DEFAULT);

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
                Toast.makeText(getApplicationContext(), "Upgrade", Toast.LENGTH_SHORT).show();

                isOnLine = isOnline();

                if (isOnLine == true) {

                    //For billing.
                    try {
                        mHelper.launchPurchaseFlow(OptionsMenuActivity.this, ITEM_SKU_PREMIUM, 10001,
                                mPurchaseFinishedListener, "ITEM_SKU_PREMIUM");
                    } catch (IabHelper.IabAsyncInProgressException e) {
                        e.printStackTrace();
                    }
                    //For billing.

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


    //for Billing
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
//for Billing


    //For billing.

/*
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
*/

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                Log.d(TAG, "Error purchasing: " + result);
                return;
            } else if (purchase.getSku().equals(ITEM_SKU_PREMIUM)) {
                // give user access to premium content and update the UI

                mIsPremium = true;

                try {
                    //updateUi();
                    consumeItem();
                } catch (IabHelper.IabAsyncInProgressException e) {
                    e.printStackTrace();
                }
            }

        }
    };


    //Этот метод срабатывает, если был успешный заказ на обновление приложения.
    public void consumeItem() throws IabHelper.IabAsyncInProgressException {
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


    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnected();
    }

    @Override
    public void onResume() {
        super.onResume();
/*

        //for Chartboost:
        Chartboost.onResume(this);
*/

        //set white background to the Activity.
        RelativeLayout rl = (RelativeLayout) findViewById(R.id.options_menu_relative_layout);
        rl.setBackgroundColor(getResources().getColor(R.color.colorBackgroundWhite));
    }


    @Override
    public void onStart() {
        super.onStart();

        /*
        //for Chartboost:
        Chartboost.onStart(this);

        //For Leaderboard
        mGoogleApiClient.connect();
        //For Leaderboard
    */


    }


    @Override
    public void onPause() {
        super.onPause();

        /*
        //for Chartboost:
        Chartboost.onPause(this);
        */

    }


    @Override
    public void onStop() {
        super.onStop();

        /*
        //for Chartboost:
        Chartboost.onStop(this);
        */

    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mHelper != null) try {
            mHelper.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
        mHelper = null;
/*

        //for Chartboost:
        Chartboost.onDestroy(this);
*/

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        /*
        //for Chartboost:
        Chartboost.onBackPressed();
        */

    }
/*
    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    private ConnectionResult mConnectionResult;

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
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
    public CheckResult onCheckServerAuthorization(String var1, Set<Scope> var2) {
        return null;
    }

    @Override
    public boolean onUploadServerAuthCode(String var1, String var2) {
        return false;
    }

    */
}
