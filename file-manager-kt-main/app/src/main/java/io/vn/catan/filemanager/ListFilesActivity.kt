package io.vn.catan.filemanager

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class ListFilesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_files)
        reload()
    }

    private fun reload() {
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        val noFilesText = findViewById<TextView>(R.id.nofiles_textview)
        val path = intent.getStringExtra("path")
        val root = File(path)
        val filesAndFolders = root.listFiles()

        if (filesAndFolders == null || filesAndFolders.isEmpty()) {
            noFilesText.visibility = View.VISIBLE
            return
        }

        filesAndFolders.sortWith(compareBy({ !it.isDirectory }, { it.extension }, { it.name }))
        noFilesText.visibility = View.INVISIBLE
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = MyAdapter(this, filesAndFolders, ::reload)
    }
}