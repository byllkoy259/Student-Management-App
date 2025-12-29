package vn.edu.hust.studentmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import vn.edu.hust.studentmanagement.databinding.FragmentStudentDetailBinding

class StudentDetailFragment : Fragment() {

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var binding: FragmentStudentDetailBinding
    private var position: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStudentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val student = arguments?.getSerializable("student") as? Student
        position = arguments?.getInt("position", -1) ?: -1

        if (student != null) {
            binding.edtMSSV.setText(student.mssv)
            binding.edtName.setText(student.hoTen)
            binding.edtPhone.setText(student.soDienThoai)
            binding.edtAddress.setText(student.diaChi)
        }

        binding.btnUpdate.setOnClickListener {
            val updatedStudent = Student(
                binding.edtMSSV.text.toString(),
                binding.edtName.text.toString(),
                binding.edtPhone.text.toString(),
                binding.edtAddress.text.toString()
            )
            viewModel.updateStudent(updatedStudent)
            findNavController().popBackStack()
        }

        binding.btnDelete.setOnClickListener {
            val currentStudent = arguments?.getSerializable("student") as? Student
            if (currentStudent != null) {
                viewModel.deleteStudent(currentStudent)
                findNavController().popBackStack()
            }
        }
    }
}