package sslcommerz.me.tashfik.sslcommerzforandroid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.sslcommerz.library.payment.model.datafield.MandatoryFieldModel;
import com.sslcommerz.library.payment.model.dataset.TransactionInfo;
import com.sslcommerz.library.payment.model.util.CurrencyType;
import com.sslcommerz.library.payment.model.util.ErrorKeys;
import com.sslcommerz.library.payment.model.util.SdkCategory;
import com.sslcommerz.library.payment.model.util.SdkType;
import com.sslcommerz.library.payment.viewmodel.listener.OnPaymentResultListener;
import com.sslcommerz.library.payment.viewmodel.management.PayUsingSSLCommerz;

public class MainActivity extends AppCompatActivity {
    private String TAG = "SSLC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText text = findViewById(R.id.amount);
        findViewById(R.id.paynow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pay(text.getText().toString());
            }
        });

    }


    private void pay(String amount) {
        try {
            MandatoryFieldModel mandatoryFieldModel = new MandatoryFieldModel("Your store Id", "Your store password", amount, System.currentTimeMillis() + "", CurrencyType.BDT, SdkType.TESTBOX, SdkCategory.BANK_LIST);
            /*Call for the payment*/
            PayUsingSSLCommerz.getInstance().setData(MainActivity.this, mandatoryFieldModel, new OnPaymentResultListener() {
                @Override
                public void transactionSuccess(TransactionInfo transactionInfo) {
                    // If payment is success and risk label is 0 get payment details from here
                    if (transactionInfo.getRiskLevel().equals("0")) {

                        Log.e(TAG, transactionInfo.getValId());
                       /* After successful transaction send this val id to your server and from
                         your server you can call this api
                         https://sandbox.sslcommerz.com/validator/api/validationserverAPI.php?val_id=yourvalid&store_id=yourstoreid&store_passwd=yourpassword
                         if you call this api from your server side you will get all the details of the transaction.
                         for more details visit:   www.tashfik.me
            */
                    }
// Payment is success but payment is not complete yet. Card on hold now.
                    else {
                        Log.e(TAG, "Transaction in risk. Risk Title : " + transactionInfo.getRiskTitle());
                    }
                }

                @Override
                public void transactionFail(String s) {
                    Log.e(TAG, s);
                }


                @Override
                public void error(int errorCode) {
                    switch (errorCode) {
// Your provides information is not valid.
                        case ErrorKeys.USER_INPUT_ERROR:
                            Log.e(TAG, "User Input Error");
                            break;
// Internet is not connected.
                        case ErrorKeys.INTERNET_CONNECTION_ERROR:
                            Log.e(TAG, "Internet Connection Error");
                            break;
// Server is not giving valid data.
                        case ErrorKeys.DATA_PARSING_ERROR:
                            Log.e(TAG, "Data Parsing Error");
                            break;
// User press back button or canceled the transaction.
                        case ErrorKeys.CANCEL_TRANSACTION_ERROR:
                            Log.e(TAG, "User Cancel The Transaction");
                            break;
// Server is not responding.
                        case ErrorKeys.SERVER_ERROR:
                            Log.e(TAG, "Server Error");
                            break;
// For some reason network is not responding
                        case ErrorKeys.NETWORK_ERROR:
                            Log.e(TAG, "Network Error");
                            break;
                    }
                }
            });
        } catch (Exception e) {
            Log.e("ExceptionException", e.toString());
        }
    }
}
