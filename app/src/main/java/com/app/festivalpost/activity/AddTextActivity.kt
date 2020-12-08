package com.app.festivalpost.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import com.app.festivalpost.R
import top.defaults.colorpicker.ColorPickerPopup
import top.defaults.colorpicker.ColorPickerPopup.ColorPickerObserver

class AddTextActivity : AppCompatActivity() {
    var selected_color = Color.BLACK
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_text)
        val edtext = findViewById<EditText>(R.id.edtext)
        val btndone = findViewById<Button>(R.id.btndone)
        val btncolor = findViewById<Button>(R.id.btncolor)
        edtext.setTextColor(selected_color)
        btndone.setOnClickListener {
            val message = edtext.text.toString()
            val intent = Intent()
            intent.putExtra("text", message)
            intent.putExtra("color", selected_color)
            setResult(1000, intent)
            finish() //finishing activity
        }
        btncolor.setOnClickListener { v ->
            ColorPickerPopup.Builder(this@AddTextActivity)
                .initialColor(Color.RED) // Set initial color
                .enableBrightness(true) // Enable brightness slider or not
                .enableAlpha(true) // Enable alpha slider or not
                .okTitle("Choose")
                .cancelTitle("Cancel")
                .showIndicator(true)
                .build()
                .show(v, object : ColorPickerObserver() {
                    override fun onColorPicked(color: Int) {
                        selected_color = color
                        edtext.setTextColor(color)
                    }
                })
        }
    }
}