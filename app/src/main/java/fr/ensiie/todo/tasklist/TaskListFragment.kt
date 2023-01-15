package fr.ensiie.todo.tasklist

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import coil.load
import fr.ensiie.todo.R
import fr.ensiie.todo.dao.Database
import fr.ensiie.todo.dao.Task
import fr.ensiie.todo.data.Api
import fr.ensiie.todo.data.User
import fr.ensiie.todo.databinding.FragmentTaskListBinding
import fr.ensiie.todo.detail.DetailActivity
import fr.ensiie.todo.tasklistclosed.ClosedTasks
import fr.ensiie.todo.user.UserActivity
import kotlinx.coroutines.launch
import java.util.concurrent.Executors
import java.util.concurrent.TimeUnit

class TaskListFragment : Fragment() {
    private lateinit var binding: FragmentTaskListBinding

    private val adapterListener : TaskListListener = object : TaskListListener {

        override fun onClickDelete(task: Task) {
            viewModel.delete(task)
            viewModel.refresh()
        }

        override fun onClickEdit(task: Task) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra("task", task)
            editTask.launch(intent)
        }

        override fun onClickClose(task: Task) {
            viewModel.close(task)
            Database.taskDao().insert(task)
            viewModel.refresh()
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

    private val adapter = TaskListAdapter(adapterListener)

    private val editTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
            viewModel.update(task)
        }

    private val createTask =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                result ->
            val task = result.data?.getSerializableExtra("task") as Task? ?: return@registerForActivityResult
            viewModel.create(task)
    }

    private val viewModel: TasksListViewModel by viewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTaskListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.recycler.adapter = adapter

        binding.addBtn.setOnClickListener {
            val intent = Intent(context, DetailActivity::class.java)
            createTask.launch(intent)
        }

        binding.doneTasks.setOnClickListener {
            val intent = Intent(context, ClosedTasks::class.java)
            createTask.launch(intent)
        }

        lifecycleScope.launch {
            viewModel.tasksStateFlow.collect { newList ->
                adapter.submitList(newList)
            }
        }

        val executor = Executors.newScheduledThreadPool(1)
        executor.scheduleAtFixedRate({
            onResume()
        }, 0, 2, TimeUnit.SECONDS)

    }

    override fun onResume() {
        super.onResume()

        lifecycleScope.launch {
            val user = fetchUser()

            binding.userTextView.text = user.name

            binding.avatar.load(user.avatar) {
                error(R.drawable.ic_launcher_background)
            }

            activity?.actionBar?.setIcon(binding.avatar.drawable)

            binding.avatar.setOnClickListener {
                val intent = Intent(context, UserActivity::class.java)
                intent.putExtra("user", user)
                createTask.launch(intent)   
            }
        }

        viewModel.refresh()
    }

    private suspend fun fetchUser() : User {
        return Api.userWebService.fetchUser().body()!!
    }

}