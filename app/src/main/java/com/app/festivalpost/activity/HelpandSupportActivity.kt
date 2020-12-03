package com.app.festivalpost.activity

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R
import com.app.festivalpost.globals.Constant

class HelpandSupportActivity : AppCompatActivity() {
    private var lay_email: LinearLayout? = null
    private var lay_call: LinearLayout? = null
    private var lay_website: LinearLayout? = null
    private var icwhatsapp: LinearLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_help_support)
        setActionbar()
        lay_email = findViewById<View>(R.id.lay_email) as LinearLayout
        lay_call = findViewById<View>(R.id.lay_call) as LinearLayout
        lay_website = findViewById<View>(R.id.lay_website) as LinearLayout
        icwhatsapp = findViewById(R.id.lay_whatsapp)
        lay_email!!.setOnClickListener {
            val subject = resources.getString(R.string.txt_help_support)
            val content = ""
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "plain/text"
            intent.putExtra(Intent.EXTRA_EMAIL, arrayOf(Constant.ADMIN_MAIL))
            intent.putExtra(Intent.EXTRA_SUBJECT, subject)
            intent.putExtra(Intent.EXTRA_TEXT, content)
            startActivity(intent)
        }
        lay_call!!.setOnClickListener {
            val u = Uri.parse("tel:" + Constant.ADMIN_PHONE)
            val i = Intent(Intent.ACTION_DIAL, u)
            try {
                startActivity(i)
            } catch (s: SecurityException) {
                Toast.makeText(this@HelpandSupportActivity, s.message, Toast.LENGTH_LONG)
                    .show()
            }
        }
        icwhatsapp!!.setOnClickListener(View.OnClickListener {
            val url =
                "https://api.whatsapp.com/send?phone=917686894444&text=Inquiry from FestivalPost&source=&data=&app_absent="
            try {
                val pm = applicationContext.packageManager
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                i.putExtra(Intent.EXTRA_TEXT, "Inquiry from FestivalPost")
                startActivity(i)
            } catch (e: PackageManager.NameNotFoundException) {
                Toast.makeText(
                    this@HelpandSupportActivity,
                    "Whatsapp app not installed in your phone",
                    Toast.LENGTH_SHORT
                ).show()
                e.printStackTrace()
            }
        })
        lay_website!!.setOnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(Constant.ADMIN_WEBSITE))
            startActivity(browserIntent)
        }
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_help_support)
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
}