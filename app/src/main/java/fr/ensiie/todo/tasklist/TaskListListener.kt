package fr.ensiie.todo.tasklist

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
    fun onLongClick(task: Task)
}