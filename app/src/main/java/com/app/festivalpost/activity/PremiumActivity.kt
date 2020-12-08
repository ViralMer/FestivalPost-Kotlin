package com.app.festivalpost.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.TransactionDetails
import com.app.festivalpost.R
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.BusinessItem
import com.app.festivalpost.models.PlanItem
import com.app.festivalpost.models.UserItem
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class PremiumActivity : AppCompatActivity(), ApiResponseListener, IBillingHandler {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var purchase_plan = 0
    private val toolbar: Toolbar? = null
    private val tvtitle: TextView? = null
    private val tvaction: ImageView? = null
    private var tvtrialamount: TextView? = null
    private var tvtrialpackage: TextView? = null
    private var tvtrialdays: TextView? = null
    private var lay_free_advantage: LinearLayout? = null
    private var tvstatus: TextView? = null
    private var tvpremiumheading: TextView? = null
    private var tvpremiumamount: TextView? = null
    private var tvpremiumoriginalamount: TextView? = null
    private var tvpremiumpackage: TextView? = null
    private var tvpremiumdays: TextView? = null
    private var tvpremiumbuy: TextView? = null
    private var lay_paid_advantage: LinearLayout? = null
    var planItemArrayList: ArrayList<PlanItem>? = null
    var userItem: UserItem? = null
    var businessItem: BusinessItem? = null
    var score = "0"
    var billingProcessor: BillingProcessor? = null
    var readyToPurchase = false
    var clicked_plan_model: PlanItem? = null
    var saveImage = 0
    var premium = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)
        apiManager = ApiManager(this@PremiumActivity)
        val bundle = intent.extras
        if (!BillingProcessor.isIabServiceAvailable(this)) {
            Toast.makeText(
                this,
                "In-app billing service is unavailable, please upgrade Android Market/Play to version >= 3.9.16",
                Toast.LENGTH_LONG
            ).show()
        }
        billingProcessor = BillingProcessor(
            this,
            "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAucS4g/HdN2QHpDYB1NTn/VKNOlGypG6/k4sn8+MjbdGuPsGqtkSRl+XE5NPdTmKcBd2IxT0WHSPYxYp+2QmYKMzQw9pT1+8G1qu2XJL+cqby81AH8MIMpWXGMP/ZX91Kme6ZQspi4OPi7dFZYfrBv0IoH7vk5gOWzH8An1lAisE5DkYXVHPYBI1wEVsFo11w+k8vOF/L/ob+mhZLwezO4uxOF7hEJKv4U5yCkQLv8URjV5Gl8eZdAlxZyhTzLRZtcQVp/aZH1x+aEldo+XaMD2BvjJ/0CaEQoKaO4DrJ25ja/o2is6vQGfnu/A4goZMYdQR08goW9zezs/45Px7aHwIDAQAB",
            null,
            this
        )
        billingProcessor!!.initialize()
        if (bundle != null) {
            if (bundle.containsKey("object")) {
                businessItem = bundle["object"] as BusinessItem?
            }
            if (bundle.containsKey("userItem")) {
                userItem = bundle["userItem"] as UserItem?
            }
            if (bundle.containsKey("businessdetails")) {
                businessItem = bundle["businessdetails"] as BusinessItem?
                saveImage = bundle.getInt("saveImage")
                premium = bundle.getInt("premium")
            }
            if (bundle.containsKey("videoData")) {
                businessItem = bundle["videoData"] as BusinessItem?
            }
        }
        setActionbar()
        tvtrialamount = findViewById<View>(R.id.tvtrialamount) as TextView
        tvtrialpackage = findViewById<View>(R.id.tvtrialpackage) as TextView
        tvtrialdays = findViewById<View>(R.id.tvtrialdays) as TextView
        tvstatus = findViewById<View>(R.id.tvstatus) as TextView
        lay_free_advantage = findViewById<View>(R.id.lay_free_advantage) as LinearLayout
        tvpremiumheading = findViewById<View>(R.id.tvpremiumheading) as TextView
        tvpremiumamount = findViewById<View>(R.id.tvpremiumamount) as TextView
        tvpremiumoriginalamount = findViewById<View>(R.id.tvpremiumoriginalamount) as TextView
        tvpremiumpackage = findViewById<View>(R.id.tvpremiumpackage) as TextView
        tvpremiumdays = findViewById<View>(R.id.tvpremiumdays) as TextView
        tvpremiumbuy = findViewById<View>(R.id.tvpremiumbuy) as TextView
        lay_paid_advantage = findViewById<View>(R.id.lay_paid_advantage) as LinearLayout
        tvpremiumbuy!!.setOnClickListener { view ->
            if (readyToPurchase) {
                val planModel = view.tag as PlanItem
                try {
                    val score1 = Global.getPreference(Constant.PREF_SCORE, 0)
                    score = score1.toString()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                if (score == "0") {
                    val builder: AlertDialog.Builder
                    builder = AlertDialog.Builder(this@PremiumActivity)
                    builder.setTitle(resources.getString(R.string.txt_buy_title))
                        .setMessage(resources.getString(R.string.txt_buy_message))
                        .setPositiveButton(resources.getString(R.string.txt_yes)) { dialog, which ->
                            Log.d("Scroe", "" + score)
                            for (i in planItemArrayList!!.indices) {
                                if (score == "0" && planItemArrayList!![i].planActualPrice == "2599") {
                                    Log.d("PlanDetails0", "" + planItemArrayList!![i].sku)
                                    clicked_plan_model = planItemArrayList!![i]
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    break
                                } else if (score == "300" && planItemArrayList!![i].planActualPrice == "2299") {
                                    Log.d("PlanDetails1", "" + planItemArrayList!![i].sku)
                                    clicked_plan_model = planItemArrayList!![i]
                                    Log.d("aaaaaaaa", clicked_plan_model!!.sku)
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    break
                                } else if (score == "600" && planItemArrayList!![i].planActualPrice == "1999") {
                                    clicked_plan_model = planItemArrayList!![i]
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    Log.d("PlanDetails2", "" + planItemArrayList!![i].sku)
                                    break
                                } else if (score == "900" && planItemArrayList!![i].planActualPrice == "1699") {
                                    clicked_plan_model = planItemArrayList!![i]
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    Log.d("PlanDetails3", "" + planItemArrayList!![i].sku)
                                    break
                                } else if (score == "1200" && planItemArrayList!![i].planActualPrice == "1399") {
                                    clicked_plan_model = planItemArrayList!![i]
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    Log.d("PlanDetails4", "" + planItemArrayList!![i].sku)
                                    break
                                } else if (score == "1500" && planItemArrayList!![i].planActualPrice == "1099") {
                                    clicked_plan_model = planItemArrayList!![i]
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    Log.d("PlanDetails5", "" + planItemArrayList!![i].sku)
                                    break
                                } else if (score == "1800" && planItemArrayList!![i].planActualPrice == "799") {
                                    clicked_plan_model = planItemArrayList!![i]
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    Log.d("PlanDetails6", "" + planItemArrayList!![i].sku)
                                    break
                                } else if (score == "2100" && planItemArrayList!![i].planActualPrice == "499") {
                                    clicked_plan_model = planItemArrayList!![i]
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    Log.d("PlanDetails7", "" + planItemArrayList!![i].sku)
                                    break
                                } else if (score == "2400" && planItemArrayList!![i].planActualPrice == "199") {
                                    clicked_plan_model = planItemArrayList!![i]
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    Log.d("PlanDetails8", "" + planItemArrayList!![i].sku)
                                    break
                                } else if (score == "2700" && planItemArrayList!![i].planActualPrice == "2599") {
                                    clicked_plan_model = planItemArrayList!![i]
                                    onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                    Log.d("PlanDetails9", "" + planItemArrayList!![i].sku)
                                    break
                                }
                                /*else {
                                                                onBuyPointsButtonClicked(planItemArrayList.get(i).getSku());
                                                                Log.d("PlanDetails10", "" + planItemArrayList.get(i).getSku());
                                                                break;
                                                            }
                        */
                            }
                        }
                        .setNegativeButton(resources.getString(R.string.txt_no)) { dialog, which -> dialog.dismiss() }
                        .show()
                } else {
                    val builder1: AlertDialog.Builder
                    builder1 = AlertDialog.Builder(this@PremiumActivity)
                    builder1.setTitle("Your Credit Score:$score")
                        .setMessage("Do you want to use Credit?")
                        .setPositiveButton("Yes") { dialog, which ->
                            dialog.dismiss()
                            val builder: AlertDialog.Builder
                            builder = AlertDialog.Builder(this@PremiumActivity)
                            builder.setTitle(resources.getString(R.string.txt_buy_title))
                                .setMessage(resources.getString(R.string.txt_buy_message))
                                .setPositiveButton(resources.getString(R.string.txt_yes)) { dialog, which ->
                                    Log.d("Scroe", "" + score)
                                    for (i in planItemArrayList!!.indices) {
                                        if (score == "0" && planItemArrayList!![i].planActualPrice == "2599") {
                                            Log.d("PlanDetails0", "" + planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "300" && planItemArrayList!![i].planActualPrice == "2299") {
                                            Log.d("PlanDetails1", "" + planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            Log.d("aaaaaaaa", clicked_plan_model!!.sku)
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "600" && planItemArrayList!![i].planActualPrice == "1999") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails2", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "900" && planItemArrayList!![i].planActualPrice == "1699") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails3", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "1200" && planItemArrayList!![i].planActualPrice == "1399") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails4", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "1500" && planItemArrayList!![i].planActualPrice == "1099") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails5", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "1800" && planItemArrayList!![i].planActualPrice == "799") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails6", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "2100" && planItemArrayList!![i].planActualPrice == "499") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails7", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "2400" && planItemArrayList!![i].planActualPrice == "199") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails8", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "2700" && planItemArrayList!![i].planActualPrice == "2599") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails9", "" + planItemArrayList!![i].sku)
                                            break
                                        }
                                        /*else {
                                                                                            onBuyPointsButtonClicked(planItemArrayList.get(i).getSku());
                                                                                            Log.d("PlanDetails10", "" + planItemArrayList.get(i).getSku());
                                                                                            break;
                                                                                        }
                                                    */
                                    }
                                }
                                .setNegativeButton(resources.getString(R.string.txt_no)) { dialog, which -> dialog.dismiss() }
                                .show()
                        }
                        .setNegativeButton("No") { dialog, which ->
                            dialog.dismiss()
                            score = "0"
                            val builder: AlertDialog.Builder
                            builder = AlertDialog.Builder(this@PremiumActivity)
                            builder.setTitle(resources.getString(R.string.txt_buy_title))
                                .setMessage(resources.getString(R.string.txt_buy_message))
                                .setPositiveButton(resources.getString(R.string.txt_yes)) { dialog, which ->
                                    Log.d("Scroe1", "" + score)
                                    for (i in planItemArrayList!!.indices) {
                                        if (score == "0" && planItemArrayList!![i].planActualPrice == "2599") {
                                            Log.d("PlanDetails0", "" + planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "300" && planItemArrayList!![i].planActualPrice == "2299") {
                                            Log.d("PlanDetails1", "" + planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "600" && planItemArrayList!![i].planActualPrice == "1999") {
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            Log.d("PlanDetails2", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "900" && planItemArrayList!![i].planActualPrice == "1699") {
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            Log.d("PlanDetails3", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "1200" && planItemArrayList!![i].planActualPrice == "1399") {
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            Log.d("PlanDetails4", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "1500" && planItemArrayList!![i].planActualPrice == "1099") {
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            Log.d("PlanDetails5", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "1800" && planItemArrayList!![i].planActualPrice == "799") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails6", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "2100" && planItemArrayList!![i].planActualPrice == "499") {
                                            clicked_plan_model = planItemArrayList!![i]
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails7", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "2400" && planItemArrayList!![i].planActualPrice == "199") {
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            Log.d("PlanDetails8", "" + planItemArrayList!![i].sku)
                                            break
                                        } else if (score == "2700" && planItemArrayList!![i].planActualPrice == "2599") {
                                            onBuyPointsButtonClicked(planItemArrayList!![i].sku)
                                            Log.d("PlanDetails9", "" + planItemArrayList!![i].sku)
                                            clicked_plan_model = planItemArrayList!![i]
                                            break
                                        }
                                    }
                                }
                                .setNegativeButton(resources.getString(R.string.txt_no)) { dialog, which -> dialog.dismiss() }
                                .show()
                        }.show()
                }
            }
        }

        /*tvstatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PlanItem planModel = (PlanItem) view.getTag();
                AlertDialog.Builder builder;
                builder = new AlertDialog.Builder(PremiumActivity.this);
                builder.setTitle(getResources().getString(R.string.txt_activate_title))
                        .setMessage(getResources().getString(R.string.txt_activate_message))
                        .setPositiveButton(getResources().getString(R.string.txt_yes), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Global.showProgressDialog(PremiumActivity.this);
                                apiManager.purchaseplan(ApiEndpoints.purchaseplan, Global.getPreference(Constant.PREF_TOKEN, ""), businessItem.getBusiId() + "", planModel.getPlanId() + "", "0",score);

                            }
                        })
                        .setNegativeButton(getResources().getString(R.string.txt_no), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .show();
            }
        });*/Global.showProgressDialog(this@PremiumActivity)
        apiManager!!.testplans(ApiEndpoints.test_plans, businessItem!!.busiId.toString())

/*
        String base64EncodedPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAucS4g/HdN2QHpDYB1NTn/VKNOlGypG6/k4sn8+MjbdGuPsGqtkSRl+XE5NPdTmKcBd2IxT0WHSPYxYp+2QmYKMzQw9pT1+8G1qu2XJL+cqby81AH8MIMpWXGMP/ZX91Kme6ZQspi4OPi7dFZYfrBv0IoH7vk5gOWzH8An1lAisE5DkYXVHPYBI1wEVsFo11w+k8vOF/L/ob+mhZLwezO4uxOF7hEJKv4U5yCkQLv8URjV5Gl8eZdAlxZyhTzLRZtcQVp/aZH1x+aEldo+XaMD2BvjJ/0CaEQoKaO4DrJ25ja/o2is6vQGfnu/A4goZMYdQR08goW9zezs/45Px7aHwIDAQAB";
        Log.d(TAG, "Creating IAB helper.");
        mHelper = new IabHelper(this, base64EncodedPublicKey);
        // enable debug logging (for a production application, you should set this to false).
        mHelper.enableDebugLogging(true);
        // Start setup. This is asynchronous and the specified listener
        // will be called once setup completes.
        Log.d(TAG, "Starting setup.");
        mHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                Log.d(TAG, "Setup finished.");

                if (!result.isSuccess()) {
                    // Oh noes, there was a problem.
                    complain("Problem setting up in-app billing: " + result);
                    return;
                }

                // Have we been disposed of in the meantime? If so, quit.
                if (mHelper == null) return;

                // IAB is fully set up. Now, let's get an inventory of stuff we own.
                Log.d(TAG, "Setup successful. Querying inventory.");
                mHelper.queryInventoryAsync(mGotInventoryListener);
            }
        });*/
    }

    public override fun onDestroy() {
        super.onDestroy()
        // very important:
        if (billingProcessor != null) {
            billingProcessor!!.release()
        }
        /*if (mHelper != null) {
            mHelper.dispose();
            mHelper = null;
        }*/
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_premium)
        val ivaction = toolbar.findViewById<ImageView>(R.id.ivaction)
        ivaction.setOnClickListener { onBackPressed() }
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@PremiumActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@PremiumActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@PremiumActivity)
            if (requestService.equals(ApiEndpoints.test_plans, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        if (planItemArrayList!!.size > 0) {
                            if (planItemArrayList!!.size != 2) {
                                /* PlanItem freePlanItem = planItemArrayList.get(0);
                                        tvtrialamount.setText("Rs. " +freePlanItem.getPlanDiscountPrice());
                                        tvtrialpackage.setText(freePlanItem.getPlanName());
                                        tvtrialdays.setText("( " + freePlanItem.getPlanValidity() + " " + freePlanItem.getPlanValidityType() + " )");
                                        tvstatus.setTag(freePlanItem);*/

                                /*if (purchase_plan==0)
                                        {
                                            tvstatus.setClickable(true);
                                            tvstatus.setText(getResources().getString(R.string.txt_activate));
                                            if(businessItem.getPlan_id().equalsIgnoreCase(String.valueOf(freePlanItem.getPlanId()))){
                                                tvstatus.setText(getResources().getString(R.string.txt_already_active));
                                                tvstatus.setBackgroundResource(R.drawable.edittext_bg_light);
                                                tvstatus.setClickable(false);
                                            }else{
                                                tvstatus.setClickable(true);
                                                tvstatus.setText(getResources().getString(R.string.txt_activate));
                                            }
                                        }
                                        else {
                                            tvstatus.setText(getResources().getString(R.string.txt_already_purchase));
                                            tvstatus.setBackgroundResource(R.drawable.edittext_bg_light);
                                            tvstatus.setClickable(false);
                                            */
                                /*if(businessItem.getPlan_id().equalsIgnoreCase(String.valueOf(freePlanItem.getPlanId()))){
                                                tvstatus.setText(getResources().getString(R.string.txt_already_active));
                                                tvstatus.setBackgroundResource(R.drawable.edittext_bg_light);
                                                tvstatus.setClickable(false);
                                            }else{
                                                tvstatus.setClickable(true);
                                                tvstatus.setText(getResources().getString(R.string.txt_activate));
                                            }*/
                                /*
                                        }*/
                                if (lay_free_advantage!!.childCount > 0) lay_free_advantage!!.removeAllViews()

                                /* if (freePlanItem.getPlanInformation().size() > 0) {
    
                                            for (int i = 0; i < freePlanItem.getPlanInformation().size(); i++) {
    
                                                String advantageStr = freePlanItem.getPlanInformation().get(i);
    
                                                View day_item = LayoutInflater.from(PremiumActivity.this).inflate(R.layout.custom_advantage_item,
                                                        lay_free_advantage, false);
                                                TextView tvmsg = (TextView) day_item.findViewById(R.id.tvmsg);
                                                tvmsg.setText(advantageStr);
                                                lay_free_advantage.addView(day_item);
                                            }
    
                                        }*/
                                val paidPlanItem = planItemArrayList!![0]
                                tvpremiumbuy!!.tag = paidPlanItem
                                tvpremiumheading!!.text = paidPlanItem.planDescount
                                tvpremiumamount!!.text = "Rs. " + paidPlanItem.planActualPrice
                                tvpremiumoriginalamount!!.text =
                                    "Rs. " + paidPlanItem.planDiscountPrice
                                tvpremiumpackage!!.text = paidPlanItem.planName
                                tvpremiumdays!!.text =
                                    "( " + paidPlanItem.planValidity + " " + paidPlanItem.planValidityType + " )"
                                if (businessItem!!.plan_id.equals(
                                        java.lang.String.valueOf(
                                            paidPlanItem.planId
                                        ), ignoreCase = true
                                    )
                                ) {
                                    tvpremiumbuy!!.text = resources.getString(R.string.txt_active)
                                    tvpremiumbuy!!.isClickable = false
                                    tvstatus!!.isClickable = false
                                } else {
                                    tvpremiumbuy!!.isClickable = true
                                    tvpremiumbuy!!.text = resources.getString(R.string.txt_upgrade)
                                }
                                if (lay_paid_advantage!!.childCount > 0) lay_paid_advantage!!.removeAllViews()
                                if (paidPlanItem.planInformation.size > 0) {
                                    for (i in paidPlanItem.planInformation.indices) {
                                        val advantageStr = paidPlanItem.planInformation[i]
                                        val day_item =
                                            LayoutInflater.from(this@PremiumActivity).inflate(
                                                R.layout.custom_advantage_item,
                                                lay_paid_advantage, false
                                            )
                                        val tvmsg =
                                            day_item.findViewById<View>(R.id.tvmsg) as TextView
                                        tvmsg.text = advantageStr
                                        lay_paid_advantage!!.addView(day_item)
                                        Log.d("CLICKED", "" + i)
                                        if (advantageStr.contains("For Any Help Call us at +917686894444")) {
                                            tvmsg.text =
                                                Html.fromHtml("For Any Help Call us at  <u><font color=blue> +917686894444</u>")
                                            Log.d(
                                                "CLICKED1",
                                                "" + advantageStr.contains("+9176868944444")
                                            )
                                            tvmsg.setOnClickListener {
                                                val u = Uri.parse("tel:" + Constant.ADMIN_PHONE)
                                                val i = Intent(Intent.ACTION_DIAL, u)
                                                try {
                                                    startActivity(i)
                                                } catch (s: SecurityException) {
                                                    Toast.makeText(
                                                        this@PremiumActivity,
                                                        s.message,
                                                        Toast.LENGTH_LONG
                                                    )
                                                        .show()
                                                }
                                            }
                                        }
                                    }
                                }
                            } else {
                                Global.getAlertDialog(
                                    this@PremiumActivity,
                                    "Opps..!",
                                    "Plan not available."
                                )
                            }
                        } else {
                            Global.showFailDialog(this@PremiumActivity, message)
                        }
                    } else {
                        Global.getAlertDialog(this@PremiumActivity, "Opps..!", message)
                    }
                } catch (e: Exception) {
                }
            }
            if (requestService.equals(ApiEndpoints.purchaseplan, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processPurchaseResponse(responseString)
                    if (status) {
                        Global.showSuccessDialog(this@PremiumActivity, message)
                        val data = Global.getPreference("premium_data", 0)
                        if (data == 2) {
                            startActivity(Intent(this@PremiumActivity, HomeActivity::class.java))
                            Global.storePreference("premium_data", 0)
                            finish()
                        } else if (data == 3) {
                            startActivity(Intent(this@PremiumActivity, HomeActivity::class.java))
                            Global.storePreference("premium_data", 0)
                            finish()
                        } else {
                            onBackPressed()
                        }
                    } else {
                        Global.showFailDialog(this@PremiumActivity, message)
                    }
                } catch (e: Exception) {
                }
            }
        }
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@PremiumActivity)
            Global.showFailDialog(this@PremiumActivity, responseString)
        }
    }

    
    fun processResponse(responseString: String?) {
        planItemArrayList = ArrayList()
        status = false
        message = ""
        try {
            val jsonObject = JSONObject(responseString)
            if (jsonObject.has("status")) {
                status = jsonObject.getBoolean("status")
            }
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message")
            }
            if (status) {
                if (jsonObject.has("plans")) {
                    val jsonArray = jsonObject.getJSONArray("plans")
                    if (jsonArray.length() > 0) {
                        for (i in 0 until jsonArray.length()) {
                            val j = jsonArray.getJSONObject(i)
                            val b = Gson().fromJson(j.toString(), PlanItem::class.java)
                            planItemArrayList!!.add(b)
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun processPurchaseResponse(responseString: String?) {
        status = false
        message = ""
        try {
            val jsonObject = JSONObject(responseString)
            if (jsonObject.has("status")) {
                status = jsonObject.getBoolean("status")
            }
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message")
            }
            if (jsonObject.has("user_credit")) {
                val score_int = jsonObject.getInt("user_credit")
                Global.storePreference(Constant.PREF_SCORE, score_int)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun onBuyPointsButtonClicked(SKU_USD: String?) {
        billingProcessor!!.purchase(this@PremiumActivity, SKU_USD)
        /* }
        catch (Exception e)
        {
            Log.d("Purchase Cancelled",""+e.getMessage());
        }*/
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (!billingProcessor!!.handleActivityResult(requestCode, resultCode, data)) {
            Log.d(
                "Request1",
                "RequestCode : " + requestCode + "resultCode: " + resultCode + "data:" + data
            )
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    override fun onProductPurchased(productId: String, details: TransactionDetails?) {
        billingProcessor!!.consumePurchase(productId)
        Global.showProgressDialog(this@PremiumActivity)
        apiManager!!.purchaseplan(
            ApiEndpoints.purchaseplan,
            Global.getPreference(Constant.PREF_TOKEN, ""),
            businessItem!!.busiId.toString(),
            clicked_plan_model!!.planId.toString(),
            details!!.purchaseInfo.purchaseData.orderId,
            score
        )
    }

    override fun onPurchaseHistoryRestored() {
        for (i in billingProcessor!!.listOwnedProducts().indices) Log.d(
            "sfsfsdf",
            "Owned Managed Product: \$sku"
        )
        /*for (sku in bp!!.listOwnedSubscriptions()) Log.d(
                "fdsgg",
                "Owned Subscription: $sku"
        )*/
    }

    override fun onBillingError(errorCode: Int, error: Throwable?) {
        //Log.d("Errrorrr",""+error.getMessage()+"sdss"+errorCode);
    }

    override fun onBillingInitialized() {
        readyToPurchase = true
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    companion object {
        fun createLink(
            targetTextView: TextView, completeString: String,
            partToClick: String, clickableAction: ClickableSpan?
        ): TextView {
            val spannableString = SpannableString(completeString)

            // make sure the String is exist, if it doesn't exist
            // it will throw IndexOutOfBoundException
            val startPosition = completeString.indexOf(partToClick)
            val endPosition = completeString.lastIndexOf(partToClick) + partToClick.length
            spannableString.setSpan(
                clickableAction, startPosition, endPosition,
                Spanned.SPAN_INCLUSIVE_EXCLUSIVE
            )
            targetTextView.text = spannableString
            targetTextView.movementMethod = LinkMovementMethod.getInstance()
            return targetTextView
        }
    }
}