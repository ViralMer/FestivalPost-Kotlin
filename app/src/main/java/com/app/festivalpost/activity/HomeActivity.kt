package com.app.festivalpost.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.fragment.AccountFragment
import com.app.festivalpost.fragment.CustomFragment
import com.app.festivalpost.fragment.HomeFragment
import com.app.festivalpost.fragment.HomeFragment.Companion.alertDialog
import com.app.festivalpost.fragment.HomeFragment.Companion.imageLogo1
import com.app.festivalpost.fragment.VideoFragment
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.fragment_home.*

class HomeActivity : AppBaseActivity(),OnItemClickListener {
    var tvaction: TextView? = null
    var tvtitle: TextView? = null
    var mBottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        instance = this

        val mainHandler = Handler(Looper.getMainLooper())
        loadHomeFragment()
        setupBottomNavigation()


    }

    private fun setupBottomNavigation() {
        mBottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        mBottomNavigationView!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadHomeFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_video -> {
                    loadVideoFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_digitalcard -> {
                    //loadCustomFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_post -> {
                    //loadAccountFragment()
                    loadCustomFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_profile -> {
                    loadAccountFragment()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    private fun loadHomeFragment() {
        val fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
         //tvtitle!!.text = resources.getString(R.string.txt_home)
    }


    private fun loadCustomFragment() {
        val fragment = CustomFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()


    }

    private fun loadAccountFragment() {
        val fragment = AccountFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()


    }

    private fun loadVideoFragment() {
        val fragment = VideoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()


    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }


    companion object {
        var instance: HomeActivity? = null
    }

    var currentBusinessID = ""
    override fun onItemClicked(`object`: Any?, index: Int) {
        val b = `object` as CurrentBusinessItem?
        currentBusinessID = "" + b!!.busi_id
        put(b, Constants.SharedPref.KEY_CURRENT_BUSINESS)
        val b1=get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS)
        Log.d("dsdsd",""+b1!!.busi_logo)
        showProgress(true)
        callApi(

            getRestApis().markascurrentbusiness(currentBusinessID), onApiSuccess = {
                getSharedPrefInstance().setValue(Constants.SharedPref.KEY_FRAME_LIST, Gson().toJson(it.frameList))
                put(it.current_business, Constants.SharedPref.KEY_CURRENT_BUSINESS)
                showProgress(false)
                if (it.status!!)
                {
                    b.is_current_business=1
                }
                alertDialog!!.dismiss()
                val currentBusinessItem=get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS)
                Glide.with(this).load(currentBusinessItem!!.busi_logo).placeholder(R.drawable.placeholder_img).error(R.drawable.placeholder_img).into(
                    imageLogo1!!)
            }, onApiError = {
                showProgress(false)

            }, onNetworkError = {
                showProgress(false)

            })
    }
}