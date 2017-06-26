package ua.a5.newnotes.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.chartboost.sdk.CBLocation;
import com.chartboost.sdk.Chartboost;
import com.google.android.gms.ads.MobileAds;

import ua.a5.newnotes.R;
import ua.a5.newnotes.util.IabHelper;
import ua.a5.newnotes.util.IabResult;
import ua.a5.newnotes.util.Inventory;
import ua.a5.newnotes.util.Purchase;


public class OptionsMenuActivity extends AppCompatActivity {
    Button btnOptionsMenuMoreApps;
    Button btnOptionsMenuUpgrade;
    Button btnOptionsMenuAbout;
    Button btnOptionsMenuBack;


//for Billing new


//for Billing new

    //переменная указывает, есть ли соединение с интернетом или нет.
    boolean isOnLine = false;


    //For billing.
    String base64EncodedPublicKey;

    private static final String TAG = "billing";
    static final String ITEM_SKU_PREMIUM = "upgrade";
    static final String ITEM_SKU_TEST = "android.test.purchased";
    static final int REQUEST_CODE_INT = 10001;
    IabHelper mHelper;
    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase)
        {
            if (result.isFailure()) {
                // Handle error
                return;
            }
            //else if (purchase.getSku().equals(ITEM_SKU_TEST)) {
            else if (purchase.getSku().equals(ITEM_SKU_PREMIUM)) {
                consumeItem();

            }

        }
    };
    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {


            if (result.isFailure()) {
                // Handle failure
            } else {
                //mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU_TEST),
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU_PREMIUM),
                        mConsumeFinishedListener);
            }
        }
    };
    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {
                        mIsPremium = true;
                    } else {
                        // handle error
                    }
                }
            };

    // Does the user have the premium upgrade?
    public static boolean mIsPremium = false;
    //For billing.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_options_menu);


//Initializing the Google Mobile Ads SDK
        MobileAds.initialize(getApplicationContext(), getString(R.string.admob_app_id));
//Initializing the Google Mobile Ads SDK


        base64EncodedPublicKey = getString(R.string.billing_pubkey);


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
                    signIn();

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

    private void signIn() {
        mHelper = new IabHelper(this, base64EncodedPublicKey);

        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    Log.d(TAG, "In-app Billing setup failed: " +
                            result);
                } else {
                    Log.d(TAG, "In-app Billing is set up OK");

                    //mHelper.launchPurchaseFlow(OptionsMenuActivity.this, ITEM_SKU_TEST, REQUEST_CODE_INT,
                    mHelper.launchPurchaseFlow(OptionsMenuActivity.this, ITEM_SKU_PREMIUM, REQUEST_CODE_INT,
                            mPurchaseFinishedListener, "mypurchasetoken");
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data)
    {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


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
        if (mHelper != null) mHelper.dispose();
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
}
