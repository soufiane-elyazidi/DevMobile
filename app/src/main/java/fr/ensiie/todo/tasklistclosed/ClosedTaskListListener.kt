package fr.ensiie.todo.tasklistclosed

import fr.ensiie.todo.dao.Task

interface ClosedTaskListListener {
    fun onClickDelete(task: Task)
    fun onClickOpen(task: Task)
    fun onLongClick(task: Task)
}