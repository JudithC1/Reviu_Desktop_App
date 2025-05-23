package com.example.reviu_app_jpc

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import coil3.Uri
import io.ktor.http.ContentDisposition.Companion.File
import java.io.File
import javax.swing.JFileChooser

@Composable
actual fun loadIcon(name: String): Painter {
    return painterResource("$name.png")
}

@Composable
actual fun pickImagePath(): ByteArray? {
    val chooser = JFileChooser()
    var path : String? = null
    print(chooser.showOpenDialog(null))
    try {
        path = chooser.selectedFile.absolutePath
    } catch (e : Exception) {}

    if (path == null) {
        return null
    }
    else {
        return File(path).readBytes()
//        throw Exception("Imtage no seleccionada")
    }
}


//actual fun seleccionarImatge(onImageSelected: (ByteArray) -> Unit) {
//    val chooser = JFileChooser()
//    val resultat = chooser.showOpenDialog(null)
//    if (resultat == JFileChooser.APPROVE_OPTION) {
//        val selectedFile: File = chooser.selectedFile
//        onImageSelected(selectedFile.readBytes())
//    }
//}