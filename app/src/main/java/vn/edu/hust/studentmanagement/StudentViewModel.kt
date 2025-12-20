package vn.edu.hust.studentmanagement

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class StudentViewModel : ViewModel() {
    private val _studentList = MutableLiveData<MutableList<Student>>()

    val studentList: LiveData<MutableList<Student>> get() = _studentList

    init {
        _studentList.value = mutableListOf(
            Student("2020001", "Nguyễn Văn A", "0987654321", "Hà Nội"),
            Student("2020002", "Trần Thị B", "0912345678", "Đà Nẵng")
        )
    }

    fun addStudent(student: Student) {
        val currentList = _studentList.value ?: mutableListOf()
        currentList.add(student)
        _studentList.value = currentList
    }

    fun updateStudent(position: Int, newStudent: Student) {
        val currentList = _studentList.value ?: return
        if (position in currentList.indices) {
            currentList[position] = newStudent
            _studentList.value = currentList
        }
    }

    fun deleteStudent(position: Int) {
        val currentList = _studentList.value ?: return
        if (position in currentList.indices) {
            currentList.removeAt(position)
            _studentList.value = currentList
        }
    }
}