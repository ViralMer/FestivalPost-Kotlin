package com.app.festivalpost.activity

import android.app.DatePickerDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatEditText
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.adapter.CustomBusinessCategoryItemAdapter
import com.app.festivalpost.models.BusinessCategoryItem
import com.app.festivalpost.utils.Constants.SharedPref.USER_TOKEN
import com.app.festivalpost.utils.SessionManager
import com.emegamart.lelys.utils.extensions.onClick
import java.util.*
import kotlin.collections.ArrayList


class CategoryViewAllActivitty : AppBaseActivity() {
    private var lvdata: RecyclerView? = null
    private var et_search: AppCompatEditText? = null
    private var daylist = arrayListOf<BusinessCategoryItem?>()
    private var dayAdapter: CustomBusinessCategoryItemAdapter? = null
    var picker: DatePickerDialog? = null
    var dateVal = ""
    var sessionManager: SessionManager?=null
    var token : String?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_view_all_activity)
        setActionbar()
        sessionManager= SessionManager(this)
        token=sessionManager!!.getValueString(USER_TOKEN)
        lvdata = findViewById<View>(R.id.lvdata) as RecyclerView
        et_search = findViewById<View>(R.id.et_search) as AppCompatEditText


        if(intent!=null) {
            daylist = intent.getSerializableExtra("category_list") as ArrayList<BusinessCategoryItem?>
        }
        dayAdapter= CustomBusinessCategoryItemAdapter(this, daylist)
        lvdata!!.adapter = dayAdapter
        et_search!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable) {
                Log.d("fdsfdsfs", s.toString());
                /*dayAdapter!!.getFilter()!!.filter(s.toString())
                dayAdapter!!.notifyDataSetChanged()*/
                filter(s.toString())
            }
        })





    }


    private fun filter(text: String) {
        val filteredList: ArrayList<BusinessCategoryItem?> = arrayListOf()
        for (item in daylist) {
            if (item!!.category_name!!.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item)
            }
        }

        dayAdapter!!.filterList(filteredList)
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        val ivBack = toolbar.findViewById<View>(R.id.ivBack) as ImageView
        ivBack.onClick { onBackPressed() }

    }


}