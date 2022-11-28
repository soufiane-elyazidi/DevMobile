package fr.ensiie.todo.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.RecyclerView
import fr.ensiie.todo.R
import fr.ensiie.todo.databinding.FragmentTaskListBinding
import fr.ensiie.todo.detail.DetailActivity
import java.util.*

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding

    private val adapter = TaskListAdapter()

    private var taskList = listOf(
        Task(id = "id_1", title = "Task 1", description = "Description 1"),
        Task(id = "id_2", title = "Task 2"),
        Task(id = "id_3", title = "Task 3")
    )

    private val createTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
            taskList = taskList + task
            adapter.submitList(taskList)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        adapter.submitList(taskList)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recycler.adapter = adapter

        binding.addBtn.setOnClickListener {
            createTask.launch(Intent(context, DetailActivity::class.java))
        }

        adapter.onClickDelete = { task ->
            taskList = taskList - task
            adapter.submitList(taskList)
        }
    }

}