package com.app.festivalpost.activity

import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.GridSpacingItemDecoration
import com.app.festivalpost.R
import com.app.festivalpost.adapter.PostAdapter
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.fragment.HomeFragment
import com.app.festivalpost.fragment.MyPostFragment
import com.app.festivalpost.fragment.MyVideoFragment
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.models.FileListItem
import com.app.festivalpost.models.PostItem
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.io.File
import java.util.*

class MyPostActivity : AppBaseActivity() {

    var tvaction: TextView? = null
    var tvtitle: TextView? = null
    var mBottomNavigationView: BottomNavigationView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_post)
        setActionbar()

        loadPostFragment()
        setupBottomNavigation()



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


    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_my_posts)
    }
    private fun setupBottomNavigation() {
        mBottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        mBottomNavigationView!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_images -> {
                    loadPostFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.nav_video -> {
                    loadVideoFragment()
                    return@OnNavigationItemSelectedListener true
                }

            }
            false
        })
    }

    private fun loadPostFragment() {
        val fragment = MyPostFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }

    private fun loadVideoFragment() {
        val fragment = MyVideoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
    }



}