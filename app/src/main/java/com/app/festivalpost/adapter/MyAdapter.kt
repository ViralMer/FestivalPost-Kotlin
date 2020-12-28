package com.app.festivalpost.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.app.festivalpost.fragment.HomeFragment
import com.app.festivalpost.fragment.MyPostFragment
import com.app.festivalpost.fragment.MyVideoFragment

class MyAdapter(private val myContext: Context, fm: FragmentManager, internal var totalTabs: Int) : FragmentPagerAdapter(fm) {
  
    // this is for fragment tabs  
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> {
                //  val homeFragment: HomeFragment = HomeFragment()
                MyPostFragment()
            }
            1 -> {
                MyVideoFragment()
            }
            else -> null!!
        }
    }  
  
    // this counts total number of tabs  
    override fun getCount(): Int {  
        return totalTabs  
    }  
}  