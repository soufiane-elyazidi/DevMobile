package fr.ensiie.todo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import fr.ensiie.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        supportActionBar?.hide()

        setContentView(binding.root)
    }
}