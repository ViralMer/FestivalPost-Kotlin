package com.app.festivalpost.fragment

import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.fragment.app.Fragment
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
import java.util.ArrayList

class MyVideoFragment : BaseFragment() {
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
        val view= inflater.inflate(R.layout.fragment_my_video, container, false)
        rvdata = view.findViewById<View>(R.id.rvdata) as RecyclerView
        val mLayoutManager = GridLayoutManager(activity!!, 3)
        rvdata!!.layoutManager = mLayoutManager

        linearLayout = view.findViewById(R.id.rlNoData)



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
            if (files.isNotEmpty()) {
                linearLayout!!.hide()
                if (file.isFile && file.path.endsWith(".mp4")) {
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
        }
        return view
    }


}