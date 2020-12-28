package com.app.festivalpost.fragment

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.R
import com.app.festivalpost.adapter.PostAdapter
import com.app.festivalpost.models.FileListItem
import com.emegamart.lelys.utils.extensions.hide
import com.emegamart.lelys.utils.extensions.show
import java.io.File
import java.util.*
import kotlin.collections.ArrayList

class MyPostFragment : BaseFragment() {

    var dataArrayList = ArrayList<FileListItem>()
    var adapter: PostAdapter? = null
    var linearLayout: LinearLayout? = null
    var rvdata: RecyclerView? = null
    var btnCreatePost: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_my_post, container, false)

        rvdata = view.findViewById<View>(R.id.rvdata) as RecyclerView
        val mLayoutManager = GridLayoutManager(activity!!, 3)
        rvdata!!.layoutManager = mLayoutManager

        linearLayout = view.findViewById(R.id.rlNoData)




        /*Log.d("data arraylist", "" + dataArrayList.size)
        val path: String =
            Environment.getExternalStorageDirectory().toString().toString() + "/FestivalPost"
        Log.d("Files", "Path: $path")
        val directory = File(path)
        val files: Array<File> = directory.listFiles()

        Log.d("Files", "Size: " + files.size)
        val filenames = ArrayList<String>()
        for (i in files.indices) {
            val file=files[i]
            if (filenames.isNotEmpty()) {
                linearLayout!!.hide()
                if (file.isFile && file.path.endsWith(".jpg")) {
                    Log.d("Files", "FileName:" + files[i].name)
                    filenames.add(files[i].name)
                    dataArrayList.add(FileListItem(files[i].name))

                }
            }
            else{
                linearLayout!!.show()
            }

            adapter = PostAdapter(activity!!, dataArrayList)
            rvdata!!.adapter = adapter
        }*/
        PhotoItemList().execute();
        return view
    }


    @SuppressLint("StaticFieldLeak")
    internal inner class PhotoItemList(

    ) : AsyncTask<String?, Void?, ArrayList<FileListItem>?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            showProgress()
        }

        override fun doInBackground(vararg p0: String?): ArrayList<FileListItem>? {
            dataArrayList = arrayListOf<FileListItem>()
            Log.d("data arraylist", "" + dataArrayList.size)
            val path: String =
                Environment.getExternalStorageDirectory().toString().toString() + "/FestivalPost"
            Log.d("Files", "Path: $path")
            val directory = File(path)
            val files: Array<File> = directory.listFiles()

            Log.d("Files", "Size: " + files.size)
            val filenames = java.util.ArrayList<String>()
            for (i in files.indices) {
                val file = files[i]
                    if (files.isNotEmpty())
                    {
                    linearLayout!!.hide()
                    if (file.isFile && file.path!!.endsWith(".jpg")) {
                        Log.d("Files", "FileName:" + files[i].name)
                        filenames.add(files[i].name)
                        dataArrayList.add(FileListItem(files[i].name))

                    }
                } else {
                    linearLayout!!.show()
                }


            }
            return dataArrayList
        }

        override fun onPostExecute(result: ArrayList<FileListItem>?) {
            super.onPostExecute(result)
            hideProgress()
            adapter = PostAdapter(activity!!, dataArrayList)
            rvdata!!.adapter = adapter
        }

    }

}