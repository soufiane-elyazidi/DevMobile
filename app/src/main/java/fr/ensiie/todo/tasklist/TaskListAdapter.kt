package fr.ensiie.todo.tasklist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import fr.ensiie.todo.R

class TaskListAdapter(val listener: TaskListListener) : ListAdapter<Task, TaskListAdapter.TaskViewHolder>(ItemsDiffCallback) {

    object ItemsDiffCallback : DiffUtil.ItemCallback<Task>() {
        override fun areItemsTheSame(oldItem: Task, newItem: Task) : Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Task, newItem: Task) : Boolean {
            return areItemsTheSame(oldItem, newItem)
                    && oldItem.title == newItem.title
                    && oldItem.description == newItem.description
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

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(task: Task) {
            itemView.setOnLongClickListener {
                listener.onLongClick(task)
                true
            }
            itemView.findViewById<TextView>(R.id.task_title).text = task.title
            itemView.findViewById<TextView>(R.id.task_description).text = task.description
            itemView.findViewById<ImageButton>(R.id.delete_task).setOnClickListener { listener.onClickDelete(task) }
            itemView.findViewById<ImageButton>(R.id.edit_task).setOnClickListener { listener.onClickEdit(task) }
            itemView.findViewById<RadioButton>(R.id.check_task).setOnClickListener { listener.onClickClose(task) }
        }

    }
}