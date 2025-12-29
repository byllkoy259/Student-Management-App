package vn.edu.hust.studentmanagement

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class StudentViewModel(application: Application) : AndroidViewModel(application) {
    private val dao: StudentDataAccessObject
    val studentList: LiveData<List<Student>>

    init {
        val database = StudentDB.getDatabase(application)
        dao = database.studentDao()
        studentList = dao.getAllStudents()
    }

    fun addStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.insertStudent(student)
        }
    }

    fun updateStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.updateStudent(student)
        }
    }

    fun deleteStudent(student: Student) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.deleteStudent(student)
        }
    }
}