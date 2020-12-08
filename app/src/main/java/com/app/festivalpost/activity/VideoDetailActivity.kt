package com.app.festivalpost.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.app.festivalpost.activity.MyBounceInterpolator

import com.app.festivalpost.R
import com.app.festivalpost.activity.VideoCreateActivity
import com.app.festivalpost.adapter.ChooseVideoAdapter
import com.app.festivalpost.apifunctions.ApiEndpoints
import com.app.festivalpost.apifunctions.ApiManager
import com.app.festivalpost.apifunctions.ApiResponseListener
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.VideoListItem
import com.google.gson.Gson
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.potyvideo.library.AndExoPlayerView
import org.json.JSONObject
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class VideoDetailActivity : AppCompatActivity(), ApiResponseListener,OnItemClickListener {
    var apiManager: ApiManager? = null
    var status = false
    var message = ""
    var color = ""
    var rvdata: RecyclerView? = null
    private var layroot: LinearLayout? = null

    //private VideoView videoView;
    private var videoView: AndExoPlayerView? = null
    var horizontalLayoutManagaer: GridLayoutManager? = null
    var savedBmp: Bitmap? = null
    var url: String? = null
    var p: ProgressDialog? = null
    var videoListItem: VideoListItem? = null
    var videoListItemArrayList = ArrayList<VideoListItem>()
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        openAddImageDialog()
        setActionbar()
        apiManager = ApiManager(this@VideoDetailActivity)
        layroot = findViewById<View>(R.id.layroot) as LinearLayout
        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        frameLayout1 = findViewById<View>(R.id.frame) as FrameLayout
        videoView = findViewById<View>(R.id.ivvideo) as AndExoPlayerView
        horizontalLayoutManagaer = GridLayoutManager(this@VideoDetailActivity, 4)
        rvdata!!.layoutManager = horizontalLayoutManagaer
        rvdata!!.itemAnimator = DefaultItemAnimator()
        Global.showProgressDialog(this@VideoDetailActivity)
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
        apiManager!!.getFestivalVideos(
            ApiEndpoints.getfestivalvideos,
            Global.getPreference(Constant.PREF_TOKEN, "")
        )
        p = ProgressDialog(this@VideoDetailActivity)


//        int colorInt=Integer.parseInt(color);
    }

    fun setVideoData(videoData: VideoListItem?) {
        val businessItem = Global.currentBusinessNEW
        Log.d(
            "BusinessData",
            "Business Mobile" + businessItem.busiMobile + "Email" + businessItem.busiEmail + "Nane:" + businessItem.busiName + "Address:" + businessItem.busiAddress + "Website:" + businessItem.busiWebsite
        )
        if (businessItem.busiName != "" && businessItem.busiName != null) {
            //Log.d("StringColorCode",""+colorInt);
            tvframename!!.visibility = View.VISIBLE
            tvframename1!!.visibility = View.VISIBLE
            tvframename!!.text = businessItem.busiName
            tvframename1!!.text = businessItem.busiName
            tvframename!!.setTextColor(Color.parseColor(videoData!!.color))
            tvframename1!!.setTextColor(Color.parseColor(videoData.color))
        }
        if (businessItem.busiLogo != "" && businessItem.busiLogo != null) {
            ivlogo!!.visibility = View.VISIBLE
            ivlogo1!!.visibility = View.VISIBLE
            //Glide.with(VideoDetailActivity.this).load(businessItem.getBusiLogo()).into(ivlogo);
            Log.d("imageName", "" + businessItem.busiLogo)
            ivlogo1!!.setImageURI(Uri.parse("/storage/emulated/0/Imagename/logo.png"))
            ivlogo!!.setImageURI(Uri.parse("/storage/emulated/0/Imagename/logo.png"))
            url = businessItem.busiLogo
        }


        //downloadFileImage(businessItem.getBusiLogo());
        if (businessItem.busiMobile != null && businessItem.busiMobile != "") {
            tvframephone!!.visibility = View.VISIBLE
            tvframephone1!!.visibility = View.VISIBLE
            tvframephone!!.text = businessItem.busiMobile
            tvframephone1!!.text = businessItem.busiMobile
            tvframephone!!.setTextColor(Color.parseColor(videoData!!.color))
            tvframephone1!!.setTextColor(Color.parseColor(videoData.color))
        }
        if (businessItem.busiAddress != null && businessItem.busiAddress != "") {
            tvframelocation!!.visibility = View.VISIBLE
            tvframelocation1!!.visibility = View.VISIBLE
            tvframelocation!!.text = businessItem.busiAddress
            tvframelocation1!!.text = businessItem.busiAddress
            tvframelocation!!.setTextColor(Color.parseColor(videoData!!.color))
            tvframelocation1!!.setTextColor(Color.parseColor(videoData.color))
        }
        if (businessItem.busiEmail != null && businessItem.busiEmail != "") {
            tvframeemail!!.visibility = View.VISIBLE
            tvframeemail1!!.visibility = View.VISIBLE
            tvframeemail!!.text = businessItem.busiEmail
            tvframeemail1!!.text = businessItem.busiEmail
            tvframeemail!!.setTextColor(Color.parseColor(videoData!!.color))
            tvframeemail1!!.setTextColor(Color.parseColor(videoData.color))
            textEmail!!.visibility = View.VISIBLE
            textEmail!!.setTextColor(Color.parseColor(videoData.color))
            textEmail1!!.visibility = View.VISIBLE
            textEmail1!!.setTextColor(Color.parseColor(videoData.color))
        }
        if (businessItem.busiWebsite != null && businessItem.busiWebsite != "") {
            tvframeweb!!.visibility = View.VISIBLE
            tvframeweb1!!.visibility = View.VISIBLE
            tvframeweb!!.text = businessItem.busiWebsite
            tvframeweb1!!.text = businessItem.busiWebsite
            tvframeweb!!.setTextColor(Color.parseColor(videoData!!.color))
            tvframeweb1!!.setTextColor(Color.parseColor(videoData.color))
            textWebsite!!.visibility = View.VISIBLE
            textWebsite!!.setTextColor(Color.parseColor(videoData.color))
            textWebsite1!!.visibility = View.VISIBLE
            textWebsite1!!.setTextColor(Color.parseColor(videoData.color))
        }
        frameLayout!!.isDrawingCacheEnabled = true
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.DONUT) {
            frameLayout!!.buildDrawingCache(true)
        }
        savedBmp = getBitmapFromView(frameLayout)
        //SaveImagetostoreinternal(savedBmp);
        SaveImage(savedBmp)
        frameLayout!!.isDrawingCacheEnabled = false
    }

    fun getBitmapFromView(view: View?): Bitmap {
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
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = resources.getString(R.string.txt_custom_video)
        tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvaction!!.text = resources.getString(R.string.txt_next)
        animateButton()
        tvaction!!.setOnClickListener { //videoView.stopPlayback();
            if (Global.getPreference(Constant.PREF_PREMIUM, false)) {
                download()
            } else {
                AlertDialog.Builder(this@VideoDetailActivity)
                    .setTitle("Sorry!!")
                    .setMessage("Please buy premium plan and save video.")
                    .setPositiveButton("Buy Premium") { dialog, which ->
                        val intent = Intent(this@VideoDetailActivity, PremiumActivity::class.java)
                        val businessItem = Global.currentBusinessNEW
                        intent.putExtra("videoData", businessItem)
                        startActivity(intent)
                    }
                    .setNegativeButton("Cancel") { dialog, which -> dialog.dismiss() }
                    .show()
            }

/*                Intent detailact = new Intent(VideoDetailActivity.this,ChooseFrameForPhotoActivityNew.class);
                //detailact.putExtra("photo_path",photo_path);
                startActivity(detailact);*/
        }
    }

    fun animateButton() {
        val myAnim = AnimationUtils.loadAnimation(this@VideoDetailActivity, R.anim.bounce)
        val interpolator = MyBounceInterpolator(0.2, 20.0)
        myAnim.interpolator = interpolator
        tvaction!!.startAnimation(myAnim)
    }

    override fun onErrorResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@VideoDetailActivity)
            Global.showFailDialog(this@VideoDetailActivity, responseString)
        }
    }

    fun processResponse(responseString: String?) {
        videoListItemArrayList = ArrayList()
        status = false
        message = ""
        try {
            val jsonObject = JSONObject(responseString)
            if (jsonObject.has("status")) {
                status = jsonObject.getBoolean("status")
            }
            if (jsonObject.has("color")) {
                color = jsonObject.getString("color")
                Log.d("colorcode", "" + color)
                tvframename!!.setTextColor(Color.parseColor(color))
                tvframename1!!.setTextColor(Color.parseColor(color))
            }
            if (jsonObject.has("message")) {
                message = jsonObject.getString("message")
            }
            if (jsonObject.has("data")) {
                val jsonArray = jsonObject.getJSONArray("data")
                if (jsonArray.length() > 0) {
                    for (i in 0 until jsonArray.length()) {
                        val j = jsonArray.getJSONObject(i)
                        val f = Gson().fromJson(j.toString(), VideoListItem::class.java)
                        val videoListItem = VideoListItem()
                        videoListItem.date_added = j.getString("date_added")
                        videoListItem.thumbnail = j.getString("thumbnail")
                        videoListItem.video_post_id = j.getInt("video_post_id")
                        videoListItem.video_url = j.getString("video_url")
                        videoListItem.color = j.getString("color")
                        videoListItem.date = j.getString("date")
                        try {
                            Log.d("DAteGEt", "" + j.getString("date"))
                        } catch (e: Exception) {
                        }
                        if (i == 0) {
                            f.setIs_selected(true)
                        } else {
                            f.setIs_selected(false)
                        }
                        videoListItemArrayList.add(videoListItem)
                        //videoListItemArrayList.add(f);
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    var video_path = ""
    override fun onItemClicked(`object`: Any?, index: Int) {
        videoListItem = `object` as VideoListItem
        setVideoData(videoListItem)
        if (videoListItem!!.thumbnail != null && !videoListItem!!.thumbnail.equals(
                "",
                ignoreCase = true
            )
        ) {
            video_path = videoListItem!!.video_url
            videoView!!.setSource(video_path)
            frameLayout1!!.visibility = View.VISIBLE
            /*Log.d("VideoView",""+videoView.getBufferPercentage());
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    Global.dismissProgressDialog(VideoDetailActivity.this);
                    setVideoData(videoListItem);
                    videoView.start();
                }
            });
*/
            //Glide.with(VideoDetailActivity.this).load(videoListItem.getThumbnail()).placeholder(R.drawable.placeholder_img).error(R.drawable.placeholder_img).into(ivbackground);
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
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

    override fun isConnected(requestService: String?, isConnected: Boolean) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@VideoDetailActivity)
            if (!isConnected) {
                Global.noInternetConnectionDialog(this@VideoDetailActivity)
            }
        }
    }

    override fun onSuccessResponse(
        requestService: String?,
        responseString: String?,
        responseCode: Int
    ) {
        Handler(Looper.getMainLooper()).post {
            Global.dismissProgressDialog(this@VideoDetailActivity)
            if (requestService.equals(ApiEndpoints.getfestivalvideos, ignoreCase = true)) {
                try {
                    Log.d("response", responseString!!)
                    processResponse(responseString)
                    if (status) {
                        fillData()
                    } else {
                        Toast.makeText(this@VideoDetailActivity, message, Toast.LENGTH_SHORT).show()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            animateButton()
        }
    }

    fun fillData() {

        /*if (photoItemArrayList.size()>3)
        {
            horizontalLayoutManagaer.setStackFromEnd(true);
        }*/
        Log.d("videoItemList", "" + videoListItemArrayList.size)
        if (videoListItemArrayList.size > 0) {
            val adapter = ChooseVideoAdapter(this@VideoDetailActivity, videoListItemArrayList)
            rvdata!!.adapter = adapter
            //new PlayVideo().execute();
            videoListItem = videoListItemArrayList[0]
            setVideoData(videoListItem)
            if (videoListItem!!.thumbnail != null && !videoListItem!!.thumbnail.equals(
                    "",
                    ignoreCase = true
                )
            ) {
                video_path = videoListItem!!.video_url
                videoView!!.setSource(video_path)
                frameLayout1!!.visibility = View.VISIBLE
                /*videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mediaPlayer) {
                        setVideoData(videoListItem);
                        videoView.setVisibility(View.VISIBLE);
                        videoView.start();



                    }
                });*/Global.dismissProgressDialog(this@VideoDetailActivity)
            }
        } else {
            Global.showFailDialog(this@VideoDetailActivity, message)
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


/*
            FileOutputStream f = new FileOutputStream(new File(myDir,
                    videoname1));

            Log.d("VideoName123", "Video Name1:" + videoname + "Video Name 2 : " + videoname1);
            InputStream in = c.getInputStream();
            byte[] buffer = new byte[1024];
            int len1 = 0;
            while ((len1 = in.read(buffer)) > 0) {
                f.write(buffer, 0, len1);
            }
            f.close();

*/


            //SaveImage(savedBmp);
        } catch (e: IOException) {
            Log.d("Error....", e.toString())
            Global.dismissProgressDialog(this@VideoDetailActivity)
        }
    }

    private fun SaveImage(finalBitmap: Bitmap?): String {
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
        Dexter.withActivity(this)
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
}