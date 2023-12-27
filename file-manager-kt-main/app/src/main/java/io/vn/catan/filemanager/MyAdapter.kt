package io.vn.catan.filemanager

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.StandardCopyOption

class MyAdapter(
    private var context: Context,
    private var filesAndFolders: Array<File>,
    private var reload: (() -> Unit)
) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.recycler_item, parent, false)
        return ViewHolder(view)
    }

    private fun renameFileWithoutPath(filePath: String, newFileName: String): Boolean {
        val path = FileSystems.getDefault().getPath(filePath)
        val parentDir = path.parent
        val newPath = if (parentDir != null) {
            parentDir.resolve(newFileName)
        } else {
            FileSystems.getDefault().getPath(newFileName)
        }

        try {
            Files.move(path, newPath, StandardCopyOption.REPLACE_EXISTING)
        } catch (e: Exception) {
            return false
        }
        return true
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val file = filesAndFolders[position]

        holder.textView.text = file.name
        holder.imageView.setImageResource(
            if (file.isDirectory) R.drawable.ic_baseline_folder_24
            else {
                when (file.extension) {
                    "png", "jpg", "jpeg", "gif" -> R.drawable.baseline_image_24
                    else -> R.drawable.ic_baseline_insert_drive_file_24
                }
            }
        )
        holder.itemView.setOnClickListener {
            if (file.isDirectory) {
                val intent = Intent(context, ListFilesActivity::class.java)
                intent.putExtra("path", file.absolutePath)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                context.startActivity(intent)
            } else {
                // TODO: open the file
                try {
                    when (file.extension) {
                        "png", "jpg", "jpeg", "gif" -> {
                            val intent = Intent(context, ViewImageActivity::class.java)
                            intent.putExtra("image", file.absolutePath)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        }
                        else -> {
                            val intent = Intent(context, ViewTextActivity::class.java)
                            intent.putExtra("text", file.absolutePath)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(
                        context.applicationContext,
                        "Cannot open this file",
                        Toast.LENGTH_SHORT
                    )
                }
            }
        }
        holder.itemView.setOnLongClickListener { v ->
            val popupMenu = PopupMenu(context, v)
            popupMenu.menu.add("DELETE")
            popupMenu.menu.add("MOVE")
            popupMenu.menu.add("RENAME")
            popupMenu.setOnMenuItemClickListener { item ->
                if (item.title == "DELETE") {
                    AlertDialog.Builder(context)
                        .setTitle("Delete ${if (file.isDirectory) "folder" else "file"}")
                        .setMessage("Are you sure to delete this?")
                        .setPositiveButton("OK") { _, _ ->
                            val deleted = file.delete()
                            if (deleted) {
                                Toast.makeText(
                                    context.applicationContext,
                                    "DELETED",
                                    Toast.LENGTH_SHORT
                                ).show()
                                v.visibility = View.GONE
                            }
                            reload()
                        }
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show()

                }
                if (item.title == "MOVE") {
                    Toast.makeText(context.applicationContext, "MOVED", Toast.LENGTH_SHORT)
                        .show()
                }
                if (item.title == "RENAME") {
                    val view =
                        LayoutInflater.from(context).inflate(R.layout.rename_dialog, null, false)
                    val input = view.findViewById<EditText>(R.id.rename_input)
                    input.setText(file.nameWithoutExtension)

                    AlertDialog.Builder(context)
                        .setTitle("Rename ${if (file.isDirectory) "folder" else "file"}")
                        .setView(view)
                        .setPositiveButton("OK") { _, _ ->
                            val target = if (file.isDirectory) {
                                input.text.toString()
                            } else {
                                input.text.toString() + "." + file.extension
                            }
                            val renamed = renameFileWithoutPath(file.absolutePath, target)
                            if (renamed) {
                                Toast.makeText(
                                    context.applicationContext,
                                    "RENAMED",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                reload()
                            } else {
                                Toast.makeText(
                                    context.applicationContext,
                                    "FAILED",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                            }
                        }
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show()
                }
                true
            }
            popupMenu.show()
            true
        }
    }

    override fun getItemCount(): Int {
        return filesAndFolders.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var textView: TextView
        var imageView: ImageView

        init {
            textView = itemView.findViewById<TextView>(R.id.file_name_text_view)
            imageView = itemView.findViewById<ImageView>(R.id.icon_view)
        }
    }
}