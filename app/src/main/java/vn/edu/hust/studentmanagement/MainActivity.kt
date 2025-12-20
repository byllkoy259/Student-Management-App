package vn.edu.hust.studentmanagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var studentAdapter: StudentAdapter
    private val studentList = mutableListOf<Student>()

    private val addStudentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val newStudent = data?.getSerializableExtra("NEW_STUDENT") as? Student
            if (newStudent != null) {
                studentList.add(newStudent)
                studentAdapter.notifyItemInserted(studentList.size - 1)
            }
        }
    }

    private val editStudentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data
            val position = data?.getIntExtra("POSITION", -1) ?: -1
            val isDelete = data?.getBooleanExtra("IS_DELETE", false) ?: false

            if (position != -1) {
                if (isDelete) {
                    studentList.removeAt(position)
                    studentAdapter.notifyItemRemoved(position)
                    studentAdapter.notifyItemRangeChanged(position, studentList.size)
                } else {
                    val updatedStudent = data?.getSerializableExtra("UPDATED_STUDENT") as? Student
                    if (updatedStudent != null) {
                        studentList[position] = updatedStudent
                        studentAdapter.notifyItemChanged(position)
                    }
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        studentList.add(Student("2020001", "Nguyễn Văn A", "0987654321", "Hà Nội"))

        studentAdapter = StudentAdapter(studentList) { position ->
            val intent = Intent(this, StudentDetailActivity::class.java)
            intent.putExtra("STUDENT_DATA", studentList[position])
            intent.putExtra("POSITION", position)
            editStudentLauncher.launch(intent)
        }

        recyclerView.adapter = studentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_add -> {
                val intent = Intent(this, AddStudentActivity::class.java)
                addStudentLauncher.launch(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}