package fr.ensiie.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.ensiie.todo.R

class TaskListAdapter : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(ItemsDiffCallback) {

    object ItemsDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
            return areItemsTheSame(oldItem, newItem)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        return TaskViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    var onClickDelete: (Task) -> Unit = {}

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(task: Task) {
            itemView.findViewById<TextView>(R.id.task_title).text = task.title
            itemView.findViewById<TextView>(R.id.task_description).text = task.description
            itemView.findViewById<ImageButton>(R.id.delete_task).setOnClickListener { onClickDelete(task) }
        }

    }
}