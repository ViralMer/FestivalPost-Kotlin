package com.app.festivalpost.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.R
import com.app.festivalpost.activity.ChooseBusinessCategoryPhotoActivity
import com.app.festivalpost.models.BusinessCategoryItem
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.*
import com.makeramen.roundedimageview.RoundedImageView

class CustomBusinessCategoryItemAdapter(
    var context: Context,
    var originaldata: ArrayList<BusinessCategoryItem?>
) :
    RecyclerView.Adapter<CustomBusinessCategoryItemAdapter.ViewHolder>(),Filterable {
    var searchCount = 0
    var businessCategoryItemList= ArrayList<BusinessCategoryItem?>()


    // exampleListFull . exampleList

    init {
        businessCategoryItemList = originaldata as ArrayList<BusinessCategoryItem?>
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view: View? = null
        view = LayoutInflater.from(parent.context).inflate(R.layout.item_business_category, null)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val homePageItem = originaldata!![position]

        Glide.with(context).load(homePageItem!!.image).error(R.drawable.placeholder_img).placeholder(
            R.drawable.placeholder_img
        ).into(holder.imageView)
        //holder.imageView.loadImageFromUrl(homePageItem!!.ima!!)
        holder.tvTitle.text=homePageItem.category_name


        holder.itemView.onClick {
            context.launchActivity<ChooseBusinessCategoryPhotoActivity> {
                putExtra("category_id", homePageItem.id)
                putExtra("category_name", homePageItem.category_name)

            }
        }



    }

    override fun getItemCount(): Int {
        return originaldata.size
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                if (charSearch.isEmpty()) {
                    businessCategoryItemList = originaldata as ArrayList<BusinessCategoryItem?>
                } else {
                    val resultList = ArrayList<BusinessCategoryItem?>()
                    for (row in originaldata) {
                        if (row!!.category_name!!.toLowerCase().contains(
                                constraint.toString().toLowerCase()
                            )) {
                            resultList.add(row)
                        }
                    }
                    businessCategoryItemList = resultList
                }
                val filterResults = FilterResults()
                filterResults.values = businessCategoryItemList
                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                businessCategoryItemList = results?.values as ArrayList<BusinessCategoryItem?>
                notifyDataSetChanged()
            }
        }
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvTitle: TextView = view.findViewById<View>(R.id.tvTitle) as TextView
        val tvNew: TextView = view.findViewById<View>(R.id.tvDate) as TextView
        val imageView: RoundedImageView = view.findViewById<View>(R.id.ivphoto) as RoundedImageView

    }

    fun filterList(filteredList: ArrayList<BusinessCategoryItem?>) {
        originaldata = filteredList
        notifyDataSetChanged()
    }


}