package com.app.festivalpost.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.app.festivalpost.GridSpacingItemDecoration
import com.app.festivalpost.R
import com.app.festivalpost.activity.HomeActivity.Companion.instance
import com.app.festivalpost.adapter.PostAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.PostItem
import com.google.gson.Gson
import org.json.JSONObject
import java.util.*

class MyPostActivity : AppCompatActivity(), ApiResponseListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var dataArrayList = ArrayList<PostItem>()
    var dataArrayList1 = ArrayList<PostItem>()
    private val showPagination = true
    private var mIsLoading = false
    private var countLoadMore = 1
    var adapter: PostAdapter? = null
    var mSwipeRefreshLayout: SwipeRefreshLayout? = null
    var linearLayout: LinearLayout? = null
    var rvdata: RecyclerView? = null
    var btnCreatePost: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_post)
        setActionbar()
        apiManager = ApiManager(this@MyPostActivity, this@MyPostActivity)
        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        val mLayoutManager = GridLayoutManager(this@MyPostActivity, 3)
        rvdata!!.addItemDecoration(GridSpacingItemDecoration(3, dpToPx(10), true))
        rvdata!!.layoutManager = mLayoutManager
        mSwipeRefreshLayout = findViewById(R.id.swipeToRefresh)
        linearLayout = findViewById(R.id.rlNoData)
        btnCreatePost = findViewById(R.id.btnCreatePost)
        btnCreatePost!!.setOnClickListener(View.OnClickListener {
            instance!!.mBottomNavigationView!!.menu.findItem(R.id.action_home).isChecked = true
            instance!!.loadHomeFragment1()
        })
        loadMoreDetails(countLoadMore)
        adapter = PostAdapter(applicationContext, dataArrayList)
        rvdata!!.adapter = adapter
        Log.d("CountLoad", "" + countLoadMore)
        Log.d("data arraylist", "" + dataArrayList.size)
        rvdata!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (showPagination) {
                    val itemCount = recyclerView.layoutManager!!.itemCount
                    var lastPosition = 0
                    if (recyclerView.layoutManager is GridLayoutManager) {
                        lastPosition =
                            (recyclerView.layoutManager as GridLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    } else if (recyclerView.layoutManager is LinearLayoutManager) {
                        lastPosition =
                            (recyclerView.layoutManager as LinearLayoutManager?)!!.findLastCompletelyVisibleItemPosition()
                    }
                    if (!mIsLoading && lastPosition != 0 && itemCount - 1 == lastPosition) {
                        mIsLoading = false
                        countLoadMore += 1
                        loadMoreDetails(countLoadMore)
                    }
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
            }
        })
        mSwipeRefreshLayout!!.setOnRefreshListener(OnRefreshListener {
            countLoadMore = 1
            dataArrayList.clear()
            loadMoreDetails(countLoadMore)
            mSwipeRefreshLayout!!.setRefreshing(false)
        })
    }

    private fun loadMoreDetails(page: Int) {
        Global.showProgressDialog(this@MyPostActivity)
        apiManager!!.getphotos(
            ApiEndpoints.getphotos,
            Global.getPreference(Constant.PREF_TOKEN, ""),
            page
        )
    }

    private fun dpToPx(dp: Int): Int {
        val r = resources
        return Math.round(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dp.toFloat(),
                r.displayMetrics
            )
        )
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@MyPostActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@MyPostActivity)
            }
        }
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_my_posts)
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@MyPostActivity)
            if (requestService.equals(ApiEndpoints.getphotos, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        rvdata!!.visibility = View.VISIBLE
                        linearLayout!!.visibility = View.GONE
                        filldata()
                    } else {
                        linearLayout!!.visibility = View.VISIBLE
                        rvdata!!.visibility = View.GONE
                        Toast.makeText(this@MyPostActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    private fun filldata() {
        if (dataArrayList.size > 0) {
            //.adapter.addMoreItems(dataArrayList);
            adapter!!.notifyDataSetChanged()
        } else {
            linearLayout!!.visibility = View.VISIBLE
            rvdata!!.visibility = View.GONE
            Toast.makeText(this@MyPostActivity, message, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@MyPostActivity)
            Global.showFailDialog(this@MyPostActivity, responseString)
        }
    }

    fun processResponse(responseString: String?) {
        Global.dismissProgressDialog(this@MyPostActivity)
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
            if (jsonObject.has("data")) {
                val jsonArray = jsonObject.getJSONArray("data")
                mIsLoading = jsonArray.length() != 20
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f = Gson().fromJson(j.toString(), PostItem::class.java)
                        dataArrayList.add(f)
                    }
                    adapter!!.notifyItemInserted(dataArrayList.size - 1)
                } else {
                    linearLayout!!.visibility = View.VISIBLE
                    rvdata!!.visibility = View.GONE
                }
                Global.dismissProgressDialog(this@MyPostActivity)
                adapter!!.notifyDataSetChanged()
            }
        } catch (e: Exception) {
            Global.dismissProgressDialog(this@MyPostActivity)
            e.printStackTrace()
        }
    }


}