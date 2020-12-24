package com.app.festivalpost.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.adapter.DayAdapter
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.onClick
import com.emegamart.lelys.utils.extensions.show
import java.util.*

class FestivalViewAllActivitty : AppBaseActivity() {
    private var lvdata: RecyclerView? = null
    private var btnchoosedate: Button? = null
    var picker: DatePickerDialog? = null
    var dateVal = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_festival_view_all_activitty)
        setActionbar()
        lvdata = findViewById<View>(R.id.lvdata) as RecyclerView
        btnchoosedate = findViewById<View>(R.id.btnchoosedate) as Button
        loadgetDaysAllData()

        btnchoosedate!!.onClick {
            val cldr = Calendar.getInstance()
            val day = cldr[Calendar.DAY_OF_MONTH]
            val month = cldr[Calendar.MONTH]
            val year = cldr[Calendar.YEAR]
            // date picker dialog
            // date picker dialog
            picker = DatePickerDialog(
                this@FestivalViewAllActivitty,
                { view, year, monthOfYear, dayOfMonth ->
                    btnchoosedate!!.text =
                        dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year
                    var strDay = dayOfMonth.toString()
                    var strMonth = ""
                    if (dayOfMonth < 12) {
                        strDay = "0$dayOfMonth"
                    }
                    if (monthOfYear + 1 < 12) {
                        strMonth = (monthOfYear + 1).toString()
                    }
                    dateVal = "$year-$strMonth-$strDay"
                    loadgetDaysAllData()
                }, year, month, day
            )
            picker!!.show()
        }

    }
    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ivBack = toolbar.findViewById<View>(R.id.ivBack) as ImageView
        ivBack.onClick { onBackPressed() }

    }

    private fun loadgetDaysAllData()
    {
        showProgress(true)
        callApi(
            getRestApis().getdays(dateVal), onApiSuccess = {
                showProgress(false)
                lvdata!!.show()
                val adapter = DayAdapter(this, it.festival)
                lvdata!!.adapter = adapter


            }, onApiError = {
                showProgress(false)
                lvdata!!.hide()

            }, onNetworkError = {
                showProgress(false)
                lvdata!!.hide()

            })
    }
}