package com.example.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;


import androidx.appcompat.app.AppCompatActivity;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPGService;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by vaibhav shitole on 4_12_2023.
 */

public class checksum extends AppCompatActivity implements PaytmPaymentTransactionCallback {

    String custid="", orderId="", mid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
        //initOrderId();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        Intent intent = getIntent();
        orderId = intent.getExtras().getString("orderid");
        custid = intent.getExtras().getString("custid");

        mid = "CySnul123458696815813"; /// your marchant key
        sendUserDetailTOServerdd dl = new sendUserDetailTOServerdd();
        dl.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);


    }

    public class sendUserDetailTOServerdd extends AsyncTask<ArrayList<String>, Void, String> {

        private ProgressDialog dialog = new ProgressDialog(checksum.this);

        //private String orderId , mid, custid, amt;
        String url ="https://www.blueappsoftware.com/payment/payment_paytm/generateChecksum.php";
        String varifyurl = "https://pguat.paytm.com/paytmchecksum/paytmCallback.jsp";
        // "https://securegw-stage.paytm.in/theia/paytmCallback?ORDER_ID"+orderId;
        String CHECKSUMHASH ="";

        @Override
        protected void onPreExecute() {
            this.dialog.setMessage("Please wait");
            this.dialog.show();
        }

        protected String doInBackground(ArrayList<String>... alldata) {
            JSONParser jsonParser = new JSONParser(checksum.this);
            String param=
                    "MID="+mid+
                            "&ORDER_ID=" + orderId+
                            "&CUST_ID="+custid+
                            "&CHANNEL_ID=WAP&TXN_AMOUNT=100&WEBSITE=WEBSTAGING"+
                            "&CALLBACK_URL="+ varifyurl+"&INDUSTRY_TYPE_ID=Retail";

            CHECKSUMHASH="vhSlRh06BJJ58F3Vv9OXHpOlyaTw9V0tCyOj3UZANFtlRum93VHOxV3Vj91uynFNCeB/3S3+7CrFQoy47tpet1/WmPxbIbZqH+8DtUzAH8Y";

            JSONObject jsonObject = jsonParser.makeHttpRequest(url,"POST",param);
//            // yaha per checksum ke saht order id or status receive hoga..
//            Log.e("CheckSum result >>",jsonObject.toString());
//            if(jsonObject != null){
//                Log.e("CheckSum result >>",jsonObject.toString());
//                try {
//
//                    CHECKSUMHASH=jsonObject.has("CHECKSUMHASH")?jsonObject.getString("CHECKSUMHASH"):"";
//                    Log.e("CheckSum result >>",CHECKSUMHASH);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
            return CHECKSUMHASH;
        }

        @Override
        protected void onPostExecute(String result) {
            Log.e(" setup acc ","  signup result  " + result);
            if (dialog.isShowing()) {
                dialog.dismiss();
            }

            PaytmPGService Service = PaytmPGService.getProductionService();
            // when app is ready to publish use production service
            // PaytmPGService  Service = PaytmPGService.getProductionService();

            // now call paytm service here
            //below parameter map is required to construct PaytmOrder object, Merchant should replace below map values with his own values
            HashMap<String, String> paramMap = new HashMap<String, String>();
            //these are mandatory parameters
            paramMap.put("MID", mid); //MID provided by paytm
            paramMap.put("ORDER_ID", orderId);
            paramMap.put("CUST_ID", custid);
            paramMap.put("CHANNEL_ID", "WAP");
            paramMap.put("TXN_AMOUNT", "1");
            paramMap.put("WEBSITE", "WEBSTAGING");
            paramMap.put("CALLBACK_URL" ,varifyurl);
            paramMap.put( "EMAIL" , "abc@gmail.com");   // no need
             paramMap.put( "MOBILE_NO" , "9144040888");  // no need
            paramMap.put("CHECKSUMHASH" ,CHECKSUMHASH);
            //paramMap.put("PAYMENT_TYPE_ID" ,"CC");    // no need
            paramMap.put("INDUSTRY_TYPE_ID", "Retail");

            PaytmOrder Order = new PaytmOrder(paramMap);
            Log.e("checksum ", "param "+ paramMap.toString());
            Service.initialize(Order,null);
            // start payment service call here
            Service.startPaymentTransaction(checksum.this, true, true,
                    checksum.this  );


        }

    }


    @Override
    public void onTransactionResponse(Bundle bundle) {
        Log.e("checksum ", " respon true " + bundle.toString());
    }

    @Override
    public void networkNotAvailable() {

    }

    @Override
    public void onErrorProceed(String s) {
        Log.e("checksum ", " Error  " );
    }

    @Override
    public void clientAuthenticationFailed(String s) {

    }

    @Override
    public void someUIErrorOccurred(String s) {
        Log.e("checksum ", " ui fail respon  "+ s );
    }

    @Override
    public void onErrorLoadingWebPage(int i, String s, String s1) {
        Log.e("checksum ", " error loading pagerespon true "+ s + "  s1 " + s1);
    }

    @Override
    public void onBackPressedCancelTransaction() {
        Log.e("checksum ", " cancel call back respon  " );
    }

    @Override
    public void onTransactionCancel(String s, Bundle bundle) {
        Log.e("checksum ", "  transaction cancel " );
    }


}