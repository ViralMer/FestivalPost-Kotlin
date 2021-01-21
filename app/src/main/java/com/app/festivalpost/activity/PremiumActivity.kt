package com.app.festivalpost.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.*
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.viewpager.widget.ViewPager
import com.anjlab.android.iab.v3.BillingProcessor
import com.anjlab.android.iab.v3.BillingProcessor.IBillingHandler
import com.anjlab.android.iab.v3.TransactionDetails
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.api.RestApis
import com.app.festivalpost.api.RestClient
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.*
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.Constants.SharedPref.USER_NAME
import com.app.festivalpost.utils.Constants.SharedPref.USER_NUMBER
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.*
import com.google.gson.Gson
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.razorpay.PaymentResultListener
import com.razorpay.PaymentResultWithDataListener
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class PremiumActivity : AppBaseActivity(), PaymentResultWithDataListener {
    var status = false
    var message = ""
    var planItemArrayList= arrayListOf<PlanListItemResponse?>()
    var businessItem: CurrentBusinessItem? = null

    var business_id:String?=""
    private var viewPager: ViewPager? = null
    private var sessionManager: SessionManager? = null
    private var token: String? = null
    var device_type : String?=""
    var payment_id : String?=""
    var planSelectedId : String?=""
    var planSelectedPrice : String?=""





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)
        sessionManager= SessionManager(this)
        token=sessionManager!!.getValueString(Constants.SharedPref.USER_TOKEN)
        device_type=sessionManager!!.getValueString(Constants.KeyIntent.DEVICE_TYPE)

        setActionbar()
        try {
            val bundle=intent.extras
            if (bundle!=null)
            {
                if (bundle.containsKey("business_id"))
                {
                    business_id=bundle["business_id"] as String?
                }
            }
            else
            {
                val businessItem = get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS,this)
                business_id=businessItem!!.busi_id.toString()
            }
        }
        catch (e:java.lang.Exception)
        {

        }



        loadPlanData()

        viewPager = findViewById(R.id.planviewPager)



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
            val iv_plan=view.findViewById<View>(R.id.iv_plan) as ImageView
            val planItemDetails = planItemArrayList!![position]

            Glide.with(this@PremiumActivity).load(planItemDetails!!.image).into(iv_plan)
            Log.d("PlanImahe",""+planItemDetails.image)
            iv_plan.onClick {
                planSelectedId=planItemDetails.id
                planSelectedPrice=planItemDetails.price

                Log.d("PlanID",""+planSelectedId)
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


    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ib_cancel_premium = toolbar.findViewById<ImageView>(R.id.ib_cancel_premium)
        ib_cancel_premium.setOnClickListener { onBackPressed() }
    }



    override fun onBackPressed() {
        super.onBackPressed()
    }

    private fun handleRazorPay() {
        val checkout = Checkout()
        checkout.setImage(R.mipmap.ic_launcher_new)
        //
        checkout.setKeyID("rzp_test_nxkzWJTVTnu6dj")
        //checkout.setKeyID("rzp_live_AorAULQtjNzfuq")

        try {
            val options = JSONObject()
            options.put("name", sessionManager!!.getValueString(USER_NAME))
            options.put("description", sessionManager!!.getValueString(USER_NUMBER))
            options.put("currency", "INR")
            //options.put("order_id", "orderid_123465")
            val planPrice=planSelectedPrice!!.toInt()
            options.put("amount", (planPrice * 100).toDouble())
            options.put("image", "https://rzp-mobile.s3.amazonaws.com/images/rzp.png")

            Log.d(this.localClassName, options.toString())
            checkout.open(this, options)
        } catch (e: Exception) {
            Log.e(this.localClassName, "Error in starting Razorpay Checkout", e)
        }
    }

    @SuppressLint("ResourceAsColor")
    override fun onPaymentSuccess(p0: String?, p1: PaymentData?) {
        payment_id=p0.toString()
        Toast.makeText(this,"Your business will approve within 24 hours.",Toast.LENGTH_LONG).show()

        AsyncTaskExampleNew().execute();



    }




    override fun onPaymentError(p0: Int, p1: String?, p2: PaymentData?) {
        Log.d("Data RazorPay",""+p0.toString()+ " p1: "+p1.toString()+ " p2:"+p2)

    }

    private fun loadAccoutData(orderID:String,paymentId:String,businessId:String,plan_id:String) {
        showProgress(true)
        callApi(
            getRestApis().purchaseplan(orderID,paymentId,businessId,plan_id,device_type!!,token!!), onApiSuccess = {
                showProgress(false)
                if (it.status!!) {
                    toast("Your business will approve within 24 hours.")
                    launchActivity<HomeActivity> {
                        finish()
                    }
                }
                Log.d("Response",""+it)

            }, onApiError = {
                showProgress(false)


            }, onNetworkError = {
                showProgress(false)
                openLottieDialog {  }

            })
    }

    private fun loadPlanData() {
        showProgress(true)
        RestClient.getClient.plans().enqueue(object : Callback<PlanListResponse>{
            override fun onResponse(
                call: Call<PlanListResponse>,
                response: Response<PlanListResponse>
            ) {
                showProgress(false)
                val res=response.body()
                if (res!!.status!!)
                {
                    planItemArrayList=res!!.data!!
                    viewPager!!.adapter = PagerAdapter()
                }
                else{
                        Toast.makeText(this@PremiumActivity,"Please try again",Toast.LENGTH_SHORT).show()
                }



            }

            override fun onFailure(call: Call<PlanListResponse>, t: Throwable) {
                showProgress(false)
                Toast.makeText(this@PremiumActivity,"Something went wrong",Toast.LENGTH_SHORT).show()
            }
        })

    }


    inner class AsyncTaskExampleNew : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            showProgress(true)
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                loadAccoutData("1234",payment_id!!,business_id!!,planSelectedId!!)
            } catch (e: Exception) {
                //p.dismiss();
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            showProgress(false)
            super.onPostExecute(aVoid)
        }
    }


}