package vn.edu.hust.studentmanagement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import vn.edu.hust.studentmanagement.databinding.FragmentAddStudentBinding

class AddStudentFragment : Fragment() {

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var binding: FragmentAddStudentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddStudentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSave.setOnClickListener {
            val mssv = binding.edtMSSV.text.toString()
            val name = binding.edtName.text.toString()
            val phone = binding.edtPhone.text.toString()
            val address = binding.edtAddress.text.toString()

            if (mssv.isNotEmpty() && name.isNotEmpty()) {
                val newStudent = Student(mssv, name, phone, address)
                viewModel.addStudent(newStudent)
                findNavController().popBackStack()
            } else {
                Toast.makeText(context, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show()
            }
        }
    }
}