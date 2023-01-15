package fr.ensiie.todo.dao

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@kotlinx.serialization.Serializable
@Entity(tableName = "tasks")
data class Task (
    @SerialName("id")
    @PrimaryKey val id: String,
    @SerialName("content")
    val title: String,
    @SerialName("description")
    val description: String = ""
) : java.io.Serializable