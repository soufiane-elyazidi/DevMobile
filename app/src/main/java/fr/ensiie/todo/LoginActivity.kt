package fr.ensiie.todo

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.lifecycleScope
import fr.ensiie.todo.data.Api
import fr.ensiie.todo.data.User
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Column(
                        modifier = Modifier.padding(all = Dp(16F)),
                        verticalArrangement = Arrangement.spacedBy(Dp(16F)),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        val email = remember { mutableStateOf("") }
                        val password = remember { mutableStateOf("") }
                        val fullName = remember { mutableStateOf("") }

                        Text(text = "Register", style = MaterialTheme.typography.h4,
                        modifier = Modifier.padding(15.dp))
                        Text("Full Name")
                        OutlinedTextField(value = fullName.value,
                            onValueChange = { fullName.value = it })

                        Text("Email")
                        OutlinedTextField(value = email.value,
                            onValueChange = { email.value = it })

                        Text("Password")
                        OutlinedTextField(value = password.value,
                            onValueChange = { password.value = it },
                            visualTransformation = PasswordVisualTransformation())

                        Button(onClick = {
                            var user = User(
                                name = fullName.value,
                                email = email.value,
                                password = password.value
                            )
                            lifecycleScope.launch {
                                try {
                                    user = Api.userWebService.register(user).body()!!
                                    if (user.token != null) {
                                        Api.TOKEN = user.token!!
                                        val sharedPref = getSharedPreferences("auth", Context.MODE_PRIVATE)
                                        val editor = sharedPref.edit()
                                        editor.putString("token", user.token)
                                        editor.apply()
                                        finish()
                                    } else {
                                        Toast.makeText(applicationContext, "Couldn't register", Toast.LENGTH_LONG).show()
                                    }
                                } catch (_: java.lang.Exception) {
                                    Toast.makeText(applicationContext, "User exists or weak password", Toast.LENGTH_LONG).show()
                                }
                            }
                        }) {
                            Text("Login")
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {

    }
}
