package com.app.festivalpost.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
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
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.models.VideoListItem
import com.app.festivalpost.utils.Constants
import com.arthenica.mobileffmpeg.Config
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_CANCEL
import com.arthenica.mobileffmpeg.Config.RETURN_CODE_SUCCESS
import com.arthenica.mobileffmpeg.FFmpeg
import com.bumptech.glide.Glide
import com.emegamart.lelys.utils.extensions.get
import com.emegamart.lelys.utils.extensions.getSharedPrefInstance
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
    var frameLayout: FrameLayout? = null
    var frameMain: FrameLayout? = null
    var save = false
    var videoListItem: VideoListItem? = null
    var stop = false
    var width = 0
    var bmp: Bitmap? = null
    var height = 0
    var imageview : AppCompatImageView?=null

    @SuppressLint("HandlerLeak")
    var handler: Handler = object : Handler() {
        @SuppressLint("WrongConstant")
        override fun handleMessage(msg: Message) {
            if (progressBar != null) {
                try {
                    progressBar!!.dismiss()
                    if (msg.what == -1) {
                        //loadJNI!!.fExit(applicationContext)
                    } else if (msg.what == 0 && stop == false) {

                        /*Intent j = new Intent(VideoCreateActivity.this.getApplicationContext(), VideoPreviewActivity.class);
                        Glob.videoname = VideoCreateActivity.this.PassCutVideoFilepath;
                        j.setFlags(67108864);
                        VideoCreateActivity.this.startActivity(j);
                        VideoCreateActivity.this.finish();*/
                    }
                } catch (e: NumberFormatException) {
                    e.printStackTrace()
                } catch (e2: NoSuchMethodError) {
                    e2.printStackTrace()
                } catch (e3: IllegalArgumentException) {
                    e3.printStackTrace()
                } catch (e4: NullPointerException) {
                    e4.printStackTrace()
                } catch (e5: Exception) {
                    e5.printStackTrace()
                }
            }
        }
    }
    var mFilename: String? = null
    var mBuilder: Notification.Builder? = null
    var mNotifyManager: NotificationManager? = null
    var progressBar: ProgressDialog? = null
    //var loadJNI: LoadJNI? = null
    var vkLogPath: String? = null
    var workFolder: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_create)
        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)
        openAddImageDialog()
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
        videoView!!.setSource("/data/data/com.app.festivalpost/files/video_demo.mp4")
        setVideoData(videoListItem)
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        height = displayMetrics.heightPixels
        width = displayMetrics.widthPixels
        if (width>1080)
        {
            val params=frameMain!!.layoutParams
            params.height=1080
            params.width=1080
        }
        else{
            val params=frameMain!!.layoutParams
            params.height=width
            params.width=width
            val params1=frameLayout!!.layoutParams
            params1.height=width
            params1.width=width
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





        Log.d(
            "VideoPAth",
            "" + getSharedPrefInstance().getStringValue("image_name") + " Video_name" + getSharedPrefInstance().getStringValue(
                "video_name"
            ),
        )
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
                AsyncTaskExampleNew().execute()
                save = true
            }
        })
    }

    private fun saveVideoToInternalStorage() {

        videoPath = File(Constant.FOLDER_NAME, videoName).absolutePath
        Log.d("IMAGEPATH2", "/storage/emulated/0$videoPath")
        videoPath = File(Constant.FOLDER_NAME, videoName).absolutePath

        

        val inputCode1: Array<String> = arrayOf(
           /* "ffmpeg",
            "-y",*/
            "-i",
            getSharedPrefInstance().getStringValue("video_name"),
            "-i",
            getSharedPrefInstance().getStringValue("image_name"),
            "-filter_complex",
            "overlay=(W-w):(H-h)",
            "-codec:a",
            "copy",
            "/storage/emulated/0/FestivalPost/$videoName"
        )

        val rc=FFmpeg.execute(inputCode1)

        if (rc == RETURN_CODE_SUCCESS) {
            Log.i(Config.TAG, "Command execution completed successfully.");
        } else if (rc == RETURN_CODE_CANCEL) {
            Log.i(Config.TAG, "Command execution cancelled by user.");
        } else {
            Log.i(Config.TAG, String.format("Command execution failed with rc=%d and the output below.", rc));
            Config.printLastCommandOutput(Log.INFO);
        }

        Log.d(
            "VideoPAth",
            "" + getSharedPrefInstance().getStringValue("image_name") + " Video_name" + getSharedPrefInstance().getStringValue(
                "video_name"
            ),
        )





        val inputCode: Array<String> = arrayOf(
            /*"ffmpeg",
            "-y",*/
            "-i",
            getSharedPrefInstance().getStringValue("video_name"),
            "-i",
            getSharedPrefInstance().getStringValue("image_name"),
            "-filter_complex",
            "overlay=(W-w):(H-h)",
            "-codec:a",
            "copy",
            "/storage/emulated/0/FestivalPost/$videoName"
        )
        /*try {
            addVideoWaterMark(inp*utCode, this@VideoCreateActivity)
        } catch (e: CommandValidationException) {
            e.printStackTrace()
        }*/
        MediaScannerConnection.scanFile(
            applicationContext, arrayOf("/storage/emulated/0$videoPath"), arrayOf("video/mp4")
        ) { path, uri ->
            Log.v(
                "FragCameraScan",
                "file $path was scanned seccessfully: $uri"
            )
        }
    }

    //@Throws(CommandValidationException::class)
    fun addVideoWaterMark(strings: Array<String>?, context: Context) {
        Log.d("WaterMarkHelper", "starting addVideoWaterMark")
        Log.e("addVideoWaterMark", "starting addVideoWaterMark")
        val startTime = System.currentTimeMillis()
        // LoadJNI vk = new LoadJNI();
        //val vk = LoadJNI()
        val workFolder = context.applicationContext.filesDir.toString() + "/"
            //vk.run(strings, workFolder, context.applicationContext)
        Log.e(
            "WaterMarkHelper",
            "finish addVideoWaterMark, took " + (System.currentTimeMillis() - startTime) / 1000 + "seconds."
        )
    }

    var tvaction: TextView? = null
    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24)
        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        tvtitle.text = "Your Video"
        tvaction = toolbar.findViewById<View>(R.id.tvaction) as TextView
        tvaction!!.text = resources.getString(R.string.txt_next)
        tvaction!!.visibility = View.GONE
        tvaction!!.setOnClickListener { }
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
            videoView!!.setSource("/storage/emulated/0$videoPath")

            Toast.makeText(this@VideoCreateActivity, "Video Saved Successfully", Toast.LENGTH_SHORT)
                .show()
            val detailAct = Intent(this@VideoCreateActivity, HomeActivity::class.java)
            detailAct.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(detailAct)
            finish()
            if (save) {
                val sharingIntent = Intent(Intent.ACTION_SEND)
                sharingIntent.type = "video/mp4" //If it is a 3gp video use ("video/3gp")
                val uri = Uri.parse("/storage/emulated/0/$videoPath")
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri)
                startActivity(Intent.createChooser(sharingIntent, "Share Video!"))
            }
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
            android.R.id.home -> onBackPressed()
            else -> {
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun setVideoData(videoData: VideoListItem?) {
        val businessItem = get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS)
        if (businessItem!!.busi_name != "") {
            //Log.d("StringColorCode",""+colorInt);
            tvframename1!!.visibility = View.VISIBLE
            tvframename1!!.text = businessItem.busi_name

        }
        if (businessItem.busi_logo != "") {
            ivlogo1!!.visibility = View.VISIBLE
            //ivlogo1!!.setImageURI(Uri.parse("/storage/emulated/0/Imagename/logo.png"))
            Glide.with(this).load(businessItem.busi_logo).into(ivlogo1!!);
            Log.d("imageName", "" + businessItem.busi_logo)
        }


        //downloadFileImage(businessItem.getBusiLogo());
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

    companion object {
        var NOTIFICATION_ID = 0
    }
}