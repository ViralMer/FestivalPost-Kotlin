package com.app.festivalpost.activity

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.fragment.AccountFragment
import com.app.festivalpost.fragment.CustomFragment
import com.app.festivalpost.fragment.HomeFragment
import com.app.festivalpost.fragment.VideoFragment
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.show
import com.google.android.material.bottomnavigation.BottomNavigationView
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppBaseActivity() {
    var tvaction: TextView? = null
    var tvtitle: TextView? = null
    var mBottomNavigationView: BottomNavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        instance = this

        val mainHandler = Handler(Looper.getMainLooper())
        val runnable = Runnable { loadHomeFragment() }

        mainHandler.postDelayed(runnable, 0)
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
        toolbar.show()
        //tvtitle!!.text = resources.getString(R.string.txt_home)
    }


    private fun loadCustomFragment() {
        val fragment = CustomFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        toolbar.show()

    }

    private fun loadAccountFragment() {
        val fragment = AccountFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        toolbar.hide()

    }

    private fun loadVideoFragment() {
        val fragment = VideoFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        toolbar.show()

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
}