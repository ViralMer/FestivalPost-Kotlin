package com.app.festivalpost.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Notification
import android.app.NotificationManager
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.media.MediaPlayer
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.*
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.app.festivalpost.R
import com.app.festivalpost.activity.HomeActivity
import com.app.festivalpost.globals.Constant
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.VideoListItem
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.netcompss.ffmpeg4android.CommandValidationException
import com.netcompss.loader.LoadJNI
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

/*import com.arthenica.mobileffmpeg.Config;
import com.arthenica.mobileffmpeg.FFmpeg;*/   class VideoCreateActivity : AppCompatActivity() {
    var videoPath: String? = null
    var selectedVideoPath: String? = null
    var PassCutVideoFilepath: String? = null
    var p: ProgressDialog? = null
    var videoView: VideoView? = null
    var btnSave: Button? = null
    var btnShare: Button? = null
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
    var save = false
    var videoListItem: VideoListItem? = null
    private val commandValidationFailedFlag = false
    var demoVideoFolder: String? = null
    var demoVideoPath: String? = null
    var filenameextesio: String? = null
    var stop = false

    @SuppressLint("HandlerLeak")
    var handler: Handler = object : Handler() {
        @SuppressLint("WrongConstant")
        override fun handleMessage(msg: Message) {
            if (progressBar != null) {
                try {
                    progressBar!!.dismiss()
                    if (msg.what == -1) {
                        loadJNI!!.fExit(applicationContext)
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
    var loadJNI: LoadJNI? = null
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
        tvframephone1 = findViewById(R.id.tvframephone)
        tvframeemail1 = findViewById(R.id.tvframeemail)
        tvframeweb1 = findViewById(R.id.tvframeweb)
        tvframelocation1 = findViewById(R.id.tvframelocation)
        tvframename1 = findViewById(R.id.tvframename)
        ivlogo1 = findViewById(R.id.ivframelogo1)
        textEmail1 = findViewById(R.id.viewPhone)
        textWebsite1 = findViewById(R.id.viewwebsite)
        videoView!!.setVideoPath("/data/data/com.app.festivalpost/files/video_demo.mp4")
        videoView!!.setOnPreparedListener(MediaPlayer.OnPreparedListener {
            setVideoData(videoListItem)
            Log.d("videoPAth", "" + Global.getPreference("video_name", ""))
            videoView!!.start()
        })
        btnSave = findViewById(R.id.btnsubmit)
        btnShare = findViewById(R.id.btnshare)
        p = ProgressDialog(this@VideoCreateActivity)


//        int colorInt=Integer.parseInt(color);
        btnSave!!.setOnClickListener(View.OnClickListener {
            videoView!!.stopPlayback()
            openAddImageDialog()
            AsyncTaskExampleNew().execute()
        })
        btnShare!!.setOnClickListener(View.OnClickListener {
            videoView!!.stopPlayback()
            openAddImageDialog()
            if (save) {
            } else {
                AsyncTaskExampleNew().execute()
                save = true
            }
        })
    }

    private fun saveVideoToInternalStorage() {
        val inputCode: Array<String>
        Log.d("VideoNameNew", "" + Global.getPreference("video_name", "") + videoPath)
        videoPath = File(Constant.FOLDER_NAME, videoName).absolutePath
        Log.d("IMAGEPATH", "" + Global.getPreference("video_name", ""))
        Log.d("IMAGEPATH1", "" + Global.getPreference("image_name", ""))
        Log.d("IMAGEPATH2", "/storage/emulated/0$videoPath")
        videoPath = File(Constant.FOLDER_NAME, videoName).absolutePath
        Log.d("VideoPAth", "" + videoPath)
        inputCode = arrayOf(
            "ffmpeg",
            "-y",
            "-i",
            Global.getPreference("video_name", ""),
            "-i",
            Global.getPreference("image_name", ""),
            "-filter_complex",
            "overlay=(W-w):(H-h)",
            "-codec:a",
            "copy",
            "/storage/emulated/0/FestivalPost/$videoName"
        )
        try {
            addVideoWaterMark(inputCode, this@VideoCreateActivity)
        } catch (e: CommandValidationException) {
            e.printStackTrace()
        }
        MediaScannerConnection.scanFile(
            applicationContext, arrayOf("/storage/emulated/0$videoPath"), arrayOf("video/mp4")
        ) { path, uri ->
            Log.v(
                "FragCameraScan",
                "file $path was scanned seccessfully: $uri"
            )
        }
    }

    @Throws(CommandValidationException::class)
    fun addVideoWaterMark(strings: Array<String>?, context: Context) {
        Log.d("WaterMarkHelper", "starting addVideoWaterMark")
        Log.e("addVideoWaterMark", "starting addVideoWaterMark")
        val startTime = System.currentTimeMillis()
        // LoadJNI vk = new LoadJNI();
        val vk = LoadJNI()
        val workFolder = context.applicationContext.filesDir.toString() + "/"
        vk.run(strings, workFolder, context.applicationContext)
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
            p!!.setMessage("Please wait...Video is Creating")
            p!!.isIndeterminate = false
            p!!.setCancelable(false)
            p!!.show()
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
            p!!.dismiss()
            frameLayout!!.visibility = View.GONE
            videoView!!.setVideoPath("/storage/emulated/0$videoPath")
            videoView!!.setOnPreparedListener {
                Global.dismissProgressDialog(this@VideoCreateActivity)
                videoView!!.start()
            }
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
        val businessItem = Global.currentBusinessNEW
        if (businessItem.busiName != "" && businessItem.busiName != null) {
            //Log.d("StringColorCode",""+colorInt);
            tvframename1!!.visibility = View.VISIBLE
            tvframename1!!.text = businessItem.busiName
            tvframename1!!.setTextColor(Color.parseColor(videoData!!.color))
        }
        if (businessItem.busiLogo != "" && businessItem.busiLogo != null) {
            ivlogo1!!.visibility = View.VISIBLE
            ivlogo1!!.setImageURI(Uri.parse("/storage/emulated/0/Imagename/logo.png"))
            //Glide.with(VideoDetailActivity.this).load(businessItem.getBusiLogo()).into(ivlogo);
            Log.d("imageName", "" + businessItem.busiLogo)
        }


        //downloadFileImage(businessItem.getBusiLogo());
        if (businessItem.busiMobile != null && businessItem.busiMobile != "") {
            tvframephone1!!.visibility = View.VISIBLE
            tvframephone1!!.text = businessItem.busiMobile
            tvframephone1!!.setTextColor(Color.parseColor(videoData!!.color))
        }
        if (businessItem.busiAddress != null && businessItem.busiAddress != "") {
            tvframelocation1!!.visibility = View.VISIBLE
            tvframelocation1!!.text = businessItem.busiAddress
            tvframelocation1!!.setTextColor(Color.parseColor(videoData!!.color))
        }
        if (businessItem.busiEmail != null && businessItem.busiEmail != "") {
            tvframeemail1!!.visibility = View.VISIBLE
            tvframeemail1!!.text = businessItem.busiEmail
            tvframeemail1!!.setTextColor(Color.parseColor(videoData!!.color))
            textEmail1!!.visibility = View.VISIBLE
            textEmail1!!.setTextColor(Color.parseColor(videoData.color))
        }
        if (businessItem.busiWebsite != null && businessItem.busiWebsite != "") {
            tvframeweb1!!.visibility = View.VISIBLE
            tvframeweb1!!.text = businessItem.busiWebsite
            tvframeweb1!!.setTextColor(Color.parseColor(videoData!!.color))
            textWebsite1!!.visibility = View.VISIBLE
            textWebsite1!!.setTextColor(Color.parseColor(videoData.color))
        }
    }

    companion object {
        var NOTIFICATION_ID = 0
    }
}