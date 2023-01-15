package fr.ensiie.todo

import android.app.Application
import fr.ensiie.todo.dao.Database

class TodoAPP : Application() {

    override fun onCreate() {
        super.onCreate()
        Database.init(this)
    }

}