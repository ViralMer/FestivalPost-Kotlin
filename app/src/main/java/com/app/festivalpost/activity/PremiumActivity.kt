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
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.TransactionDetails
import com.app.festivalpost.R
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.*
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.onClick
import com.emegamart.lelys.utils.extensions.onPageSelected
import com.emegamart.lelys.utils.extensions.toast
import com.google.gson.Gson
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import org.json.JSONObject
import java.util.*

class PremiumActivity : AppCompatActivity(), ApiResponseListener, IBillingHandler,PaymentResultListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var planItemArrayList= arrayListOf<PlanItemDetails>()
    var userItem: UserItem? = null
    var businessItem: CurrentBusinessItem? = null
    var score = "0"
    var billingProcessor: BillingProcessor? = null
    var readyToPurchase = false
    var clicked_plan_model: PlanItem? = null
    private var viewPager: ViewPager? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)
        apiManager = ApiManager(this@PremiumActivity)
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
        setActionbar()
        planItemArrayList!!.add(PlanItemDetails("1","Basic Plan","1199"))

        viewPager = findViewById(R.id.planviewPager)
        viewPager!!.adapter = PagerAdapter()


        Checkout.preload(this)

        viewPager.apply {
            this!!.clipChildren = false
            pageMargin = resources.getDimensionPixelOffset(R.dimen.spacing_small)
            offscreenPageLimit = 3
            setPageTransformer(false, CarouselEffectTransformer(this@PremiumActivity))
            offscreenPageLimit = 0

            onPageSelected { position: Int ->
                val animFadeIn = android.view.animation.AnimationUtils.loadAnimation(applicationContext, R.anim.fade_in)
            }
        }




    }

    inner class PagerAdapter : androidx.viewpager.widget.PagerAdapter() {
        override fun getCount(): Int {
            return planItemArrayList!!.size
        }

        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view === `object`
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            val view = LayoutInflater.from(this@PremiumActivity).inflate(R.layout.item_premium, container, false)
            val tvPlanName=view.findViewById<View>(R.id.tvPlanName) as TextView
            val tvPlanPrice=view.findViewById<View>(R.id.tvPlanPrice) as TextView
            val tvPlanMoths=view.findViewById<View>(R.id.tvPlanmonths) as TextView
            val tvPurchase=view.findViewById<View>(R.id.tvPurchase) as TextView
            val planItemDetails = planItemArrayList!![position]

            tvPlanName.text=planItemDetails.name
            tvPlanPrice.text=planItemDetails.price
            tvPlanMoths.text=getString(R.string.lbl_three_months)
            if (position==0)
            {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    tvPurchase.background=ContextCompat.getDrawable(this@PremiumActivity,R.drawable.bg_gradient_blue)
                }
            }
            if (position==1)
            {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    tvPurchase.background=ContextCompat.getDrawable(this@PremiumActivity,R.drawable.bg_gradient_purple)
                }
            }
            if (position==2)
            {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    tvPurchase.background=ContextCompat.getDrawable(this@PremiumActivity,R.drawable.bg_gradient_pink)
                }
            }

            tvPurchase.onClick {
                handleRazorPay()
            }



            container.addView(view)
            return view
        }


        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(`object` as View)
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
        // very important:
        if (billingProcessor != null) {
            billingProcessor!!.release()
        }

    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ib_cancel_premium = toolbar.findViewById<ImageView>(R.id.ib_cancel_premium)
        ib_cancel_premium.setOnClickListener { onBackPressed() }
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

    private fun onBuyPointsButtonClicked(SKU_USD: String?) {
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
            businessItem!!.busi_id.toString(),
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

    override fun onPaymentSuccess(p0: String?) {
        TODO("Not yet implemented")
    }

    override fun onPaymentError(p0: Int, p1: String?) {
        Log.d(this.localClassName, p0.toString())
        //toast(response!!)
        Log.d(this.localClassName, p1.toString())
    }

    private fun handleRazorPay() {
        val checkout = Checkout()
        checkout.setImage(R.mipmap.ic_launcher_new)

        try {
            val options = JSONObject()
            options.put("name", "Iqonic")
            options.put("description", "")
            options.put("currency", "INR")
            //options.put("order_id", orderData?.id)
            options.put("amount", (1000 * 100).toDouble())
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png")

            Log.d(this.localClassName, options.toString())
            checkout.open(this, options)
        } catch (e: Exception) {
            Log.e(this.localClassName, "Error in starting Razorpay Checkout", e)
        }
    }


}