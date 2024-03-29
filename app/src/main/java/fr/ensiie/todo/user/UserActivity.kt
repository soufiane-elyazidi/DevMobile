package fr.ensiie.todo.user

import android.annotation.SuppressLint
import android.content.ContentValues
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import coil.compose.AsyncImage
import fr.ensiie.todo.data.Api
import fr.ensiie.todo.data.User
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.*

class UserActivity : ComponentActivity() {

    private val capturedUri by lazy {
        contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, ContentValues())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val user = intent.getSerializableExtra("user") as User

        setContent {
            var newUser by remember { mutableStateOf(user) }

            //val bitmap: Bitmap? by remember { mutableStateOf(null) }
            var uri: Uri? by remember { mutableStateOf(null) }

            val coroutineScope = rememberCoroutineScope()

            /*val takePicture = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicturePreview()) {
                    bitmap = it
                    if (bitmap == null) return@rememberLauncherForActivityResult
                    coroutineScope.launch {
                        Api.userWebService.updateAvatar(bitmap!!.toRequestBody())
                    }
            }*/

            val takePicture = rememberLauncherForActivityResult(
                ActivityResultContracts.TakePicture()) { success ->
                if (success) uri = capturedUri
                coroutineScope.launch {
                    Api.userWebService.updateAvatar(uri!!.toRequestBody())
                }
            }

            val pickMedia = rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) {
                uri = it
                if (uri == null) return@rememberLauncherForActivityResult
                coroutineScope.launch {
                    Api.userWebService.updateAvatar(uri!!.toRequestBody())
                }
            }

            Column(
                modifier = Modifier.padding(all = Dp(16F)),
                verticalArrangement = Arrangement.spacedBy(Dp(16F)),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "User Details", style = MaterialTheme.typography.h3)
                AsyncImage(
                    modifier = Modifier.fillMaxHeight(.2f),
                    model = /*bitmap ?: */uri ?: user.avatar,
                    contentDescription = null
                )
                OutlinedTextField(value = newUser.name, onValueChange = {
                    newUser = newUser.copy(name = it)
                })
                OutlinedTextField(value = newUser.email, onValueChange = {
                    newUser = newUser.copy(email = it)
                })
                Button(
                    onClick = {
                        takePicture.launch(capturedUri)
                    },
                    content = { Text("Take picture") }
                )
                Button(
                    onClick = {
                        pickMedia.launch(
                            PickVisualMediaRequest(
                                ActivityResultContracts.PickVisualMedia.ImageOnly
                            )
                        )
                    },
                    content = { Text("Pick photo") }
                )
                Button(
                    onClick = {
                        coroutineScope.launch {
                            Api.userWebService.update(newUser)
                        }
                        finish()
                    },
                    content = { Text("Save") }
                )
            }
        }
    }

    private fun Bitmap.toRequestBody(): MultipartBody.Part {
        val tmpFile = File.createTempFile("avatar", "jpg")
        tmpFile.outputStream().use {
            this.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = tmpFile.readBytes().toRequestBody()
        )
    }

    @SuppressLint("Recycle")
    private fun Uri.toRequestBody(): MultipartBody.Part {
        val fileInputStream = contentResolver.openInputStream(this)!!
        val fileBody = compressImage(fileInputStream.readBytes()).toRequestBody()
        return MultipartBody.Part.createFormData(
            name = "avatar",
            filename = "avatar.jpg",
            body = fileBody
        )
    }

    private fun compressImage(imageByteArray: ByteArray): ByteArray {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size, options)
        options.inSampleSize = calculateInSampleSize(options, 1080, 1080)
        options.inJustDecodeBounds = false
        val bitmap = BitmapFactory.decodeByteArray(imageByteArray, 0, imageByteArray.size, options)

        val outputStream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, outputStream)
        return outputStream.toByteArray()
    }

    private fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
        val (height: Int, width: Int) = options.run { outHeight to outWidth }
        var inSampleSize = 1

        if (height > reqHeight || width > reqWidth) {

            val halfHeight: Int = height / 2
            val halfWidth: Int = width / 2

            while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }

        return inSampleSize
    }

}


