package com.app.festivalpost.activity

import android.Manifest
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
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
import com.app.festivalpost.globals.Global
import com.app.festivalpost.models.CurrentBusinessItem
import com.app.festivalpost.models.VideoItem
import com.app.festivalpost.models.VideoLanguageItem
import com.app.festivalpost.utils.Constants
import com.app.festivalpost.utils.Constants.KeyIntent.CURRENT_DATE
import com.app.festivalpost.utils.Constants.SharedPref.USER_TOKEN
import com.app.festivalpost.utils.SessionManager
import com.app.festivalpost.utils.extensions.callApi
import com.app.festivalpost.utils.extensions.getRestApis
import com.emegamart.lelys.utils.extensions.*
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
import java.text.ParseException
import java.text.SimpleDateFormat
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
    var frameMain: FrameLayout? = null
    var videoid: String? = null
    var videoTitle: VideoItem? = null
    var width = 0
    var height = 0
    private var day = 0
    var sessionManager: SessionManager? = null
    var token: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_detail)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        openAddImageDialog()
        sessionManager = SessionManager(this)
        token = sessionManager!!.getValueString(USER_TOKEN)
        setActionbar()



        rvdata = findViewById<View>(R.id.rvdata) as RecyclerView
        frameLayout1 = findViewById<View>(R.id.frame) as FrameLayout
        frameMain = findViewById<View>(R.id.frameMain) as FrameLayout
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

        val bundle = intent.extras
        if (bundle!!.containsKey("object")) {
            videoid = bundle.getString("video_id")
            videoTitle = bundle.getSerializable("object") as VideoItem?
        } else if (bundle!!.containsKey("video_id")) {
            videoid = bundle.getString("video_id")
        }
        if (bundle!!.containsKey("video_date")) {
            day = getCountOfDays(
                sessionManager!!.getValueString(CURRENT_DATE),
                bundle.getString("video_date")
            )


        }
        sessionManager!!.getValueString(CURRENT_DATE)
        Log.d("CurrentDate",""+sessionManager!!.getValueString(CURRENT_DATE))



        loadAccoutData()
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
        }

        videoView!!.setShowController(true)


    }


    var tvaction: TextView? = null
    override fun onResume() {
        videoView!!.stopPlayer()
        super.onResume()
    }

    fun setActionbar() {
        val toolbar = findViewById<View>(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        val tvtitle = toolbar.findViewById<View>(R.id.tvtitle) as TextView
        val ivBack = toolbar.findViewById<View>(R.id.ivBack) as AppCompatImageView
        val tvaction = toolbar.findViewById<View>(R.id.btn_next) as TextView

        tvtitle.text = resources.getString(R.string.txt_choose_video_post)

        ivBack.onClick {
            onBackPressed()
            videoView!!.stopPlayer()
        }

        tvaction.setOnClickListener {
            videoView!!.stopPlayer()
            val currentBusinessItem =
                get<CurrentBusinessItem>(Constants.SharedPref.KEY_CURRENT_BUSINESS, this)
            if (currentBusinessItem == null) {
                val materialAlertDialogBuilder = AlertDialog.Builder(this)
                val inflater =
                    this.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
                val view = inflater.inflate(R.layout.custom_add_busines_dialog, null)
                val tvTitle: TextView
                val tvMessage: TextView
                val btnOk: Button
                val btnCancel: Button
                tvTitle = view.findViewById(R.id.tvTitle)
                tvMessage = view.findViewById(R.id.tvMessage)
                btnOk = view.findViewById(R.id.btnOk)
                btnCancel = view.findViewById(R.id.btnCancel)
                tvTitle.text = "Sorry!!"
                tvMessage.text = "For making post please add your business details first."
                materialAlertDialogBuilder.setView(view).setCancelable(true)
                val b = materialAlertDialogBuilder.create()
                btnCancel.setOnClickListener { b.dismiss() }
                btnOk.setOnClickListener { launchActivity<AddBusinessActivity> { finish() } }
                b.show()
            } else {
                if (day in 0..1) {
                    launchActivity<ChooseVideoFrameActivity> {
                        putExtra("video_path", video_path)
                        putExtra("video_type", video_type)
                    }
                } else {
                    Global.getAlertDialog(
                        this,
                        "Sorry!!",
                        "This Festival is locked today.This festival photos will open before 24 hours of festival."
                    )
                }
            }
        }
    }

    var video_path = ""
    var video_type = ""
    override fun onItemClicked(`object`: Any?, index: Int) {
        videoListItem = `object` as VideoLanguageItem
        //setVideoData(videoListItem)
        if (videoListItem!!.image != null && !videoListItem!!.image.equals(
                "",
                ignoreCase = true
            )
        ) {
            video_type = videoListItem!!.type!!
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
        videoView!!.stopPlayer()
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
            getRestApis().getVideoLanguageData(videoid!!, "0", token!!), onApiSuccess = {
                showProgress(false)
                Log.d("videoItemList", "" + it.data.size)
                val adapter = ChooseVideoAdapter(this@VideoDetailActivity, it.data)
                rvdata!!.adapter = adapter
                videoListItemArrayList = it.data

                videoListItem = videoListItemArrayList[0]
                videoListItem!!.isIs_selected = true

                if (videoListItem!!.image != null && !videoListItem!!.image.equals(
                        "",
                        ignoreCase = true
                    )
                ) {
                    video_path = videoListItem!!.video!!
                    video_type = videoListItem!!.type!!
                    videoView!!.setSource(video_path)
                    frameLayout1!!.visibility = View.VISIBLE
                }

            }, onApiError = {
                showProgress(false)


            }, onNetworkError = {
                showProgress(false)
            })
    }

    private fun getCountOfDays(createdDateString: String?, expireDateString: String?): Int {
        val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
        var createdConvertedDate: Date? = null
        var expireCovertedDate: Date? = null
        var todayWithZeroTime: Date? = null
        try {
            createdConvertedDate = dateFormat.parse(createdDateString)
            expireCovertedDate = dateFormat.parse(expireDateString)
            val today = Date()
            todayWithZeroTime = dateFormat.parse(dateFormat.format(today))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        var cYear = 0
        var cMonth = 0
        var cDay = 0
        if (createdConvertedDate!!.after(todayWithZeroTime)) {
            val cCal = Calendar.getInstance()
            cCal.time = createdConvertedDate
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        } else {
            val cCal = Calendar.getInstance()
            cCal.time = todayWithZeroTime
            cYear = cCal[Calendar.YEAR]
            cMonth = cCal[Calendar.MONTH]
            cDay = cCal[Calendar.DAY_OF_MONTH]
        }
        val eCal = Calendar.getInstance()
        eCal.time = expireCovertedDate
        val eYear = eCal[Calendar.YEAR]
        val eMonth = eCal[Calendar.MONTH]
        val eDay = eCal[Calendar.DAY_OF_MONTH]
        val date1 = Calendar.getInstance()
        val date2 = Calendar.getInstance()
        date1.clear()
        date1[cYear, cMonth] = cDay
        date2.clear()
        date2[eYear, eMonth] = eDay
        val diff = date2.timeInMillis - date1.timeInMillis
        val dayCount = diff.toFloat() / (24 * 60 * 60 * 1000)
        return dayCount.toInt()
    }

    fun saveVideo()
    {
        val values = ContentValues()
//        values.put(MediaStore.MediaColumns.DISPLAY_NAME, "$name.jpg")
        //va/lues.put(MediaStore.MediaColumns.MIME_TYPE, "video/.mp4")
        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        //val uri: Uri = cr.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)
        //getDataColumn(context, uri, null, null);
    }

    fun getDataColumn(
        context: Context, uri: Uri?, selection: String?,
        selectionArgs: Array<String?>?
    ): String? {
        var cursor: Cursor? = null
        val column = MediaStore.MediaColumns.DATA
        val projection = arrayOf(
            column
        )
        try {
            cursor = context.contentResolver.query(
                uri!!, projection, selection, selectionArgs,
                null
            )
            if (cursor != null && cursor.moveToFirst()) {
                val column_index: Int = cursor.getColumnIndexOrThrow(column)
                return cursor.getString(column_index)
            }
        } finally {
            if (cursor != null) cursor.close()
        }
        return null
    }


}