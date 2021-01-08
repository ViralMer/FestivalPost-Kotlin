package com.app.festivalpost.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.ChoosePhotoActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.VideoCreateActivity
import com.app.festivalpost.activity.VideoDetailActivity
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.HomePageItem
import com.app.festivalpost.models.VideoItem
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class DayVideoAdapter(var context: Context, var originaldata: ArrayList<VideoItem?>) :
    RecyclerView.Adapter<DayVideoAdapter.ViewHolder>() {
    var searchCount = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.custom_day_item, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val festivalItem = originaldata[position]
        if (!festivalItem!!.video_image.equals(
                "",
                ignoreCase = true
            )
        ) {
            Glide.with(context).load(festivalItem.video_image).placeholder(R.drawable.placeholder_img)
                .error(
                    R.drawable.placeholder_img
                ).into(holder.ivphoto)
        }
        holder.tvname.text = festivalItem.video_date
        holder.tvdate.text = festivalItem.video_date
        holder.layMain.tag = position
        holder.layMain.setOnClickListener { view ->
            val c = Calendar.getInstance()

            val detailact = Intent(context, VideoDetailActivity::class.java)
            detailact.putExtra("object", festivalItem)
            detailact.putExtra("video_id", festivalItem.video_id)
            detailact.putExtra("video_date", festivalItem.video_date)
            context.startActivity(detailact)

        }
    }

    override fun getItemCount(): Int {
        return originaldata.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val layMain: LinearLayout = view.findViewById<View>(R.id.lay_main) as LinearLayout
        val ivphoto: RoundedImageView = view.findViewById<View>(R.id.ivphoto) as RoundedImageView
        val tvname: TextView = view.findViewById<View>(R.id.tvname) as TextView
        val tvdate: TextView = view.findViewById<View>(R.id.tvdate) as TextView

    }

    fun getCountOfDays(createdDateString: String?, expireDateString: String?): Int {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        var createdConvertedDate: Date? = null
        var expireCovertedDate: Date? = null
        var todayWithZeroTime: Date? = null
        try {
            createdConvertedDate = dateFormat.parse(createdDateString)
            expireCovertedDate = dateFormat.parse(expireDateString)
            val today = Date()
            todayWithZeroTime = dateFormat.parse(dateFormat.format(today))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var cYear = 0
        var cMonth = 0
        var cDay = 0
        if (createdConvertedDate!!.after(todayWithZeroTime)) {
            val cCal = Calendar.getInstance()
            cCal.time = createdConvertedDate
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        } else {
            val cCal = Calendar.getInstance()
            cCal.time = todayWithZeroTime
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        }
        val eCal = Calendar.getInstance()
        eCal.time = expireCovertedDate
        val eYear = eCal[Calendar.YEAR]
        val eMonth = eCal[Calendar.MONTH]
        val eDay = eCal[Calendar.DAY_OF_MONTH]
        val date1 = Calendar.getInstance()
        val date2 = Calendar.getInstance()
        date1.clear()
        date1[cYear, cMonth] = cDay
        date2.clear()
        date2[eYear, eMonth] = eDay
        val diff = date2.timeInMillis - date1.timeInMillis
        val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
        return dayCount.toInt()
    }
}