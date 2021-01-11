package com.app.festivalpost

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatEditText
import com.app.festivalpost.FestivalPost.Companion.noInternetDialog
import com.emegamart.lelys.utils.extensions.color
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump
import java.util.*


open class AppBaseActivity : AppCompatActivity(),View.OnFocusChangeListener {
    private var progressDialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

         ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath(resources!!.getString(R.string.font_regular))
                            .setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                )
                .build()
        )
        noInternetDialog = null
        if (progressDialog == null) {
            progressDialog = Dialog(this)
            progressDialog!!.window?.setBackgroundDrawable(ColorDrawable(0))
            progressDialog!!.setContentView(R.layout.custom_dialog)

        }

    }




    fun showProgress(show: Boolean) {
        when {
            show -> {
                if (!isFinishing && !progressDialog!!.isShowing) {
                    progressDialog?.setCanceledOnTouchOutside(false)
                    progressDialog?.setCancelable(false)
                    try {

                        progressDialog?.show()
                    }
                    catch (e:WindowManager.InvalidDisplayException) {
                    }
                    catch (e:WindowManager.BadTokenException) {
                    }


                }
            }
            else -> try {
                if (progressDialog?.isShowing!! && !isFinishing) {
                    progressDialog?.dismiss()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun changeLanguage(context: Context, locale: Locale): Context {
        Locale.setDefault(locale)
        val config = Configuration()
        config.setLocale(locale)
        config.setLayoutDirection(locale)
        return context.createConfigurationContext(config)
    }

    override fun onStart() {
        Log.d("onStart", "called")
        super.onStart()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onFocusChange(v: View?, hasFocus: Boolean) {

        if (hasFocus) {
            (v as AppCompatEditText).setTextColor(this.color(R.color.colorPrimary))
            v.background = resources.getDrawable(R.drawable.edit_text_border_selected)
        } else {
            (v as AppCompatEditText).setTextColor(this.color(R.color.black))
            v.background = resources.getDrawable(R.drawable.edit_text_border)
        }
    }

    open fun onActivityResult(requestCode: Int?, resultCode: Int?, data: Intent?) {}
}
