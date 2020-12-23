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
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.models.FileListItem
import com.app.festivalpost.models.PostItem
import java.io.File
import java.util.*

class MyPostActivity : AppBaseActivity() {

    var dataArrayList = ArrayList<FileListItem>()
    var adapter: PostAdapter? = null
    var linearLayout: LinearLayout? = null
    var rvdata: RecyclerView? = null
    var btnCreatePost: Button? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_post)
        setActionbar()

        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        val mLayoutManager = GridLayoutManager(this@MyPostActivity, 3)
        rvdata!!.layoutManager = mLayoutManager

        linearLayout = findViewById(R.id.rlNoData)
        btnCreatePost = findViewById(R.id.btnCreatePost)



        Log.d("data arraylist", "" + dataArrayList.size)
        val path: String =
            Environment.getExternalStorageDirectory().toString().toString() + "/FestivalPost"
        Log.d("Files", "Path: $path")
        val directory = File(path)
        val files: Array<File> = directory.listFiles()

        Log.d("Files", "Size: " + files.size)
        val filenames = ArrayList<String>()
        for (i in files.indices) {
            val file=files[i]
            if (file.isFile && file.path.endsWith(".jpg"))
            {
                Log.d("Files", "FileName:" + files[i].name)
                filenames.add(files[i].name)
                dataArrayList.add(FileListItem(files[i].name))

            }

            adapter = PostAdapter(applicationContext, dataArrayList)
            rvdata!!.adapter = adapter
        }

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



}