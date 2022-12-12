package fr.ensiie.todo.detail

import android.content.Intent
import android.os.Bundle
import android.util.Log
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

        var task = intent.getSerializableExtra("task") as Task?
        if (task == null) task = Task(id = UUID.randomUUID().toString(),
            title = "Task Title",
            description = "Task Description")

        when (intent?.action) {
            Intent.ACTION_SEND -> {
                if ("text/plain" == intent.type) {
                    intent.getStringExtra(Intent.EXTRA_TEXT)?.let {
                        task = task!!.copy(description = it)
                    }
                }
            }
        }

        setContent {
            MyTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Detail(task!!) { newTask ->
                        intent.putExtra("task", newTask)
                        setResult(RESULT_OK, intent)
                        Log.d("TASK_DEBUG", (parent == null).toString())
                        finish()
                    }
                }
            }
        }
    }
}

@Composable
fun Detail(task: Task, onValidate: (Task) -> Unit) {
    var newTask by remember { mutableStateOf(task) }
    Column(
        modifier = Modifier.padding(all = Dp(16F)),
        verticalArrangement = Arrangement.spacedBy(Dp(16F))
    ) {
        Text(text = "Task Detail", style = MaterialTheme.typography.h2)
        OutlinedTextField(value = newTask.title, onValueChange = {
            newTask = newTask.copy(title = it)
        })
        OutlinedTextField(value = newTask.description, onValueChange = {
            newTask = newTask.copy(description = it)
        })
        Button(onClick = {
            onValidate(newTask)
        }, ) {
            Text(text = "Validate")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DetailPreview() {
    MyTheme {
        Detail(
            Task(id = UUID.randomUUID().toString(),
            title = "Task Title",
            description = "Task Description")
        )
        {}
    }
}