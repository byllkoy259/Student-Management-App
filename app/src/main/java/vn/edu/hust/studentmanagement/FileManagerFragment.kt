package vn.edu.hust.studentmanagement

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.view.*
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class FileManagerFragment : Fragment(R.layout.fragment_student_list) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter
    private var currentPath: File = Environment.getExternalStorageDirectory()
    private var selectedFile: File? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)

        adapter = FileAdapter(listOf(),
            onFileClick = { file ->
                if (file.isDirectory) {
                    currentPath = file
                    loadFiles(file)
                } else {
                    openFile(file)
                }
            },
            onFileLongClick = { file, view ->
                selectedFile = file
                view.showContextMenu()
                true
            }
        )
        recyclerView.adapter = adapter

        registerForContextMenu(recyclerView)

        checkPermissionAndLoad()
    }

    private fun checkPermissionAndLoad() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (Environment.isExternalStorageManager()) {
                loadFiles(currentPath)
            } else {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.data = Uri.parse("package:${requireContext().packageName}")
                startActivity(intent)
            }
        } else {
            loadFiles(currentPath)
        }
    }

    private fun loadFiles(directory: File) {
        try {
            val files = directory.listFiles()
            if (files != null) {
                val sortedList = files.sortedWith(compareBy({ !it.isDirectory }, { it.name.lowercase() }))
                adapter.updateData(sortedList)
                activity?.title = directory.name
            }
        } catch (e: Exception) {
            Toast.makeText(context, "Không thể đọc thư mục", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_file_option, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_create_folder -> {
                showCreateDialog(isFolder = true)
                true
            }
            R.id.menu_create_file -> {
                showCreateDialog(isFolder = false)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onCreateContextMenu(menu: ContextMenu, v: View, menuInfo: ContextMenu.ContextMenuInfo?) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.add(0, 1, 0, "Đổi tên")
        menu.add(0, 2, 0, "Xóa")
        if (selectedFile?.isFile == true) {
            menu.add(0, 3, 0, "Sao chép")
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val file = selectedFile ?: return false
        when (item.itemId) {
            1 -> showRenameDialog(file)
            2 -> showDeleteDialog(file)
            3 -> showCopyDialog(file)
        }
        return true
    }

    private fun openFile(file: File) {
        val extension = file.extension.lowercase()
        if (extension == "txt") {
            val text = FileInputStream(file).bufferedReader().use { it.readText() }
            AlertDialog.Builder(context)
                .setTitle(file.name)
                .setMessage(text)
                .setPositiveButton("Đóng", null)
                .show()
        } else if (extension in listOf("png", "jpg", "jpeg", "bmp")) {
            Toast.makeText(context, "Chức năng xem ảnh đang cập nhật", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Không hỗ trợ định dạng này", Toast.LENGTH_SHORT).show()
        }
    }

    private fun showCreateDialog(isFolder: Boolean) {
        val editText = EditText(context)
        AlertDialog.Builder(context)
            .setTitle(if (isFolder) "Tạo thư mục mới" else "Tạo file văn bản mới")
            .setView(editText)
            .setPositiveButton("Tạo") { _, _ ->
                val name = editText.text.toString()
                if (name.isNotEmpty()) {
                    val newFile = File(currentPath, name + if(isFolder) "" else ".txt")
                    if (isFolder) newFile.mkdir() else newFile.createNewFile()
                    loadFiles(currentPath) // Load lại danh sách
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showRenameDialog(file: File) {
        val editText = EditText(context)
        editText.setText(file.name)
        AlertDialog.Builder(context)
            .setTitle("Đổi tên")
            .setView(editText)
            .setPositiveButton("Lưu") { _, _ ->
                val newName = editText.text.toString()
                val newFile = File(file.parent, newName)
                if (file.renameTo(newFile)) {
                    loadFiles(currentPath)
                    Toast.makeText(context, "Đổi tên thành công", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Đổi tên thất bại", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showDeleteDialog(file: File) {
        AlertDialog.Builder(context)
            .setTitle("Xóa")
            .setMessage("Bạn có chắc muốn xóa ${file.name}?")
            .setPositiveButton("Xóa") { _, _ ->
                val result = if (file.isDirectory) file.deleteRecursively() else file.delete()
                if (result) {
                    loadFiles(currentPath)
                    Toast.makeText(context, "Đã xóa", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Xóa thất bại", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showCopyDialog(file: File) {
        val newFile = File(currentPath, "Copy_${file.name}")
        try {
            file.inputStream().use { input ->
                newFile.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            loadFiles(currentPath)
            Toast.makeText(context, "Đã sao chép", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Lỗi sao chép", Toast.LENGTH_SHORT).show()
        }
    }
    fun goBack(): Boolean {
        if (currentPath.absolutePath == Environment.getExternalStorageDirectory().absolutePath) {
            return false
        }
        currentPath = currentPath.parentFile ?: return false
        loadFiles(currentPath)
        return true
    }
}