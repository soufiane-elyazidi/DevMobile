package fr.ensiie.todo.detail

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import fr.ensiie.todo.detail.ui.theme.MyTheme
import fr.ensiie.todo.tasklist.Task
import java.util.UUID

class DetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Detail { task ->
                        intent.putExtra("task", task)
                        setResult(RESULT_OK, intent)
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun Detail(onValidate: (Task) -> Unit) {
    var task by remember { mutableStateOf(Task(id = UUID.randomUUID().toString(),
        title = "Task Title",
        description = "Task Description"))
    }
    Column(
        modifier = Modifier.padding(all = Dp(16F)),
        verticalArrangement = Arrangement.spacedBy(Dp(16F))
    ) {
        Text(text = "Task Detail", style = MaterialTheme.typography.h2)
        OutlinedTextField(value = task.title, onValueChange = {
            task = task.copy(title = it)
        })
        OutlinedTextField(value = task.description, onValueChange = {
            task = task.copy(description = it)
        })
        Button(onClick = {
            onValidate(task)
        }, ) {
            Text(text = "Validate")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    MyTheme {
        Detail{}
    }
}