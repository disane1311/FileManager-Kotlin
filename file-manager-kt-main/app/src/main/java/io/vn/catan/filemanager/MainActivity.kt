package io.vn.catan.filemanager

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.button.MaterialButton

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val storageBtn = findViewById<MaterialButton>(R.id.storage_btn)
        storageBtn.setOnClickListener {
            if (checkPermission()) {
                // permission allowed
                val intent = Intent(this, ListFilesActivity::class.java)
                val path = Environment.getExternalStorageDirectory().path
                intent.putExtra("path", path)
                startActivity(intent)
            } else {
                // permission not allowed
                requestPermission()
            }
        }
    }

    private fun checkPermission(): Boolean {
//        val result = ContextCompat.checkSelfPermission(
//            this,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE
//        )
//        return result == PackageManager.PERMISSION_GRANTED
        return Environment.isExternalStorageManager()
    }

    private fun requestPermission() {
//        if (ActivityCompat.shouldShowRequestPermissionRationale(
//                this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE
//            )
//        ) {
//            Toast.makeText(
//                this,
//                "Storage permission is requires, please allow from settings",
//                Toast.LENGTH_SHORT
//            ).show()
//        } else {
//            Log.d("debugg", "requesting")
//            ActivityCompat.requestPermissions(
//                this,
//                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                110
//            )
//        }
        val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
        startActivity(intent)
    }
}