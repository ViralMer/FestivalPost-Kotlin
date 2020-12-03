package com.app.festivalpost.globals

import android.annotation.SuppressLint
import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.*
import java.util.*

class FileUtils {
    companion object {
        private val mSdCard = File(Environment.getExternalStorageDirectory().absolutePath)
        var APP_DIRECTORY = File(mSdCard, "ABCD")
        val TEMP_DIRECTORY = File(APP_DIRECTORY, ".temp")
        val TEMP_DIRECTORY_AUDIO = File(APP_DIRECTORY, ".temp_audio")
        private val TEMP_VID_DIRECTORY = File(TEMP_DIRECTORY, ".temp_vid")
        val frameFile = File(APP_DIRECTORY, ".frame.png")
        private var mDeleteFileCount: Long = 0
        private const val FFMPEG_FILE_NAME = "ffmpeg"
        fun getFFmpeg(context: Context): File {
            val folder = context.filesDir
            return File(folder, FFMPEG_FILE_NAME)
        }

        fun inputStreamToFile(stream: InputStream?, file: File?): Boolean {
            try {
                val input: InputStream = BufferedInputStream(stream)
                val output: OutputStream = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var bytesRead: Int
                while (input.read(buffer, 0, buffer.size).also { bytesRead = it } >= 0) {
                    output.write(buffer, 0, bytesRead)
                }
                output.flush()
                output.close()
                input.close()
                return true
            } catch (e: IOException) {
                Log.e("error while writing", e.toString())
            }
            return false
        }

        private fun getImageDirectory(theme: String): File {
            val imageDir = File(TEMP_DIRECTORY, theme)
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            return imageDir
        }

        fun getImageDirectory(theme: String, iNo: Int): File {
            val imageDir =
                File(getImageDirectory(theme), String.format(Locale.getDefault(), "IMG_%03d", iNo))
            if (!imageDir.exists()) {
                imageDir.mkdirs()
            }
            return imageDir
        }

        fun deleteThemeDir(theme: String): Boolean {
            return deleteFile(getImageDirectory(theme))
        }

        fun deleteTempDir() {
            for (child in TEMP_DIRECTORY.listFiles()) {
                Thread { FileUtils.deleteFile(child) }.start()
            }
        }

        fun deleteFile(mFile: File?): Boolean {
            var idDelete = false
            if (mFile == null) {
                return true
            }
            if (mFile.exists()) {
                if (mFile.isDirectory) {
                    val children = mFile.listFiles()
                    if (children != null && children.size > 0) {
                        for (child in children) {
                            mDeleteFileCount += child.length()
                            idDelete = deleteFile(child)
                        }
                    }
                    mDeleteFileCount += mFile.length()
                    idDelete = mFile.delete()
                } else {
                    mDeleteFileCount += mFile.length()
                    idDelete = mFile.delete()
                }
            }
            return idDelete
        }

        @SuppressLint("DefaultLocale")
        fun getDuration(duration: Long): String {
            if (duration < 1000) {
                return String.format("%02d:%02d", 0, 0)
            }
            val hours = (duration / (1000 * 60 * 60)).toInt()
            val minutes = (duration % (1000 * 60 * 60)).toInt() / (1000 * 60)
            val seconds = (duration % (1000 * 60 * 60) % (1000 * 60) / 1000).toInt()
            return if (hours == 0) {
                String.format("%02d:%02d", minutes, seconds)
            } else String.format("%02d:%02d:%02d", hours, minutes, seconds)
        }

        init {
            if (!TEMP_DIRECTORY.exists()) {
                TEMP_DIRECTORY.mkdirs()
            }
            if (!TEMP_VID_DIRECTORY.exists()) {
                TEMP_VID_DIRECTORY.mkdirs()
            }
        }
    }

    init {
        mDeleteFileCount = 0
    }
}