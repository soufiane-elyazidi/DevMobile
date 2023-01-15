package fr.ensiie.todo.dao

import android.content.Context
import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM tasks")
    fun getAll(): List<Task>

    @Insert
    fun insert(task: Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)

    @Query("SELECT COUNT(*) FROM tasks WHERE id = :id")
    fun getCount(id: Int): Int
}

@androidx.room.Database(entities = [Task::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
}

object Database {
    lateinit var db: AppDatabase

    fun init(context: Context) {
        db = Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "tasks.db"
        ).allowMainThreadQueries().build()
    }
    fun taskDao() = db.taskDao()
}
