package vn.edu.hust.studentmanagement

import java.io.Serializable

data class Student(
    var mssv: String,
    var hoTen: String,
    var soDienThoai: String,
    var diaChi: String
) : Serializable