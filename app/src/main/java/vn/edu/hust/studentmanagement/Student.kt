package vn.edu.hust.studentmanagement

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "students")

data class Student(
    @PrimaryKey
    var mssv: String,
    var hoTen: String,
    var soDienThoai: String,
    var diaChi: String
) : Serializable