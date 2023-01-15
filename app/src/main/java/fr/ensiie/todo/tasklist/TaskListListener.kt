package fr.ensiie.todo.tasklist

import fr.ensiie.todo.dao.Task

interface TaskListListener {
    fun onClickDelete(task: Task)
    fun onClickEdit(task: Task)
    fun onClickClose(task: Task)
    fun onLongClick(task: Task)
}