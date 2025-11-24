package vn.edu.hust.studentmanagement

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var edtName: EditText
    private lateinit var edtMSSV: EditText
    private lateinit var btnAdd: Button
    private lateinit var btnUpdate: Button
    private lateinit var recyclerView: RecyclerView

    private lateinit var studentAdapter: StudentAdapter
    private val studentList = mutableListOf<Student>()

    private var currentEditingIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Ánh xạ View
        edtName = findViewById(R.id.edtName)
        edtMSSV = findViewById(R.id.edtMSSV)
        btnAdd = findViewById(R.id.btnAdd)
        btnUpdate = findViewById(R.id.btnUpdate)
        recyclerView = findViewById(R.id.recyclerView)

        // Khởi tạo dữ liệu mẫu
        studentList.add(Student("Nguyễn Văn An", "20200001"))
        studentList.add(Student("Trần Thị Bình", "20200002"))
        studentList.add(Student("Lê Hoàng Phúc", "20200003"))
        studentList.add(Student("Phạm Thu Hà", "20200004"))
        studentList.add(Student("Võ Minh Khang", "20200005"))
        studentList.add(Student("Đặng Thúy Vy", "20200006"))
        studentList.add(Student("Bùi Anh Tuấn", "20200007"))
        studentList.add(Student("Hoàng Gia Huy", "20200008"))
        studentList.add(Student("Đỗ Khánh Ngọc", "20200009"))
        studentList.add(Student("Ngô Lan Chi", "20200010"))


        // Cấu hình RecyclerView và Adapter
        studentAdapter = StudentAdapter(
            studentList,
            onEditClick = { position -> showStudentDataToEdit(position) },
            onDeleteClick = { position -> deleteStudent(position) }
        )

        recyclerView.adapter = studentAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Xử lý sự kiện nút ADD
        btnAdd.setOnClickListener {
            addStudent()
        }

        // Xử lý sự kiện nút UPDATE
        btnUpdate.setOnClickListener {
            updateStudent()
        }
    }

    // Hàm thêm sinh viên
    private fun addStudent() {
        val name = edtName.text.toString()
        val mssv = edtMSSV.text.toString()

        if (name.isNotEmpty() && mssv.isNotEmpty()) {
            val newStudent = Student(name, mssv)
            studentList.add(newStudent)
            studentAdapter.notifyItemInserted(studentList.size - 1)

            // Clear ô nhập liệu
            edtName.text.clear()
            edtMSSV.text.clear()
        } else {
            Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
        }
    }

    // Hàm hiển thị dữ liệu lên EditText khi click vào item
    private fun showStudentDataToEdit(position: Int) {
        val student = studentList[position]
        edtName.setText(student.hoTen)
        edtMSSV.setText(student.mssv)

        // Lưu lại vị trí đang sửa
        currentEditingIndex = position
    }

    // Hàm cập nhật sinh viên
    private fun updateStudent() {
        if (currentEditingIndex != -1) { // Đảm bảo đang có item được chọn
            val newName = edtName.text.toString()
            val newMSSV = edtMSSV.text.toString() // Cho phép sửa cả MSSV nếu muốn

            if (newName.isNotEmpty() && newMSSV.isNotEmpty()) {
                // Cập nhật dữ liệu trong list
                studentList[currentEditingIndex].hoTen = newName
                studentList[currentEditingIndex].mssv = newMSSV

                // Thông báo cho Adapter cập nhật giao diện tại vị trí đó
                studentAdapter.notifyItemChanged(currentEditingIndex)

                // Reset lại trạng thái
                currentEditingIndex = -1
                edtName.text.clear()
                edtMSSV.text.clear()
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Vui lòng chọn sinh viên để sửa", Toast.LENGTH_SHORT).show()
        }
    }

    // Hàm xóa sinh viên
    private fun deleteStudent(position: Int) {
        studentList.removeAt(position)
        studentAdapter.notifyItemRemoved(position)
        // Cập nhật lại range để tránh lỗi index nếu xóa liên tục
        studentAdapter.notifyItemRangeChanged(position, studentList.size)
    }
}