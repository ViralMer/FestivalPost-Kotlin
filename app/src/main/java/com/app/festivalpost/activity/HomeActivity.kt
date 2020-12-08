package com.app.festivalpost.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R
import com.app.festivalpost.fragment.AccountFragment
import com.app.festivalpost.fragment.CustomFragment
import com.app.festivalpost.fragment.DaysFragment
import com.app.festivalpost.fragment.HomeFragment
import com.app.festivalpost.utility.NetworkStateChecker
import com.google.android.material.bottomnavigation.BottomNavigationView

class HomeActivity : AppCompatActivity() {
    var tvaction: TextView? = null
    var tvtitle: TextView? = null
    var mBottomNavigationView: BottomNavigationView? = null
    var networkStateChecker: NetworkStateChecker? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setActionbar()
        tvaction!!.setOnClickListener {
            try {
                val detailact = Intent(this@HomeActivity, ChooseFrameActivityNew::class.java)
                startActivity(detailact)
            } catch (e: Exception) {
            }
        }
        instance = this
        setupBottomNavigation()
        if (savedInstanceState == null) {
            loadHomeFragment()
        }

        /*final FirebaseRemoteConfig remoteConfig=FirebaseRemoteConfig.getInstance();

        Map<String,Object> defaultValue=new HashMap<>();
        defaultValue.put(UpdateHelper.KEY_UPDATE_ENABLE,true);
        defaultValue.put(UpdateHelper.KEY_UPDATE_VERSION,"6.9");
        defaultValue.put(UpdateHelper.KEY_UPDATE_URL,"https://play.google.com/store/apps/details?id=com.app.festivalpost");

        //remoteConfig.setDefaults(defaultValue);

        FirebaseRemoteConfigSettings remoteConfigSettings=new FirebaseRemoteConfigSettings.Builder().setFetchTimeoutInSeconds(5).build();
        remoteConfig.setConfigSettingsAsync(remoteConfigSettings);
        remoteConfig.setDefaultsAsync(defaultValue);

        remoteConfig.fetchAndActivate().addOnCompleteListener(new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> task) {
                if (task.isSuccessful())
                {

                    AlertDialog builder=new AlertDialog.Builder(HomeActivity.this).setTitle("Need To Upgrade").setMessage("Please Update to new version to continue use").setPositiveButton("Update", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            try {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.app.festivalpost")));
                            }
                             catch (ActivityNotFoundException e)
                             {
                                 startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=com.app.festivalpost")));
                             }
                            dialog.dismiss();

                        }
                    }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    }).create();
                    builder.show();
                }
            }
        });*/
    }

    override fun onStart() {
        super.onStart()
    }

    private fun setupBottomNavigation() {
        mBottomNavigationView = findViewById<View>(R.id.bottom_navigation) as BottomNavigationView
        mBottomNavigationView!!.setOnNavigationItemSelectedListener(BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.action_home -> {
                    loadHomeFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_days -> {
                    loadDaysFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_post -> {
                    loadCustomFragment()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.action_account -> {
                    loadAccountFragment()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        })
    }

    fun loadHomeFragment() {
        tvaction!!.visibility = View.VISIBLE
        val fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        tvtitle!!.text = resources.getString(R.string.txt_home)
    }

    fun loadHomeFragment1() {
        tvaction!!.visibility = View.VISIBLE
        val fragment = HomeFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        tvtitle!!.text = resources.getString(R.string.txt_home)
    }

    private fun loadDaysFragment() {
        tvaction!!.visibility = View.INVISIBLE
        val fragment = DaysFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        tvtitle!!.text = resources.getString(R.string.txt_festival)
    }



    private fun loadCustomFragment() {
        tvaction!!.visibility = View.INVISIBLE
        val fragment = CustomFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        tvtitle!!.text = resources.getString(R.string.txt_custom)
    }

    private fun loadAccountFragment() {
        tvaction!!.visibility = View.INVISIBLE
        val fragment = AccountFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .commit()
        tvtitle!!.text = resources.getString(R.string.txt_account)
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvtitle!!.text = resources.getString(R.string.txt_home)
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

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        var instance: HomeActivity? = null
    }
}