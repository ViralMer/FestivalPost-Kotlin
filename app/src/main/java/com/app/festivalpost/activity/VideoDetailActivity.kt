package com.app.festivalpost.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.AppBaseActivity

import com.app.festivalpost.R
import com.app.festivalpost.adapter.ChooseVideoAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.VideoLanguageItem
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.getCustomFrameList
import com.emegamart.lelys.utils.extensions.onClick
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.potyvideo.library.AndExoPlayerView
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class VideoDetailActivity : AppBaseActivity(), OnItemClickListener {
    var color = ""
    var rvdata: RecyclerView? = null
    private var videoView: AndExoPlayerView? = null
    var horizontalLayoutManagaer: GridLayoutManager? = null
    var savedBmp: Bitmap? = null
    var url: String? = null
    var p: ProgressDialog? = null
    var videoListItem: VideoLanguageItem? = null
    var videoListItemArrayList = arrayListOf<VideoLanguageItem?>()
    var tvframephone: TextView? = null
    var tvframephone1: TextView? = null
    var tvframeemail: TextView? = null
    var tvframeemail1: TextView? = null
    var tvframeweb: TextView? = null
    var tvframeweb1: TextView? = null
    var tvframelocation: TextView? = null
    var tvframelocation1: TextView? = null
    var tvframename: TextView? = null
    var tvframename1: TextView? = null
    var textEmail: TextView? = null
    var textWebsite: TextView? = null
    var textEmail1: TextView? = null
    var textWebsite1: TextView? = null
    var ivlogo: ImageView? = null
    var ivlogo1: ImageView? = null
    var frameLayout: FrameLayout? = null
    var frameLayout1: FrameLayout? = null
    var videoid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        openAddImageDialog()
        setActionbar()

        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        frameLayout1 = findViewById<View>(R.id.frame) as FrameLayout
        videoView = findViewById<View>(R.id.ivvideo) as AndExoPlayerView
        horizontalLayoutManagaer = GridLayoutManager(this@VideoDetailActivity, 4)
        rvdata!!.layoutManager = horizontalLayoutManagaer
        rvdata!!.itemAnimator = DefaultItemAnimator()
        val frame_view = layoutInflater.inflate(R.layout.video_save_image, null, false)
        frameLayout = frame_view.findViewById(R.id.frame)
        tvframephone = frame_view.findViewById(R.id.tvframephone)
        tvframephone1 = findViewById(R.id.tvframephone)
        tvframeemail = frame_view.findViewById(R.id.tvframeemail)
        tvframeemail1 = findViewById(R.id.tvframeemail)
        tvframeweb = frame_view.findViewById(R.id.tvframeweb)
        tvframeweb1 = findViewById(R.id.tvframeweb)
        tvframelocation = frame_view.findViewById(R.id.tvframelocation)
        tvframelocation1 = findViewById(R.id.tvframelocation)
        tvframename = frame_view.findViewById(R.id.tvframename)
        tvframename1 = findViewById(R.id.tvframename)
        textEmail = frame_view.findViewById(R.id.viewPhone)
        textEmail1 = findViewById(R.id.viewPhone)
        textWebsite = frame_view.findViewById(R.id.viewwebsite)
        textWebsite1 = findViewById(R.id.viewwebsite)
        ivlogo = frame_view.findViewById(R.id.ivframelogo1)
        ivlogo1 = findViewById(R.id.ivframelogo1)

        videoid = intent.getStringExtra("video_id")


        loadAccoutData()
        p = ProgressDialog(this@VideoDetailActivity)


//        int colorInt=Integer.parseInt(color);
    }

    /*fun setVideoData(videoData: VideoLanguageItem?) {

        Log.d(
            "BusinessData",
            "Business Mobile" + businessItem.busiMobile + "Email" + businessItem.busiEmail + "Nane:" + businessItem.busiName + "Address:" + businessItem.busiAddress + "Website:" + businessItem.busiWebsite
        )
        if (businessItem.busiName != "") {
            //Log.d("StringColorCode",""+colorInt);
            tvframename!!.visibility = View.VISIBLE
            tvframename1!!.visibility = View.VISIBLE
            tvframename!!.text = businessItem.busiName
            tvframename1!!.text = businessItem.busiName
            //tvframename!!.setTextColor(Color.parseColor(videoData!!.color))
            //tvframename1!!.setTextColor(Color.parseColor(videoData.color))
        }
        if (businessItem.busiLogo != "") {
            ivlogo!!.visibility = View.VISIBLE
            ivlogo1!!.visibility = View.VISIBLE
            //Glide.with(VideoDetailActivity.this).load(businessItem.getBusiLogo()).into(ivlogo);
            Log.d("imageName", "" + businessItem.busiLogo)
            ivlogo1!!.setImageURI(Uri.parse("/storage/emulated/0/Imagename/logo.png"))
            ivlogo!!.setImageURI(Uri.parse("/storage/emulated/0/Imagename/logo.png"))
            url = businessItem.busiLogo
        }
        if (businessItem.busiMobile != "") {
            tvframephone!!.visibility = View.VISIBLE
            tvframephone1!!.visibility = View.VISIBLE
            tvframephone!!.text = businessItem.busiMobile
            tvframephone1!!.text = businessItem.busiMobile
            //tvframephone!!.setTextColor(Color.parseColor(videoData!!.color))
            //tvframephone1!!.setTextColor(Color.parseColor(videoData.color))
        }
        if (businessItem.busiAddress != "") {
            tvframelocation!!.visibility = View.VISIBLE
            tvframelocation1!!.visibility = View.VISIBLE
            tvframelocation!!.text = businessItem.busiAddress
            tvframelocation1!!.text = businessItem.busiAddress
            *//*tvframelocation!!.setTextColor(Color.parseColor(videoData!!.color))
            tvframelocation1!!.setTextColor(Color.parseColor(videoData.color))*//*
        }
        if (businessItem.busiEmail != "") {
            tvframeemail!!.visibility = View.VISIBLE
            tvframeemail1!!.visibility = View.VISIBLE
            tvframeemail!!.text = businessItem.busiEmail
            tvframeemail1!!.text = businessItem.busiEmail
            *//*tvframeemail!!.setTextColor(Color.parseColor(videoData!!.color))
            tvframeemail1!!.setTextColor(Color.parseColor(videoData.color))*//*
            textEmail!!.visibility = View.VISIBLE
            //textEmail!!.setTextColor(Color.parseColor(videoData.color))
            textEmail1!!.visibility = View.VISIBLE
            //textEmail1!!.setTextColor(Color.parseColor(videoData.color))
        }
        if (businessItem.busiWebsite != "") {
            tvframeweb!!.visibility = View.VISIBLE
            tvframeweb1!!.visibility = View.VISIBLE
            tvframeweb!!.text = businessItem.busiWebsite
            tvframeweb1!!.text = businessItem.busiWebsite
            *//*tvframeweb!!.setTextColor(Color.parseColor(videoData!!.color))
            tvframeweb1!!.setTextColor(Color.parseColor(videoData.color))*//*
            textWebsite!!.visibility = View.VISIBLE
            //textWebsite!!.setTextColor(Color.parseColor(videoData.color))
            textWebsite1!!.visibility = View.VISIBLE
            //textWebsite1!!.setTextColor(Color.parseColor(videoData.color))
        }
        frameLayout!!.isDrawingCacheEnabled = true
        frameLayout!!.buildDrawingCache(true)
        savedBmp = getBitmapFromView(frameLayout)

        saveImage(savedBmp)
        frameLayout!!.isDrawingCacheEnabled = false
    }*/

    private fun getBitmapFromView(view: View?): Bitmap {
        view!!.measure(view.width, view.height)
        val bitmap = Bitmap.createBitmap(1080, 1080, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        view.layout(0, 0, 1080, 1080)
        view.draw(canvas)
        return bitmap
    }

    var tvaction: TextView? = null
    override fun onResume() {
        super.onResume()
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        val ivBack = toolbar.findViewById<View>(R.id.ivBack) as AppCompatImageView
        val tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView

            tvtitle.text = resources.getString(R.string.txt_add_business)

        ivBack.onClick {
            onBackPressed()
        }
        tvaction.setOnClickListener {
            val builder: AlertDialog.Builder = AlertDialog.Builder(this)
            builder.setTitle(resources.getString(R.string.txt_delete_title))
                .setMessage(resources.getString(R.string.txt_delete_message))
                .setPositiveButton(resources.getString(R.string.txt_yes)) { dialog, which ->



                }
                .setNegativeButton(resources.getString(R.string.txt_no)) { dialog, which -> dialog.dismiss() }
                .show()
        }
    }

    fun animateButton() {
        val myAnim = AnimationUtils.loadAnimation(this@VideoDetailActivity, R.anim.bounce)
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator
        tvaction!!.startAnimation(myAnim)
    }



    var video_path = ""
    override fun onItemClicked(`object`: Any?, index: Int) {
        videoListItem = `object` as VideoLanguageItem
        //setVideoData(videoListItem)
        if (videoListItem!!.image != null && !videoListItem!!.image.equals(
                "",
                ignoreCase = true
            )
        ) {
            video_path = videoListItem!!.video!!
            videoView!!.setSource(video_path)
            frameLayout1!!.visibility = View.VISIBLE

        }
    }




    private fun fillData() {
        if (videoListItemArrayList.size > 0) {

        }
    }

    private val videoName: String
        private get() = "video_demo.mp4"

    private fun download() {
        val DB = DownloadVideo()
        DB.execute("")
    }

    private inner class DownloadVideo : AsyncTask<String?, String?, String?>() {
        override fun onPreExecute() {
            super.onPreExecute()
            Global.showProgressDialog(this@VideoDetailActivity)
        }

        override fun doInBackground(vararg p0: String?): String? {
            val vidurl = video_path
            downloadfile(vidurl)
            return null
        }

        override fun onPostExecute(s: String?) {
            Global.dismissProgressDialog(this@VideoDetailActivity)
            super.onPostExecute(s)
        }
    }


    private fun downloadfile(vidurl: String) {
        try {
            val url = URL(vidurl)
            val c = url.openConnection() as HttpURLConnection
            c.requestMethod = "GET"
            c.doOutput = true
            c.connect()
            val videoname = "/data/data/com.app.festivalpost/files/$videoName"
            val videoname1 = videoName
            Global.storePreference("video_name", videoname)
            val file: File
            var fileOutputStream: FileOutputStream? = null
            try {
                file = filesDir
                fileOutputStream = openFileOutput(videoName, MODE_PRIVATE) //MODE PRIVATE
                val `in` = c.inputStream
                val buffer = ByteArray(1024)
                var len1 = 0
                while (`in`.read(buffer).also { len1 = it } > 0) {
                    fileOutputStream.write(buffer, 0, len1)
                }
                fileOutputStream.close()
            } catch (ex: Exception) {
                ex.printStackTrace()
            } finally {
                try {
                    fileOutputStream!!.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            Log.d("ImageName and VideoName", "" + Global.getPreference("image_name", ""))
            val intent = Intent(this, VideoCreateActivity::class.java)
            intent.putExtra("video_item", videoListItem)
            startActivity(intent)
            finish()


        } catch (e: IOException) {
            Log.d("Error....", e.toString())
            Global.dismissProgressDialog(this@VideoDetailActivity)
        }
    }

    private fun saveImage(finalBitmap: Bitmap?): String {
        val root = Environment.getExternalStorageDirectory().absolutePath
        val myDir = File("$root/Imagename")
        myDir.mkdirs()
        myDir.mkdir()
        val fname = "Image-Bitmap" + ".png"
        val file = File(myDir, fname)
        Global.storePreference("image_name", file.absolutePath)
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            finalBitmap!!.compress(Bitmap.CompressFormat.PNG, 100, out)
            out.flush()
            out.close()
            val filePath = file.absolutePath
            Log.d("Filepath", "" + filePath.toString())
            return filePath
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    override fun onDestroy() {
        Global.dismissProgressDialog(this)
        super.onDestroy()
    }

    fun openAddImageDialog() {
        Dexter.withContext(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                    } else {
                        //showSettingsDialog();
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest>,
                    token: PermissionToken
                ) {
                    token.continuePermissionRequest()
                }
            }).check()
    }


    private fun loadAccoutData() {
        showProgress(true)
        callApi(

            getRestApis().getVideoLanguageData(videoid!!, "0"), onApiSuccess = {
                showProgress(false)
                Log.d("videoItemList", "" + it.data.size)
                val adapter = ChooseVideoAdapter(this@VideoDetailActivity, it.data)
                rvdata!!.adapter = adapter
                videoListItemArrayList=it.data
                //new PlayVideo().execute();
                videoListItem = videoListItemArrayList[0]
                //setVideoData(videoListItem)
                if (videoListItem!!.image != null && !videoListItem!!.image.equals(
                        "",
                        ignoreCase = true
                    )
                ) {
                    video_path = videoListItem!!.video!!
                    videoView!!.setSource(video_path)
                    frameLayout1!!.visibility = View.VISIBLE
                }

            }, onApiError = {
                showProgress(false)

            }, onNetworkError = {
                showProgress(false)

            })
    }
}