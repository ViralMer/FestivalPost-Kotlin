package com.app.festivalpost.activity

/*import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg*/

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.os.Environment.DIRECTORY_PICTURES
import android.util.DisplayMetrics
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.widget.AppCompatImageView
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.AppBaseActivity
import com.app.festivalpost.R
import com.app.festivalpost.activity.HomeActivity
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.models.VideoListItem
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.SessionManager
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.get
import com.facebook.ads.AdSize
import com.facebook.ads.AdView
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.potyvideo.library.AndExoPlayerView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class VideoCreateActivity : AppBaseActivity() {
    var videoPath: String? = null
    var videoView: AndExoPlayerView? = null
    var btnSave: AppCompatImageView? = null
    var btnShare: AppCompatImageView? = null
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
    var textEmail1: TextView? = null
    var textWebsite1: TextView? = null
    var ivlogo: ImageView? = null
    var ivlogo1: ImageView? = null
    var banner_container: LinearLayout? = null
    var frameLayout: FrameLayout? = null
    var frameMain: FrameLayout? = null
    var save = false
    var videoListItem: VideoListItem? = null
    var stop = false
    var width = 0
    var bmp: Bitmap? = null
    var height = 0

    var imageview: AppCompatImageView? = null
    var sessionManager: SessionManager? = null
    private var adView: AdView? = null

    var mFilename: String? = null
    var mVideoName: String? = null
    var mBuilder: Notification.Builder? = null
    var mNotifyManager: NotificationManager? = null
    var progressBar: ProgressDialog? = null

    //var loadJNI: LoadJNI? = null
    var vkLogPath: String? = null
    var workFolder: String? = null

    @SuppressLint("SdCardPath")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_create)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        openAddImageDialog()
        sessionManager = SessionManager(this)
        videoView = findViewById(R.id.ivvideo)
        setActionbar()
        val bundle = intent.extras
        if (bundle != null) {
            if (bundle.containsKey("video_item")) {
                videoListItem = bundle["video_item"] as VideoListItem?
                Log.d("VideoItem", "" + videoListItem.toString())
            }
        }
        frameLayout = findViewById(R.id.frame)
        frameMain = findViewById<View>(R.id.frameMain) as FrameLayout
        tvframephone1 = findViewById(R.id.tvframephone)
        tvframeemail1 = findViewById(R.id.tvframeemail)
        tvframeweb1 = findViewById(R.id.tvframeweb)
        tvframelocation1 = findViewById(R.id.tvframelocation)
        tvframename1 = findViewById(R.id.tvframename)
        ivlogo1 = findViewById(R.id.ivframelogo1)
        textEmail1 = findViewById(R.id.viewPhone)
        textWebsite1 = findViewById(R.id.viewwebsite)
        imageview = findViewById(R.id.imageview)
        banner_container = findViewById(R.id.banner_container)
        setVideoData(videoListItem)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        if (width > 1080) {
            val params = frameMain!!.layoutParams
            params.height = 1080
            params.width = 1080
        } else {
            val params = frameMain!!.layoutParams
            params.height = width
            params.width = width
            val params1 = frameLayout!!.layoutParams
            params1.height = width
            params1.width = width
        }
        val filename = "video_bitmap.png"
        try {
            val `is` = openFileInput(filename)
            bmp = BitmapFactory.decodeStream(`is`)
            `is`.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        imageview!!.setImageBitmap(bmp)
        banner_container = findViewById(R.id.banner_container)
        adView = AdView(
            this,
            "637278886950456_679310799413931",
            AdSize.BANNER_HEIGHT_50
        )
        videoView!!.setSource(filesDir.absolutePath + "/video_demo.mp4")
        videoView!!.setPlayWhenReady(true)

        if (!sessionManager!!.getBooleanValue(Constants.KeyIntent.IS_PREMIUM)!!) {
            banner_container!!.addView(adView)
            adView!!.loadAd()
        }


        btnSave = findViewById(R.id.btnsubmit)
        btnShare = findViewById(R.id.btnshare)


//        int colorInt=Integer.parseInt(color);
        btnSave!!.setOnClickListener(View.OnClickListener {
            videoView!!.stopPlayer()
            openAddImageDialog()
            AsyncTaskExampleNew().execute()
        })
        btnShare!!.setOnClickListener(View.OnClickListener {
            openAddImageDialog()
            videoView!!.stopPlayer()
            if (save) {
            } else {
                save = true
                AsyncTaskExampleNew().execute()

            }
        })
    }

    private fun saveVideoToInternalStorage() {
        val musicDirectory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/FestivalPost")
        musicDirectory.mkdirs()
        musicDirectory.mkdir()
        mVideoName=videoName


        val inputCode1: Array<String> = arrayOf(
            "-i",
            sessionManager!!.getValueString("video_name")!!,
            "-i",
            sessionManager!!.getValueString("image_name")!!,
            "-filter_complex",
            "overlay=(W-w):(H-h)",
            "-codec:a",
            "copy",
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/FestivalPost/$mVideoName"
        )

        val rc = FFmpeg.execute(inputCode1)

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
        } else {
            Log.i(
                Config.TAG, String.format(
                    "Command execution failed with rc=%d and the output below.",
                    rc
                )
            );
            Config.printLastCommandOutput(Log.INFO);
        }



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                    Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/FestivalPost/$mVideoName")
                )
            )
        } else {
            sendBroadcast(
                Intent(
                    Intent.ACTION_MEDIA_MOUNTED,
                    Uri.parse
                        (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/FestivalPost/$mVideoName")

                )
            )
        }

        MediaScannerConnection.scanFile(
            applicationContext,
            arrayOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/FestivalPost/$mVideoName"),
            arrayOf("video/mp4")
        ) { path, uri ->

        }
    }


    var tvaction: TextView? = null
    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = "Save & Share"
        tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvaction!!.text = resources.getString(R.string.txt_next)
        tvaction!!.visibility = View.GONE

    }

    fun openAddImageDialog() {
        Dexter.withActivity(this)
            .withPermissions(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_MEDIA_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                    if (report.areAllPermissionsGranted()) {
                        /*val root = Environment.getExternalStorageDirectory().absolutePath
                        val myDir = File(root + "/" + Constant.FOLDER_NAME)
                        myDir.mkdirs()
                        myDir.mkdir()*/
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

    private val videoName: String
        private get() = "video_" + SimpleDateFormat("yyyy_MMM_dd_HH_mm_ss", Locale.ENGLISH).format(
            Date()
        ) + ".mp4"

    inner class AsyncTaskExampleNew : AsyncTask<Void?, Void?, Void?>() {
        override fun onPreExecute() {
            showProgress(true)
            super.onPreExecute()
        }

        override fun doInBackground(vararg p0: Void?): Void? {
            try {
                saveVideoToInternalStorage()
            } catch (e: Exception) {
                //p.dismiss();
            }
            return null
        }

        override fun onPostExecute(aVoid: Void?) {
            showProgress(false)
            frameLayout!!.visibility = View.GONE
            videoView!!.setSource(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/FestivalPost/$mVideoName")
            scanner()
            Toast.makeText(this@VideoCreateActivity, "Video Saved Successfully", Toast.LENGTH_SHORT)
                .show()
            val detailAct = Intent(this@VideoCreateActivity, HomeActivity::class.java)
            detailAct.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(detailAct)

            if (save) {

                    val uri = Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath+"/FestivalPost/$mVideoName")
                    val sharingIntent = Intent(Intent.ACTION_SEND)
                    sharingIntent.type = "video/mp4" //If it is a 3gp video use ("video/3gp")
                    sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
                    sharingIntent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    sharingIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    Log.d("uriPArse", "" + uri)
                    startActivity(Intent.createChooser(sharingIntent, "Share Video!"))
            }
            finish()


            super.onPostExecute(aVoid)
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
            android.R.id.home -> {
                onBackPressed()
                videoView!!.stopPlayer()
            }
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setVideoData(videoData: VideoListItem?) {
        val businessItem = get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS, this)
        if (businessItem!!.busi_name != "") {
            tvframename1!!.visibility = View.VISIBLE
            tvframename1!!.text = businessItem.busi_name

        }
        if (businessItem.busi_logo != "") {
            ivlogo1!!.visibility = View.VISIBLE
            Glide.with(this).load(businessItem.busi_logo).into(ivlogo1!!);
            Log.d("imageName", "" + businessItem.busi_logo)
        }


        if (businessItem.busi_mobile != "") {
            tvframephone1!!.visibility = View.VISIBLE
            tvframephone1!!.text = businessItem.busi_mobile
        }
        if (businessItem.busi_address != "") {
            tvframelocation1!!.visibility = View.VISIBLE
            tvframelocation1!!.text = businessItem.busi_address
        }
        if (businessItem.busi_email != "") {
            tvframeemail1!!.visibility = View.VISIBLE
            tvframeemail1!!.text = businessItem.busi_email
            textEmail1!!.visibility = View.VISIBLE
        }
        if (businessItem.busi_website != "") {
            tvframeweb1!!.visibility = View.VISIBLE
            tvframeweb1!!.text = businessItem.busi_website
            textWebsite1!!.visibility = View.VISIBLE
        }
    }

    private fun scanner() {
        sendBroadcast(
            Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
                Uri.parse(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/FestivalPost/$mVideoName")
            )
        )
        MediaScannerConnection.scanFile(
            this,
            arrayOf(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).absolutePath + "/FestivalPost/$mVideoName"),
            arrayOf(
                "video/mp4"
            )
        ) { path, uri -> Log.i("TAG", "Finished scanning $path") }
    }

    override fun onDestroy() {
        super.onDestroy()
        scanner()
        Log.d("onDestroy", "OnDestroy");
    }

    companion object {
        var NOTIFICATION_ID = 0
    }

    fun shareVideo(path: String) {
        MediaScannerConnection.scanFile(
            this, arrayOf(path),
            null
        ) { path, uri ->
            val shareIntent = Intent(
                Intent.ACTION_SEND
            )
            shareIntent.type = "video/mp4"

            shareIntent.putExtra(Intent.EXTRA_STREAM, uri)
            shareIntent
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET)
            startActivity(
                Intent.createChooser(
                    shareIntent,
                    "Share Video"
                )
            )
        }
    }
}