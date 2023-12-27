package io.vn.catan.filemanager

import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.io.File

class ViewImageActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_image)

        val src = intent.getStringExtra("image")

        if (src == null) {
            val noImage = findViewById<TextView>(R.id.no_image)
            noImage.visibility = View.VISIBLE
            return
        }

        val bitmap = BitmapFactory.decodeFile(src)
        val image = findViewById<ImageView>(R.id.image_view)
        image.setImageBitmap(bitmap)
    }
}