package vn.edu.hust.studentmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class FileAdapter(
    private var files: List<File>,
    private val onFileClick: (File) -> Unit,
    private val onFileLongClick: (File, View) -> Boolean
) : RecyclerView.Adapter<FileAdapter.FileViewHolder>() {

    inner class FileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgIcon: ImageView = itemView.findViewById(R.id.imgIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvDate: TextView = itemView.findViewById(R.id.tvDate)

        init {
            itemView.setOnClickListener { onFileClick(files[adapterPosition]) }
            itemView.setOnLongClickListener {
                onFileLongClick(files[adapterPosition], it)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FileViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_file, parent, false)
        return FileViewHolder(view)
    }

    override fun onBindViewHolder(holder: FileViewHolder, position: Int) {
        val file = files[position]
        holder.tvName.text = file.name

        val lastModified = Date(file.lastModified())
        val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        holder.tvDate.text = formatter.format(lastModified)

        if (file.isDirectory) {
            holder.imgIcon.setImageResource(android.R.drawable.ic_menu_view)
        } else {
            if (file.extension.lowercase() in listOf("png", "jpg", "jpeg", "bmp")) {
                holder.imgIcon.setImageResource(android.R.drawable.ic_menu_gallery)
            } else {
                holder.imgIcon.setImageResource(android.R.drawable.ic_menu_agenda)
            }
        }
    }

    override fun getItemCount() = files.size

    fun updateData(newFiles: List<File>) {
        files = newFiles
        notifyDataSetChanged()
    }
}