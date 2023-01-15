package fr.ensiie.todo.tasklistclosed

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import fr.ensiie.todo.dao.Database
import fr.ensiie.todo.databinding.ActivityClosedTasksBinding
import fr.ensiie.todo.dao.Task
import fr.ensiie.todo.tasklist.TasksListViewModel

class ClosedTasks : AppCompatActivity() {

    private lateinit var binding: ActivityClosedTasksBinding

    private val adapterListener : ClosedTaskListListener = object : ClosedTaskListListener {

        override fun onClickDelete(task: Task) {
            viewModel.delete(task)
            Database.taskDao().delete(task)
            adapter.submitList(Database.taskDao().getAll())
        }

        override fun onClickOpen(task: Task) {
            viewModel.reopen(task)
            Database.taskDao().delete(task)
            adapter.submitList(Database.taskDao().getAll())
        }

        override fun onLongClick(task: Task) {
            val shareIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, task.description)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, null))
        }
    }

    private val adapter = ClosedTasksAdapter(adapterListener)

    private val viewModel: TasksListViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClosedTasksBinding.inflate(layoutInflater)

        binding.recycler.adapter = adapter

        adapter.submitList(Database.taskDao().getAll())

        supportActionBar?.hide()
        setContentView(binding.root)
    }

}