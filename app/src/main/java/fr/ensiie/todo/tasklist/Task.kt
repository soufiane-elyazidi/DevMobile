package fr.ensiie.todo.tasklist

data class Task (val id: String, val title: String, val description: String = "") : java.io.Serializable