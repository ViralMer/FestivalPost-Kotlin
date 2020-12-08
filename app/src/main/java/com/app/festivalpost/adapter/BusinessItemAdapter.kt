package com.app.festivalpost.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.app.festivalpost.activity.OnItemClickListener
import com.app.festivalpost.activity.PremiumActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.AddBusinessActivity
import com.app.festivalpost.models.BusinessItem
import com.app.festivalpost.models.UserItem
import com.bumptech.glide.Glide
import java.util.*

class BusinessItemAdapter(
    private val context: Context,
    objects: ArrayList<BusinessItem>,
    userItem: UserItem
) : BaseAdapter() {
    private var objects = ArrayList<BusinessItem>()
    private val layoutInflater: LayoutInflater
    var userItem: UserItem
    var onItemClickListener: OnItemClickListener
    override fun getCount(): Int {
        return objects.size
    }

    override fun getItem(position: Int): BusinessItem {
        return objects[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View, parent: ViewGroup): View {
        var convertView = convertView
        initializeViews(getItem(position), convertView.tag as ViewHolder)
        return convertView
    }

    private fun initializeViews(`object`: BusinessItem, holder: ViewHolder) {
        holder.tvheading.text = `object`.busiName
        holder.tvdescription.text = "" + `object`.planName
        holder.tvexpiration.text = "" + `object`.purcEndDate
        if (`object`.busiLogo != null) {
            Glide.with(context).load(`object`.busiLogo).placeholder(R.drawable.placeholder_img)
                .error(
                    R.drawable.placeholder_img
                ).into(holder.ivlogo)
        }
        if (`object`.isCurrentBusiness) {
            holder.ivcurrentbusiness.visibility = View.VISIBLE
        } else {
            holder.ivcurrentbusiness.visibility = View.GONE
        }
        if (`object`.need_to_upgrade == "1") {
            holder.tvupgrade.visibility = View.VISIBLE
        } else {
            holder.tvupgrade.visibility = View.GONE
        }
        holder.tvupgrade.tag = `object`
        holder.tvupgrade.setOnClickListener { view ->
            val b = view.tag as BusinessItem
            val detailact = Intent(context, PremiumActivity::class.java)
            detailact.putExtra("userItem", userItem)
            detailact.putExtra("object", b)
            context.startActivity(detailact)
        }
        holder.tvedit.tag = `object`
        holder.tvedit.setOnClickListener { v ->
            val b = v.tag as BusinessItem
            val detailact = Intent(context, AddBusinessActivity::class.java)
            detailact.putExtra("object", b)
            context.startActivity(detailact)
        }
        holder.laymain.tag = `object`
        holder.laymain.setOnClickListener { view ->
            val b = view.tag as BusinessItem

            val materialAlertDialogBuilder = AlertDialog.Builder(
                context
            )
            val inflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            val view1 = inflater.inflate(R.layout.custom_business_dialog, null)
            val tvMessage: TextView
            val btnOk: Button
            val btnCancel: Button
            tvMessage = view1.findViewById(R.id.tvMessage)
            btnOk = view1.findViewById(R.id.btnOk)
            btnCancel = view1.findViewById(R.id.btnCancel)
            tvMessage.text = "Set Your Current Business"
            materialAlertDialogBuilder.setView(view1).setCancelable(true)
            val b1 = materialAlertDialogBuilder.create()
            btnOk.setOnClickListener {
                onItemClickListener.onItemClicked(b, 0)
                b1.dismiss()
            }
            btnCancel.setOnClickListener { b1.dismiss() }
            b1.show()
        }
    }

    protected inner class ViewHolder(view: View) {
        val laymain: LinearLayout
        val ivlogo: ImageView
        val ivcurrentbusiness: ImageView
        val tvheading: TextView
        val tvdescription: TextView
        val tvexpiration: TextView
        val tvupgrade: TextView
        val tvedit: TextView

        init {
            laymain = view.findViewById(R.id.laymain)
            ivlogo = view.findViewById<View>(R.id.ivlogo) as ImageView
            ivcurrentbusiness = view.findViewById<View>(R.id.ivcurrentbusiness) as ImageView
            tvheading = view.findViewById<View>(R.id.tvheading) as TextView
            tvdescription = view.findViewById<View>(R.id.tvdescription) as TextView
            tvexpiration = view.findViewById<View>(R.id.tvexpiration) as TextView
            tvupgrade = view.findViewById<View>(R.id.tvupgrade) as TextView
            tvedit = view.findViewById<View>(R.id.tvedit) as TextView
        }
    }

    init {
        this.objects = objects
        this.userItem = userItem
        onItemClickListener = context as OnItemClickListener
        layoutInflater = LayoutInflater.from(context)
    }
}