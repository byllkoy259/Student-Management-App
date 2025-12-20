package vn.edu.hust.studentmanagement

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class StudentDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_detail)

        val edtMSSV = findViewById<EditText>(R.id.edtMSSV)
        val edtName = findViewById<EditText>(R.id.edtName)
        val edtPhone = findViewById<EditText>(R.id.edtPhone)
        val edtAddress = findViewById<EditText>(R.id.edtAddress)
        val btnUpdate = findViewById<Button>(R.id.btnUpdate)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        val student = intent.getSerializableExtra("STUDENT_DATA") as? Student
        val position = intent.getIntExtra("POSITION", -1)

        if (student != null) {
            edtMSSV.setText(student.mssv)
            edtName.setText(student.hoTen)
            edtPhone.setText(student.soDienThoai)
            edtAddress.setText(student.diaChi)
        }

        btnUpdate.setOnClickListener {
            val updatedStudent = Student(
                edtMSSV.text.toString(),
                edtName.text.toString(),
                edtPhone.text.toString(),
                edtAddress.text.toString()
            )

            val resultIntent = Intent()
            resultIntent.putExtra("UPDATED_STUDENT", updatedStudent)
            resultIntent.putExtra("POSITION", position)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        btnDelete.setOnClickListener {
            val resultIntent = Intent()
            resultIntent.putExtra("POSITION", position)
            resultIntent.putExtra("IS_DELETE", true)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}