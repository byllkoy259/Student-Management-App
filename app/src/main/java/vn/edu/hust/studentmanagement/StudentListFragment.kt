package vn.edu.hust.studentmanagement

import android.os.Bundle
import android.view.View
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class StudentListFragment : Fragment(R.layout.fragment_student_list) {

    private val viewModel: StudentViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)
        val adapter = StudentAdapter(mutableListOf()) { position ->
            val student = viewModel.studentList.value?.get(position)
            if (student != null) {
                val bundle = bundleOf("student" to student, "position" to position)
                findNavController().navigate(R.id.action_list_to_detail, bundle)
            }
        }

        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Quan sát dữ liệu từ ViewModel
        viewModel.studentList.observe(viewLifecycleOwner) { students ->
            (recyclerView.adapter as? StudentAdapter)?.updateData(students)
        }
    }
}