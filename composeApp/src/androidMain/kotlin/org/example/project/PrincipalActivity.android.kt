package com.example.reviu_app_jpc

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.core.net.toUri
import coil3.Bitmap
import coil3.Image
import coil3.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.net.URI
import kotlin.coroutines.resume

@Composable
actual fun loadIcon(name: String): Painter {
    val context = LocalContext.current
    val resId = context.resources.getIdentifier(name, "drawable", context.packageName)
    return painterResource(id = resId)
}


lateinit var androidAppContext: Context

@Composable
actual fun pickImagePath(): ByteArray? {
    var fotoUri by remember { mutableStateOf<android.net.Uri?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri: android.net.Uri? ->
        fotoUri = uri
    }
    LaunchedEffect(Unit) {
        launcher.launch("image/*")
    }
    val contentResolver = androidAppContext.contentResolver
    val inputStream = fotoUri?.let { contentResolver.openInputStream(it) }
    return inputStream?.readBytes()
}


//class ImagePickerActivity : Activity() {
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        val pickIntent = Intent(Intent.ACTION_PICK).apply { type = "image/*" }
//        startActivityForResult(pickIntent, 1234)
//    }
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        val uri = data?.data?.toString()
//        val resultIntent = Intent().apply {
//            putExtra("imageUri", uri)
//        }
//        ImagePickerManager.notifyResult(resultCode, resultIntent)
//        finish()
//    }
//}
//interface ImagePickerListener {
//    fun onResult(resultCode: Int, data: Intent?)
//}
//object ImagePickerManager {
//    private var listener: ImagePickerListener? = null
//    fun register(listener: ImagePickerListener) {
//        this.listener = listener
//    }
//    fun unregister() {
//        listener = null
//    }
//    fun notifyResult(resultCode: Int, data: Intent?) {
//        listener?.onResult(resultCode, data)
//        unregister()
//    }
//}

//actual fun seleccionarImatge(onImageSelected: (ByteArray) -> Unit) {
//}