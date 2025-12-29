package vn.edu.hust.studentmanagement

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface StudentDataAccessObject {
    @Query("SELECT * FROM students")
    fun getAllStudents(): LiveData<List<Student>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStudent(student: Student)

    @Update
    suspend fun updateStudent(student: Student)

    @Delete
    suspend fun deleteStudent(student: Student)

    @Query("DELETE FROM students WHERE mssv = :mssv")
    suspend fun deleteByMSSV(mssv: String)
}