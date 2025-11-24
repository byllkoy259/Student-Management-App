package vn.edu.hust.studentmanagement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(
    private val studentList: MutableList<Student>,
    private val onEditClick: (Int) -> Unit,  // Callback khi nhấn vào item để sửa
    private val onDeleteClick: (Int) -> Unit // Callback khi nhấn nút xóa
) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    inner class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)
        val tvMSSV: TextView = itemView.findViewById(R.id.tvMSSV)
        val btnDelete: ImageView = itemView.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_student, parent, false)
        return StudentViewHolder(view)
    }

    override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
        val student = studentList[position]
        holder.tvName.text = student.hoTen
        holder.tvMSSV.text = student.mssv

        // Xử lý sự kiện xóa
        holder.btnDelete.setOnClickListener {
            onDeleteClick(position)
        }

        // Xử lý sự kiện click vào item (để đưa dữ liệu lên edit text)
        holder.itemView.setOnClickListener {
            onEditClick(position)
        }
    }

    override fun getItemCount(): Int = studentList.size
}