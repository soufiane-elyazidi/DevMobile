package fr.ensiie.todo

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import fr.ensiie.todo.dao.Task
import fr.ensiie.todo.data.Api
import fr.ensiie.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private val login =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (Api.TOKEN != null) {
                binding = ActivityMainBinding.inflate(layoutInflater)
                setContentView(binding.root)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val token = sharedPref.getString("token", null)

        if (token == null) {
            login.launch(Intent(applicationContext, LoginActivity::class.java))
            return
        }
        else Api.TOKEN = token

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

}